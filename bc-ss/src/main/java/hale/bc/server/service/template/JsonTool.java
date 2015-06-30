package hale.bc.server.service.template;

import net.minidev.json.JSONArray;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

@org.apache.velocity.tools.config.DefaultKey("json")
public class JsonTool {

	public String path(String content, String jsonPath) {

		ReadContext context = JsonPath.parse(content);
		Object result = context.read(jsonPath);
		if (result == null) {
			return null;
		}
		if (result instanceof JSONArray) {
			return ((JSONArray) result).toJSONString();
		}
		return result.toString();
	}

}
