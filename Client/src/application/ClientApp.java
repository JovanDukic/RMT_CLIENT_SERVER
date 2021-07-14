package application;

import javafx.application.Application;
import javafx.stage.Stage;
import layout.MasterLayout;

public class ClientApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			new MasterLayout(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
