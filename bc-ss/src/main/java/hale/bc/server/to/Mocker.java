package hale.bc.server.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mocker {
	private Long id;
	private String owner;
	private String name;
	private String desc;
	private MockerType type = MockerType.Public;
	
	public Mocker() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public MockerType getType() {
		return type;
	}

	public void setType(MockerType type) {
		this.type = type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
