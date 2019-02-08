package fr.spriggans.bot.shodan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fooock.shodan.ShodanRestApi;
import com.fooock.shodan.model.host.HostReport;

import fr.spriggans.bot.Constants;
import fr.spriggans.bot.logger.Logger;
import io.reactivex.observers.DisposableObserver;

public class ShodanParser {
	
	
	static boolean DEBUG = false;
	
	private ShodanParser() {
		// Empty
	}
	
	public static List<String> getIps() {
		
		if(DEBUG) {
			return Arrays.asList(new String[] {"79.246.119.5"});
		}
		
		
		ShodanRestApi api = new ShodanRestApi(Constants.SHODAN_API_KEY);
		List<String> ipList = new ArrayList<>();
		
		api.hostSearch(Constants.QUERY).subscribe(new DisposableObserver<HostReport>() {

			@Override
			public void onComplete() {
				Logger.log("Shodan search completed !");
				Logger.log("Found " + ipList.size() + " IPs.");
			}

			@Override
			public void onError(Throwable t) {
				Logger.err("ERROR");
				Logger.err(t);
				Logger.stack(t);
			}

			@Override
			public void onNext(HostReport report) {
				for(int i = 0 ; i < report.getBanners().size(); i++) {
					ipList.add(report.getBanners().get(i).getIpStr());
				}
				Logger.separationLine();
			}
		});
		
		return ipList;
	}
}
