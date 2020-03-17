package com.example.dell.smartpos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.visa.mvisa.QRCodeTag;
import com.visa.mvisa.generator.InputInvalidException;
import com.visa.mvisa.generator.QrCodeDataGenerator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class PresentQRPayment_3 extends AppCompatActivity {

    final Timer updateTimer = new Timer();
    public int QRcodeWidth;
    public int timeout = 0;
    String merchantId = "";
    ImageView merchantQR;
    Bitmap bitmap;
    Boolean toastError = false;
    Boolean checkOE = false;
    ProgressDialog progressDialog;
    Handler CheckStatusHandler = new Handler();
    Timer checkOePayStatus = new Timer();
    Boolean checkBoost = false;
    Timer checkBoostStatus = new Timer();
    private TextView editAmount, editMerRefNo, edtCurrCode, QRStatus;
    private TextView editPayCode;
    private ImageView ic_payMethod;
    private Button btnCancel;
    //    private Button btnCheckStatus;
    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;
    private String amount;
    private String merRequestAmt;
    private String surcharge;
    private String mdr;
    private String currcode1;
    private String currCode;
    private String merchantRef;
    private String payType = "";
    private String remark;
    private String hideSurcharge;
    private String surC;
    private String merId;
    private String merName;
    private String payMethod;
    private String orderId;
    private String trxNo;
    private String operatorId;
    private String gatewayRef;
    private String businessDate;
    private String oepayToken;
    private String boostToken;
    private AlertDialog alert;
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            System.out.println("---OEPAY check status2");
            checkOctopusStatus();

            CheckStatusHandler.postDelayed(this, 5000);
        }
    };


    //Inquiry QR
    Timer inquiryQRStatus = new Timer();

    boolean presentQRTimeout = false;
    boolean isQRCancelled = false;

    Date dateNow;
    Date dateExpired;

    ScreenReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrpayment_3);
        setTitle(R.string.present_qr);

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

        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        QRcodeWidth = (int) (displaymetrics.densityDpi * 1.4);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        merRequestAmt = intent.getStringExtra("MerRequestAmt");
        surcharge = intent.getStringExtra("Surcharge");
        mdr = intent.getStringExtra("Mdr");
        currcode1 = intent.getStringExtra("currCode1");
        merchantRef = intent.getStringExtra("MerchantRef");
        currCode = intent.getStringExtra("currCode");
        remark = intent.getStringExtra("remark");
        hideSurcharge = intent.getStringExtra("hideSurcharge");
        surC = intent.getStringExtra("surC");
        merName = intent.getStringExtra(Constants.MERNAME);
        merId = intent.getStringExtra(Constants.MERID);
        payMethod = intent.getStringExtra(Constants.PAYMETHOD);
        payType = intent.getStringExtra(Constants.PAYTYPE);
        operatorId = intent.getStringExtra(Constants.OPERATORID);

        if (payMethod.equalsIgnoreCase("fps")) {
            setTitle(R.string.fps_menu);
            step1.setText(R.string.enter_amount);
            step2.setText(R.string.generate_qr);
            step3.setText(R.string.payment_result);
            step4.setVisibility(View.GONE);
            findViewById(R.id.circle3).setBackgroundResource(R.drawable.progress_circle_background_2);
            findViewById(R.id.line2).setBackgroundColor(Color.parseColor("#414141"));
            findViewById(R.id.circle4).setVisibility(View.GONE);
            findViewById(R.id.line3).setVisibility(View.GONE);
            findViewById(R.id.gap3).setVisibility(View.GONE);

            step2.setTypeface(null, Typeface.BOLD);
        }

        if(payMethod.equalsIgnoreCase("Boost")) {
            payMethod = "BOOSTOFFL";
        } else if (payMethod.equalsIgnoreCase("PromptPay")) {
            payMethod = "PROMPTPAYOFFL";
        } else if (payMethod.equalsIgnoreCase("O!ePay")) {
            payMethod = "OEPAYOFFL";
        } else if (payMethod.equalsIgnoreCase("GrabPay")) {
            payMethod = "GRABPAYOFFL";
        }

        Calendar c = Calendar.getInstance();
        businessDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

        //        btnCheckStatus = (Button) findViewById(R.id.btnCheck_status);
        btnCancel = (Button) findViewById(R.id.btnCancel);


        editAmount = (TextView) findViewById(R.id.editAmount);
        //editMerRefNo = (TextView) findViewById(R.id.editmerRefNo);
        edtCurrCode = (TextView) findViewById(R.id.edtCurrCode);
        QRStatus = (TextView) findViewById(R.id.QRStatus);
        ic_payMethod = (ImageView) findViewById(R.id.ic_payMethod);
        editPayCode = (TextView) findViewById(R.id.edtPayCode);
        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        String amount = formatter.format(Double.parseDouble(merRequestAmt));

        editAmount.setText(amount);
        //editMerRefNo.setText(merchantRef);
        edtCurrCode.setText(currCode);

        merchantQR = (ImageView) findViewById(R.id.merchantQR);

        //--Edited 25/07/18 by KJ--//
        DesEncrypter encrypt;
        try {
            encrypt = new DesEncrypter(merName);
            merchantId = encrypt.encrypt(merId);
        } catch (Exception e2) {
            e2.printStackTrace();
        }

        String whereargs[] = {merchantId};

        SharedPreferences pref = this.getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);

        String oepayTimeout = pref.getString(Constants.pref_oepay_timeout, Constants.default_payment_timeout);
        String boostTimeout = pref.getString(Constants.pref_boost_timeout, Constants.default_payment_timeout);
        String promptpayTimeout = pref.getString(Constants.pref_promptpay_timeout, Constants.default_payment_timeout);
        String grabpayTimeout = pref.getString(Constants.pref_grabpay_timeout, Constants.default_payment_timeout);

        if (payMethod.equalsIgnoreCase("PROMPTPAYOFFL")) {
            ic_payMethod.setImageResource(R.drawable.promptpay);

            generateKBank(payMethod);

            timeout = Integer.parseInt(promptpayTimeout);

            //            ProgressDialog progress = new ProgressDialog(this);
            //            progress.setMessage("Loading...");
            //            new generateQRTask(progress).execute("");
        } else if (payMethod.equalsIgnoreCase("mVisa")) {
            ic_payMethod.setImageResource(R.drawable.ic_mvisa);

            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            new generateQRTask(progress).execute("");
            //            testEMVMapBasedGenerator();
        } else if (payMethod.equalsIgnoreCase("OEPAYOFFL")) {

            if (oepayTimeout != null) {
                timeout = Integer.parseInt(oepayTimeout);
            } else {
                timeout = 3;
            }

            //            findViewById(R.id.view_checkBtn).setVisibility(View.GONE);
            //            btnCheckStatus.setVisibility(View.GONE);

            gatewayRef = "PG" + (new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));

            OctopusPay();
            ic_payMethod.setImageResource(R.drawable.ic_olepay);
        } else if (payMethod.equalsIgnoreCase("fps")) {
            ic_payMethod.setImageResource(R.drawable.ic_fps);

            Bitmap fpsqr = BitmapFactory.decodeResource(getResources(), R.drawable.fps_qr);
            merchantQR.setImageBitmap(fpsqr);
        } else if (payMethod.equalsIgnoreCase("MasterWallet")) {
            ic_payMethod.setImageResource(R.drawable.ic_masterwallet);

            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            new generateQRTask(progress).execute("");
        } else if (payMethod.equalsIgnoreCase("PayMe")) {
            ic_payMethod.setImageResource(R.drawable.ic_payme);

            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            new generateQRTask(progress).execute("");
        } else if (payMethod.equalsIgnoreCase("PayNow")) {
            ic_payMethod.setImageResource(R.drawable.ic_paynow);

            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            new generateQRTask(progress).execute("");

        } else if (payMethod.equalsIgnoreCase("BharatPay")) {
            ic_payMethod.setImageResource(R.drawable.ic_bharatpay);

            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            new generateQRTask(progress).execute("");

        } else if (payMethod.equalsIgnoreCase("masterWallet")) {
            ic_payMethod.setImageResource(R.drawable.ic_masterwallet);
            generateQR();
        } else if (payMethod.equalsIgnoreCase("BOOSTOFFL")) {
            if (boostTimeout != null) {
                timeout = Integer.parseInt(boostTimeout);
            } else {
                timeout = 3;
            }

            //            findViewById(R.id.view_checkBtn).setVisibility(View.GONE);
            //            btnCheckStatus.setVisibility(View.GONE);

            ic_payMethod.setImageResource(R.drawable.ic_boost);
            BoostQR();

        } else if (payMethod.equalsIgnoreCase("GRABPAYOFFL")) {

            ic_payMethod.setImageResource(R.drawable.ic_grabpay);
            timeout = Integer.parseInt(grabpayTimeout);
            generateKBank(payMethod);
        }

        // Cancel QR button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payMethod.equalsIgnoreCase("OEPAYOFFL")) {
                    checkOePayStatus.cancel();
                    cancelOctopus();
                } else if (payMethod.equalsIgnoreCase("BOOSTOFFL")) {
                    checkBoostStatus.cancel();
                    cancelBoost(0);

                } else if (payMethod.equalsIgnoreCase("PROMPTPAYOFFL")) {
                    cancelQR(orderId);

                } else if (payMethod.equalsIgnoreCase("GRABPAYOFFL")) {
                    cancelQR(orderId);

                } else {
                    finish();
                    Intent intent = new Intent(PresentQRPayment_3.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        //        btnCheckStatus.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                checkOePayStatus.cancel();
        //
        //                if (merId.equalsIgnoreCase("560200335")) {
        //                    Intent intent = new Intent(PresentQRPayment_3.this, PresentQR_result.class);
        //                    intent.putExtra("successCode", "0");
        //                    intent.putExtra("payMethod", payMethod);
        //                    intent.putExtra("merName", merName);
        //                    intent.putExtra("merRequestAmt", merRequestAmt);
        //                    intent.putExtra("merchantRef", merchantRef);
        //
        //                    startActivity(intent);
        //
        //                } else if (merId.equalsIgnoreCase("560200303")) {
        //                    Intent intent = new Intent(PresentQRPayment_3.this, PresentQR_failResult.class);
        //                    intent.putExtra("successCode", "1");
        //                    intent.putExtra("payMethod", payMethod);
        //                    intent.putExtra("merName", merName);
        //                    intent.putExtra("merRequestAmt", merRequestAmt);
        //                    intent.putExtra("merchantRef", merchantRef);
        //
        //                    startActivity(intent);
        //                }
        ////                if (payMethod.equalsIgnoreCase("olePay")) {
        ////                    checkOctopusStatus();
        ////
        ////                } else {
        ////                    PresentQRPayment_3.CheckStatusTask checkStatus = new PresentQRPayment_3.CheckStatusTask(
        ////                            PresentQRPayment_3.this,
        ////                            getPrefPayGate());
        ////                    checkStatus.execute(
        ////                            merId,
        ////                            merRequestAmt,
        ////                            merchantRef,
        ////                            "banana",
        ////                            "banana",
        ////                            "banana",
        ////                            payMethod,
        ////                            merName);
        ////                }
        //            }
        //        });

        CheckStatusHandler = new Handler();


        //        checkOePayStatus.scheduleAtFixedRate(new TimerTask() {
        //            @Override
        //            public void run() {
        //                checkOctopusStatus();
        //                System.out.println("---OEPAY check status");
        //
        //            }
        //        }, 0, 10000);//put here time 1000 milliseconds=1 second

        if (payMethod.equalsIgnoreCase("OEPAYOFFL")) {
            checkOePayStatus.schedule(new TimerTask() {
                @Override
                public void run() {

                    if (checkOE) {
                        System.out.println("---OEPAY check status1");
                        checkOctopusStatus();
                    }


                }
            }, 0, 5000);
        }


        //        CheckStatusHandler.postDelayed(runnableCode, 5000);

        if (payMethod.equalsIgnoreCase("BOOSTOFFL")) {
            checkBoostStatus.schedule(new TimerTask() {
                @Override
                public void run() {

                    if (checkBoost) {
                        System.out.println("---Boost check status1");
                        checkBoostStatus();
                    }
                }
            }, 0, 5000);
        }

        // Start ScreenReceiver
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, intentFilter);
    }

    //    public void onStop() {
    //        try {
    //            unregisterReceiver(br);
    //        } catch (Exception e) {
    //            // Receiver was probably already stopped in onPause()
    //        }
    //        super.onStop();
    ////        updateTimer.cancel();
    //    }
    //
    //    @Override
    //    public void onPause() {
    //        super.onPause();
    //        unregisterReceiver(br);
    //    }

    @Override
    public void onDestroy() {

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }

        updateTimer.cancel();

        //        stopService(new Intent(this, TimerService.class));
        super.onDestroy();
    }

    private void generateQR() {
        try {

            PromptPayGenerate qr = new PromptPayGenerate();

            qr.setPointOfInitiationMethod("12");
            qr.setMerchantIdentifier(qr.getAid(), merId, merchantRef, merchantRef);
            qr.setTransactionCurrencyCode(currCode);
            qr.setTransactionAmount(amount.toString());
            qr.setCountryCode(currCode);
            qr.setMerchantName(merName);
            qr.setAdditionalDataFieldTemplate(
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    payMethod, //banker terminal
                    "",
                    "");
            qr.generateCRC();

            String rawMsg = "";
            rawMsg = qr.construct(true);

            bitmap = promptPayQR(
                    rawMsg
            );

            merchantQR.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    // public void generateSuccess(String rawMsg) {
    //     try {
    //         bitmap = promptPayQR(
    //                 rawMsg
    //         );
    //         merchantQR.setImageBitmap(bitmap);
    //     } catch (WriterException e) {
    //         e.printStackTrace();
    //     }
    // }

    Bitmap promptPayQR(
            String rawMessage) throws WriterException {
        BitMatrix bitMatrix;
        try {
            // Create the ByteMatrix for the QR-Code that encodes the given String
            Hashtable hintMap = new Hashtable();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            bitMatrix = qrCodeWriter.encode(
                    rawMessage,
                    BarcodeFormat.QR_CODE,
                    QRcodeWidth,
                    QRcodeWidth,
                    hintMap);


        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, QRcodeWidth, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    @Override
    public boolean onSupportNavigateUp() {

        if (payMethod.equalsIgnoreCase("OEPAYOFFL") || payMethod.equalsIgnoreCase("BOOSTOFFL") || payMethod.equalsIgnoreCase("PROMPTPAYOFFL") || payMethod.equalsIgnoreCase("GRABPAYOFFL")) {
            confirmCancelTrx();
        } else {
            finish();
        }

        //        Intent intent = new Intent(PresentQRPayment_3.this, MainActivity.class);
        //        startActivity(intent);
        return true;
    }


    private HashMap<String, Object> createMerchantDataReqestMap() {
        //Request object
        HashMap<String, Object> merchantQrDataRequest = new HashMap<>();

        //populate request data
        merchantQrDataRequest.put("payloadFormatIndicator", "01");
        merchantQrDataRequest.put("pointOfInitiation", "12");
        merchantQrDataRequest.put("mVisaMerchantID", "47613617");
        merchantQrDataRequest.put("merchantCategoryCode", "5454");
        merchantQrDataRequest.put("currencyCode", currcode1);
        merchantQrDataRequest.put("transactionAmount", amount);
        merchantQrDataRequest.put("countryCode", "IN");
        merchantQrDataRequest.put("merchantName", "Kipling Cafe");
        merchantQrDataRequest.put("cityName", "Chennai");
        //      merchantQrDataRequest.put("convenienceFeeIndicator", "03");
        //      merchantQrDataRequest.put("convenienceFeePercentage", "10");
        //Create separate HashMaps for subtags
        HashMap<String, String> additionalDataFieldTag = new HashMap<>();
        //populate subtag values
        additionalDataFieldTag.put("01", "billID");
        additionalDataFieldTag.put("02", "9940615178");
        //Populate subtags into main tags
        merchantQrDataRequest.put(String.valueOf(QRCodeTag.ADDITIONAL_DATA_FIELD_TEMPLATE), additionalDataFieldTag);
        return merchantQrDataRequest;
    }

    public Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_8888);

        bitmap.setPixels(pixels, 0, QRcodeWidth, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public void success(String successcode,
                        String merchant_ref_no,
                        String PayRef,
                        String amount,
                        String merRequestAmt,
                        String Surcharge,
                        String Currcode,
                        String Time,
                        String errcode,
                        String merchantid,
                        String mername,
                        String payType,
                        String pMethod,
                        String operatorId,
                        String cardNo) {

        if (pMethod.equalsIgnoreCase("OEPAYOFFL") || pMethod == null || pMethod.equals("")) {
            checkOePayStatus.cancel();
        } else if (pMethod.equalsIgnoreCase("BOOSTOFFL")) {
            checkBoostStatus.cancel();
        }

        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        toneGen1.startTone(ToneGenerator.TONE_PROP_PROMPT, 300);

        incMerRef();
        //        Toast.makeText(ScanQRPayment_2.this, R.string.scan_success, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();

        if (!"".equals(amount)) {
            amount = String.valueOf(Double.valueOf(amount));
        }
        if (!"".equals(Surcharge)) {
            Surcharge = String.valueOf(Double.valueOf(Surcharge));
        }
        if (!"".equals(merRequestAmt)) {
            merRequestAmt = String.valueOf(Double.valueOf(merRequestAmt));
        }

        Log.d("OTTO", "scanback amount:" + amount);
        Log.d("OTTO", "scanback surcharge:" + Surcharge);
        Log.d("OTTO", "scanback merRequestAmt:" + merRequestAmt);
        intent.putExtra(Constants.SUCCESS_CODE, successcode);
        intent.putExtra(Constants.MERCHANT_REF, merchant_ref_no);
        intent.putExtra(Constants.PAYREF, PayRef);
        intent.putExtra(Constants.AMOUNT, amount);
        intent.putExtra(Constants.MERREQUESTAMT, merRequestAmt);
        intent.putExtra(Constants.SURCHARGE, Surcharge);
        intent.putExtra(Constants.CURRCODE, Currcode);
        intent.putExtra(Constants.TXTIME, Time);
        intent.putExtra(Constants.ERRMSG, errcode);
        intent.putExtra(Constants.MERID, merchantid);
        intent.putExtra(Constants.MERNAME, mername);
        intent.putExtra(Constants.PAYTYPE, payType);
        intent.putExtra(Constants.PAYMETHOD, pMethod);
        intent.putExtra(Constants.OPERATORID, operatorId);
        intent.putExtra(Constants.CARDNO, cardNo);
        intent.putExtra(Constants.pref_hideSurcharge, hideSurcharge);
        System.out.println("OTTO-----" + "Payment Tab success->   successcod:" + successcode + ",Ref:" + merchant_ref_no + ",PayRef:" + PayRef + ",amount:" + amount + ",merRequestAmt:" + merRequestAmt + ",surcharge:" + Surcharge + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode + ",merchantid:" + merchantid + ",mername:" + mername + ",payType:" + payType + ",pMethod:" + pMethod + ",operatorId:" + operatorId + ",cardNo:" + cardNo);
        intent.setClass(PresentQRPayment_3.this, PresentQR_result.class);

        setResult(RESULT_OK);

        startActivity(intent);
        finish();
    }

    public void fail(String successcode,
                     String merchantid,
                     String mername,
                     String paytype,
                     String pMethod,
                     String operatorid,
                     String cardno,
                     String merchant_ref_no,
                     String PayRef,
                     String amount,
                     String merRequestAmt,
                     String Surcharge,
                     String Currcode,
                     String Time,
                     String errcode) {

        if (pMethod.equalsIgnoreCase("OEPAYOFFL") || pMethod == null || pMethod.equals("")) {
            checkOePayStatus.cancel();
        } else if (pMethod.equalsIgnoreCase("BOOSTOFFL")) {
            checkBoostStatus.cancel();
        }

        if (amount != null) {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            amount = formatter.format(Double.parseDouble(amount));
        }

        incMerRef();

        Intent intent = new Intent();

        intent.putExtra(Constants.MERID, merchantid);
        intent.putExtra(Constants.MERNAME, mername);
        intent.putExtra(Constants.PAYTYPE, paytype);
        intent.putExtra(Constants.PAYMETHOD, pMethod);
        intent.putExtra(Constants.OPERATORID, operatorid);
        intent.putExtra(Constants.CARDNO, cardno);
        intent.putExtra(Constants.SUCCESS_CODE, successcode);
        intent.putExtra(Constants.MERCHANT_REF, merchant_ref_no);
        intent.putExtra(Constants.PAYREF, PayRef);
        intent.putExtra(Constants.AMOUNT, amount);
        intent.putExtra(Constants.MERREQUESTAMT, merRequestAmt);
        intent.putExtra(Constants.SURCHARGE, Surcharge);
        intent.putExtra(Constants.CURRCODE, Currcode);
        intent.putExtra(Constants.TXTIME, Time);
        intent.putExtra(Constants.ERRMSG, errcode);
        intent.putExtra(Constants.pref_hideSurcharge, hideSurcharge);

        System.out.println("OTTO-----" + "Payment Tab fail->   successcod:" + successcode + ",merchantid:" + merchantid + ",mername:" + mername + ",paytype:" + paytype + ",pMethod:" + pMethod + ",operatorid:" + operatorid + ",cardno:" + cardno + ",Ref:" + merchant_ref_no + ",PayRef:" + PayRef + ",amount:" + amount + ",merRequestAmt:" + merRequestAmt + ",surcharge:" + Surcharge + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode);
        intent.setClass(PresentQRPayment_3.this, PresentQR_failResult.class);

        setResult(RESULT_OK);

        startActivity(intent);
        finish();
    }

    public String getPrefPayGate() {
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
        String prefpaygate = prefsettings.getString("PayGate", Constants.default_paygate);
        return prefpaygate;
    }

    //OCTOPUS
    public void OctopusPay() {
        progressDialog = new ProgressDialog(PresentQRPayment_3.this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        String userID = null;
        String merchantId1 = merId;
        String merName1 = merName;
        String orgMerchantId = merchantId1;
        DesEncrypter encrypt;
        String encMerchantId = "";
        DatabaseHelper db = new DatabaseHelper(this);
        try {
            encrypt = new DesEncrypter(merName1);
            encMerchantId = encrypt.encrypt(orgMerchantId);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String whereargs[] = {encMerchantId};
        String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
        db.close();
        if (dbuserID != null) {
            try {
                DesEncrypter encrypter = new DesEncrypter(merName1);
                userID = encrypter.decrypt(dbuserID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //-------------------get userID/operatorId end-------------------//


        OctopusQR octopusQR = new OctopusQR(PresentQRPayment_3.this, progressDialog, getPrefPayGate());
        //        octopusQR.execute(gatewayId, gatewayRef, businessDate, amount, currcode1, merRequestAmt, merId, merchantRef, payMethod, userID, surC);
        octopusQR.execute(gatewayRef, businessDate, amount, currcode1, merRequestAmt, merId, merchantRef, payMethod, userID, surC, String.valueOf(timeout));
    }

    private void testEMVMapBasedGenerator() {
        //MerchantQrDataRequest merchantQrDataRequest = createMerchantDataRequest();
        String tlv = "";

        try {
            tlv = QrCodeDataGenerator.generateQrCodeData(createMerchantDataReqestMap());

        } catch (InputInvalidException exception) {
            Log.d("TLV", "Error = " + exception.getMessage());
            System.out.print(exception.getMessage());
            return;
        }
        Log.d("TLV", "TLV = " + tlv);

        try {
            Bitmap bitmap = TextToImageEncode(tlv);
            merchantQR.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
    //    amount, merRequestAmt, Surcharge, currCode, MerchantRef, auth_code, pMethodalipayHK, merchantId, merName, userID, useSurcharge

    //Boost
    public void BoostQR() {
        progressDialog = new ProgressDialog(PresentQRPayment_3.this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        String userID = null;
        String merchantId1 = merId;
        String merName1 = merName;
        String orgMerchantId = merchantId1;
        DesEncrypter encrypt;
        String encMerchantId = "";
        DatabaseHelper db = new DatabaseHelper(this);
        try {
            encrypt = new DesEncrypter(merName1);
            encMerchantId = encrypt.encrypt(orgMerchantId);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String whereargs[] = {encMerchantId};
        String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
        db.close();
        if (dbuserID != null) {
            try {
                DesEncrypter encrypter = new DesEncrypter(merName1);
                userID = encrypter.decrypt(dbuserID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //-------------------get userID/operatorId end-------------------//

        BoostQR boostQR = new BoostQR(PresentQRPayment_3.this, progressDialog, getPrefPayGate());
        boostQR.execute(businessDate, amount, currcode1, merRequestAmt, merId, merchantRef, payMethod, userID, surC, String.valueOf(timeout));
    }

    public Bitmap generateOctopusQR(String landingURL, String orderId1, String payRef1) {

        try {
            orderId = orderId1;
            gatewayRef = payRef1;
            bitmap = TextToImageEncode(landingURL);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    //KBank
    public void generateKBank(String payMethod) {
        progressDialog = new ProgressDialog(PresentQRPayment_3.this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        String userID = null;
        String merchantId1 = merId;
        String merName1 = merName;
        String orgMerchantId = merchantId1;
        DesEncrypter encrypt;
        String encMerchantId = "";
        DatabaseHelper db = new DatabaseHelper(this);
        try {
            encrypt = new DesEncrypter(merName1);
            encMerchantId = encrypt.encrypt(orgMerchantId);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String whereargs[] = {encMerchantId};
        String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
        db.close();
        if (dbuserID != null) {
            try {
                DesEncrypter encrypter = new DesEncrypter(merName1);
                userID = encrypter.decrypt(dbuserID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //-------------------get userID/operatorId end-------------------//

        KBankQR kbankQR = new KBankQR(PresentQRPayment_3.this, progressDialog, getPrefPayGate());
        kbankQR.execute(merId, amount, merRequestAmt, currcode1, merchantRef, payType, payMethod, userID);
    }

    public void displayQR(Bitmap bitmap, String orderID, String QRRef, String QRRef2){

        merchantQR.setImageBitmap(bitmap);
        orderId = orderID;

        editPayCode.setText(QRRef);
        trxNo = QRRef;

        checkKBANKStatus(orderID);

//        inquiryQRStatus.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                checkKBANKStatus(orderId, false);
//            }
//        }, 0, 3000);
    }

    public Bitmap generateBoostQR(String base64QR, String orderId1, String payRef1) {

        try {
            orderId = orderId1;
            gatewayRef = payRef1;

            final byte[] decodedString = Base64.decode(base64QR, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            //            merchantQR.setImageBitmap(decodedByte);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public void displayQR(Bitmap bitmap, String token, final String expiryTime, String payRef, String gatewayRef1) {

        if (token.equals("null") || token == null || token.equals("")) {
            System.out.println("---KJ PresentQRPayment3: displaying QR1");
            orderId = payRef;
            gatewayRef = gatewayRef1;
            checkOePayStatus.cancel();
            System.out.println("---KJ order id=" + orderId + ";PGref=" + gatewayRef);
            AlertDialog.Builder builder = new AlertDialog.Builder(PresentQRPayment_3.this);
            builder.setTitle(getString(R.string.error));

            builder.setMessage(getString(R.string.error));
            builder.setPositiveButton(getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
            alert = builder.create();
            alert.setCancelable(false);
            if (!((Activity) this).isFinishing()) {
                alert.show();
            }
        } else {
            checkOE = true;
            System.out.println("---KJ PresentQRPayment3: displaying QR2");
            oepayToken = token;
            editPayCode.setText(oepayToken);

            merchantQR.setImageBitmap(bitmap);
            orderId = payRef;

            gatewayRef = gatewayRef1;
            System.out.println("---KJ order id=" + orderId + ";PGref=" + gatewayRef);
            //        final Timer updateTimer = new Timer();

            setTimeoutTimer();
        }
    }

    public void checkOctopusStatus() {
        System.out.println("---KJ PresentQRPayment3: checkOctopusStatus");

        //        progressDialog_OEPAY = new ProgressDialog(PresentQRPayment_3.this);
        //        progressDialog_OEPAY.setMessage(getString(R.string.please_wait));
        //        progressDialog_OEPAY.setCancelable(false);

        String userID = null;
        String merchantId1 = merId;
        String merName1 = merName;
        String orgMerchantId = merchantId1;
        DesEncrypter encrypt;
        String encMerchantId = "";
        DatabaseHelper db = new DatabaseHelper(this);
        try {
            encrypt = new DesEncrypter(merName1);
            encMerchantId = encrypt.encrypt(orgMerchantId);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String whereargs[] = {encMerchantId};
        String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
        db.close();
        if (dbuserID != null) {
            try {
                DesEncrypter encrypter = new DesEncrypter(merName1);
                userID = encrypter.decrypt(dbuserID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //-------------------get userID/operatorId end-------------------//

        OctopusQR_Enquiry enquiry = new OctopusQR_Enquiry(PresentQRPayment_3.this, getPrefPayGate());
        enquiry.execute(gatewayRef, businessDate, amount, currcode1, merRequestAmt, merId, merName, merchantRef, payMethod, userID, surC, orderId, String.valueOf(timeout));
    }

    public void cancelOctopus() {
        System.out.println("---KJ PresentQRPayment3: cancelOctopus");
        progressDialog = new ProgressDialog(PresentQRPayment_3.this);
        progressDialog.setMessage(getString(R.string.cancel_txn));
        progressDialog.setCancelable(false);

        String userID = null;
        String merchantId1 = merId;
        String merName1 = merName;
        String orgMerchantId = merchantId1;
        DesEncrypter encrypt;
        String encMerchantId = "";
        DatabaseHelper db = new DatabaseHelper(this);
        try {
            encrypt = new DesEncrypter(merName1);
            encMerchantId = encrypt.encrypt(orgMerchantId);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String whereargs[] = {encMerchantId};
        String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
        db.close();
        if (dbuserID != null) {
            try {
                DesEncrypter encrypter = new DesEncrypter(merName1);
                userID = encrypter.decrypt(dbuserID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //-------------------get userID/operatorId end-------------------//

        OctopusQR_Cancellation cancellation = new OctopusQR_Cancellation(PresentQRPayment_3.this, progressDialog, getPrefPayGate());
        cancellation.execute(gatewayRef, businessDate, amount, currcode1, merRequestAmt, merId, merchantRef, payMethod, userID, surC, orderId, String.valueOf(timeout));
    }

    public void displayBoostQR(Bitmap bitmap, String token, final String expiryTime, String payRef) {

        if (token.equals("null") || token == null || token.equals("")) {
            System.out.println("---KJ PresentQRPayment3: displaying QR1");
            orderId = payRef;
            checkBoostStatus.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(PresentQRPayment_3.this);
            builder.setTitle(getString(R.string.error));

            builder.setMessage(getString(R.string.error));
            builder.setPositiveButton(getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
            alert = builder.create();
            alert.setCancelable(false);
            if (!((Activity) this).isFinishing()) {
                alert.show();
            }
        } else {
            checkBoost = true;
            System.out.println("---KJ PresentQRPayment3: displaying QR2");
            boostToken = token;
            editPayCode.setText(boostToken);

            merchantQR.setImageBitmap(Bitmap.createScaledBitmap(bitmap, QRcodeWidth, QRcodeWidth, false));
            orderId = payRef;

            setTimeoutTimer();
        }
    }

    public void checkBoostStatus() {
        System.out.println("---KJ PresentQRPayment3: checkOctopusStatus");
        String userID = null;
        String merchantId1 = merId;
        String merName1 = merName;
        String orgMerchantId = merchantId1;
        DesEncrypter encrypt;
        String encMerchantId = "";
        DatabaseHelper db = new DatabaseHelper(this);
        try {
            encrypt = new DesEncrypter(merName1);
            encMerchantId = encrypt.encrypt(orgMerchantId);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String whereargs[] = {encMerchantId};
        String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
        db.close();
        if (dbuserID != null) {
            try {
                DesEncrypter encrypter = new DesEncrypter(merName1);
                userID = encrypter.decrypt(dbuserID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //-------------------get userID/operatorId end-------------------//

        BoostQR_Enquiry enquiry = new BoostQR_Enquiry(PresentQRPayment_3.this, getPrefPayGate());
        enquiry.execute(merId, orderId, userID, merName1);
    }

    public void cancelBoost(int i) {
        System.out.println("---KJ PresentQRPayment3: cancelBoost");

        if (i == 0) {
            progressDialog = new ProgressDialog(PresentQRPayment_3.this);
            progressDialog.setMessage(getString(R.string.cancel_txn));
            progressDialog.setCancelable(false);
        } else {
            progressDialog = null;
        }

        String userID = null;
        String merchantId1 = merId;
        String merName1 = merName;
        String orgMerchantId = merchantId1;
        DesEncrypter encrypt;
        String encMerchantId = "";
        DatabaseHelper db = new DatabaseHelper(this);
        try {
            encrypt = new DesEncrypter(merName1);
            encMerchantId = encrypt.encrypt(orgMerchantId);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String whereargs[] = {encMerchantId};
        String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
        db.close();
        if (dbuserID != null) {
            try {
                DesEncrypter encrypter = new DesEncrypter(merName1);
                userID = encrypter.decrypt(dbuserID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //-------------------get userID/operatorId end-------------------//


        BoostCancel cancellation = new BoostCancel(PresentQRPayment_3.this, progressDialog, getPrefPayGate());
        cancellation.execute(businessDate, amount, currcode1, merRequestAmt, merId, merchantRef, payMethod, userID, surC, orderId);
    }

    public void continueEnquiry() {
        if (payMethod.equalsIgnoreCase("OEPAYOFFL")) {
            checkOePayStatus = new Timer();
            checkOePayStatus.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (checkOE) {
                        System.out.println("---OEPAY check status1");
                        checkOctopusStatus();
                    }
                }
            }, 0, 5000);
        } else if (payMethod.equalsIgnoreCase("BOOSTOFFL")) {
            checkBoostStatus = new Timer();
            checkBoostStatus.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (checkBoost) {
                        checkBoostStatus();
                    }
                }
            }, 0, 5000);
        }
    }

    public void cancelDone() {
        Toast.makeText(PresentQRPayment_3.this, getString(R.string.cancel_done),
                Toast.LENGTH_LONG).show();
Log.d("ActionQR", "456" + String.valueOf(isQRCancelled));
        isQRCancelled = true;

        if (payMethod.equalsIgnoreCase("OEPAYOFFL")) {
            checkOE = false;
            checkOePayStatus.cancel();
        } else if (payMethod.equalsIgnoreCase("BOOSTOFFL")) {
            checkBoost = false;
            checkBoostStatus.cancel();
        } else if(payMethod.equalsIgnoreCase("PROMPTPAYOFFL") || payMethod.equalsIgnoreCase(
                "GRABPAYOFFL")) {
            inquiryQRStatus.cancel();
        }

        updateTimer.cancel();

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String autoMerRef = prefsettings.getString(Constants.pref_MerRef, Constants.default_merchantref);

        if (autoMerRef.equals(Constants.MERREF_AUTO)) {
            incMerRef();
        }

        finish();
        Intent intent = new Intent(PresentQRPayment_3.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void cancelFailed(){

        new AlertDialog.Builder(PresentQRPayment_3.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Alert")
                .setMessage(R.string.cannot_cancel)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with operation
                        if(presentQRTimeout){
                            isQRCancelled = true;
                            finish();
                        }
                    }
                })
                .show();
    }

    public void incMerRef() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("refCounter", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("refCounter", curCounter += 1);
        edit.commit();
    }

    public void showCancelError() {
        String error_msg = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(PresentQRPayment_3.this);
        builder.setTitle(getString(R.string.error));
        error_msg = getString(R.string.cancel_again);

        builder.setMessage(error_msg);
        builder.setPositiveButton(getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        toastError = false;
                    }
                });
        alert = builder.create();
        alert.setCancelable(false);
        toastError = true;

        if (!((Activity) this).isFinishing()) {
            alert.show();
        }
    }

    public void showQRError() {
        String error_msg = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(PresentQRPayment_3.this);
        builder.setTitle(getString(R.string.error));
        error_msg = getString(R.string.get_token_failed);

        builder.setMessage(error_msg);
        builder.setPositiveButton(getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alert = builder.create();
        alert.setCancelable(false);
        toastError = true;

        if (payMethod.equalsIgnoreCase("OEPAYOFFL")) {
            checkOePayStatus.cancel();
        } else if (payMethod.equalsIgnoreCase("BOOSTOFFL")) {
            checkBoostStatus.cancel();
        }

        if (!((Activity) this).isFinishing()) {
            alert.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (payMethod.equalsIgnoreCase("OEPAYOFFL") || payMethod.equalsIgnoreCase("BOOSTOFFL") || payMethod.equalsIgnoreCase("PROMPTPAYOFFL") || payMethod.equalsIgnoreCase("GRABPAYOFFL")) {
            confirmCancelTrx();
        } else {
            super.onBackPressed();
        }
    }

    private void confirmCancelTrx() {
        String cancel_msg = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(PresentQRPayment_3.this);
        cancel_msg = getString(R.string.confirm_cancel_qr);

        builder.setMessage(cancel_msg);
        builder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (payMethod.equalsIgnoreCase("OEPAYOFFL")) {
                            checkOePayStatus.cancel();
                            cancelOctopus();
                        } else if (payMethod.equalsIgnoreCase("BOOSTOFFL")) {
                            checkBoostStatus.cancel();
                            cancelBoost(0);
                        } else if (payMethod.equalsIgnoreCase("PROMPTPAYOFFL")) {
                            cancelQR(orderId);
                        } else if (payMethod.equalsIgnoreCase("GRABPAYOFFL")) {
                            cancelQR(orderId);
                        }

                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

        alert = builder.create();
        alert.setCancelable(false);

        alert.show();
    }

    public class CheckStatusTask extends AsyncTask<String, Void, String> {

        InputStream inputStream = null;
        private String baseUrl2;
        private PresentQRPayment_3 activity;
        private String payGate;
        private HashMap<String, String> map = null;
        private String result = "";

        public CheckStatusTask(PresentQRPayment_3 activity, String payGate) {
            this.activity = activity;
            this.payGate = "Paydollar";

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String merchantId = arg0[0];
            String merRequestAmt = arg0[1];
            String merchantRef = arg0[2];
            String SMS = arg0[3];
            String Email = arg0[4];
            String payRef = arg0[5];
            String paymentMethod = arg0[6];
            String merchantName = arg0[7];

            NameValuePair merchantIdNVpair = new BasicNameValuePair("merchantId", merchantId);
            NameValuePair loginidNVpair = new BasicNameValuePair("merRequestAmt", merRequestAmt);
            NameValuePair passwordNVpair = new BasicNameValuePair("orderRef", merchantRef);
            NameValuePair mobileNVpair = new BasicNameValuePair("email", Email);
            NameValuePair emailNVpair = new BasicNameValuePair("mobileNo", SMS);
            NameValuePair payRefNVpair = new BasicNameValuePair("orderId", payRef);
            NameValuePair payMethodNVpair = new BasicNameValuePair("pMethod", paymentMethod);
            NameValuePair merNameNVpair = new BasicNameValuePair("merName", merchantName);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            System.out.println("-----" + merchantIdNVpair + " " + loginidNVpair);
            System.out.println("-----" + passwordNVpair + " " + mobileNVpair);
            System.out.println("-----" + emailNVpair + " " + payRefNVpair);
            System.out.println("-----" + payMethodNVpair + " " + merNameNVpair);
            nameValuePairs.add(merchantIdNVpair);
            nameValuePairs.add(loginidNVpair);
            nameValuePairs.add(passwordNVpair);
            nameValuePairs.add(mobileNVpair);
            nameValuePairs.add(emailNVpair);
            nameValuePairs.add(payRefNVpair);
            nameValuePairs.add(payMethodNVpair);
            nameValuePairs.add(merNameNVpair);
            try {
                baseUrl2 = PayGate.getURL_CheckStatus(payGate);
                URL url = new URL(baseUrl2);
                HttpURLConnection con = (HttpURLConnection)
                        url.openConnection();
                TrustModifier.relaxHostChecking(con);
                con.setReadTimeout(30000);
                con.setConnectTimeout(25000);
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

            return result;

        }

        protected void onPostExecute(String result) {

            System.out.println(result);
            map = PayGate.split(result);
            String successcode = map.get("successcode");
            String errcode = map.get("errMsg");
            String payMethod = map.get("payMethod");
            String merName = map.get("merName");
            String paytype = map.get("payType");
            String operatorId = map.get("operatorId");
            String cardNo = map.get("cardNo");
            String amount = map.get("Amt");
            String merRef = map.get("Ref");
            String Currcode = map.get("Cur");
            String payRef = map.get("payRef");
            String Time = map.get("TxTime");
            if (successcode == null) {

                activity.fail(
                        successcode,
                        merId,
                        merName,
                        paytype,
                        payMethod,
                        operatorId,
                        cardNo,
                        merRef,
                        payRef,
                        amount,
                        merRequestAmt,
                        surC,
                        Currcode,
                        Time,
                        errcode);
            } else if (successcode.equals("0")) {
                activity.success(
                        successcode,
                        merRef,
                        payRef,
                        amount,
                        merRequestAmt,
                        surC,
                        Currcode,
                        Time,
                        errcode,
                        merId,
                        merName,
                        paytype,
                        payMethod,
                        operatorId,
                        cardNo);
            } else {
                activity.fail(
                        successcode,
                        merId,
                        merName,
                        paytype,
                        payMethod,
                        operatorId,
                        cardNo,
                        merRef,
                        payRef,
                        amount,
                        merRequestAmt,
                        surC,
                        Currcode,
                        Time,
                        errcode);
            }
        }
    }

    public class generateQRTask extends AsyncTask<String, Bitmap, Bitmap> {

        ProgressDialog pg;

        public generateQRTask(ProgressDialog progressDialogg) {
            this.pg = progressDialogg;
        }

        public void onPreExecute() {
            pg.show();
        }

        public Bitmap doInBackground(String... arg0) {

            String tlv = arg0[0];

            Bitmap bitmap1 = null;
            try {

                tlv = QrCodeDataGenerator.generateQrCodeData(createMerchantDataReqestMap());
                bitmap1 = TextToImageEncode(tlv);

            } catch (InputInvalidException exception) {
                Log.d("TLV", "Error = " + exception.getMessage());
                System.out.print(exception.getMessage());

            } catch (WriterException e) {
                e.printStackTrace();
            }


            return bitmap1;


        }

        public void onPostExecute(Bitmap bitmap) {

            try {

                merchantQR.setImageBitmap(bitmap);
                //
            } catch (Exception e) {
                e.printStackTrace();
            }
            pg.dismiss();
        }
    }

    public void checkKBANKStatus(String orderId) {

        if(!isQRCancelled){
            ActionQR inquiryQR = new ActionQR(PresentQRPayment_3.this, getPrefPayGate(), "Inquiry");
            if (!presentQRTimeout) {
                inquiryQR.execute(merId, orderId, payMethod, "inquiry", "F");
            } else {
                inquiryQR.execute(merId, orderId, payMethod, "inquiry", "T");
            }
        }
    }

    public void cancelQR(String orderId) {

        ActionQR cancelQR = new ActionQR(PresentQRPayment_3.this, getPrefPayGate(), "Cancel");
        cancelQR.execute(merId, orderId, payMethod, "cancel");
    }

    public void setTimeoutTimer(){

        // QR Expired Time
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, timeout);
        dateExpired = cal.getTime();

        // QR Expired Time
        //                    Date createdTime = df.parse(trxTime);
        //                    cal.setTime(createdTime);
        //                    cal.add(Calendar.MINUTE, timeout);

        updateTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    // Current Time
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    Calendar cal = Calendar.getInstance();
                    String now = df.format(cal.getTime());
                    dateNow = df.parse(now);

                    long mills = dateExpired.getTime() - dateNow.getTime();
                    int mins = (int) (mills / (1000 * 60)) % 60;
                    int secs = (int) (mills / (1000)) % 60;
                    NumberFormat formatter = new DecimalFormat("00");
                    String seconds = formatter.format(secs); // ----> 01

                    final String diff = mins + ":" + seconds; // updated value every 1 second

                    if (diff.equalsIgnoreCase("0:00")) {

                        presentQRTimeout = true;
                        inquiryQRStatus.cancel();
                        updateTimer.cancel();

//                        //Last inquiry when timeout
//                        checkKBANKStatus(orderId);

                        //Cancel QR
                        /*cancelQR(orderId);*/
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            QRStatus.setText(diff);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, 0, 1000);
    }

    public void afterQR(String successCode,
                        String payRef,
                        String trxTime,
                        String returnMsg,
                        String bankRef) {

        inquiryQRStatus.cancel();

        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        toneGen1.startTone(ToneGenerator.TONE_PROP_PROMPT, 300);

        incMerRef();

        Intent intent = new Intent();

        intent.putExtra(Constants.SUCCESS_CODE, successCode);
        intent.putExtra(Constants.MERCHANT_REF, merchantRef);
        intent.putExtra(Constants.PAYREF, payRef);
        intent.putExtra(Constants.AMOUNT, amount);
        intent.putExtra(Constants.MERREQUESTAMT, merRequestAmt);
        intent.putExtra(Constants.SURCHARGE, surcharge);
        intent.putExtra(Constants.CURRCODE, currCode);
        intent.putExtra(Constants.TXTIME, trxTime);
        intent.putExtra(Constants.ERRMSG, returnMsg);
        intent.putExtra(Constants.MERID, merId);
        intent.putExtra(Constants.MERNAME, merName);
        intent.putExtra(Constants.PAYTYPE, payType);
        intent.putExtra(Constants.PAYMETHOD, payMethod);
        intent.putExtra(Constants.CARDNO, bankRef); // 20191010 Eric Change "trxNo" to "bankRef"
        intent.putExtra(Constants.OPERATORID, operatorId);
        intent.putExtra(Constants.pref_hideSurcharge, hideSurcharge);

        System.out.println("Kennedy-----" + "Payment Tab Success->   successCode:" + successCode + ",merchantRef:" + merchantRef + ",payRef:" + payRef + ",amount:" + amount + ",merRequestAmt:" + merRequestAmt + ",surcharge:" + surcharge + ",Currcode" + currCode + ",TxTime:" + trxTime + ",errcode:" + returnMsg + ",merchantid:" + merId + ",mername:" + merName + ",payType:" + payType + ",payMethod:" + payMethod);
        intent.setClass(PresentQRPayment_3.this, PresentQR_result.class);

        setResult(RESULT_OK);

        startActivity(intent);
        finish();
    }
}