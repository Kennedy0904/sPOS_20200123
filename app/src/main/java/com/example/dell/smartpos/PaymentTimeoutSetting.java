package com.example.dell.smartpos;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;

public class PaymentTimeoutSetting extends AppCompatActivity {

	private final static String TABLE_ID = "Settings";
	private final static String MERCHANT_ID = "merID";

	private Button btnAddTimeCard;
	private Button btnAddTimeAlipay;
	private Button btnAddTimeAlipayHK;
	private Button btnAddTimeBoost;
	private Button btnAddTimeGrabPay;
	private Button btnAddTimeGCash;
	private Button btnAddTimeOePay;
	private Button btnAddTimePromptPay;
	private Button btnAddTimeUnionPay;
	private Button btnAddTimeWechat;
	private Button btnAddTimeWechatHK;
	private Button btnMinusTimeCard;
	private Button btnMinusTimeAlipay;
	private Button btnMinusTimeAlipayHK;
	private Button btnMinusTimeBoost;
	private Button btnMinusTimeGrabPay;
	private Button btnMinusTimeGCash;
	private Button btnMinusTimeOePay;
	private Button btnMinusTimePromptPay;
	private Button btnMinusTimeUnionPay;
	private Button btnMinusTimeWechat;
	private Button btnMinusTimeWechatHK;
	private Button btnOK;
	private TextView timeCard;
	private TextView timeAlipay;
	private TextView timeAlipayHK;
	private TextView timeBoost;
	private TextView timeGrabPay;
	private TextView timeGCash;
	private TextView timeOePay;
	private TextView timePromptPay;
	private TextView timeUnionPay;
	private TextView timeWechat;
	private TextView timeWechatHK;

	private LinearLayout Card;
	private LinearLayout Alipay;
	private LinearLayout AlipayHK;
	private LinearLayout Boost;
	private LinearLayout GrabPay;
	private LinearLayout GCash;
	private LinearLayout OePay;
	private LinearLayout PromptPay;
	private LinearLayout UnionPay;
	private LinearLayout Wechat;
	private LinearLayout WechatHK;

	String merchantId = "";
	String merchantName = "";
	String payMethod = "";
	String cardPayBankId = "";

