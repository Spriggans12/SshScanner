package fr.spriggans.models;

public class Connection {

	private String user;

	private String password;

	private String host;

	public Connection(String user, String password, String host) {
		this.user = user;
		this.password = password;
		this.host = host;
	}
	
	@Override
	public String toString() {
		return this.host + " @ " + this.user + " / " + this.password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
