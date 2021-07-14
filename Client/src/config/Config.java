package config;

public class Config {

	public static final int SCREEN_WIDTH = 600;
	public static final int SCREEN_HEIGHT = 400;

	public static final int DIALOG_WIDTH = 300;
	public static final int DIALOG_HEIGHT = 200;

	public static final int MEDIUM_DIALOG_WIDTH = 420;
	public static final int MEDIUN_DIALOG_HEIGHT = 200;

	public static final String QUESTION_1 = "Da li ste putovali van Srbije u okviru 14 dana od početka simptoma?";
	public static final String QUESTION_2 = "Da li ste bili u kontaktu sa zaraženim osobama?";
	public static final String QUESTION_3 = "Da li imate od simptoma:";

	public static final String SUB_1 = "1) Povišena temperatura";
	public static final String SUB_2 = "2) Kašalj";
	public static final String SUB_3 = "3) Opšta slabost";
	public static final String SUB_4 = "4) Gubitak čula mirisa";
	public static final String SUB_5 = "5) Gubitak/promena čula ukusa";

	public static final String YES = "DA";
	public static final String NO = "NE";

	public static final String EXIT = "Da li ste sigurni da želite da napustite program?";
	public static final String INPUT_WARNING = "Neophodno je da popunite sva polja!";
	public static final String EMAIL_WARNING = "Uneti e-mail je neprihvatljiv!";
	public static final String SERVER_AFK = "Server je trenutno nedostupdan!";
	public static final String TEST_CHOOSER = "Potreno je da izaberete bar jedan način testiranja!";
	public static final String LAST_SELF_TEST_ERROR = "Niste se još testirali!";
	public static final String SERVER_ERROR = "Došlo je do prekida veze sa serverom, program će se ugasiti!";

	public static final String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
	public static final String BORDER_STYLE = "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
			+ "-fx-border-radius: 5;" + "-fx-border-insets: 20";

	private Config() {

	}

}
