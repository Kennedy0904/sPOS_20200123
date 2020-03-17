package com.example.dell.smartpos.Printer.K9;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.centerm.smartpos.aidl.printer.AidlPrinter;
import com.centerm.smartpos.aidl.printer.AidlPrinterStateChangeListener;
import com.centerm.smartpos.aidl.printer.PrinterParams;
import com.centerm.smartpos.aidl.sys.AidlDeviceManager;
import com.centerm.smartpos.constant.Constant;
import com.centerm.smartpos.util.LogUtil;
import com.example.dell.smartpos.GlobalFunction;
import com.example.dell.smartpos.R;
import com.example.dell.smartpos.Record;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PrintServiceThaiK9 {

    private final static String TAG = "PrintServiceThaiK9";

    public AidlDeviceManager manager = null;
    private AidlPrinter printDev = null;
    private AidlPrinterStateChangeListener callback = new PrinterCallback();
    Context context;

    List<PrinterParams> textList1 = new ArrayList<PrinterParams>();
    PrinterParams printerParams = new PrinterParams();
    public String lang;

    public PrintServiceThaiK9(Context context) {
        super();
        this.context = context;
        printDev = PrintUtilK9.getPrinterDev();
    }

    /**
     * Bind Service
     */
    public boolean connect() {

        Boolean flag = false;

        if(printDev == null){
            Log.d(TAG, "Printer is null");
        }else{
            flag = true;
        }

//        try {
//            printDev.initPrinter();
//        } catch (RemoteException e) {
//            flag = false;
//            e.printStackTrace();
//            Log.d(TAG, "Printer initial failed");
//        }

        return flag;
    }

    private class PrinterCallback extends AidlPrinterStateChangeListener.Stub {

        @Override
        public void onPrintError(int arg0) throws RemoteException {
            // showMessage("打印机异常" + arg0, Color.RED);
            //getMessStr(arg0);
            Log.d(TAG, String.valueOf(arg0));
        }

        @Override
        public void onPrintFinish() throws RemoteException {
            //showMessage(getResources().getString(R.string.printer_finish), "", Color.BLACK);
            Log.d(TAG,"The print data is complete");
        }

        @Override
        public void onPrintOutOfPaper() throws RemoteException {
            //showMessage(getResources().getString(R.string.printer_need_paper), "", Color.RED);
            Log.d(TAG,"The printer is out of paper,Please load paper and try again");
        }
    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("base", "action:" +intent.getAction());
        }
    }

    public void bindService() {
        Log.d(TAG,"Enter bindService");
        Intent intent = new Intent();
        intent.setPackage("com.centerm.smartposservice");
        intent.setAction("com.centerm.smartpos.service.MANAGER_SERVICE");
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
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
            Log.d(TAG,"Enter onServiceConnected");
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

    public void printReceipt(Context context, Map<String, String> info, Map<String, String> product, Bitmap bitmap) {
        bindService();
        try {

            Log.d(TAG,"Start Receipt Print (Thai)");

            printBitmap(bitmap, 1);

            // Merchant Name
            printHeader(info.get("MerchantName"));
			printLine(1);

			if (!GlobalFunction.getAddress1(context).equals("")) {
				printText(GlobalFunction.getAddress1(context));
				printLine(1);
			}

			if (!GlobalFunction.getAddress2(context).equals("")) {
				printText(GlobalFunction.getAddress2(context));
				printLine(1);
			}

			if (!GlobalFunction.getAddress3(context).equals("")) {
				printText(GlobalFunction.getAddress3(context));
				printLine(1);
			}

            // Payment Type
            printTwoColumnNoAlign(info.get("PaymentType"), product.get("PayType"));
//            printTwoColumnWithCharSpacing(info.get("PaymentType"), product.get("PayType"), 26);

            // Payment Method
            printTwoColumnNoAlign(info.get("PayMethod"),  product.get("PayMethod"));
//            printTwoColumnWithCharSpacing(info.get("PayMethod"),  product.get("PayMethod"), 35);

            // Payment Status
            printTwoColumnNoAlign(info.get("PayStatus"), product.get("PayStatus"));
//            printTwoColumnWithCharSpacing(info.get("PayStatus"), product.get("PayStatus"), 25);

            // Operator
            printTwoColumnNoAlign(info.get("Operator"), product.get("Operator"));
//            printTwoColumnWithCharSpacing(info.get("Operator"), product.get("Operator"), 25);

            // Trx Date
            printTwoColumnNoAlign(info.get("TransactionDate"), product.get("TransactionDate"));
//            printTwoColumnWithCharSpacing(info.get("TransactionDate"), product.get("TransactionDate"), 5);

            // Trx No
            printTwoColumnNoAlign(info.get("getCardNo"), product.get("getCardNo"));
//            printTwoColumnWithCharSpacing(info.get("getCardNo"), product.get("getCardNo"), 30);

            // Merchant Ref
            printTwoColumnNoAlign(info.get("MerchantRef"), product.get("MerchantRef"));
//            printTwoColumnWithCharSpacing(info.get("MerchantRef"), product.get("MerchantRef"), 39);

            // Payment Ref
            printTwoColumnNoAlign(info.get("PaymentRef"), product.get("PaymentRef"));
//            printTwoColumnWithCharSpacing(info.get("PaymentRef"), product.get("PaymentRef"), 2);

            // Amount
            printTwoColumnNoAlign(info.get("TotalAmount"),product.get("CurrCode") + "\u2004" + product.get("Amount"));
//            printTwoColumnWithCharSpacing(info.get("TotalAmount"),product.get("CurrCode") + "\u2004" + product.get("Amount"), 45);

            // Surcharge
            /*SharedPreferences prefsettings = context.getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
            String SurCalstat = (prefsettings.getString(Constants.pref_surcharge_calculation, Constants.default_surcharge_calculation));
            SharedPreferences pref_sur = context.getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
            String mdr = pref_sur.getString(Constants.mer_mdr, "");
            if ((product.get("Surcharge") != null) || (!"".equals(product.get("Surcharge")))) {
                if ("OFF".equals(SurCalstat) || mdr == null || "".equals(mdr)) {
                    Log.d(TAG, "PrintService OFF null empty");
                } else if ("OFF".equals(SurCalstat) || Double.valueOf(mdr) <= 0) {
                    Log.d(TAG,"PrintService OFF or <0");
                } else if ("ON".equals(SurCalstat) && Double.valueOf(mdr) > 0) {
                    if ("".equals(product.get("Surcharge")) || product.get("Surcharge") == null) {
                        Log.d(TAG,"PrintService surcharge null empty");
                    } else if (Double.valueOf(product.get("Surcharge")) <= 0) {
                        Log.d(TAG,"PrintService surcharge <0");
                    } else if ((Double.valueOf(product.get("Surcharge")) > 0)) {
                        Log.d(TAG,"PrintService surcharge >0");
                        printTwoColumnWithCharSpacing(info.get("Surcharge"), product.get("CurrCode") + " " + product.get("Surcharge"));
                        printTwoColumnWithCharSpacing(info.get("MerRequestAmt"), product.get("CurrCode") + " " + product.get("MerRequestAmt"));
                    } else {
                        Log.d(TAG,"PrintService surcharge else");
                    }
                } else {
                    Log.d(TAG,"PrintService else");
                }
            }*/

            printText(makelinefeed(info.get("note"), info.get("isCN")));
            printDashLine();

            // Current Date
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            printTextCenterWithSpacing(date.format(new Date()), 15);

            // Copy
            if (info.get("copy").equalsIgnoreCase(context.getString(R.string.print_customerCopy))) {
                printTextCenter("=== " + context.getString(R.string.print_customerCopy) + " ===");
            } else if (info.get("copy").equalsIgnoreCase(context.getString(R.string.print_merchantCopy))) {
                printTextCenter("=== " + context.getString(R.string.print_merchantCopy) + " ===");
            }

			printTextCenter("***" + info.get("Title") + "***");

            printLine(8);
            printDev.printDatas(textList1, callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printSummaryReport(Context context, Map<String, String> label, Map<String, String> value) {

        try{
            Log.d(TAG,"Start Receipt Summary Report (Thai)");

            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            printHeader(label.get("Title"));

            printTextCenter(value.get("MerName"));

            printTwoColumnAlign(label.get("FromDateLabel"), value.get("From_date"), 22);
            printTwoColumnAlign(label.get("ToDateLabel"), " " + value.get("To_date"), 23);
//            printText(label.get("FromDateLabel") + "                   " +  value.get("From_date"));
//            printText(label.get("ToDateLabel") + "                   " +  value.get("ToDateLabel"));
            printText(date.format(new Date()));

            printDashLine();

            printTextCenter(label.get("Header"));
            printTwoColumnNoAlign(label.get("GrandTotalTrxLabel"), value.get("currCode") + " " + value.get("GrandTotalAmnt"));
            printTwoColumnNoAlign(label.get("GrandTotalRefundLabel"), value.get("currCode") + " " + value.get("GrandTotalRefundAmnt"));
            printTwoColumnNoAlign(label.get("GrandTotalRequestRefundLabel"), value.get("currCode") + " " + value.get("GrandTotalRequestRefundAmnt"));
            printTwoColumnNoAlign(label.get("GrandTotalVoidLabel"), value.get("currCode") + " " + value.get("GrandTotalVoidAmnt"));

            printDashLine();

            if (value.get("GrandTotalAmnt").equals("0.00") && value.get("GrandTotalRefundAmnt").equals("0.00") && value.get("GrandTotalRequestRefundAmnt").equals("0.00") && value.get("GrandTotalVoidAmnt").equals("0.00")) {
                printTextCenter(label.get("NoTxnFound"));
                printDashLine();
            }

            // ALIPAYCNOFFL
            if (!value.get("TotalTrxALIPAYCNOFFL").equals("0") || !value.get("TotalRefundTrxALIPAYCNOFFL").equals("0") || !value.get("TotalRequestRefundTrxALIPAYCNOFFL").equals("0") || !value.get("TotalVoidTrxALIPAYCNOFFL").equals("0")) {
                printTextCenter(label.get("ALIPAYCNOFFL"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYCNOFFL"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYCNOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxALIPAYCNOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntALIPAYCNOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxALIPAYCNOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntALIPAYCNOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxALIPAYCNOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntALIPAYCNOFFL"));
                printDashLine();
            }

            // ALIPAYHKOFFL
            if (!value.get("TotalTrxALIPAYHKOFFL").equals("0") || !value.get("TotalRefundTrxALIPAYHKOFFL").equals("0") || !value.get("TotalRequestRefundTrxALIPAYHKOFFL").equals("0") || !value.get("TotalVoidTrxALIPAYHKOFFL").equals("0")) {
                printTextCenter(label.get("ALIPAYHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxALIPAYHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntALIPAYHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxALIPAYHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntALIPAYHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxALIPAYHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntALIPAYHKOFFL"));
                printDashLine();
            }

            // ALIPAYOFFL
            if (!value.get("TotalTrxALIPAYOFFL").equals("0") || !value.get("TotalRefundTrxALIPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxALIPAYOFFL").equals("0") || !value.get("TotalVoidTrxALIPAYOFFL").equals("0")) {
                printTextCenter(label.get("ALIPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxALIPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntALIPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxALIPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntALIPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxALIPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntALIPAYOFFL"));
                printDashLine();
            }

            // BOOSTOFFL
            if (!value.get("TotalTrxBOOSTOFFL").equals("0") || !value.get("TotalRefundTrxBOOSTOFFL").equals("0") || !value.get("TotalRequestRefundTrxBOOSTOFFL").equals("0") || !value.get("TotalVoidTrxBOOSTOFFL").equals("0")) {
                printTextCenter(label.get("BOOSTOFFL"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxBOOSTOFFL"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntBOOSTOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxBOOSTOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntBOOSTOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxBOOSTOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntBOOSTOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxBOOSTOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntBOOSTOFFL"));
                printDashLine();
            }

            // GCASHOFFL
            if (!value.get("TotalTrxGCASHOFFL").equals("0") || !value.get("TotalRefundTrxGCASHOFFL").equals("0") || !value.get("TotalRequestRefundTrxGCASHOFFL").equals("0") || !value.get("TotalVoidTrxGCASHOFFL").equals("0")) {
                printTextCenter(label.get("GCASHOFFL"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxGCASHOFFL"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGCASHOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxGCASHOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntGCASHOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxGCASHOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntGCASHOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxGCASHOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntGCASHOFFL"));
                printDashLine();
            }

            // GRABPAYOFFL
            if (!value.get("TotalTrxGRABPAYOFFL").equals("0") || !value.get("TotalRefundTrxGRABPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxGRABPAYOFFL").equals("0") || !value.get("TotalVoidTrxGRABPAYOFFL").equals("0")) {
                printTextCenter(label.get("GRABPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxGRABPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGRABPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxGRABPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntGRABPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxGRABPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntGRABPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxGRABPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntGRABPAYOFFL"));
                printDashLine();
            }

            // MASTER
            if (!value.get("TotalTrxMASTER").equals("0")) {
                printTextCenter(label.get("MASTER"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxMASTER"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntMASTER"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxMASTER"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntMASTER"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxMASTER"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntMASTER"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxMASTER"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntMASTER"));
                printDashLine();
            }

            // OEPAYOFFL
            if (!value.get("TotalTrxOEPAYOFFL").equals("0") || !value.get("TotalRefundTrxOEPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxOEPAYOFFL").equals("0") || !value.get("TotalVoidTrxOEPAYOFFL").equals("0")) {
                printTextCenter(label.get("OEPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxOEPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntOEPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxOEPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntOEPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxOEPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntOEPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxOEPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntOEPAYOFFL"));
                printDashLine();
            }

            // PROMPTPAYOFFL
            if (!value.get("TotalTrxPROMPTPAYOFFL").equals("0") || !value.get("TotalRefundTrxPROMPTPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxPROMPTPAYOFFL").equals("0") || !value.get("TotalVoidTrxPROMPTPAYOFFL").equals("0")) {
                printTextCenter(label.get("PROMPTPAYOFFL"));
                printLine(1);
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxPROMPTPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntPROMPTPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxPROMPTPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntPROMPTPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxPROMPTPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntPROMPTPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxPROMPTPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntPROMPTPAYOFFL"));
                printDashLine();
            }

            // UNIONPAYOFFL
            if (!value.get("TotalTrxUNIONPAYOFFL").equals("0") || !value.get("TotalRefundTrxUNIONPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxUNIONPAYOFFL").equals("0") || !value.get("TotalVoidTrxUNIONPAYOFFL").equals("0")) {
                printTextCenter(label.get("UNIONPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxUNIONPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntUNIONPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxUNIONPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntUNIONPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxUNIONPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntUNIONPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxUNIONPAYOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntUNIONPAYOFFL"));
                printDashLine();
            }

            // VISA
            if (!value.get("TotalTrxVISA").equals("0") || !value.get("TotalRefundTrxVISA").equals("0") || !value.get("TotalRequestRefundTrxVISA").equals("0") || !value.get("TotalVoidTrxVISA").equals("0")) {
                printTextCenter(label.get("VISA"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxVISA"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntVISA"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxVISA"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntVISA"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxVISA"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntVISA"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxVISA"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntVISA"));
                printDashLine();
            }

            // WECHATOFFL
            if (!value.get("TotalTrxWECHATOFFL").equals("0") || !value.get("TotalRefundTrxWECHATOFFL").equals("0") || !value.get("TotalRequestRefundTrxWECHATOFFL").equals("0") || !value.get("TotalVoidTrxWECHATOFFL").equals("0")) {
                printTextCenter(label.get("WECHATOFFL"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATOFFL"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxWECHATOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntWECHATOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxWECHATOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntWECHATOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxWECHATOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntWECHATOFFL"));
                printDashLine();
            }

            // WECHATHKOFFL
            if (!value.get("TotalTrxWECHATHKOFFL").equals("0") || !value.get("TotalRefundTrxWECHATHKOFFL").equals("0") || !value.get("TotalRequestRefundTrxWECHATHKOFFL").equals("0") || !value.get("TotalVoidTrxWECHATHKOFFL").equals("0")) {
                printTextCenter(label.get("WECHATHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxWECHATHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntWECHATHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxWECHATHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntWECHATHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxWECHATHKOFFL"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntWECHATHKOFFL"));
                printDashLine();
            }

            // WECHATONL
            if (!value.get("TotalTrxWECHATONL").equals("0") || !value.get("TotalRefundTrxWECHATONL").equals("0") || !value.get("TotalRequestRefundTrxWECHATONL").equals("0") || !value.get("TotalVoidTrxWECHATONL").equals("0")) {
                printTextCenter(label.get("WECHATONL"));
                printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATONL"));
                printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATONL"));
                printTwoColumnNoAlign(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxWECHATONL"));
                printTwoColumnNoAlign(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntWECHATONL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxWECHATONL"));
                printTwoColumnNoAlign(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntWECHATONL"));
                printTwoColumnNoAlign(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxWECHATONL"));
                printTwoColumnNoAlign(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntWECHATONL"));
                printDashLine();
            }

            printTextCenter("===" + label.get("Footer") + "===");
            printLine(8);

            printDev.printDatas(textList1, callback);

        }catch (Exception e){
            Toast.makeText(this.context, R.string.bluetooth_send_fail, Toast.LENGTH_SHORT).show();
        }
    }

	public void printReportCenterm(Context context, Map<String, String> label, Map<String, String> value,
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

		try{
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			printHeader(label.get("Title"));

			printTextCenter(value.get("MerName"));

			printTwoColumnAlign(label.get("FromDateLabel"), value.get("From_date"), 22);
			printTwoColumnAlign(label.get("ToDateLabel"), " " + value.get("To_date"), 23);

			printText(date.format(new Date()));

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
				printText(label.get("NoTxnFound"));
				printDashLine();
			}

			// ALIPAYCNOFFL
			if (!value.get("TotalTrxALIPAYCNOFFL").equals("0")) {
				printTextCenter(label.get("ALIPAYCNOFFL"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYCNOFFL"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYCNOFFL"));
				printDashLine();

				printQrContent(label, ALIPAYCNOFFLRecordsArray);
			}

			// ALIPAYHKOFFL
			if (!value.get("TotalTrxALIPAYHKOFFL").equals("0")) {
				printTextCenter(label.get("ALIPAYHKOFFL"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYHKOFFL"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYHKOFFL"));
				printDashLine();

				printQrContent(label, ALIPAYHKOFFLRecordsArray);
			}

			// ALIPAYOFFL
			if (!value.get("TotalTrxALIPAYOFFL").equals("0")) {
				printTextCenter(label.get("ALIPAYOFFL"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYOFFL"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYOFFL"));
				printDashLine();

				printQrContent(label, ALIPAYOFFLRecordsArray);
			}

			// BOOSTOFFL
			if (!value.get("TotalTrxBOOSTOFFL").equals("0")) {
				printTextCenter(label.get("BOOSTOFFL"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxBOOSTOFFL"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntBOOSTOFFL"));
				printDashLine();

				printQrContent(label, BOOSTOFFLRecordsArray);
			}

			// GCASHOFFL
			if (!value.get("TotalTrxGCASHOFFL").equals("0")) {
				printTextCenter(label.get("GCASHOFFL"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxGCASHOFFL"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGCASHOFFL"));
				printDashLine();

				printQrContent(label, GCASHOFFLRecordsArray);
			}

			// GRABPAYOFFL
			if (!value.get("TotalTrxGRABPAYOFFL").equals("0")) {
				printTextCenter(label.get("GRABPAYOFFL"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxGRABPAYOFFL"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGRABPAYOFFL"));
				printDashLine();

				printQrContent(label, GRABPAYOFFLRecordsArray);
			}

			// MASTER
			if (!value.get("TotalTrxMASTER").equals("0")) {
				printTextCenter(label.get("MASTER"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxMASTER"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntMASTER"));
				printDashLine();

				printCardContent(label, masterRecordsArray);
			}

			// OEPAYOFFL
			if (!value.get("TotalTrxOEPAYOFFL").equals("0")) {
				printTextCenter(label.get("OEPAYOFFL"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxOEPAYOFFL"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntOEPAYOFFL"));
				printDashLine();

				printQrContent(label, OEPAYOFFLRecordsArray);
			}

			// PROMPTPAYOFFL
			if (!value.get("TotalTrxPROMPTPAYOFFL").equals("0")) {
				printTextCenter(label.get("PROMPTPAYOFFL"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxPROMPTPAYOFFL"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntPROMPTPAYOFFL"));
				printDashLine();

				printQrContent(label, PROMPTPAYOFFLRecordsArray);
			}

			// UNIONPAYOFFL
			if (!value.get("TotalTrxUNIONPAYOFFL").equals("0")) {
				printTextCenter(label.get("UNIONPAYOFFL"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxUNIONPAYOFFL"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntUNIONPAYOFFL"));
				printDashLine();

				printCardContent(label, UNIONPAYOFFLRecordsArray);
			}

			// VISA
			if (!value.get("TotalTrxVISA").equals("0")) {
				printTextCenter(label.get("VISA"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxVISA"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntVISA"));
				printDashLine();

				printCardContent(label, visaRecordsArray);
			}


			// WECHATOFFL
			if (!value.get("TotalTrxWECHATOFFL").equals("0")) {
				printTextCenter(label.get("WECHATOFFL"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATOFFL"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATOFFL"));
				printDashLine();

				printQrContent(label, WECHATOFFLRecordsArray);
			}

			// WECHATHKOFFL
			if (!value.get("TotalTrxWECHATHKOFFL").equals("0")) {
				printTextCenter(label.get("WECHATHKOFFL"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATHKOFFL"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATHKOFFL"));
				printDashLine();

				printQrContent(label, WECHATHKOFFLRecordsArray);
			}

			// WECHATONL
			if (!value.get("TotalTrxWECHATONL").equals("0")) {
				printTextCenter(label.get("WECHATONL"));
				printTwoColumnNoAlign(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATONL"));
				printTwoColumnNoAlign(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATONL"));
				printDashLine();

				printQrContent(label, WECHATONLRecordsArray);
			}

			printTextCenter("===" + label.get("Footer") + "===");
			printLine(8);

			printDev.printDatas(textList1, callback);

		}catch (Exception e){
			Toast.makeText(this.context, R.string.bluetooth_send_fail, Toast.LENGTH_SHORT).show();
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
				printTwoColumnNoAlign(label.get("TransactionDate"), sbdate.toString() + " " + sbtime.toString());
				printTwoColumnNoAlign(label.get("Amount"), currency + " " + amount);
				printTwoColumnNoAlign(label.get("MerRef"), merRef);
				printTwoColumnNoAlign(label.get("PaymentRef"), paymentRef);
				printText(label.get("QRNumber"));
				printText(qr);
				printDashLine();
			} catch (Exception e) {
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
				printTwoColumnNoAlign(label.get("TransactionDate"), sbdate.toString() + " " + sbtime.toString());
				printTwoColumnNoAlign(label.get("Amount"), currency + " " + amount);
				printTwoColumnNoAlign(label.get("MerRef"), merRef);
				printTwoColumnNoAlign(label.get("PaymentRef"), paymentRef);
				printTwoColumnNoAlign(label.get("CardNo"), cardNo);
				printDashLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    private void printText(String text){

        try {
            printerParams = new PrinterParams();
            printerParams.setAlign(PrinterParams.ALIGN.LEFT);
            printerParams.setText(text);
            printerParams.setTextSize(23);
            printerParams.setLineSpace(3);
            textList1.add(printerParams);

            // Add a line
            printLine(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printTextWithSpacing(String text, int spacing){

        try {
            printerParams = new PrinterParams();
            printerParams.setLineSpace(spacing);
            printerParams.setTextSize(23);
            printerParams.setText(text);
            textList1.add(printerParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printTextCenter(String text){

        try {
            PrinterParams printerParams = new PrinterParams();
            printerParams.setAlign(PrinterParams.ALIGN.CENTER);
            printerParams.setText(text);
            printerParams.setTextSize(23);
            textList1.add(printerParams);
            printLine(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printTextCenterWithSpacing(String text, int spacing){

        try {
            printerParams = new PrinterParams();
            printerParams.setAlign(PrinterParams.ALIGN.CENTER);
            printerParams.setLineSpace(spacing);
            printerParams.setTextSize(23);
            printerParams.setText(text);
            textList1.add(printerParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printHeader(String text){

        try {
            printerParams = new PrinterParams();
            printerParams.setAlign(PrinterParams.ALIGN.CENTER);
            printerParams.setLineSpace(10);
            printerParams.setTextSize(30);
            printerParams.setBold(true);
            printerParams.setText(text);
            textList1.add(printerParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printBitmap(Bitmap bitmap, int align){
        try {
            printDev.printBmpFastSync(bitmap, align);
            printLine(1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void printTwoColumnWithCharSpacing(String title, String content, int spacing) throws IOException {

        title = title == null?"":title;
        content = content == null?"":content;

        StringBuilder sb = new StringBuilder();
        sb.append(title);

        String s = new String("\u2006");
        int i = 0;
        while (i < spacing){
            sb.append(s);
            i++;
        }

        sb.append(content);

        Log.d(TAG, sb.toString());
        printText(sb.toString());
    }

    private void printTwoColumnAlign(String title, String content,int space) throws IOException {

//        String s1 = new String(title.getBytes("TIS-620"));
//        String s2 = new String(content.getBytes("TIS-620"));

        title = title == null ? "" : title;
        content = content == null ? "" : content;

        String titleFormat = "";

        if(title.isEmpty()){
            titleFormat = "%s";
        }else{
            titleFormat = "%-" + title.length() + "s";
        }

//        String contentFormat = "%" + (space - countThai(title)) + "s";
        String contentFormat = "%" + (space - title.length()) + "s";
        // compose the complete format information
        String columnFormat = titleFormat  + "" + contentFormat;
        Log.d(TAG, String.format(columnFormat, title, content));
        printText(String.format(columnFormat, title, content));
    }

    private void printTwoColumnNoAlign(String title, String content) throws IOException {

        title = title == null ? "" : title;
        content = content == null ? "" : content;

        StringBuilder sb = new StringBuilder();
        sb.append(title);

        String s = new String("\u2003");
        sb.append(s);

        sb.append(content);

        Log.d(TAG, sb.toString());
        printText(sb.toString());
    }

//    private void printTwoColumnWithLineSpacing(String title, String content, int spacing) throws IOException {
//
//        String titleFormat = "";
//
//        if(title.isEmpty()){
//            titleFormat = "%s";
//        }else{
//            titleFormat = "%-" + title.length() + "s";
//        }
//
//        String contentFormat = "%" + 5 + content.length() + "s";
//        // compose the complete format information
//        String columnFormat = titleFormat + "" + contentFormat;
//
//        printTextWithSpacing(String.format(columnFormat, title, content), spacing);
//    }

    private void printDashLine() throws IOException {
        printText("--------------------------------");
    }

    private void printLine(int line) throws RemoteException {

        int i = 0;

        try {
            for(i = 0; i < line; i++) {
                printerParams = new PrinterParams();
                printerParams.setLineSpace(0);
                printerParams.setTextSize(8);
//                printerParams.setText("|");
                textList1.add(printerParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    static boolean isMark(char ch)
    {
        int type = Character.getType(ch);
        return type == Character.NON_SPACING_MARK ||
                type == Character.ENCLOSING_MARK ||
                type == Character.COMBINING_SPACING_MARK;
    }

    public int countThai(String text){
        int count = 0;
        for(int i=0; i<text.length(); i++)
        {
            if(!isMark(text.charAt(i)))
                count++;
        }
        return count;
    }

    private byte[] getEncoding(String stText) throws IOException {
        byte[] returnText = stText.getBytes("TIS-620"); // 必须放在try内才可以
        return returnText;
    }
}
