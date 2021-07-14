package layout.user;

import config.Config;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import utils.Utils;

public class InfoPanel extends VBox {

	private Label timestampLabel;
	private Label testTimeLabel;

	public InfoPanel(int vGap) {
		super(vGap);
		init();
	}

	private void init() {
		initComponents();
		initProps();
		initLayout();
	}

	private void initComponents() {
		setStyle(Config.BORDER_STYLE);
		timestampLabel = new Label();
		testTimeLabel = new Label();
	}

	private void initProps() {
		timestampLabel.setFont(Utils.dialogFont(15));
		testTimeLabel.setFont(Utils.dialogFont(15));
	}

	private void initLayout() {
		setAlignment(Pos.CENTER);
		getChildren().addAll(timestampLabel, testTimeLabel);
	}

	public void setTimestamp(String timestamp) {
		timestampLabel.setText("Poslednja prijava na sistem: " + timestamp);
	}

	public void setTestTime(String testTime) {
		testTimeLabel.setText("Poslednji test samoprocene: " + testTime);
	}

}
