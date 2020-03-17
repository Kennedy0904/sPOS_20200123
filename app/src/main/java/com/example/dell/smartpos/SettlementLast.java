package com.example.dell.smartpos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dell.smartpos.FDMS.FdmsApiFunction;
import com.example.dell.smartpos.Printer.PrintUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static java.lang.Double.parseDouble;

public class SettlementLast extends AppCompatActivity {

	FdmsApiFunction fdmsRequest;
	PrintUtil printUtil;
	JSONObject resultObject;
	JSONArray resultArray;

	TreeMap<String,String> label;
	TreeMap<String,String> labelValue;

	ArrayList<Record> masterRecordArray;
	ArrayList<Record> visaRecordArray;

	FrameLayout progressBar;

	LoadSettlementXML loadxml;
	ProgressDialog progressDialog;

	Button btnReprintSettlement;

	TextView tvBatchNo;
	TextView tvSettlementTime;
	TextView tvHostName;
	TextView tvGrandTotalSaleTxn;
	TextView tvGrandTotalSaleAmount;
	TextView tvGrandTotalVoidTxn;
	TextView tvGrandTotalVoidAmount;
	TextView tvGrandTotalAmount;

	String merID;
	String merName;
	String apiID;
	String apiPW;
	String userID;
	String settlementAcquirer = "";
	String currName;
	String settlementTime;
	String batchNo;

	String label_title;
	String label_master;
	String label_visa;


