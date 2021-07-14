package entity;

import enums.TestResult;
import enums.TestType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Test {

	private SimpleIntegerProperty id;
	private SimpleObjectProperty<TestType> testType;
	private SimpleObjectProperty<TestResult> testResult;
	private SimpleStringProperty date;

	public Test(int id, TestType testType, TestResult testResult, String date) {
		this.id = new SimpleIntegerProperty(id);
		this.testType = new SimpleObjectProperty<TestType>(testType);
		this.testResult = new SimpleObjectProperty<TestResult>(testResult);
		this.date = new SimpleStringProperty(date);
	}

	public int getId() {
		return id.get();
	}

	public TestType getTestType() {
		return testType.get();
	}

	public TestResult getTestResult() {
		return testResult.get();
	}

	public String getDate() {
		return date.get();
	}

}
