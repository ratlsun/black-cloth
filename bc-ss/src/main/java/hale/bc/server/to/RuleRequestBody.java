package hale.bc.server.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleRequestBody {
	private String content;
	private RuleRequestType type;
	
	public RuleRequestBody() {
		super();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public RuleRequestType getType() {
		return type;
	}

	public void setType(RuleRequestType type) {
		this.type = type;
	}

	
	
}
