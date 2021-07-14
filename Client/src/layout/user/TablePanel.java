package layout.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TablePanel extends VBox {

	private Label title;
	private Table table;

	public TablePanel(int vGap) {
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
		title = new Label("Rezultati testiranja");
		table = new Table();
	}

	private void initProps() {
		title.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		title.setPadding(new Insets(10, 0, 0, 0));
	}

	private void initActions() {

	}

	private void initLayout() {
		setAlignment(Pos.CENTER);
		getChildren().addAll(title, table);
	}

}
