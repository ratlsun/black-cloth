package hale.bc.server.repository;

abstract public class KeyUtils {
	static final String ID_GENERATOR = "id-generator:";
	
	static final String USER = "user:";
	static final String CHANNEL = "channel:";
	static final String MOCKER = "mocker:";
	static final String RULE = "rule:";
	static final String USER_OPERATION = "user-operation:";
	static final String MOCK_ACTIVITY = "mock-activity:";
	static final String MOCK_HIT = "mock-hit:";
	
	static String userId(String uid) {
		return USER + uid;
	}

	static String users() {
		return "users";
	}

	static String userName(String name) {
		return USER + name + ":uid";
	}
	
	static String userCode(String code) {
		return "user-code:" + code + ":uid";
	}
	
	static String userId() {
		return ID_GENERATOR + "user";
	}

	static String channelId() {
		return ID_GENERATOR + "channel";
	}

	static String channelName(char system, String serviceName) {
		return CHANNEL + system + "-system:" + serviceName + ":cid";
	}

	static String channelId(String channelId) {
		return CHANNEL + channelId;
	}

	static String channelSystem(char system) {
		return CHANNEL + system + "-system:ids";
	}

	static String channelSystemValue(String key) {
		int l = CHANNEL.length();
		return key.substring(l, l+1);
	}
	
	static String mockerId() {
		return ID_GENERATOR + "mocker";
	}
	
	static String mockerId(String mockerId) {
		return MOCKER + mockerId;
	}

	static String mockerOwnerName(String owner, String name) {
		return MOCKER + "owner-" + owner + ":" + name + ":mid";
	}

	static String mockerOwner(String owner) {
		return MOCKER + "owner-" + owner + ":names";
	}
	
	static String mockerPublic() {
		return MOCKER + "public:ids";
	}

	static String ruleId() {
		return ID_GENERATOR + "rule";
	}

	static String ruleId(String ruleId) {
		return RULE + ruleId;
	}

	static String ruleMocker(Long mockerId) {
		return RULE + "mocker-" + mockerId + ":ids";
	}

	static String userOperationMocker(Long mockerId) {
		return USER_OPERATION + "mocker-" + mockerId + ":values";
	}
	
	static String userOperationUsername(String username) {
		return USER_OPERATION + "user-" + username + ":values";
	}

	static String mockActivityCode(String code) {
		return MOCK_ACTIVITY + code;
	}

	static String mockActivityOwner(String owner) {
		return MOCK_ACTIVITY + "owner-" + owner + ":macodes";
	}
	
	public static String mockActivityMocker(Long mockerId) {
		return MOCK_ACTIVITY + "mocker-" + mockerId + ":macodes";
	}

	static String mockHitCode(String mockActivityCode) {
		return MOCK_HIT + "macode-" + mockActivityCode + ":values";
	}
	
	static String pwdCode(String code) {
		return "user-pwd-code:" + code + ":uid";
	}
	
	static String mockerCollectOwnerName(String owner, String name) {
		return MOCKER + "collect-" + owner + ":" + name + ":mid";
	}

	static String mockerCollectOwner(String owner) {
		return MOCKER + "collect-" + owner + ":names";
	}
	
	static String collectMockerOwner(String mockerName) {
		return MOCKER + "collect-" + mockerName + ":names";
	}
}
