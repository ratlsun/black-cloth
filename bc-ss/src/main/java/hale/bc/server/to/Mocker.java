package hale.bc.server.to;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mocker {
	private Long id;
	private String owner;
	private String name;
	private String desc;
	private Date created;
	private Date updated;
	private Long ruleCount = 0l;
	private MockerType type = MockerType.Public;
	private Long watcherCount = 0l;
	
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

	public Long getWatcherCount() {
		return watcherCount;
	}

	public void setWatcherCount(Long watcherCount) {
		this.watcherCount = watcherCount;
	}
	
	public void increaseWatcherCount(){
		watcherCount++;
	}
	
	public void decreaseWatcherCount(){
		watcherCount--;
	}

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

	public Long getRuleCount() {
		return ruleCount;
	}

	public void setRuleCount(Long ruleCount) {
		this.ruleCount = ruleCount;
	}
	
	public void increaseRuleCount(){
		ruleCount++;
	}

	public void decreaseRuleCount(){
		ruleCount--;
	}
	
}
