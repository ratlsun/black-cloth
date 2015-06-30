package hale.bc.server.web;

import hale.bc.server.repository.MockHitDao;
import hale.bc.server.service.MockActivityService;
import hale.bc.server.service.template.TemplateService;
import hale.bc.server.to.MockHit;
import hale.bc.server.to.Rule;
import hale.bc.server.to.RuleRequest;
import hale.bc.server.to.RuleRequestBody;
import hale.bc.server.to.RuleRequestHeader;
import hale.bc.server.to.RuleResponse;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
	final private String MOCK_PATTERN = "/{code:[a-z0-9A-Z+-]{16}}.mock/**";
	final private int MOCK_PATTERN_LENGTH = 23;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private MockActivityService mockActivityService;
	
	@Autowired
	private MockHitDao mockHitDao;
	
	@RequestMapping(value="/touch", method=RequestMethod.GET)
	@ResponseBody
    public String touch() {
        return "It works!";
    }
	
	@RequestMapping(value=MOCK_PATTERN)
    public ResponseEntity<String> match(@PathVariable String code, HttpMethod method, HttpServletRequest request,
    		@RequestBody(required = false) String body) {
		String respContent = "";
		RuleRequestBody rb = null;
		String ct = "application/json";
		String api = request.getServletPath().substring(MOCK_PATTERN_LENGTH);
		String qs = request.getQueryString();
		if (qs != null && qs.length() > 0){
			api = api + '?' + qs;
		}
		if (body != null && body.length() > 0){
			rb = RuleRequestBody.buildBody(body);
		}
		RuleRequest req = RuleRequest.buildRequest(RuleRequestHeader.buildHeader(api, "*", method.name()), rb);
		MockHit hit = MockHit.buildUnmatchedHit(code, req);
		Rule rule = mockActivityService.match(code, req);
		if (rule != null) {
			RuleResponse resp = rule.getResponse();
			respContent = templateService.evaluate(body, rule.getResponse().getBody().getContent());
			ct = resp.getHeader().getContentType();
			resp.getBody().setContent(respContent);
			hit.setResponse(resp);
			hit.setMatch(true);
		}
		mockHitDao.createMockHit(hit);
		return ResponseEntity.ok().header("Content-Type", ct).body(respContent);
    }
	
}
