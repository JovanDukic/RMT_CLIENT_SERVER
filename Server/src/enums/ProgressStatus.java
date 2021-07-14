package enums;

public enum ProgressStatus {

	NONE(-1), QUEUING(0), SENDING(25), PROCESSING_1(50), PROCESSING_2(75), DONE(100);

	private double progress;

	private ProgressStatus(double progress) {
		this.progress = progress;
	}

	public double progress() {
		return progress;
	}

}
