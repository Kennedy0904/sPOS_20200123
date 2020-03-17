package com.example.dell.smartpos.Printer.K9;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.centerm.smartpos.aidl.printer.AidlPrinter;
import com.centerm.smartpos.aidl.sys.AidlDeviceManager;
import com.centerm.smartpos.constant.Constant;
import com.centerm.smartpos.util.LogUtil;

public class PrintUtilK9 {

	public AidlDeviceManager manager = null;
	private static AidlPrinter printDev = null;

	Context context;

	public PrintUtilK9(Context context){
		this.context = context;
	}

	class MyBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("base", "action:" +intent.getAction());

		}

	}
 
	public void bindService() {
		System.out.println("Enter Bind Service");
		Intent intent = new Intent();
		intent.setPackage("com.centerm.smartposservice");
		intent.setAction("com.centerm.smartpos.service.MANAGER_SERVICE");
		context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}

	public void unbindService(){
		context.unbindService(conn);
	}

	/**
	 * 服务连接桥
	 */
	public ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			manager = null;
//			LogUtil.print(getResources().getString(R.string.bind_service_fail));
			LogUtil.print("manager = " + manager);
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			System.out.println("Enter onServiceConnected");
			manager = AidlDeviceManager.Stub.asInterface(service);
//			LogUtil.print(getResources().getString(R.string.bind_service_success));
			LogUtil.print("manager = " + manager);
			if (null != manager) {
				onDeviceConnected(manager);
			}
		}
	};

	public void onDeviceConnected(AidlDeviceManager deviceManager) {
		System.out.println("Start onDeviceConnected");
		try {
			printDev = AidlPrinter.Stub.asInterface(deviceManager
					.getDevice(Constant.DEVICE_TYPE.DEVICE_TYPE_PRINTERDEV));

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public static AidlPrinter getPrinterDev() {

		if (printDev == null) {
			Log.d("PrintUtilK9", "printDev is null");
		}

		return printDev;

	}
}
