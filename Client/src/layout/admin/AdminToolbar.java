package layout.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import utils.Utils;

public class AdminToolbar extends HBox {

	private Label infoLabel;

	public AdminToolbar(int hGap) {
		super(hGap);
		init();
	}

	private void init() {
		initComponents();
		initProps();
		initLayout();
	}

	private void initComponents() {
		infoLabel = new Label();
	}

	private void initProps() {
		infoLabel.setFont(Utils.dialogFont(13));
	}

	private void initLayout() {
		setPrefHeight(25);
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(5));
		getChildren().addAll(infoLabel);
	}

	public void updateMatches(int matches, String type) {
		infoLabel.setText("Broj rezultata: " + matches + ", Tip: " + type);
	}

}
