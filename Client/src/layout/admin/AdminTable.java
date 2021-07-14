package layout.admin;

import controller.Controller;
import entity.User;
import enums.UserStatus;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import utils.Utils;

public class AdminTable extends TableView<User> {

	private ObservableList<User> users = Controller.CONTROLLER.getusUsers();

	public AdminTable() {
		init();
	}

	private void init() {
		initComponents();
		initAcitons();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		TableColumn<User, Integer> id = new TableColumn<User, Integer>("ID");
		TableColumn<User, String> firstName = new TableColumn<User, String>("Ime");
		TableColumn<User, String> lastName = new TableColumn<User, String>("Prezime");
		TableColumn<User, String> email = new TableColumn<User, String>("E-mail");
		TableColumn<User, UserStatus> userStatus = new TableColumn<User, UserStatus>("Korisnički status");

		id.setPrefWidth(50);
		firstName.setPrefWidth(120);
		lastName.setPrefWidth(120);
		email.setPrefWidth(190);
		userStatus.setPrefWidth(120);

		id.setCellFactory(new Callback<TableColumn<User, Integer>, TableCell<User, Integer>>() {

			@Override
			public TableCell<User, Integer> call(TableColumn<User, Integer> arg0) {
				TableCell<User, Integer> tc = new TableCell<User, Integer>() {
					protected void updateItem(Integer item, boolean empty) {
						if (item == null || empty) {
							setText(null);
						} else {
							setText(String.valueOf(item));
						}
					}
				};
				tc.setAlignment(Pos.CENTER);
				return tc;
			}
		});

		firstName.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {

			@Override
			public TableCell<User, String> call(TableColumn<User, String> arg0) {
				TableCell<User, String> tc = new TableCell<User, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
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

		lastName.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {

			@Override
			public TableCell<User, String> call(TableColumn<User, String> arg0) {
				TableCell<User, String> tc = new TableCell<User, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
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

		email.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {

			@Override
			public TableCell<User, String> call(TableColumn<User, String> arg0) {
				TableCell<User, String> tc = new TableCell<User, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
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

		userStatus.setCellFactory(new Callback<TableColumn<User, UserStatus>, TableCell<User, UserStatus>>() {

			@Override
			public TableCell<User, UserStatus> call(TableColumn<User, UserStatus> arg0) {
				TableCell<User, UserStatus> tc = new TableCell<User, UserStatus>() {
					@Override
					protected void updateItem(UserStatus item, boolean empty) {
						if (item == null || empty) {
							setText(null);
						} else {
							setText(Utils.userStatus(item));
						}
					}
				};
				tc.setAlignment(Pos.CENTER);
				return tc;
			}
		});

		id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
		firstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
		lastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
		email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
		userStatus.setCellValueFactory(new PropertyValueFactory<User, UserStatus>("userStatus"));

		setItems(users);
		getColumns().addAll(id, firstName, lastName, email, userStatus);
	}

	private void initAcitons() {

	}

}
