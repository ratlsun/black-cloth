package hale.bc.server.service.template;

import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {
	private final VelocityEngine velocityEngine;
	private final ToolContext toolContext;

	public TemplateService() {
		velocityEngine = new VelocityEngine();
		velocityEngine.init();

		ToolManager toolManager = new ToolManager();
		toolManager.configure("velocity-tool.xml");
		toolContext = toolManager.createContext();
	}

	public String evaluate(String requestBody, String template) {
		Context context = new VelocityContext(toolContext);
		context.put("request", requestBody);

		Writer writer = new StringWriter();
		velocityEngine.evaluate(context, writer, TemplateService.class.getName(), template);

		return writer.toString();
	}

}
