package com.example.dell.smartpos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.smartpos.FDMS.FdmsApiFunction;
import com.example.dell.smartpos.FDMS.FdmsVariable;
import com.example.dell.smartpos.Printer.K9.PrintUtilK9;
import com.example.dell.smartpos.Printer.PAX.GetObj;
import com.example.dell.smartpos.Printer.PrintUtil;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class HistoryDetails extends AppCompatActivity {

    FdmsApiFunction fdmsRequest;

    LinearLayout SurchargelinearLayout;
    LinearLayout merRequestAmtlinearLayout;
    LinearLayout batchNoLayout;
    LinearLayout invoiceNoLayout;
    LinearLayout traceNoLayout;

    TextView edtAmount;
    TextView lblAmount;
    TextView edtStatus;
    TextView lblSurcharge;
    TextView edtSurcharge;
    TextView edtSur_CurrCode;
    TextView edtmerRequestAmt;
    TextView edtmerRequestAmt_CurrCode;
    TextView edtMerName;
    TextView edtCurrCode;
    TextView tvBatchNo;
    TextView tvInvoiceNo;
    TextView tvTraceNo;
    TextView edtPayMethod;
    TextView edtPayType;
    TextView edtPayRef;
    TextView edtMerRef;
    TextView edtCardNo;
    TextView edtCardHolder;

    String userID;
    String merchantId;
    String merName;
    String currency;
    String amount;
    String merRequestAmt;
    String Surcharge;
    String payBankId;
    //String payBankName;
    String payMethod;
    String payRef;
    String merRef;
    String cardNo;
    String date;
    String cardHolder;
    String status;
    String remark;
    String type;
    String action;
    String settle;
    String PayStatus;
    String hideSurcharge;
    String newAmount;
    String bankId;
    String batchNo;
    String STAN;
    String bankMid;
    String bankTid;
    String bankRef;
    String host;
    String voidTime;
    String authId;
    String traceNo;
    String invoiceNo;
    String fdmsRequestStatus = "";
    String cardRefundControl = "";
    String cardRefundPassword = "";
    String cardVoidControl = "";
    String cardVoidPassword = "";
    String qrRefundControl = "";
    String qrRefundPassword = "";
    String qrVoidControl = "";
    String qrVoidPassword = "";
    String password = "";
    String deviceMan = "";
    String showPayMethod = "";

    boolean hasmdr = false;
    boolean pass = true;

    String finalDate;
    Button void_cap;
    Button send_receipt;
    Button print_receipt;

    public static String customerCopy = "";
    public static String merchantCopy = "";

    View space2;

    static final String actCapture = "Capture";
    static final String actVoid = "Void";
    static final String actRefund = "OnlineRefund";

    //---------for autologout---------//
    Handler CheckTimeOutHandler = new Handler();
    Date lastUpdateTime;//lastest operation time
    long timePeriod;//no operation time
    float SESSION_EXPIRED = Constants.SESSION_EXPIRY / 1000;//session expired time
    boolean CheckLogout = false;//logout flag
    long CheckInterval = 1000;//checking time intercal
    //---------for autologout---------//

    //---------for print func---------//
    String printeraddress = "";
    String printername = "";
    Map<String, String> info = null;
    Map<String, String> product = null;
    PrintUtil printUtil = null;
    GetObj getObj = null;

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
    //---------for print func---------//

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

    String pwProtection = "";
    String sameDayRefund = "";

    boolean edited = false;

    AlertDialog.Builder dialog;
    AlertDialog alertDialog;

    String deviceName;
    PrintUtilK9 printUtilK9 = new PrintUtilK9(HistoryDetails.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);

        setTitle(R.string.transaction_history_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Locale locale = getResources().getConfiguration().locale;
        language = locale.getLanguage();
        Log.d("OTTO", "current language:" + language);

        //---------for autologout---------//
        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout, false);
        lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
        Log.d("OTTO", "init History_Item CheckLogout:" + CheckLogout + ",lastUpdateTime:" + lastUpdateTime);
        //---------for autologout---------//

        // Check language
        GlobalFunction.changeLanguage(HistoryDetails.this);

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

        //---------for print func---------//
        deviceName = android.os.Build.MODEL;
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

        // Get device manufacturer
        deviceMan = GlobalFunction.getDeviceMan();
        System.out.println("deviceMan" + deviceMan);

        //Initialize K9 printer
        if(deviceName.equalsIgnoreCase("K9")){
            printUtilK9.bindService();
        }
        //---------for print func---------//

        //---------judge is airpay or not-----------//
        SharedPreferences merDetails = getApplicationContext().getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
        hideSurcharge = merDetails.getString(Constants.pref_hideSurcharge, "");
        try {
            DesEncrypter encrypter = new DesEncrypter(merName);
            hideSurcharge = encrypter.decrypt(hideSurcharge);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("OTTO", "+++++++History_Item hideSurcharge:" + hideSurcharge);
        //---------judge is airpay or not----------//

        //---------has mdr-----------//
        String SurCalstat = (prefsettings.getString(Constants.pref_surcharge_calculation, Constants.default_surcharge_calculation));
        SharedPreferences pref_sur = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String mdr = pref_sur.getString(Constants.mer_mdr, "");
        if ("OFF".equals(SurCalstat) || mdr == null || "".equals(mdr)) {
            hasmdr = false;
        } else if ("OFF".equals(SurCalstat) || Double.valueOf(mdr) <= 0) {
            hasmdr = false;
        } else if ("ON".equals(SurCalstat) && Double.valueOf(mdr) > 0) {
            hasmdr = true;
        } else {
            hasmdr = false;
        }
        //---------has mdr-----------//
        Intent intent = getIntent();
        userID = intent.getStringExtra(Constants.USERID);
        merName = intent.getStringExtra(Constants.MERNAME);
        Surcharge = intent.getStringExtra(Constants.SURCHARGE);
        currency = intent.getStringExtra(Constants.CURR);
        payMethod = intent.getStringExtra(Constants.PAYMETHOD);
        amount = intent.getStringExtra(Constants.AMOUNT);
        merRequestAmt = intent.getStringExtra(Constants.MERREQUESTAMT);
        payBankId = intent.getStringExtra(Constants.PAYBANKID) == null ? "" : intent.getStringExtra(Constants.PAYBANKID);
        //payBankName = intent.getStringExtra(Constants.PAYBANKNAME) == null ? "" :intent.getStringExtra(Constants.PAYBANKNAME);
        payRef = intent.getStringExtra(Constants.PAYREF);
        merRef = intent.getStringExtra(Constants.MERCHANT_REF);
        cardNo = intent.getStringExtra(Constants.CARDNO);
        date = intent.getStringExtra(Constants.DATE);
        cardHolder = intent.getStringExtra(Constants.CARDHOLDER);
        status = intent.getStringExtra(Constants.STATUS);
        settle = intent.getStringExtra(Constants.SETTLE);
        remark = intent.getStringExtra(Constants.REMARK);
        merchantId = intent.getStringExtra(Constants.MERID);
        batchNo = intent.getStringExtra(Constants.BATCH_NO) == null ? "" : intent.getStringExtra(Constants.BATCH_NO);
        traceNo = intent.getStringExtra(Constants.TRACE_NO) == null ? "" : intent.getStringExtra(Constants.TRACE_NO);
        invoiceNo = intent.getStringExtra(Constants.INVOICE_NO) == null ? "" : intent.getStringExtra(Constants.INVOICE_NO);

        //System.out.println("[HistoryDetails] payBankName: " + payBankName);
        System.out.println("[HistoryDetails] payBankId: " + payBankId);

        pwProtection = (prefsettings.getString(Constants.pref_refund_pw_protection, Constants.default_refund_pw_protection));
        sameDayRefund = (prefsettings.getString(Constants.pref_same_day_refund, Constants.default_same_day_refund));

        merRequestAmtlinearLayout = (LinearLayout) findViewById(R.id.merRequestAmtlayout);
        SurchargelinearLayout = (LinearLayout) findViewById(R.id.surchargelayout);
        merRequestAmtlinearLayout = (LinearLayout) findViewById(R.id.merRequestAmtlayout);
        batchNoLayout = (LinearLayout) findViewById(R.id.batchNolayout);
        invoiceNoLayout = (LinearLayout) findViewById(R.id.invoiceNolayout);
        traceNoLayout = (LinearLayout) findViewById(R.id.traceNolayout);

        edtMerName = (TextView) findViewById(R.id.edtMername);
        lblSurcharge = (TextView) findViewById(R.id.lblSurcharge);
        lblAmount = (TextView) findViewById(R.id.lblAmount);
        edtAmount = (TextView) findViewById(R.id.edtAmount);
        edtCurrCode = (TextView) findViewById(R.id.edtCurrCode);
        tvBatchNo = (TextView) findViewById(R.id.edtBatchNo);
        tvInvoiceNo = (TextView) findViewById(R.id.edtInvoiceNo);
        tvTraceNo = (TextView) findViewById(R.id.edtTraceNo);
        edtmerRequestAmt = (TextView) findViewById(R.id.edtmerRequestAmt);
        edtmerRequestAmt_CurrCode = (TextView) findViewById(R.id.edtmerRequestAmt_CurrCode);
        edtSurcharge = (TextView) findViewById(R.id.edtSurcharge);
        edtSur_CurrCode = (TextView) findViewById(R.id.edtSur_CurrCode);
        edtPayMethod = (TextView) findViewById(R.id.edtPayMethod);
        edtPayType = (TextView) findViewById(R.id.edtPayType);
        edtPayRef = (TextView) findViewById(R.id.edtPayRef);
        edtMerRef = (TextView) findViewById(R.id.edtMerRef);
        edtCardNo = (TextView) findViewById(R.id.edtCardNo);
        edtCardHolder = (TextView) findViewById(R.id.edtCardHolder);
        edtStatus = (TextView) findViewById(R.id.edtStatus);

        send_receipt = (Button) findViewById(R.id.his_send_receipt);
        void_cap = (Button) findViewById(R.id.void_capture);
        print_receipt = (Button) findViewById(R.id.his_print_receipt);
        space2 = (View) findViewById(R.id.space2);

        edtMerName.setText(merName);
        edtAmount.setText(amount);
        edtCurrCode.setText(currency);

        System.out.println("---payMethod: " + payMethod);


        if ((merRequestAmt == null) || ("".equals(merRequestAmt))) {
            edtmerRequestAmt.setText(amount);
        } else {
            if (Double.valueOf(merRequestAmt) <= 0) {
                edtmerRequestAmt.setText(amount);
            } else {
                edtmerRequestAmt.setText(merRequestAmt);
            }
        }

        edtmerRequestAmt_CurrCode.setText(currency);
        edtSurcharge.setText(Surcharge);
        edtSur_CurrCode.setText(currency);

        Surcharge = Surcharge == null ? "0" : Surcharge;
        if (!hasmdr || (Double.valueOf(Surcharge) <= 0)) {
            SurchargelinearLayout.setVisibility(GONE);
            edtSurcharge.setVisibility(GONE);
            edtSur_CurrCode.setVisibility(GONE);
            lblSurcharge.setVisibility(GONE);
            merRequestAmtlinearLayout.setVisibility(GONE);
            edtmerRequestAmt.setVisibility(GONE);
            edtmerRequestAmt_CurrCode.setVisibility(GONE);
        }

        edtPayType.setText(R.string.sale);

        /** Control the payMethod display text **/
        if (payMethod.equalsIgnoreCase("Master")) {
            showPayMethod = getString(R.string.MasterCard);
        } else if (payMethod.equalsIgnoreCase("VISA")) {
            showPayMethod = getString(R.string.visa);
        } else {
            showPayMethod = payMethod;
        }
        edtPayMethod.setText(showPayMethod);

        /** To control 'Batch No' 'Invoice no' 'Trace No' layout **/
        if (payBankId.equalsIgnoreCase(Constants.FIRST_DATA)) {
            batchNoLayout.setVisibility(VISIBLE);
            invoiceNoLayout.setVisibility(VISIBLE);
            traceNoLayout.setVisibility(VISIBLE);

            tvBatchNo.setText(batchNo);
            tvInvoiceNo.setText(invoiceNo);
            tvTraceNo.setText(traceNo);
        } else {
            batchNoLayout.setVisibility(GONE);
            invoiceNoLayout.setVisibility(GONE);
            traceNoLayout.setVisibility(GONE);
        }

        /** Set amount label to 'Refunded Amount' if status is 'Refunded' / 'PartialRefunded' **/
        if (status.equalsIgnoreCase(getString(R.string.refunded)) ||
                status.equalsIgnoreCase(getString(R.string.partialrefunded))) {
            lblAmount.setText(getString(R.string.refunded_amount));
        }

        edtPayRef.setText(payRef);
        edtMerRef.setText(merRef);
        edtCardNo.setText(cardNo);
        edtCardHolder.setText(cardHolder);

        if ("ALIPAYOFFL".equals(payMethod)
                || "ALIPAYCNOFFL".equals(payMethod)
                || "ALIPAYHKOFFL".equals(payMethod)
                || "ALIPAYHKONL".equals(payMethod)
                || "BOOSTOFFL".equals(payMethod)
                || "GCASHOFFL".equals(payMethod)
                || "GRABPAYOFFL".equals(payMethod)
                || "OEPAYOFFL".equals(payMethod)
                || "PROMPTPAYOFFL".equals(payMethod)
                || "UNIONPAYOFFL".equals(payMethod)
                || "WECHATOFFL".equals(payMethod)
                || "WECHATONL".equals(payMethod)) {
            LinearLayout cardholderlinearlayout = (LinearLayout) findViewById(R.id.cardholderlinearlayout);
            cardholderlinearlayout.setVisibility(GONE);
            TextView cardnotitle = (TextView) findViewById(R.id.cardnotitle);
            //--Edited 25/07/18 by KJ-
            cardnotitle.setText(getString(R.string.txn_number_label));
            //--done Edited 25/07/18 by KJ-
        }

        TextView edtDate = (TextView) findViewById(R.id.edtDate);
        String date1 = date.substring(0, 8);
        String time = date.substring(8, 14);
        StringBuilder sbdate = new StringBuilder(date1);
        sbdate.insert(2, "/");
        sbdate.insert(5, "/");
        StringBuilder sbtime = new StringBuilder(time);
        sbtime.insert(2, ":");
        sbtime.insert(5, ":");
        finalDate = sbdate.toString() + " " + sbtime.toString();
        edtDate.setText(finalDate);




        initViews();

        System.out.println("---sameDay=" + sameDayRefund);

        /** Reprint receipt is not allowed for card settled txn **/
        if (settle.equalsIgnoreCase("T") && payBankId.equalsIgnoreCase(Constants.FIRST_DATA)) {
            print_receipt.setVisibility(GONE);
        }

        if (sameDayRefund.equalsIgnoreCase("ON")) {
            if (isRefundable(payMethod.toLowerCase(Locale.US))) {
                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                float days = 0;
                try {
                    Date now = new Date();
                    Date date3 = myFormat.parse(finalDate);

                    long diff = now.getTime() - date3.getTime();
                    days = (diff / (1000 * 60 * 60 * 24));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (days >= 1) {
                    void_cap.setEnabled(false);
                }
            }
        } else {
            if ("BOOSTOFFL".equals(payMethod)) {
                if (isRefundable(payMethod.toLowerCase(Locale.US))) {
                    SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                    float days = 0;
                    try {
                        Date now = new Date();
                        Date date3 = myFormat.parse(finalDate);

                        long diff = now.getTime() - date3.getTime();
                        days = (diff / (1000 * 60 * 60 * 24));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    System.out.println("---days=" + days);
                    if (days >= 1) {
                        void_cap.setEnabled(false);
                    }
                }
            }
        }

        /** Void or Refund Transaction **/
        void_cap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                System.out.println("---status: " + status);
                System.out.println("---settle: " + settle);
                pass = false;
                if (status.equalsIgnoreCase(getString(R.string.authorized))) {
                    pass = true;
                    status = getString(R.string.authorized);
                    type = actCapture;
                    action = "Capture Transaction";
                } else if (status.equalsIgnoreCase(getString(R.string.accepted)) || status.equalsIgnoreCase(getString(R.string.accepted_adj))) {
//                    if (status.equalsIgnoreCase(getString(R.string.accepted))) {
//                        status = getString(R.string.accepted);
//                    } else {
//                        status = getString(R.string.accepted_adj);
//                    }
//

                    if (settle.equalsIgnoreCase("T")) {

                        /** Refund Transaction **/
                        if (isRefundable(payMethod.toLowerCase(Locale.US))) {
                            type = actRefund;
                            System.out.println("[HistoryDetails] Enter Refund Transaction");
                            System.out.println("[HistoryDetails] payMethod: " + payMethod);

                            /** Card Refund Transaction **/
                            if (GlobalFunction.isCardpMethods(payMethod)) {
                                cardRefundControl = GlobalFunction.getCardRefundControl(HistoryDetails.this);
                                if (cardRefundControl.equalsIgnoreCase("ON")) {
                                        dialog = new AlertDialog.Builder(HistoryDetails.this);
                                        dialog.setTitle(R.string.input_PW);

                                        // EditText
                                        final EditText inputSettingPassword = new EditText(HistoryDetails.this);
                                        inputSettingPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                        inputSettingPassword.setHint(getString(R.string.password));

                                        cardRefundPassword = GlobalFunction.getCardRefundPassword(HistoryDetails.this);

                                    dialog.setView(inputSettingPassword);
                                    dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            password = inputSettingPassword.getText().toString().trim();
                                            if (password.equals(cardRefundPassword)) {
                                                // Correct card refund password
                                                confirmAction();
                                            } else {
                                                // Wrong card refund password
                                                Toast.makeText(HistoryDetails.this, R.string.error3, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    alertDialog = dialog.create();
                                    alertDialog.show();
                                } else {
                                    // Without password protection
                                    confirmAction();
                                }

                            }
                            /** QR Refund Transaction **/
                            else if (GlobalFunction.isQRpMethods(payMethod)) {
                                qrRefundControl = GlobalFunction.getQRRefundControl(HistoryDetails.this);
                                System.out.println("qrRefundControl: " + qrRefundControl);
                                if (qrRefundControl.equalsIgnoreCase("ON")) {
                                    dialog = new AlertDialog.Builder(HistoryDetails.this);
                                    dialog.setTitle(R.string.input_PW);

                                    // EditText
                                    final EditText inputSettingPassword = new EditText(HistoryDetails.this);
                                    inputSettingPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                    inputSettingPassword.setHint(getString(R.string.password));

                                    qrRefundPassword = GlobalFunction.getQRRefundPassword(HistoryDetails.this);

                                    dialog.setView(inputSettingPassword);
                                    dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            password = inputSettingPassword.getText().toString().trim();
                                            if (password.equals(qrRefundPassword)) {
                                                // Correct QR refund password
                                                confirmAction();
                                            } else {
                                                // Wrong QR refund password
                                                Toast.makeText(HistoryDetails.this, R.string.error3, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    alertDialog = dialog.create();
                                    alertDialog.show();
                                } else {
                                    // Without password protection
                                    confirmAction();
                                }
                            }
                        } /** End of Refund Transaction **/

                    } else {
                        /** Void Transaction **/
                        type = actRefund;
                        System.out.println("[HistoryDetails] Enter Void Transaction");
                        System.out.println("[HistoryDetails] isCardpMethods: " + GlobalFunction.isCardpMethods(payMethod));

                        /** Card Void Transaction **/
                        if (GlobalFunction.isCardpMethods(payMethod)) {
                            cardVoidControl = GlobalFunction.getCardVoidControl(HistoryDetails.this);
                            if (cardVoidControl.equalsIgnoreCase("ON")) {
                                dialog = new AlertDialog.Builder(HistoryDetails.this);
                                dialog.setTitle(R.string.input_PW);

                                // EditText
                                final EditText inputSettingPassword = new EditText(HistoryDetails.this);
                                inputSettingPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                inputSettingPassword.setHint(getString(R.string.password));

                                cardVoidPassword = GlobalFunction.getCardVoidPassword(HistoryDetails.this);

                                dialog.setView(inputSettingPassword);
                                dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        password = inputSettingPassword.getText().toString().trim();
                                        if (password.equals(cardVoidPassword)) {
                                            // Correct card void password
                                            confirmAction();
                                        } else {
                                            // Wrong card void password
                                            Toast.makeText(HistoryDetails.this, R.string.error3, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                alertDialog = dialog.create();
                                alertDialog.show();
                            } else {
                                // Without password protection
                                confirmAction();
                            }

                        }
                        /** QR Void Transaction **/
                        else if (GlobalFunction.isQRpMethods(payMethod)) {
                            qrVoidControl = GlobalFunction.getQRVoidControl(HistoryDetails.this);
                            if (qrVoidControl.equalsIgnoreCase("ON")) {
                                dialog = new AlertDialog.Builder(HistoryDetails.this);
                                dialog.setTitle(R.string.input_PW);

                                // EditText
                                final EditText inputSettingPassword = new EditText(HistoryDetails.this);
                                inputSettingPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                inputSettingPassword.setHint(getString(R.string.password));

                                qrVoidPassword = GlobalFunction.getQRVoidPassword(HistoryDetails.this);


                                dialog.setView(inputSettingPassword);
                                dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        password = inputSettingPassword.getText().toString().trim();
                                        if (password.equals(qrVoidPassword)) {
                                            // Correct QR void password
                                            confirmAction();
                                        } else {
                                            // Wrong QR void password
                                            Toast.makeText(HistoryDetails.this, R.string.error3, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                alertDialog = dialog.create();
                                alertDialog.show();
                            } else {
                                // Without password protection
                                confirmAction();
                            }
                        }
                    } /** End of Void Transaction **/
                }
            }
        });

        send_receipt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constants.MERNAME, merName);
                intent.putExtra(Constants.MERID, merchantId);
                intent.putExtra(Constants.PAYREF, payRef);
                intent.setClass(HistoryDetails.this, DialogSendReceipt.class);
                startActivity(intent);

            }

        });

        /** Reprint Receipt **/
        /** Note: Card txn not allow to reprint after settlement **/
        print_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payBankId.equalsIgnoreCase(Constants.FIRST_DATA)) {
                    // For First-Data printing only
                    System.out.println("Enter print FDMS");
                    System.out.println("TraceNo: " + traceNo);
                    fdmsRequestStatus = "reprintReceipt";

                    TextView edtTraceNo = findViewById(R.id.edtTraceNo);
                    FdmsVariable.setTraceNo(edtTraceNo.getText().toString());

                    fdmsRequest = new FdmsApiFunction(HistoryDetails.this);
                    fdmsRequest.reprintRequest();
                } else {
                    printReceipt();
                }
            }
        });
    }

    public void confirmAction() {

        if (payBankId.equalsIgnoreCase(Constants.FIRST_DATA)) {
            fdmsRequestStatus = "voidTxn";
            FdmsVariable.setMerId(merchantId);
            FdmsVariable.setPayRef(payRef);
            FdmsVariable.setUserID(userID);
            FdmsVariable.setMerName(merName);
            FdmsVariable.setSurcharge(Surcharge);
            //System.out.println("Currency " + currency);
            FdmsVariable.setCurrName(currency);
            FdmsVariable.setPayMethod(payMethod);
            FdmsVariable.setAmount(amount);
            FdmsVariable.setMerRequestAmt(merRequestAmt);
            FdmsVariable.setPayBankId(payBankId);
            FdmsVariable.setMerRef(merRef);
            FdmsVariable.setCardNo(cardNo);
            FdmsVariable.setTxnTime(date);
            FdmsVariable.setCardHolder(cardHolder);
            FdmsVariable.setStatus(status);
            FdmsVariable.setSettle(settle);
            FdmsVariable.setRemark(remark);
            FdmsVariable.setBatchNo(batchNo);
            FdmsVariable.setTraceNo(traceNo);
            FdmsVariable.setInvoiceNo(invoiceNo);

            fdmsRequest = new FdmsApiFunction(HistoryDetails.this);
            fdmsRequest.voidRequest();

//            Intent intent = new Intent(HistoryDetails.this, DialogActivity.class);
//            startActivityForResult(intent, 1);

        } else {
            Intent intent = new Intent(HistoryDetails.this, DialogActivity.class);
            intent.putExtra(Constants.MERNAME, merName);
            intent.putExtra("button", "first");
            // intent.putExtra("action", action);
            intent.putExtra("type", type);
            intent.putExtra(Constants.SETTLE, settle);
            intent.putExtra(Constants.MERCHANT_REF, merRef);
            intent.putExtra(Constants.PAYREF, payRef);
            intent.putExtra(Constants.DATE, finalDate);
            if (Double.valueOf(Surcharge) <= 0) {
                intent.putExtra(Constants.AMOUNT, amount);
            } else {
                intent.putExtra(Constants.AMOUNT, merRequestAmt);
            }
            intent.putExtra(Constants.CURR, currency);
            intent.putExtra(Constants.MERID, merchantId);
            intent.putExtra(Constants.MERREQUESTAMT, merRequestAmt);
            intent.putExtra(Constants.SURCHARGE, Surcharge);
            intent.putExtra(Constants.STATUS, status);
            intent.putExtra(Constants.PAYBANKID, payBankId);
            //intent.putExtra(Constants.PAYBANKNAME, payBankName);
            intent.putExtra(Constants.PAYMETHOD, payMethod);
            startActivityForResult(intent, 1);
        }
    }


    private void initViews() {
        if (status.equalsIgnoreCase("Authorized")) {
            void_cap.setText(getString(R.string.capture_c));

        } else if (status.equalsIgnoreCase("Accepted") ||
                status.equalsIgnoreCase("Accepted_Adj")) {
            Log.d("OTTO", "HISTORY_ITEM settle:" + settle + ",payMethod:" + payMethod);
            if (settle.equalsIgnoreCase("T")) {

                if (isRefundable(payMethod.toLowerCase(Locale.US))) {
                    void_cap.setText(getString(R.string.refund_c));

                    if (payMethod.equalsIgnoreCase("OEPAYOFFL")) {
                        void_cap.setVisibility(GONE);
                    } else if (payMethod.equalsIgnoreCase("BOOSTOFFL")) {
                        void_cap.setText(getString(R.string.void_c));
                    } else if (payMethod.equalsIgnoreCase("PROMPTPAYOFFL") && payBankId.equalsIgnoreCase("SCB")) {
                        void_cap.setVisibility(GONE);
                    }

                    Log.d("OTTO", "HISTORY_ITEM settle:" + settle + ",show:" + payMethod);
                } else {
                    void_cap.setVisibility(GONE);
                    send_receipt.setVisibility(GONE);

//                    if (!payMethod.equalsIgnoreCase("OEPAYOFFL")) {
//                        print_receipt.setVisibility(GONE);
//                    }

                    Log.d("OTTO", "HISTORY_ITEM settle:" + settle + ",gone:" + payMethod);
                }

            } else {
                void_cap.setText(getString(R.string.void_c));

                if (payMethod.equalsIgnoreCase("OEPAYOFFL")) {
                    void_cap.setVisibility(GONE);
                } else if (payMethod.equalsIgnoreCase("PROMPTPAYOFFL") &&
                        payBankId.equalsIgnoreCase("SCB")) {
                    void_cap.setVisibility(GONE);
                }


            }
            if (status.equalsIgnoreCase("Accepted_Adj")) {
                if (((amount != null) && (!"".equals(amount))) && ((Surcharge != null) && (!"".equals(Surcharge)))) {
                    double btn_amount = 0;
                    double btn_Surcharge = 0;
                    try {
                        btn_amount = Double.parseDouble(amount);
                        btn_Surcharge = Double.parseDouble(Surcharge);
                        if (btn_amount == btn_Surcharge) {
                            void_cap.setVisibility(GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } else {
            void_cap.setVisibility(GONE);
            send_receipt.setVisibility(GONE);
            if (status.equalsIgnoreCase("Refunded") || status.equalsIgnoreCase(getString(R.string.pending_refund))
                    || status.equalsIgnoreCase("Voided") || status.equalsIgnoreCase(getString(R.string.partialrefunded))
                    || status.equalsIgnoreCase("Accepted_Adj") || status.equalsIgnoreCase(getString(R.string.requestRefund))) {
                print_receipt.setVisibility(VISIBLE);
            } else {
                print_receipt.setVisibility(GONE);
            }
        }


        if (status.equalsIgnoreCase("Authorized")) {
            status = getString(R.string.authorized);
            type = actCapture;
            action = "Capture Transaction";
        } else if (status.equalsIgnoreCase("Accepted")) {
            System.out.println("KJ---settle:" + settle);
            if (settle.equalsIgnoreCase("T")) {
                status = getString(R.string.accepted);
                type = actRefund;
                action = "Refund Transaction";
            } else {
                status = getString(R.string.accepted);
                type = actVoid;
                action = "Void Transaction";
            }
        } else if (status.equalsIgnoreCase("Accepted_Adj")) {
            if (settle.equalsIgnoreCase("T")) {
                status = getString(R.string.accepted_adj);
                type = actRefund;
                action = "Refund Transaction";
            } else {
                status = getString(R.string.accepted_adj);
                status = "Accepted_Adj";
                type = actVoid;
                action = "Void Transaction";
            }
        } else if (status.equalsIgnoreCase("Rejected")) {
            status = getString(R.string.rejected);
        } else if (status.equalsIgnoreCase("Voided")) {
            status = getString(R.string.voided);
        } else if (status.equalsIgnoreCase(("Pending"))) {
            status = getString(R.string.pending);
        } else if (status.equalsIgnoreCase("PartialRefunded")) {
            status = getString(R.string.partialrefunded);
        } else if (status.equalsIgnoreCase("RequestRefund")) {
            status = getString(R.string.requestRefund);
        } else if (status.equalsIgnoreCase("Refunded")) {
            status = getString(R.string.refunded);
        } else if (status.equalsIgnoreCase("Cancelled")) {
            status = getString(R.string.cancelled);
        }
        edtStatus.setText(status);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("---resultcode=" + requestCode + " " + resultCode);


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

        if (payBankId.equalsIgnoreCase(Constants.FIRST_DATA) && deviceMan.equals("PAX")) {
            /** First-Data void success **/
            GlobalFunction.disablePaxNavigationButton(HistoryDetails.this);

            if (fdmsRequestStatus.equalsIgnoreCase("voidTxn")) {

                // First-Data requestCode = 100
                fdmsRequest.voidResponse(requestCode, resultCode, data);

                //Change status to editted
                edited = true;
            }
        } else if (requestCode == 1) {
            /** Callback from DialogActivity **/
            /** Refund or Void success **/
            if (resultCode == RESULT_OK) {
                String oriAmount = data.getStringExtra("oriAmount");
                oriAmount = oriAmount.replaceAll(",", "");
                newAmount = data.getStringExtra("amt");
                newAmount = newAmount.replaceAll(",", "");
                String newStatus = data.getStringExtra("newStatus");
                String tx = data.getStringExtra("printText");

                DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");

                if (type.equalsIgnoreCase(actRefund)) {
                    /** Refund success **/
                    System.out.println("oriAmount: " + oriAmount);

                    lblAmount.setText(getString(R.string.refunded_amount));
                    System.out.println("newAmount: " + newAmount);

                    if (payBankId.equalsIgnoreCase("SUYUPAY")) {
                        newStatus = getString(R.string.pending_refund);
                    } else if (Double.parseDouble(newAmount) < Double.parseDouble(oriAmount)) {
                        /** Set new status to 'partial refunded' **/
                        newStatus = getString(R.string.partialrefunded);
                    } else {
                        /** Set default new status to 'refunded' **/
                        newStatus = getString(R.string.refunded);
                    }
                } else if (type.equalsIgnoreCase(actVoid)) {
                    /** Void success **/
                    newStatus = getString(R.string.voided);
                    lblAmount.setText(getString(R.string.voided_amount));
                    newAmount = formatter.format(Double.parseDouble(data.getStringExtra("amt2")));
                }

                System.out.println("amount:" + newAmount);
                edtStatus.setText(newStatus);
                edtAmount.setText(GlobalFunction.toCurrencyFormat(newAmount));
                status = newStatus;
                void_cap.setEnabled(false);

                void_cap.setBackgroundResource(R.drawable.btn_lightblue);

                //Print Receipt after Refund done
                if (tx != null) {
                    bankId = data.getStringExtra("bankId");
                    batchNo = data.getStringExtra("batchNo");
                    STAN = data.getStringExtra("sysTraceNo");
                    bankMid = data.getStringExtra("bankMid");
                    bankTid = data.getStringExtra("bankTid");
                    bankRef = data.getStringExtra("bankRef");
                    host = data.getStringExtra("host");
                    voidTime = data.getStringExtra("voidTime");
                    authId = data.getStringExtra("authId");

                    printText(tx);
                    printBBLReceipt();
                } else {
                    printReceipt();
                }
                //Change status to editted
                edited = true;
            }

            if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    public Boolean isRefundable(String payMethod) {
        SharedPreferences merdetails = getApplicationContext().getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
        String encConVisaOR = "";
        String encConAmexOR = "";
        String encConMasterOR = "";
        String encConJcbOR = "";
        try {
            DesEncrypter encrypter = new DesEncrypter(merName);
            encConVisaOR = encrypter.encrypt(Constants.pref_visaOR);
            encConAmexOR = encrypter.encrypt(Constants.pref_amexOR);
            encConMasterOR = encrypter.encrypt(Constants.pref_masterOR);
            encConJcbOR = encrypter.encrypt(Constants.pref_jcbOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String VisaOR = merdetails.getString(encConVisaOR, "");
        String AmexOR = merdetails.getString(encConAmexOR, "");
        String MasterOR = merdetails.getString(encConMasterOR, "");
        String JcbOR = merdetails.getString(encConJcbOR, "");
        if (payMethod.equalsIgnoreCase("VISA")) {
            if (VisaOR.equalsIgnoreCase("t")) {
                return true;
            } else {
                return false;
            }
        } else if (payMethod.equalsIgnoreCase("Master") || payMethod.equalsIgnoreCase("MasterCard")) {
            if (MasterOR.equalsIgnoreCase("t")) {
                return true;
            } else {
                return false;
            }
        } else if (payMethod.equalsIgnoreCase("AMEX")) {
            if (AmexOR.equalsIgnoreCase("t")) {
                return true;
            } else {
                return false;
            }
        } else if (payMethod.equalsIgnoreCase("JCB")) {
            if (JcbOR.equalsIgnoreCase("t")) {
                return true;
            } else {
                return false;
            }
        } else if (payMethod.equalsIgnoreCase("WECHATOFFL") || payMethod.equalsIgnoreCase("WECHATONL")
                || payMethod.equalsIgnoreCase("ALIPAYCNOFFL") || payMethod.equalsIgnoreCase("ALIPAYOFFL")
                || payMethod.equalsIgnoreCase("ALIPAYHKOFFL") || payMethod.equalsIgnoreCase("BOOSTOFFL")
                || payMethod.equalsIgnoreCase("GRABPAYOFFL") || payMethod.equalsIgnoreCase("GCASHOFFL")
                || payMethod.equalsIgnoreCase("UNIONPAYOFFL") || payMethod.equalsIgnoreCase("WECHATHKOFFL")) {
            return true;
        } else {
            return false;
        }
    }

    private void printReceipt() {

        String print_payMethod = "";

        //initialize
        final String[] printCopy = {getResources().getString(R.string.print_customerCopy), getResources().getString(R.string.print_merchantCopy)};

        if (status.equalsIgnoreCase(getString(R.string.authorized))) {
            PayStatus = getString(R.string.authorized);
        } else if (status.equalsIgnoreCase(getString(R.string.accepted))) {
            PayStatus = getString(R.string.accepted);
        } else if (status.equalsIgnoreCase((getString(R.string.pending)))) {
            PayStatus = getString(R.string.pending);
        } else if (status.equalsIgnoreCase(getString(R.string.partialrefunded))) {
            PayStatus = getString(R.string.partialrefunded);
        } else if (status.equalsIgnoreCase(getString(R.string.capture))) {
            PayStatus = getString(R.string.capture);
        } else if (status.equalsIgnoreCase(getString(R.string.accepted_adj))) {
            PayStatus = getString(R.string.accepted_adj);
        } else if (status.equalsIgnoreCase(getString(R.string.void_))) {
            PayStatus = getString(R.string.void_);
        } else if (status.equalsIgnoreCase(getString(R.string.refund))) {
            PayStatus = getString(R.string.refund);
        } else if (status.equalsIgnoreCase(getString(R.string.refunded))) {
            PayStatus = getString(R.string.refunded);
        } else if (status.equalsIgnoreCase("OnlineRefunded")) {
            PayStatus = getString(R.string.refunded);
        } else if (status.equals(getString(R.string.authorized))) {
            PayStatus = getString(R.string.authorized);
        } else if (status.equals(getString(R.string.accepted))) {
            PayStatus = getString(R.string.accepted);
        } else if (status.equalsIgnoreCase(getString(R.string.capture))) {
            PayStatus = getString(R.string.capture);
        } else if (status.equals(getString(R.string.accepted_adj))) {
            PayStatus = getString(R.string.accepted_adj);
        } else if (status.equals(getString(R.string.rejected))) {
            PayStatus = getString(R.string.rejected);
        } else if (status.equals(getString(R.string.voided))) {
            PayStatus = getString(R.string.voided);
        } else if (status.equals(getString(R.string.pending_refund))) {
            PayStatus = getString(R.string.pending_refund);
        } else if (status.equals(getString(R.string.requestRefund))) {
            PayStatus = getString(R.string.requestRefund);
        } else {
            PayStatus = "";
        }



        String payType = "N";
        if ("N".equals(payType)) {
            payType = getString(R.string.sale);
        } else if ("H".equals(payType)) {
            payType = getString(R.string.authorized);
        }
        Log.d("OTTO", "pMethod:" + payMethod + ",payType:" + payType);

        info_merchantName = merName;
        info_operator = getString(R.string.print_operator);
        info_payType = getString(R.string.print_paymentType);
        if (status.equalsIgnoreCase("OnlineRefunded") || status.equals(getString(R.string.voided))) {
            info_title = getString(R.string.print_receipt);
        } else {
            info_title = getString(R.string.reprint_receipt);
        }

        /*if (type != null) {
            if (type.equalsIgnoreCase(actRefund)) {
                info_totalamount = getString(R.string.refunded_amount);
            } else if (type.equalsIgnoreCase(actVoid)) {
                info_totalamount = getString(R.string.voided_amount);
            } else {
                info_totalamount = getString(R.string.print_amount);
            }
        } else {
            info_totalamount = getString(R.string.print_amount);
        }*/


        if (status.equalsIgnoreCase("OnlineRefunded") ||
                status.equalsIgnoreCase(getString(R.string.refunded)) ||
                status.equalsIgnoreCase(getString(R.string.partialrefunded))) {
            info_totalamount = getString(R.string.refunded_amount);
        } else if (status.equalsIgnoreCase(getString(R.string.voided))) {
            info_totalamount = getString(R.string.voided_amount);
        } else {
            info_totalamount = getString(R.string.print_amount);
        }


        info_surcharge = getString(R.string.print_surcharge);
        info_merRequestAmt = getString(R.string.print_merRequestAmt);
        info_paymethod = getString(R.string.print_paymethod);
        info_transactionDate = getString(R.string.print_transactionDate);
        info_PayStatus = getString(R.string.print_PayStatus);
        if (GlobalFunction.isQRpMethods(payMethod)) {
            // Change label to "Trx No." if payment methods is QR
            info_cardno = getString(R.string.txn_number_label);
        } else {
            info_cardno = getString(R.string.print_cardno);
        }
        info_merchantRef = getString(R.string.print_merchantRef);
        info_paymentRef = getString(R.string.print_paymentRef);
        info_note = getString(R.string.print_note);
        info_isCN = (!"en".equals(language)) ? "T" : "F";

        info = new TreeMap<String, String>();
        info.put("hideSurcharge", hideSurcharge);
        info.put("MerchantName", info_merchantName);
        info.put("Operator", info_operator);
        info.put("PaymentType", info_payType);
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

        if ("ALIPAYHKOFFL".equals(payMethod)) {
            print_payMethod = getString(R.string.print_payMethod_alipayHKOffline);
        } else if ("ALIPAYOFFL".equals(payMethod)) {
            print_payMethod = getString(R.string.print_payMethod_alipayOffline);
        } else if ("ALIPAYCNOFFL".equals(payMethod)) {
            print_payMethod = getString(R.string.print_payMethod_alipayOffline);
        } else if ("WECHATOFFL".equals(payMethod)) {
            print_payMethod = getString(R.string.print_payMethod_wechatOffline);
        } else if ("WECHATONL".equals(payMethod)) {
            print_payMethod = getString(R.string.print_payMethod_wechatOnline);
        } else if ("OEPAYOFFL".equals(payMethod)) {
            print_payMethod = getString(R.string.print_payMethod_oepayOffline);
        } else if ("BOOSTOFFL".equals(payMethod)) {
            print_payMethod = getString(R.string.print_payMethod_boostOffline);
        } else if ("PROMPTPAYOFFL".equals(payMethod)) {
            print_payMethod = getString(R.string.print_payMethod_promptpayOffline);
        } else if ("GCASHOFFL".equals(payMethod)) {
            print_payMethod = getString(R.string.print_payMethod_gcashOffline);
        } else if ("UNIONPAYOFFL".equals(payMethod)) {
            print_payMethod = getString(R.string.print_payMethod_unionpayOffline);
        } else if ("GRABPAYOFFL".equals(payMethod)) {
            print_payMethod = getString(R.string.print_payMethod_grabpayOffline);
        } else if (payMethod.equalsIgnoreCase("Master")) {
            print_payMethod = getString(R.string.MasterCard);
        } else {
            print_payMethod = payMethod;

        }

        System.out.println("---paystatus" + PayStatus);
        product_OperatorNumber = userID;
        product_payType = payType;
        product_CurrCode = currency;

        if (type != null) {
            if (type.equalsIgnoreCase(actRefund)) {
                product_Amount = newAmount;
            } else {
                product_Amount = amount;
            }
        } else {
            product_Amount = amount;
        }

//        if (status.equalsIgnoreCase("OnlineRefunded") ||
//                status.equalsIgnoreCase(getString(R.string.refunded)) ||
//                status.equalsIgnoreCase(getString(R.string.partialrefunded))) {
//            product_Amount = amount;
//            System.out.println("[HistoryDetails] product_Amount: " + product_Amount);
//        } else {
//            product_Amount = amount;
//        }

        product_Surcharge = Surcharge;
        product_merRequestAmt = merRequestAmt;
        product_PayMethod = print_payMethod;
        product_PayStatus = PayStatus;
        product_CardNo = cardNo;
        product_merchantRef = merRef;
        product_paymentRef = payRef;
        product_transactionDate = finalDate;

        product = new TreeMap<String, String>();
        product.put("OperatorNumber", product_OperatorNumber);
        product.put("PayType", product_payType);
        product.put("CurrCode", product_CurrCode);
        product.put("Amount", edtAmount.getText().toString());
        product.put("Surcharge", product_Surcharge);
        product.put("MerRequestAmt", product_merRequestAmt);
        product.put("PayMethod", product_PayMethod);
        product.put("PayStatus", product_PayStatus);
        product.put("CardNo", product_CardNo);
        product.put("MerchantRef", product_merchantRef);
        product.put("PaymentRef", product_paymentRef);
        product.put("TransactionDate", product_transactionDate);

		// Merchant Logo
		Bitmap initbitmap = GlobalFunction.getMerLogo(HistoryDetails.this);

        printUtil = new PrintUtil(HistoryDetails.this, printeraddress, printername, info, product, initbitmap);

        dialog = new AlertDialog.Builder(HistoryDetails.this);
        dialog.setTitle(R.string.Print);
        dialog.setItems(printCopy, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int which) {
                switch (which) {
                    case 0:
                        copy = getString(R.string.print_customerCopy);
                        break;
                    case 1:
                        copy = getString(R.string.print_merchantCopy);
                        break;
                    default:
                }

                info_copy = copy;
                info.put("copy", info_copy);
                printUtil.sends();
            }
        });
        alertDialog = dialog.create();
        alertDialog.show();

    }

    private void printBBLReceipt() {
        String print_payMethod = "";

        //initialize
        final String[] printCopy = {getResources().getString(R.string.print_customerCopy), getResources().getString(R.string.print_merchantCopy)};

        Date transactionDate = null;

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd,yyyy");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        try {
            transactionDate = dateFormat4.parse(voidTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

        int midlength = 15 - bankMid.length();

        if(midlength < 15){

            String add0 = "";
            for(int i = 0; i < midlength; i++){
                add0 += "0";
            }

            bankMid = add0 + bankMid;
        }

        if(!cardNo.isEmpty() && cardNo != null ){

            int cardNoLength = cardNo.length();
            String cardNo1 = cardNo.substring(0, 4);
            String cardNo2 = cardNo.substring(4, 8);
            String cardNo3 = cardNo.substring(8,12);
            String cardNo4 = cardNo.substring(12, cardNoLength);

            cardNo = cardNo1 + " " + cardNo2 + " " + cardNo3 + " " + cardNo4;
        }

        product_tid = bankTid;
        product_merid = bankMid;
        product_batchno = batchNo;
        product_traceno = merRef;
        product_host = fromHexString(host);
        product_stan = STAN;
        product_walletcard = cardNo;
        product_transactionDate = txnDate;
        product_time = txnTime;
        product_apprcode = authId;
        product_refNo = bankRef;
        product_CurrCode = currency;
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
        Bitmap initbitmap = GlobalFunction.getMerLogo(HistoryDetails.this);

        printUtil = new PrintUtil(HistoryDetails.this, printeraddress, printername, info, product, initbitmap);

        dialog = new AlertDialog.Builder(HistoryDetails.this);
        dialog.setTitle(R.string.Print);
        dialog.setItems(printCopy, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int which) {
                // TODO Auto-generated method stub

                switch (which) {
                    case 0:
                        copy = getString(R.string.print_customerCopy);
                        break;
                    case 1:
                        copy = getString(R.string.print_merchantCopy);
                        break;
                    default:
                }

                info_copy = copy;
                info.put("copy", info_copy);
                printUtil.sendAlipayTH();
            }
        });
        alertDialog = dialog.create();
        alertDialog.show();
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

            if (CheckLogout) {

                /* do logout */
                Log.d("OTTO", "åšç™»å‡ºæ“ä½œ" + this.getClass());
                //logout flag change to true
                CheckLogout = true;
//                autologout();
            } else {
                if (timePeriodSecond > SESSION_EXPIRED) {
                    /* do logout */
                    Log.d("OTTO", "åšç™»å‡ºæ“ä½œ" + this.getClass());
                    //logout flag change to true
                    CheckLogout = true;
//                    autologout();
                } else {
                    Log.d("OTTO", "æ²¡æœ‰è¶…è¿‡è§„å®šæ—¶é•¿" + this.getClass());
                }
            }

            if (!CheckLogout) {
                //check no opt time per 'CheckInterval' sencond
                CheckTimeOutHandler.postDelayed(checkTimeOutTask, CheckInterval);
            }
        }
    };

    //listen touch movement
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("OTTO", "History_Item onTouchEvent checklogout:" + CheckLogout);
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
//        SharedPreferences.Editor prefedit = pref.edit();
//        prefedit.putBoolean(Constants.CheckLogout, true);
//        prefedit.putBoolean(Constants.IsAutoLogout, true);
//        prefedit.putBoolean("user_logged_in", false);
//        prefedit.commit();
//        if (!pref.getBoolean("remember", false)) {
//            SharedPreferences merdetails = getApplicationContext().getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
//            SharedPreferences.Editor meredit = merdetails.edit();
//            meredit.clear();
//            meredit.commit();
//            prefedit.clear();
//            prefedit.commit();
//            meredit.putString(Constants.pref_MerName, getIntent().getStringExtra(Constants.MERNAME));
//            meredit.commit();
//        }
//        Intent intent = new Intent();
//        intent.setClass(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("EXIT", true);
//        startActivity(intent);
//        finish();
//    }

    @Override
    protected void onResume() {

        SharedPreferences Resume_prefsettings = getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
        String SurCalstat = (Resume_prefsettings.getString(Constants.pref_surcharge_calculation, Constants.default_surcharge_calculation));
        SharedPreferences pref_sur = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String mdr = pref_sur.getString(Constants.mer_mdr, "");
        if ("OFF".equals(SurCalstat) || mdr == null || "".equals(mdr)) {
            hasmdr = false;
        } else if ("OFF".equals(SurCalstat) || Double.valueOf(mdr) <= 0) {
            hasmdr = false;
        } else if ("ON".equals(SurCalstat) && Double.valueOf(mdr) > 0) {
            hasmdr = true;
        } else {
            hasmdr = false;
        }

        if (!hasmdr) {
            edtSurcharge.setVisibility(GONE);
            edtSur_CurrCode.setVisibility(GONE);
            lblSurcharge.setVisibility(GONE);
        } else {
            edtSurcharge.setVisibility(VISIBLE);
            edtSur_CurrCode.setVisibility(VISIBLE);
            lblSurcharge.setVisibility(VISIBLE);
        }

        //start check timeout thread when activity show
        CheckTimeOutHandler.postAtTime(checkTimeOutTask, CheckInterval);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //stop check timeout thread when activity no show
        CheckTimeOutHandler.removeCallbacks(checkTimeOutTask);
        super.onPause();
    }
    //-------------------------------------auto logout-------------------------------------//


    @Override
    public boolean onSupportNavigateUp() {

        System.out.println("HistoryDetails onSupportNavigateUp: edited " + edited);
        if (edited == true) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
            return true;
        } else {
            Intent returnIntent = new Intent();
            setResult(2, returnIntent);
            finish();
            return true;
        }
    }

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

    public static void printCondition(String print, String length) {

        int len = Integer.parseInt(length)-2;

        for(int i = 0; i < len; i ++ ) {
            customerCopy += print;
            merchantCopy += print;
        }

    }

    @Override
    public void onBackPressed() {

        System.out.println("HistoryDetails onBackPressed: edited " + edited);

        if (edited == true) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            Intent returnIntent = new Intent();
            setResult(2, returnIntent);
            finish();
        }

        super.onBackPressed();
    }
}