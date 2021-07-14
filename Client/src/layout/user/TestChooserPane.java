package layout.user;

import config.Config;
import controller.UserController;
import enums.TestType;
import interfaces.ScreenCloser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import utils.Utils;

public class TestChooserPane extends VBox {

	private UserController userController;

	private Label title;
	private CheckBox fast;
	private CheckBox pcr;
	private Button submit;

	private ScreenCloser screenCloser;

	public TestChooserPane(int vGap, UserController userController) {
		super(vGap);
		this.userController = userController;
		init();
	}

	private void init() {
		initComponents();
		initActions();
		initProps();
		initLayout();
	}

	private void initComponents() {
		title = new Label("Izaberite željenji način testiranja");
		fast = new CheckBox("Fast");
		pcr = new CheckBox("PCR");
		submit = new Button("Submit");
	}

	private void initProps() {
		setStyle(Config.BORDER_STYLE);

		title.setFont(Utils.dialogFont(15));
		fast.setFont(Utils.dialogFont(13));
		pcr.setFont(Utils.dialogFont(13));
		submit.setFont(Utils.dialogFont(13));

		submit.setPrefWidth(100);
	}

	private void initActions() {
		submit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				if (fast.isSelected() && pcr.isSelected()) {
					userController.testing(TestType.FAST);
					userController.testing(TestType.PCR);
					screenCloser.closeScreen();
				} else if (fast.isSelected()) {
					userController.testing(TestType.FAST);
					screenCloser.closeScreen();
				} else if (pcr.isSelected()) {
					userController.testing(TestType.PCR);
					screenCloser.closeScreen();
				} else {
					Utils.makeAlert(AlertType.WARNING, "Test chooser", Config.TEST_CHOOSER).show();
				}
			}
		});
	}

	private void initLayout() {
		setAlignment(Pos.CENTER);
		getChildren().addAll(title, fast, pcr, submit);
	}

	public void setScreenCloser(ScreenCloser screenCloser) {
		this.screenCloser = screenCloser;
	}

}
