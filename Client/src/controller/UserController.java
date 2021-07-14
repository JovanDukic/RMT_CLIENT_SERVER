package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import config.Config;
import entity.Test;
import enums.Gender;
import enums.MessageCode;
import enums.ProgressStatus;
import enums.StatusCode;
import enums.TestResult;
import enums.TestType;
import interfaces.ControllerInterface;
import javafx.application.Platform;
import utils.Utils;

public class UserController implements Runnable {

	private Socket socket;

	private DataOutputStream outputStream;
	private DataInputStream inputStream;

	private ControllerInterface controllerInterface;

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
				StatusCode statusCode = Utils.statusCode(readStatusCode());
				switch (statusCode) {
				case LOGOFF: {
					Platform.exit();
					return;
				}
				case LOGIN: {
					MessageCode messageCode = Utils.messageCode(readMessageCode());
					switch (messageCode) {
					case LOGIN_OK: {
						controllerInterface.loginOK(readMessage());
						break;
					}
					case LOGIN_CANCLE: {
						controllerInterface.loginCancle(readMessage());
						break;
					}
					default:
						break;
					}
					break;
				}
				case REGISTER: {
					MessageCode messageCode = Utils.messageCode(readMessageCode());
					switch (messageCode) {
					case REGISTER_OK: {
						controllerInterface.registerOK(readMessage());
						break;
					}
					case REGISTER_CANCLE: {
						controllerInterface.registerCancle(readMessage());
						break;
					}
					default:
						break;
					}
					break;
				}
				case TESTS: {
					createTest();
					break;
				}
				case TEST_REQUEST: {
					MessageCode messageCode = Utils.messageCode(readMessageCode());
					switch (messageCode) {
					case TEST_OK: {
						controllerInterface.testOK(readMessage());
						break;
					}
					case TEST_NO: {
						controllerInterface.testNO(readMessage());
						break;
					}
					default:
						break;
					}
					break;
				}
				case TESTING: {
					controllerInterface.testResultMessage(readMessage());
					break;
				}
				case PROGRESS: {
					updateProgress();
					break;
				}
				case INFO: {
					controllerInterface.setInfo(loginTime(), lastSelfTestTime());
					break;
				}
				case ADMIN: {
					controllerInterface.initAdmin(new AdminController(socket));
					return;
				}
				case EXPIRED_SURVEILLANCE: {
					controllerInterface.expiredSurveillance(readMessage());
					break;
				}
				default:
					break;
				}
			}
		} catch (IOException e) {
			controllerInterface.connectionError();
		}
	}

	// === receiving data === //
	private void updateProgress() throws IOException {
		double progress = readProgress();
		controllerInterface.updateProgressStatus(progressStatus(progress), progress);
	}

	private void createTest() throws IOException {
		int id = inputStream.readInt();
		TestType testType = TestType.valueOf(inputStream.readUTF());
		TestResult testResult = TestResult.valueOf(inputStream.readUTF());
		String date = new SimpleDateFormat("dd/MM/yyyy    hh:mm:ss").format((new Date(inputStream.readLong())));
		Controller.CONTROLLER.addTest(new Test(id, testType, testResult, date));
	}

	private String readMessage() throws IOException {
		return inputStream.readUTF();
	}

	private int readStatusCode() throws IOException {
		return inputStream.readInt();
	}

	private int readMessageCode() throws IOException {
		return inputStream.readInt();
	}

	private double readProgress() throws IOException {
		return inputStream.readDouble();
	}

	private ProgressStatus progressStatus(double progress) throws IOException {
		return Utils.progressStatus(progress);
	}

	private String lastSelfTestTime() throws IOException {
		long time = inputStream.readLong();
		if (time == -1) {
			return Config.LAST_SELF_TEST_ERROR;
		} else {
			return new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss").format(new Date(time));
		}
	}

	private String loginTime() throws IOException {
		return new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss").format(new Date(inputStream.readLong()));
	}

	// === sending data === //
	public void login(String username, String password) {
		try {
			outputStream.writeInt(StatusCode.LOGIN.statusCode());
			outputStream.writeUTF(username);
			outputStream.writeUTF(password);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void register(String username, String password, String firstName, String lastName, String email,
			Gender gender) {
		try {
			outputStream.writeInt(StatusCode.REGISTER.statusCode());
			outputStream.writeUTF(username);
			outputStream.writeUTF(password);
			outputStream.writeUTF(firstName);
			outputStream.writeUTF(lastName);
			outputStream.writeUTF(email);
			outputStream.writeUTF(gender.name());
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logoff() {
		try {
			outputStream.writeInt(StatusCode.LOGOFF.statusCode());
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendLoadTestRequest() {
		try {
			outputStream.writeInt(StatusCode.TESTS.statusCode());
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendTestRequest(int result) {
		try {
			outputStream.writeInt(StatusCode.TEST_REQUEST.statusCode());
			outputStream.writeInt(result);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendInfoRequest() {
		try {
			outputStream.writeInt(StatusCode.INFO.statusCode());
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testing(TestType testType) {
		try {
			outputStream.writeInt(StatusCode.TESTING.statusCode());
			outputStream.writeInt(testType.getType());
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setControllerInterface(ControllerInterface controllerInterface) {
		this.controllerInterface = controllerInterface;
	}

}
