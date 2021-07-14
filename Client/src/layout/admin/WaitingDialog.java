package layout.admin;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import utils.Utils;

public class WaitingDialog extends VBox {

	private Label statusLabel;
	private ProgressIndicator progressIndicator;

	public WaitingDialog(int vGap) {
		super(vGap);
		init();
	}

	private void init() {
		initComponents();
		initProps();
		initLayout();
	}

	private void initComponents() {
		statusLabel = new Label("Loading data...");
		progressIndicator = new ProgressIndicator();
	}

	private void initProps() {
		statusLabel.setFont(Utils.dialogFont(13));
	}

	private void initLayout() {
		setAlignment(Pos.CENTER);
		getChildren().addAll(statusLabel, progressIndicator);
	}

}
