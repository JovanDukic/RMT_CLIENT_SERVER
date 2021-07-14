package config;

public class Config {

	public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String DATABASE_URI = "jdbc:mysql://localhost:3306";

	public static final String USERNAME = "root";
	public static final String PASSWORD = "root";

	public static final String ADMIN_USERNAME = "admin";
	public static final String ADMIN_PASSWORD = "admin";

	public static final String CREATE_DATABASE = "create database rmt";
	public static final String USE_DATABASE = "use rmt";

	public static final String CREATE_ADMIN = "insert into users(username, password) values(?, ?);";

	public static final String CREATE_USERS_TABLE = "create table users(" + "id int auto_increment primary key,"
			+ "username varchar(100)," + "password varchar(100)," + "first_name varchar(100),"
			+ "last_name varchar(100)," + "email varchar(100)," + "gender varchar(100)," + "user_status varchar(100),"
			+ "login_date timestamp);";

	public static final String CREATE_NEW_POSITIVE_TABLE = "create table new_positive(id int, foreign key(id) references users(id));";

	public static final String DELETE_ALL_NEW_POSITIVE = "delete from new_positive;";
	public static final String ADD_NEW_POSITIVE = "insert into new_positive(id) values (?);";
	public static final String DELETE_NEW_POSITIVE = "delete from new_positive where id = ?;";
	public static final String CHECK_NEW_POSITIVE = "select * from new_positive where id = ?";
	public static final String GET_NEW_POSITIVE = "select u.id, u.first_name, u.last_name, u.email, u.user_status from users u join new_positive n on n.id = u.id;";

	public static final String CREATE_TESTS_TABLE = "create table tests(" + "id int auto_increment primary key,"
			+ "test_type varchar(100)," + "test_result varchar(100)," + "date timestamp," + "user_id int);";

	public static final String CREATE_USER = "insert into users(username, password, first_name, last_name, email, gender, user_status, login_date)"
			+ "values (?, ?, ?, ?, ?, ?, ?, ?);";

	public static final String CREATE_TEST = "insert into tests(test_type, test_result, date, user_id)"
			+ "values (?, ?, ?, ?);";

	public static final String ALL_USERS = "select id, first_name, last_name, email, user_status from users where id != 1 and user_status != \"UNDEFINED\";";
	public static final String POSITIVE = "select id, first_name, last_name, email, user_status from users where id != 1 and user_status = \"POSITIVE\";";
	public static final String NEGATIVE = "select id, first_name, last_name, email, user_status from users where id != 1 and user_status = \"NEGATIVE\";";
	public static final String UNDER_SURVEILANCE = "select id, first_name, last_name, email, user_status from users where id != 1 and user_status = \"UNDER_SURVEILLANCE\";";
	public static final String UNDEFINED = "select id, first_name, last_name, email, user_status from users where id != 1 and user_status = \"UNDEFINED\";";

	public static final String UNDER_SURVEILLANCE_USERS = "select id, first_name, last_name, email, user_status from users where user_status = \"UNDER_SURVEILLANCE\";";
	public static final String SELF_TEST = "select date from tests where user_id = ? and test_type = \"SELF\" and test_result = \"NEGATIVE\" order by date desc limit 1;";

	public static final String USER_STATUS = "select user_status from users where id = ?;";

	public static final String FIND_USER = "select id from users where username = ? and password = ?;";
	public static final String TEST_TIME = "select date from tests where user_id = ? and test_type = ? order by date desc limit 1";

	public static final String LOAD_USER_TESTS = "select * from tests where user_id = ?;";
	public static final String LOGIN_DATE = "select login_date from users where id = ?";

	public static final String CHECK_EMAIL = "select * from users where email = ?;";
	public static final String CHECK_USERNAME = "select * from users where username = ?;";

	public static final String SET_USER_STATUS = "update users set user_status = ? where id = ?;";
	public static final String SET_LOGIN_DATE = "update users set login_date = ? where id = ?";

	public static final String REGISTRATION_OK = "Uspešno ste se registrovali!";
	public static final String EMAIL_ERROR = "Uneti e-mail već postoji!";
	public static final String USERNAME_ERROR = "Korisničko ime već postoji!";

	public static final String LOGIN_OK = "Uspešno ste se prijavili!";
	public static final String LOGIN_ERROR = "Pogrešno korisničko ime ili lozinka!";

	public static final String SELF_NO = "Nije prošao jedan dan od poslednjeg testa samoprocene!";
	public static final String FAST_NO = "Nije prošao jedan dan od poslednjeg brzog testa!";
	public static final String PCR_NO = "Nije prošao jedan dan od poslednjeg PCR testa!";

	public static final String TEST_NO_NEED = "Nemate dovoljno simptoma korona virusa!";
	public static final String TEST_POSITIVE = " - pozitivni ste";
	public static final String TEST_NEGATIVE = " - negativni ste";
	public static final String TEST_OK = "Imate dva ili više potvrdnih odgovora, sledi nastavak procedure.";
	public static final String EXPIRED_SURVEILLANCE = "Potrebno je da ponovo odradite test samoprocene!";

	public static final float ONE_DAY_MILIS = 1000 * 10f;
	public static final long TEST_AGAIN_TIMER = 1000 * 10l;

	public static final long QUEUING_DURATION = 10000l;
	public static final long SENDING_DURATION = 10000l;
	public static final long PROCESSING_DURATION = 10000l;
	public static final long DONE_DURATION = 2000l;

	private Config() {

	}

}
