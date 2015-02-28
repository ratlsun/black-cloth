package com.trigon.ctmw.server.web;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class RequestController {
	
	private static Set<String> uus = new HashSet<>();
	
	@RequestMapping(value="/g.do", method=RequestMethod.GET)
    public String gateway(
    		@RequestParam(value = "aid", required = true) String accountId, 
    		@RequestParam(value = "tid", required = true) String tableId) {
		String uu = String.valueOf(UUID.randomUUID());
		uus.add(uu);
		return String.format("redirect:/i.do?u=%s#!/home?aid=%s&tid=%s", uu, accountId, tableId);  
    }
	
	@RequestMapping(value="/i.do", method=RequestMethod.GET)
    public String index(@RequestParam(value = "u", required = true) String uuid) {
		if (uus.contains(uuid)) {
			 return "forward:/app.html";
		}
		throw new ResourceNotFoundException();   
    }
	
	@RequestMapping(value="/t.do", method=RequestMethod.GET)
	@ResponseBody
    public String touch() {
        return "It works!";
    }
}
