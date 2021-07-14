package layout;

import config.Config;
import enums.Gender;
import interfaces.RegisterInterface;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import utils.Utils;

public class RegistrationPanel extends VBox {

	private TextField username;
	private PasswordField password;
	private TextField firstName;
	private TextField lastName;
	private TextField emaill;
	private RadioButton male;
	private RadioButton female;
	private Button submit;
	private Label loginLabel;

	private ToggleGroup gender;

	private RegisterInterface registerInterface;

	public RegistrationPanel(int vGap) {
		super(vGap);
		init();
	}

	private void init() {
		initComponents();
		initProps();
		initActions();
		initLayout();
	}

	private void initComponents() {
		username = new TextField();
		password = new PasswordField();
		firstName = new TextField();
		lastName = new TextField();
		emaill = new TextField();
		male = new RadioButton("muško");
		female = new RadioButton("žensko");
		submit = new Button("REGISTRUJ SE");
		loginLabel = new Label("prijava korisnika");

		gender = new ToggleGroup();
	}

	private void initProps() {
		username.setPrefColumnCount(15);
		password.setPrefColumnCount(15);
		firstName.setPrefColumnCount(15);
		lastName.setPrefColumnCount(15);

		male.setToggleGroup(gender);
		female.setToggleGroup(gender);

		male.setUserData(Gender.MALE);
		female.setUserData(Gender.FEMALE);

		male.setFont(Utils.dialogFont(13));
		female.setFont(Utils.dialogFont(13));

		male.setSelected(true);

		submit.setFont(Utils.dialogFont(13));
		submit.setPrefWidth(160);
		submit.setPrefHeight(40);

		loginLabel.setUnderline(true);
		loginLabel.setTextFill(Color.DARKBLUE);
		loginLabel.setFont(Utils.dialogFont(13));
	}

	private void initActions() {
		username.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					password.requestFocus();
				}
			}

		});

		password.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					firstName.requestFocus();
				}
			}

		});

		firstName.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					lastName.requestFocus();
				}
			}

		});

		lastName.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					emaill.requestFocus();
				}
			}

		});

		submit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				String userName = username.getText();
				String passWord = password.getText();
				String firstname = firstName.getText();
				String lastname = lastName.getText();
				String mail = emaill.getText();
				Gender gen = (Gender) gender.getSelectedToggle().getUserData();

				if (checkInput(userName, passWord, firstname, lastname, mail)) {
					Utils.makeAlert(AlertType.WARNING, "Input alert", Config.INPUT_WARNING).showAndWait();
				} else if (!checkEmail(mail)) {
					Utils.makeAlert(AlertType.WARNING, "Email alert", Config.EMAIL_WARNING).showAndWait();
				} else {
					registerInterface.register(userName, passWord, firstname, lastname, mail, gen);
				}
			}
		});

		loginLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				registerInterface.changeScreen();
				clearForm();
			}

		});

		loginLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				setCursor(Cursor.HAND);
			}
		});

		loginLabel.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				setCursor(Cursor.DEFAULT);
			}
		});
	}

	private void initLayout() {
		GridPane gridPane = new GridPane();
		gridPane.setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 2;" + "-fx-border-radius: 5;"
				+ "-fx-border-insets: 0 120 0 120");
		gridPane.setPadding(new Insets(10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setAlignment(Pos.CENTER);

		Label label = new Label("Registracija korisnika");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(label);
		gridPane.add(box, 0, 0, 2, 1);

		Label label1 = new Label("Korisničko ime:");
		label1.setFont(Utils.font(FontWeight.BOLD, 13));
		gridPane.add(label1, 0, 1);
		gridPane.add(username, 1, 1);

		Label label2 = new Label("Lozinka:");
		label2.setFont(Utils.font(FontWeight.BOLD, 13));
		gridPane.add(label2, 0, 2);
		gridPane.add(password, 1, 2);

		Label label3 = new Label("Ime:");
		label3.setFont(Utils.font(FontWeight.BOLD, 13));
		gridPane.add(label3, 0, 3);
		gridPane.add(firstName, 1, 3);

		Label label4 = new Label("Prezime:");
		label4.setFont(Utils.font(FontWeight.BOLD, 13));
		gridPane.add(label4, 0, 4);
		gridPane.add(lastName, 1, 4);

		Label label5 = new Label("E-mail:");
		label5.setFont(Utils.font(FontWeight.BOLD, 13));
		gridPane.add(label5, 0, 5);
		gridPane.add(emaill, 1, 5);

		Label label6 = new Label("Pol:");
		label6.setFont(Utils.font(FontWeight.BOLD, 13));
		gridPane.add(label6, 0, 6);
		HBox hBox = new HBox(15);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.getChildren().addAll(male, female);
		gridPane.add(hBox, 1, 6);

		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(submit, loginLabel);

		setAlignment(Pos.CENTER);
		getChildren().addAll(gridPane, vBox);
	}

	private void clearForm() {
		username.clear();
		password.clear();
		firstName.clear();
		lastName.clear();
		emaill.clear();
		male.setSelected(true);
	}

	private boolean checkInput(String username, String password, String firstName, String lastName, String email) {
		if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
			return true;
		}
		return false;
	}

	private boolean checkEmail(String email) {
		return Utils.validateEmail(email);
	}

	public void setRegistrationInterface(RegisterInterface registerInterface) {
		this.registerInterface = registerInterface;
	}

}
