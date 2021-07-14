package enums;

public enum ProgressStatus {

	NONE("nema obrade"), QUEUING("na čekanju"), SENDING("poslato"), PROCESSING_1("u obradi"), PROCESSING_2("u obradi"),
	DONE("gotovo");

	private String progresStatus;

	private ProgressStatus(String progresStatus) {
		this.progresStatus = progresStatus;
	}

	public String getProgressStatus() {
		return progresStatus;
	}

}
