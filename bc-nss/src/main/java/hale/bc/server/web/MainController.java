package hale.bc.server.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private final Logger log = LoggerFactory.getLogger(MainController.class);

	@GetMapping(value="/touch")
    public String touch() {
        return "It works!";
    }

    @PostMapping(value="/login")
    @ResponseStatus(HttpStatus.OK)
    public void login() {
        log.info("=================login================");
    }
}
