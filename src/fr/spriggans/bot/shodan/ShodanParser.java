package fr.spriggans.bot.shodan;

import java.util.ArrayList;
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
		ShodanRestApi api = new ShodanRestApi(Constants.SHODAN_API_KEY);
		List<String> ipList = new ArrayList<>();

		int page = 1;
		while (searchPage(page++, api, ipList)) {
		}

		return ipList;
	}

	/**
	 * @return true if we added some new results.
	 */
	private static boolean searchPage(int page, ShodanRestApi api, List<String> ipList) {
		int sizeBefore = ipList.size();
		api.hostSearch(page, Constants.QUERY, null).subscribe(new DisposableObserver<HostReport>() {

			@Override
			public void onComplete() {
				Logger.log("Shodan search completed !");
				Logger.separationLine();
				Logger.log("Found " + ipList.size() + " IPs so far");
				Logger.separationLine();
				try {
					Thread.sleep(2_000);
				} catch (InterruptedException e) {
					// Nothing
				}
			}

			@Override
			public void onError(Throwable t) {
				Logger.err("ERROR");
				Logger.err(t);
				Logger.stack(t);
			}

			@Override
			public void onNext(HostReport report) {
				Logger.log("Found " + report.getBanners().size() + " IPs in this page.");
				for (int i = 0; i < report.getBanners().size(); i++) {
					String ip = report.getBanners().get(i).getIpStr();
					if (!ipList.contains(ip)) {
						ipList.add(ip);
					}
				}
				Logger.separationLine();
			}
		});
		
		return ipList.size() != sizeBefore;
	}
}
