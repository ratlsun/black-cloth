package hale.bc.server.repository;

abstract class KeyUtils {
	static final String ID_GENERATOR = "id-generator:";
	
	static final String USER = "user:";
	static final String CHANNEL = "channel:";
	static final String MOCKER = "mocker:";
	static final String RULE = "rule:";
	static final String USER_OPERATION = "user-operation:";
	static final String MOCK_ACTIVITY = "mock-activity:";
	
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

	public static String channelId() {
		return ID_GENERATOR + "channel";
	}

	public static String channelName(char system, String serviceName) {
		return CHANNEL + system + "-system:" + serviceName + ":cid";
	}

	public static String channelId(String channelId) {
		return CHANNEL + channelId;
	}

	public static String channelSystem(char system) {
		return CHANNEL + system + "-system:ids";
	}

	public static String channelSystemValue(String key) {
		int l = CHANNEL.length();
		return key.substring(l, l+1);
	}
	
	public static String mockerId() {
		return ID_GENERATOR + "mocker";
	}
	
	public static String mockerId(String mockerId) {
		return MOCKER + mockerId;
	}

	public static String mockerOwnerName(String owner, String name) {
		return MOCKER + "owner-" + owner + ":" + name + ":mid";
	}

	public static String mockerOwner(String owner) {
		return MOCKER + "owner-" + owner + ":names";
	}

	public static String ruleId() {
		return ID_GENERATOR + "rule"	;
	}

	public static String ruleId(String ruleId) {
		return RULE + ruleId;
	}

	public static String ruleMocker(Long mockerId) {
		return RULE + "mocker-" + mockerId + ":ids";
	}

	public static String userOperationMocker(Long mockerId) {
		return USER_OPERATION + "mocker-" + mockerId + ":values";
	}
	
	public static String userOperationUsername(String username) {
		return USER_OPERATION + "user-" + username + ":values";
	}

	public static String mockActivityCode(String code) {
		return MOCK_ACTIVITY + code;
	}

	public static String mockActivityOwner(String owner) {
		return MOCK_ACTIVITY + "owner-" + owner + ":macodes";
	}
}
