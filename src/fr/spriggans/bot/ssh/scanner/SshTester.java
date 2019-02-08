package fr.spriggans.bot.ssh.scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import fr.spriggans.bot.Constants;
import fr.spriggans.bot.logger.Logger;
import fr.spriggans.bot.models.Connection;
import fr.spriggans.bot.models.Results;
import fr.spriggans.bot.ssh.scanner.thread.SshConnectionAttemptRunable;

public class SshTester {

	private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

	private SshTester() {
		// Nothing
	}

	public static Results testIps(List<String> ipList) throws InterruptedException {
		Results results = new Results();
		addAllConnectionsToPool(constructConnections(ipList), results);
		THREAD_POOL.shutdown();
		Logger.separationLine();
		Logger.log("All the connections have been scheduled !");
		Logger.log("Waiting for completion of all the connections attempts...");
		Logger.separationLine();
		THREAD_POOL.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		return results;
	}

	private static List<Connection> constructConnections(List<String> ipList) {
		List<Connection> toTest = new ArrayList<>();
		String user = Constants.getUser();
		List<String> passwords = Constants.getPasswords();
		for (String password : passwords) {
			for (String host : ipList) {
				toTest.add(new Connection(user, password, host));
			}
		}
		return toTest;
	}

	private static void addAllConnectionsToPool(List<Connection> connections, Results results) {
		String lastPassword = "";
		for (Connection conn : connections) {
			if (!lastPassword.equals(conn.getPassword())) {
				lastPassword = conn.getPassword();
			}
			addConnectionToPool(conn, results);
		}
	}

	private static void addConnectionToPool(Connection conn, Results results) {
		SshConnectionAttemptRunable connectionAttempt = new SshConnectionAttemptRunable(conn, results);
		FutureTask<?> task = new FutureTask<>(connectionAttempt, null);
		THREAD_POOL.execute(task);
	}
}
