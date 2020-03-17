package com.example.dell.smartpos;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.widget.Toast;

import com.pax.dal.IDAL;
import com.pax.dal.entity.ENavigationKey;
import com.pax.neptunelite.api.NeptuneLiteUser;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Integer.parseInt;

public class GlobalFunction {

	/** This function used to check the payment type is QR payment type or not **/
	public static Boolean isQRpMethods(String pMethod) {

		String[] pMethodArray = {"ALIPAYHKOFFL", "ALIPAYOFFL", "BOOSTOFFL", "GCASHOFFL",
				"GRABPAYOFFL", "OEPAYOFFL", "PROMPTPAYOFFL", "UNIONPAYOFFL", "WECHATHKOFFL",
				"WECHATOFFL", "WECHATONL", "WECHATHKOFFL"};

		Boolean isQR = false;

		for (String value: pMethodArray) {
			if (value.equalsIgnoreCase(pMethod)) {
				isQR = true;
				break;
			}
		}

		return isQR;
	}

	/** This function used to check the payment type is QR payment type or not **/
	public static Boolean isCardpMethods(String pMethod) {

		String[] pMethodArray = {"VISA", "MASTERCARD", "MASTER"};

		Boolean isCard = false;

		for (String value: pMethodArray) {
			if (value.equalsIgnoreCase(pMethod)) {
				isCard = true;
				break;
			}
		}

		return isCard;
	}

	public static Boolean isValidCardBank(String pMethod) {

		String[] pMethodArray = {"First-Data", "Global-Payment"};

		Boolean isValid = false;

		for (String value: pMethodArray) {
			if (value.equalsIgnoreCase(pMethod)) {
				isValid = true;
				break;
			}
		}

		return isValid;
	}


	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

