package hale.bc.server.to.result;

public class FailedResult implements ApplicationResult {
	private int result = -1;
	private String errorMsg;
	
	public FailedResult(int result, String errorMsg) {
		super();
		this.result = result;
		this.errorMsg = errorMsg;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
}
