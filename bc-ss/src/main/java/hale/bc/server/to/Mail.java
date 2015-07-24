package hale.bc.server.to;

public class Mail {
	private String autoSendCode;
	private String from;
	private String to;
	private String subject;
	private String content;
	private String templateName;
	public String getAutoSendCode() {
		return autoSendCode;
	}
	public void setAutoSendCode(String autoSendCode) {
		this.autoSendCode = autoSendCode;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
}
