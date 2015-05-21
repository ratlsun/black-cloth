package hale.bc.server.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleRequestHeader {
	private String url;
	private String contentType;
	private String method;
	
	public RuleRequestHeader() {
		super();
	}
	
	static public RuleRequestHeader buildHeader(String url, String contentType, String method){
		RuleRequestHeader header = new RuleRequestHeader();
		header.setContentType(contentType);
		header.setMethod(method);
		header.setUrl(url);
		return header;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	
	
}