	Boolean CP = false;
	Boolean A = false;
	Boolean AHK = false;
	Boolean B = false;
	Boolean GP = false;
	Boolean GC = false;
	Boolean OE = false;
	Boolean PP = false;
	Boolean UP = false;
	Boolean W = false;
	Boolean WHK = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_timeout_setting);
		this.setTitle(R.string.setting_payment_timeout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent settingIntent = getIntent();
		String orgMerchantId = settingIntent.getStringExtra(Constants.MERID);
		merchantName = settingIntent.getStringExtra(Constants.MERNAME);
		payMethod = settingIntent.getStringExtra(Constants.PAYMETHODLIST);

		btnAddTimeCard = (Button) findViewById(R.id.addTimeCard);
		btnMinusTimeCard = (Button) findViewById(R.id.minusTimeCard);
		btnAddTimeAlipay = (Button) findViewById(R.id.addTimeAlipay);
		btnMinusTimeAlipay = (Button) findViewById(R.id.minusTimeAlipay);
		btnAddTimeAlipayHK = (Button) findViewById(R.id.addTimeAlipayHK);
		btnMinusTimeAlipayHK = (Button) findViewById(R.id.minusTimeAlipayHK);
		btnAddTimeBoost = (Button) findViewById(R.id.addTimeBoost);
		btnMinusTimeBoost = (Button) findViewById(R.id.minusTimeBoost);
		btnAddTimeGrabPay = (Button) findViewById(R.id.addTimeGrabPay);
		btnMinusTimeGrabPay = (Button) findViewById(R.id.minusTimeGrabPay);
		btnAddTimeGCash = (Button) findViewById(R.id.addTimeGCash);
		btnMinusTimeGCash = (Button) findViewById(R.id.minusTimeGCash);
		btnAddTimeOePay = (Button) findViewById(R.id.addTimeOePay);
		btnMinusTimeOePay = (Button) findViewById(R.id.minusTimeOePay);
		btnAddTimePromptPay = (Button) findViewById(R.id.addTimePromptPay);
		btnMinusTimePromptPay = (Button) findViewById(R.id.minusTimePromptPay);
		btnAddTimeUnionPay = (Button) findViewById(R.id.addTimeUnionPay);
		btnMinusTimeUnionPay = (Button) findViewById(R.id.minusTimeUnionPay);
		btnAddTimeWechat = (Button) findViewById(R.id.addTimeWechat);
		btnMinusTimeWechat = (Button) findViewById(R.id.minusTimeWechatPay);
		btnAddTimeWechatHK = (Button) findViewById(R.id.addTimeWechatHK);
		btnMinusTimeWechatHK = (Button) findViewById(R.id.minusTimeWechatPayHK);

		btnOK = (Button) findViewById(R.id.btnOK);

		timeCard = (TextView) findViewById(R.id.timeCard);
		timeAlipay = (TextView) findViewById(R.id.timeAlipay);
		timeAlipayHK = (TextView) findViewById(R.id.timeAlipayHK);
		timeBoost = (TextView) findViewById(R.id.timeBoost);
		timeGrabPay = (TextView) findViewById(R.id.timeGrabPay);
		timeGCash = (TextView) findViewById(R.id.timeGCash);
		timeOePay = (TextView) findViewById(R.id.timeOePay);
		timePromptPay = (TextView) findViewById(R.id.timePromptPay);
		timeUnionPay = (TextView) findViewById(R.id.timeUnionPay);
		timeWechat = (TextView) findViewById(R.id.timeWechat);
		timeWechatHK = (TextView) findViewById(R.id.timeWechatHK);

		String[] payMethod_CARD = {"VISA", "Master"};
		String[] payMethod_ALIPAYHK = {"ALIPAYHKOFFL"};
		String[] payMethod_ALIPAY = {"ALIPAYOFFL"};
		String[] payMethod_BOOSTOFFL = {"BOOSTOFFL"};
		String[] payMethod_GRABPAYOFFL = {"GRABPAYOFFL"};
		String[] payMethod_GCASHOFFL = {"GCASHOFFL"};
		String[] payMethod_OEPAYOFFL = {"OEPAYOFFL"};
		String[] payMethod_PROMPTPAY = {"PROMPTPAY"};
		String[] payMethod_UNIONPAYOFFL = {"UNIONPAYOFFL"};
		String[] payMethod_WECHATOFFL = {"WECHATOFFL"};
		String[] payMethod_WECHATHKOFFL = {"WECHATHKOFFL"};

		CP = containstr(payMethod, payMethod_CARD);
		A = containstr(payMethod, payMethod_ALIPAY);
		AHK = containstr(payMethod, payMethod_ALIPAYHK);
		B = containstr(payMethod, payMethod_BOOSTOFFL);
		GP = containstr(payMethod, payMethod_GRABPAYOFFL);
		GC = containstr(payMethod, payMethod_GCASHOFFL);
		OE = containstr(payMethod, payMethod_OEPAYOFFL);
		PP = containstr(payMethod, payMethod_PROMPTPAY);
		UP = containstr(payMethod, payMethod_UNIONPAYOFFL);
		W = containstr(payMethod, payMethod_WECHATOFFL);
		WHK = containstr(payMethod, payMethod_WECHATHKOFFL);

		Card = (LinearLayout) findViewById(R.id.card);
		Alipay = (LinearLayout) findViewById(R.id.AliPay);
		AlipayHK = (LinearLayout) findViewById(R.id.AliPayHK);
		Boost = (LinearLayout) findViewById(R.id.boost);
		GrabPay = (LinearLayout) findViewById(R.id.grabpay);
		GCash = (LinearLayout) findViewById(R.id.gcash);
		OePay = (LinearLayout) findViewById(R.id.oepay);
		PromptPay = (LinearLayout) findViewById(R.id.promptpay);
		UnionPay = (LinearLayout) findViewById(R.id.unionPay);
		Wechat = (LinearLayout) findViewById(R.id.weChatPay);
		WechatHK = (LinearLayout) findViewById(R.id.weChatPayHK);

		if (orgMerchantId.equals("560200335") || orgMerchantId.equals("560200303")) {
			CP = true;
			A = true;
			AHK = true;
			B = true;
			GP = true;
			GC = true;
			OE = true;
			PP = true;
			UP = true;
			W = true;
			WHK = true;

		}

/*		else if (orgMerchantId.substring(0, 2).equals("85")) {
			B = true;
			GP = true;
		} else if (orgMerchantId.substring(0, 2).equals("76")) {
			PP = true;
		} else if (orgMerchantId.substring(0, 2).equals("88")) {
			OE = true;
		} else if (orgMerchantId.substring(0, 2).equals("18")) {
			GC = true;
		}*/

		cardPayBankId = GlobalFunction.getCardPayBankId(PaymentTimeoutSetting.this);

		if (CP) {
			if (!cardPayBankId.equalsIgnoreCase(Constants.FIRST_DATA)) {
				Card.setVisibility(View.VISIBLE);
			}
		}

		if (A) {
			Alipay.setVisibility(View.VISIBLE);
		}

		if (AHK) {
			AlipayHK.setVisibility(View.VISIBLE);
		}

		if (B) {
			Boost.setVisibility(View.VISIBLE);
		}

		if (GP) {
			GrabPay.setVisibility(View.VISIBLE);
		}

		if (GC) {
			GCash.setVisibility(View.VISIBLE);
		}

		if (OE) {
			OePay.setVisibility(View.VISIBLE);
		}

		if (PP) {
			PromptPay.setVisibility(View.VISIBLE);
		}

		if (UP) {
			UnionPay.setVisibility(View.VISIBLE);
		}

		if (W) {
			Wechat.setVisibility(View.VISIBLE);
		}

		if (WHK) {
			WechatHK.setVisibility(View.VISIBLE);
		}

		SharedPreferences pref = this.getApplicationContext().getSharedPreferences(
				Constants.SETTINGS, MODE_PRIVATE);

		String cardTimeout = pref.getString(Constants.pref_card_timeout, Constants.default_payment_timeout);
		final String alipayTimeout = pref.getString(Constants.pref_alipay_timeout, Constants.default_payment_timeout);
		final String alipayHKTimeout = pref.getString(Constants.pref_alipayhk_timeout, Constants.default_payment_timeout);
		final String boostTimeout = pref.getString(Constants.pref_boost_timeout, Constants.default_payment_timeout);
		final String grabpayTimeout = pref.getString(Constants.pref_grabpay_timeout, Constants.default_payment_timeout);
		final String gcashTimeout = pref.getString(Constants.pref_gcash_timeout, Constants.default_payment_timeout);
		final String oepayTimeout = pref.getString(Constants.pref_oepay_timeout, Constants.default_payment_timeout);
		final String promptpayTimeout = pref.getString(Constants.pref_promptpay_timeout, Constants.default_payment_timeout);
		final String unionpayTimeout = pref.getString(Constants.pref_unionpay_timeout, Constants.default_payment_timeout);
		final String wechatTimeout = pref.getString(Constants.pref_wechat_timeout, Constants.default_payment_timeout);
		final String wechatHKTimeout = pref.getString(Constants.pref_wechathk_timeout, Constants.default_payment_timeout);

		timeCard.setText(cardTimeout);
		timeAlipay.setText(alipayTimeout);
		timeAlipayHK.setText(alipayHKTimeout);
		timeBoost.setText(boostTimeout);
		timeGrabPay.setText(grabpayTimeout);
		timeGCash.setText(gcashTimeout);
		timeOePay.setText(oepayTimeout);
		timePromptPay.setText(promptpayTimeout);
		timeUnionPay.setText(unionpayTimeout);
		timeWechat.setText(wechatTimeout);
		timeWechatHK.setText(wechatHKTimeout);

		btnAddTimeCard.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String qtyString = timeCard.getText().toString();
				int qty = Integer.parseInt(qtyString) + 1;

				if (qtyString.equals("30")) {
					timeCard.setText(Integer.toString(30));
				} else {
					timeCard.setText(Integer.toString(qty));
				}
			}

		});

		btnMinusTimeCard.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String qtyString = timeCard.getText().toString();
				int qty = Integer.parseInt(qtyString) - 1;

				if (qtyString.equals("1")) {
					timeCard.setText(Integer.toString(1));
				} else {
					timeCard.setText(Integer.toString(qty));
				}
			}

		});

		btnAddTimeAlipay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String qtyString = timeAlipay.getText().toString();
				int qty = Integer.parseInt(qtyString) + 1;

				if (qtyString.equals("30")) {
					timeAlipay.setText(Integer.toString(30));
				} else {
					timeAlipay.setText(Integer.toString(qty));
				}
			}

		});

		btnMinusTimeAlipay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String qtyString = timeAlipay.getText().toString();
				int qty = Integer.parseInt(qtyString) - 1;

				if (qtyString.equals("1")) {
					timeAlipay.setText(Integer.toString(1));
				} else {
					timeAlipay.setText(Integer.toString(qty));
				}
			}

		});

		btnAddTimeAlipayHK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String qtyString = timeAlipayHK.getText().toString();
				int qty = Integer.parseInt(qtyString) + 1;

				if (qtyString.equals("30")) {
					timeAlipayHK.setText(Integer.toString(30));
				} else {
					timeAlipayHK.setText(Integer.toString(qty));
				}
			}

		});

		btnMinusTimeAlipayHK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String qtyString = timeAlipayHK.getText().toString();
				int qty = Integer.parseInt(qtyString) - 1;

				if (qtyString.equals("1")) {
					timeAlipayHK.setText(Integer.toString(1));
				} else {
					timeAlipayHK.setText(Integer.toString(qty));
				}
			}

		});

		btnAddTimeBoost.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String qtyString = timeBoost.getText().toString();
				int qty = Integer.parseInt(qtyString) + 1;

				if (qtyString.equals("30")) {
					timeBoost.setText(Integer.toString(30));
				} else {
					timeBoost.setText(Integer.toString(qty));
				}
			}

		});

		btnMinusTimeBoost.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String qtyString = timeBoost.getText().toString();
				int qty = Integer.parseInt(qtyString) - 1;

				if (qtyString.equals("1")) {
					timeBoost.setText(Integer.toString(1));
				} else {
					timeBoost.setText(Integer.toString(qty));
				}
			}

		});

		btnAddTimeGrabPay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String qtyString = timeGrabPay.getText().toString();
				int qty = Integer.parseInt(qtyString) + 1;

				if (qtyString.equals("30")) {
					timeGrabPay.setText(Integer.toString(30));
				} else {
					timeGrabPay.setText(Integer.toString(qty));
				}
			}

		});

		btnMinusTimeGrabPay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String qtyString = timeGrabPay.getText().toString();
				int qty = Integer.parseInt(qtyString) - 1;

				if (qtyString.equals("1")) {
					timeGrabPay.setText(Integer.toString(1));
				} else {
					timeGrabPay.setText(Integer.toString(qty));
				}
			}

		});

		btnAddTimeGCash.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String qtyString = timeGCash.getText().toString();
				int qty = Integer.parseInt(qtyString) + 1;

				if (qtyString.equals("30")) {
					timeGCash.setText(Integer.toString(30));
				} else {
					timeGCash.setText(Integer.toString(qty));
				}
			}

		});

		btnMinusTimeGCash.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String qtyString = timeGCash.getText().toString();
				int qty = Integer.parseInt(qtyString) - 1;

				if (qtyString.equals("1")) {
					timeGCash.setText(Integer.toString(1));
				} else {
					timeGCash.setText(Integer.toString(qty));
				}
			}

		});

		btnAddTimeOePay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String qtyString = timeOePay.getText().toString();
				int qty = Integer.parseInt(qtyString) + 1;

				if (qtyString.equals("30")) {
					timeOePay.setText(Integer.toString(30));
				} else {
					timeOePay.setText(Integer.toString(qty));
				}
			}

		});

		btnMinusTimeOePay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String qtyString = timeOePay.getText().toString();
				int qty = Integer.parseInt(qtyString) - 1;

				if (qtyString.equals("1")) {
					timeOePay.setText(Integer.toString(1));
				} else {
					timeOePay.setText(Integer.toString(qty));
				}
			}

		});

		btnAddTimePromptPay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String qtyString = timePromptPay.getText().toString();
				int qty = Integer.parseInt(qtyString) + 1;

				if (qtyString.equals("10")) {
					timePromptPay.setText(Integer.toString(10));
				} else {
					timePromptPay.setText(Integer.toString(qty));
				}
			}

		});

		btnMinusTimePromptPay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String qtyString = timePromptPay.getText().toString();
				int qty = Integer.parseInt(qtyString) - 1;

				if (qtyString.equals("1")) {
					timePromptPay.setText(Integer.toString(1));
				} else {
					timePromptPay.setText(Integer.toString(qty));
				}
			}

		});

		btnAddTimeUnionPay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String qtyString = timeUnionPay.getText().toString();
				int qty = Integer.parseInt(qtyString) + 1;

				if (qtyString.equals("30")) {
					timeUnionPay.setText(Integer.toString(30));
				} else {
					timeUnionPay.setText(Integer.toString(qty));
				}
			}

		});

		btnMinusTimeUnionPay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String qtyString = timeUnionPay.getText().toString();
				int qty = Integer.parseInt(qtyString) - 1;

				if (qtyString.equals("1")) {
					timeUnionPay.setText(Integer.toString(1));
				} else {
					timeUnionPay.setText(Integer.toString(qty));
				}
			}

		});

		btnAddTimeWechat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String qtyString = timeWechat.getText().toString();
				int qty = Integer.parseInt(qtyString) + 1;

				if (qtyString.equals("30")) {
					timeWechat.setText(Integer.toString(30));
				} else {
					timeWechat.setText(Integer.toString(qty));
				}
			}

		});

		btnMinusTimeWechat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String qtyString = timeWechat.getText().toString();
				int qty = Integer.parseInt(qtyString) - 1;

				if (qtyString.equals("1")) {
					timeWechat.setText(Integer.toString(1));
				} else {
					timeWechat.setText(Integer.toString(qty));
				}

			}

		});

		btnAddTimeWechatHK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String qtyString = timeWechatHK.getText().toString();
				int qty = Integer.parseInt(qtyString) + 1;

				if (qtyString.equals("30")) {
					timeWechatHK.setText(Integer.toString(30));
				} else {
					timeWechatHK.setText(Integer.toString(qty));
				}
			}

		});

		btnMinusTimeWechatHK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String qtyString = timeWechatHK.getText().toString();
				int qty = Integer.parseInt(qtyString) - 1;

				if (qtyString.equals("1")) {
					timeWechatHK.setText(Integer.toString(1));
				} else {
					timeWechatHK.setText(Integer.toString(qty));
				}

			}

		});

		final SharedPreferences.Editor edit = pref.edit();

		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				edit.putString(Constants.pref_card_timeout, timeCard.getText().toString());
				edit.putString(Constants.pref_alipay_timeout, timeAlipay.getText().toString());
				edit.putString(Constants.pref_alipayhk_timeout, timeAlipayHK.getText().toString());
				edit.putString(Constants.pref_boost_timeout, timeBoost.getText().toString());
				edit.putString(Constants.pref_grabpay_timeout, timeGrabPay.getText().toString());
				edit.putString(Constants.pref_gcash_timeout, timeGCash.getText().toString());
				edit.putString(Constants.pref_oepay_timeout, timeOePay.getText().toString());
				edit.putString(Constants.pref_promptpay_timeout, timePromptPay.getText().toString());
				edit.putString(Constants.pref_unionpay_timeout, timeUnionPay.getText().toString());
				edit.putString(Constants.pref_wechat_timeout, timeWechat.getText().toString());
				edit.putString(Constants.pref_wechathk_timeout, timeWechatHK.getText().toString());
				edit.commit();

				finish();
			}

		});

	}

	public static boolean containstr(String str, String[] s) {
		boolean flag = false;
		for (int i = 0; i < s.length; i++) {
			if (str.contains(s[i])) {
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	public void setFragment(Fragment fragment) {

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		//        fragmentTransaction.replace(R.id.main_frame, fragment).addToBackStack(null);
		fragmentTransaction.commit();
	}
}
