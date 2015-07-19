package hale.bc.server.service;

import hale.bc.server.repository.RuleDao;
import hale.bc.server.to.MockActivity;
import hale.bc.server.to.Rule;
import hale.bc.server.to.RuleRequest;
import hale.bc.server.to.RuleRequestBody;
import hale.bc.server.to.RuleRequestHeader;
import hale.bc.server.to.RuleRequestType;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MockActivityService {

	private final static String BASE_URL_KEY_SIGN = "B:";
	
	@Autowired
	private RuleDao ruleDao;
	
	@Autowired
	private RedisTemplate<String, Rule> template;
	
	private ObjectMapper mapper = new ObjectMapper();
	private DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder builder;
	private TransformerFactory transFactory = TransformerFactory.newInstance();
	private Transformer transformer;

	public MockActivityService() throws TransformerConfigurationException, IOException, ParserConfigurationException {
		docFactory.setIgnoringElementContentWhitespace(true);
		docFactory.setIgnoringComments(true);
		builder = docFactory.newDocumentBuilder();
		
	    ClassPathResource cpr = new ClassPathResource("xml-matcher-normalize.xml");
        transformer = transFactory.newTransformer(new StreamSource(cpr.getInputStream()));
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
	}

	public void loadRules(MockActivity activity) {

		for (Long mid : activity.getMockerIds()) {
			List<Rule> rules = ruleDao.getRulesByMocker(mid);
			for (Rule rule : rules) {
				StringBuffer key = null;
				StringBuffer baseKey = null;
				key = new StringBuffer(activity.getCode());
				key.append(':');
				RuleRequestHeader header = rule.getRequest().getHeader();
				
				baseKey = new StringBuffer(key);
				baseKey.append(BASE_URL_KEY_SIGN);
				try {
					baseKey.append(mapper.writeValueAsString(RuleRequestHeader.buildHeader(
							header.getBaseUrl(), header.getContentType(), header.getMethod())));
					baseKey.append(':');
					
					key.append(mapper.writeValueAsString(header));
					key.append(':');
				} catch (JsonProcessingException e) {
					// eaten, this rule does not be load.
					e.printStackTrace();
					continue;
				} 
				
				RuleRequestBody body = rule.getRequest().getBody();
				String content = body.getContent();
				if (content != null && !content.isEmpty()) {
					switch (body.getType()) {
					case Text:
						key.append(content);
						baseKey.append(content);
						break;
					case Json:
						String json = generateJsonContent(content);
						key.append(json);
						baseKey.append(json);
						break;
					case XML:
						String xml = generateXmlContent(content);
						key.append(xml);
						baseKey.append(xml);
						break;
					default:
					}
				}
					
				if (key != null && baseKey != null) {
					template.opsForValue().set(key.toString(), rule);
					template.opsForList().leftPush(baseKey.toString(), rule);
				}
			}
		}
	}
	
	public void clearRules(MockActivity activity) {
		String code = activity.getCode();
		template.delete(template.keys(code + ":*"));
	}

	public Rule match(String code, RuleRequest request) {
		Rule result = null;
		String key = getMatchKey(code, request.getHeader(), request.getBody(), RuleRequestType.Text, false);
		result = matchFullUrl(key);
		if (result == null) {
			key = getMatchKey(code, request.getHeader(), request.getBody(), RuleRequestType.Json, false);
			result = matchFullUrl(key);
		} 
		if (result == null) {
			key = getMatchKey(code, request.getHeader(), request.getBody(), RuleRequestType.XML, false);
			result = matchFullUrl(key);
		} 
		if (result == null && request.getBody()!= null && !request.getBody().getContent().isEmpty()) {
			key = getMatchKey(code, request.getHeader(), RuleRequestBody.buildBody(""), RuleRequestType.None, false);
			result = matchFullUrl(key);
		} 
		if (result == null && request.getHeader().hasUrlParameter()) {
			RuleRequestHeader header = request.getHeader();
			RuleRequestHeader baseHeader = RuleRequestHeader.buildHeader(
					header.getBaseUrl(), header.getContentType(), header.getMethod());
			key = getMatchKey(code, baseHeader, request.getBody(), RuleRequestType.Text, true);
			result = matchBaseUrl(request, key);
			if (result == null) {
				key = getMatchKey(code, baseHeader, request.getBody(), RuleRequestType.Json, true);
				result = matchBaseUrl(request, key);
			} 
			if (result == null) {
				key = getMatchKey(code, baseHeader, request.getBody(), RuleRequestType.XML, true);
				result = matchBaseUrl(request, key);
			} 
			if (result == null && request.getBody()!= null && !request.getBody().getContent().isEmpty()) {
				key = getMatchKey(code, baseHeader, RuleRequestBody.buildBody(""), RuleRequestType.None, true);
				result = matchBaseUrl(request, key);
			} 
		} 
		return result;
	}
	
	private String getMatchKey(String code, RuleRequestHeader header, RuleRequestBody body, RuleRequestType type, boolean isBaseUrl) {
		StringBuffer key = new StringBuffer(code);
		try {
			key.append(':');
			if (isBaseUrl) {
				key.append(BASE_URL_KEY_SIGN);
			}
			key.append(mapper.writeValueAsString(header));
			key.append(':');
			if (body == null || body.getContent() == null || body.getContent().isEmpty()) {
				return key.toString();
			}
			switch (type) {
			case Text:
				key.append(body.getContent());
				break;
			case Json:
				key.append(generateJsonContent(body.getContent()));
				break;
			case XML:
				key.append(generateXmlContent(body.getContent()));
				break;
			default:
			}
		} catch (JsonProcessingException e) {
			// eaten
			e.printStackTrace();
		} 
		return key.toString();
	}
	
	private Rule matchBaseUrl(RuleRequest request, String key) {
		Rule result = null;
		List<Rule> rules = template.opsForList().range(key, 0, -1);
		if (rules == null || rules.isEmpty()){
			return null;
		}
		Map<String, String> reqParas = request.getHeader().getUrlParameters();
		int paraCount = reqParas.size();
		int maxCount = -1;
		for (Rule r : rules) {
			int matchCount = 0;
			boolean matched = true;
			RuleRequestHeader header = r.getRequest().getHeader();
			if (header.hasUrlParameter()) {
				Map<String, String> paras = header.getUrlParameters();
				for (String p: paras.keySet()) {
					if (reqParas.containsKey(p) && reqParas.get(p).equals(paras.get(p))){
						matchCount++;
					} else {
						matched = false;
						break;
					}
				}
			}
			if (!matched) {
				continue;
			}
			if (matchCount == paraCount) {
				result = r;
				break;
			}
			if (matchCount > maxCount) {
				maxCount = matchCount;
				result = r;
			}
		}
		return result;
	}
	
	private Rule matchFullUrl(String key) {
		return template.opsForValue().get(key);
	}
	
	private String generateJsonContent(String json) {
		try {
			return mapper.writeValueAsString(mapper.readTree(json));
		} catch (JsonProcessingException e) {
			// eaten, this rule loaded as empty body.
			//e.printStackTrace();
		} catch (IOException e) {
			// eaten, this rule loaded as empty body.
			//e.printStackTrace();
		}
		return "";
	}
	
	private String generateXmlContent(String xml) {
		try {
		    Document doc = builder.parse(IOUtils.toInputStream(xml, "UTF-8"));
		    
		    StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.getBuffer().toString();
            
		} catch (IOException e) {
			// eaten, this rule loaded as empty body.
			//e.printStackTrace();
		} catch (SAXException e) {
			// eaten, this rule loaded as empty body.
			//e.printStackTrace();
		} catch (TransformerException e) {
			// eaten, this rule loaded as empty body.
			//e.printStackTrace();
		}
		return "";
	}
	
}
