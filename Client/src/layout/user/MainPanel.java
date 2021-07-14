package layout.user;

import java.util.Optional;

import config.Config;
import controller.UserController;
import enums.ProgressStatus;
import interfaces.ScreenCloser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.Utils;

public class MainPanel extends VBox {

	private UserController userController;

	private TabbedPanel tabbedPanel;

	private Stage primaryStage;

	private Stage stage;
	private Scene scene;
	private ProgressDialog progressDialog;

	private Stage stage2;
	private Scene scene2;
	private InfoPanel infoPanel;

	public MainPanel(UserController userController, Stage primaryStage) {
		this.userController = userController;
		this.primaryStage = primaryStage;
		init();
	}

	private void init() {
		initComponents();
		initActions();
		initProps();
		initLayout();
	}

	private void initComponents() {
		tabbedPanel = new TabbedPanel(userController);
		progressDialog = new ProgressDialog(10);
		infoPanel = new InfoPanel(10);

		stage = new Stage();
		scene = new Scene(progressDialog, Config.DIALOG_WIDTH, Config.DIALOG_HEIGHT);

		stage2 = new Stage();
		scene2 = new Scene(infoPanel, Config.MEDIUM_DIALOG_WIDTH, Config.MEDIUN_DIALOG_HEIGHT);
	}

	private void initProps() {
		stage.setTitle("PCR Status");
		stage.setScene(scene);
		stage.initOwner(primaryStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);

		stage2.setTitle("Korisnički podaci");
		stage2.setScene(scene2);
		stage2.initOwner(primaryStage);
		stage2.initModality(Modality.WINDOW_MODAL);
		stage2.setResizable(false);
	}

	private void initActions() {
		progressDialog.setScreenCloser(new ScreenCloser() {

			@Override
			public void closeScreen() {
				stage.hide();
			}
		});
	}

	private void initLayout() {
		getChildren().addAll(initMenu(), tabbedPanel);
	}

	private MenuBar initMenu() {
		MenuBar menuBar = new MenuBar();

		Menu file = new Menu("File");
		Menu info = new Menu("Info");

		MenuItem exit = new MenuItem("Exit");
		MenuItem userData = new MenuItem("Korisnički podaci");
		MenuItem pcrStatus = new MenuItem("PCR Status");

		exit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				Optional<ButtonType> result = Utils.makeCustomAlert("Exit", Config.EXIT).showAndWait();
				if (result.get().getButtonData() == ButtonData.OK_DONE) {
					userController.logoff();
					Platform.exit();
				}
			}
		});

		userData.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				showInfoPanel();
			}
		});

		pcrStatus.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				showStatusDialog();
			}
		});

		file.getItems().add(exit);
		info.getItems().addAll(userData, pcrStatus);

		menuBar.getMenus().addAll(file, info);

		return menuBar;
	}

	private void showStatusDialog() {
		stage.show();
	}

	private void showInfoPanel() {
		stage2.show();
	}

	public void showTestChooserPane() {
		Stage stage = new Stage();

		TestChooserPane chooserPane = new TestChooserPane(10, userController);
		chooserPane.setScreenCloser(new ScreenCloser() {

			@Override
			public void closeScreen() {
				stage.close();
			}
		});

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent e) {
				e.consume();
			}
		});

		stage.setTitle("Izbor testa");
		stage.initOwner(primaryStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		stage.setScene(new Scene(chooserPane, Config.DIALOG_WIDTH, Config.DIALOG_HEIGHT));
		stage.show();
	}

	public void updateProgressStatus(ProgressStatus progressStatus, double progress) {
		progressDialog.updateProgress(progressStatus, progress);
	}

	public void setInfo(String timestamp, String testTime) {
		infoPanel.setTimestamp(timestamp);
		infoPanel.setTestTime(testTime);
	}

}
