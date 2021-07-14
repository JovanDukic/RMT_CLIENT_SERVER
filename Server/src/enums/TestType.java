package enums;

public enum TestType {

	SELF(0), FAST(1), PCR(2);

	private int type;

	private TestType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

}