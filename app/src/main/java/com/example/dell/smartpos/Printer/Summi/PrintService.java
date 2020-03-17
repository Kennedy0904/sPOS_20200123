package com.example.dell.smartpos.Printer.Summi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.ecity.android.tinypinyin.Pinyin;
import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.GlobalFunction;
import com.example.dell.smartpos.Printer.utils.ESCUtil;
import com.example.dell.smartpos.R;
import com.example.dell.smartpos.Record;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class PrintService {

	private Context context = null;
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private BluetoothDevice device = null;
	private static BluetoothSocket bluetoothSocket = null;
	private static OutputStream outputStream = null;
	private OutputStreamWriter mWriter = null;
	private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private boolean isConnection = false;
	private final static int WIDTH_PIXEL = 384;
	public final static int IMAGE_SIZE = 360;
	public String lang;

	public PrintService(Context context, String deviceAddress) {
		super();
		this.context = context;
		Log.d("OTTO", "Context:3::" + this.context + ",," + context);
		if (!"".equals(deviceAddress)) {
			this.device = this.bluetoothAdapter.getRemoteDevice(deviceAddress);
		}
	}

	/**
	 * 连接蓝牙设备
	 */
	public boolean connect() {
		if (!this.isConnection) {
			try {
				bluetoothSocket = this.device.createRfcommSocketToServiceRecord(uuid);
				bluetoothSocket.connect();
				outputStream = bluetoothSocket.getOutputStream();
				mWriter = new OutputStreamWriter(outputStream, "GBK");
				this.isConnection = true;
				if (this.bluetoothAdapter.isDiscovering()) {
					Log.d("OTTO", "关闭适配器！");
					this.bluetoothAdapter.isDiscovering();
				}
			} catch (Exception e) {
				Log.d("OTTO", "connect() 连接失败！----" + e);
				Toast.makeText(this.context, context.getString(R.string.bluetooth_connect_fail), Toast.LENGTH_SHORT).show();
				this.isConnection = false;
				return false;
			}
			Toast.makeText(this.context, this.device.getName() + context.getString(R.string.bluetooth_connect_success), Toast.LENGTH_SHORT).show();
			return true;
		} else {
			return true;
		}
	}

	/**
	 * 断开蓝牙设备连接
	 */
	public static void disconnect() {
		Log.d("OTTO", "断开蓝牙设备连接");
		try {
			if (bluetoothSocket != null) {
				bluetoothSocket.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 发送数据
	 */
	// Reprint Receipt printing
	public void send(Context context, Map<String, String> info, Map<String, String> product, Bitmap bitmap) {
		if (this.isConnection) {
			Log.d("OTTO", "开始打印:" + "\n" + info + "\n" + product);
			try {
				// Get device language
				lang = GlobalFunction.getLanguage(context);

				Log.d("OTTO", "pMethod:" + product.get("PayMethod").toString() + ",pType:" + product.get("PayStatus").toString() + ",currencyname:" + product.get("CurrCode").toString());

				printLine();
				printAlignment(1);
				printBitmap(bitmap);

				printLine();

				// Merchant Name
				printLargeText(info.get("MerchantName"));
				printLine();

				if (!GlobalFunction.getAddress1(context).equals("")) {
					printText(GlobalFunction.getAddress1(context));
					printLine();
				}

				if (!GlobalFunction.getAddress2(context).equals("")) {
					printText(GlobalFunction.getAddress2(context));
					printLine();
				}

				if (!GlobalFunction.getAddress3(context).equals("")) {
					printText(GlobalFunction.getAddress3(context));
					printLine();
				}

				printLine();

				// Payment Type
				printTwoColumn(info.get("PaymentType"), product.get("PayType"));
				printLine();

				// Payment Method
				printTwoColumn(info.get("PayMethod"), product.get("PayMethod"));
				printLine();

				// Payment Status
				printTwoColumn(info.get("PayStatus"), product.get("PayStatus"));
				printLine();

				// Operator
				printTwoColumn(info.get("Operator"), product.get("OperatorNumber"));
				printLine(2);

				// Trx Date
				printTwoColumn(info.get("TransactionDate"), product.get("TransactionDate"));
				printLine();

				// Trx No
				printTwoColumn(info.get("CardNo"), product.get("CardNo"));
				printLine();

				// Merchant Ref
				printTwoColumn(info.get("MerchantRef"), product.get("MerchantRef"));
				printLine();

				// Payment Ref
				printTwoColumn(info.get("PaymentRef"), product.get("PaymentRef"));
				printLine();

				// Amount
				printTwoColumn(info.get("TotalAmount"), product.get("CurrCode") + " " + product.get("Amount"));

				SharedPreferences prefsettings = context.getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
				String SurCalstat = (prefsettings.getString(Constants.pref_surcharge_calculation, Constants.default_surcharge_calculation));
				SharedPreferences pref_sur = context.getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
				String mdr = pref_sur.getString(Constants.mer_mdr, "");
				if ((product.get("Surcharge") != null) || (!"".equals(product.get("Surcharge")))) {
					if ("OFF".equals(SurCalstat) || mdr == null || "".equals(mdr)) {
						Log.d("OTTO", "PrintService OFF null empty");
					} else if ("OFF".equals(SurCalstat) || Double.valueOf(mdr) <= 0) {
						Log.d("OTTO", "PrintService OFF or <0");
					} else if ("ON".equals(SurCalstat) && Double.valueOf(mdr) > 0) {
						if ("".equals(product.get("Surcharge")) || product.get("Surcharge") == null) {
							Log.d("OTTO", "PrintService surcharge null empty");
						} else if (Double.valueOf(product.get("Surcharge")) <= 0) {
							Log.d("OTTO", "PrintService surcharge <0");
						} else if ((Double.valueOf(product.get("Surcharge")) > 0)) {
							Log.d("OTTO", "PrintService surcharge >0");
							printLine();
							printTwoColumn(info.get("Surcharge"), product.get("CurrCode") + " " + product.get("Surcharge"));
							printLine();
							printTwoColumn(info.get("MerRequestAmt"), product.get("CurrCode") + " " + product.get("MerRequestAmt"));
						} else {
							Log.d("OTTO", "PrintService surcharge else");
						}
					} else {
						Log.d("OTTO", "PrintService else");
					}
				}

				printLine(2);

				printText(makelinefeed(info.get("note"), info.get("isCN")));
				printLine();
				printDashLine();
				printLine();

				printAlignment(1);
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				printText(date.format(new Date()));
				printLine(2);

				printAlignment(1);
				if (info.get("copy").equalsIgnoreCase(context.getString(R.string.print_customerCopy))) {
					printText("=== " + context.getString(R.string.print_customerCopy) + " ===");
				} else if (info.get("copy").equalsIgnoreCase(context.getString(R.string.print_merchantCopy))) {
					printText("=== " + context.getString(R.string.print_merchantCopy) + " ===");
				}
				printLine();
				printAlignment(1);
				printText("***" + info.get("Title") + "***");

				printLine(4);

			} catch (Exception e) {
				Log.d("OTTO", "开始打印:" + e);
				e.printStackTrace();
				Toast.makeText(this.context, R.string.bluetooth_send_fail, Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this.context, R.string.bluetooth_connect_fail, Toast.LENGTH_SHORT).show();
		}
	}

	public void sendAlipayTHreceipt(Context context, Map<String, String> info, Map<String, String> product, Bitmap bitmap) {
		if (this.isConnection) {
			Log.d("OTTO", "开始打印:" + "\n" + info + "\n" + product);
			try {

				printLine();
				printAlignment(1);
				printBitmap(bitmap);

				printLargerText(info.get("MerchantName"));

				printLine();
				printDashLine();
				printLine();

				printAlignment(0);
				printText(info.get("TID") + " " + product.get("TID"));
				printLine();

				printText(info.get("merId") + " " + product.get("merId"));
				printLine();

				printTwoColumn(info.get("batchNo") + " " + product.get("batchNo"), info.get("host") + " " + product.get("host"));
				printLine();

				printTwoColumn(info.get("traceNo") + " " + product.get("traceNo"), info.get("stan") + " " + product.get("stan"));
				printLine();

				printDoubleDashLine();
				printLine();

				printText(product.get("walletCard"));
				printLine();

				printText(makelinefeed(info.get("walletCard"), info.get("isCN")));
				printLine();

				printTwoColumn(product.get("txnDate"), product.get("txnTime"));
				printLine();

				printTwoColumn(info.get("apprCode"), product.get("apprCode"));
				printLine();

				printTwoColumn(info.get("refNo"), product.get("refNo"));
				printLine();

				printLargerText(info.get("walletSale"));
				printLine();

				printTwoColumn(info.get("total"), product.get("currCode") + " " + product.get("amount"));
				printLine();

				printLine();

				printText(product.get("payMethod"));
				printLine();

				printLine();
				printAlignment(1);
				printText(makelinefeed(info.get("note"), info.get("isCN")));
				printLine();

				printAlignment(1);
				printText("***NO REFUND***");
				printLine();

				printAlignment(1);
				if (info.get("copy").equalsIgnoreCase(context.getString(R.string.print_customerCopy))) {
					printText("--- " + context.getString(R.string.print_customerCopy) + " ---");
				} else if (info.get("copy").equalsIgnoreCase(context.getString(R.string.print_merchantCopy))) {
					printText("--- " + context.getString(R.string.print_merchantCopy) + " ---");
				}
				printLine(4);

			} catch (Exception e) {
				Log.d("OTTO", "开始打印:" + e);
				e.printStackTrace();
				Toast.makeText(this.context, R.string.bluetooth_send_fail, Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this.context, R.string.bluetooth_connect_fail, Toast.LENGTH_SHORT).show();
		}
	}


	//===KM LIEW summry report printing feature 30/5/2018===
	public void printSummaryReport(Context context, Map<String, String> label, Map<String, String> value) {
		if (this.isConnection) {
			Log.d("KM LIEW", "Start Printing:" + "\n" + label + "\n" + value);
			try {
				SharedPreferences prefsettings = context.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
				String lang = prefsettings.getString(Constants.pref_Lang, Constants.default_language);

				printLine();

				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				printAlignment(1);
				if (!lang.equalsIgnoreCase(Constants.LANG_THAI)) {
					printLargeText(label.get("Title"));
				} else {
					System.out.println("Title thai: " + label.get("Title"));
					printLargeText(label.get("Title"));
				}
				printLine();

				printText(value.get("MerName"));
				printLine();

				printTwoColumn(label.get("FromDateLabel"), value.get("From_date"));
				printLine();
				printTwoColumn(label.get("ToDateLabel"), value.get("To_date"));

				printLine();
				printText(date.format(new Date()));

				printLine();
				printDashLine();
				printAlignment(1);

				printText(label.get("Header"));
				printLine();
				printTwoColumn(label.get("GrandTotalTrxLabel"), value.get("currCode") + " " + value.get("GrandTotalAmnt"));
				printLine();
				printTwoColumn(label.get("GrandTotalRefundLabel"), value.get("currCode") + " " + value.get("GrandTotalRefundAmnt"));
				printLine();
				printTwoColumn(label.get("GrandTotalRequestRefundLabel"), value.get("currCode") + " " + value.get("GrandTotalRequestRefundAmnt"));
				printLine();
				printTwoColumn(label.get("GrandTotalVoidLabel"), value.get("currCode") + " " + value.get("GrandTotalVoidAmnt"));

				printLine();
				printDashLine();
				printAlignment(1);

				if (value.get("GrandTotalAmnt").equals("0.00") && value.get("GrandTotalRefundAmnt").equals("0.00") && value.get("GrandTotalRequestRefundAmnt").equals("0.00") && value.get("GrandTotalVoidAmnt").equals("0.00")) {
					printText(label.get("NoTxnFound"));
					printLine();
					printDashLine();
					printAlignment(1);
				}

				// ALIPAYCNOFFL
				if (!value.get("TotalTrxALIPAYCNOFFL").equals("0") || !value.get("TotalRefundTrxALIPAYCNOFFL").equals("0") || !value.get("TotalRequestRefundTrxALIPAYCNOFFL").equals("0") || !value.get("TotalVoidTrxALIPAYCNOFFL").equals("0")) {
					printText(label.get("ALIPAYCNOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYCNOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYCNOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxALIPAYCNOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntALIPAYCNOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxALIPAYCNOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntALIPAYCNOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxALIPAYCNOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntALIPAYCNOFFL"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// ALIPAYHKOFFL
				if (!value.get("TotalTrxALIPAYHKOFFL").equals("0") || !value.get("TotalRefundTrxALIPAYHKOFFL").equals("0") || !value.get("TotalRequestRefundTrxALIPAYHKOFFL").equals("0") || !value.get("TotalVoidTrxALIPAYHKOFFL").equals("0")) {
					printText(label.get("ALIPAYHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxALIPAYHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntALIPAYHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxALIPAYHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntALIPAYHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxALIPAYHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntALIPAYHKOFFL"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// ALIPAYOFFL
				if (!value.get("TotalTrxALIPAYOFFL").equals("0") || !value.get("TotalRefundTrxALIPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxALIPAYOFFL").equals("0") || !value.get("TotalVoidTrxALIPAYOFFL").equals("0")) {
					printText(label.get("ALIPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxALIPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntALIPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxALIPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntALIPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxALIPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntALIPAYOFFL"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// BOOSTOFFL
				if (!value.get("TotalTrxBOOSTOFFL").equals("0") || !value.get("TotalRefundTrxBOOSTOFFL").equals("0") || !value.get("TotalRequestRefundTrxBOOSTOFFL").equals("0") || !value.get("TotalVoidTrxBOOSTOFFL").equals("0")) {
					printText(label.get("BOOSTOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxBOOSTOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntBOOSTOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxBOOSTOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntBOOSTOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxBOOSTOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntBOOSTOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxBOOSTOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntBOOSTOFFL"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// GCASHOFFL
				if (!value.get("TotalTrxGCASHOFFL").equals("0") || !value.get("TotalRefundTrxGCASHOFFL").equals("0") || !value.get("TotalRequestRefundTrxGCASHOFFL").equals("0") || !value.get("TotalVoidTrxGCASHOFFL").equals("0")) {
					printText(label.get("GCASHOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxGCASHOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGCASHOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxGCASHOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntGCASHOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxGCASHOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntGCASHOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxGCASHOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntGCASHOFFL"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// GRABPAYOFFL
				if (!value.get("TotalTrxGRABPAYOFFL").equals("0") || !value.get("TotalRefundTrxGRABPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxGRABPAYOFFL").equals("0") || !value.get("TotalVoidTrxGRABPAYOFFL").equals("0")) {
					printText(label.get("GRABPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxGRABPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGRABPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxGRABPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntGRABPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxGRABPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntGRABPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxGRABPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntGRABPAYOFFL"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// MASTER
				if (!value.get("TotalTrxMASTER").equals("0")) {
					printText(label.get("MASTER"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxMASTER"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntMASTER"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxMASTER"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntMASTER"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxMASTER"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntMASTER"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxMASTER"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntMASTER"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// OEPAYOFFL
				if (!value.get("TotalTrxOEPAYOFFL").equals("0") || !value.get("TotalRefundTrxOEPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxOEPAYOFFL").equals("0") || !value.get("TotalVoidTrxOEPAYOFFL").equals("0")) {
					printText(label.get("OEPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxOEPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntOEPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxOEPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntOEPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxOEPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntOEPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxOEPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntOEPAYOFFL"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// PROMPTPAYOFFL
				if (!value.get("TotalTrxPROMPTPAYOFFL").equals("0") || !value.get("TotalRefundTrxPROMPTPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxPROMPTPAYOFFL").equals("0") || !value.get("TotalVoidTrxPROMPTPAYOFFL").equals("0")) {
					printText(label.get("PROMPTPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxPROMPTPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntPROMPTPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxPROMPTPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntPROMPTPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxPROMPTPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntPROMPTPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxPROMPTPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntPROMPTPAYOFFL"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// UNIONPAYOFFL
				if (!value.get("TotalTrxUNIONPAYOFFL").equals("0") || !value.get("TotalRefundTrxUNIONPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxUNIONPAYOFFL").equals("0") || !value.get("TotalVoidTrxUNIONPAYOFFL").equals("0")) {
					printText(label.get("UNIONPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxUNIONPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntUNIONPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxUNIONPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntUNIONPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxUNIONPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntUNIONPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxUNIONPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntUNIONPAYOFFL"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// VISA
				if (!value.get("TotalTrxVISA").equals("0") || !value.get("TotalRefundTrxVISA").equals("0") || !value.get("TotalRequestRefundTrxVISA").equals("0") || !value.get("TotalVoidTrxVISA").equals("0")) {
					printText(label.get("VISA"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxVISA"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntVISA"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxVISA"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntVISA"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxVISA"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntVISA"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxVISA"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntVISA"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// WECHATOFFL
				if (!value.get("TotalTrxWECHATOFFL").equals("0") || !value.get("TotalRefundTrxWECHATOFFL").equals("0") || !value.get("TotalRequestRefundTrxWECHATOFFL").equals("0") || !value.get("TotalVoidTrxWECHATOFFL").equals("0")) {
					printText(label.get("WECHATOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxWECHATOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntWECHATOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxWECHATOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntWECHATOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxWECHATOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntWECHATOFFL"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// WECHATHKOFFL
				if (!value.get("TotalTrxWECHATHKOFFL").equals("0") || !value.get("TotalRefundTrxWECHATHKOFFL").equals("0") || !value.get("TotalRequestRefundTrxWECHATHKOFFL").equals("0") || !value.get("TotalVoidTrxWECHATHKOFFL").equals("0")) {
					printText(label.get("WECHATHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxWECHATHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntWECHATHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxWECHATHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntWECHATHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxWECHATHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntWECHATHKOFFL"));

					printLine();
					printDashLine();
					printAlignment(1);
				}

				// WECHATONL
				if (!value.get("TotalTrxWECHATONL").equals("0") || !value.get("TotalRefundTrxWECHATONL").equals("0") || !value.get("TotalRequestRefundTrxWECHATONL").equals("0") || !value.get("TotalVoidTrxWECHATONL").equals("0")) {
					printText(label.get("WECHATONL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATONL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATONL"));
					printLine();
					printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxWECHATONL"));
					printLine();
					printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntWECHATONL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxWECHATONL"));
					printLine();
					printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntWECHATONL"));
					printLine();
					printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxWECHATONL"));
					printLine();
					printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntWECHATONL"));


					printLine();
					printDashLine();
					printAlignment(1);
				}

				printLine();
				printAlignment(1);
				printText("===" + label.get("Footer") + "===");
				printLine(4);
			} catch (Exception e) {
				Log.d("KM LIEW", "Start Printing:" + e);
				Toast.makeText(this.context, R.string.bluetooth_send_fail, Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this.context, R.string.bluetooth_connect_fail, Toast.LENGTH_SHORT).show();
		}
	}

	public void printReportSUNMI(Context context, Map<String, String> label, Map<String, String> value,
								 ArrayList<Record> ALIPAYCNOFFLRecordsArray,
								 ArrayList<Record> ALIPAYHKOFFLRecordsArray,
								 ArrayList<Record> ALIPAYOFFLRecordsArray,
								 ArrayList<Record> BOOSTOFFLRecordsArray,
								 ArrayList<Record> GCASHOFFLRecordsArray,
								 ArrayList<Record> GRABPAYOFFLRecordsArray,
								 ArrayList<Record> masterRecordsArray,
								 ArrayList<Record> OEPAYOFFLRecordsArray,
								 ArrayList<Record> PROMPTPAYOFFLRecordsArray,
								 ArrayList<Record> UNIONPAYOFFLRecordsArray,
								 ArrayList<Record> visaRecordsArray,
								 ArrayList<Record> WECHATHKOFFLRecordsArray,
								 ArrayList<Record> WECHATOFFLRecordsArray,
								 ArrayList<Record> WECHATONLRecordsArray) {
		if (this.isConnection) {

			Log.d("KM LIEW", "Start Printing:" + "\n" + label + "\n" + value);
			try {
				SharedPreferences pref_sur = context.getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
				String lang = pref_sur.getString(Constants.pref_Lang, Constants.default_language);

				printLine();

				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				printAlignment(1);
				printLargeText(label.get("Title"));
				printLine();

				printText(value.get("MerName"));
				printLine();

				printTwoColumn(label.get("FromDateLabel"), value.get("From_date"));
				printLine();
				printTwoColumn(label.get("ToDateLabel"), value.get("To_date"));

				printLine();
				printText(date.format(new Date()));

				printLine();
				printDashLine();

				if (value.get("TotalTrxALIPAYCNOFFL").equals("0")
						&& value.get("TotalTrxALIPAYHKOFFL").equals("0")
						&& value.get("TotalTrxALIPAYOFFL").equals("0")
						&& value.get("TotalTrxBOOSTOFFL").equals("0")
						&& value.get("TotalTrxGCASHOFFL").equals("0")
						&& value.get("TotalTrxGRABPAYOFFL").equals("0")
						&& value.get("TotalTrxMASTER").equals("0")
						&& value.get("TotalTrxOEPAYOFFL").equals("0")
						&& value.get("TotalTrxPROMPTPAYOFFL").equals("0")
						&& value.get("TotalTrxUNIONPAYOFFL").equals("0")
						&& value.get("TotalTrxVISA").equals("0")
						&& value.get("TotalTrxWECHATOFFL").equals("0")
						&& value.get("TotalTrxWECHATHKOFFL").equals("0")
						&& value.get("TotalTrxWECHATONL").equals("0")) {
					printAlignment(1);
					printText(label.get("NoTxnFound"));
					printLine();
					printDashLine();
				}

				// ALIPAYCNOFFL
				if (!value.get("TotalTrxALIPAYCNOFFL").equals("0")) {
					printAlignment(1);
					printText(label.get("ALIPAYCNOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYCNOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYCNOFFL"));
					printLine();
					printDashLine();

					printQrContent(label, ALIPAYCNOFFLRecordsArray);
				}

				// ALIPAYHKOFFL
				if (!value.get("TotalTrxALIPAYHKOFFL").equals("0")) {
					printAlignment(1);
					printText(label.get("ALIPAYHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYHKOFFL"));
					printLine();
					printDashLine();

					printQrContent(label, ALIPAYHKOFFLRecordsArray);
				}

				// ALIPAYOFFL
				if (!value.get("TotalTrxALIPAYOFFL").equals("0")) {
					printAlignment(1);
					printText(label.get("ALIPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYOFFL"));
					printLine();
					printDashLine();

					printQrContent(label, ALIPAYOFFLRecordsArray);
				}

				// BOOSTOFFL
				if (!value.get("TotalTrxBOOSTOFFL").equals("0")) {
					printAlignment(1);
					printText(label.get("BOOSTOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxBOOSTOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntBOOSTOFFL"));
					printLine();
					printDashLine();

					printQrContent(label, BOOSTOFFLRecordsArray);
				}

				// GCASHOFFL
				if (!value.get("TotalTrxGCASHOFFL").equals("0")) {
					printAlignment(1);
					printText(label.get("GCASHOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxGCASHOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGCASHOFFL"));
					printLine();
					printDashLine();

					printQrContent(label, GCASHOFFLRecordsArray);
				}

				// GRABPAYOFFL
				if (!value.get("TotalTrxGRABPAYOFFL").equals("0")) {
					printAlignment(1);
					printText(label.get("GRABPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxGRABPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGRABPAYOFFL"));
					printLine();
					printDashLine();

					printQrContent(label, GRABPAYOFFLRecordsArray);
				}

				// MASTER
				if (!value.get("TotalTrxMASTER").equals("0")) {
					printAlignment(1);
					printText(label.get("MASTER"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxMASTER"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntMASTER"));
					printLine();
					printDashLine();

					printCardContent(label, masterRecordsArray);
				}

				// OEPAYOFFL
				if (!value.get("TotalTrxOEPAYOFFL").equals("0")) {
					printAlignment(1);
					printText(label.get("OEPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxOEPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntOEPAYOFFL"));
					printLine();
					printDashLine();

					printQrContent(label, OEPAYOFFLRecordsArray);
				}

				// PROMPTPAYOFFL
				if (!value.get("TotalTrxPROMPTPAYOFFL").equals("0")) {
					printAlignment(1);
					printText(label.get("PROMPTPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxPROMPTPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntPROMPTPAYOFFL"));
					printLine();
					printDashLine();

					printQrContent(label, PROMPTPAYOFFLRecordsArray);
				}

				// UNIONPAYOFFL
				if (!value.get("TotalTrxUNIONPAYOFFL").equals("0")) {
					printAlignment(1);
					printText(label.get("UNIONPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxUNIONPAYOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntUNIONPAYOFFL"));
					printLine();
					printDashLine();

					printCardContent(label, UNIONPAYOFFLRecordsArray);
				}

				// VISA
				if (!value.get("TotalTrxVISA").equals("0")) {
					printAlignment(1);
					printText(label.get("VISA"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxVISA"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntVISA"));
					printLine();
					printDashLine();

					printCardContent(label, visaRecordsArray);
				}


				// WECHATOFFL
				if (!value.get("TotalTrxWECHATOFFL").equals("0")) {

					printAlignment(1);
					printText(label.get("WECHATOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATOFFL"));
					printLine();
					printDashLine();

					printQrContent(label, WECHATOFFLRecordsArray);
				}

				// WECHATHKOFFL
				if (!value.get("TotalTrxWECHATHKOFFL").equals("0")) {

					printAlignment(1);
					printText(label.get("WECHATHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATHKOFFL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATHKOFFL"));
					printLine();
					printDashLine();

					printQrContent(label, WECHATHKOFFLRecordsArray);
				}

				// WECHATONL
				if (!value.get("TotalTrxWECHATONL").equals("0")) {
					printAlignment(1);
					printText(label.get("WECHATONL"));
					printLine();
					printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATONL"));
					printLine();
					printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATONL"));
					printLine();
					printDashLine();

					printQrContent(label, WECHATONLRecordsArray);
				}

				printLine();
				printAlignment(1);
				printText("===" + label.get("Footer") + "===");
				printLine(4);
			} catch (Exception e) {
				Log.d("KM LIEW", "Start Printing:" + e);
				Toast.makeText(this.context, R.string.bluetooth_send_fail, Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this.context, R.string.bluetooth_connect_fail, Toast.LENGTH_SHORT).show();

		}
	}

	public void printQrContent(Map<String, String> label, ArrayList<Record> records) {

		for (Record record : records) {
			String trxDate = record.getOrderdate();
			String datetrx = trxDate.substring(0, 8);
			String timetrx = trxDate.substring(8, 14);
			StringBuilder sbdate = new StringBuilder(datetrx);
			sbdate.insert(2, "/");
			sbdate.insert(5, "/");
			StringBuilder sbtime = new StringBuilder(timetrx);
			sbtime.insert(2, ":");
			sbtime.insert(5, ":");

			String currency = record.currency();
			String amount = record.getamt();
			String merRef = record.merref();
			String paymentRef = record.PayRef();
			String qr = record.getCardNo();

			try {
				printTwoColumn(label.get("TransactionDate"), sbdate.toString() + " " + sbtime.toString());
				printTwoColumn(label.get("Amount"), currency + " " + amount);
				printTwoColumn(label.get("MerRef"), merRef);
				printTwoColumn(label.get("PaymentRef"), paymentRef);
				printTwoColumn(label.get("QRNumber"), qr);
				printDashLine();
				printLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void printCardContent(Map<String, String> label, ArrayList<Record> records) {

		for (Record record : records) {
			String trxDate = record.getOrderdate();
			String datetrx = trxDate.substring(0, 8);
			String timetrx = trxDate.substring(8, 14);
			StringBuilder sbdate = new StringBuilder(datetrx);
			sbdate.insert(2, "/");
			sbdate.insert(5, "/");
			StringBuilder sbtime = new StringBuilder(timetrx);
			sbtime.insert(2, ":");
			sbtime.insert(5, ":");

			String currency = record.currency();
			String amount = record.getamt();
			String merRef = record.merref();
			String paymentRef = record.PayRef();
			String cardNo = record.getCardNo();

			try {
				printTwoColumn(label.get("TransactionDate"), sbdate.toString() + " " + sbtime.toString());
				printTwoColumn(label.get("Amount"), currency + " " + amount);
				printTwoColumn(label.get("MerRef"), merRef);
				printTwoColumn(label.get("PaymentRef"), paymentRef);
				printTwoColumn(label.get("CardNo"), cardNo);
				printDashLine();
				printLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendSettlementReport(Context context, Map<String, String> info, Map<String, String> product, Bitmap bitmap) {
		if (this.isConnection) {
			Log.d("OTTO", "开始打印:" + "\n" + info + "\n" + product);
			try {

				printLine();
				printAlignment(1);
				printBitmap(bitmap);

				printLargerText(info.get("MerchantName"));
				printLine();

				printTwoColumn(info.get("info_tid"), product.get("value_tid"));
				printLine();

				printTwoColumn(info.get("info_bankMid"), product.get("value_bankMid"));
				printLine();

				printTwoColumn(info.get("info_date"), info.get("info_time"));
				printLine();

				printTwoColumn(info.get("info_batch"), product.get("value_batch"));
				printLine();

				printTwoColumn(info.get("info_host"), product.get("value_host"));
				printLine();

				printTwoColumn(info.get("info_nii"), product.get("value_nii"));
				printLine();

				printText(product.get("payMethod"));
				printLine();

				printAlignment(1);
				printText("***SETTLEMENT***");

				printLine();
				printAlignment(1);
				printText("SUCCESSFUL");
				printLine();

				printLine();
				printLine();
				printAlignment(1);

			} catch (Exception e) {
				Log.d("OTTO", "开始打印:" + e);
				e.printStackTrace();
				Toast.makeText(this.context, R.string.bluetooth_send_fail, Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this.context, R.string.bluetooth_connect_fail, Toast.LENGTH_SHORT).show();
		}
	}


	//--------------打印排版-----------------

	/**
	 * return length 需要打印的空行数
	 */
	public static String makelinefeed(String s, String isCN) {
		String result = "";
		if ("T".equals(isCN)) {
			result = makelinefeedCN(s);
		} else {
			result = makelinefeedEN(s);
		}
		return result;
	}

	public static String makelinefeedEN(String s) {
		String result = "";

		String[] str = s.split(" ");
		int len = 0;
		String res_str = "";

		for (int i = 0; i < str.length; i++) {
			if ((len + str[i].length()) > 32) {
				res_str = res_str + "\n" + str[i] + " ";
				len = str[i].length() + 1;
			} else {
				if ((len + str[i].length() + 1) >= 32) {
					res_str = res_str + str[i];
					len = len + str[i].length();
				} else {
					res_str = res_str + str[i] + " ";
					len = len + str[i].length() + 1;
				}
			}
		}
		result = res_str;
		result = result.replace(", ", ",");
		result = result.replace(". ", ".");
		return result;
	}

	public static String makelinefeedCN(String s) {
		String result = "";
		result = s;
		return result;
	}

	//---------------------------printer layout things--------------------------------//
	private void printDashLine() throws IOException {
		printText("--------------------------------");
	}

	private void printDoubleDashLine() throws IOException {
		printText("================================");
	}

	private void printLine(int lineNum) throws IOException {
		for (int i = 0; i < lineNum; i++) {
			mWriter.write("\n");
			mWriter.flush();
		}
	}

	private void printAlignment(int alignment) throws IOException {
		mWriter.write(0x1b);
		mWriter.write(0x61);
		mWriter.write(alignment);
	}

	private void printLine() throws IOException {
		printLine(1);
	}

	private void printText(String text) throws IOException {
		outputStream.write(ESCUtil.boldOff());
		outputStream.write(ESCUtil.setCodeSystem((byte) 0));
		outputStream.write(getEncoding(text));
		outputStream.flush();
        /*mWriter.write(text);
        mWriter.flush();*/
	}

    /*private void printTextAlignLeft(String text) throws IOException {
        outputStream.write(ESCUtil.boldOff());
        outputStream.write(ESCUtil.alignLeft());
        outputStream.write(ESCUtil.setCodeSystem((byte) 0));
        outputStream.write(getEncoding(text));
    }

    private void printTextAlignRight(String text) throws IOException {
        outputStream.write(ESCUtil.boldOff());
        outputStream.write(ESCUtil.alignRight());
        outputStream.write(ESCUtil.setCodeSystem((byte) 0));
        outputStream.write(getEncoding(text));
    }*/

	private void printLargeText(String text) throws IOException {
		outputStream.write(ESCUtil.boldOn());
		outputStream.write(ESCUtil.alignCenter());
		outputStream.write(ESCUtil.singleByteOff());
		outputStream.write(ESCUtil.setCodeSystem((byte) 0));
		outputStream.write(getEncoding(text));
       /* mWriter.write(0x1b);
        mWriter.write(0x21);
        mWriter.write(12);
        mWriter.write(getEncoding(text).toString());
        mWriter.write(0x1b);
        mWriter.write(0x21);
        mWriter.write(0);
        mWriter.flush();*/
	}

	private void printLargerText(String text) throws IOException {
		outputStream.write(ESCUtil.boldOn());
		outputStream.write(ESCUtil.alignCenter());
		outputStream.write(ESCUtil.singleByteOff());
		outputStream.write(ESCUtil.setCodeSystem((byte) 0));
		outputStream.write(getEncoding(text));
       /* mWriter.write(0x1b);
        mWriter.write(0x21);
        mWriter.write(22);
        mWriter.write(text);
        mWriter.write(0x1b);
        mWriter.write(0x21);
        mWriter.write(0);
        mWriter.flush();*/
	}

	private void printTwoColumn(String title, String content) throws IOException {

		int titleWidth = 0;
		int contentWidth = 0;

		titleWidth = getStringPixLength(title);
		contentWidth = getStringPixLength(content);
		int contentSpace = WIDTH_PIXEL - titleWidth;

		int index = 0;
		List<String> strings = new ArrayList<String>();
		do{
			StringBuffer sbContent = new StringBuffer();

			while (index < content.length() && getStringPixLength(sbContent.toString()) < contentSpace) {
				sbContent.append(content.charAt(index));

				index++;
			}
			// Add the string into a list
			strings.add(sbContent.toString());

		}while (index < content.length());

		int loop = contentWidth / contentSpace;
		int charLeft = contentWidth % contentSpace;

		if (charLeft != 0 || contentWidth == 0) {
			loop += 1;
		}

		for (int count = 0; count < loop; count++) {

			int iNum = 0;
			byte[] byteBuffer = new byte[200];
			byte[] tmp;
			if (count == 0) {

				tmp = getEncoding(title);
				System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
				iNum += tmp.length;

				tmp = setLocation(getOffset(strings.get(count)));
				System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
				iNum += tmp.length;

				tmp = getEncoding(strings.get(count));
				System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);

			} else {
				printLine();
				tmp = setLocation(getOffset(strings.get(count)));
				System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
				iNum += tmp.length;

				tmp = getEncoding(strings.get(count));
				System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
			}

			print(byteBuffer);
		}

		/*int iNum = 0;
		byte[] byteBuffer = new byte[200];
		byte[] tmp;

		tmp = getEncoding(title);
		System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
		iNum += tmp.length;
		//        System.out.println("tmp.length: " + tmp.length);
		//        System.out.println("iNum: " + iNum);

		tmp = setLocation(getOffset(content));
		System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
		iNum += tmp.length;
		//        System.out.println("tmp.length: " + tmp.length);
		//        System.out.println("iNum: " + iNum);

		tmp = getEncoding(content);
		System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);*/
	}

	private void printLargeTwoColumn(String title, String content) throws IOException {
		mWriter.write(0x1b);
		mWriter.write(0x61);
		mWriter.write(0);
		mWriter.write(0x1b);
		mWriter.write(0x21);
		mWriter.write(22);
		mWriter.write(title);
		mWriter.write(0x1b);
		mWriter.write(0x21);
		mWriter.write(0);
		mWriter.flush();
		mWriter.write(0x1b);
		mWriter.write(0x61);
		mWriter.write(2);
		mWriter.write(0x1b);
		mWriter.write(0x21);
		mWriter.write(22);
		mWriter.write(content);
		mWriter.write(0x1b);
		mWriter.write(0x21);
		mWriter.write(0);
		mWriter.flush();
	}

	private byte[] getEncoding(String stText) throws IOException {
		byte[] returnText = stText.getBytes("GB18030"); // 必须放在try内才可以
		return returnText;
	}

	private int getEncodingLength(String stText) throws IOException {
		int length = 0;

		if(lang.equals(Constants.LANG_THAI)){
			length = stText.getBytes("TIS-620").length;
		}else{
			length = stText.getBytes("GB18030").length;
		}
		return length;
	}

	private void print(byte[] bs) throws IOException {
		outputStream.write(ESCUtil.font());
		outputStream.write(ESCUtil.boldOff());
		outputStream.write(ESCUtil.singleByteOff());
		outputStream.write(ESCUtil.setCodeSystem((byte) 0));
		outputStream.write(bs);
	}

	private byte[] setLocation(int offset) throws IOException {
		byte[] bs = new byte[4];
		bs[0] = 0x1B;
		bs[1] = 0x24;
		bs[2] = (byte) (offset % 256);
		bs[3] = (byte) (offset / 256);
		return bs;
	}

	private int getOffset(String str) {
		return WIDTH_PIXEL - getStringPixLength(str);
	}

	private int getStringPixLength(String str) {
		int pixLength = 0;
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (Pinyin.isChinese(c)) {
				pixLength += 24;
			} else {
				pixLength += 12;
			}
		}
		return pixLength;
	}
	//---------------------------printer layout things--------------------------------//

	public void printBitmap(Bitmap bmp) throws IOException {
		bmp = compressPic(bmp);
		byte[] bmpByteArray = draw2PxPoint(bmp);
		printRawBytes(bmpByteArray);
	}

	public void printRawBytes(byte[] bytes) throws IOException {
		outputStream.write(bytes);
		outputStream.flush();
	}

	/*************************************************************************
	 * 假设一个360*360的图片，分辨率设为24, 共分15行打印 每一行,是一个 360 * 24 的点阵,y轴有24个点,存储在3个byte里面。
	 * 即每个byte存储8个像素点信息。因为只有黑白两色，所以对应为1的位是黑色，对应为0的位是白色
	 **************************************************************************/
	private byte[] draw2PxPoint(Bitmap bmp) {
		//先设置一个足够大的size，最后在用数组拷贝复制到一个精确大小的byte数组中
		int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
		byte[] tmp = new byte[size];
		int k = 0;
		// 设置行距为0
		tmp[k++] = 0x1B;
		tmp[k++] = 0x33;
		tmp[k++] = 0x00;
		// 居中打印
		tmp[k++] = 0x1B;
		tmp[k++] = 0x61;
		tmp[k++] = 1;
		for (int j = 0; j < bmp.getHeight() / 24f; j++) {
			tmp[k++] = 0x1B;
			tmp[k++] = 0x2A;// 0x1B 2A 表示图片打印指令
			tmp[k++] = 33; // m=33时，选择24点密度打印
			tmp[k++] = (byte) (bmp.getWidth() % 256); // nL
			tmp[k++] = (byte) (bmp.getWidth() / 256); // nH
			for (int i = 0; i < bmp.getWidth(); i++) {
				for (int m = 0; m < 3; m++) {
					for (int n = 0; n < 8; n++) {
						byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
						tmp[k] += tmp[k] + b;
					}
					k++;
				}
			}
			tmp[k++] = 10;// 换行
		}
		// 恢复默认行距
		tmp[k++] = 0x1B;
		tmp[k++] = 0x32;

		byte[] result = new byte[k];
		System.arraycopy(tmp, 0, result, 0, k);
		return result;
	}

	/**
	 * 图片二值化，黑色是1，白色是0
	 *
	 * @param x   横坐标
	 * @param y   纵坐标
	 * @param bit 位图
	 * @return
	 */
	private byte px2Byte(int x, int y, Bitmap bit) {
		if (x < bit.getWidth() && y < bit.getHeight()) {
			byte b;
			int pixel = bit.getPixel(x, y);
			int red = (pixel & 0x00ff0000) >> 16; // 取高两位
			int green = (pixel & 0x0000ff00) >> 8; // 取中两位
			int blue = pixel & 0x000000ff; // 取低两位
			int gray = RGB2Gray(red, green, blue);
			if (gray < 128) {
				b = 1;
			} else {
				b = 0;
			}
			return b;
		}
		return 0;
	}

	/**
	 * 图片灰度的转化
	 */
	private int RGB2Gray(int r, int g, int b) {
		int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b); // 灰度转化公式
		return gray;
	}

	/**
	 * 对图片进行压缩（去除透明度）
	 *
	 * @param bitmapOrg
	 */
	private Bitmap compressPic(Bitmap bitmapOrg) {
		// 获取这个图片的宽和高
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		// 定义预转换成的图片的宽度和高度
		int newWidth = IMAGE_SIZE;
		int newHeight = IMAGE_SIZE / 2;
		Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
		Canvas targetCanvas = new Canvas(targetBmp);
		targetCanvas.drawColor(0xffffffff);
		targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
		return targetBmp;
	}
}
