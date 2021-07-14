package utils;

import java.util.regex.Pattern;

import config.Config;
import enums.MessageCode;
import enums.ProgressStatus;
import enums.StatusCode;
import enums.TestType;
import enums.UserStatus;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Utils {

	private static final Pattern PATTERN = Pattern.compile(Config.EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

	public static boolean validateEmail(String email) {
		return PATTERN.matcher(email).find();
	}

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

	public static MessageCode messageCode(int messageCode) {
		if (messageCode == -1) {
			return MessageCode.LOGIN_CANCLE;
		} else if (messageCode == 1) {
			return MessageCode.LOGIN_OK;
		} else if (messageCode == -2) {
			return MessageCode.REGISTER_CANCLE;
		} else if (messageCode == 2) {
			return MessageCode.REGISTER_OK;
		} else if (messageCode == -3) {
			return MessageCode.TEST_NO;
		} else if (messageCode == 3) {
			return MessageCode.TEST_OK;
		}
		return null;
	}

	public static ProgressStatus progressStatus(double progress) {
		if (progress == -1) {
			return ProgressStatus.NONE;
		} else if (progress == 0) {
			return ProgressStatus.QUEUING;
		} else if (progress == 25) {
			return ProgressStatus.SENDING;
		} else if (progress == 50D) {
			return ProgressStatus.PROCESSING_1;
		} else if (progress == 75) {
			return ProgressStatus.PROCESSING_2;
		} else if (progress == 100) {
			return ProgressStatus.DONE;
		}
		return null;
	}

	public static String testType(TestType testType) {
		switch (testType) {
		case SELF: {
			return "test samoprocene";
		}
		case FAST: {
			return "brzi test";
		}
		case PCR: {
			return "PCR test";
		}
		default:
			return "Error";
		}
	}

	public static String userStatus(UserStatus userStatus) {
		switch (userStatus) {
		case POSITIVE: {
			return "Pozitivan";
		}
		case NEGATIVE: {
			return "Negativan";
		}
		case UNDER_SURVEILLANCE: {
			return "Pod nadzorom";
		}
		case UNDEFINED: {
			return "Nedefinisan";
		}
		default:
			return "Error";
		}
	}

	public static Alert makeCustomAlert(String title, String contextText) {
		ButtonType yes = new ButtonType("DA", ButtonData.OK_DONE);
		ButtonType no = new ButtonType("NE", ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(AlertType.CONFIRMATION, contextText, yes, no);
		alert.setTitle(title);
		return alert;
	}

	public static Alert makeAlert(AlertType alertType, String title, String contextText) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(contextText);
		return alert;
	}

	public static Font dialogFont(int size) {
		return Font.font("Arial", FontWeight.BOLD, size);
	}

	public static Font font(FontWeight fontWeight, int size) {
		return Font.font("Arial", fontWeight, size);
	}

	private Utils() {

	}

}
