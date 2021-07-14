package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import config.Config;
import data.Database;
import entity.Test;
import enums.Connection;
import enums.Gender;
import enums.MessageCode;
import enums.ProgressStatus;
import enums.StatusCode;
import enums.TestResult;
import enums.TestType;
import enums.UserStatus;
import utils.Utils;

public class UserController implements Runnable {

	private Socket socket;

	private DataOutputStream outputStream;
	private DataInputStream inputStream;

	private int userID = -1;

	private Date lastSelfTestTime = null;
	private Date lastFastTestTime = null;
	private Date lastPcrTestTime = null;
	private Date lastLoginDate = null;

	private ProgressStatus progressStatus = ProgressStatus.NONE;

	private Connection connection = Connection.CLOSED;

	public UserController(Socket socket) {
		this.socket = socket;
		init();
	}

	private void init() {
		try {
			initStreams();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initStreams() throws IOException {
		outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	}

	@Override
	public void run() {
		try {
			while (true) {
				StatusCode statusCode = Utils.statusCode(inputStream.readInt());
				switch (statusCode) {
				case LOGOFF: {
					logoff();
					return;
				}
				case LOGIN: {
					if (login()) {
						return;
					}
					break;
				}
				case REGISTER: {
					register();
					break;
				}
				case TESTS: {
					sendTests();
					break;
				}
				case TEST_REQUEST: {
					testRequest(result());
					break;
				}
				case TESTING: {
					testing(testType());
					break;
				}
				case INFO: {
					info();
					break;
				}
				default:
					break;
				}
			}
		} catch (IOException e) {
			System.err.println("Broken connection, user: " + userID);
		}
	}

	// === reading data === //
	private void testRequest(int result) throws IOException {
		System.out.println("User: " + userID + ", has sent SELF test request!");
		if (lastSelfTestTime == null || timeElapsed(lastSelfTestTime)) {
			if (result < 2) {
				Test test = createTest(TestType.SELF, TestResult.NEGATIVE);
				lastSelfTestTime = test.getDate();
				sendTest(test);
				sendSelfResultMessage(MessageCode.TEST_NO, Config.TEST_NO_NEED);
				Database.setUserStatus(UserStatus.UNDER_SURVEILLANCE, userID);
			} else {
				Test test = createTest(TestType.SELF, TestResult.POSITIVE);
				lastSelfTestTime = test.getDate();
				sendTest(test);
				sendSelfResultMessage(MessageCode.TEST_OK, Config.TEST_OK);
				Database.setUserStatus(UserStatus.POSITIVE, userID);
			}
			info();
		} else {
			System.out.println("User: " + userID + ", has already taken " + " SELF test today!");
			sendTestResponseNO(Config.SELF_NO);
		}
	}

	private void testing(TestType testType) throws IOException {
		System.out.println("User: " + userID + ", has sent " + testType.name() + ", test request!");
		switch (testType) {
		case FAST: {
			if (lastFastTestTime == null || timeElapsed(lastFastTestTime)) {
				Test test = createTest(testType);
				lastFastTestTime = test.getDate();
				sendTest(test);
				sendTestResultMessage(testType, test.getTestResult());

				Database.setUserStatus(UserStatus.valueOf(test.getTestResult().name()), userID);

				if (test.getTestResult() == TestResult.POSITIVE) {
					Database.addNewPositive(userID);
				} else {
					Database.deleteNewPositive(userID);
				}

			} else {
				System.out.println("User: " + userID + ", has already taken " + testType.name() + " test today!");
				sendTestResponseNO(Config.FAST_NO);
			}
			break;
		}
		case PCR: {
			if (lastPcrTestTime == null || timeElapsed(lastPcrTestTime)) {
				queuingPCR();
			} else {
				System.out.println("User: " + userID + ", has already taken " + testType.name() + " test today!");
				sendTestResponseNO(Config.PCR_NO);
			}
			break;
		}
		default:
			break;
		}
	}

	private void logoff() throws IOException {
		sendLogoffStatusCode();
		if (connection.isConnected()) {
			updateLastLoginDate();
			if (progressStatus != ProgressStatus.NONE) {
				Controller.CONTROLLER.addPcrProcessingUser(this);
			}
		}
		connection = Connection.CLOSED;
		System.out.println("User: " + userID + ", has logged off.");
	}

	public boolean login() throws IOException {
		String username = inputStream.readUTF();
		String password = inputStream.readUTF();

		if (username.equals(Config.ADMIN_USERNAME) && password.equals(Config.ADMIN_PASSWORD)) {
			System.out.println("Admin has logged in!");
			new Thread(new AdminController(socket)).start();
			initAdmin();
			return true;
		}

		userID = Database.findUser(username, password);

		if (userID == -1) {
			sendLoginErrorMessage();
		} else {
			UserController userController = Controller.CONTROLLER.checkUser(userID);
			if (userController == null) {
				confirmLogin();
				sendStatusExpired();
				setLastLoginDate();
			} else {
				Controller.CONTROLLER.removePcrProcessingUser(userController);
				userController.reset(socket);
				new Thread(userController).start();
				return true;
			}

		}
		return false;
	}

	private void confirmLogin() throws IOException {
		sendLoginConfirmed();
		connection = Connection.CONNECTED;
		System.out.println("User: " + userID + ", has successfully logged in.");
	}

	private void sendStatusExpired() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					if (connection.isConnected()) {
						sendExpiredMessage();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 5000l);
	}

	private void info() throws IOException {
		info(Database.getUserLoginDate(userID), lastSelfTestTime);
	}

	private void info(Date lastLoginTime, Date lastSelfTestTime) throws IOException {
		outputStream.writeInt(StatusCode.INFO.statusCode());
		outputStream.writeLong(lastLoginTime.getTime());

		if (lastSelfTestTime == null) {
			outputStream.writeLong(-1);
		} else {
			outputStream.writeLong(lastSelfTestTime.getTime());
		}
		outputStream.flush();
	}

	private void initAdmin() throws IOException {
		outputStream.writeInt(StatusCode.ADMIN.statusCode());
		outputStream.flush();
	}

	private void register() throws IOException {
		String username = inputStream.readUTF();
		String password = inputStream.readUTF();
		String firstName = inputStream.readUTF();
		String lastName = inputStream.readUTF();
		String email = inputStream.readUTF();
		Gender gender = Gender.valueOf(inputStream.readUTF());

		if (checkEmail(email)) {
			sendEmailErrorMessage();
		} else if (checkUsername(username)) {
			sendUsernameErrorMessage();
		} else {
			userID = Database.createUser(username, password, firstName, lastName, email, gender, UserStatus.UNDEFINED,
					new GregorianCalendar().getTime());
			sendRegistrationConfirmed();
			setLastLoginDate();
			connection = Connection.CONNECTED;
		}
	}

	private boolean checkEmail(String email) {
		return Database.checkEmail(email);
	}

	private boolean checkUsername(String username) {
		return Database.checkUsername(username);
	}

	// === sending data === //
	private void sendEmailErrorMessage() throws IOException {
		outputStream.writeInt(StatusCode.REGISTER.statusCode());
		outputStream.writeInt(MessageCode.REGISTER_CANCLE.messageCode());
		outputStream.writeUTF(Config.EMAIL_ERROR);
		outputStream.flush();
	}

	private void sendUsernameErrorMessage() throws IOException {
		outputStream.writeInt(StatusCode.REGISTER.statusCode());
		outputStream.writeInt(MessageCode.REGISTER_CANCLE.messageCode());
		outputStream.writeUTF(Config.USERNAME_ERROR);
		outputStream.flush();
	}

	private void sendRegistrationConfirmed() throws IOException {
		outputStream.writeInt(StatusCode.REGISTER.statusCode());
		outputStream.writeInt(MessageCode.REGISTER_OK.messageCode());
		outputStream.writeUTF(Config.REGISTRATION_OK);
		outputStream.flush();
	}

	private void sendLoginErrorMessage() throws IOException {
		outputStream.writeInt(StatusCode.LOGIN.statusCode());
		outputStream.writeInt(MessageCode.LOGIN_CANCLE.messageCode());
		outputStream.writeUTF(Config.LOGIN_ERROR);
		outputStream.flush();
	}

	private void sendLoginConfirmed() throws IOException {
		outputStream.writeInt(StatusCode.LOGIN.statusCode());
		outputStream.writeInt(MessageCode.LOGIN_OK.messageCode());
		outputStream.writeUTF(Config.LOGIN_OK);
		outputStream.flush();
	}

	private void sendLogoffStatusCode() throws IOException {
		outputStream.writeInt(StatusCode.LOGOFF.statusCode());
		outputStream.flush();
	}

	private void sendTests() throws IOException {
		ArrayList<Test> tests = Database.loadTests(userID);
		for (Test test : tests) {
			sendTest(test);
		}
		setLastTestTime(tests);
	}

	private void sendTest(Test test) throws IOException {
		outputStream.writeInt(StatusCode.TESTS.statusCode());
		outputStream.writeInt(test.getId());
		outputStream.writeUTF(test.getTestType().name());
		outputStream.writeUTF(test.getTestResult().name());
		outputStream.writeLong(test.getDate().getTime());
		outputStream.flush();
	}

	private void setLastTestTime(ArrayList<Test> tests) {
		if (tests.isEmpty()) {
			lastSelfTestTime = null;
			lastFastTestTime = null;
			lastPcrTestTime = null;
		} else {
			lastSelfTestTime = getLastTestTime(TestType.SELF);
			lastFastTestTime = getLastTestTime(TestType.FAST);
			lastPcrTestTime = getLastTestTime(TestType.PCR);
		}
	}

	private Date getLastTestTime(TestType testType) {
		return Database.getTestTime(testType, userID);
	}

	private void sendTestResponseNO(String message) throws IOException {
		outputStream.writeInt(StatusCode.TEST_REQUEST.statusCode());
		outputStream.writeInt(MessageCode.TEST_NO.messageCode());
		outputStream.writeUTF(message);
		outputStream.flush();
	}

	private boolean timeElapsed(Date date) {
		long difference = (new GregorianCalendar()).getTime().getTime() - date.getTime();
		return difference / Config.ONE_DAY_MILIS >= 1 ? true : false;
	}

	private Test createTest(TestType testType) {
		return Database.createTest(testType, Utils.getTesResult(), new GregorianCalendar().getTime(), userID);
	}

	private Test createTest(TestType testType, TestResult testResult) {
		return Database.createTest(testType, testResult, new GregorianCalendar().getTime(), userID);
	}

	private int result() throws IOException {
		return inputStream.readInt();
	}

	private void sendTestResultMessage(TestType testType, TestResult testResult) throws IOException {
		outputStream.writeInt(StatusCode.TESTING.statusCode());
		outputStream.writeUTF(
				testType.name() + (testResult == TestResult.POSITIVE ? Config.TEST_POSITIVE : Config.TEST_NEGATIVE));
		outputStream.flush();
	}

	private void sendSelfResultMessage(MessageCode messageCode, String message) throws IOException {
		outputStream.writeInt(StatusCode.TEST_REQUEST.statusCode());
		outputStream.writeInt(messageCode.messageCode());
		outputStream.writeUTF(message);
		outputStream.flush();
	}

	private void queuingPCR() throws IOException {
		progressStatus = ProgressStatus.QUEUING;

		if (connection.isConnected()) {
			updateProgressStatus(ProgressStatus.QUEUING);
		}

		Timer timer = new Timer(false);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					sendingPCR();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, Config.QUEUING_DURATION);
	}

