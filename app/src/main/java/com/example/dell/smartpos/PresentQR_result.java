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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.smartpos.Printer.PAX.GetObj;
import com.example.dell.smartpos.Printer.PrintUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PresentQR_result extends AppCompatActivity {

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

    private ImageView payImg;
    private TextView payMsg;
    private TextView payMethod;
    private TextView merchantName;
    private TextView txtamount;
    private TextView txtmerRef;
    private TextView wxTXTime;

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

    String merId;
    String successCode;
    String paymentMethod;
    String merRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.present_qr);
        setContentView(R.layout.activity_present_qr_result);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        step1 = (TextView) findViewById(R.id.progress_step_1);
        step2 = (TextView) findViewById(R.id.progress_step_2);
        step3 = (TextView) findViewById(R.id.progress_step_3);
        step4 = (TextView) findViewById(R.id.progress_step_4);

        step1.setText(R.string.enter_amount);
        step2.setText(R.string.select_qr_payment);
        step3.setText(R.string.generate_qr);
        step4.setText(R.string.payment_result);

        step2.setTypeface(null, Typeface.BOLD);
        step3.setTypeface(null, Typeface.BOLD);
        step4.setTypeface(null, Typeface.BOLD);


        Locale locale = getResources().getConfiguration().locale;
        language = locale.getLanguage();

        final String[] printCopy = {getResources().getString(R.string.print_customerCopy), getResources().getString(R.string.print_merchantCopy)};

        //---------for autologout---------//
        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout, false);
        lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
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
        amount = intent.getStringExtra(Constants.AMOUNT) == null ? "0" : intent.getStringExtra(Constants.AMOUNT);
        merrequestamt = intent.getStringExtra(Constants.MERREQUESTAMT) == null ? "" : intent.getStringExtra(Constants.MERREQUESTAMT);
        Surcharge = intent.getStringExtra(Constants.SURCHARGE) == null ? "" : intent.getStringExtra(Constants.SURCHARGE);
        Currcode = intent.getStringExtra(Constants.CURRCODE) == null ? "HKD" : intent.getStringExtra(Constants.CURRCODE);
        TXTime = intent.getStringExtra(Constants.TXTIME) == null ? "2018-11-16 10:00:00.0" : intent.getStringExtra(Constants.TXTIME);
        errMsg = intent.getStringExtra(Constants.ERRMSG) == null ? "" : intent.getStringExtra(Constants.ERRMSG);

        payType = intent.getStringExtra(Constants.PAYTYPE) == null ? "" : intent.getStringExtra(Constants.PAYTYPE);
        pMethod = intent.getStringExtra(Constants.PAYMETHOD) == null ? "" : intent.getStringExtra(Constants.PAYMETHOD);
        operatorId = intent.getStringExtra(Constants.OPERATORID) == null ? "" : intent.getStringExtra(Constants.OPERATORID);
        cardNo = intent.getStringExtra(Constants.CARDNO) == null ? "" : intent.getStringExtra(Constants.CARDNO);

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
//        if(mdr==null||"".equals(mdr)||("T".equals(hideSurcharge))){
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
        showErrMsg = (TextView) findViewById(R.id.paymentMsg);
        System.out.println("KJ---merRef:" + merchant_ref_no);
        showMerchantName.setText(merName);
        showMerchantRef.setText(merchant_ref_no);
        showPayRef.setText(payRef);
        if ("T".equals(hideSurcharge)) {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            amount = formatter.format(Double.parseDouble(amount));
            showAmount.setText(amount);
        } else {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            amount = formatter.format(Double.parseDouble(amount));
            showAmount.setText(amount);
        }
        showSurcharge.setText(Surcharge);
        showCurrcode.setText(Currcode);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        Date transactionDate = null;
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

        if (pMethod.equalsIgnoreCase("OEPAYOFFL")) {
            pMethod = getString(R.string.print_payMethod_oepayOffline);
        } else if (pMethod.equalsIgnoreCase("BOOSTOFFL")) {
            pMethod = getString(R.string.print_payMethod_boostOffline);
        }

        showQRPayment.setText(pMethod);

        //pay status is success
        PayStatus = getString(R.string.accepted);
        showStatus.setText(PayStatus);

        if (pMethod.equalsIgnoreCase("FPS")) {
            setTitle(R.string.fps_menu);
            step1.setText(R.string.enter_amount);
            step2.setText(R.string.generate_qr);
            step3.setText(R.string.payment_result);
            step4.setVisibility(View.GONE);
            findViewById(R.id.circle4).setVisibility(View.GONE);
            findViewById(R.id.line3).setVisibility(View.GONE);
            findViewById(R.id.gap3).setVisibility(View.GONE);

            step2.setTypeface(null, Typeface.BOLD);
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        amount = amount.replaceAll(",", "");
        final String txt_amount = formatter.format(Double.parseDouble(amount));

        Date curDate = new Date();
        String strDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(curDate);

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
            printReceipt("Merchant");
            newpayment.setEnabled(false);

            if (printmode.equals(Constants.PRINT_MODE1)) {
                threadRunObj = new Runnable() {
                    public void run() {
                        printReceipt("Consumer");
                        newpayment.setEnabled(true);
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
            printReceipt("Merchant");
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
                printReceipt("Consumer");
            }
        });

        //Present QR
      /*  payMsg = (TextView) findViewById(R.id.paymentMsg);
        payMethod = (TextView) findViewById(R.id.wxPaymentMethod);
        merchantName = (TextView) findViewById(R.id.txtMerName);
        txtamount = (TextView) findViewById(R.id.wxAmount);
        txtmerRef = (TextView) findViewById(R.id.wxMerchantRef);
        wxTXTime = (TextView) findViewById(R.id.wxTXTime);

        Intent resultIntent = getIntent();
        merName = resultIntent.getStringExtra("merName");
        successCode = resultIntent.getStringExtra("successCode");
        paymentMethod = resultIntent.getStringExtra("payMethod");
        merRef = resultIntent.getStringExtra("merchantRef");
        amount = resultIntent.getStringExtra("merRequestAmt");*/


        if (!pMethod.equalsIgnoreCase("OEPAYOFFL") && !pMethod.equalsIgnoreCase("OEPAY Offline")
                && !pMethod.equalsIgnoreCase("BOOSTOFFL") && !pMethod.equalsIgnoreCase("Boost Offline")
                && !pMethod.equalsIgnoreCase(getString(R.string.print_payMethod_promptpayOffline)) && !pMethod.equalsIgnoreCase(getString(R.string.print_payMethod_grabpayOffline))) {

            if (success_code.equals("0")) {
//            payImg.setImageResource(R.drawable.ic_payment_successful);
                showErrMsg.setText(R.string.payment_success);
                showQRPayment.setText(pMethod);
                showMerchantName.setText(merName);
                showAmount.setText(txt_amount);
                showMerchantRef.setText(merchant_ref_no);
                showTXTime.setText(strDate);
            } else if (success_code.equals("1")) {
//            payImg.setImageResource(R.drawable.ic_payment_failure);
                showErrMsg.setText(R.string.rs_aop_ACQ_PAYMENT_FAIL);
                showQRPayment.setText(pMethod);
                showMerchantName.setText(merName);
                showAmount.setText(amount);
                showMerchantRef.setText(merchant_ref_no);
            }

            threadRunObj = new Runnable() {
                public void run() {
                    returnMainMenu();
                }
            };
            threadHandler = new Handler();
            threadHandler.postDelayed(threadRunObj, 10000);
        }

    }

    public void returnMainMenu() {
        Constants.LIST_ACTIVITY = 0;
        Constants.tabSuccess = 0;
        Intent intentNew = new Intent();
        intentNew.setClass(PresentQR_result.this, MainActivity.class);
        intentNew.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentNew);
        setResult(RESULT_OK);
        System.out.println("---kj2---");
        finish();
    }

    public void printReceipt(String receiptType) {

        if ("BOOSTOFFL".equals(pMethod)) {
            pMethod = getString(R.string.print_payMethod_boostOffline);
        } else if ("OEPAYOFFL".equals(pMethod)) {
            pMethod = getString(R.string.print_payMethod_oepayOffline);
        } else if ("ALIPAYCNOFFL".equals(pMethod)) {
            pMethod = getString(R.string.print_payMethod_alipayOffline);
        } else if ("WECHATOFFL".equals(pMethod)) {
            pMethod = getString(R.string.print_payMethod_wechatOffline);
        } else if ("PROMPTPAYOFFL".equals(pMethod)) {
            pMethod = getString(R.string.print_payMethod_promptpayOffline);
        } else if ("GRABPAYOFFL".equals(pMethod)) {
            pMethod = getString(R.string.print_payMethod_grabpayOffline);
        }

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
        amount = formatter.format(Double.parseDouble(amount.replaceAll(",", "")));

        product_OperatorNumber = operatorId;
        product_payType = payType;
        product_CurrCode = Currcode;
        product_Amount = amount;
        product_Surcharge = Surcharge;
        product_merRequestAmt = merrequestamt;
        product_PayMethod = pMethod;
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
        Bitmap initbitmap = GlobalFunction.getMerLogo(PresentQR_result.this);

        printUtil = new PrintUtil(PresentQR_result.this, printeraddress, printername, info, product, initbitmap);

//                dialog = new AlertDialog.Builder(ScanQR_Success.this);
//                dialog.setTitle(R.string.Print);
//                dialog.setItems(printCopy, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int which) {
//
//
//                        switch (which){
//                            case 0:
//                                copy = getString(R.string.print_customerCopy);
//                                break;
//                            case 1:
//                                copy = getString(R.string.print_merchantCopy);
//                                break;
//                            default:
//                        }

        if (receiptType.equals("Consumer")) {
            copy = getString(R.string.print_customerCopy);
        } else {
            copy = getString(R.string.print_merchantCopy);
        }

        info_copy = copy;
        info.put("copy", info_copy);
        printUtil.sends();
//                    }});
//                alertDialog= dialog.create();
//                alertDialog.show();

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
            /*curTime - lastest opt time = no opt time*/
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

//    @Override
//    public boolean onSupportNavigateUp() {
//        finish();
//        return true;
//    }


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
