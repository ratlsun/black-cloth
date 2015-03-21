package hale.bc.server.repository;

abstract class KeyUtils {
	static final String USER = "user:";
	
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
	
	static String uid() {
		return "id-generator:user";
	}

}
