package hale.bc.server.to;

public class CodeSender {
	private String autoSendCode;
	private String serverHost;
	private String serverPort;
	private String username;
	private String password;
	public String getAutoSendCode() {
		return autoSendCode;
	}
	public void setAutoSendCode(String autoSendCode) {
		this.autoSendCode = autoSendCode;
	}
	public String getServerHost() {
		return serverHost;
	}
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
