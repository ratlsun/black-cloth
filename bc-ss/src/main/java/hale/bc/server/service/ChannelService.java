package hale.bc.server.service;

import hale.bc.server.to.Channel;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class ChannelService {

	@Autowired
	private VelocityEngine engine;
	
	public void generateConfigFile(Channel channel) {
		
		BufferedWriter writer = null;
		try {
			writer = Files.newBufferedWriter(configFilePath(channel), 
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("channel", channel);
			VelocityEngineUtils.mergeTemplate(engine, "mpe-config-template.xml", "UTF-8", model, writer);
			
		} catch (IOException e) {
			//Eaten, do nothing!
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void removeConfigFile(Channel channel) {
		try {
			Files.deleteIfExists(configFilePath(channel));
		} catch (IOException e) {
			//Eaten, do nothing!
			e.printStackTrace();
		}
	}
	
	private Path configFilePath(Channel channel) {
		String fileName = String.format("sih-%s-mpe.xml", channel.getId());
		return FileSystems.getDefault().getPath("mpe-configs", fileName);
	}
	
}
