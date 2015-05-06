package hale.bc.server.repository;

import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.to.Mocker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	private ValueOperations<String, Mocker> mockers;
	private RedisAtomicLong mockerIdGenerator;
	
	@Autowired
	public MockerDao(RedisTemplate<String, Mocker> mockerTemplate, StringRedisTemplate template) {
		stringTemplate = template;
		mockers = mockerTemplate.opsForValue();
		ownerNames = stringTemplate.opsForValue();
		ownerIndex = stringTemplate.opsForZSet();
		mockerIdGenerator = new RedisAtomicLong(KeyUtils.mockerId(), stringTemplate.getConnectionFactory());
	}
	
	public Mocker createMocker(Mocker mocker) throws DuplicatedEntryException {
		String mockerOwnerNameKey = getOwnerNameKey(mocker);
		checkMockerName(mockerOwnerNameKey);
			
		long mid = mockerIdGenerator.incrementAndGet();
		mocker.setId(mid);
		String mockerId = String.valueOf(mid);
		
		mockers.set(KeyUtils.mockerId(mockerId), mocker);
		ownerNames.set(mockerOwnerNameKey, mockerId);
		ownerIndex.add(KeyUtils.mockerOwner(mocker.getOwner()), mocker.getName(), 0);
		
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

	public Mocker getMockerById(Long mockerId, String owner) {
		Mocker m = mockers.get(KeyUtils.mockerId(String.valueOf(mockerId)));
		if (m.getOwner().equals(owner)) {
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
	
	public Mocker deleteMocker(Long mockerId, String owner) {
		Mocker m = getMockerById(mockerId, owner);
		if (m == null) {
			return null;
		}
		stringTemplate.delete(getOwnerNameKey(m));
		ownerIndex.remove(KeyUtils.mockerOwner(m.getOwner()), m.getName());
		stringTemplate.delete(KeyUtils.mockerId(String.valueOf(mockerId)));
		mockers.set(KeyUtils.mockerId(String.valueOf(-mockerId)), m);
		return m;
	}
	
	public Mocker updateMocker(Mocker mocker, String owner) throws DuplicatedEntryException {
		Long mockerId = mocker.getId();
		String mockerIdText = String.valueOf(mockerId);
		Mocker oldMocker = getMockerById(mockerId, owner);
		if (oldMocker == null) {
			return null;
		}
		if (!oldMocker.getName().equals(mocker.getName())){
			String newOwnerNameKey = getOwnerNameKey(mocker);
			checkMockerName(newOwnerNameKey);
			
			stringTemplate.delete(getOwnerNameKey(oldMocker));
			ownerNames.set(newOwnerNameKey, mockerIdText);
			
			String ownerKey = KeyUtils.mockerOwner(owner);
			ownerIndex.remove(ownerKey, oldMocker.getName());
			ownerIndex.add(ownerKey, mocker.getName(), 0);
		}
		mockers.set(KeyUtils.mockerId(mockerIdText), mocker);
		return mocker;
	}
}
