package utils;

import java.util.Random;

import enums.StatusCode;
import enums.TestResult;
import enums.TestType;

public class Utils {

	private static final Random RANDOM = new Random();

	public static StatusCode statusCode(int statusCode) {
		if (statusCode == -1) {
			return StatusCode.LOGOFF;
		} else if (statusCode == 1) {
			return StatusCode.LOGIN;
		} else if (statusCode == 2) {
			return StatusCode.REGISTER;
		} else if (statusCode == 3) {
			return StatusCode.TESTS;
		} else if (statusCode == 4) {
			return StatusCode.TESTING;
		} else if (statusCode == 5) {
			return StatusCode.TEST_REQUEST;
		} else if (statusCode == 6) {
			return StatusCode.PROGRESS;
		} else if (statusCode == 7) {
			return StatusCode.INFO;
		} else if (statusCode == 8) {
			return StatusCode.ADMIN;
		} else if (statusCode == 9) {
			return StatusCode.USERS;
		} else if (statusCode == 10) {
			return StatusCode.POSITIVE;
		} else if (statusCode == 11) {
			return StatusCode.NEGATIVE;
		} else if (statusCode == 12) {
			return StatusCode.UNDER_SURVEILLANCE;
		} else if (statusCode == 13) {
			return StatusCode.UNDEFINED;
		} else if (statusCode == 14) {
			return StatusCode.NEW_POSITIVE;
		} else if (statusCode == 15) {
			return StatusCode.EXPIRED_SURVEILLANCE;
		} else if (statusCode == 16) {
			return StatusCode.DONE;
		}
		return null;
	}

	public static TestType testType(int type) {
		if (type == 0) {
			return TestType.SELF;
		} else if (type == 1) {
			return TestType.FAST;
		} else if (type == 2) {
			return TestType.PCR;
		}
		return null;
	}

	public static TestResult getTesResult() {
		return RANDOM.nextInt(2) == 0 ? TestResult.NEGATIVE : TestResult.POSITIVE;
	}

	private Utils() {

	}

}
