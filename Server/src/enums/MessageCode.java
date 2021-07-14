package enums;

public enum MessageCode {

	LOGIN_OK(1), LOGIN_CANCLE(-1), REGISTER_OK(2), REGISTER_CANCLE(-2), TEST_OK(3), TEST_NO(-3);

	private int messageCode;

	private MessageCode(int messageCode) {
		this.messageCode = messageCode;
	}

	public int messageCode() {
		return messageCode;
	}

}
