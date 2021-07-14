package layout.user;

import config.Config;
import controller.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import utils.Utils;

public class TestPanel extends GridPane {

	private UserController userController;

	private Label title;
	private Label question1;
	private Label question2;
	private Label question3;
	private Label sub1;
	private Label sub2;
	private Label sub3;
	private Label sub4;
	private Label sub5;

	private RadioButton A1;
	private RadioButton A2;
	private RadioButton B1;
	private RadioButton B2;
	private RadioButton C1;
	private RadioButton C2;
	private RadioButton D1;
	private RadioButton D2;
	private RadioButton E1;
	private RadioButton E2;
	private RadioButton F1;
	private RadioButton F2;
	private RadioButton G1;
	private RadioButton G2;

	private ToggleGroup A;
	private ToggleGroup B;
	private ToggleGroup C;
	private ToggleGroup D;
	private ToggleGroup E;
	private ToggleGroup F;
	private ToggleGroup G;

	private Button submit;

	public TestPanel(UserController userController) {
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
		title = new Label("Test samoprocene");
		question1 = new Label(Config.QUESTION_1);
		question2 = new Label(Config.QUESTION_2);
		question3 = new Label(Config.QUESTION_3);
		sub1 = new Label(Config.SUB_1);
		sub2 = new Label(Config.SUB_2);
		sub3 = new Label(Config.SUB_3);
		sub4 = new Label(Config.SUB_4);
		sub5 = new Label(Config.SUB_5);

		A1 = new RadioButton(Config.YES);
		A2 = new RadioButton(Config.NO);
		B1 = new RadioButton(Config.YES);
		B2 = new RadioButton(Config.NO);
		C1 = new RadioButton(Config.YES);
		C2 = new RadioButton(Config.NO);
		D1 = new RadioButton(Config.YES);
		D2 = new RadioButton(Config.NO);
		E1 = new RadioButton(Config.YES);
		E2 = new RadioButton(Config.NO);
		F1 = new RadioButton(Config.YES);
		F2 = new RadioButton(Config.NO);
		G1 = new RadioButton(Config.YES);
		G2 = new RadioButton(Config.NO);

		A = new ToggleGroup();
		B = new ToggleGroup();
		C = new ToggleGroup();
		D = new ToggleGroup();
		E = new ToggleGroup();
		F = new ToggleGroup();
		G = new ToggleGroup();

		submit = new Button("SUBMIT");
	}

	private void initProps() {
		A1.setToggleGroup(A);
		A2.setToggleGroup(A);
		B1.setToggleGroup(B);
		B2.setToggleGroup(B);
		C1.setToggleGroup(C);
		C2.setToggleGroup(C);
		D1.setToggleGroup(D);
		D2.setToggleGroup(D);
		E1.setToggleGroup(E);
		E2.setToggleGroup(E);
		F1.setToggleGroup(F);
		F2.setToggleGroup(F);
		G1.setToggleGroup(G);
		G2.setToggleGroup(G);

		A1.setUserData(Config.YES);
		A2.setUserData(Config.NO);
		B1.setUserData(Config.YES);
		B2.setUserData(Config.NO);
		C1.setUserData(Config.YES);
		C2.setUserData(Config.NO);
		D1.setUserData(Config.YES);
		D2.setUserData(Config.NO);
		E1.setUserData(Config.YES);
		E2.setUserData(Config.NO);
		F1.setUserData(Config.YES);
		F2.setUserData(Config.NO);
		G1.setUserData(Config.YES);
		G2.setUserData(Config.NO);

		A2.setSelected(true);
		B2.setSelected(true);
		C2.setSelected(true);
		D2.setSelected(true);
		E2.setSelected(true);
		F2.setSelected(true);
		G2.setSelected(true);

		A1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		A2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		B1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		B2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		C1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		C2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		D1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		D2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		E1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		E2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		F1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		F2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		G1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		G2.setFont(Font.font("Arial", FontWeight.BOLD, 12));

		title.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		question1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		question2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		question3.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		sub1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		sub2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		sub3.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		sub4.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		sub5.setFont(Font.font("Arial", FontWeight.BOLD, 12));

		submit.setPrefWidth(150);
		submit.setFont(Utils.dialogFont(13));
	}

	private void initActions() {
		submit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				userController.sendTestRequest(getResult());
			}
		});
	}

	private void initLayout() {
		setHgap(10);
		setVgap(10);
		setAlignment(Pos.CENTER);

		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().add(title);
		add(hBox, 0, 0, 2, 1);

		add(question1, 0, 1);
		add(new HBox(10, A1, A2), 1, 1);

		add(question2, 0, 2);
		add(new HBox(10, B1, B2), 1, 2);

		add(question3, 0, 3);

		add(sub1, 0, 4);
		add(new HBox(10, C1, C2), 1, 4);

		add(sub2, 0, 5);
		add(new HBox(10, D1, D2), 1, 5);

		add(sub3, 0, 6);
		add(new HBox(10, E1, E2), 1, 6);

		add(sub4, 0, 7);
		add(new HBox(10, F1, F2), 1, 7);

		add(sub5, 0, 8);
		add(new HBox(10, G1, G2), 1, 8);

		HBox hBox2 = new HBox(submit);
		hBox2.setPadding(new Insets(20, 0, 0, 0));
		hBox2.setAlignment(Pos.CENTER);
		add(hBox2, 0, 9, 2, 1);
	}

	private int getResult() {
		int res = 0;

		if (A.getSelectedToggle().getUserData().equals(Config.YES)) {
			res++;
		}

		if (B.getSelectedToggle().getUserData().equals(Config.YES)) {
			res++;
		}

		if (C.getSelectedToggle().getUserData().equals(Config.YES)) {
			res++;
		}

		if (D.getSelectedToggle().getUserData().equals(Config.YES)) {
			res++;
		}

		if (E.getSelectedToggle().getUserData().equals(Config.YES)) {
			res++;
		}

		if (F.getSelectedToggle().getUserData().equals(Config.YES)) {
			res++;
		}

		if (G.getSelectedToggle().getUserData().equals(Config.YES)) {
			res++;
		}

		return res;
	}

}
