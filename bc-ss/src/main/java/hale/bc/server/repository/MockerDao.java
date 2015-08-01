package hale.bc.server.repository;

import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.to.Mocker;
import hale.bc.server.to.MockerType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class MockerDao {

	private StringRedisTemplate stringTemplate;
	
	private ZSetOperations<String, String> ownerIndex;
	private ValueOperations<String, String> ownerNames;
	private BoundZSetOperations<String,String> publicGroup;
	private ZSetOperations<String, String> watcherGroup;
	private ValueOperations<String, Mocker> mockers;
	private RedisAtomicLong mockerIdGenerator;
	
	@Autowired
	public MockerDao(RedisTemplate<String, Mocker> mockerTemplate, StringRedisTemplate template) {
		stringTemplate = template;
		mockers = mockerTemplate.opsForValue();
		ownerNames = stringTemplate.opsForValue();
		ownerIndex = stringTemplate.opsForZSet();
		publicGroup = stringTemplate.boundZSetOps(KeyUtils.mockerPublic());
		watcherGroup = stringTemplate.opsForZSet();
		mockerIdGenerator = new RedisAtomicLong(KeyUtils.mockerId(), stringTemplate.getConnectionFactory());
	}
	
	public Mocker createMocker(Mocker mocker) throws DuplicatedEntryException {
		String mockerOwnerNameKey = getOwnerNameKey(mocker);
		checkMockerName(mockerOwnerNameKey);
			
		long mid = mockerIdGenerator.incrementAndGet();
		mocker.setId(mid);
		mocker.setCreated(new Date());
		mocker.setUpdated(new Date());
		String mockerId = String.valueOf(mid);
		
		mockers.set(KeyUtils.mockerId(mockerId), mocker);
		ownerNames.set(mockerOwnerNameKey, mockerId);
		ownerIndex.add(KeyUtils.mockerOwner(mocker.getOwner()), mocker.getName(), 0);
		
		if (MockerType.Public == mocker.getType()) {
			publicGroup.add(mockerId, mocker.getUpdated().getTime());
		}
		return mocker;
	}
	
	private String getOwnerNameKey(Mocker mocker)  {
		return KeyUtils.mockerOwnerName(mocker.getOwner(), mocker.getName());
	}
	
	private void checkMockerName(String mockerOwnerNameKey) throws DuplicatedEntryException {
		if (stringTemplate.hasKey(mockerOwnerNameKey)) {
			throw new DuplicatedEntryException(mockerOwnerNameKey);
		}
	}
	
	public Mocker getMockerById(Long mockerId) {
		return mockers.get(KeyUtils.mockerId(String.valueOf(mockerId)));
	}
	
	public Mocker getMockerById(Long mockerId, String owner) {
		Mocker m = mockers.get(KeyUtils.mockerId(String.valueOf(mockerId)));
		if (m != null && m.getOwner() != null && (m.getOwner().equals(owner) || MockerType.Public == m.getType())) {
			return m;
		}
		return null;
	}

	public List<Mocker> getMockers(String owner) {
		List<Mocker> result = new ArrayList<>();
		for (String mockerName : ownerIndex.range(KeyUtils.mockerOwner(owner), 0, -1)){
			Mocker m = mockers.get(KeyUtils.mockerId(ownerNames.get(KeyUtils.mockerOwnerName(owner, mockerName))));
			if (m != null) {
				result.add(m);
			}
		}
		return result;
	}
	
	public List<Mocker> getPublicMockers() {
		List<Mocker> result = new ArrayList<>();
		for (String mockerId : publicGroup.reverseRange(0, -1)){
			Mocker m = mockers.get(KeyUtils.mockerId(mockerId));
			if (m != null) {
				result.add(m);
			}
		}
		return result;
	}
	
	public List<Mocker> getMockersByWatcher(String watcher) {
		List<Mocker> result = new ArrayList<>();
		for (String mockerId : watcherGroup.reverseRange(KeyUtils.mockerWatcher(watcher), 0, -1)){
			Mocker m = mockers.get(KeyUtils.mockerId(mockerId));
			if (m != null && m.getType() == MockerType.Public) {
				result.add(m);
			}
		}
		return result;
	}
	
	public Mocker deleteMocker(Long mockerId) {
		Mocker m = getMockerById(mockerId);
		if (m == null) {
			return null;
		}
		stringTemplate.delete(getOwnerNameKey(m));
		ownerIndex.remove(KeyUtils.mockerOwner(m.getOwner()), m.getName());
		stringTemplate.delete(KeyUtils.mockerId(String.valueOf(mockerId)));
		if (MockerType.Public == m.getType()) {
			publicGroup.remove(String.valueOf(mockerId));
		}
		mockers.set(KeyUtils.mockerId(String.valueOf(-mockerId)), m);
		return m;
	}
	
	public Mocker updateMocker(Mocker mocker, boolean modifyUpdated) throws DuplicatedEntryException {
		Long mockerId = mocker.getId();
		String mockerIdText = String.valueOf(mockerId);
		Mocker oldMocker = getMockerById(mockerId);
		if (oldMocker == null) {
			return null;
		}
		if (!oldMocker.getName().equals(mocker.getName())){
			String newOwnerNameKey = getOwnerNameKey(mocker);
			checkMockerName(newOwnerNameKey);
			
			stringTemplate.delete(getOwnerNameKey(oldMocker));
			ownerNames.set(newOwnerNameKey, mockerIdText);
			
			String ownerKey = KeyUtils.mockerOwner(mocker.getOwner());
			ownerIndex.remove(ownerKey, oldMocker.getName());
			ownerIndex.add(ownerKey, mocker.getName(), 0);
		}
		if (modifyUpdated) {
			mocker.setUpdated(new Date());
		}
		if (MockerType.Public == mocker.getType()) {
			publicGroup.add(mockerIdText, mocker.getUpdated().getTime());
		} else { // Private
			publicGroup.remove(mockerIdText);
			mocker.setWatcherCount(0l);
		}
		mockers.set(KeyUtils.mockerId(mockerIdText), mocker);
		return mocker;
	}
	
	public Mocker updateMocker(Mocker mocker) throws DuplicatedEntryException {
		return updateMocker(mocker, true); 
	}

	public boolean putMockerToWatcherGroup(Mocker mocker, String watcher) {
		return watcherGroup.add(KeyUtils.mockerWatcher(watcher), String.valueOf(mocker.getId()), (new Date()).getTime());
	}
	
	public boolean removeMockerFromWatcherGroup(Mocker mocker, String watcher) {
		return  watcherGroup.remove(KeyUtils.mockerWatcher(watcher), String.valueOf(mocker.getId())) == 1;
	}
	
	public Mocker updateMockerLastDate(long mid, Date updated){
		Mocker mocker = getMockerById(mid);
		if (mocker == null) {
			return null;
		}
		mocker.setUpdated(updated);
		mockers.set(KeyUtils.mockerId(String.valueOf(mid)), mocker);
		return mocker;
	}
}
