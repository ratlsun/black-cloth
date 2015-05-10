package hale.bc.server.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleResponse {
	private RuleResponseHeader header;
	private RuleResponseBody body;
	
	public RuleResponse() {
		super();
	}

	public RuleResponseHeader getHeader() {
		return header;
	}

	public void setHeader(RuleResponseHeader header) {
		this.header = header;
	}

	public RuleResponseBody getBody() {
		return body;
	}

	public void setBody(RuleResponseBody body) {
		this.body = body;
	}

	
	
}