		// Return true / false
		return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
	}

	public static int wifiInternetStrength(Context context){
		// level 1 No Internet
		// level 2 Poor
		// level 3 Moderate
		// level 4 Good
		// level 5 Excellent
		final WifiManager wifiManager =
				(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		int numberOfLevels = 5;
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
	}

	// This function used to set the payment method report header inside
	// (Transaction, Refund, Request Refund, Void) Report
	// Note: Modify this function will affect TransactionReportAdapter, RefundReportAdapter,
	// VoidReportAdapter and RequestRefundReportAdapter
	public static String setReportHeader(Context context, String paymethod) {

		String reportHeaderText = "No Payment Method Matched";

		if (paymethod.equalsIgnoreCase("ALIPAYCNOFFL")) {
			reportHeaderText = context.getString(R.string.ALIPAYCNOFFL_label);
		} else if (paymethod.equalsIgnoreCase("ALIPAYHKOFFL")) {
			reportHeaderText = context.getString(R.string.ALIPAYHKOFFL_label);
		} else if (paymethod.equalsIgnoreCase("ALIPAYOFFL")) {
			reportHeaderText = context.getString(R.string.ALIPAYOFFL_label);
		} else if (paymethod.equalsIgnoreCase("BOOSTOFFL")) {
			reportHeaderText = context.getString(R.string.BOOSTOFFL_label);
		} else if (paymethod.equalsIgnoreCase("GCASHOFFL")) {
			reportHeaderText = context.getString(R.string.GCASHOFFL_label);
		} else if (paymethod.equalsIgnoreCase("GRABPAYOFFL")) {
			reportHeaderText = context.getString(R.string.GRABPAYOFFL_label);
		} else if (paymethod.equalsIgnoreCase("MASTER")) {
			reportHeaderText = context.getString(R.string.master_label);
		} else if (paymethod.equalsIgnoreCase("OEPAYOFFL")) {
			reportHeaderText = context.getString(R.string.OEPAYOFFL_label);
		} else if (paymethod.equalsIgnoreCase("PROMPTPAYOFFL")) {
			reportHeaderText = context.getString(R.string.PROMPTPAYOFFL_label);
		} else if (paymethod.equalsIgnoreCase("UNIONPAYOFFL")) {
			reportHeaderText = context.getString(R.string.UNIONPAYOFFL_label);
		} else if (paymethod.equalsIgnoreCase("VISA")) {
			reportHeaderText = context.getString(R.string.visa_label);
		} else if (paymethod.equalsIgnoreCase("WECHATHKOFFL")) {
			reportHeaderText = context.getString(R.string.WECHATHKOFFL_label);
		} else if (paymethod.equalsIgnoreCase("WECHATOFFL")) {
			reportHeaderText = context.getString(R.string.WECHATOFFL_label);
		} else if (paymethod.equalsIgnoreCase("WECHATONL")) {
			reportHeaderText = context.getString(R.string.WECHATONL_label);
		}

		return reportHeaderText;
	}

	public static String getDeviceMan() {
		return android.os.Build.MANUFACTURER;
	}

	public static void disablePaxNavigationButton(Context context) {

		if (GlobalFunction.getDeviceMan().equals("PAX")) {
			IDAL idal = null;

			try {
				idal = NeptuneLiteUser.getInstance().getDal(context);
			} catch (Exception e) {
				e.printStackTrace();
			}

			idal.getSys().enableNavigationKey(ENavigationKey.HOME, false);
			idal.getSys().enableNavigationKey(ENavigationKey.RECENT, false);
		}
	}

	public static void enablePaxNavigationButton(Context context) {

		if (GlobalFunction.getDeviceMan().equals("PAX")) {
			IDAL idal = null;

			try {
				idal = NeptuneLiteUser.getInstance().getDal(context);
			} catch (Exception e) {
				e.printStackTrace();
			}

			idal.getSys().enableNavigationKey(ENavigationKey.HOME, true);
			idal.getSys().enableNavigationKey(ENavigationKey.RECENT, true);
		}
	}

	public static void killFDMSBackgroundSevices(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
		am.killBackgroundProcesses("com.pax.fdms.base24");
	}

	public static void changeLanguage(Context context) {

		String lang = GlobalFunction.getLanguage(context);

		Locale myLocale = null;
		if (lang.equalsIgnoreCase(Constants.LANG_ENG)) {
			myLocale = Locale.ENGLISH;
		} else if (lang.equalsIgnoreCase(Constants.LANG_TRAD)) {
			myLocale = Locale.TRADITIONAL_CHINESE;
		} else if (lang.equalsIgnoreCase(Constants.LANG_SIMP)) {
			myLocale = Locale.SIMPLIFIED_CHINESE;
		} else if (lang.equalsIgnoreCase(Constants.LANG_THAI)) {
			myLocale = new Locale("th", "TH");
		}
		Configuration config = new Configuration();
		config.locale = myLocale;
		context.getResources().updateConfiguration(config,
				context.getResources().getDisplayMetrics());
	}

	public static String toCurrencyFormat(String amount) {
		// 1000000 -> 1,000,000.00
		return String.format("%,.2f", Double.parseDouble(amount));
	}

	public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
		try {
			packageManager.getPackageInfo(packageName, 0);
			System.out.println("The package " + packageName + " was installed");
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			System.out.println("The package " + packageName + " is not installed");
			return false;
		}
	}

	public static boolean isFDMS_BASE24Installed(PackageManager packageManager) {
		try {
			packageManager.getPackageInfo("com.pax.fdms.base24", 0);
			System.out.println("The package com.pax.fdms.base24 was installed");
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			System.out.println("The package com.pax.fdms.base24 is not installed");
			return false;
		}
	}

	public static void incMerRef(Context context) {
		SharedPreferences pref = context.getApplicationContext().getSharedPreferences(Constants.COUNTER, MODE_PRIVATE);
		int curCounter = pref.getInt("refCounter", 0);
		SharedPreferences.Editor edit = pref.edit();
		edit.putInt("refCounter", curCounter += 1);
		edit.commit();
	}

	public static String getPrefPayGate(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getString(Constants.pref_PayGate, Constants.default_paygate);
	}

