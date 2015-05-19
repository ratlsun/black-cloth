package hale.bc.server.service;

import hale.bc.server.repository.UserOperationDao;
import hale.bc.server.to.UserChange;
import hale.bc.server.to.UserChangeType;
import hale.bc.server.to.UserOperation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserOperationService {

	final static long MaxTimeDiff = 15 * 60 * 1000;	//15min
	final static long MaxTimeInterval = 5 * 60 * 1000; //5min
	
	@Autowired
	private UserOperationDao userOperationDao;
	
    public void log(UserOperation userOperation) {
    		userOperationDao.createUserOperation(userOperation);
    }
	
    public List<UserChange> summary(Date after, String user){
    		List<UserChange> chs = new ArrayList<>();
    		List<UserOperation> ops = userOperationDao.getUserOperationsAfter(after, user);
    		
    		Map<Long, UserChange> mockerRuleMap = new HashMap<>();
    		for (UserOperation op : ops) {
    			switch(op.getType()) {
	    			case CreateMocker:
	    			case ChangeMockerOwner:
	    			case ChangeMockerType:
	    			case ChangeMockerName:
	    			case DeleteMocker:
	    				chs.add(UserChange.mockerChange(op.getMocker(), op, UserChangeType.valueOf(op.getType().name())));
	    				break;
	    			case CreateRule:
	    			case UpdateRule:
	    			case DeleteRule:
	    			case EnableRule:
	    			case DisableRule:
	    				Long mid = op.getMocker().getId();
	    				if (needMerge(mockerRuleMap, op)){
	    					mockerRuleMap.get(mid).merge(op);
	    				} else {
	    					UserChange uc = UserChange.mockerChange(op.getMocker(), op, UserChangeType.ChangeMockerRules);
	    					chs.add(uc);
	    					mockerRuleMap.put(mid, uc);
	    				}
	    				break;
	    			case StartMock:
	    			case StopMock:
    					chs.add(UserChange.activityChange(op));
	    				break;
	    			case PauseMock:
	    			case ResumeMock:
	    			default:
    			}
    		}
    		return chs;
    }
    
    private boolean needMerge(Map<Long, UserChange> map, UserOperation op) {
    		Long mid = op.getMocker().getId();
    		if (map.containsKey(mid)){
    			UserChange uc = map.get(mid);
    			long logTime = op.getLogged().getTime();
				if (uc.getEndChanged().getTime() - logTime < MaxTimeDiff ||
						uc.getStartChanged().getTime() - logTime < MaxTimeInterval) {
					return true;
				} 
    		}
    		return false;
    }
}
