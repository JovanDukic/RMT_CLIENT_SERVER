package layout;

import config.Config;
import interfaces.LoginInterface;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import utils.Utils;

public class LoginPanel extends VBox {

	private TextField username;
	private PasswordField password;
	private Label register;
	private Button submit;

	private LoginInterface loginInterface;

	public LoginPanel(int vGap) {
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
		register = new Label("registracija korisnika");
		submit = new Button("PRIJAVI SE");
	}

	private void initProps() {
		username.setPrefColumnCount(15);
		password.setPrefColumnCount(15);

		submit.setPrefWidth(160);
		submit.setPrefHeight(40);
		submit.setFont(Utils.dialogFont(13));

		register.setFont(Utils.dialogFont(13));
		register.setTextFill(Color.DARKBLUE);
		register.setUnderline(true);
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
					submit.requestFocus();
				}
			}

		});

		submit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				String name = username.getText();
				String pass = password.getText();

				if (name.isEmpty() || pass.isEmpty()) {
					Utils.makeAlert(AlertType.WARNING, "Input alert", Config.INPUT_WARNING).show();
				} else {
					loginInterface.login(name, pass);
				}
			}
		});

		register.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				loginInterface.changeScreen();
				clearForm();
			}
		});
		register.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				setCursor(Cursor.HAND);
			}

		});
		register.setOnMouseExited(new EventHandler<MouseEvent>() {

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
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10));
		gridPane.setAlignment(Pos.CENTER);

		Label label = new Label("Prijava korisnika");
		label.setFont(Utils.font(FontWeight.BOLD, 25));
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(label);
		gridPane.add(box, 0, 0, 2, 1);

		Label userLabel = new Label("Korisničko ime:");
		userLabel.setFont(Utils.dialogFont(13));

		gridPane.add(userLabel, 0, 1);
		gridPane.add(username, 1, 1);

		Label passLabel = new Label("Lozinka:");
		passLabel.setFont(Utils.dialogFont(13));

		gridPane.add(passLabel, 0, 2);
		gridPane.add(password, 1, 2);

		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(submit, register);

		setAlignment(Pos.CENTER);
		getChildren().addAll(gridPane, vBox);

	}

	private void clearForm() {
		username.clear();
		password.clear();
	}

	public void setLogin(LoginInterface loginInterface) {
		this.loginInterface = loginInterface;
	}

}
