package com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection_detect {

	public static boolean isConnectingToInternet(Context c) {
		ConnectivityManager connectivity = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (NetworkInfo anInfo : info)
					if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}
}
