package com.trigon.ctmw.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application implements EmbeddedServletContainerCustomizer {

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
		mappings.add("html", "text/html;charset=utf-8");
		mappings.add("js", "application/javascript;charset=utf-8");
		container.setMimeMappings(mappings);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public static class MySavedRequestAwareAuthenticationSuccessHandler extends
			SimpleUrlAuthenticationSuccessHandler {

		private RequestCache requestCache = new HttpSessionRequestCache();

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request,
				HttpServletResponse response, Authentication authentication)
				throws ServletException, IOException {
			SavedRequest savedRequest = requestCache.getRequest(request,
					response);

			if (savedRequest == null) {
				clearAuthenticationAttributes(request);
				return;
			}
			String targetUrlParam = getTargetUrlParameter();
			if (isAlwaysUseDefaultTargetUrl()
					|| (targetUrlParam != null && StringUtils.hasText(request
							.getParameter(targetUrlParam)))) {
				requestCache.removeRequest(request, response);
				clearAuthenticationAttributes(request);
				return;
			}

			clearAuthenticationAttributes(request);
		}

		public void setRequestCache(RequestCache requestCache) {
			this.requestCache = requestCache;
		}
	}

	public static class RestAuthenticationEntryPoint implements
			AuthenticationEntryPoint {

		@Override
		public void commence(HttpServletRequest request,
				HttpServletResponse response,
				AuthenticationException authException) throws IOException {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Unauthorized");
		}
	}

	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class ApplicationSecurity extends
			WebSecurityConfigurerAdapter {

		@Autowired
		private SecurityProperties security;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf()
					.disable()
					.authorizeRequests()
					.antMatchers("/css/**", "/fonts/**", "/js/**",
							"/index.html", "/")
					.permitAll()
					.anyRequest()
					.fullyAuthenticated()
					.and()
					.formLogin()
					.successHandler(
							new MySavedRequestAwareAuthenticationSuccessHandler())
					.failureHandler(new SimpleUrlAuthenticationFailureHandler())
					.and().exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint());
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth)
				throws Exception {
			auth.inMemoryAuthentication().withUser("admin").password("admin")
					.roles("ADMIN", "USER").and().withUser("user@user")
					.password("user").roles("USER");
		}

	}

}
