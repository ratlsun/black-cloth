package hale.bc.server.to.result;

public class DefaultResult implements ApplicationResult {
	private int result = 0;
	
	public DefaultResult(int result) {
		super();
		this.result = result;
	}
	
	public DefaultResult() {
		super();
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
