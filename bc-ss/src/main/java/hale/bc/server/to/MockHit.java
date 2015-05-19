package hale.bc.server.to;

import java.util.Date;

public class MockHit {
	private boolean match;
	private Date created;
	private RuleRequest request;
	private RuleResponse response;
	
	public boolean isMatch() {
		return match;
	}
	public void setMatch(boolean match) {
		this.match = match;
	}
	public RuleRequest getRequest() {
		return request;
	}
	public void setRequest(RuleRequest request) {
		this.request = request;
	}
	public RuleResponse getResponse() {
		return response;
	}
	public void setResponse(RuleResponse response) {
		this.response = response;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
}
