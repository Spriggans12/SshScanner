package fr.spriggans.bot.ssh.scanner.thread;

import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import fr.spriggans.bot.Constants;
import fr.spriggans.bot.logger.Logger;
import fr.spriggans.bot.models.Connection;
import fr.spriggans.bot.models.Results;

public class SshConnectionAttemptRunable implements Runnable {

	private final Connection connection;

	private volatile Results results;

	public SshConnectionAttemptRunable(Connection connection, Results results) {
		this.connection = connection;
		this.results = results;
	}

	@Override
	public void run() {
		Logger.log("Attempting connection to : " + connection + " ...");
		boolean success = attemptConnection();
		if (success) {
			Logger.separationLine();
			Logger.log(connection + " DID SUCCEED !!!");
			Logger.separationLine();
			results.add(connection);
		}
	}

	private boolean attemptConnection() {
		JSch jsch = new JSch();
		Session session;
		try {
			session = jsch.getSession(connection.getUser(), connection.getHost(), Constants.PORT);
			session.setPassword(connection.getPassword());
			session.setConfig("StrictHostKeyChecking", "no");
			session.setTimeout(Constants.MAX_TIME_PER_CONNECTION);
			session.connect();
		} catch (JSchException e) {
			return false;
		}

		Channel channel;

		// SFTP
		try {
			channel = session.openChannel("sftp");
			((ChannelSftp) channel).connect();
			channel.disconnect();
		} catch (JSchException e) {
			session.disconnect();
			return false;
		}

		// EXEC
		try {
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand("uname -a");
			InputStream in = channel.getInputStream();
			channel.connect();
			byte[] buf = new byte[1024];
			if (in.read(buf) == 0) {
				return false;
			}

			String uname = new String(buf, "UTF-8");
			if (uname.toUpperCase().contains("PLEASE")) {
				// False positive
				session.disconnect();
				Logger.err(uname);
				return false;
			}

			Logger.log("######");
			Logger.log(connection);
			Logger.log(uname.replaceAll("\n", " ").replaceAll("\r", " "));
			Logger.log("######");
			in.close();

			session.disconnect();
			return true;
		} catch (JSchException | IOException e) {
			session.disconnect();
			return false;
		}
	}
}