	int grandTotalSaleTxn = 0;
	Double grandTotalSaleAmount = 0.00;
	int grandTotalVoidTxn = 0;
	Double grandTotalVoidAmount = 0.00;
	int visaTotalSaleTxn = 0;
	Double visaTotalSaleAmount = 0.00;
	int visaTotalVoidTxn = 0;
	Double visaTotalVoidAmount = 0.00;
	int masterTotalSaleTxn = 0;
	Double masterTotalSaleAmount = 0.00;
	int masterTotalVoidTxn = 0;
	Double masterTotalVoidAmount = 0.00;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settlement_process_last);

		setTitle(R.string.settlement);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		//Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_history);
		//setSupportActionBar(toolbar);

		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		progressBar = (FrameLayout) findViewById(R.id.settlementLastProgressBar);

		btnReprintSettlement = (Button) findViewById(R.id.btnReprintSettlement);

		tvBatchNo = (TextView) findViewById(R.id.tvBatchNo);
		tvSettlementTime = (TextView) findViewById(R.id.tvSettlementTime);
		tvHostName = (TextView) findViewById(R.id.tvHostName);
		tvGrandTotalSaleTxn = (TextView) findViewById(R.id.tvGrandTotalSaleTxn);
		tvGrandTotalSaleAmount = (TextView) findViewById(R.id.tvGrandTotalSaleAmount);
		tvGrandTotalVoidTxn = (TextView) findViewById(R.id.tvGrandTotalVoidTxn);
		tvGrandTotalVoidAmount = (TextView) findViewById(R.id.tvGrandTotalVoidAmount);
		tvGrandTotalAmount = (TextView) findViewById(R.id.tvGrandTotalAmount);

		Intent intent = getIntent();
		merID = intent.getStringExtra(Constants.MERID);
		merName = intent.getStringExtra(Constants.MERNAME);

		// Reset value back to zero
		grandTotalSaleTxn = 0;
		grandTotalSaleAmount = 0.00;
		grandTotalVoidTxn = 0;
		grandTotalVoidAmount = 0.00;

		visaTotalSaleTxn = 0;
		visaTotalSaleAmount = 0.00;
		visaTotalVoidTxn = 0;
		visaTotalVoidAmount = 0.00;

		masterTotalSaleTxn = 0;
		masterTotalSaleAmount = 0.00;
		masterTotalVoidTxn = 0;
		masterTotalVoidAmount = 0.00;

		// Get settlement acquirer
		settlementAcquirer = GlobalFunction.getCardPayBankId(SettlementLast.this);
		System.out.println("settlementAcquirer: " + settlementAcquirer);

		if (settlementAcquirer.toLowerCase().contains("first-data")) {
			settlementAcquirer = Constants.FIRST_DATA;
			btnReprintSettlement.setVisibility(View.VISIBLE);
		}

		getApiAccount();

		loadxml = new LoadSettlementXML(GlobalFunction.getPrefPayGate(
				SettlementLast.this), settlementAcquirer);
		loadxml.execute();

		btnReprintSettlement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				preparePrintContent();
			}
		});
	}

	private class LoadSettlementXML extends AsyncTask<String, Void, String> {
		private String payGate;

		private String acquirer;
		private String baseUrl = null;
		private String result = "";

		public LoadSettlementXML(String payGate, String acquirer) {
			this.payGate = payGate;
			this.acquirer = acquirer;
		}

		@Override
		protected void onPreExecute() {
			// Show the progress bar
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... arg0) {

			while (!isCancelled()) {
				Calendar c = Calendar.getInstance();
				String beginDay = "01/01/2015";
				String today = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
				String d1 = beginDay.replace("/", "") + "000000";
				String d2 = today.replace("/", "") + "235959";

				batchNo = GlobalFunction.getLastBatchNo(SettlementLast.this);


				NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", merID);
				NameValuePair loginIdNVPair = new BasicNameValuePair("loginId", apiID);
				NameValuePair passwordNVPair = new BasicNameValuePair("password", apiPW);
				NameValuePair startDateNVPair = new BasicNameValuePair("startDate", d1);
				NameValuePair endDateNVPair = new BasicNameValuePair("endDate", d2);
				NameValuePair batchNoNVpair = new BasicNameValuePair("batchNo", batchNo);
				NameValuePair mobileNVPair = new BasicNameValuePair("enableMobile", "T");
				// sort orderId
				NameValuePair sortOrderNVpair = new BasicNameValuePair("sortOrder", "asc");
				NameValuePair operatorIdNVPair = new BasicNameValuePair("operatorId", userID);
				NameValuePair orderStatusNVPair = new BasicNameValuePair("orderStatus", "Accepted");
				NameValuePair payBankIdNVPair = new BasicNameValuePair("payBankId", acquirer);
				NameValuePair settlementStatusIdNVPair = new BasicNameValuePair("settlementStatus",
						"T");

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(merchantIdNVPair);
				nameValuePairs.add(loginIdNVPair);
				nameValuePairs.add(passwordNVPair);
				nameValuePairs.add(startDateNVPair);
				nameValuePairs.add(endDateNVPair);
				nameValuePairs.add(batchNoNVpair);
				nameValuePairs.add(mobileNVPair);
				nameValuePairs.add(sortOrderNVpair);
				nameValuePairs.add(operatorIdNVPair);
				//nameValuePairs.add(orderStatusNVPair);
				nameValuePairs.add(payBankIdNVPair);
				nameValuePairs.add(settlementStatusIdNVPair);

				try {
					baseUrl = PayGate.getURL_genTxtXMLMPOS_Settlement(payGate);
					URL url = new URL(baseUrl);

					System.out.println("Request URL    : " + baseUrl);
					System.out.println("Request Params : " + PayGate.getQuery(nameValuePairs));

					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					TrustModifier.relaxHostChecking(con);
					con.setReadTimeout(15000);
					con.setConnectTimeout(15000);
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

					try {
						reader = new BufferedReader(new InputStreamReader(in));
						String line = "";
						while ((line = reader.readLine()) != null) {
							result = result + line;
						}

						String text = result;
						System.out.println("result: " + result);
						InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
						XML2Record xml2Record = new XML2Record();
						Xml.parse(is, Xml.Encoding.UTF_8, xml2Record);
						List<Record> records = xml2Record.getRecords();
						for (Record record : records) {

							// Prevent negative amount txn view by user
							// e.g. voided txn will have two txn,
							// one is negative and one is positive
							settlementTime = record.getSettleTime();

							if (currName == null) {
								currName = record.currency();
							}

							if (GlobalFunction.isCardpMethods(record.getPaymethod())) {
								if ((record.orderstatus().equalsIgnoreCase("Accepted") || record.orderstatus().equalsIgnoreCase("Voided")) && parseDouble(record.getamt()) > 0) {
									grandTotalSaleTxn++;
									grandTotalSaleAmount += Double.parseDouble(record.getamt());
								} else if (record.orderstatus().equalsIgnoreCase("Voided") && parseDouble(record.getamt()) < 0) {
									grandTotalVoidTxn++;
									// Get voided txn negative value
									grandTotalVoidAmount += Double.parseDouble(record.getamt());
								}

								if (record.getPaymethod() != null &&
										record.getPaymethod().equalsIgnoreCase("VISA")) {
									if ((record.orderstatus().equalsIgnoreCase("Accepted") || record.orderstatus().equalsIgnoreCase("Voided")) && parseDouble(record.getamt()) > 0) {
										visaTotalSaleTxn++;
										visaTotalSaleAmount += Double.parseDouble(record.getamt());
									} else if (record.orderstatus().equalsIgnoreCase("Voided") && parseDouble(record.getamt()) < 0) {
										visaTotalVoidTxn++;
										// Get voided txn negative value
										visaTotalVoidAmount += Double.parseDouble(record.getamt());
									}
								} else if (record.getPaymethod() != null &&
										record.getPaymethod().equalsIgnoreCase("Master")) {
									if ((record.orderstatus().equalsIgnoreCase("Accepted") || record.orderstatus().equalsIgnoreCase("Voided")) && parseDouble(record.getamt()) > 0) {
										masterTotalSaleTxn++;
										masterTotalSaleAmount += Double.parseDouble(record.getamt());
									} else if (record.orderstatus().equalsIgnoreCase("Voided") && parseDouble(record.getamt()) < 0) {
										masterTotalVoidTxn++;
										// Get voided txn negative value
										masterTotalVoidAmount += Double.parseDouble(record.getamt());
									}
								}
							}
						} //end for loop

					} catch (Exception e) {
						System.out.println("RRR");
						e.printStackTrace();
					} finally {
						if (reader != null) {
							try {
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();

					// Check isCancelled() to prevent alertDialog show on a closed activity
					// that cause application crash
					if (!isCancelled()) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								GlobalFunction.showAlertDialog(SettlementLast.this,
										getString(R.string.failed),
										getString(R.string.FAILED_ERR_MSG_2));
								btnReprintSettlement.setVisibility(View.GONE);
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();

					// Check isCancelled() to prevent alertDialog show on a closed activity
					// that cause application crash
					if (!isCancelled()) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {

								// Check internet connection
								if (!GlobalFunction.isNetworkConnected(SettlementLast.this)) {
									GlobalFunction.showAlertDialog(SettlementLast.this,
											getString(R.string.failed),
											getString(R.string.notConnected));

								} else {
									GlobalFunction.showAlertDialog(SettlementLast.this,
											getString(R.string.failed),
											getString(R.string.FAILED_ERR_MSG_1));

								}

								btnReprintSettlement.setVisibility(View.GONE);
							}
						});
					}
				}
				return result;
			}
			return null;
		}

		@SuppressLint("DefaultLocale")
		@Override
		protected void onPostExecute(String result) {

			// Close the progress bar
			progressBar.setVisibility(View.INVISIBLE);

			//GlobalFunction.setBatchNo(SettlementCurrent.this, 3);
			if (currName == null) {
				currName = GlobalFunction.getCurrName(SettlementLast.this);
				System.out.println("Set default currName value");
			}

			try {
				// Prepare JSON array to create dynamic scrollview
				resultObject = new JSONObject();
				resultArray = new JSONArray();

				resultObject.put("date", settlementTime.substring(0,10));
				resultObject.put("time", settlementTime.substring(11));
				resultObject.put("mid", GlobalFunction.getBankMerId(SettlementLast.this,
						settlementAcquirer));
				resultObject.put("tid", GlobalFunction.getTerminalId(SettlementLast.this, settlementAcquirer));
				resultObject.put("batchNo", batchNo);
				resultObject.put("host", settlementAcquirer);


				//GlobalFunction.setBatchNo(SettlementCurrent.this, 3);
				if (currName == null) {
					currName = GlobalFunction.getCurrName(SettlementLast.this);
					System.out.println("Set default currName value");
				}

				if (visaTotalSaleTxn != 0 || visaTotalVoidTxn != 0) {
					JSONObject visaData = new JSONObject();
					visaData.put("method", "VISA");
					visaData.put("saleTxn", visaTotalSaleTxn);
					visaData.put("saleAmount", currName + " " + String.format("%,.2f", visaTotalSaleAmount));
					visaData.put("refundTxn", 0);
					visaData.put("refundAmount", currName + " " + String.format("%,.2f", 0.0));
					visaData.put("voidTxn", visaTotalVoidTxn);
					visaData.put("voidAmount", "- " + currName + " " + String.format("%,.2f",
							(visaTotalVoidAmount < 0) ? visaTotalVoidAmount * -1 : 0));
					visaData.put("voidRefundTxn", 0);
					visaData.put("voidRefundAmount", currName + " " + String.format("%,.2f", 0.0));
					visaData.put("offlineTxn", 0);
					visaData.put("offlineAmount", currName + " " + String.format("%,.2f", 0.0));
					visaData.put("preAuthTxn", 0);
					visaData.put("preAuthAmount", currName + " " + String.format("%,.2f", 0.0));
					visaData.put("compCancelTxn", 0);
					visaData.put("compCancelAmount", currName + " " + String.format("%,.2f", 0.0));

					// visaTotalVoidAmount is negative number, so use addition;
					int totalTxn = visaTotalSaleTxn + visaTotalVoidTxn;
					double totalAmount = visaTotalSaleAmount + visaTotalVoidAmount;

					visaData.put("totalTxn",totalTxn);
					visaData.put("totalAmount", (totalAmount > 0) ? currName + " " + String.format(
							"%,.2f", totalAmount) : "- " + currName + " " + String.format(
							"%,.2f", totalAmount * -1));

					resultArray.put(visaData);
				}

				if (masterTotalSaleTxn != 0 || masterTotalVoidTxn != 0) {
					JSONObject masterData = new JSONObject();
					masterData.put("method", "MASTER");
					masterData.put("saleTxn", masterTotalSaleTxn);
					masterData.put("saleAmount", currName + " " + String.format("%,.2f",
							masterTotalSaleAmount));
					masterData.put("refundTxn", 0);
					masterData.put("refundAmount", currName + " " + String.format("%,.2f", 0.0));
					masterData.put("voidTxn", masterTotalVoidTxn);
					masterData.put("voidAmount", "- " + currName + " " + String.format("%,.2f",
							(masterTotalVoidAmount < 0) ? masterTotalVoidAmount * -1: 0));
					masterData.put("voidRefundTxn", 0);
					masterData.put("voidRefundAmount", currName + " " + String.format("%,.2f",
							0.0));
					masterData.put("offlineTxn", 0);
					masterData.put("offlineAmount", currName + " " + String.format("%,.2f", 0.0));
					masterData.put("preAuthTxn", 0);
					masterData.put("preAuthAmount", currName + " " + String.format("%,.2f", 0.0));
					masterData.put("compCancelTxn", 0);
					masterData.put("compCancelAmount", currName + " " + String.format("%,.2f",
							0.0));

					// masterTotalVoidAmount is negative number, so use addition;
					int totalTxn = masterTotalSaleTxn + masterTotalVoidTxn;
					double totalAmount = masterTotalSaleAmount + masterTotalVoidAmount;

					masterData.put("totalTxn",totalTxn);
					masterData.put("totalAmount", (totalAmount >= 0) ? currName + " " + String.format(
							"%,.2f", totalAmount) : "- " + currName + " " + String.format(
							"%,.2f", totalAmount * -1));

					resultArray.put(masterData);
				}

				if (grandTotalSaleTxn != 0 || grandTotalVoidTxn != 0) {
					JSONObject grandData = new JSONObject();
					grandData.put("method", getString(R.string.grand_Total));
					grandData.put("saleTxn", grandTotalSaleTxn);
					grandData.put("saleAmount", currName + " " + String.format("%,.2f", grandTotalSaleAmount));
					grandData.put("refundTxn", 0);
					grandData.put("refundAmount", currName + " " + String.format("%,.2f", 0.0));
					grandData.put("voidTxn", grandTotalVoidTxn);
					grandData.put("voidAmount", "- " + currName + " " + String.format("%,.2f",
							(grandTotalVoidAmount < 0) ? grandTotalVoidAmount * -1: 0));
					grandData.put("voidRefundTxn", 0);
					grandData.put("voidRefundAmount", currName + " " + String.format("%,.2f",
							0.0));
					grandData.put("offlineTxn", 0);
					grandData.put("offlineAmount", currName + " " + String.format("%,.2f", 0.0));
					grandData.put("preAuthTxn", 0);
					grandData.put("preAuthAmount", currName + " " + String.format("%,.2f", 0.0));
					grandData.put("compCancelTxn", 0);
					grandData.put("compCancelAmount", currName + " " + String.format("%,.2f",
							0.0));

					// masterTotalVoidAmount is negative number, so use addition;
					int totalTxn = grandTotalSaleTxn + grandTotalVoidTxn;
					double totalAmount = grandTotalSaleAmount + grandTotalVoidAmount;

					grandData.put("totalTxn",totalTxn);
					grandData.put("totalAmount", (totalAmount >= 0) ? currName + " " + String.format(
							"%,.2f", totalAmount) : "- " + currName + " " + String.format(
							"%,.2f", totalAmount * -1));

					resultArray.put(grandData);
				}

				resultObject.put("data", resultArray);

				System.out.println("resultObject " + resultObject);
				System.out.println("resultObject " + resultObject.length());
				//System.out.println("resultObject " + resultObject.optString(0));
			} catch (Exception e) {
				e.printStackTrace();
			}

			tvBatchNo.setText(GlobalFunction.getLastBatchNo(SettlementLast.this));
			tvSettlementTime.setText(settlementTime);
			tvHostName.setText(settlementAcquirer);

			tvGrandTotalSaleTxn.setText(String.valueOf(grandTotalSaleTxn));
			tvGrandTotalSaleAmount.setText(currName + " " + String.format("%,.2f",
					grandTotalSaleAmount));
			tvGrandTotalVoidTxn.setText(String.valueOf(grandTotalVoidTxn));
			tvGrandTotalVoidAmount.setText("- " + currName + " " + String.format("%,.2f",
					(grandTotalVoidAmount < 0) ? grandTotalVoidAmount * -1 : 0));

			// grandTotalVoidAmount is negative value, so use addition
			tvGrandTotalAmount.setText((grandTotalSaleAmount + grandTotalVoidAmount >= 0) ?
					currName + " " + String.format("%,.2f", grandTotalSaleAmount + grandTotalVoidAmount) :
					"- " + currName + " " + String.format("%,.2f", grandTotalSaleAmount + grandTotalVoidAmount));

			dynamicScrollView(resultArray);
		}
	}

	public void dynamicScrollView(JSONArray resultArray) {

		System.out.println("SettlementLast resultArray " + resultArray);

		RelativeLayout.LayoutParams layout_alignParentLeft = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// set width is to prevent the newest created element that too short then cause
		// others element could not display properly
		layout_alignParentLeft.width = 300;
		layout_alignParentLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

		RelativeLayout.LayoutParams layout_alignParentRight = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layout_alignParentRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

		/*RelativeLayout.LayoutParams layout_toLeftOf = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layout_alignParentRight.addRule(RelativeLayout.LEFT_OF, RelativeLayout.TRUE);*/


		ScrollView sv = (ScrollView) findViewById(R.id.scrollTxn);
		sv.removeAllViews();

		LinearLayout ll = new LinearLayout(this);
		ll.removeAllViewsInLayout();
		ll.setOrientation(LinearLayout.VERTICAL);

		if (resultArray != null && resultArray.length() > 0) {
			for (int i = 0; i<resultArray.length(); i++)
			{
				try {
					JSONObject json = new JSONObject(resultArray.optString(i));
					String method = json.optString("method", "");
					String saleTxn = json.optString("saleTxn", "");
					String saleAmount = json.optString("saleAmount", "");
					String voidTxn = json.optString("voidTxn", "");
					String voidAmount = json.optString("voidAmount","");
					String totalAmount = json.optString("totalAmount","");
					System.out.println("bbb" + method);

					if (!method.equalsIgnoreCase(getString(R.string.grand_Total))) {
						// Method Title
						TextView tvTitle = new TextView(this);
						tvTitle.setText(method);
						tvTitle.setPadding(0, 20, 0, 0);
						tvTitle.setTypeface(null, Typeface.BOLD);
						tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
						tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
						ll.addView(tvTitle);

						// Start 'Total Sale Txn'
						RelativeLayout saleTxnLayout = new RelativeLayout(this);

						TextView tvSaleTxn_1 = new TextView(this);
						tvSaleTxn_1.setLayoutParams(layout_alignParentLeft);
						tvSaleTxn_1.setText(getString(R.string.total_sale_txn));
						saleTxnLayout.addView(tvSaleTxn_1);

						TextView tvSaleTxn_2 = new TextView(this);
						tvSaleTxn_2.setLayoutParams(layout_alignParentRight);
						tvSaleTxn_2.setText(saleTxn);
						tvSaleTxn_2.setGravity(Gravity.RIGHT);
						saleTxnLayout.addView(tvSaleTxn_2);
						ll.addView(saleTxnLayout);
						// End 'Total Sale Txn'

						// Start 'Total Sale Amount'
						RelativeLayout saleAmountLayout = new RelativeLayout(this);

						TextView tvSaleAmount_1 = new TextView(this);
						tvSaleAmount_1.setLayoutParams(layout_alignParentLeft);
						tvSaleAmount_1.setText(getString(R.string.total_sale_amt));
						saleAmountLayout.addView(tvSaleAmount_1);

						TextView tvSaleAmount_2 = new TextView(this);
						tvSaleAmount_2.setLayoutParams(layout_alignParentRight);
						tvSaleAmount_2.setText(saleAmount);
						tvSaleAmount_2.setGravity(Gravity.RIGHT);
						saleAmountLayout.addView(tvSaleAmount_2);
						ll.addView(saleAmountLayout);
						// End 'Total Sale Amount'

						// Start 'Total Void Txn'
						RelativeLayout voidTxnLayout = new RelativeLayout(this);

						TextView tvVoidTxn_1 = new TextView(this);
						tvVoidTxn_1.setLayoutParams(layout_alignParentLeft);
						tvVoidTxn_1.setText(getString(R.string.total_void_txn));
						voidTxnLayout.addView(tvVoidTxn_1);

						TextView tvVoidTxn_2 = new TextView(this);
						tvVoidTxn_2.setLayoutParams(layout_alignParentRight);
						tvVoidTxn_2.setText(voidTxn);
						tvVoidTxn_2.setGravity(Gravity.RIGHT);
						voidTxnLayout.addView(tvVoidTxn_2);
						ll.addView(voidTxnLayout);
						// End 'Total Void Txn'

						// Start 'Total Void Amount'
						RelativeLayout voidAmountLayout = new RelativeLayout(this);

						TextView tvVoidAmount_1 = new TextView(this);
						tvVoidAmount_1.setLayoutParams(layout_alignParentLeft);
						tvVoidAmount_1.setText(getString(R.string.total_void_amt));
						voidAmountLayout.addView(tvVoidAmount_1);

						TextView tvVoidAmount_2 = new TextView(this);
						tvVoidAmount_2.setLayoutParams(layout_alignParentRight);
						tvVoidAmount_2.setText(voidAmount);
						tvVoidAmount_2.setGravity(Gravity.RIGHT);
						voidAmountLayout.addView(tvVoidAmount_2);
						ll.addView(voidAmountLayout);
						// End 'Total Void Amount'

						// Start 'Total Amount'
						RelativeLayout totalAmountLayout = new RelativeLayout(this);

						TextView tvTotalAmount_1 = new TextView(this);
						tvTotalAmount_1.setLayoutParams(layout_alignParentLeft);
						tvTotalAmount_1.setText(getString(R.string.grand_total_2));
						tvTotalAmount_1.setTypeface(null, Typeface.BOLD);
						totalAmountLayout.addView(tvTotalAmount_1);

						TextView tvTotalAmount_2 = new TextView(this);
						tvTotalAmount_2.setLayoutParams(layout_alignParentRight);
						tvTotalAmount_2.setText(totalAmount);
						tvTotalAmount_2.setGravity(Gravity.RIGHT);
						tvTotalAmount_2.setTypeface(null, Typeface.BOLD);
						totalAmountLayout.addView(tvTotalAmount_2);
						ll.addView(totalAmountLayout);
						// End 'Total Amount'
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		sv.addView(ll);
	}

	public void preparePrintContent() {

		// Show the progress bar

		progressBar.setVisibility(View.VISIBLE);
//		progressDialog = ProgressDialog.show(SettlementLast.this, "",
//				getString(R.string.please_wait_2),	true);
//		progressDialog.setCancelable(false);

		label_title = getString(R.string.print_last_settlement_report);
		label_master = getString(R.string.master_label);
		label_visa = getString(R.string.visa_label);

		label = new TreeMap<String, String>();
		label.put("Title", label_title);


		label.put("NoTxnFound", getString(R.string.noTxnFound));

		label.put("MASTER", label_master);
		label.put("VISA", label_visa);

		labelValue = new TreeMap<String, String>();

		labelValue.put("TotalTrxMASTER", Integer.toString((1)));
		labelValue.put("TotalAmntMASTER", String.format("%,.2f", 2.0));

		labelValue.put("TotalTrxVISA", Integer.toString((1)));
		labelValue.put("TotalAmntVISA", String.format("%,.2f", 2.0));

		printUtil = new PrintUtil(SettlementLast.this, resultObject, progressBar);
		printUtil.sendLastSettlement();
	}

	public void getApiAccount() {
		//GET API FROM DB
		DatabaseHelper db = new DatabaseHelper(SettlementLast.this);
		String orgMerchantId = merID;
		String encMerchantId = "";
		DesEncrypter encrypt;
		try {
			encrypt = new DesEncrypter(merName);
			encMerchantId = encrypt.encrypt(orgMerchantId);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		String whereargs[] = {encMerchantId};
		String dbuser = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_API_ID, Constants.DB_MERCHANT_ID, whereargs);
		String dbpassword = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_API_PW, Constants.DB_MERCHANT_ID, whereargs);
		String dbuserid = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);

		db.close();
		try {
			DesEncrypter encrypter = new DesEncrypter(merName);
			apiID = encrypter.decrypt(dbuser);
			apiPW = encrypter.decrypt(dbpassword);
			userID = encrypter.decrypt(dbuserid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("apiID: " + apiID);
		System.out.println("apiPW: " + apiPW);
		System.out.println("userID: " + userID);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {


		// Printout Intent value
		if (data != null) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				Set<String> keys = bundle.keySet();
				Iterator<String> it = keys.iterator();
				System.out.println("Intent value start");
				while (it.hasNext()) {
					String key = it.next();
					System.out.println("[" + key + "=" + bundle.get(key) + "]");
				}
				System.out.println("Intent value end");
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (GlobalFunction.onKeyDownEvent(SettlementLast.this, keyCode, event, getPackageManager())) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();

		if (GlobalFunction.isNetworkConnected(SettlementLast.this)) {
			loadxml.cancel(true);
		}

		System.out.println("onBackPressed");
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
}
