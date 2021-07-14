package layout.user;

import controller.Controller;
import entity.Test;
import enums.TestResult;
import enums.TestType;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import utils.Utils;

public class Table extends TableView<Test> {

	private ObservableList<Test> observableArray = Controller.CONTROLLER.getTests();

	public Table() {
		init();
	}

	private void init() {
		initComponents();
		initProps();
		initActions();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		TableColumn<Test, Integer> id = new TableColumn<Test, Integer>("ID");
		TableColumn<Test, TestType> type = new TableColumn<Test, TestType>("Tip testa");
		TableColumn<Test, TestResult> result = new TableColumn<Test, TestResult>("Rezultat testa");
		TableColumn<Test, String> date = new TableColumn<Test, String>("Datum i vreme");

		id.setMinWidth(50);
		type.setMinWidth(150);
		result.setMinWidth(150);
		date.setMinWidth(250);

		id.setCellFactory(new Callback<TableColumn<Test, Integer>, TableCell<Test, Integer>>() {

			@Override
			public TableCell<Test, Integer> call(TableColumn<Test, Integer> test) {
				TableCell<Test, Integer> tc = new TableCell<Test, Integer>() {

					@Override
					protected void updateItem(Integer item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText(null);
						} else {
							setText(String.valueOf(item.intValue()));
						}

					}
				};
				tc.setAlignment(Pos.CENTER);
				return tc;
			}
		});

		type.setCellFactory(new Callback<TableColumn<Test, TestType>, TableCell<Test, TestType>>() {

			@Override
			public TableCell<Test, TestType> call(TableColumn<Test, TestType> test) {
				TableCell<Test, TestType> tc = new TableCell<Test, TestType>() {

					@Override
					protected void updateItem(TestType item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText(null);
						} else {
							setText(Utils.testType(item));
						}
					}
				};
				tc.setAlignment(Pos.CENTER);
				return tc;
			}
		});

		result.setCellFactory(new Callback<TableColumn<Test, TestResult>, TableCell<Test, TestResult>>() {

			@Override
			public TableCell<Test, TestResult> call(TableColumn<Test, TestResult> test) {
				TableCell<Test, TestResult> tc = new TableCell<Test, TestResult>() {

					@Override
					protected void updateItem(TestResult item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText(null);
						} else {
							setText(item.getResult());
						}
					}
				};
				tc.setAlignment(Pos.CENTER);
				return tc;
			}
		});

		date.setCellFactory(new Callback<TableColumn<Test, String>, TableCell<Test, String>>() {

			@Override
			public TableCell<Test, String> call(TableColumn<Test, String> test) {
				TableCell<Test, String> tc = new TableCell<Test, String>() {

					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText(null);
						} else {
							setText(item);
						}
					}
				};
				tc.setAlignment(Pos.CENTER);
				return tc;
			}
		});

		id.setCellValueFactory(new PropertyValueFactory<Test, Integer>("id"));
		type.setCellValueFactory(new PropertyValueFactory<Test, TestType>("testType"));
		result.setCellValueFactory(new PropertyValueFactory<Test, TestResult>("testResult"));
		date.setCellValueFactory(new PropertyValueFactory<Test, String>("date"));

		setItems(observableArray);
		getColumns().addAll(id, type, result, date);
	}

	private void initProps() {

	}

	private void initActions() {

	}

}
