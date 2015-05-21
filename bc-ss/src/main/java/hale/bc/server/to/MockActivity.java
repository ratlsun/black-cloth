package hale.bc.server.to;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MockActivity {
	private String owner;
	private String code;
	private Date created;
	private Date updated;
	private List<Long> mockerIds;
	private MockActivityStatus status;
	
	public MockActivity() {
		super();
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Long> getMockerIds() {
		return mockerIds;
	}

	public void setMockerIds(List<Long> mockerIds) {
		this.mockerIds = mockerIds;
	}

	public MockActivityStatus getStatus() {
		return status;
	}

	public void setStatus(MockActivityStatus status) {
		this.status = status;
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
