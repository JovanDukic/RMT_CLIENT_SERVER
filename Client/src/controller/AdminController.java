package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import entity.User;
import enums.StatusCode;
import enums.UserStatus;
import interfaces.AdminInterface;
import javafx.application.Platform;
import utils.Utils;

public class AdminController implements Runnable {

	private Socket socket;

	private DataOutputStream outputStream;
	private DataInputStream inputStream;

	private AdminInterface adminInterface;

	public AdminController(Socket socket) {
		this.socket = socket;
		init();
	}

	private void init() {
		initStreams();
	}

	private void initStreams() {
		try {
			outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				StatusCode statusCode = Utils.statusCode(inputStream.readInt());
				switch (statusCode) {
				case LOGOFF: {
					Platform.exit();
					return;
				}
				case USERS: {
					addUser();
					break;
				}
				case DONE: {
					Controller.CONTROLLER.swap();
					Controller.CONTROLLER.clearTemp();
					adminInterface.done(inputStream.readInt());
					break;
				}
				default:
					break;
				}
			}
		} catch (IOException e) {
			adminInterface.serverError();
		}
	}

	private void addUser() throws IOException {
		int id = inputStream.readInt();
		String firstName = inputStream.readUTF();
		String lastName = inputStream.readUTF();
		String email = inputStream.readUTF();
		UserStatus userStatus = UserStatus.valueOf(inputStream.readUTF());
		Controller.CONTROLLER.addTemp(new User(id, firstName, lastName, email, userStatus));
	}

	public void sendUsersRequser(StatusCode statusCode) {
		try {
			outputStream.writeInt(statusCode.statusCode());
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setAdminInterface(AdminInterface adminInterface) {
		this.adminInterface = adminInterface;
	}
	
	public void logoffRequest() {
		try {
			outputStream.writeInt(StatusCode.LOGOFF.statusCode());
			outputStream.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
