package hale.bc.server.to;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleRequestHeader {
	private String url;
	private String contentType;
	private String method;
	
	public RuleRequestHeader() {
		super();
	}
	
	static public RuleRequestHeader buildHeader(String url, String contentType, String method){
		RuleRequestHeader header = new RuleRequestHeader();
		header.setContentType(contentType);
		header.setMethod(method);
		header.setUrl(url);
		return header;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@JsonIgnore
	public String getBaseUrl() {
		return url == null ? null : url.split("\\?")[0];
	}
	
	public boolean hasUrlParameter() {
		return url == null ? false : url.indexOf('?') > -1;
	}
	
	@JsonIgnore
	public Map<String, String> getUrlParameters() {
		URI uri;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			return null;
		}
		List<NameValuePair> paras = URLEncodedUtils.parse(uri, "UTF-8");
		Map<String, String> result = new HashMap<>(paras.size());
		for (NameValuePair p : paras) {
			result.put(p.getName(), p.getValue());
		}
		return result;
	}
	
}
