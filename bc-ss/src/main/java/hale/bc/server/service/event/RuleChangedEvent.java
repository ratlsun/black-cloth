package hale.bc.server.service.event;

import hale.bc.server.to.Rule;

import org.springframework.context.ApplicationEvent;

public class RuleChangedEvent extends ApplicationEvent {

	private static final long serialVersionUID = -4372398725184160220L;
	
	private final Rule rule;

	public RuleChangedEvent(Rule source) {
		super(source);
		this.rule = source;
	}

	public Rule getRule() {
		return rule;
	}

}
