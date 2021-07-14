package enums;

public enum TestResult {

	POSITIVE("Pozitivan"), NEGATIVE("Negativan");
	
	private String result;
	
	private TestResult(String result) {
		this.result = result;
	}
	
	public String getResult() {
		return result;
	}

}
