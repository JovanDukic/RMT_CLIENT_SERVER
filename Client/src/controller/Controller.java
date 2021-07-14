package controller;

import entity.Test;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Controller {

	public static final Controller CONTROLLER = new Controller();

	private ObservableList<Test> tests = FXCollections.observableArrayList();

	private ObservableList<User> users = FXCollections.observableArrayList();
	private ObservableList<User> temp = FXCollections.observableArrayList();

	// === tests === //
	public void addTest(Test test) {
		tests.add(test);
	}

	public ObservableList<Test> getTests() {
		return tests;
	}

	// === users === //
	public void addUser(User user) {
		users.add(user);
	}

	public ObservableList<User> getusUsers() {
		return users;
	}

	// === temp === //
	public void addTemp(User user) {
		temp.add(user);
	}

	public void swap() {
		users.clear();
		users.addAll(temp);
	}

	public void clearTemp() {
		temp.clear();
	}

	private Controller() {

	}

}
