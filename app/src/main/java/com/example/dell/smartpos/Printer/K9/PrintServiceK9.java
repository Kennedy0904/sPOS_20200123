package com.example.dell.smartpos.Printer.K9;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.centerm.smartpos.aidl.printer.AidlPrinter;
import com.centerm.smartpos.aidl.printer.AidlPrinterStateChangeListener;
import com.centerm.smartpos.aidl.printer.PrintDataObject;
import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.GlobalFunction;
import com.example.dell.smartpos.R;
import com.example.dell.smartpos.Record;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PrintServiceK9 {

    private final static String TAG = "PrintServiceK9";
    private AidlPrinter printDev = null;
    private AidlPrinterStateChangeListener callback = new PrinterCallback();

    Context context;
    List<PrintDataObject> textList = new ArrayList<PrintDataObject>();
    public String lang;

    public PrintServiceK9(Context context) {
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
            Log.d(TAG, "Printer is ready");
            flag = true;
        }

//        try {
//            printDev.initPrinter();
//        } catch (RemoteException e) {
//            flag = false;
//            e.printStackTrace();
//            Log.d(TAG, "Printer initial failed");
//        }
        Log.d(TAG, String.valueOf(flag));
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

    public void printReceipt(Context context, Map<String, String> info, Map<String, String> product, Bitmap bitmap) {

        try {

            Log.d(TAG,"Start Print");

            // Get device language
            lang = getLanguage(context);

            printBitmap(bitmap, 1);
            printLine(1);

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

            printLine(1);

            // Payment Type
            printTwoColumn(info.get("PaymentType"), product.get("PayType"));

            // Payment Method
            printTwoColumn(info.get("PayMethod"),  product.get("PayMethod"));

            // Payment Status
            printTwoColumn(info.get("PayStatus"), product.get("PayStatus"));

            // Operator
            printTwoColumnWithSpacing(info.get("Operator"), product.get("Operator"), 50);

            // Trx Date
            printTwoColumn(info.get("TransactionDate"), product.get("TransactionDate"));

            // Trx No
            printTwoColumn(info.get("getCardNo"), product.get("getCardNo"));

            // Merchant Ref
            printTwoColumn(info.get("MerchantRef"), product.get("MerchantRef"));
//
            // Payment Ref
            printTwoColumn(info.get("PaymentRef"), product.get("PaymentRef"));

            // Amount
            printTwoColumnWithSpacing(info.get("TotalAmount"),  product.get("CurrCode") + " " + product.get("Amount"), 50);

            // Surcharge
            SharedPreferences prefsettings = context.getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
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
                        printTwoColumn(info.get("Surcharge"), product.get("CurrCode") + " " + product.get("Surcharge"));
                        printTwoColumnWithSpacing(info.get("MerRequestAmt"), product.get("CurrCode") + " " + product.get("MerRequestAmt"), 50);
                    } else {
                        Log.d(TAG,"PrintService surcharge else");
                    }
                } else {
                    Log.d(TAG,"PrintService else");
                }
            }

            printTextWithSpacing(makelinefeed(info.get("note"), info.get("isCN")), 30);
            printDashLine();

            // Current Date
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            printTextCenterWithSpacing(date.format(new Date()), 50);

            // Copy
            if (info.get("copy").equalsIgnoreCase(context.getString(R.string.print_customerCopy))) {
                printTextCenter("=== " + context.getString(R.string.print_customerCopy) + " ===");
            } else if (info.get("copy").equalsIgnoreCase(context.getString(R.string.print_merchantCopy))) {
                printTextCenter("=== " + context.getString(R.string.print_merchantCopy) + " ===");
            }

            printTextCenter("***" + info.get("Title") + "***");

            printLine(3);
            printDev.printText(textList, callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printSummaryReport(Context context, Map<String, String> label, Map<String, String> value) {

        try{
            Log.d(TAG,"Start Summary Report Printing");

            // Get device language
            lang = getLanguage(context);

            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            printHeader(label.get("Title"));

            printTextCenter(value.get("MerName"));

            printTwoColumn(label.get("FromDateLabel"), value.get("From_date"));
            printTwoColumn(label.get("ToDateLabel"), value.get("To_date"));

            printText(date.format(new Date()));

            printDashLine();

            printTextCenter(label.get("Header"));
            printTwoColumn(label.get("GrandTotalTrxLabel"), value.get("currCode") + " " + value.get("GrandTotalAmnt"));
            printTwoColumn(label.get("GrandTotalRefundLabel"), value.get("currCode") + " " + value.get("GrandTotalRefundAmnt"));
            printTwoColumn(label.get("GrandTotalRequestRefundLabel"), value.get("currCode") + " " + value.get("GrandTotalRequestRefundAmnt"));
            printTwoColumn(label.get("GrandTotalVoidLabel"), value.get("currCode") + " " + value.get("GrandTotalVoidAmnt"));
            printDashLine();

            if (value.get("GrandTotalAmnt").equals("0.00") && value.get("GrandTotalRefundAmnt").equals("0.00") && value.get("GrandTotalRequestRefundAmnt").equals("0.00") && value.get("GrandTotalVoidAmnt").equals("0.00")) {
                printText(label.get("NoTxnFound"));
                printDashLine();
            }

            // ALIPAYCNOFFL
            if (!value.get("TotalTrxALIPAYCNOFFL").equals("0") || !value.get("TotalRefundTrxALIPAYCNOFFL").equals("0") || !value.get("TotalRequestRefundTrxALIPAYCNOFFL").equals("0") || !value.get("TotalVoidTrxALIPAYCNOFFL").equals("0")) {
                printTextCenter(label.get("ALIPAYCNOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYCNOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYCNOFFL"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxALIPAYCNOFFL"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntALIPAYCNOFFL"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxALIPAYCNOFFL"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntALIPAYCNOFFL"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxALIPAYCNOFFL"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntALIPAYCNOFFL"));
                printDashLine();
            }

            // ALIPAYHKOFFL
            if (!value.get("TotalTrxALIPAYHKOFFL").equals("0") || !value.get("TotalRefundTrxALIPAYHKOFFL").equals("0") || !value.get("TotalRequestRefundTrxALIPAYHKOFFL").equals("0") || !value.get("TotalVoidTrxALIPAYHKOFFL").equals("0")) {
                printTextCenter(label.get("ALIPAYHKOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYHKOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYHKOFFL"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxALIPAYHKOFFL"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntALIPAYHKOFFL"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxALIPAYHKOFFL"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntALIPAYHKOFFL"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxALIPAYHKOFFL"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntALIPAYHKOFFL"));
                printDashLine();
            }

            // ALIPAYOFFL
            if (!value.get("TotalTrxALIPAYOFFL").equals("0") || !value.get("TotalRefundTrxALIPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxALIPAYOFFL").equals("0") || !value.get("TotalVoidTrxALIPAYOFFL").equals("0")) {
                printTextCenter(label.get("ALIPAYOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYOFFL"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxALIPAYOFFL"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntALIPAYOFFL"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxALIPAYOFFL"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntALIPAYOFFL"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxALIPAYOFFL"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntALIPAYOFFL"));
                printDashLine();
            }

            // BOOSTOFFL
            if (!value.get("TotalTrxBOOSTOFFL").equals("0") || !value.get("TotalRefundTrxBOOSTOFFL").equals("0") || !value.get("TotalRequestRefundTrxBOOSTOFFL").equals("0") || !value.get("TotalVoidTrxBOOSTOFFL").equals("0")) {
                printTextCenter(label.get("BOOSTOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxBOOSTOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntBOOSTOFFL"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxBOOSTOFFL"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntBOOSTOFFL"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxBOOSTOFFL"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntBOOSTOFFL"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxBOOSTOFFL"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntBOOSTOFFL"));
                printDashLine();
            }

            // GCASHOFFL
            if (!value.get("TotalTrxGCASHOFFL").equals("0") || !value.get("TotalRefundTrxGCASHOFFL").equals("0") || !value.get("TotalRequestRefundTrxGCASHOFFL").equals("0") || !value.get("TotalVoidTrxGCASHOFFL").equals("0")) {
                printTextCenter(label.get("GCASHOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxGCASHOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGCASHOFFL"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxGCASHOFFL"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntGCASHOFFL"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxGCASHOFFL"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntGCASHOFFL"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxGCASHOFFL"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntGCASHOFFL"));
                printDashLine();
            }

            // GRABPAYOFFL
            if (!value.get("TotalTrxGRABPAYOFFL").equals("0") || !value.get("TotalRefundTrxGRABPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxGRABPAYOFFL").equals("0") || !value.get("TotalVoidTrxGRABPAYOFFL").equals("0")) {
                printTextCenter(label.get("GRABPAYOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxGRABPAYOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGRABPAYOFFL"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxGRABPAYOFFL"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntGRABPAYOFFL"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxGRABPAYOFFL"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntGRABPAYOFFL"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxGRABPAYOFFL"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntGRABPAYOFFL"));
                printDashLine();
            }

            // MASTER
            if (!value.get("TotalTrxMASTER").equals("0")) {
                printTextCenter(label.get("MASTER"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxMASTER"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntMASTER"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxMASTER"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntMASTER"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxMASTER"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntMASTER"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxMASTER"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntMASTER"));
                printDashLine();
            }

            // OEPAYOFFL
            if (!value.get("TotalTrxOEPAYOFFL").equals("0") || !value.get("TotalRefundTrxOEPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxOEPAYOFFL").equals("0") || !value.get("TotalVoidTrxOEPAYOFFL").equals("0")) {
                printTextCenter(label.get("OEPAYOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxOEPAYOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntOEPAYOFFL"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxOEPAYOFFL"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntOEPAYOFFL"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxOEPAYOFFL"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntOEPAYOFFL"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxOEPAYOFFL"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntOEPAYOFFL"));
                printDashLine();
            }

            // PROMPTPAYOFFL
            if (!value.get("TotalTrxPROMPTPAYOFFL").equals("0") || !value.get("TotalRefundTrxPROMPTPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxPROMPTPAYOFFL").equals("0") || !value.get("TotalVoidTrxPROMPTPAYOFFL").equals("0")) {
                printTextCenter(label.get("PROMPTPAYOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxPROMPTPAYOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntPROMPTPAYOFFL"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxPROMPTPAYOFFL"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntPROMPTPAYOFFL"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxPROMPTPAYOFFL"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntPROMPTPAYOFFL"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxPROMPTPAYOFFL"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntPROMPTPAYOFFL"));
                printDashLine();
            }

            // UNIONPAYOFFL
            if (!value.get("TotalTrxUNIONPAYOFFL").equals("0") || !value.get("TotalRefundTrxUNIONPAYOFFL").equals("0") || !value.get("TotalRequestRefundTrxUNIONPAYOFFL").equals("0") || !value.get("TotalVoidTrxUNIONPAYOFFL").equals("0")) {
                printTextCenter(label.get("UNIONPAYOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxUNIONPAYOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntUNIONPAYOFFL"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxUNIONPAYOFFL"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntUNIONPAYOFFL"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxUNIONPAYOFFL"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntUNIONPAYOFFL"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxUNIONPAYOFFL"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntUNIONPAYOFFL"));
                printDashLine();
            }

            // VISA
            if (!value.get("TotalTrxVISA").equals("0") || !value.get("TotalRefundTrxVISA").equals("0") || !value.get("TotalRequestRefundTrxVISA").equals("0") || !value.get("TotalVoidTrxVISA").equals("0")) {
                printTextCenter(label.get("VISA"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxVISA"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntVISA"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxVISA"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntVISA"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxVISA"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntVISA"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxVISA"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntVISA"));
                printDashLine();
            }

            // WECHATOFFL
            if (!value.get("TotalTrxWECHATOFFL").equals("0") || !value.get("TotalRefundTrxWECHATOFFL").equals("0") || !value.get("TotalRequestRefundTrxWECHATOFFL").equals("0") || !value.get("TotalVoidTrxWECHATOFFL").equals("0")) {
                printTextCenter(label.get("WECHATOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATOFFL"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxWECHATOFFL"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntWECHATOFFL"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxWECHATOFFL"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntWECHATOFFL"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxWECHATOFFL"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntWECHATOFFL"));
                printDashLine();
            }

            // WECHATHKOFFL
            if (!value.get("TotalTrxWECHATHKOFFL").equals("0") || !value.get("TotalRefundTrxWECHATHKOFFL").equals("0") || !value.get("TotalRequestRefundTrxWECHATHKOFFL").equals("0") || !value.get("TotalVoidTrxWECHATHKOFFL").equals("0")) {
                printTextCenter(label.get("WECHATHKOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATHKOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATHKOFFL"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxWECHATHKOFFL"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntWECHATHKOFFL"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxWECHATHKOFFL"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntWECHATHKOFFL"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxWECHATHKOFFL"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntWECHATHKOFFL"));
                printDashLine();
            }

            // WECHATONL
            if (!value.get("TotalTrxWECHATONL").equals("0") || !value.get("TotalRefundTrxWECHATONL").equals("0") || !value.get("TotalRequestRefundTrxWECHATONL").equals("0") || !value.get("TotalVoidTrxWECHATONL").equals("0")) {
                printTextCenter(label.get("WECHATONL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATONL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATONL"));
                printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxWECHATONL"));
                printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntWECHATONL"));
                printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxWECHATONL"));
                printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntWECHATONL"));
                printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxWECHATONL"));
                printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntWECHATONL"));
                printDashLine();
            }

            printTextCenter("===" + label.get("Footer") + "===");
            printLine(5);
            printDev.printText(textList, callback);

        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,"Failed to print Summary Report");
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
            Log.d(TAG,"Start Transaction Report Printing");

            lang = getLanguage(context);

            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            printHeader(label.get("Title"));

            printTextCenter(value.get("MerName"));

            printTwoColumn(label.get("FromDateLabel"), value.get("From_date"));
            printTwoColumn(label.get("ToDateLabel"), value.get("To_date"));

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
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYCNOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYCNOFFL"));
                printDashLine();

                printQrContent(label, ALIPAYCNOFFLRecordsArray);
            }

            // ALIPAYHKOFFL
            if (!value.get("TotalTrxALIPAYHKOFFL").equals("0")) {
                printTextCenter(label.get("ALIPAYHKOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYHKOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYHKOFFL"));
                printDashLine();

                printQrContent(label, ALIPAYHKOFFLRecordsArray);
            }

            // ALIPAYOFFL
            if (!value.get("TotalTrxALIPAYOFFL").equals("0")) {
                printTextCenter(label.get("ALIPAYOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYOFFL"));
                printDashLine();

                printQrContent(label, ALIPAYOFFLRecordsArray);
            }

            // BOOSTOFFL
            if (!value.get("TotalTrxBOOSTOFFL").equals("0")) {
                printTextCenter(label.get("BOOSTOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxBOOSTOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntBOOSTOFFL"));
                printDashLine();

                printQrContent(label, BOOSTOFFLRecordsArray);
            }

            // GCASHOFFL
            if (!value.get("TotalTrxGCASHOFFL").equals("0")) {
                printTextCenter(label.get("GCASHOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxGCASHOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGCASHOFFL"));
                printDashLine();

                printQrContent(label, GCASHOFFLRecordsArray);
            }

            // GRABPAYOFFL
            if (!value.get("TotalTrxGRABPAYOFFL").equals("0")) {
                printTextCenter(label.get("GRABPAYOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxGRABPAYOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGRABPAYOFFL"));
                printDashLine();

                printQrContent(label, GRABPAYOFFLRecordsArray);
            }

            // MASTER
            if (!value.get("TotalTrxMASTER").equals("0")) {
                printTextCenter(label.get("MASTER"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxMASTER"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntMASTER"));
                printDashLine();

                printCardContent(label, masterRecordsArray);
            }

            // OEPAYOFFL
            if (!value.get("TotalTrxOEPAYOFFL").equals("0")) {
                printTextCenter(label.get("OEPAYOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxOEPAYOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntOEPAYOFFL"));
                printDashLine();

                printQrContent(label, OEPAYOFFLRecordsArray);
            }

            // PROMPTPAYOFFL
            if (!value.get("TotalTrxPROMPTPAYOFFL").equals("0")) {
                printTextCenter(label.get("PROMPTPAYOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxPROMPTPAYOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntPROMPTPAYOFFL"));
                printDashLine();

                printQrContent(label, PROMPTPAYOFFLRecordsArray);
            }

            // UNIONPAYOFFL
            if (!value.get("TotalTrxUNIONPAYOFFL").equals("0")) {
                printTextCenter(label.get("UNIONPAYOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxUNIONPAYOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntUNIONPAYOFFL"));
                printDashLine();

                printCardContent(label, UNIONPAYOFFLRecordsArray);
            }

            // VISA
            if (!value.get("TotalTrxVISA").equals("0")) {
                printTextCenter(label.get("VISA"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxVISA"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntVISA"));
                printDashLine();

                printCardContent(label, visaRecordsArray);
            }


            // WECHATOFFL
            if (!value.get("TotalTrxWECHATOFFL").equals("0")) {
                printTextCenter(label.get("WECHATOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATOFFL"));
                printDashLine();

                printQrContent(label, WECHATOFFLRecordsArray);
            }

            // WECHATHKOFFL
            if (!value.get("TotalTrxWECHATHKOFFL").equals("0")) {
                printTextCenter(label.get("WECHATHKOFFL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATHKOFFL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATHKOFFL"));
                printDashLine();

                printQrContent(label, WECHATHKOFFLRecordsArray);
            }

            // WECHATONL
            if (!value.get("TotalTrxWECHATONL").equals("0")) {
                printTextCenter(label.get("WECHATONL"));
                printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATONL"));
                printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATONL"));
                printDashLine();

                printQrContent(label, WECHATONLRecordsArray);
            }

            printTextCenter("===" + label.get("Footer") + "===");
            printLine(3);

            printDev.printText(textList, callback);

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
                printTwoColumn(label.get("TransactionDate"), sbdate.toString() + " " + sbtime.toString());
                printTwoColumn(label.get("Amount"), currency + " " + amount);
                printTwoColumn(label.get("MerRef"), merRef);
                printTwoColumn(label.get("PaymentRef"), paymentRef);
                printTwoColumn(label.get("QRNumber"), qr);
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
                printTwoColumn(label.get("TransactionDate"), sbdate.toString() + " " + sbtime.toString());
                printTwoColumn(label.get("Amount"), currency + " " + amount);
                printTwoColumn(label.get("MerRef"), merRef);
                printTwoColumn(label.get("PaymentRef"), paymentRef);
                printTwoColumn(label.get("CardNo"), cardNo);
                printDashLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private void printText(String text){

        try {
            textList.add(new PrintDataObject(text, 16, false, PrintDataObject.ALIGN.LEFT, false, true, 35));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printTextWithSpacing(String text, int spacing){

        try {
            textList.add(new PrintDataObject(text, 16, false, PrintDataObject.ALIGN.LEFT, false, true, spacing));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printTextCenter(String text){

        try {
            textList.add(new PrintDataObject(text, 16, false, PrintDataObject.ALIGN.CENTER, false, true, 35));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printTextCenterWithSpacing(String text, int spacing){

        try {
            textList.add(new PrintDataObject(text, 16, false, PrintDataObject.ALIGN.CENTER, false, true, spacing));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printHeader(String text){

        try {
            textList.add(new PrintDataObject(text, 20, true, PrintDataObject.ALIGN.CENTER, false, true, 35));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printBitmap(Bitmap bitmap, int align){
        try {
            printDev.printBmpFastSync(bitmap, align);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void printTwoColumn(String title, String content) throws IOException {

        String titleFormat = "";

        if(title.isEmpty()){
            titleFormat = "%s";
        }else{
            titleFormat = "%-" + title.length() + "s";
        }

        String contentFormat = "";

        if (lang.equalsIgnoreCase(Constants.LANG_ENG)) {
            contentFormat = "%" + (32 - title.length()) + "s";
        } else if(lang.equalsIgnoreCase(Constants.LANG_TRAD) || lang.equalsIgnoreCase(Constants.LANG_SIMP) ){
            contentFormat = "%" + (24 - title.length()) + "s";
        }

        // compose the complete format information
        String columnFormat = titleFormat + "" + contentFormat;

        printText(String.format(columnFormat, title, content));
    }

    private void printTwoColumnWithSpacing(String title, String content, int spacing) throws IOException {

        String titleFormat = "";

        if(title.isEmpty()){
            titleFormat = "%s";
        }else{
            titleFormat = "%-" + title.length() + "s";
        }

        String contentFormat = "";

        if (lang.equalsIgnoreCase(Constants.LANG_ENG)) {
            contentFormat = "%" + (32 - title.length()) + "s";
        } else if(lang.equalsIgnoreCase(Constants.LANG_TRAD) || lang.equalsIgnoreCase(Constants.LANG_SIMP) ){
            contentFormat = "%" + (24 - title.length()) + "s";
        }

        // compose the complete format information
        String columnFormat = titleFormat + "" + contentFormat;

        printTextWithSpacing(String.format(columnFormat, title, content), spacing);
    }

    private void printDashLine() throws IOException {
        printText("--------------------------------");
    }

    private void printLine(int line) throws RemoteException {

        int i = 0;

        try {
            for(i = 0; i < line; i++) {
                textList.add(new PrintDataObject("\n", 16, false, PrintDataObject.ALIGN.LEFT, false, true, 25));
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

    // Find language by sharePreferences
    public String getLanguage(Context context) {
        SharedPreferences pref_lang = context.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String lang = pref_lang.getString(Constants.pref_Lang, "");
        return lang;
    }


}
