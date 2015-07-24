package hale.bc.server.to;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserOperation {
	private String userName;
	private Mocker mocker;
	private Long ruleId;
	private String mockActivityCode;
	private String name;
	private String desc;
	private Date logged;
	private UserOperationType type;
	
	public UserOperation() {
		super();
	}
	
	static public UserOperation mockerOperation(String userName, Mocker oldMocker, Mocker newMocker, 
			UserOperationType type) {
		UserOperation uo = new UserOperation();
		uo.setUserName(userName);
		uo.setType(type);
		switch(type) {
			case CreateMocker:
				uo.setMocker(newMocker);
				uo.setName("新建模拟系统－" + newMocker.getName());
				break;
			case ChangeMockerOwner:
				uo.setMocker(oldMocker);
				uo.setName("将模拟系统－" + oldMocker.getName() + " 移交给用户 " + newMocker.getOwner());
				break;
			case ChangeMockerType:
				uo.setMocker(oldMocker);
				uo.setName("将模拟系统－" + oldMocker.getName() + " 更改为 " +
						(newMocker.getType() == MockerType.Private ? "私有的" : "公开的"));
				break;
			case ChangeMockerName:
				uo.setMocker(oldMocker);
				uo.setName("将模拟系统－" + oldMocker.getName() + " 改名为 " + newMocker.getName());
				break;
			case DeleteMocker:
				uo.setMocker(oldMocker);
				uo.setName("删除模拟系统－" + oldMocker.getName());
				break;
//			case CollectMocker:
//				uo.setMocker(oldMocker);
//				uo.setName("将公共模拟系统－" + oldMocker.getName() + " 标记为关注");
//				break;
//			case CancelCollectMocker:
//				uo.setMocker(oldMocker);
//				uo.setName("将公共模拟系统－" + oldMocker.getName() + " 取消关注");
//				break;
			default:
		}
		return uo;
	}
	
	static public UserOperation ruleOperation(String userName, Long ruleId, Mocker mocker, UserOperationType type) {
		UserOperation uo = new UserOperation();
		uo.setUserName(userName);
		uo.setMocker(mocker);
		uo.setRuleId(ruleId);
		uo.setType(type);
		switch(type) {
			case CreateRule:
				uo.setName("新建规则－" + ruleId);
				break;
			case UpdateRule:
				uo.setName("更新规则－" + ruleId);
				break;
			case DeleteRule:
				uo.setName("删除规则－" + ruleId);
				break;
			case EnableRule:
				uo.setName("生效规则－" + ruleId);
				break;
			case DisableRule:
				uo.setName("失效规则－" + ruleId);
				break;
			default:
		}
		return uo;
	}
	
	static public UserOperation activityOperation(String userName, String mockActivityCode, UserOperationType type) {
		UserOperation uo = new UserOperation();
		uo.setUserName(userName);
		uo.setMockActivityCode(mockActivityCode);
		uo.setType(type);
		switch(type) {
			case StartMock:
				uo.setName("开始进行模拟(" + mockActivityCode + ")");
				break;
			case StopMock:
				uo.setName("结束模拟(" + mockActivityCode + ")");
				break;
			case PauseMock:
				uo.setName("暂停模拟(" + mockActivityCode + ")");
				break;
			case ResumeMock:
				uo.setName("恢复模拟(" + mockActivityCode + ")");
				break;
			default:
		}
		return uo;
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

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getLogged() {
		return logged;
	}

	public void setLogged(Date logged) {
		this.logged = logged;
	}

	public UserOperationType getType() {
		return type;
	}

	public void setType(UserOperationType type) {
		this.type = type;
	}
	
}
