package interfaces;

import enums.StatusCode;

public interface RequestSender {
	
	public void sendRequest(StatusCode statusCode);

}
