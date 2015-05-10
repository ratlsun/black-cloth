package hale.bc.server.to;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule {
	private Long id;
	private Long mockerId;
	private Date created;
	private Date updated;
	private RuleRequest request;
	private RuleResponse response;
	
	public Rule() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMockerId() {
		return mockerId;
	}

	public void setMockerId(Long mockerId) {
		this.mockerId = mockerId;
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

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
}
