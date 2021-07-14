package enums;

public enum StatusCode {

	LOGOFF(-1), LOGIN(1), REGISTER(2), TESTS(3), TESTING(4), TEST_REQUEST(5), PROGRESS(6), INFO(7), ADMIN(8), USERS(9),
	POSITIVE(10), NEGATIVE(11), UNDER_SURVEILLANCE(12), UNDEFINED(13), NEW_POSITIVE(14), EXPIRED_SURVEILLANCE(15),
	DONE(16);

	private int statusCode;

	private StatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int statusCode() {
		return statusCode;
	}

}
