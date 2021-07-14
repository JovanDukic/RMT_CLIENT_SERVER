package enums;

public enum Connection {

	CONNECTED, CLOSED;

	// === public methods === //
	public boolean isConnected() {
		return this == CONNECTED;
	}

	public boolean isClosed() {
		return this == CLOSED;
	}

}
