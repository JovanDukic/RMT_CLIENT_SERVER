package entity;

import java.util.Date;

import enums.TestResult;
import enums.TestType;

public class Test {

	private int id;
	private TestType testType;
	private TestResult testResult;
	private Date date;

	public Test(int id, TestType testType, TestResult testResult, Date date) {
		super();
		this.id = id;
		this.testType = testType;
		this.testResult = testResult;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public TestType getTestType() {
		return testType;
	}

	public TestResult getTestResult() {
		return testResult;
	}

	public Date getDate() {
		return date;
	}

}
