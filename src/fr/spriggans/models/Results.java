package fr.spriggans.models;

import java.util.ArrayList;
import java.util.List;

public class Results {

	private volatile List<Connection> successfullConnections;

	public Results() {
		this.successfullConnections = new ArrayList<>();
	}

	public synchronized void add(Connection connection) {
		this.successfullConnections.add(connection);
	}

	public List<Connection> getSuccessfullConnections() {
		return successfullConnections;
	}
}
