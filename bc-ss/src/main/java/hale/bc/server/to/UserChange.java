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
	private String mockActivityCode;
	private String name;
	private Date startChanged;
	private Date endChanged;
	private UserChangeType type;
	
	public UserChange() {
		super();
	}
	
	static public UserChange mockerChange(Mocker mocker, UserOperation mockerOperation,
			UserChangeType type) {
		UserChange uc = new UserChange();
		uc.setUserName(mockerOperation.getUserName());
		uc.setMocker(mocker);
		uc.setStartChanged(mockerOperation.getLogged());
		uc.setEndChanged(mockerOperation.getLogged());
		uc.setType(type);
		switch(type) {
			case CreateMocker:
			case ChangeMockerOwner:
			case ChangeMockerType:
			case ChangeMockerName:
			case WatchMocker:
			case UnwatchMocker:
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
	
	static public UserChange activityChange(UserOperation activityOperation) {
		UserChange uc = new UserChange();
		uc.setUserName(activityOperation.getUserName());
		uc.setMockActivityCode(activityOperation.getMockActivityCode());
		uc.setName(activityOperation.getName());
		uc.setType(UserChangeType.MockActivity);
		uc.setStartChanged(activityOperation.getLogged());
		uc.setEndChanged(activityOperation.getLogged());
		return uc;
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

	public String getMockActivityCode() {
		return mockActivityCode;
	}

	public void setMockActivityCode(String mockActivityCode) {
		this.mockActivityCode = mockActivityCode;
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
