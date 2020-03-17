package com.example.dell.smartpos;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.smartpos.Printer.PAX.GetObj;
import com.example.dell.smartpos.Printer.PrintUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ScanQR_Success extends AppCompatActivity {

    private Handler threadHandler = null;
    private Runnable threadRunObj = null;

    public String merchantId;
    public String merName;
    public String success_code;
    public String merchant_ref_no;
    public String payRef;
    public String amount;
    public String merrequestamt;
    public String Surcharge;
    public String Currcode;
    public String TXTime;
    public String errMsg;
    public String payType;
    public String pMethod;
    public String operatorId;
    public String cardNo;
    public String PayStatus;
    public String hideSurcharge;
    public String showPayMethod;
    public String printText;
    public String paybankId;
    public String batchNo;
    public String STAN;
    public String host;
    public String tid;
    public String mid;
    public String partnerTrxId;
    public String approvalCode;

    public TextView showMerchantName;
    public TextView showPayRef;
    public TextView showQRPayment;
    public TextView showAmount;
    public LinearLayout surchargeTableRow;
    public TextView surcharge_title;
    public TextView showSurcharge;
    public TextView showCurrcode;
    public TextView showMerchantRef;
    public TextView showTXTime;
    public TextView showErrMsg;
    public TextView showStatus;
    public TextView showQRNumber;

    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;

    String printmode;
    String printeraddress = "";
    String printername = "";
    Map<String, String> info = null;
    Map<String, String> product = null;
    PrintUtil printUtil = null;
    GetObj getObj = null;

    public static String customerCopy = "";
    public static String merchantCopy = "";

    String partnerlogo = "";

    String language = "";
    String copy = "";

    //info product
    String info_merchantName = "";
    String info_payType = "";
    String info_operator = "";
    String info_title = "";
    String info_totalamount = "";
    String info_surcharge = "";
    String info_merRequestAmt = "";
    String info_paymethod = "";
    String info_PayStatus = "";
    String info_cardno = "";
    String info_merchantRef = "";
    String info_paymentRef = "";
    String info_transactionDate = "";
    String info_note = "";
    String info_isCN = "";
    String info_copy = "";

    String product_OperatorNumber = "";
    String product_payType = "";
    String product_CurrCode = "";
    String product_Amount = "";
    String product_Surcharge = "";
    String product_merRequestAmt = "";
    String product_PayMethod = "";
    String product_PayStatus = "";
    String product_CardNo = "";
    String product_merchantRef = "";
    String product_paymentRef = "";
    String product_transactionDate = "";

    /**** for ALIPAYTHOFFL ****/
    String info_tid = "";
    String info_merid = "";
    String info_batchno = "";
    String info_traceno = "";
    String info_host = "";
    String info_stan = "";
    String info_walletcard = "";
    String info_apprcode = "";
    String info_refNo = "";
    String info_walletSale = "";
    String info_amount = "";
    String info_datetime = "";

    String product_tid = "";
    String product_merid = "";
    String product_batchno = "";
    String product_traceno = "";
    String product_host = "";
    String product_stan = "";
    String product_walletcard = "";
    String product_apprcode = "";
    String product_refNo = "";
    String product_amount = "";
    String product_time = "";

    Date transactionDate = null;

    //---------for autologout---------//
    Handler CheckTimeOutHandler = new Handler();
    Date lastUpdateTime;//lastest operation time
    long timePeriod;//no operation time
    float SESSION_EXPIRED = Constants.SESSION_EXPIRY / 1000;//session expired time
    boolean CheckLogout = false;//logout flag
    long CheckInterval = 1000;//checking time intercal
    //---------for autologout---------//

    AlertDialog.Builder dialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle(R.string.scan_qr);
        setContentView(R.layout.activity_success_scan);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        step1 = (TextView) findViewById(R.id.progress_step_1);
        step2 = (TextView) findViewById(R.id.progress_step_2);
        step3 = (TextView) findViewById(R.id.progress_step_3);
        step4 = (TextView) findViewById(R.id.progress_step_4);

        step1.setText(R.string.enter_amount);
        step2.setText(R.string.select_qr_payment);
        step3.setText(R.string.read_qr);
        step4.setText(R.string.payment_result);

        step2.setTypeface(null, Typeface.BOLD);
        step3.setTypeface(null, Typeface.BOLD);
        step4.setTypeface(null, Typeface.BOLD);


        Locale locale = getResources().getConfiguration().locale;
        language = locale.getLanguage();
        Log.d("OTTO", "current language:" + language);

        //initialize
        final String[] printCopy = {getResources().getString(R.string.print_customerCopy), getResources().getString(R.string.print_merchantCopy)};

        //---------for autologout---------//
        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout, false);
        lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
        Log.d("OTTO", "init ScanQR_Success CheckLogout:" + CheckLogout + ",lastUpdateTime:" + lastUpdateTime);
        //---------for autologout---------//

        //---------for print func---------//
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);

        printeraddress = prefsettings.getString(Constants.pref_printer_address, "");
        printername = prefsettings.getString(Constants.pref_printer_name, "");
        if ("".equals(printeraddress) || printeraddress == null) {
            printeraddress = "";
        }
        if ("".equals(printername) || printername == null) {
            printername = "";
        }
        Log.d("OTTO", "device SETTEXT>>" + printername + ":" + printeraddress);

        //Initialize PAX Printer
        getObj = new GetObj(getApplicationContext());
        //---------for print func---------//

        //--------set partnerlogo-----------//
        SharedPreferences partnerlogomerdetails = getApplicationContext().getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
        String rawPartnerlogo = partnerlogomerdetails.getString(Constants.pref_Partnerlogo, "");
        merName = partnerlogomerdetails.getString(Constants.MERNAME, "");
        try {
            DesEncrypter encrypter = new DesEncrypter(merName);
            partnerlogo = encrypter.decrypt(rawPartnerlogo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate, Constants.default_paygate);
        String prefpartnerlogopaygate = prefpaygate;
        String baseUrl = PayGate.getURL_partnerlogo(prefpartnerlogopaygate) + partnerlogo;
//        ImageView partnerlogoview = (ImageView)findViewById(R.id.partnerlogo);
        PartnerLogoUtil.setImageToImageView(null, baseUrl, partnerlogo);
        //--------set partnerlogo-----------//

        printmode = prefsettings.getString(Constants.pref_PrintMode, Constants.default_printmode);
        Log.d("printmode", printmode);

        final Intent intent = getIntent();

        merchantId = intent.getStringExtra(Constants.MERID) == null ? "" : intent.getStringExtra(Constants.MERID);
        merName = intent.getStringExtra(Constants.MERNAME) == null ? "" : intent.getStringExtra(Constants.MERNAME);

        success_code = intent.getStringExtra(Constants.SUCCESS_CODE) == null ? "" : intent.getStringExtra(Constants.SUCCESS_CODE);
        merchant_ref_no = intent.getStringExtra(Constants.MERCHANT_REF) == null ? "" : intent.getStringExtra(Constants.MERCHANT_REF);
        payRef = intent.getStringExtra(Constants.PAYREF) == null ? "" : intent.getStringExtra(Constants.PAYREF);
        amount = intent.getStringExtra(Constants.AMOUNT) == null ? "" : intent.getStringExtra(Constants.AMOUNT);
        merrequestamt = intent.getStringExtra(Constants.MERREQUESTAMT) == null ? "" : intent.getStringExtra(Constants.MERREQUESTAMT);
        Surcharge = intent.getStringExtra(Constants.SURCHARGE) == null ? "" : intent.getStringExtra(Constants.SURCHARGE);
        Currcode = intent.getStringExtra(Constants.CURRCODE) == null ? "" : intent.getStringExtra(Constants.CURRCODE);
        TXTime = intent.getStringExtra(Constants.TXTIME) == null ? "" : intent.getStringExtra(Constants.TXTIME);
        errMsg = intent.getStringExtra(Constants.ERRMSG) == null ? "" : intent.getStringExtra(Constants.ERRMSG);
        printText = intent.getStringExtra("printText") == null ? "" : intent.getStringExtra("printText");
        paybankId = intent.getStringExtra("payBankId") == null ? "" : intent.getStringExtra("payBankId");
        batchNo = intent.getStringExtra("batchNo") == null ? "" : intent.getStringExtra("batchNo");
        host = intent.getStringExtra("host") == null ? "" : intent.getStringExtra("host");
        STAN = intent.getStringExtra("sysTraceNo") == null ? "" : intent.getStringExtra("sysTraceNo");
        mid = intent.getStringExtra("MId") == null ? "" : intent.getStringExtra("MId");
        tid = intent.getStringExtra("TId") == null ? "" : intent.getStringExtra("TId");
        partnerTrxId = intent.getStringExtra("partnerTrxId") == null ? "" : intent.getStringExtra("partnerTrxId");
        approvalCode = intent.getStringExtra("approvalCode") == null ? "" : intent.getStringExtra("approvalCode");

        payType = intent.getStringExtra(Constants.PAYTYPE) == null ? "" : intent.getStringExtra(Constants.PAYTYPE);
        pMethod = intent.getStringExtra(Constants.PAYMETHOD) == null ? "" : intent.getStringExtra(Constants.PAYMETHOD);
        operatorId = intent.getStringExtra(Constants.OPERATORID) == null ? "" : intent.getStringExtra(Constants.OPERATORID);
        cardNo = intent.getStringExtra(Constants.CARDNO) == null ? "" : intent.getStringExtra(Constants.CARDNO);
        Log.d("OTTO", "cardNo:" + cardNo);

        hideSurcharge = intent.getStringExtra(Constants.pref_hideSurcharge) == null ? "F" : intent.getStringExtra(Constants.pref_hideSurcharge);

        showMerchantName = (TextView) findViewById(R.id.MerName);
        showMerchantRef = (TextView) findViewById(R.id.wxMerchantRef);
        showPayRef = (TextView) findViewById(R.id.wxPayRef);
        showAmount = (TextView) findViewById(R.id.wxAmount);
        surchargeTableRow = (LinearLayout) findViewById(R.id.surchargetablerow);
        surcharge_title = (TextView) findViewById(R.id.surcharge_title);
        showSurcharge = (TextView) findViewById(R.id.wxSurcharge);
        showQRPayment = (TextView) findViewById(R.id.wxPaymentMethod);
        SharedPreferences pref_sur = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String mdr = pref_sur.getString(Constants.mer_mdr, "");
        Surcharge = Surcharge == null ? "0" : Surcharge;

        if (mdr == null || "".equals(mdr)) {
            surchargeTableRow.setVisibility(GONE);
            surcharge_title.setVisibility(GONE);
            showSurcharge.setVisibility(GONE);
        } else if (Double.valueOf(mdr) <= 0) {
            surchargeTableRow.setVisibility(GONE);
            surcharge_title.setVisibility(GONE);
            showSurcharge.setVisibility(GONE);
        } else if (Double.valueOf(mdr) > 0 && Double.valueOf(Surcharge) > 0) {
            surchargeTableRow.setVisibility(VISIBLE);
            surcharge_title.setVisibility(VISIBLE);
            showSurcharge.setVisibility(VISIBLE);
        } else {
            surchargeTableRow.setVisibility(GONE);
            surcharge_title.setVisibility(GONE);
            showSurcharge.setVisibility(GONE);
        }
        showCurrcode = (TextView) findViewById(R.id.wxCurrcode);
        showTXTime = (TextView) findViewById(R.id.wxTXTime);
        showStatus = (TextView) findViewById(R.id.wxPaymentStatus);
        showQRNumber = (TextView) findViewById(R.id.wxQRNumber);
        showErrMsg = (TextView) findViewById(R.id.wxErrMsg);

        showMerchantName.setText(merName);
        showMerchantRef.setText(merchant_ref_no);
        showPayRef.setText(payRef);
        if ("T".equals(hideSurcharge)) {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            merrequestamt = formatter.format(Double.parseDouble(merrequestamt));
            showAmount.setText(merrequestamt);
        } else {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            amount = formatter.format(Double.parseDouble(amount));
            showAmount.setText(amount);
        }
        showSurcharge.setText(Surcharge);
        showCurrcode.setText(Currcode);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        try {
            transactionDate = dateFormat1.parse(TXTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TXTime = dateFormat2.format(transactionDate);
        showTXTime.setText(TXTime);
        showErrMsg.setText(R.string.payment_success);
        showQRNumber.setText(cardNo);

        if ("ALIPAYCNOFFL".equals(pMethod)) {
            showPayMethod = getString(R.string.print_payMethod_alipayOffline);
        } else if ("ALIPAYHKOFFL".equals(pMethod)) {
            showPayMethod = getString(R.string.print_payMethod_alipayHKOffline);
        } else if ("ALIPAYOFFL".equals(pMethod)) {
            showPayMethod = getString(R.string.print_payMethod_alipayOffline);
        } else if ("ALIPAYTHOFFL".equals(pMethod)) {
            showPayMethod = getString(R.string.print_payMethod_alipayTHOffline);
        } else if ("BOOSTOFFL".equals(pMethod)) {
            showPayMethod = getString(R.string.print_payMethod_boostOffline);
        } else if ("GCASHOFFL".equals(pMethod)) {
            showPayMethod = getString(R.string.print_payMethod_gcashOffline);
        } else if ("GRABPAYOFFL".equals(pMethod)) {
            showPayMethod = getString(R.string.print_payMethod_grabpayOffline);
        } else if ("OEPAYOFFL".equals(pMethod)) {
            showPayMethod = getString(R.string.print_payMethod_oepayOffline);
        } else if ("PROMPTPAYOFFL".equals(pMethod)) {
            showPayMethod = getString(R.string.print_payMethod_promptpayOffline);
        } else if ("UNIONPAYOFFL".equals(pMethod)) {
            showPayMethod = getString(R.string.print_payMethod_unionpayOffline);
        } else if ("WECHATHKOFFL".equals(pMethod)) {
            showPayMethod = getString(R.string.print_payMethod_wechatOffline);
        } else if ("WECHATOFFL".equals(pMethod)) {
            showPayMethod = getString(R.string.print_payMethod_wechatOffline);
        }

        showQRPayment.setText(showPayMethod);

        //pay status is success
        PayStatus = getString(R.string.accepted);
        showStatus.setText(PayStatus);

        final Button newpayment = (Button) findViewById(R.id.btnNewpayment);
        final Button print = (Button) findViewById(R.id.btnPrint);

        /** OK Button **/
        newpayment.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Kill the thread before back to MainMenu
                if (threadHandler != null) {
                    threadHandler.removeCallbacks(threadRunObj);
                    returnMainMenu();
                } else {
                    returnMainMenu();
                }
            }
        });

        if (printmode.equals(Constants.PRINT_MODE1) || printmode.equals(Constants.PRINT_MODE3)) {
            print.setEnabled(false);
            if ("WECHATPAYTHOFFL".equalsIgnoreCase(pMethod)) {

            } else {
                if (paybankId.isEmpty()) {
                    printReceipt("Merchant");
                } else {
                    printText(printText);
                    printAlipayTHReceipt("Merchant");
                }

            }

            newpayment.setEnabled(false);

            if (printmode.equals(Constants.PRINT_MODE1)) {

                threadRunObj = new Runnable() {
                    public void run() {
                        if (!paybankId.isEmpty()) {
                            printText(printText);
                            printAlipayTHReceipt("Consumer");
                        } else {
                            printReceipt("Consumer");
                        }
                        newpayment.setEnabled(true);                    }
                };
                threadHandler = new Handler();
                threadHandler.postDelayed(threadRunObj, 5000);

                threadRunObj = new Runnable() {
                    public void run() {
                        returnMainMenu();
                    }
                };
                threadHandler = new Handler();
                threadHandler.postDelayed(threadRunObj, 10000);

            } else {
                newpayment.setEnabled(true);

                threadRunObj = new Runnable() {
                    public void run() {
                        returnMainMenu();
                    }
                };
                threadHandler = new Handler();
                threadHandler.postDelayed(threadRunObj, 5000);

            }
        } else {
            if ("ALIPAYTHOFFL".equalsIgnoreCase(pMethod)) {
                printText(printText);
                printAlipayTHReceipt("Merchant");
            } else if ("WECHATPAYTHOFFL".equalsIgnoreCase(pMethod)) {

            } else {
                printReceipt("Merchant");
            }


            print.setEnabled(false);

            threadRunObj = new Runnable() {
                public void run() {
                    Button print = (Button) findViewById(R.id.btnPrint);
                    print.setEnabled(true);
                    newpayment.setEnabled(true);
                }
            };
            threadHandler = new Handler();
            threadHandler.postDelayed(threadRunObj, 3000);

            threadRunObj = new Runnable() {
                public void run() {
                    returnMainMenu();
                }
            };
            threadHandler = new Handler();
            threadHandler.postDelayed(threadRunObj, 10000);

        }

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print.setEnabled(false);

                if ("ALIPAYTHOFFL".equalsIgnoreCase(pMethod)) {
                    printText(printText);
                    printAlipayTHReceipt("Consumer");
                } else if ("WECHATPAYTHOFFL".equalsIgnoreCase(pMethod)) {

                } else {
                    printReceipt("Consumer");
                }
            }
        });
    }

    public void returnMainMenu() {
        Constants.LIST_ACTIVITY = 0;
        Constants.tabSuccess = 0;
        Intent intentNew = new Intent();
        intentNew.setClass(ScanQR_Success.this, MainActivity.class);
        intentNew.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentNew);
        setResult(RESULT_OK);
        finish();
    }

    public void printReceipt(String receiptType) {

        if ("N".equals(payType)) {
            payType = getString(R.string.sale);
        } else if ("H".equals(payType)) {
            payType = getString(R.string.authorized);
        }
        Log.d("OTTO", "pMethod:" + pMethod + ",payType:" + payType);

        info_merchantName = merName;
        info_payType = getString(R.string.print_paymentType);
        info_operator = getString(R.string.print_operator);
        info_title = getString(R.string.print_receipt);
        info_totalamount = getString(R.string.print_amount);
        info_surcharge = getString(R.string.print_surcharge);
        info_merRequestAmt = getString(R.string.print_merRequestAmt);
        info_paymethod = getString(R.string.print_paymethod);
        info_PayStatus = getString(R.string.print_PayStatus);
        info_cardno = getString(R.string.txn_number_label);
        info_merchantRef = getString(R.string.print_merchantRef);
        info_paymentRef = getString(R.string.print_paymentRef);
        info_transactionDate = getString(R.string.print_transactionDate);
        info_note = getString(R.string.print_note);
        info_isCN = (!"en".equals(language)) ? "T" : "F";
        //一个中文2个字节 一个英文/一个空格/一个标点符号1个字节
        // 简体繁体中文一行16个 空格一行32个 英文大小写一行32个 一个感叹号或逗号


        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        amount = formatter.format(Double.parseDouble(amount.replaceAll(",","")));

        product_OperatorNumber = operatorId;
        product_payType = payType;
        product_CurrCode = Currcode;
        product_Amount = amount;
        product_Surcharge = Surcharge;
        product_merRequestAmt = merrequestamt;
        product_PayMethod = showPayMethod;
        product_PayStatus = PayStatus;
        product_CardNo = cardNo;
        product_merchantRef = merchant_ref_no;
        product_paymentRef = payRef;
        product_transactionDate = TXTime;

        info = new TreeMap<String, String>();
        info.put("hideSurcharge", hideSurcharge);
        info.put("MerchantName", info_merchantName);
        info.put("PaymentType", info_payType);
        info.put("Operator", info_operator);
        info.put("Title", info_title);
        info.put("TotalAmount", info_totalamount);
        info.put("Surcharge", info_surcharge);
        info.put("MerRequestAmt", info_merRequestAmt);
        info.put("PayMethod", info_paymethod);
        info.put("PayStatus", info_PayStatus);
        info.put("CardNo", info_cardno);
        info.put("MerchantRef", info_merchantRef);
        info.put("PaymentRef", info_paymentRef);
        info.put("TransactionDate", info_transactionDate);
        info.put("note", info_note);
        info.put("isCN", info_isCN);

        product = new TreeMap<String, String>();
        product.put("OperatorNumber", product_OperatorNumber);
        product.put("PayType", product_payType);
        product.put("CurrCode", product_CurrCode);
        product.put("Amount", product_Amount);
        product.put("Surcharge", product_Surcharge);
        product.put("MerRequestAmt", product_merRequestAmt);
        product.put("PayMethod", product_PayMethod);
        product.put("PayStatus", product_PayStatus);
        product.put("CardNo", product_CardNo);
        product.put("MerchantRef", product_merchantRef);
        product.put("PaymentRef", product_paymentRef);
        product.put("TransactionDate", product_transactionDate);

        // Merchant Logo
        Bitmap initbitmap = GlobalFunction.getMerLogo(ScanQR_Success.this);

        printUtil = new PrintUtil(ScanQR_Success.this, printeraddress, printername, info, product, initbitmap);

        if (receiptType.equals("Consumer")) {
            copy = getString(R.string.print_customerCopy);
        } else {
            copy = getString(R.string.print_merchantCopy);
        }

        info_copy = copy;
        info.put("copy", info_copy);
        printUtil.sends();

    }

    public void printAlipayTHReceipt(String receiptType) {

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd,yyyy");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("HH:mm:ss");
        String txnDate = dateFormat2.format(transactionDate);
        String txnTime = dateFormat3.format(transactionDate);

        info_merchantName = merName;
        info_tid = getString(R.string.print_tid);
        info_merid = getString(R.string.print_merid);
        info_batchno = getString(R.string.print_batch);
        info_traceno = getString(R.string.print_trace);
        info_host = getString(R.string.print_host);
        info_stan = getString(R.string.print_stan);
        info_walletcard = getString(R.string.print_walletcard);
        info_apprcode = getString(R.string.print_apprcode);
        info_refNo = getString(R.string.print_refno);
        info_walletSale = getString(R.string.print_walletsale);
        info_amount = getString(R.string.print_total);
        info_note = getString(R.string.print_note_bbl);

        int midlength = 15 - mid.length();

        if(midlength < 15){

            String add0 = "";
            for(int i = 0; i < midlength; i++){
                add0 += "0";
            }

            mid = add0 + mid;
        }

        if(!cardNo.isEmpty() && cardNo != null ){

            int cardNoLength = cardNo.length();
            String cardNo1 = cardNo.substring(0, 4);
            String cardNo2 = cardNo.substring(4, 8);
            String cardNo3 = cardNo.substring(8,12);
            String cardNo4 = cardNo.substring(12, cardNoLength);

            cardNo = cardNo1 + " " + cardNo2 + " " + cardNo3 + " " + cardNo4;
        }

        product_tid = tid;
        product_merid = mid;
        product_batchno = batchNo;
        product_traceno = merchant_ref_no;
        product_host = fromHexString(host);
        product_stan = STAN;
        product_walletcard = cardNo;
        product_transactionDate = txnDate;
        product_time = txnTime;
        product_apprcode = approvalCode;
        product_refNo = partnerTrxId;
        product_CurrCode = Currcode;
        product_amount = amount;

        info = new TreeMap<String, String>();
        info.put("hideSurcharge", hideSurcharge);
        info.put("MerchantName", info_merchantName);
        info.put("TID", info_tid);
        info.put("merId", info_merid);
        info.put("batchNo", info_batchno);
        info.put("traceNo", info_traceno);
        info.put("host", info_host);
        info.put("stan", info_stan);
        info.put("walletCard", info_walletcard);
        info.put("apprCode", info_apprcode);
        info.put("refNo", info_refNo);
        info.put("walletSale", info_walletSale);
        info.put("total", info_amount);
        info.put("dateTime", info_datetime);
        info.put("note", info_note);
        info.put("isCN", info_isCN);

        product = new TreeMap<String, String>();
        product.put("TID", product_tid);
        product.put("merId", product_merid);
        product.put("batchNo", product_batchno);
        product.put("traceNo", product_traceno);
        product.put("host", product_host);
        product.put("stan", product_stan);
        product.put("walletCard", product_walletcard);
        product.put("txnDate", product_transactionDate);
        product.put("txnTime", product_time);
        product.put("apprCode", product_apprcode);
        product.put("refNo", product_refNo);
        product.put("currCode", product_CurrCode);
        product.put("amount", product_amount);
        product.put("payMethod", merchantCopy);

        // Merchant Logo
        Bitmap initbitmap = GlobalFunction.getMerLogo(ScanQR_Success.this);

        printUtil = new PrintUtil(ScanQR_Success.this, printeraddress, printername, info, product, initbitmap);

        if (receiptType.equals("Consumer")) {
            copy = getString(R.string.print_customerCopy);
        } else {
            copy = getString(R.string.print_merchantCopy);
        }

        info_copy = copy;
        info.put("copy", info_copy);
        printUtil.sendAlipayTH();

    }


    public String getPrefPayGate() {
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
        String prefpaygate = prefsettings.getString("PayGate", Constants.default_paygate);
        return prefpaygate;
    }

    //-------------------------------------auto logout-------------------------------------//
    /**
     * Time counter Thread
     */
    private Runnable checkTimeOutTask = new Runnable() {

        public void run() {
            Date timeNow = new Date(System.currentTimeMillis());
            //calculate no operation time
            timePeriod = timeNow.getTime() - lastUpdateTime.getTime();

            /*converted into second*/
            float timePeriodSecond = ((float) timePeriod / 1000);

            if (CheckLogout) {
                /* do logout */
                Log.d("OTTO", "做登出操作" + this.getClass());
                //logout flag change to true
                CheckLogout = true;
//                autologout();
            } else {
                if (timePeriodSecond > SESSION_EXPIRED) {
                    /* do logout */
                    Log.d("OTTO", "做登出操作" + this.getClass());
                    //logout flag change to true
                    CheckLogout = true;
//                    autologout();
                } else {
                    Log.d("OTTO", "没有超过规定时长" + this.getClass());
                }
            }

            if (!CheckLogout) {
                //check no opt time per 'CheckInterval' sencond
                CheckTimeOutHandler.postDelayed(checkTimeOutTask, CheckInterval);
            }
        }
    };

    public static String fromHexString(String hex) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }

    public static void printText(String tx) {

        List<String> txList = Arrays.asList(tx.split(","));
        customerCopy = "";
        merchantCopy = "";

        String print = "";
        String waitNextValue = "";
        int previousValue = 0;

        for(int i = 0; i < txList.size(); i++) {

            if(!waitNextValue.isEmpty()) {

                switch(previousValue) {
                    case 1:
                        printCondition("-", txList.get(i));

                        previousValue = 0;
                        waitNextValue = "";
                        break;
                    case 2:
                        printCondition(" ", txList.get(i));

                        previousValue = 0;
                        waitNextValue = "";
                        break;
                }
            }else {

                int switchVal = 0;

                if(txList.get(i).equalsIgnoreCase("03")) {
                    switchVal = 1;
                }else if(txList.get(i).equalsIgnoreCase("FB")) {
                    switchVal = 2;
                }else if(txList.get(i).equalsIgnoreCase("0A")) {
                    switchVal = 3;
                }else if(txList.get(i).equalsIgnoreCase("FC")) {
                    switchVal = 4;
                }

                switch(switchVal) {
                    case 1:
                        customerCopy += fromHexString(print);
                        merchantCopy += fromHexString(print);

                        print = "";
                        break;
                    case 2:
                        waitNextValue = "T";
                        previousValue = 1;

                        if(print.length() > 2) {
                            customerCopy += fromHexString(print);
                            merchantCopy += fromHexString(print);
                        }

                        print = "";
                        break;
                    case 3:
                        if(print.length() > 2) {
                            customerCopy += fromHexString(print);
                            merchantCopy += fromHexString(print);
                        }

                        customerCopy += "\n";
                        merchantCopy += "\n";

                        print = "";
                        break;

                    case 4:
                        waitNextValue = "T";
                        previousValue = 2;

                        if(print.length() > 2) {
                            customerCopy += fromHexString(print);
                            merchantCopy += fromHexString(print);
                        }

                        print = "";
                        break;

                    default:
                        print += txList.get(i);
                        break;
                }

            }
        }

    }

    public static void printCondition(
            String print,
            String length) {

        int len = Integer.parseInt(length)-2;

        for(int i = 0; i < len; i ++ ) {
            customerCopy += print;
            merchantCopy += print;
        }

    }

    //listen touch movement
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("OTTO", "onTouchEvent checklogout:" + CheckLogout);
        if (!CheckLogout) {
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

//    public void autologout() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        SharedPreferences.Editor edit = pref.edit();
//        edit.putBoolean(Constants.CheckLogout, true);
//        edit.commit();
//    }

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


    @Override
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
}
