package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import config.Config;
import data.Database;
import entity.User;
import enums.StatusCode;
import utils.Utils;

public class AdminController implements Runnable {

	private Socket socket;

	private DataOutputStream outputStream;
	private DataInputStream inputStream;

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
					logoff();
					System.out.println("Admin has logged off!");
					Database.deleteAllNewPositive();
					return;
				}
				case USERS: {
					sendUsers();
					break;
				}
				case POSITIVE: {
					sendPositive();
					break;
				}
				case NEGATIVE: {
					sendNegative();
					break;
				}
				case UNDER_SURVEILLANCE: {
					sendUnderSurveillance();
					break;
				}
				case UNDEFINED: {
					sendUndefined();
					break;
				}
				case NEW_POSITIVE: {
					sendNewPositive();
					break;
				}
				case EXPIRED_SURVEILLANCE: {
					sendExpiredSurveillance();
					break;
				}
				default:
					break;
				}
			}
		} catch (IOException e) {
			System.err.println("Broken connection, user: 1");
		}
	}

	private void logoff() throws IOException {
		outputStream.writeInt(StatusCode.LOGOFF.statusCode());
		outputStream.flush();
	}

	private void sendUsers() throws IOException {
		ArrayList<User> users = Database.getUsers(Config.ALL_USERS);
		for (User user : users) {
			sendUser(user);
		}
		sendDone(users.size());
	}

	private void sendPositive() throws IOException {
		ArrayList<User> users = Database.getUsers(Config.POSITIVE);
		for (User user : users) {
			sendUser(user);
		}
		sendDone(users.size());
	}

	private void sendNegative() throws IOException {
		ArrayList<User> users = Database.getUsers(Config.NEGATIVE);
		for (User user : users) {
			sendUser(user);
		}
		sendDone(users.size());
	}

	private void sendUnderSurveillance() throws IOException {
		ArrayList<User> users = Database.getUsers(Config.UNDER_SURVEILANCE);
		for (User user : users) {
			sendUser(user);
		}
		sendDone(users.size());
	}

	private void sendUndefined() throws IOException {
		ArrayList<User> users = Database.getUsers(Config.UNDEFINED);
		for (User user : users) {
			sendUser(user);
		}
		sendDone(users.size());
	}

	private void sendNewPositive() throws IOException {
		ArrayList<User> users = Database.getUsers(Config.GET_NEW_POSITIVE);
		for (User user : users) {
			sendUser(user);
		}
		sendDone(users.size());
	}

	private void sendExpiredSurveillance() throws IOException {
		ArrayList<User> users = Database.expiredSurveillance();
		for (User user : users) {
			sendUser(user);
		}
		sendDone(users.size());
	}

	private void sendUser(User user) throws IOException {
		outputStream.writeInt(StatusCode.USERS.statusCode());
		outputStream.writeInt(user.getId());
		outputStream.writeUTF(user.getFirstName());
		outputStream.writeUTF(user.getLastName());
		outputStream.writeUTF(user.getEmail());
		outputStream.writeUTF(user.getUserStatus().name());
	}

	private void sendDone(int matches) throws IOException {
		outputStream.writeInt(StatusCode.DONE.statusCode());
		outputStream.writeInt(matches);
		outputStream.flush();
	}

}
