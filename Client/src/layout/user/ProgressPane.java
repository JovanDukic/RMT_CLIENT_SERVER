package layout.user;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import utils.Utils;

public class ProgressPane extends StackPane {

	private Label progressLabel;
	private ProgressBar progressBar;

	public ProgressPane() {
		init();
	}

	private void init() {
		initComponents();
		initProps();
		initLayout();
	}

	private void initComponents() {
		progressLabel = new Label();
		progressBar = new ProgressBar(-1);
	}

	private void initProps() {
		progressLabel.setFont(Utils.dialogFont(15));
		progressBar.setPrefWidth(150);
		progressBar.setPrefHeight(30);
	}

	private void initLayout() {
		getChildren().setAll(progressBar, progressLabel);
	}

	public void updateProgress(double progress) {
		progressBar.setProgress(progress / 100);
		progressLabel.setText(String.valueOf(progress) + "%");
	}

	public void reset() {
		progressLabel.setText("");
		progressBar.setProgress(-1);
	}

}
