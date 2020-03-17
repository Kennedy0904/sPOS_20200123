package com.example.dell.smartpos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DialogSendOrder extends Activity {

	private String merName;
	private String merId;
	private String user;
	private String password;
	private String payRef;
	private ProgressBar loading;
	private RelativeLayout result_suc;
	private RelativeLayout result_fail;
	private Button result_OK;
	private Button result_retry;
	private RelativeLayout input_receipt;
	private LinearLayout verify_receipt;
	private LinearLayout result_receipt;
	Button verifyConfirm;

	String gEmail="";
	String orderQty = "";

	//---------for autologout---------//
	Handler CheckTimeOutHandler = new Handler();
	Date lastUpdateTime;//lastest operation time
	long timePeriod;//no operation time
	float SESSION_EXPIRED = Constants.SESSION_EXPIRY/1000;//session expired time
	boolean CheckLogout = false;//logout flag
	long CheckInterval = 1000;//checking time intercal
	//---------for autologout---------//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_send_order);

		//---------for autologout---------//
		SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//		CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout,false);
		lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
		Log.d("OTTO","init DialogSendReceipt CheckLogout:"+CheckLogout+",lastUpdateTime:"+lastUpdateTime);
		//---------for autologout---------//

		Intent sendReceipt = getIntent();
		merName = sendReceipt.getStringExtra(Constants.MERNAME);
		merId = sendReceipt.getStringExtra(Constants.MERID);
		orderQty = sendReceipt.getStringExtra("orderQty");

		loading = (ProgressBar)findViewById(R.id.loading);
		result_OK = (Button)findViewById(R.id.result_OK);
		result_retry = (Button)findViewById(R.id.result_retry);

		input_receipt = (RelativeLayout)findViewById(R.id.input_receipt);
		verify_receipt = (LinearLayout)findViewById(R.id.verify_receipt);
		result_receipt = (LinearLayout)findViewById(R.id.result_receipt);

		input_receipt.setVisibility(View.VISIBLE);
		verify_receipt.setVisibility(View.GONE);
		result_receipt.setVisibility(View.GONE);

		Button inputCancel = (Button)findViewById(R.id.inputCancel);
		Button inputConfirm = (Button)findViewById(R.id.inputConfirm);

		final EditText edtEmail = (EditText)findViewById(R.id.edtEmail);

		//SET COUNTRY CODE
		SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
		String getCountryCode = prefsettings.getString(Constants.pref_CtyCode, "");
		String orderEmail = prefsettings.getString(Constants.pref_SupEmail, Constants.default_supportEmail);

		if(orderEmail.equalsIgnoreCase("")){
			edtEmail.setText(orderEmail);
		}else{
			edtEmail.setText(orderEmail);
		}

		gEmail = edtEmail.getText().toString();

		//GET API FROM DB
		DatabaseHelper db =new DatabaseHelper(this);
		String orgMerchantId = merId;
		String encMerchantId ="";
		DesEncrypter encrypt;
		try {
			encrypt = new DesEncrypter(merName);
			encMerchantId= encrypt.encrypt(orgMerchantId);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		String whereargs[]= {encMerchantId};
		String dbuser = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_API_ID, Constants.DB_MERCHANT_ID, whereargs);
		String dbpassword = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_API_PW, Constants.DB_MERCHANT_ID, whereargs);

		db.close();

		if(dbuser!=null){
			try {
				DesEncrypter encrypter = new DesEncrypter(merName);
				user = encrypter.decrypt(dbuser);
				password = encrypter.decrypt(dbpassword);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			Toast.makeText(this, getString(R.string.apiID_pw_notset), Toast.LENGTH_LONG).show();
		}
		final TextView verifyEmail = (TextView)findViewById(R.id.verifyEmail);

		Button verifyBack = (Button)findViewById(R.id.verifyBack);
		verifyConfirm = (Button)findViewById(R.id.verifyConfirm);

		result_suc = (RelativeLayout)findViewById(R.id.result_suc);
		result_fail = (RelativeLayout)findViewById(R.id.result_fail);

		inputCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		inputConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(edtEmail.getText().toString().trim().equals("")){
					Toast.makeText(DialogSendOrder.this, getString(R.string.pls_mobile_email), Toast.LENGTH_SHORT).show();
				}else{
					gEmail = edtEmail.getText().toString().trim();

					input_receipt.setVisibility(View.GONE);
					verify_receipt.setVisibility(View.VISIBLE);
					result_receipt.setVisibility(View.GONE);
					//For Verify
					if(!gEmail.equals("")){
						verifyEmail.setText(getString(R.string.confirm_email)+ gEmail);
						verifyEmail.setVisibility(View.VISIBLE);
					}else{
						verifyEmail.setVisibility(View.GONE);
					}
				}

			}
		});


		verifyBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				gEmail="";
				verifyEmail.setText("");
				input_receipt.setVisibility(View.VISIBLE);
				verify_receipt.setVisibility(View.GONE);
				result_receipt.setVisibility(View.GONE);
			}
		});

		verifyConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				loading.setVisibility(View.VISIBLE);
				SendOrderTask sendNotif = new SendOrderTask(DialogSendOrder.this, getPrefPayGate());
				sendNotif.execute(
						merId,
						merName,
						orderQty,
						gEmail);
				verifyConfirm.setEnabled(false);
			}
		});

		result_retry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				input_receipt.setVisibility(View.VISIBLE);
				verify_receipt.setVisibility(View.GONE);
				result_receipt.setVisibility(View.GONE);
				loading.setVisibility(View.GONE);
			}
		});

		result_OK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}



	public class SendOrderTask extends AsyncTask<
			String,
			Void,
			String> {

		private String baseUrl2;
		private DialogSendOrder activity;
		private String payGate;
		private HashMap<String, String> map=null;

		private String result="";
		InputStream inputStream=null;

		public SendOrderTask(DialogSendOrder activity, String payGate)
		{
			this.activity = activity;
			this.payGate = payGate;

		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String merchantId = arg0[0];
			String merchantName = arg0[1];
			String orderQty = arg0[2];
			String email = arg0[3];

			NameValuePair merchantIdNVpair = new BasicNameValuePair("merchantId",merchantId);
			NameValuePair merchantNameNVpair = new BasicNameValuePair("merchantName",merchantName);
			NameValuePair orderQtyNVpair = new BasicNameValuePair("orderQty",orderQty);
			NameValuePair emailNVpair = new BasicNameValuePair("email",email);
			List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
			nameValuePairs.add(merchantIdNVpair);
			nameValuePairs.add(merchantNameNVpair);
			nameValuePairs.add(orderQtyNVpair);
			nameValuePairs.add(emailNVpair);
			try{
				baseUrl2 = PayGate.getURL_SendOrder(payGate);
				URL url = new URL(baseUrl2);
				HttpURLConnection con = (HttpURLConnection)
						url.openConnection();
				TrustModifier.relaxHostChecking(con);
				con.setReadTimeout(30000);
				con.setConnectTimeout(25000);
				con.setRequestMethod("POST");
				con.setDoInput(true);
				con.setDoOutput(true);
				OutputStream os = con.getOutputStream();
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(os, "UTF-8"));
				writer.write(PayGate.getQuery(nameValuePairs));
				writer.flush();
				writer.close();
				os.close();

				InputStream in = con.getInputStream();
				BufferedReader reader = null;

				try{
					reader= new BufferedReader(new InputStreamReader(in));
					String line="";
					while((line=reader.readLine())!=null){
						result=result+line;
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{

					if (reader != null) {
						try{
							reader.close();
						}catch(IOException e){
							e.printStackTrace();
						}
					}
				}

			}catch(Exception e){
				e.printStackTrace();
			}

			return result;

		}
		protected void onPostExecute(String result) {

//			System.out.println(result);
			verifyConfirm.setEnabled(true);
			map= PayGate.split(result);
			String successcode=map.get("successcode");
			if(successcode == null){

				activity.fail();
			}
			else if(successcode.equals("0")){
				activity.success();
			}else{
				activity.fail();
			}
		}


	}

	public void success() {
		// TODO Auto-generated method stub
		loading.setVisibility(View.GONE);


		input_receipt.setVisibility(View.GONE);
		verify_receipt.setVisibility(View.GONE);
		result_receipt.setVisibility(View.VISIBLE);

		result_suc.setVisibility(View.VISIBLE);
		result_fail.setVisibility(View.GONE);

		result_OK.setVisibility(View.VISIBLE);
		result_retry.setVisibility(View.GONE);
	}

	public void fail() {
		// TODO Auto-generated method stub
		loading.setVisibility(View.GONE);

		input_receipt.setVisibility(View.GONE);
		verify_receipt.setVisibility(View.GONE);
		result_receipt.setVisibility(View.VISIBLE);

		result_suc.setVisibility(View.GONE);
		result_fail.setVisibility(View.VISIBLE);

		result_OK.setVisibility(View.GONE);
		result_retry.setVisibility(View.VISIBLE);
	}

	public String getPrefPayGate(){
		SharedPreferences prefsettings = getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
		String prefpaygate = prefsettings.getString("PayGate", Constants.default_paygate);
		return prefpaygate;
	}

	public boolean isSmsEnabled(String merchantId){
		DatabaseHelper db =new DatabaseHelper(this);
		String whereargs[]= {merchantId};
		String dbSMS = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_ENABLE_SMS, Constants.DB_MERCHANT_ID, whereargs);
		db.close();
		if(dbSMS!=null){
			if(dbSMS.equalsIgnoreCase("T")){
				return true;
			}
			return false;
		}
		return false;
	}

	//-------------------------------------auto logout-------------------------------------//
	/**
	 * Time counter Thread
	 */
	private Runnable checkTimeOutTask = new Runnable() {

		public void run() {
			Date timeNow = new Date(System.currentTimeMillis());
			//calculate no operation time
			/*curTime - lastest opt time = no opt time*/
			timePeriod = timeNow.getTime() - lastUpdateTime.getTime();

			/*converted into second*/
			float timePeriodSecond = ((float) timePeriod / 1000);

			if(CheckLogout){
				/* do logout */
				Log.d("OTTO","做登出操作"+this.getClass());
				//logout flag change to true
				CheckLogout = true;
//				autologout();
			}else{
				if( timePeriodSecond > SESSION_EXPIRED ){
					/* do logout */
					Log.d("OTTO","做登出操作"+this.getClass());
					//logout flag change to true
					CheckLogout = true;
//					autologout();
				}else{
					Log.d("OTTO", "没有超过规定时长"+this.getClass());
				}
			}

			if(!CheckLogout) {
				//check no opt time per 'CheckInterval' sencond
				CheckTimeOutHandler.postDelayed(checkTimeOutTask, CheckInterval);
			}
		}
	};

	//listen touch movement
	public boolean dispatchTouchEvent(MotionEvent event) {
		Log.d("OTTO","onTouchEvent checklogout:"+CheckLogout);
		if( !CheckLogout ) {
			updateUserActionTime();
		}
		return super.dispatchTouchEvent(event);
	}

	//reset no opt time and lastest opt time when user opt
	public void updateUserActionTime() {
		Date timeNow = new Date(System.currentTimeMillis());
		timePeriod = timeNow.getTime() - lastUpdateTime.getTime();
		lastUpdateTime.setTime(timeNow.getTime());
	}

//	public void autologout(){
//		SharedPreferences pref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//		SharedPreferences.Editor edit = pref.edit();
//		edit.putBoolean(Constants.CheckLogout,true);
//		edit.commit();
//	}

	@Override
	protected void onResume() {
		//start check timeout thread when activity show
		CheckTimeOutHandler.postAtTime(checkTimeOutTask, CheckInterval);
		super.onResume();
	}

	@Override
	protected void onPause() {
		/*activity不可见的时候取消线程*/
		//stop check timeout thread when activity no show
		CheckTimeOutHandler.removeCallbacks(checkTimeOutTask);
		super.onPause();
	}
	//-------------------------------------auto logout-------------------------------------//
}
