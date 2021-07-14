package app;

import java.io.IOException;
import java.net.ServerSocket;

import controller.Controller;
import controller.UserController;

public class ServerApp {

	public static void main(String[] args) {
		Controller.CONTROLLER.initDatabase();
		while (true) {
			try (ServerSocket serverSocket = new ServerSocket(8888)) {
				new Thread(new UserController(serverSocket.accept())).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