//	public static String getPayBankId(Context context) {
//		SharedPreferences pref = context.getApplicationContext()
//				.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
//
//		return pref.getString(Constants.pref_pBankId, "");
//	}

	public static String getCurrName(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
		String currCode = pref.getString(Constants.pref_CurrCode, Constants.pref_CurrCode);
		String currName = CurrCode.getName(currCode);
		return currName;
	}

	public static String getRate(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
		String rate = pref.getString(Constants.pref_Rate, "0");
		return rate;
	}

	public static String getBatchNo(Context context){
		SharedPreferences pref = context.getSharedPreferences(Constants.BATCH_COUNTER, MODE_PRIVATE);
		String batchNo = pref.getString("batchCounter", "000001");

		while (batchNo.length() < 6) {
			batchNo = "0" + batchNo;
		}

		System.out.println("Current batch = " + batchNo);
		return batchNo;
	}

	public static String getLastBatchNo(Context context){
		SharedPreferences pref = context.getSharedPreferences(Constants.BATCH_COUNTER, MODE_PRIVATE);
		String batchNo = pref.getString("batchCounter", "000001");
		batchNo = String.valueOf(parseInt(batchNo) - 1);

		while (batchNo.length() < 6) {
			batchNo = "0" + batchNo;
		}
		System.out.println("Last Batch ID = " + batchNo);
		return batchNo;
	}

	public static void setBatchNo(Context context, String value){
		SharedPreferences pref = context.getSharedPreferences(Constants.BATCH_COUNTER, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();
		edit.putString("batchCounter", value);
		System.out.println("Set Batch ID = " + value);
		edit.commit();
	}

	public static void incBatchNo(Context context){
		SharedPreferences pref = context.getSharedPreferences(Constants.BATCH_COUNTER, MODE_PRIVATE);
		String curCounter = pref.getString("batchCounter", "000001");
		SharedPreferences.Editor edit = pref.edit();
		edit.putString("batchCounter", String.valueOf(parseInt(curCounter) + 1));
		System.out.println("Inc Batch ID = " + parseInt(curCounter) + 1);
		edit.commit();
	}

	public static boolean getLockPayment(Context context){
		SharedPreferences pref = context.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
		boolean lockPayment = pref.getBoolean("lockPayment", false);
		return lockPayment;
	}

	public static void setLockPayment(Context context, boolean value){
		SharedPreferences pref = context.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();
		edit.putBoolean("lockPayment", value);
		System.out.println("Lock Payment = " + value);
		edit.commit();
	}

	public static void showAlertDialog(Context context, String title, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
				.setMessage(msg);

		// Positive button
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User clicked OK button
			}
		});
		builder.show();
	}

	public static Boolean onKeyDownEvent(Context context, int keyCode, KeyEvent event,
										 PackageManager packageManager) {

		if (GlobalFunction.isFDMS_BASE24Installed(packageManager)) {
			if (keyCode == KeyEvent.KEYCODE_HOME) {
				Toast.makeText(context, "Not allowed!", Toast.LENGTH_SHORT).show();
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
				Toast.makeText(context, "Not allowed!", Toast.LENGTH_SHORT).show();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/** cardPayBankId **/
	public static String getCardPayBankId(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);

		return pref.getString(Constants.pref_cardPayBankId, "");
	}

	public static void setCardPayBankId(Context context, String payBankId) {
		// cardPayBankId should have only one acquirer
		SharedPreferences pref = context.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();

		String cardPayBankId = "";

		String[] payBankIdArray = {Constants.FIRST_DATA, "Global-Payment"};

		for (String value: payBankIdArray) {
			if (payBankId.contains(value)) {
				cardPayBankId = value;
				break;
			}
		}

		edit.putString(Constants.pref_cardPayBankId, cardPayBankId);
		System.out.println("cardPayBankId = " + cardPayBankId);
		edit.commit();
	}
	/** End of cardPayBankId **/

	public static String getCardRefundControl(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getString(Constants.pref_control_card_refund_password, "");
	}

	public static String getCardSettlementControl(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getString(Constants.pref_control_card_settlement_password, "");
	}

	public static String getCardVoidControl(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getString(Constants.pref_control_card_void_password, "");
	}

	public static String getQRRefundControl(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getString(Constants.pref_control_qr_refund_password, "");
	}

	public static String getQRVoidControl(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getString(Constants.pref_control_qr_void_password, "");
	}

	public static String getCardRefundPassword(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getString(Constants.pref_password_card_refund, "");
	}

	public static String getCardSettlementPassword(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getString(Constants.pref_password_card_settlement, "");
	}

	public static String getCardVoidPassword(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getString(Constants.pref_password_card_void, "");
	}

	public static String getQRRefundPassword(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getString(Constants.pref_password_qr_refund, "");
	}

	public static String getQRVoidPassword(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getString(Constants.pref_password_qr_void, "");
	}

	public static void setBankMerIdArray(Context context, String bankMerIdArray) {
		SharedPreferences pref = context.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();

		edit.putString(Constants.pref_BankMerIdArray, bankMerIdArray);
		System.out.println("bankMerIdArray = " + bankMerIdArray);
		edit.commit();
	}

	public static String getBankMerIdArray(Context context) {
		// bankMerIdArray: 2088621894215567, 123456, 620000000001332

		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);

		return pref.getString(Constants.pref_BankMerIdArray, "");
	}

	public static void setPayBankIdArray(Context context, String payBankIdArray) {
		System.out.println("sasasasasss: " + payBankIdArray);
		SharedPreferences pref = context.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();

		edit.putString(Constants.pref_pBankIdArray, payBankIdArray);
		edit.commit();
	}

	public static String getPayBankIdArray(Context context) {
		// payBankIdArray: ALIPAY, First-Data, First-Data
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);

		return pref.getString(Constants.pref_pBankIdArray, "");
	}



	public static String getBankMerId(Context context, String payBankId) {
		// payBankId: ALIPAY First-Data First-Data

		String bankMerId = null;
		String[] payBankIdArray;
		String[] bankMerIdArray;

		payBankIdArray = GlobalFunction.getPayBankIdArray(context).split(",");
		bankMerIdArray = GlobalFunction.getBankMerIdArray(context).split(",");

//		System.out.println("dsdsdsdsdsdsd: " + payBankIdArray.toString());
//		System.out.println("dsdsdsdsdsdsd: " + bankMerIdArray.toString());


		for (int i=0; i<payBankIdArray.length; i++) {
			if (!payBankIdArray[i].equals("null") && !payBankIdArray[i].equals("") && payBankIdArray[i].equalsIgnoreCase(payBankId)) {
				System.out.println("payBankId = " + payBankIdArray[i]);
				System.out.println("bankMerId = " + bankMerIdArray[i]);
				bankMerId = bankMerIdArray[i];
				break;
			}
		}

		return bankMerId;
	}

	public static void setTerminalIdArray(Context context, String payBankTerIdArray) {
		// payBankTerIdArray: null, null, 00001332
		SharedPreferences pref = context.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();

		edit.putString(Constants.pref_TerminalIdArray, payBankTerIdArray);
		System.out.println("payBankTerIdArray = " + payBankTerIdArray);
		edit.commit();
	}

	public static String getTerminalIdArray(Context context) {
		// payBankTerIdArray: null, null, 00001332

		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);

		return pref.getString(Constants.pref_TerminalIdArray, "");
	}

	public static String getTerminalId(Context context, String payBankId) {
		// payBankId: ALIPAY First-Data First-Data

		String terminalId = null;
		String[] payBankIdArray;
		String[] terminalIdArray;

		payBankIdArray = GlobalFunction.getPayBankIdArray(context).split(",");
		terminalIdArray = GlobalFunction.getTerminalIdArray(context).split(",");

		System.out.println("qqqqqqqqqqqqqqqqqq" + GlobalFunction.getPayBankIdArray(context));

		for (int i=0; i<payBankIdArray.length; i++) {

			System.out.println("payBankId = " + payBankIdArray[i]);
			System.out.println("terminalId = " + terminalIdArray[i]);

			if (!payBankIdArray[i].equals("null") && !payBankIdArray[i].equals("") &&
					payBankIdArray[i].equalsIgnoreCase(payBankId) && !terminalIdArray[i].equals("null")) {
				System.out.println("payBankId = " + payBankIdArray[i]);
				System.out.println("terminalId = " + terminalIdArray[i]);
				terminalId = terminalIdArray[i];
				break;
			}
		}

		return terminalId;
	}

	public static Boolean getLoginStatus(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);

		return pref.getBoolean(Constants.pref_LoggedIn, false);
	}

	public static void setLoginStatus(Context context, Boolean status) {
		SharedPreferences pref = context.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();

		edit.putBoolean(Constants.pref_LoggedIn, status);
		edit.commit();
	}

	public static String getLoginTime(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SESSION_DATA, MODE_PRIVATE);

		return pref.getString(Constants.pref_last_login, "");
	}

	public static String getLanguage(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getString(Constants.pref_Lang, Constants.default_language);
	}

	public static void setLanguage(Context context, String language) {
		SharedPreferences pref = context.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();

		edit.putString(Constants.pref_Lang, language);
		edit.commit();
	}

	public static int getTxnHistoryLimit(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

		return pref.getInt(Constants.pref_transaction_checking, 1);
	}

	public static void setAddress1(Context context, String address) {
		SharedPreferences pref = context.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();

		edit.putString(Constants.pref_Address_1, address);
		edit.commit();
	}

	public static String getAddress1(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);

		return pref.getString(Constants.pref_Address_1, "");
	}

	public static void setAddress2(Context context, String address) {
		SharedPreferences pref = context.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();

		edit.putString(Constants.pref_Address_2, address);
		edit.commit();
	}

	public static String getAddress2(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);

		return pref.getString(Constants.pref_Address_2, "");
	}

	public static void setAddress3(Context context, String address) {
		SharedPreferences pref = context.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();

		edit.putString(Constants.pref_Address_3, address);
		edit.commit();
	}

	public static String getAddress3(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);

		return pref.getString(Constants.pref_Address_3, "");
	}

	public static void setMerLogo(Context context, String logo) {
		SharedPreferences pref = context.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();

		edit.putString(Constants.pref_Partnerlogo, logo);
		edit.commit();
	}

	public static Bitmap getMerLogo(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);

		String logo = pref.getString(Constants.pref_Partnerlogo, "");

		LocalCacheUtil initlocalCacheUtil = new LocalCacheUtil();
		Bitmap initbitmap = initlocalCacheUtil.getBitmapFromLocal(logo);

		if (initbitmap == null) {
			if (Constants.pg_paydollar.equalsIgnoreCase(getPrefPayGate(context))) {
				initbitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.logo_paydollar_print_blk);
			} else if (Constants.pg_pesopay.equalsIgnoreCase(getPrefPayGate(context))) {
				initbitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.logo_pesopay_black);
			} else if (Constants.pg_siampay.equalsIgnoreCase(getPrefPayGate(context))) {
				initbitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.logo_siampay_black);
			} else {
				System.out.println("Default Merchant Logo: PayDollar");
				initbitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.logo_paydollar_print_blk);
			}
		}

		return initbitmap;
	}
}
