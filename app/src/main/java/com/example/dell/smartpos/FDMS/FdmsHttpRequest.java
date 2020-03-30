package com.example.dell.smartpos.FDMS;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.DatabaseHelper;
import com.example.dell.smartpos.DesEncrypter;
import com.example.dell.smartpos.GlobalFunction;
import com.example.dell.smartpos.PayGate;
import com.example.dell.smartpos.R;
import com.example.dell.smartpos.SettlementCurrent;
import com.example.dell.smartpos.TrustModifier;

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
import java.util.HashMap;
import java.util.List;

public class FdmsHttpRequest extends AsyncTask<String, Void, String> {

	private FdmsApiFunction createTxn = null;
	private FdmsApiFunction updateTxn = null;
	private FdmsApiFunction voidTxn = null;
	private FdmsApiFunction settleTxn = null;

	private Context context;
	ProgressDialog progressDialog;

	private static int retryCounter = 0;
	private String URL;
	private String payGate;
	private String result = "";

	private String user;
	private String password;
	private String userID;
	int responseCode = 0;

	public FdmsHttpRequest(FdmsApiFunction activity, Context context) {
		this.context = context;

		if (FdmsVariable.requestAction.equalsIgnoreCase("createTxn")) {
			this.createTxn = activity;
		} else if (FdmsVariable.requestAction.contains("updateTxn")) {
			this.updateTxn = activity;
		} else if (FdmsVariable.requestAction.equalsIgnoreCase("voidTxn")) {
			this.voidTxn = activity;
		} else if (FdmsVariable.requestAction.equalsIgnoreCase("settlementTxn")) {
			this.settleTxn = activity;
		}
	}

	@Override
	protected void onPreExecute(){

		if (createTxn != null) {
			//context.getApplicationContext().
			progressDialog = ProgressDialog.show(context, "",
					context.getString(R.string.create_txn),
					true);
			progressDialog.setCancelable(false);
		} else if (updateTxn != null || voidTxn != null) {
			progressDialog = ProgressDialog.show(context, "",
					context.getString(R.string.update_txn),	true);
			progressDialog.setCancelable(false);
		} else if (settleTxn != null) {
			progressDialog = ProgressDialog.show(context, "",
					context.getString(R.string.please_wait_2),	true);
			progressDialog.setCancelable(false);
		}
	}

