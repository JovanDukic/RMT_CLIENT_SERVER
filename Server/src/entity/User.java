package entity;

import enums.Gender;
import enums.UserStatus;

public class User {

	private int id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private Gender gender;
	private UserStatus userStatus;

	public User(int id, String username, String password, String firstName, String lastName, String emial,
			Gender gender, UserStatus userStatus) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = emial;
		this.gender = gender;
		this.userStatus = userStatus;
	}

	public User(int id, String firstName, String lastName, String email, UserStatus userStatus) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userStatus = userStatus;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public Gender getGender() {
		return gender;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

}
