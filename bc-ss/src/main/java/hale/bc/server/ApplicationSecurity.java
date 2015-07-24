package hale.bc.server;

import hale.bc.server.service.UserService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers()
				.disable()
				.csrf()
				.disable()
				.authorizeRequests()
				.antMatchers("/css/**", "/fonts/**", "/js/**", "/index.html",
						"/users/active", "/*/reset-pwd/**",
						"/users/*/resetPwd", "/users/*/forgetPwd",
						"/users/pwdCode", "/users", "/*.mock/**", "/touch", "/")
				.permitAll().anyRequest().fullyAuthenticated().and()
				.formLogin()
				.successHandler(new RestAuthenticationSuccessHandler())
				.failureHandler(new SimpleUrlAuthenticationFailureHandler())
				.and().logout()
				.logoutSuccessHandler(new RestLogoutSuccessHandler()).and()
				.exceptionHandling()
				.authenticationEntryPoint(new RestAuthenticationEntryPoint());
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	public static class RestAuthenticationSuccessHandler extends
			SimpleUrlAuthenticationSuccessHandler {

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request,
				HttpServletResponse response, Authentication authentication)
				throws ServletException, IOException {

			clearAuthenticationAttributes(request);
		}
	}

	public static class RestLogoutSuccessHandler extends
			SimpleUrlLogoutSuccessHandler {

		@Override
		public void onLogoutSuccess(HttpServletRequest request,
				HttpServletResponse response, Authentication authentication)
				throws IOException, ServletException {
			// Do nothing!
		}
	}

	public static class RestAuthenticationEntryPoint implements
			AuthenticationEntryPoint {

		@Override
		public void commence(HttpServletRequest request,
				HttpServletResponse response,
				AuthenticationException authException) throws IOException {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Authentication Failed: " + authException.getMessage());
		}
	}

}