	@Override
	protected String doInBackground(String... arg0) {

		retryCounter = 0;
//		handler1 = new Handler();

		do {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			payGate = GlobalFunction.getPrefPayGate(context);

			// Prepare POST data
			if (FdmsVariable.getRequestAction().equalsIgnoreCase("createTxn")) {

				URL = PayGate.getURL_PayComp(payGate);

				NameValuePair merchantIdNVPair=new BasicNameValuePair("merchantId", FdmsVariable.getMerId());
				NameValuePair merchantNameNVPair=new BasicNameValuePair("merName", FdmsVariable.getMerName());
				NameValuePair currCodeNVPair=new BasicNameValuePair("currCode", FdmsVariable.getCurrCode());
				NameValuePair amountNVPair=new BasicNameValuePair("amount", FdmsVariable.getAmount());
				NameValuePair merRequestAmtNVPair = new BasicNameValuePair("merRequestAmt", FdmsVariable.getMerRequestAmt());
				NameValuePair surchargeNVPair = new BasicNameValuePair("surcharge", FdmsVariable.getSurcharge());
				NameValuePair payTypeNVPair=new BasicNameValuePair("payType", FdmsVariable.getPayType());
				NameValuePair merRefNVPair=new BasicNameValuePair("orderRef", FdmsVariable.getMerRef());
				NameValuePair pMethodNVPair=new BasicNameValuePair("pMethod", FdmsVariable.getpMethod());
				NameValuePair cardNoNVPair=new BasicNameValuePair("cardNo", FdmsVariable.getCardNo());
				NameValuePair cardHolderNVPair=new BasicNameValuePair("cardHolder", FdmsVariable.getCardHolder());
				NameValuePair epMonthNVPair=new BasicNameValuePair("epMonth", FdmsVariable.getEpMonth());
				NameValuePair epYearNVPair=new BasicNameValuePair("epYear", FdmsVariable.getEpYear());
				NameValuePair CVV2DataNVPair=new BasicNameValuePair("CVV2Data", FdmsVariable.getCVV2Data());
				NameValuePair operatorIdNVPair = new BasicNameValuePair("operatorId", FdmsVariable.getOperatorId());
				NameValuePair channelTypeNVPair = new BasicNameValuePair("channelType", FdmsVariable.getChannel());
				NameValuePair useSurchageTypeNVPair = new BasicNameValuePair("useSurcharge", FdmsVariable.getHideSurcharge());
				NameValuePair processingCodeNVPair=new BasicNameValuePair("processingCode", FdmsVariable.getProcessingCode());
				NameValuePair posEntryModeNVPair=new BasicNameValuePair("POSEntryMode", FdmsVariable.getPOSEntryMode());
				NameValuePair panSeqNoNVPair=new BasicNameValuePair("PANSeqNo", FdmsVariable.getPANSeqNo());
				NameValuePair posConditionCodeNVPair=new BasicNameValuePair("POSCondtionCode", FdmsVariable.getPOSCondtionCode());
				NameValuePair track2DataIdNVPair = new BasicNameValuePair("track2Data", FdmsVariable.getTrack2Data());
				NameValuePair encryptedPINNVPair = new BasicNameValuePair("enryptedPIN", FdmsVariable.getEnryptedPIN());
				NameValuePair EMVDataNVPair = new BasicNameValuePair("EMVData", FdmsVariable.getEMVICCRelatedData());
				NameValuePair invoiceRefNVPair = new BasicNameValuePair("invoiceRef", "");
				NameValuePair batchNoNVPair = new BasicNameValuePair("batchNo", "");

				nameValuePairs.add(merchantIdNVPair);
				nameValuePairs.add(merchantNameNVPair);
				nameValuePairs.add(currCodeNVPair);
				nameValuePairs.add(amountNVPair);
				nameValuePairs.add(merRequestAmtNVPair);
				nameValuePairs.add(surchargeNVPair);
				nameValuePairs.add(payTypeNVPair);
				nameValuePairs.add(merRefNVPair);
				nameValuePairs.add(pMethodNVPair);
				nameValuePairs.add(cardNoNVPair);
				nameValuePairs.add(cardHolderNVPair);
				nameValuePairs.add(epMonthNVPair);
				nameValuePairs.add(epYearNVPair);
				nameValuePairs.add(CVV2DataNVPair);
				nameValuePairs.add(operatorIdNVPair);
				nameValuePairs.add(channelTypeNVPair);
				nameValuePairs.add(useSurchageTypeNVPair);
				nameValuePairs.add(processingCodeNVPair);
				nameValuePairs.add(posEntryModeNVPair);
				nameValuePairs.add(panSeqNoNVPair);
				nameValuePairs.add(posConditionCodeNVPair);
				nameValuePairs.add(track2DataIdNVPair);
				nameValuePairs.add(encryptedPINNVPair);
				nameValuePairs.add(EMVDataNVPair);
				nameValuePairs.add(invoiceRefNVPair);
				nameValuePairs.add(batchNoNVPair);

			} else if (FdmsVariable.getRequestAction().equalsIgnoreCase("updateFailedTxn")) {
				URL = PayGate.getURL_payFDMSReturnMPOS(payGate);

				NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", FdmsVariable.getMerId());
				NameValuePair orderIdNVPair = new BasicNameValuePair("orderId", FdmsVariable.getPayRef());
				NameValuePair actionNVPair = new BasicNameValuePair("action", FdmsVariable.getAction());

				nameValuePairs.add(merchantIdNVPair);
				nameValuePairs.add(orderIdNVPair);
				nameValuePairs.add(actionNVPair);

			} else if (FdmsVariable.getRequestAction().equalsIgnoreCase("updateTxnAccepted")) {
				URL = PayGate.getURL_payFDMSReturnMPOS(payGate);

				NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", FdmsVariable.getMerId());
				NameValuePair orderIdNVPair = new BasicNameValuePair("orderId", FdmsVariable.getPayRef());
				NameValuePair actionNVPair = new BasicNameValuePair("action", FdmsVariable.getAction());
				NameValuePair invoiceNoNVPair = new BasicNameValuePair("invoiceNo",
						FdmsVariable.getInvoiceNo());
				NameValuePair cardNoNVPair = new BasicNameValuePair("cardNo",
						FdmsVariable.getCardNo());
				NameValuePair rrnNVPair = new BasicNameValuePair("RRN", FdmsVariable.getRRN());
				NameValuePair batchNoNVPair = new BasicNameValuePair("batchNo",
						FdmsVariable.getBatchNo());
				NameValuePair traceNoNVPair = new BasicNameValuePair("traceNo",
						FdmsVariable.getTraceNo());
				NameValuePair payMethodNVPair = new BasicNameValuePair("payMethod",
						FdmsVariable.getPayMethod());
				NameValuePair appCodeNVPair = new BasicNameValuePair("appCode",
						FdmsVariable.getAppCode());
				NameValuePair tcNVPair = new BasicNameValuePair("TC", FdmsVariable.getTc());
				NameValuePair tsiNVPair = new BasicNameValuePair("TSI", FdmsVariable.getTsi());
				NameValuePair atcNVPair = new BasicNameValuePair("ATC", FdmsVariable.getAtc());
				NameValuePair tvrNVPair = new BasicNameValuePair("TVR", FdmsVariable.getTvr());
				NameValuePair appNameNVPair = new BasicNameValuePair("appName",
						FdmsVariable.getAppName());
				NameValuePair aidNVPair = new BasicNameValuePair("AID", FdmsVariable.getAid());

				nameValuePairs.add(merchantIdNVPair);
				nameValuePairs.add(orderIdNVPair);
				nameValuePairs.add(actionNVPair);
				nameValuePairs.add(invoiceNoNVPair);
				nameValuePairs.add(cardNoNVPair);
				nameValuePairs.add(rrnNVPair);
				nameValuePairs.add(batchNoNVPair);
				nameValuePairs.add(traceNoNVPair);
				nameValuePairs.add(payMethodNVPair);
				nameValuePairs.add(appCodeNVPair);
				nameValuePairs.add(tcNVPair);
				nameValuePairs.add(tsiNVPair);
				nameValuePairs.add(atcNVPair);
				nameValuePairs.add(tvrNVPair);
				nameValuePairs.add(appNameNVPair);
				nameValuePairs.add(aidNVPair);

			} else if (FdmsVariable.getRequestAction().equalsIgnoreCase("updateTxnRejected")
					|| FdmsVariable.getRequestAction().equalsIgnoreCase("updateTxnCancelled")) {

				URL = PayGate.getURL_payFDMSReturnMPOS(payGate);

				NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", FdmsVariable.getMerId());
				NameValuePair orderIdNVPair = new BasicNameValuePair("orderId", FdmsVariable.getPayRef());
				NameValuePair actionNVPair = new BasicNameValuePair("action", FdmsVariable.getAction());
				NameValuePair payMethodNVPair = new BasicNameValuePair("payMethod", FdmsVariable.getPayMethod());

				nameValuePairs.add(merchantIdNVPair);
				nameValuePairs.add(orderIdNVPair);
				nameValuePairs.add(actionNVPair);
				nameValuePairs.add(payMethodNVPair);

			} else if (FdmsVariable.getRequestAction().equalsIgnoreCase("voidTxn")) {
				URL = PayGate.getURL_orderAPI(payGate);

				getApiAccount();

				NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", FdmsVariable.getMerId());
				NameValuePair loginIdNVPair = new BasicNameValuePair("loginId", user);
				NameValuePair passwordNVPair = new BasicNameValuePair("password", password);
				NameValuePair orderIdNVPair = new BasicNameValuePair("payRef",
						FdmsVariable.getPayRef());
				NameValuePair actionNVPair = new BasicNameValuePair("actionType", FdmsVariable.getAction());
				NameValuePair invoiceNoNVPair = new BasicNameValuePair("invoiceNo",
						FdmsVariable.getInvoiceNo());
				NameValuePair traceNoNVPair = new BasicNameValuePair("traceNo",
						FdmsVariable.getTraceNo());

				nameValuePairs.add(merchantIdNVPair);
				nameValuePairs.add(loginIdNVPair);
				nameValuePairs.add(passwordNVPair);
				nameValuePairs.add(orderIdNVPair);
				nameValuePairs.add(actionNVPair);
				nameValuePairs.add(invoiceNoNVPair);
				nameValuePairs.add(traceNoNVPair);
			} else if (FdmsVariable.getRequestAction().equalsIgnoreCase("settlementTxn")) {

				URL = PayGate.getURL_payFDMSReturnMPOS(payGate);

				NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", FdmsVariable.getMerId());
				NameValuePair payRefArrayNVPair = new BasicNameValuePair("payRefArray", FdmsVariable.getPayRefArray());
				NameValuePair actionNVPair = new BasicNameValuePair("action", FdmsVariable.getAction());

				nameValuePairs.add(merchantIdNVPair);
				nameValuePairs.add(payRefArrayNVPair);
				nameValuePairs.add(actionNVPair);
			}

			try {
				System.out.println("Request Action : " + FdmsVariable.getRequestAction());
				System.out.println("Request URL    : " + URL);
				System.out.println("Request Params : " + "TransactionQuery:" + PayGate.getQuery(nameValuePairs));

				URL url = new URL (URL);
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				TrustModifier.relaxHostChecking(con);
				con.setReadTimeout(15000);
				con.setConnectTimeout(15000);
				con.setRequestMethod("POST");
				con.setDoInput(true);
				con.setDoOutput(true);

				OutputStream os = con.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				writer.write(PayGate.getQuery(nameValuePairs));
				writer.flush();
				writer.close();
				os.close();

				responseCode = con.getResponseCode();

				InputStream in = con.getInputStream();
				BufferedReader reader = null;

				try{
					reader= new BufferedReader(new InputStreamReader(in));
					String line = "";
					while((line=reader.readLine())!=null){
						result = result + line;
					}

				} catch (Exception e) {
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

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (responseCode == 200) {
				retryCounter = 0;
				return result;
			} else {
				retryCounter++;
			}
			// createTxn no need to retry request while failed
			// when restarting UAT, the request will keep retry
			// and will create many txn, to prevent this do not
			// allow retry for create txn
		} while (responseCode != 200 && retryCounter != 5 && !FdmsVariable.getRequestAction().equalsIgnoreCase("createTxn"));

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		System.out.println("onPostExecute result " + result);
		if (result != null) {
			// Close the progressDialog
			if ((progressDialog != null) && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			HashMap<String, String> map = null;
			map = PayGate.split(result);

			if (createTxn != null) {
				System.out.println("FDMS Txn create result: " + result);
				//System.out.println("PayRef " + map.get("PayRef"));

				FdmsVariable.setCreateTxnCode(map.get("successcode"));
				FdmsVariable.setMerRef(map.get("Ref"));
				FdmsVariable.setPayRef(map.get("PayRef"));
				FdmsVariable.setAmount(map.get("Amt"));
				// PayDollar error msg
				FdmsVariable.setErrMsg(map.get("errMsg"));

				if (FdmsVariable.getCreateTxnCode() != null &&
						FdmsVariable.getCreateTxnCode().equalsIgnoreCase("0")) {
					GlobalFunction.incMerRef(context);
					createTxn.saleRequest();
				} else {

					if (FdmsVariable.getErrMsg() != null) {
						GlobalFunction.showAlertDialog(context,
								context.getString(R.string.failed),
								FdmsVariable.getErrMsg());

						FdmsVariable.setRequestAction("updateFailedTxn");
						FdmsVariable.setAction("Update_Failed_Txn");

						// Update the created txn to "Rejected"
						FdmsHttpRequest request = new FdmsHttpRequest(createTxn, context);
						request.execute();

					} else {
						GlobalFunction.showAlertDialog(context,
								context.getString(R.string.failed),
								context.getString(R.string.create_transaction_failed));
					}
				}
			} else if (updateTxn != null) {
				System.out.println("FDMS Txn update result: " + result);

				FdmsVariable.setUpdateTxnCode(map.get("resultCode"));
				FdmsVariable.setStatus(map.get("status"));
				FdmsVariable.setErrCode(map.get("errCode"));
				FdmsVariable.setReturnMsg(map.get("returnMsg"));
				FdmsVariable.setPayRef(map.get("orderId"));
				FdmsVariable.setTxnTime(map.get("trxTime"));
				FdmsVariable.setBankRef(map.get("bankRef"));
				FdmsVariable.setAmount(map.get("amount"));

				if (FdmsVariable.getUpdateTxnCode() != null &&
						FdmsVariable.getUpdateTxnCode().equalsIgnoreCase("0") &&
						FdmsVariable.getRequestAction().equalsIgnoreCase("updateTxnAccepted")) {
					// For successful Accepted Txn
					updateTxn.successIntent();
				} else if (FdmsVariable.getUpdateTxnCode() != null &&
						FdmsVariable.getUpdateTxnCode().equalsIgnoreCase("0") &&
						(FdmsVariable.getRequestAction().equalsIgnoreCase("updateTxnRejected") ||
								FdmsVariable.getRequestAction()
										.equalsIgnoreCase("updateTxnCancelled"))) {
					// For successful Rejected and Cancelled Txn
					updateTxn.failedIntent();
				} else {
					GlobalFunction.showAlertDialog(context,
							context.getString(R.string.failed),
							context.getString(R.string.err_during_update));
				}

			} else if (voidTxn != null) {

				System.out.println("FDMS Txn void result: " + result);

				FdmsVariable.setVoidTxnCode(map.get("resultCode"));
				FdmsVariable.setPayRef(map.get("payRef"));
				FdmsVariable.setStatus(map.get("orderStatus"));
				FdmsVariable.setReturnMsg(map.get("errMsg"));
				FdmsVariable.setAmount(map.get("amt"));

				if (FdmsVariable.getVoidTxnCode() != null && FdmsVariable.getVoidTxnCode().equals("0")) {

					TextView edtStatus = ((Activity) context).findViewById(R.id.edtStatus);
					TextView edtTraceNo = ((Activity) context).findViewById(R.id.edtTraceNo);
					TextView edtInvoiceNo = ((Activity) context).findViewById(R.id.edtInvoiceNo);
					Button voidBtn = ((Activity) context).findViewById(R.id.void_capture);

					edtStatus.setText(context.getString(R.string.voided));
					edtTraceNo.setText(FdmsVariable.getTraceNo());
					edtInvoiceNo.setText(FdmsVariable.getInvoiceNo());
					voidBtn.setEnabled(false);

					//intent.putExtra(Constants.TRACE_NO, FdmsVariable.getTraceNo());
					//					intent.putExtra(Constants.INVOICE_NO, FdmsVariable.getInvoiceNo());

//					((Activity) context).finish();
//					Intent intent = new Intent();
//					intent.putExtra(Constants.USERID, voidTxn.userID);
//					intent.putExtra(Constants.MERNAME, FdmsVariable.getMerName());
//					intent.putExtra(Constants.SURCHARGE, FdmsVariable.getSurcharge());
//					intent.putExtra(Constants.CURR, FdmsVariable.getCurrName());
//					intent.putExtra(Constants.PAYMETHOD, FdmsVariable.getPayMethod());
//					intent.putExtra(Constants.AMOUNT, String.format("%.2f", parseDouble(FdmsVariable.getAmount())));
//					intent.putExtra(Constants.MERREQUESTAMT, FdmsVariable.getMerRequestAmt());
//					intent.putExtra(Constants.PAYBANKID, FdmsVariable.getPayBankId());
//					intent.putExtra(Constants.PAYREF, FdmsVariable.getPayRef());
//					intent.putExtra(Constants.MERCHANT_REF, FdmsVariable.getMerRef());
//					intent.putExtra(Constants.CARDNO, FdmsVariable.getCardNo());
//					intent.putExtra(Constants.DATE, FdmsVariable.getTxnTime());
//					intent.putExtra(Constants.CARDHOLDER, FdmsVariable.getCardHolder());
//					intent.putExtra(Constants.STATUS, context.getString(R.string.voided));
//					intent.putExtra(Constants.SETTLE, FdmsVariable.getSettle());
//					intent.putExtra(Constants.REMARK, FdmsVariable.getRemark());
//					intent.putExtra(Constants.MERID, FdmsVariable.getMerId());
//					intent.putExtra(Constants.BATCH_NO, FdmsVariable.getBatchNo());
//					intent.putExtra(Constants.TRACE_NO, FdmsVariable.getTraceNo());
//					intent.putExtra(Constants.INVOICE_NO, FdmsVariable.getInvoiceNo());
//					intent.setClass(context, HistoryDetails.class);
//
//					((Activity) context).setResult(100, intent);

				}
			} else if (settleTxn != null) {
				System.out.println("FDMS Txn settlement result: " + result);

				FdmsVariable.setSettleTxnCode(map.get("resultCode"));
				FdmsVariable.setStatus(map.get("status"));
				FdmsVariable.setErrCode(map.get("errCode"));
				FdmsVariable.setReturnMsg(map.get("returnMsg"));
				FdmsVariable.setPayRef(map.get("orderId"));
				FdmsVariable.setTxnTime(map.get("TxnTime"));
				FdmsVariable.setBankRef(map.get("bankRef"));
				FdmsVariable.setAmount(map.get("amount"));

				if (FdmsVariable.getSettleTxnCode() != null && FdmsVariable.getSettleTxnCode().equals("0")) {
					GlobalFunction.setLockPayment(context, false);
					GlobalFunction.incBatchNo(context);

					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle(context.getString(R.string.success))
							.setMessage(context.getString(R.string.settlement_successful));

					// Positive button
					builder.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									((Activity) context).finish();
									Intent intent = new Intent();
									intent.putExtra(Constants.MERID, FdmsVariable.getMerId());
									intent.putExtra(Constants.MERNAME, FdmsVariable.getMerName());
									intent.setClass(context, SettlementCurrent.class);
									context.startActivity(intent);
								}
							});
					builder.show();

				} else {

					GlobalFunction.setLockPayment(context, true);
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle(context.getString(R.string.fail))
							.setMessage(FdmsVariable.getReturnMsg());

					// Positive button
					builder.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									((Activity) context).finish();
									Intent intent = new Intent();
									intent.putExtra(Constants.MERID, FdmsVariable.getMerId());
									intent.putExtra(Constants.MERNAME, FdmsVariable.getMerName());
									intent.setClass(context, SettlementCurrent.class);
									context.startActivity(intent);
								}
							});
					builder.show();
				}
			}
		} else {
			// Close the progressDialog
			if ((progressDialog != null) && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (createTxn != null) {
				GlobalFunction.showAlertDialog(context,
						context.getString(R.string.failed),
						context.getString(R.string.create_transaction_failed));
			}
		}
	}

	public void getApiAccount() {
		//GET API FROM DB
		DatabaseHelper db = new DatabaseHelper(context);
		String orgMerchantId = FdmsVariable.getMerId();
		String encMerchantId = "";
		DesEncrypter encrypt;
		try {
			encrypt = new DesEncrypter(FdmsVariable.getMerName());
			encMerchantId = encrypt.encrypt(orgMerchantId);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		String whereargs[] = {encMerchantId};
		String dbuser = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_API_ID, Constants.DB_MERCHANT_ID, whereargs);
		String dbpassword = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_API_PW, Constants.DB_MERCHANT_ID, whereargs);
		String dbuserid = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
		db.close();
		if (dbuser != null) {
			try {
				DesEncrypter encrypter = new DesEncrypter(FdmsVariable.getMerName());
				user = encrypter.decrypt(dbuser);
				password = encrypter.decrypt(dbpassword);
				userID = encrypter.decrypt(dbuserid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

		}
	}
}