package layout.user;

import controller.UserController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabbedPanel extends TabPane {

	private UserController userController;

	private TestPanel testPanel;
	private TablePanel tablePanel;

	public TabbedPanel(UserController userController) {
		this.userController = userController;
		init();
	}

	private void init() {
		initComponents();
		initProps();
		initActions();
		initLayout();
	}

	private void initComponents() {
		testPanel = new TestPanel(userController);
		tablePanel = new TablePanel(10);
	}

	private void initProps() {
		setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}

	private void initActions() {

	}

	private void initLayout() {
		Tab tab = new Tab("Test samoprocene", testPanel);
		Tab tab2 = new Tab("Rezultati testiranja", tablePanel);
		getTabs().addAll(tab, tab2);
	}

}
