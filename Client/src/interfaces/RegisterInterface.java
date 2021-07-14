package interfaces;

import enums.Gender;

public interface RegisterInterface {

	public void register(String username, String password, String firstName, String lastName, String email,
			Gender gender);

	public void changeScreen();

}
