package fr.spriggans.run;

import java.util.ArrayList;
import java.util.List;

public class Constants {

	/**
	 * Found that somewhere on YT.
	 */
	public static final String SHODAN_API_KEY = "61TvA2dNwxNxmWziZxKzR5aO9tFD00Nj";

	/**
	 * Port for ssh connection attempt.
	 */
	public static final int PORT = 22;

	/**
	 * Change that if needed.
	 */
	public static final String QUERY = "raspbian port:\"22\"";

	/**
	 * Milliseconds.
	 */
	public static final int MAX_TIME_PER_CONNECTION = 15_000;
	
	

	private Constants() {
		// Nothing
	}

	public static List<String> getPasswords() {
		List<String> passwords = new ArrayList<>();
		passwords.add("raspberry");
		passwords.add("pi");
		passwords.add("raspi");
		passwords.add("admin");
		passwords.add("password");
		return passwords;
	}

	public static String getUser() {
		return "pi";
	}
}
