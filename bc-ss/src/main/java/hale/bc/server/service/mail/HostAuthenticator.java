package hale.bc.server.service.mail;

import javax.mail.*;

public class HostAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public HostAuthenticator() {
	}

	public HostAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}