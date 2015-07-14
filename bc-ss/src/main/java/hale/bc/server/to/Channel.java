package hale.bc.server.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
		* @ClassName: Channel 
		* @Description: TODO(这里用一句话描述这个类的作用) 
		* @date 2015年7月15日 上午12:16:50 
		*
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel {
	private Long id;
	private String serviceName;
	private String ip;
	private String port;
	private char system;
	private String desc;
	private ChannelType type = ChannelType.Public;
	
	public Channel() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public char getSystem() {
		return system;
	}

	public void setSystem(char system) {
		this.system = system;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public ChannelType getType() {
		return type;
	}

	public void setType(ChannelType type) {
		this.type = type;
	}
}
