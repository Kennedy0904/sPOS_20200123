package com.example.dell.smartpos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.smartpos.Printer.PAX.GetObj;
import com.example.dell.smartpos.Printer.PrintUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class CardPayment_Success extends AppCompatActivity {

    private Handler threadHandler = null;
    private Runnable threadRunObj = null;

    //Print Setup
    String printeraddress = "";
    String printername = "";
    String printmode;
    PrintUtil printUtil = null;
    GetObj getObj = null;

    String merID;
    String merName;
    String successCode;
    String merRef;
    String payRef;
    String traceNo;
    String amount;
    String merRequestAmt;
    String surcharge;
    String currcode;
    String trxTime;
    String errMsg;
    String payType;
    String pMethod;
    String operatorId;
    String cardNo;
    String cardHolder;
    String epMonth;
    String epYear;
    String status;
    String hideSurcharge;
    String AID;
    String AppName;
    String entryMode;
    String batchNo;
    String terminalID;
    String CVMResult;
    String invoiceNo;
    String payBankId;
    String showPayMethod;

    private String fileName;
    private Bitmap signatureBm;

    private LinearLayout surchargeLayout;
    private LinearLayout invoiceNoLayout;
    private LinearLayout terminalIdLayout;

    private Button btnPrint;
    private Button btnOk;

    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;
    private TextView tvMerName;
    private TextView tvMerRef;
    private TextView tvPayRef;
    private TextView tvTraceNo;
    private TextView tvAmount;
    private TextView tvSurcharge;
    private TextView tvTrxTime;
    private TextView tvPayMethod;
    private TextView tvCurrcode;
    private TextView tvSurCurrcode;
    private TextView tvErrMsg;
    private TextView tvStatus;
    private TextView tvCardNo;
    private TextView tvBatchNo;
    private TextView tvTerminalID;
    private TextView tvInvoiceNo;

    Map<String, String> label;
    Map<String, String> value;
    String partnerlogo = "";
    Bitmap initbitmap;

    //label value
    String label_terminalID = "";
    String label_merID = "";
    String label_trxType = "";
    String label_payMethod = "";
    String label_cardNo = "";
    String label_epDate = "";
    String label_cardHolder = "";
    String label_merRef = "";
    String label_payRef = "";
    String label_batchNo = "";
    String label_traceNo = "";
    String label_trxTime = "";
    String label_RRN = "";
    String label_approveCode = "";
    String label_appName = "";
    String label_AID = "";
    String label_TC = "";
    String label_trxID = "";
    String label_amount = "";
    String label_version = "";

    String value_title = "";
    String value_merName = "";
    String value_merAddress = "";
    String value_terminalID = "";
    String value_merID = "";
    String value_payType = "";
    String value_trxType = "";
    String value_payMethod = "";
    String value_cardNo = "";
    String value_entryMode = "";
    String value_epDate = "";
    String value_cardHolder = "";
    String value_merRef = "";
    String value_payRef = "";
    String value_batchNo = "";
    String value_traceNo = "";
    String value_trxTime = "";
    String value_RRN = "";
    String value_approveCode = "";
    String value_appName = AppName;
    String value_AID = AID;
    String value_TC = "";
    String value_trxID = "";
    String value_amount = "";
    String value_currCode = "";
    String value_CVMResult = "";
    String value_copy = "";
    String value_version = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment__success);
        setTitle(R.string.card_payment);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        step1 = (TextView) findViewById(R.id.progress_card_text_1);
        step2 = (TextView) findViewById(R.id.progress_card_text_2);
        step3 = (TextView) findViewById(R.id.progress_card_text_3);
        step4 = (TextView) findViewById(R.id.progress_card_text_4);

        step2.setTypeface(null, Typeface.BOLD);
        step3.setTypeface(null, Typeface.BOLD);
        step4.setTypeface(null, Typeface.BOLD);

        //---------for print func---------//
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        printeraddress = prefsettings.getString(Constants.pref_printer_address, "");
        printername = prefsettings.getString(Constants.pref_printer_name, "");
        if ("".equals(printeraddress) || printeraddress == null) {
            printeraddress = "";
        }
        if ("".equals(printername) || printername == null) {
            printername = "";
        }
        Log.d("OTTO", "device SETTEXT>>" + printername + ":" + printeraddress);
        printmode = prefsettings.getString(Constants.pref_PrintMode, Constants.default_printmode);

        //Initialize PAX Printer
        if (GlobalFunction.getDeviceMan().equalsIgnoreCase("PAX")) {
            getObj = new GetObj(getApplicationContext());
        }
        //---------for print func---------//

        Intent intent = getIntent();

        merID = intent.getStringExtra(Constants.MERID) == null ? "" : intent.getStringExtra(Constants.MERID);
        merName = intent.getStringExtra(Constants.MERNAME) == null ? "" : intent.getStringExtra(Constants.MERNAME);
        successCode = intent.getStringExtra(Constants.SUCCESS_CODE) == null ? "" : intent.getStringExtra(Constants.SUCCESS_CODE);
        merRef = intent.getStringExtra(Constants.MERCHANT_REF) == null ? "" : intent.getStringExtra(Constants.MERCHANT_REF);
        payRef = intent.getStringExtra(Constants.PAYREF) == null ? "" : intent.getStringExtra(Constants.PAYREF);
        traceNo = intent.getStringExtra(Constants.TRACE_NO) == null ? "" : intent.getStringExtra(Constants.TRACE_NO);
        amount = intent.getStringExtra(Constants.AMOUNT) == null ? "" : intent.getStringExtra(Constants.AMOUNT);
        merRequestAmt = intent.getStringExtra(Constants.MERREQUESTAMT) == null ? "" : intent.getStringExtra(Constants.MERREQUESTAMT);
        surcharge = intent.getStringExtra(Constants.SURCHARGE) == null ? "" : intent.getStringExtra(Constants.SURCHARGE);
        currcode = intent.getStringExtra(Constants.CURRCODE) == null ? "" : intent.getStringExtra(Constants.CURRCODE);
        trxTime = intent.getStringExtra(Constants.TXTIME) == null ? "" : intent.getStringExtra(Constants.TXTIME);
        errMsg = intent.getStringExtra(Constants.ERRMSG) == null ? "" : intent.getStringExtra(Constants.ERRMSG);
        payBankId = intent.getStringExtra(Constants.PAYBANKID) == null ? "" : intent.getStringExtra(Constants.PAYBANKID);
        payType = intent.getStringExtra(Constants.PAYTYPE) == null ? "" : intent.getStringExtra(Constants.PAYTYPE);
        pMethod = intent.getStringExtra(Constants.PAYMETHOD) == null ? "" : intent.getStringExtra(Constants.PAYMETHOD);
        operatorId = intent.getStringExtra(Constants.OPERATORID) == null ? "" : intent.getStringExtra(Constants.OPERATORID);
        cardNo = intent.getStringExtra(Constants.CARDNO) == null ? "" : intent.getStringExtra(Constants.CARDNO);
        //cardNo = cardNo.substring(0, 6) + "********" + cardNo.substring(14);
        Log.d("OTTO", "cardNo:" + cardNo);
        cardHolder = intent.getStringExtra(Constants.CARDHOLDER) == null ? "" : intent.getStringExtra(Constants.CARDHOLDER);
        epMonth = intent.getStringExtra(Constants.EXPMONTH) == null ? "" : intent.getStringExtra(Constants.EXPMONTH);
        epYear = intent.getStringExtra(Constants.EXPYEAR) == null ? "" : intent.getStringExtra(Constants.EXPYEAR);
        hideSurcharge = intent.getStringExtra(Constants.pref_hideSurcharge) == null ? "F" : intent.getStringExtra(Constants.pref_hideSurcharge);
        AID = intent.getStringExtra(Constants.AID) == null ? "" : intent.getStringExtra(Constants.AID);
        AppName = intent.getStringExtra(Constants.AppName) == null ? "" : intent.getStringExtra(Constants.AppName);
        entryMode = intent.getStringExtra(Constants.EntryMode) == null ? "" : intent.getStringExtra(Constants.EntryMode);
        Log.d("entryMode", entryMode +"123");
        batchNo = intent.getStringExtra(Constants.BATCH_NO) == null ? "" : intent.getStringExtra(Constants.BATCH_NO);
        terminalID = intent.getStringExtra(Constants.TERMINAL_ID) == null ? "" : intent.getStringExtra(Constants.TERMINAL_ID);
        CVMResult = intent.getStringExtra(Constants.CVMResult) == null ? "" : intent.getStringExtra(Constants.CVMResult);
        invoiceNo = intent.getStringExtra(Constants.INVOICE_NO) == null ? "" : intent.getStringExtra(Constants.INVOICE_NO);

        surchargeLayout = (LinearLayout) findViewById(R.id.cardSurchargeRow);
        invoiceNoLayout = (LinearLayout) findViewById(R.id.invoiceNoLayout);
        terminalIdLayout = (LinearLayout) findViewById(R.id.terminalIdLayout);

        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnOk = (Button) findViewById(R.id.btnOk);

        tvMerName = (TextView) findViewById(R.id.cardMerName);
        tvMerRef = (TextView) findViewById(R.id.cardMerRef);
        tvPayRef = (TextView) findViewById(R.id.cardPayRef);
        tvTraceNo = (TextView) findViewById(R.id.cardTraceNo);
        tvAmount = (TextView) findViewById(R.id.cardAmount);
        tvSurcharge = (TextView) findViewById(R.id.cardSurcharge);
        tvPayMethod = (TextView) findViewById(R.id.cardPayMethod);
        tvCurrcode = (TextView) findViewById(R.id.cardCurrcode);
        tvSurCurrcode = (TextView) findViewById(R.id.cardSurCurrcode);
        tvTrxTime = (TextView) findViewById(R.id.cardTrxTime);
        tvStatus = (TextView) findViewById(R.id.cardPaymentStatus);
        tvCardNo = (TextView) findViewById(R.id.cardNo);
        tvErrMsg = (TextView) findViewById(R.id.cardErrMsg);
        tvBatchNo = (TextView) findViewById(R.id.cardBatchNo);
        tvTerminalID = (TextView) findViewById(R.id.cardTerminalID);
        tvInvoiceNo = (TextView) findViewById(R.id.cardInvoiceNo);

        String mdr = prefsettings.getString(Constants.mer_mdr, "");
        surcharge = surcharge == null ? "0" : surcharge;
