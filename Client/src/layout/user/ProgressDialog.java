package layout.user;

import config.Config;
import enums.ProgressStatus;
import interfaces.ScreenCloser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import utils.Utils;

public class ProgressDialog extends VBox {

	private Label statusLabel;
	private ProgressPane progressPane;
	private Button confirm;

	private ScreenCloser screenCloser;

	public ProgressDialog(int vGap) {
		super(vGap);
		init();
	}

	private void init() {
		initComponents();
		initActions();
		initProps();
		initLayout();
	}

	private void initComponents() {
		statusLabel = new Label("Status: nema obrade");
		progressPane = new ProgressPane();
		confirm = new Button("OK");
	}

	private void initProps() {
		setStyle(Config.BORDER_STYLE);

		confirm.setPrefWidth(150);

		statusLabel.setFont(Utils.dialogFont(15));
		confirm.setFont(Utils.dialogFont(13));
	}

	private void initActions() {
		confirm.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				screenCloser.closeScreen();
			}
		});
	}

	private void initLayout() {
		setAlignment(Pos.CENTER);
		getChildren().addAll(statusLabel, progressPane, confirm);
	}

	public void setScreenCloser(ScreenCloser screenCloser) {
		this.screenCloser = screenCloser;
	}

	public void updateProgress(ProgressStatus progressStatus, double progress) {
		if (progressStatus == ProgressStatus.NONE) {
			statusLabel.setText("Status: " + progressStatus.getProgressStatus());
			progressPane.reset();
		} else {
			statusLabel.setText("Status: " + progressStatus.getProgressStatus());
			progressPane.updateProgress(progress);
		}
	}

}
