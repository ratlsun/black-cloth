package hale.bc.server.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleRequest {
	private RuleRequestHeader header;
	private RuleRequestBody body;
	
	public RuleRequest() {
		super();
	}

	static public RuleRequest buildRequest(RuleRequestHeader header, RuleRequestBody body){
		RuleRequest r = new RuleRequest();
		r.header = header;
		r.body = body;
		return r;
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