	private void sendingPCR() throws IOException {
		progressStatus = ProgressStatus.SENDING;

		if (connection.isConnected()) {
			updateProgressStatus(ProgressStatus.SENDING);
		}

		Timer timer = new Timer(false);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					processingPCR1();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, Config.SENDING_DURATION);
	}

	private void processingPCR1() throws IOException {
		progressStatus = ProgressStatus.PROCESSING_1;

		if (connection.isConnected()) {
			updateProgressStatus(ProgressStatus.PROCESSING_1);
		}

		Timer timer = new Timer(false);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					processingPCR2();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, Config.PROCESSING_DURATION);
	}

	private void processingPCR2() throws IOException {
		progressStatus = ProgressStatus.PROCESSING_2;

		if (connection.isConnected()) {
			updateProgressStatus(ProgressStatus.PROCESSING_2);
		}

		Timer timer = new Timer(false);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					donePCR();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, Config.PROCESSING_DURATION);
	}

	private void donePCR() throws IOException {
		progressStatus = ProgressStatus.DONE;

		if (connection.isConnected()) {
			updateProgressStatus(ProgressStatus.DONE);
		}

		Timer timer = new Timer(false);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					Test test = createTest(TestType.PCR);

					if (connection.isConnected()) {
						sendTest(test);
						sendTestResultMessage(test.getTestType(), test.getTestResult());
						updateProgressStatus(ProgressStatus.NONE);
					} else {
						removePcrProcessingUser();
					}

					lastPcrTestTime = test.getDate();

					Database.setUserStatus(UserStatus.valueOf(test.getTestResult().name()), userID);

					if (test.getTestResult() == TestResult.POSITIVE) {
						Database.addNewPositive(userID);
					} else {
						Database.deleteNewPositive(userID);
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					progressStatus = ProgressStatus.NONE;
				}
			}
		}, Config.DONE_DURATION);
	}

	private void updateProgressStatus(ProgressStatus progressStatus) throws IOException {
		outputStream.writeInt(StatusCode.PROGRESS.statusCode());
		outputStream.writeDouble(progressStatus.progress());
		outputStream.flush();
	}

	private void sendExpiredMessage() throws IOException {
		if (Database.checkUserStatus(userID)) {
			outputStream.writeInt(StatusCode.EXPIRED_SURVEILLANCE.statusCode());
			outputStream.writeUTF(Config.EXPIRED_SURVEILLANCE);
			outputStream.flush();
		}
	}

	private TestType testType() throws IOException {
		return Utils.testType(inputStream.readInt());
	}

	private void removePcrProcessingUser() {
		Controller.CONTROLLER.removePcrProcessingUser(this);
	}

	private void setLastLoginDate() {
		this.lastLoginDate = new GregorianCalendar().getTime();
	}

	private void updateLastLoginDate() {
		Database.updateUserLoginDate(userID, lastLoginDate);
	}

	public void reset(Socket socket) throws IOException {
		this.socket = socket;
		initStreams();
		confirmLogin();
		updateProgressStatus(progressStatus);
	}

	public int getUserID() {
		return userID;
	}
}