//        if(mdr==null||"".equals(mdr)||("T".equals(hideSurcharge))){
        if (mdr == null || "".equals(mdr)) {
            surchargeLayout.setVisibility(GONE);
        } else if (Double.valueOf(mdr) <= 0) {
            surchargeLayout.setVisibility(GONE);
        } else if (Double.valueOf(mdr) > 0 && Double.valueOf(surcharge) > 0) {
            surchargeLayout.setVisibility(VISIBLE);
            tvSurCurrcode.setText(currcode);
            tvSurcharge.setText(surcharge);
        } else {
            surchargeLayout.setVisibility(GONE);
        }

        tvMerName.setText(merName);
        tvMerRef.setText(merRef);
        tvPayRef.setText(payRef);
        tvTraceNo.setText(traceNo);

        if ("T".equals(hideSurcharge)) {
            tvAmount.setText(merRequestAmt);
        } else {
            tvAmount.setText(amount);
        }
        tvCurrcode.setText(currcode);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        Date transactionDate = null;
        try {
            transactionDate = dateFormat1.parse(trxTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        trxTime = dateFormat2.format(transactionDate);
        tvTrxTime.setText(trxTime);
        tvErrMsg.setText(errMsg);
        tvCardNo.setText(cardNo);
        tvStatus.setText(getString(R.string.accepted));
        tvBatchNo.setText(batchNo);
        tvTerminalID.setText(terminalID);

        if (pMethod.equalsIgnoreCase("Master")) {
            showPayMethod = getString(R.string.MasterCard);
        } else if (pMethod.equalsIgnoreCase("Visa")) {
            showPayMethod = getString(R.string.visa);
        }
        tvPayMethod.setText(showPayMethod);


        /** Control the Invoice No Layout **/
        if (payBankId.equalsIgnoreCase(Constants.FIRST_DATA)) {
            invoiceNoLayout.setVisibility(VISIBLE);
            tvInvoiceNo.setText(invoiceNo);
        }

        /** Control the Terminal ID Layout **/
        terminalIdLayout.setVisibility(GONE);

        ImageView jpgView = (ImageView)findViewById(R.id.imgsig);
        String cachepath = getBaseContext().getCacheDir().toString();
        if (savedInstanceState != null){
            signatureBm = (Bitmap) savedInstanceState.getParcelable("bitmap");
        } else {
            fileName = intent.getStringExtra(Constants.FILE_NAME);
            String myJpgPath = cachepath + "/"+fileName;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            signatureBm = BitmapFactory.decodeFile(myJpgPath, options);
        }
        jpgView.setImageBitmap(signatureBm);

        if (payBankId.equalsIgnoreCase(Constants.FIRST_DATA)) {
            // Skip auto merchant printing for 'First-Data' bankId
            // Set print button visibility to gone
            btnPrint.setVisibility(GONE);
            threadRunObj = new Runnable() {
                public void run() {
                    returnMainMenu();
                }
            };
            threadHandler = new Handler();
            threadHandler.postDelayed(threadRunObj, 5000);

        } else if (GlobalFunction.getLanguage(CardPayment_Success.this).equalsIgnoreCase("Thai")) {
            btnPrint.setEnabled(false);
        } else {
            if (printmode.equals(Constants.PRINT_MODE1) || printmode.equals(Constants.PRINT_MODE3)) {
                btnPrint.setEnabled(false);
                printCardReceipt("Merchant");

                if (printmode.equals(Constants.PRINT_MODE1)) {

                    threadRunObj = new Runnable() {
                        public void run() {
                            printCardReceipt("Consumer");
                        }
                    };
                    threadHandler = new Handler();
                    threadHandler.postDelayed(threadRunObj, 10000);

                    threadRunObj = new Runnable() {
                        public void run() {
                            returnMainMenu();
                        }
                    };
                    threadHandler = new Handler();
                    threadHandler.postDelayed(threadRunObj, 15000);

                } else {
                    threadRunObj = new Runnable() {
                        public void run() {
                            returnMainMenu();
                        }
                    };
                    threadHandler = new Handler();
                    threadHandler.postDelayed(threadRunObj, 5000);

                }
            } else {
                printCardReceipt("Merchant");
                btnPrint.setEnabled(false);

                threadRunObj = new Runnable() {
                    public void run() {
                        Button print = (Button) findViewById(R.id.btnPrint);
                        print.setEnabled(true);
                    }
                };
                threadHandler = new Handler();
                threadHandler.postDelayed(threadRunObj, 5000);

                threadRunObj = new Runnable() {
                    public void run() {
                        returnMainMenu();
                    }
                };
                threadHandler = new Handler();
                threadHandler.postDelayed(threadRunObj, 15000);
            }
        }

        /** OK Button **/
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kill the thread before back to MainMenu
                if (threadHandler != null) {
                    threadHandler.removeCallbacks(threadRunObj);
                    returnMainMenu();
                } else {
                    returnMainMenu();
                }
            }
        });

        /** Print Receipt Button **/
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printCardReceipt("Consumer");
            }
        });
    }

    private void printCardReceipt(String copy){
        // Merchant Logo
        Bitmap initbitmap = GlobalFunction.getMerLogo(CardPayment_Success.this);

        if (payType.equalsIgnoreCase("N")) {
            payType = getString(R.string.sale);
        }else if(payType.equalsIgnoreCase("H")) {
            payType = getString(R.string.authorize);
        }

        if (copy.equals("Consumer")) {
            copy = getString(R.string.print_customerCopy);
        } else {
            copy = getString(R.string.print_merchantCopy);
        }

        label_terminalID = getString(R.string.print_terminalID);
        label_merID = getString(R.string.print_merID);
        label_trxType = getString(R.string.print_TrxType);
        label_payMethod = getString(R.string.print_paymethod);
        label_cardNo = getString(R.string.print_cardno);
        label_epDate = getString(R.string.print_epDate);
        label_cardHolder = getString(R.string.print_cardHolder);
        label_merRef = getString(R.string.merchant_ref_label);
        label_batchNo = getString(R.string.print_batchNo);
        label_traceNo = getString(R.string.print_traceNo);
        label_trxTime = getString(R.string.print_transactionDate);
        label_RRN = getString(R.string.print_RRN);
        label_approveCode = getString(R.string.print_approveCode);
        label_appName = getString(R.string.print_appName);
        label_AID = getString(R.string.print_AID);
        label_TC = getString(R.string.print_TC);
        label_trxID = getString(R.string.print_TrxID);
        label_amount = getString(R.string.print_amount);
        label_version = getString(R.string.print_version);

        label = new TreeMap<String, String>();
        label.put("TerminalID", label_terminalID);
        label.put("MerID", label_merID);
        label.put("PayMethod", label_payMethod);
        label.put("TrxType", label_trxType);
        label.put("CardNo", label_cardNo);
        label.put("epDate", label_epDate);
        label.put("CardHolder", label_cardHolder);
        label.put("MerRef", label_merRef);
        label.put("BatchNo", label_batchNo);
        label.put("TraceNo", label_traceNo);
        label.put("TrxTime", label_trxTime);
        label.put("RRN", label_RRN);
        label.put("ApproveCode", label_approveCode);
        label.put("AppName", label_appName);
        label.put("AID", label_AID);
        label.put("TC", label_TC);
        label.put("TrxID", label_trxID);
        label.put("TotalAmnt", label_amount);
        label.put("Version", label_version);

        value_title =  getString(R.string.print_receipt);
        value_merName = merName;
        value_merAddress = "";
        value_terminalID = terminalID;
        value_merID = merID;
        value_payMethod = pMethod;
        value_payType = payType;
        value_entryMode = entryMode;
        value_trxType = payType;
        value_cardNo = cardNo;
        value_epDate = epMonth + "/" + epYear;
        value_cardHolder = cardHolder;
        value_merRef = merRef;
        value_batchNo = batchNo;
        value_traceNo = traceNo;
        value_trxTime = trxTime;
        value_RRN = "";
        value_approveCode = "";
        value_appName = AppName;
        value_AID = AID;
        value_TC = "";
        value_trxID = payRef;
        value_amount = amount;
        value_currCode = currcode;
        value_CVMResult = CVMResult;
        value_copy = copy;
//        value_version = BuildConfig.VERSION_NAME;
        value_version = Constants.current_VersionCode;

        value = new TreeMap<String, String>();
        value.put("Title", value_title);
        value.put("MerName", value_merName);
        value.put("MerAddress", value_merAddress);
        value.put("TerminalID", value_terminalID);
        value.put("MerID", value_merID);
        value.put("PayMethod", value_payMethod);
        value.put("EntryMode", value_entryMode);
        value.put("TrxType", value_trxType);
        value.put("CardNo", value_cardNo);
        value.put("epDate", value_epDate);
        value.put("CardHolder", value_cardHolder);
        value.put("MerRef", value_merRef);
        value.put("BatchNo", value_batchNo);
        value.put("TraceNo", value_traceNo);
        value.put("TrxTime", value_trxTime);
        value.put("RRN", value_RRN);
        value.put("ApproveCode", value_approveCode);
        value.put("AppName", value_appName);
        value.put("AID", value_AID);
        value.put("TC", value_TC);
        value.put("TrxID", value_trxID);
        value.put("TotalAmnt", value_amount);
        value.put("currCode", value_currCode);
        value.put("CVMResult", value_CVMResult);
        value.put("Footer", value_copy);
        value.put("Version", value_version);

        printUtil = new PrintUtil(CardPayment_Success.this, printeraddress, printername, label, value, initbitmap, signatureBm);
        printUtil.sendCardPaymentReceipt();

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        Intent intent = new Intent(CardPayment_Success.this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    public void returnMainMenu() {

        Intent intentNew = new Intent();
        intentNew.setClass(CardPayment_Success.this, MainActivity.class);
        intentNew.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentNew);
        setResult(RESULT_OK);
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();

        // Kill the thread before back to MainMenu
        if (threadHandler != null) {
            threadHandler.removeCallbacks(threadRunObj);
            returnMainMenu();
        } else {
            returnMainMenu();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (GlobalFunction.onKeyDownEvent(CardPayment_Success.this, keyCode, event, getPackageManager())) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
