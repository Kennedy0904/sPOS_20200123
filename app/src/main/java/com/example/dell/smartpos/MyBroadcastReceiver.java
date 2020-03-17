package com.example.dell.smartpos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("intent.getAction(): " + intent.getAction());

		/** CONNECTIVITY_ACTION **/
		/** network connection on/off **/
		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			Log.d("NetworkCheckReceiver", "NetworkCheckReceiver invoked...");


//			boolean noConnectivity = intent.getBooleanExtra(
//					ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
//
//			if (!noConnectivity) {
//				Intent intent1 = new Intent();
//				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//				intent1.setClass(context, SplashScreen.class);
//				context.startActivity(intent1);
//			}
//			else
//			{
//				Log.d("NetworkCheckReceiver", "disconnected");
//			}
		}

		/** ACTION_BOOT_COMPLETED **/
		/** start the app when boot done **/
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			System.out.print("[BroadcastReceiver] ACTION_BOOT_COMPLETED");
			Intent intent1 = new Intent();
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			intent1.setClass(context, SplashScreen.class);
			context.startActivity(intent1);
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			System.out.print("[BroadcastReceiver] ACTION_SCREEN_ON");
			Intent intent1 = new Intent();
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			intent1.setClass(context, SplashScreen.class);
			context.startActivity(intent1);
		}
	}
}
