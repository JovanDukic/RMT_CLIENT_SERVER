package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import data.Database;

public class Controller {

	public static final Controller CONTROLLER = new Controller();

	private ArrayList<UserController> pcrProccesingUsers = new ArrayList<>();

	public ArrayList<UserController> getPcrProccesingUsers() {
		return pcrProccesingUsers;
	}

	public void addPcrProcessingUser(UserController user) {
		pcrProccesingUsers.add(user);
	}

	public void removePcrProcessingUser(UserController user) {
		pcrProccesingUsers.remove(user);
	}

	public UserController checkUser(int userID) {
		for (UserController user : pcrProccesingUsers) {
			if (user.getUserID() == userID) {
				return user;
			}
		}
		return null;
	}

	public void initDatabase() {
		System.out.println("Starting server...");
		try {
			File file = new File("ServerStatus");
			if (file.exists()) {
				System.out.println("Connecting to database...");
				Database.connectToMySQL();
				Database.useDatabase();
				System.out.println("Connected to database.");
				return;
			}
			file.createNewFile();
			Database.connectToMySQL();

			System.out.println("Creating database...");
			Database.createDatabase();
			System.out.println("Database created.");

			System.out.println("Connecting to database...");
			Database.useDatabase();
			System.out.println("Connected to database.");

			System.out.println("Creating tables...");
			Database.createTables();
			System.out.println("Tables have been successfilly created.");

			System.out.println("Creating admin...");
			Database.createAdmin();
			System.out.println("Admin created.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Controller() {

	}

}
