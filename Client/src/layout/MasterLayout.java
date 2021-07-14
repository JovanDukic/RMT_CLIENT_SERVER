package layout;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

import config.Config;
import controller.AdminController;
import controller.UserController;
import enums.Gender;
import enums.ProgressStatus;
import enums.StatusCode;
import interfaces.AdminInterface;
import interfaces.ControllerInterface;
import interfaces.LoginInterface;
import interfaces.RegisterInterface;
import interfaces.RequestSender;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import layout.admin.AdminPanel;
import layout.user.MainPanel;
import utils.Utils;

public class MasterLayout {

	private LoginPanel loginPanel;
	private RegistrationPanel registrationPanel;
	private MainPanel mainPanel;
	private AdminPanel adminPanel;

	private Scene scene;
	private Stage primaryStage;

	private UserController userController;
	private AdminController adminController;

	private boolean flag1 = true;
	private boolean flag2 = true;

	public MasterLayout(Stage primaryStage) {
		super();
		this.primaryStage = primaryStage;
		init();
	}

	private void init() {
		initComponents();
		initProps();
		initActions();
		showScreen();
	}

	private void initComponents() {
		loginPanel = new LoginPanel(20);
		registrationPanel = new RegistrationPanel(20);
		scene = new Scene(loginPanel, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
	}

	private void initProps() {
		// TODO add stage icon
	}

	private void initActions() {
		connect();
		initLoginActions();
		initRegistrationActions();
		initStageActions();
	}

	private void initLoginActions() {
		loginPanel.setLogin(new LoginInterface() {

			@Override
			public void login(String username, String password) {
				if (userController == null) {
					return;
				}
				userController.login(username, password);
			}

			@Override
			public void changeScreen() {
				scene.setRoot(registrationPanel);
			}
		});
	}

	private void initRegistrationActions() {
		registrationPanel.setRegistrationInterface(new RegisterInterface() {

			@Override
			public void changeScreen() {
				scene.setRoot(loginPanel);
			}

			@Override
			public void register(String username, String password, String firstName, String lastName, String email,
					Gender gender) {
				if (userController == null) {
					return;
				}
				userController.register(username, password, firstName, lastName, email, gender);
			}
		});
	}

	private void initAdminPanelActions() {
		adminPanel.setRequestSender(new RequestSender() {

			@Override
			public void sendRequest(StatusCode statusCode) {
				showWaitingDialog();
				adminController.sendUsersRequser(statusCode);
			}
		});
	}

	private void initStageActions() {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent e) {
				flag1 = false;
				Optional<ButtonType> result = Utils.makeCustomAlert("Exit", Config.EXIT).showAndWait();
				if (result.isPresent() && result.get().getButtonData() == ButtonData.OK_DONE) {
					if (adminController == null && userController != null) {
						userController.logoff();
					} else if (adminController != null && userController == null) {
						adminController.logoffRequest();
					} else {
						Platform.exit();
					}
				} else if (!flag2) {
					Utils.makeAlert(AlertType.WARNING, "Greška!", Config.SERVER_AFK).showAndWait();
					Platform.exit();
				} else {
					flag1 = true;
					e.consume();
				}
			}
		});
	}

	// === other functions === //
	private void showMessage(AlertType alertType, String title, String contextText) {
		Utils.makeAlert(alertType, title, contextText).showAndWait();
	}

	private void showScreen() {
		primaryStage.setScene(scene);
		primaryStage.setTitle("Covid-19");
		primaryStage.show();
	}

	private void initMainPanel() {
		mainPanel = new MainPanel(userController, primaryStage);
	}

	private void initAdminPanel() {
		adminPanel = new AdminPanel(adminController, primaryStage);
	}

	private void showMainPanel() {
		scene.setRoot(mainPanel);
	}

	private void showAdminPanel() {
		scene.setRoot(adminPanel);
	}

	private void initAdminControllerActions() {
		adminController.setAdminInterface(new AdminInterface() {

			@Override
			public void done(int matches) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						adminPanel.updateMatches(matches);
						hideWaitingDialog();
					}
				});
			}

			@Override
			public void serverError() {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						showMessage(AlertType.WARNING, "Greška!", Config.SERVER_ERROR);
						Platform.exit();
					}
				});
			}
		});
	}

	private void initUserControllerActions() {
		userController.setControllerInterface(new ControllerInterface() {

			@Override
			public void logoff() {
				Platform.exit();
			}

			@Override
			public void registerOK(String message) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						initMainPanel();
						userController.sendInfoRequest();
						showMessage(AlertType.INFORMATION, "Obaveštenje", message);
						showMainPanel();
					}
				});
			}

			@Override
			public void registerCancle(String message) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						showMessage(AlertType.WARNING, "Greška!", message);
					}
				});
			}

			@Override
			public void loginOK(String message) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						initMainPanel();
						userController.sendLoadTestRequest();
						userController.sendInfoRequest();
						showMessage(AlertType.INFORMATION, "Obaveštenje", message);
						showMainPanel();
					}
				});
			}

			@Override
			public void loginCancle(String message) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						showMessage(AlertType.WARNING, "Greška!", message);
					}
				});
			}

			@Override
			public void testOK(String message) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						showMessage(AlertType.INFORMATION, "Obaveštenje", message);
						mainPanel.showTestChooserPane();
					}
				});
			}

			@Override
			public void testNO(String message) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						showMessage(AlertType.WARNING, "Upozorenje", message);
					}
				});
			}

			@Override
			public void testResultMessage(String message) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						showMessage(AlertType.INFORMATION, "Obaveštenje", message);
					}
				});
			}

			@Override
			public void updateProgressStatus(ProgressStatus progressStatus, double progress) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						mainPanel.updateProgressStatus(progressStatus, progress);
					}
				});
			}

			@Override
			public void setInfo(String timestamp, String testTime) {
				mainPanel.setInfo(timestamp, testTime);
			}

			@Override
			public void initAdmin(AdminController adminController) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						userController = null;
						setAdminController(adminController);
						initAdminControllerActions();
						startAdminController();

						initAdminPanel();
						initAdminPanelActions();
						showAdminPanel();
						showWaitingDialog();
						adminController.sendUsersRequser(StatusCode.USERS);
					}
				});
			}

			@Override
			public void expiredSurveillance(String message) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						showMessage(AlertType.WARNING, "Upozorenje", message);
					}
				});
			}

			@Override
			public void connectionError() {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						showMessage(AlertType.WARNING, "Greška!", Config.SERVER_ERROR);
						Platform.exit();
					}
				});
			}

		});
	}

	private void showWaitingDialog() {
		adminPanel.showWaitingDialog();
	}

	private void hideWaitingDialog() {
		adminPanel.hideWaitingDialog();
	}

	private void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					userController = new UserController(new Socket("localhost", 8888));
					initUserControllerActions();
					startUserController();
				} catch (IOException e) {
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							if (flag1) {
								Utils.makeAlert(AlertType.WARNING, "Greška!", Config.SERVER_AFK).showAndWait();
								Platform.exit();
							} else {
								flag2 = false;
							}
						}
					});
				}
			}
		}).start();
	}

	private void setAdminController(AdminController adminController) {
		this.adminController = adminController;
	}

	private void startAdminController() {
		new Thread(adminController).start();
	}

	private void startUserController() {
		new Thread(userController).start();
	}

}
