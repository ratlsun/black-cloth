package hale.bc.server.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleRequest {
	private RuleRequestHeader header;
	private RuleRequestBody body;
	
	public RuleRequest() {
		super();
	}

	public RuleRequestHeader getHeader() {
		return header;
	}

	public void setHeader(RuleRequestHeader header) {
		this.header = header;
	}

	public RuleRequestBody getBody() {
		return body;
	}

	public void setBody(RuleRequestBody body) {
		this.body = body;
	}

	
	
}
