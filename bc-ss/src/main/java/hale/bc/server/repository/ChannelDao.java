package hale.bc.server.repository;

import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.to.Channel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelDao {

	private StringRedisTemplate stringTemplate;
	
	private ListOperations<String, String> channelSystemIndex;
	private ValueOperations<String, String> channelSystemNames;
	private ValueOperations<String, Channel> channels;
	private RedisAtomicLong channelIdGenerator;
	
	@Autowired
	public ChannelDao(RedisTemplate<String, Channel> channelTemplate, StringRedisTemplate template) {
		stringTemplate = template;
		channels = channelTemplate.opsForValue();
		channelSystemNames = stringTemplate.opsForValue();
		channelSystemIndex = stringTemplate.opsForList();
		channelIdGenerator = new RedisAtomicLong(KeyUtils.channelId(), stringTemplate.getConnectionFactory());
	}
	
	public Channel createChannel(Channel channel) throws DuplicatedEntryException {
		String channelNameKey = getChannelNameKey(channel);
		checkChannelName(channelNameKey);
			
		long cid = channelIdGenerator.incrementAndGet();
		channel.setId(cid);
		String channelId = String.valueOf(cid);
		
		channels.set(KeyUtils.channelId(channelId), channel);
		channelSystemNames.set(channelNameKey, channelId);
		channelSystemIndex.rightPush(KeyUtils.channelSystem(channel.getSystem()), channelId);
		
		return channel;
	}
	
	public Channel updateChannel(Channel channel) throws DuplicatedEntryException {
		String channelId = String.valueOf(channel.getId());
		String channelIdKey = KeyUtils.channelId(channelId);
		if (stringTemplate.hasKey(channelIdKey)) {
			Channel older = channels.get(channelIdKey);
			
			String olderNameKey = getChannelNameKey(older);
			String channelNameKey = getChannelNameKey(channel);
			if (!olderNameKey.equals(channelNameKey)) {
				checkChannelName(channelNameKey);
				stringTemplate.delete(olderNameKey);
				channelSystemNames.set(channelNameKey, channelId);
			}
			
			char oldSystem = older.getSystem();
			char newSystem = channel.getSystem();
			if (oldSystem != newSystem) {
				channelSystemIndex.remove(KeyUtils.channelSystem(oldSystem), 1, channelId);
				channelSystemIndex.rightPush(KeyUtils.channelSystem(newSystem), channelId);
			}
			channels.set(channelIdKey, channel);
			return channels.get(channelIdKey);
		}
		return null;
	}

	private String getChannelNameKey(Channel channel)  {
		char system = channel.getSystem(); 
		String serviceName = channel.getServiceName();
		return KeyUtils.channelName(system, serviceName);
	}
	
	private void checkChannelName(String channelNameKey) throws DuplicatedEntryException {
		if (stringTemplate.hasKey(channelNameKey)) {
			throw new DuplicatedEntryException(channelNameKey);
		}
	}
	
	public Channel deleteChannel(Long cid) {
		String channelId = String.valueOf(cid);
		String channelIdKey = KeyUtils.channelId(channelId);
		if (stringTemplate.hasKey(channelIdKey)) {
			Channel older = channels.get(channelIdKey);
			stringTemplate.delete(getChannelNameKey(older));
			channelSystemIndex.remove(KeyUtils.channelSystem(older.getSystem()), 1, channelId);
			stringTemplate.delete(channelIdKey);
			
			channels.set(KeyUtils.channelId(String.valueOf(-cid)), older);
			return older;
		}
		return null;
	}
	
	public List<Channel> getAllChannels() {
		List<Channel> channels = new ArrayList<>();
		for (String systemKey : stringTemplate.keys(KeyUtils.channelSystem('?'))) {
			for (String cid : channelSystemIndex.range(systemKey, 0, -1)) {
				channels.add(this.channels.get(KeyUtils.channelId(cid)));
			}
		}
		return channels;
	}
	
	public List<Channel> getChannelsBySystems(String systems) {
		List<Channel> channels = new ArrayList<>();
		for (char system : systems.toCharArray()) {
			channels.addAll(getChannelsBySystem(system));
		}
		return channels;
	}
	
	public List<Channel> getChannelsBySystem(char system) {
		List<Channel> channels = new ArrayList<>();
		String systemKey = KeyUtils.channelSystem(system);
		if (stringTemplate.hasKey(systemKey)) {
			for (String cid : channelSystemIndex.range(systemKey, 0, -1)) {
				channels.add(this.channels.get(KeyUtils.channelId(cid)));
			}
		}
		return channels;
	}

	public List<String> getAllSystems() {
		List<String> systems = new ArrayList<>();
		for (String systemKey : stringTemplate.keys(KeyUtils.channelSystem('?'))) {
			systems.add(KeyUtils.channelSystemValue(systemKey));
		}
		return systems;
	}

}
