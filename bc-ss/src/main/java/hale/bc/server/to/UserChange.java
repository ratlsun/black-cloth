package hale.bc.server.to;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserChange {
	private String userName;
	private Mocker mocker;
	private Integer ruleAddedCount = 0;
	private Integer ruleModifiedCount = 0;
	private Integer ruleDeletedCount = 0;
	private Integer ruleMatchedCount = 0;
	private Integer ruleMismatchCount = 0;
	private Long mockActivityId;
	private String name;
	private Date startChanged;
	private Date endChanged;
	private UserChangeType type;
	
	public UserChange() {
		super();
	}
	
	static public UserChange mockerChange(String userName, Mocker mocker, UserOperation mockerOperation,
			UserChangeType type) {
		UserChange uc = new UserChange();
		uc.setUserName(userName);
		uc.setMocker(mocker);
		uc.setStartChanged(mockerOperation.getLogged());
		uc.setEndChanged(mockerOperation.getLogged());
		uc.setType(type);
		switch(type) {
			case CreateMocker:
			case ChangeMockerOwner:
			case ChangeMockerType:
			case ChangeMockerName:
			case DeleteMocker:
				uc.setName(mockerOperation.getName());
				break;
			case ChangeMockerRules:
				uc.setName("修改了模拟系统－" + mocker.getName() + " 的规则");
				switch(mockerOperation.getType()) {
					case CreateRule:
						uc.setRuleAddedCount(1);
						break;
					case UpdateRule:
						uc.setRuleModifiedCount(1);
						break;
					case DeleteRule:
						uc.setRuleDeletedCount(1);
						break;
					default:
				}		
				break;
			default:
		}
		return uc;
	}
	
	static public UserChange activityChange(String userName, Long mockActivityId) {
		UserChange uc = new UserChange();
		uc.setUserName(userName);
		uc.setMockActivityId(mockActivityId);
		uc.setName("进行了一次模拟");
		uc.setType(UserChangeType.MockActivity);
		return uc;
	}

	public void merge(UserOperation op) {
		setStartChanged(op.getLogged());
		switch(op.getType()) {
			case CreateRule:
				ruleAddedCount++;
				break;
			case UpdateRule:
				ruleModifiedCount++;
				break;
			case DeleteRule:
				ruleDeletedCount++;
				break;
			default:
		}		
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Mocker getMocker() {
		return mocker;
	}

	public void setMocker(Mocker mocker) {
		this.mocker = mocker;
	}

	public Integer getRuleAddedCount() {
		return ruleAddedCount;
	}

	public void setRuleAddedCount(Integer ruleAddedCount) {
		this.ruleAddedCount = ruleAddedCount;
	}

	public Integer getRuleModifiedCount() {
		return ruleModifiedCount;
	}

	public void setRuleModifiedCount(Integer ruleModifiedCount) {
		this.ruleModifiedCount = ruleModifiedCount;
	}

	public Integer getRuleDeletedCount() {
		return ruleDeletedCount;
	}

	public void setRuleDeletedCount(Integer ruleDeletedCount) {
		this.ruleDeletedCount = ruleDeletedCount;
	}

	public Long getMockActivityId() {
		return mockActivityId;
	}

	public void setMockActivityId(Long mockActivityId) {
		this.mockActivityId = mockActivityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartChanged() {
		return startChanged;
	}

	public void setStartChanged(Date startChanged) {
		this.startChanged = startChanged;
	}

	public Date getEndChanged() {
		return endChanged;
	}

	public void setEndChanged(Date endChanged) {
		this.endChanged = endChanged;
	}

	public UserChangeType getType() {
		return type;
	}

	public void setType(UserChangeType type) {
		this.type = type;
	}

	public Integer getRuleMatchedCount() {
		return ruleMatchedCount;
	}

	public void setRuleMatchedCount(Integer ruleMatchedCount) {
		this.ruleMatchedCount = ruleMatchedCount;
	}

	public Integer getRuleMismatchCount() {
		return ruleMismatchCount;
	}

	public void setRuleMismatchCount(Integer ruleMismatchCount) {
		this.ruleMismatchCount = ruleMismatchCount;
	}

}
