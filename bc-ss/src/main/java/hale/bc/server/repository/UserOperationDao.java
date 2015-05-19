package hale.bc.server.repository;

import hale.bc.server.to.UserOperation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class UserOperationDao {

	private ZSetOperations<String, UserOperation> mockerIndex;
	private ZSetOperations<String, UserOperation> userIndex;
	
	@Autowired
	public UserOperationDao(RedisTemplate<String, UserOperation> userOperationTemplate) {
		mockerIndex = userOperationTemplate.opsForZSet();
		userIndex = userOperationTemplate.opsForZSet();
	}
	
	public UserOperation createUserOperation(UserOperation userOperation) {
		userOperation.setLogged(new Date());
		if(userOperation.getMocker() != null){
			mockerIndex.add(KeyUtils.userOperationMocker(userOperation.getMocker().getId()), userOperation, userOperation.getLogged().getTime());
		}
		userIndex.add(KeyUtils.userOperationUsername(userOperation.getUserName()), userOperation, userOperation.getLogged().getTime());
		return userOperation;
	}

	public List<UserOperation> getUserOperationsAfter(Date after, String user) {
		Set<UserOperation> uos = userIndex.reverseRangeByScore(KeyUtils.userOperationUsername(user), after.getTime(), Long.MAX_VALUE);
		return new ArrayList<UserOperation>(uos);
	}
	
}
