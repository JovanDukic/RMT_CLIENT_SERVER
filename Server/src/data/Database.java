package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import config.Config;
import entity.Test;
import entity.User;
import enums.Gender;
import enums.TestResult;
import enums.TestType;
import enums.UserStatus;

public class Database {

	private static Connection connection;

	public static void connectToMySQL() {
		try {
			Class.forName(Config.JDBC_DRIVER);
			connection = DriverManager.getConnection(Config.DATABASE_URI, Config.USERNAME, Config.PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createDatabase() {
		try {
			connection.createStatement().execute(Config.CREATE_DATABASE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void useDatabase() {
		try {
			connection.createStatement().execute(Config.USE_DATABASE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createTables() {
		try {
			connection.createStatement().execute(Config.CREATE_USERS_TABLE);
			connection.createStatement().execute(Config.CREATE_TESTS_TABLE);
			connection.createStatement().execute(Config.CREATE_NEW_POSITIVE_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createAdmin() {
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.CREATE_ADMIN)) {
			preparedStatement.setString(1, Config.ADMIN_USERNAME);
			preparedStatement.setString(2, Config.ADMIN_PASSWORD);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<User> getUsers(String query) {
		ArrayList<User> users = new ArrayList<User>();
		try {
			ResultSet rs = connection.createStatement().executeQuery(query);
			while (rs.next()) {
				users.add(getUser(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public static boolean checkUserStatus(int userID) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.USER_STATUS)) {
			preparedStatement.setInt(1, userID);
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();

			if (UserStatus.valueOf(rs.getString(1)) == UserStatus.UNDER_SURVEILLANCE) {
				PreparedStatement preparedStatement2 = connection.prepareStatement(Config.SELF_TEST);
				preparedStatement2.setInt(1, userID);

				ResultSet rs1 = preparedStatement2.executeQuery();
				rs1.next();

				Date date = new Date(rs1.getTimestamp(1).getTime());
				Date now = new GregorianCalendar().getTime();

				if (now.getTime() - date.getTime() >= Config.TEST_AGAIN_TIMER) {
					return true;
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<User> expiredSurveillance() {
		ArrayList<User> users = new ArrayList<User>();

		Date now = new GregorianCalendar().getTime();

		try {
			ResultSet rs = connection.createStatement().executeQuery(Config.UNDER_SURVEILLANCE_USERS);
			while (rs.next()) {
				PreparedStatement preparedStatement = connection.prepareStatement(Config.SELF_TEST);

				preparedStatement.setInt(1, rs.getInt(1));
				ResultSet rs1 = preparedStatement.executeQuery();
				rs1.next();

				Date date = new Date(rs1.getTimestamp(1).getTime());

				if (now.getTime() - date.getTime() >= Config.TEST_AGAIN_TIMER) {
					users.add(getUser(rs));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}

	private static User getUser(ResultSet rs) throws SQLException {
		int id = rs.getInt(1);
		String firstName = rs.getString(2);
		String lastName = rs.getString(3);
		String email = rs.getString(4);
		UserStatus userStatus = UserStatus.valueOf(rs.getString(5));
		return new User(id, firstName, lastName, email, userStatus);
	}

	public static int findUser(String username, String password) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.FIND_USER)) {
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			preparedStatement.execute();
			ResultSet rs = preparedStatement.getResultSet();
			return rs.next() ? rs.getInt(1) : -1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static int createUser(String username, String password, String firstName, String lastName, String email,
			Gender gender, UserStatus userStatus, Date date) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.CREATE_USER,
				Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, firstName);
			preparedStatement.setString(4, lastName);
			preparedStatement.setString(5, email);
			preparedStatement.setString(6, gender.name());
			preparedStatement.setString(7, userStatus.name());
			preparedStatement.setTimestamp(8, new Timestamp(date.getTime()));
			preparedStatement.execute();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			rs.next();
			int userID = rs.getInt(1);
			System.out.println("User with userID: " + userID + " has been successfully created!");
			return userID;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static Test createTest(TestType testType, TestResult testResult, Date date, int userID) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.CREATE_TEST,
				Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, testType.name());
			preparedStatement.setString(2, testResult.name());
			preparedStatement.setTimestamp(3, new Timestamp(date.getTime()));
			preparedStatement.setInt(4, userID);
			preparedStatement.execute();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			rs.next();
			return new Test(rs.getInt(1), testType, testResult, date);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static boolean checkNewPositive(int userID) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.CHECK_NEW_POSITIVE)) {
			preparedStatement.setInt(1, userID);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void addNewPositive(int userID) {
		if (checkNewPositive(userID)) {
			return;
		} else {
			try (PreparedStatement preparedStatement = connection.prepareStatement(Config.ADD_NEW_POSITIVE)) {
				preparedStatement.setInt(1, userID);
				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void deleteNewPositive(int userID) {
		if (checkNewPositive(userID)) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(Config.DELETE_NEW_POSITIVE)) {
				preparedStatement.setInt(1, userID);
				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void deleteAllNewPositive() {
		try {
			connection.createStatement().execute(Config.DELETE_ALL_NEW_POSITIVE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<User> getNewPositive() {
		ArrayList<User> users = new ArrayList<User>();
		try {
			ResultSet rs = connection.createStatement().executeQuery(Config.GET_NEW_POSITIVE);
			while (rs.next()) {
				users.add(getUser(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public static Date getTestTime(TestType testType, int userID) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.TEST_TIME)) {
			preparedStatement.setInt(1, userID);
			preparedStatement.setString(2, testType.name());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return new Date(rs.getTimestamp(1).getTime());
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getUserLoginDate(int userID) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.LOGIN_DATE)) {
			preparedStatement.setInt(1, userID);
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			return new Date(rs.getTimestamp(1).getTime());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void updateUserLoginDate(int userID, Date date) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.SET_LOGIN_DATE)) {
			preparedStatement.setTimestamp(1, new Timestamp(date.getTime()));
			preparedStatement.setInt(2, userID);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Test> loadTests(int userID) {
		ArrayList<Test> tests = new ArrayList<Test>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.LOAD_USER_TESTS)) {
			preparedStatement.setInt(1, userID);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				TestType testType = TestType.valueOf(rs.getString(2));
				TestResult testResult = TestResult.valueOf(rs.getString(3));
				Date date = new Date(rs.getTimestamp(4).getTime());
				tests.add(new Test(id, testType, testResult, date));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tests;
	}

	public static boolean checkUsername(String username) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.CHECK_USERNAME)) {
			preparedStatement.setString(1, username);
			ResultSet rs = preparedStatement.executeQuery();
			return rs.next() ? true : false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean checkEmail(String email) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.CHECK_EMAIL)) {
			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();
			return rs.next() ? true : false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void setUserStatus(UserStatus userStatus, int userID) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(Config.SET_USER_STATUS)) {
			preparedStatement.setString(1, userStatus.name());
			preparedStatement.setInt(2, userID);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Database() {

	}

}
