package hale.bc.server.web;

import hale.bc.server.repository.ChannelDao;
import hale.bc.server.repository.exception.DuplicatedEntryException;
import hale.bc.server.to.Channel;
import hale.bc.server.to.FailedResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/channels")
public class ChannelController {
	
	@Autowired
	private ChannelDao channelDao;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method=RequestMethod.POST)
    public Channel add(@RequestBody Channel channel) throws DuplicatedEntryException {
		return channelDao.createChannel(channel);
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method=RequestMethod.GET)
    public List<Channel> getAll()  {
		return channelDao.getAllChannels();
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method=RequestMethod.GET, params="sys")
    public List<Channel> getBySystems(@RequestParam(value = "sys", required = true) String sysChars)  {
		return channelDao.getChannelsBySystems(sysChars);
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/systems", method=RequestMethod.GET)
    public List<String> getAllSystems()  {
		return channelDao.getAllSystems();
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{cid}", method=RequestMethod.PUT)
    public Channel update(@RequestBody Channel channel)  throws DuplicatedEntryException {
		return channelDao.updateChannel(channel);
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{cid}", method=RequestMethod.DELETE)
    public Channel delete(@PathVariable Long cid) {
		return channelDao.deleteChannel(cid);
    }
	
	@ExceptionHandler(DuplicatedEntryException.class)
	public FailedResult handleCustomException(DuplicatedEntryException ex) {
		return new FailedResult(-1, ex.getMessage());
	}
	
}
