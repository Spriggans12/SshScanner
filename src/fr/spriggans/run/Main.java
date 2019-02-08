package fr.spriggans.run;

import java.util.List;

import fr.spriggans.models.Results;
import fr.spriggans.shodan.ShodanParser;
import fr.spriggans.sshbot.SshTester;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		List<String> ipList = ShodanParser.getIps();
		Results results = SshTester.testIps(ipList);
		displayResults(results);
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
