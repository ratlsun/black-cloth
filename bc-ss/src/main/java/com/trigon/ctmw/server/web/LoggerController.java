package com.trigon.ctmw.server.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoggerController {
	
	private Log log = LogFactory.getLog(LoggerController.class);
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	@RequestMapping(value="/log.do", method=RequestMethod.POST)
    public String log(@RequestBody LoggingMessage message) {
		String d = dateFormat.format(new Date(message.getDatetime()));
		String c = message.getClient().split("u=")[1];
		String m = String.format("%s %5s [%s] - %s", d, message.getLevel(), c, message.getMessage());
		log.info(m);
		return "{\"result\": \"SUCCES\"}";  
    }
}
