package com.aman.teenscribblers.galgotiasuniversitymsim.BroadCastReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aman.teenscribblers.galgotiasuniversitymsim.Service.SimService;

public class SimStartServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, SimService.class);
		context.startService(service);
	}
}
