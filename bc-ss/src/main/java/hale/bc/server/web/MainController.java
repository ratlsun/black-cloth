package hale.bc.server.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
	@RequestMapping(value="/touch", method=RequestMethod.GET)
	@ResponseBody
    public String touch() {
        return "It works!";
    }
}
