package interfaces;

import controller.AdminController;
import enums.ProgressStatus;

public interface ControllerInterface {

	public void logoff();

	public void loginOK(String message);

	public void loginCancle(String message);

	public void registerOK(String message);

	public void registerCancle(String message);

	public void testOK(String message);

	public void testNO(String message);

	public void testResultMessage(String message);

	public void expiredSurveillance(String message);

	public void connectionError();

	public void updateProgressStatus(ProgressStatus progressStatus, double progress);

	public void setInfo(String timestamp, String testTime);

	public void initAdmin(AdminController adminController);

}
