package entity;

import enums.UserStatus;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {

	private SimpleIntegerProperty id;
	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleStringProperty email;
	private SimpleObjectProperty<UserStatus> userStatus;

	public User(int id, String firstName, String lastName, String email, UserStatus userStatus) {
		this.id = new SimpleIntegerProperty(id);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.email = new SimpleStringProperty(email);
		this.userStatus = new SimpleObjectProperty<UserStatus>(userStatus);
	}

	public int getId() {
		return id.get();
	}

	public String getFirstName() {
		return firstName.get();
	}

	public String getLastName() {
		return lastName.get();
	}

	public String getEmail() {
		return email.get();
	}

	public UserStatus getUserStatus() {
		return userStatus.get();
	}

}
