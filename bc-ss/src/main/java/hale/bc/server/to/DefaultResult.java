package hale.bc.server.to;

public class DefaultResult {
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
