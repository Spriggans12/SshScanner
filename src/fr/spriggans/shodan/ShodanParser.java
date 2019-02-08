package fr.spriggans.shodan;

import java.util.ArrayList;
import java.util.List;

import com.fooock.shodan.ShodanRestApi;
import com.fooock.shodan.model.host.HostReport;

import fr.spriggans.run.Constants;
import fr.spriggans.run.Logger;
import io.reactivex.observers.DisposableObserver;

public class ShodanParser {
	
	private ShodanParser() {
		// Empty
	}
	
	public static List<String> getIps() {
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
