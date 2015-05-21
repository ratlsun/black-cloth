package hale.bc.server.to;

import java.util.Date;

public class MockHit {
	private String mockActivityCode;
	private boolean match;
	private Date created;
	private RuleRequest request;
	private RuleResponse response;

	public MockHit() {
		super();
	}
	
	static public MockHit buildUnmatchedHit(String mockActivityCode, RuleRequest request) {
		MockHit hit = new MockHit();
		hit.mockActivityCode = mockActivityCode;
		hit.request = request;
		hit.match = false;
		return hit;
	}

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

	public String getMockActivityCode() {
		return mockActivityCode;
	}

	public void setMockActivityCode(String mockActivityCode) {
		this.mockActivityCode = mockActivityCode;
	}

}
