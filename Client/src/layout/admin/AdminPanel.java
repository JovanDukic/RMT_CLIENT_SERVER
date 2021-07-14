package layout.admin;

import java.util.Optional;

import config.Config;
import controller.AdminController;
import enums.StatusCode;
import interfaces.RequestSender;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Utils;

public class AdminPanel extends BorderPane {

	private AdminTable adminTable;
	private AdminToolbar adminToolbar;

	private AdminController adminController;

	private ToggleGroup toggleGroup;

	private Stage primaryStage;
	private Stage stage;
	private Scene scene;
	private WaitingDialog waitingDialog;

	private RequestSender requestSender;

	public AdminPanel(AdminController adminController, Stage primaryStage) {
		this.adminController = adminController;
		this.primaryStage = primaryStage;
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initActions();
		initProps();
	}

	private void initComponents() {
		adminTable = new AdminTable();
		adminToolbar = new AdminToolbar(50);
		waitingDialog = new WaitingDialog(10);
		toggleGroup = new ToggleGroup();

		stage = new Stage();
		scene = new Scene(waitingDialog, Config.DIALOG_WIDTH, Config.DIALOG_HEIGHT);
	}

	private void initProps() {
		stage.setScene(scene);
		stage.initOwner(primaryStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setTitle("Loading");
	}

	private void initActions() {
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
				requestSender.sendRequest((StatusCode) toggleGroup.getSelectedToggle().getUserData());
			};
		});
	}

	private void initLayout() {
		setCenter(adminTable);
		setTop(new VBox(getMenuBar(), adminToolbar));
	}

	private MenuBar getMenuBar() {
		MenuBar menuBar = new MenuBar();

		Menu file = new Menu("File");
		Menu view = new Menu("View");
		Menu data = new Menu("Podaci");

		MenuItem exit = new MenuItem("Exit");
		MenuItem refresh = new MenuItem("Osveži");

		exit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				Optional<ButtonType> result = Utils.makeCustomAlert("Exit", Config.EXIT).showAndWait();
				if (result.get().getButtonData() == ButtonData.OK_DONE) {
					adminController.logoffRequest();
				}
			}
		});

		refresh.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				requestSender.sendRequest((StatusCode) toggleGroup.getSelectedToggle().getUserData());
			}
		});

		RadioMenuItem all = new RadioMenuItem("svi testirani");
		RadioMenuItem positive = new RadioMenuItem("pozitivni");
		RadioMenuItem negative = new RadioMenuItem("negativni");
		RadioMenuItem underSurveillance = new RadioMenuItem("pod nadzorom");
		RadioMenuItem undefined = new RadioMenuItem("nedefinisani");
		RadioMenuItem newPositive = new RadioMenuItem("novi pozitivni");
		RadioMenuItem expiredSurveillance = new RadioMenuItem("istekao nadzor");

		all.setUserData(StatusCode.USERS);
		positive.setUserData(StatusCode.POSITIVE);
		negative.setUserData(StatusCode.NEGATIVE);
		underSurveillance.setUserData(StatusCode.UNDER_SURVEILLANCE);
		undefined.setUserData(StatusCode.UNDEFINED);
		newPositive.setUserData(StatusCode.NEW_POSITIVE);
		expiredSurveillance.setUserData(StatusCode.EXPIRED_SURVEILLANCE);

		all.setToggleGroup(toggleGroup);
		positive.setToggleGroup(toggleGroup);
		negative.setToggleGroup(toggleGroup);
		underSurveillance.setToggleGroup(toggleGroup);
		undefined.setToggleGroup(toggleGroup);
		newPositive.setToggleGroup(toggleGroup);
		expiredSurveillance.setToggleGroup(toggleGroup);

		file.getItems().add(exit);
		view.getItems().addAll(data, refresh);
		data.getItems().addAll(all, positive, negative, underSurveillance, undefined, newPositive, expiredSurveillance);

		all.setSelected(true);

		menuBar.getMenus().addAll(file, view);

		return menuBar;
	}

	private String matchType() {
		StatusCode statusCode = (StatusCode) toggleGroup.getSelectedToggle().getUserData();
		switch (statusCode) {
		case USERS: {
			return "Svi testirani korisnici";
		}
		case POSITIVE: {
			return "Pozitivni";
		}
		case NEGATIVE: {
			return "Negativni";
		}
		case UNDER_SURVEILLANCE: {
			return "Pod nadzorom";
		}
		case UNDEFINED: {
			return "Nedefinisani";
		}
		case NEW_POSITIVE: {
			return "Novi pozitivni";
		}
		case EXPIRED_SURVEILLANCE: {
			return "Korisnici sa isteklim nadozorm";
		}
		default:
			return "Error";
		}
	}

	public void showWaitingDialog() {
		stage.show();
	}

	public void hideWaitingDialog() {
		stage.hide();
	}

	public void setRequestSender(RequestSender requestSender) {
		this.requestSender = requestSender;
	}

	public void updateMatches(int matches) {
		adminToolbar.updateMatches(matches, matchType());
	}

}
