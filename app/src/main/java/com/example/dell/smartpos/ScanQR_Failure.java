package com.example.dell.smartpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.smartpos.Scanner.Fail_word;
import java.util.Date;

public class ScanQR_Failure extends AppCompatActivity {

    String merName = "";

    public String success_code;
    public String merchantId;
    public String merchant_ref_no;
    public String payRef;
    public String amount;
    public String Surcharge;
    public String Currcode;
    public String TXTime;
    public String errMsg;
    public String errAdvise;
    public String merrequestamt;
    public String payType;
    public String pMethod;
    public String operatorId;
    public String cardNo;
    public String hideSurcharge;
    public String auth_code;

    public TextView MerName;
    public TextView tvPayRef;
    public TextView failmsg;
    public TextView failadvise;
    public TextView showAmount;
    public TextView showCurrcode;
    public TextView showPayMethod;
    public TextView wxPaymentStatus;

    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;

    ProgressDialog progressDialog_ALIPAYTH;
    Context context;

    //---------for autologout---------//
    Handler CheckTimeOutHandler = new Handler();
    Date lastUpdateTime;//lastest operation time
    long timePeriod;//no operation time
    float SESSION_EXPIRED = Constants.SESSION_EXPIRY / 1000;//session expired time
    boolean CheckLogout = false;//logout flag
    long CheckInterval = 1000;//checking time intercal
    //---------for autologout---------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.scan_qr);
        setContentView(R.layout.activity_qrpayment__failure);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

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


        //---------for autologout---------//
        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout, false);
        lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
        Log.d("OTTO", "init Fail_scan CheckLogout:" + CheckLogout + ",lastUpdateTime:" + lastUpdateTime);
        //---------for autologout---------//

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
        errMsg = intent.getStringExtra(Constants.ERRMSG) == null ? "" : Fail_word.failreason(intent.getStringExtra(Constants.ERRMSG), ScanQR_Failure.this);
        payType = intent.getStringExtra(Constants.PAYTYPE) == null ? "" : intent.getStringExtra(Constants.PAYTYPE);
        pMethod = intent.getStringExtra(Constants.PAYMETHOD) == null ? "" : intent.getStringExtra(Constants.PAYMETHOD);
        operatorId = intent.getStringExtra(Constants.OPERATORID) == null ? "" : intent.getStringExtra(Constants.OPERATORID);
        cardNo = intent.getStringExtra(Constants.CARDNO) == null ? "" : intent.getStringExtra(Constants.CARDNO);
        hideSurcharge = intent.getStringExtra(Constants.pref_hideSurcharge) == null ? "F" : intent.getStringExtra(Constants.pref_hideSurcharge);
        errAdvise = intent.getStringExtra(Constants.ERRMSG) == null ? "" : Fail_word.failadvise(intent.getStringExtra(Constants.ERRMSG), ScanQR_Failure.this);

        if(pMethod.equalsIgnoreCase("ALIPAYTHOFFL")){
            auth_code = intent.getStringExtra(Constants.AUTHID) == null ? "F" : intent.getStringExtra(Constants.AUTHID);
        }

        System.out.println("OTTO-----failwechat.java---" + "success_code:" + success_code + "," + "merchantId:" + merchantId + "," + "merName:" + merName + "," + "merchant_ref_no:" + merchant_ref_no + "," + "payRef:" + payRef + "," + "amount:" + amount + "," + "Surcharge:" + Surcharge + "," + "Currcode:" + Currcode + "," + "TXTime:" + TXTime + "," + "errMsg:" + errMsg + "," + "errAdvise:" + errAdvise + "," + "merrequestamt:" + merrequestamt + "," + "payType:" + payType + "," + "pMethod:" + pMethod + "," + "operatorId:" + operatorId + "," + "cardNo:" + cardNo + "," + "hideSurcharge:" + hideSurcharge);

        MerName = (TextView) findViewById(R.id.MerName);
        MerName.setText(merName);

        tvPayRef = (TextView) findViewById(R.id.txtPayRef);
        tvPayRef.setText(payRef);

        failmsg = (TextView) findViewById(R.id.failreason);
        failmsg.setText(errMsg);

        failadvise = (TextView) findViewById(R.id.failadvise);
        failadvise.setText(errAdvise);

        showAmount = (TextView) findViewById(R.id.wxAmount);
        showCurrcode = (TextView) findViewById(R.id.wxCurrcode);
        showPayMethod = (TextView) findViewById(R.id.wxPaymentMethod);
        wxPaymentStatus = (TextView) findViewById(R.id.wxPaymentStatus);
        showAmount.setText(amount);
        showCurrcode.setText(Currcode);
        showPayMethod.setText(pMethod);

        Button btnMainMenu = (Button) findViewById(R.id.btnMainMenu);
        btnMainMenu.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(ScanQR_Failure.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        });

        Button btnTryAgain = (Button) findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }

        });

        Button btnCheck = (Button) findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                progressDialog_ALIPAYTH = new ProgressDialog(ScanQR_Failure.this);
                progressDialog_ALIPAYTH.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                progressDialog_ALIPAYTH.setCancelable(false);
                QRTrxInq queryTrx = new QRTrxInq(ScanQR_Failure.this, progressDialog_ALIPAYTH, getPrefPayGate(), true);

                //-------------------get userID/operatorId start-------------------//
                String userID = null;

                DesEncrypter encrypt;
                String encMerchantId = "";
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                try {
                    encrypt = new DesEncrypter(merName);
                    encMerchantId = encrypt.encrypt(merchantId);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                String whereargs[] = {encMerchantId};
                String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
                String deviceSerial = Build.SERIAL;
                String softwareVersion = Constants.current_VersionCode;
                db.close();
                if (dbuserID != null) {
                    try {
                        DesEncrypter encrypter = new DesEncrypter(merName);
                        userID = encrypter.decrypt(dbuserID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //-------------------get userID/operatorId end-------------------//

                queryTrx.execute(
                        amount,
                        merrequestamt,
                        Surcharge,
                        Currcode,
                        merchant_ref_no,
                        cardNo,
                        pMethod,
                        merchantId,
                        merName,
                        userID,
                        String.valueOf(hideSurcharge),
                        autoBBLQRBatchID(),
                        deviceSerial,
                        softwareVersion,
                        payRef);
            }

        });

        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                progressDialog_ALIPAYTH = new ProgressDialog(ScanQR_Failure.this);
                progressDialog_ALIPAYTH.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                progressDialog_ALIPAYTH.setCancelable(false);

                CancelOrder cancelOrder = new CancelOrder(ScanQR_Failure.this, progressDialog_ALIPAYTH, getPrefPayGate(), true);

                //-------------------get userID/operatorId start-------------------//
                String userID = null;
                String password = null;

                DesEncrypter encrypt;
                String encMerchantId = "";
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                try {
                    encrypt = new DesEncrypter(merName);
                    encMerchantId = encrypt.encrypt(merchantId);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                String whereargs[] = {encMerchantId};
                String dbuserID = db.getSingleData(Constants.DB_TABLE_ID,
                        Constants.DB_API_ID, Constants.DB_MERCHANT_ID, whereargs);
                String dbpassword = db.getSingleData(Constants.DB_TABLE_ID,
                        Constants.DB_API_PW, Constants.DB_MERCHANT_ID, whereargs);
                String deviceSerial = Build.SERIAL;
                db.close();
                if (dbuserID != null) {
                    try {
                        DesEncrypter encrypter = new DesEncrypter(merName);
                        userID = encrypter.decrypt(dbuserID);
                        password = encrypter.decrypt(dbpassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //-------------------get userID/operatorId end-------------------//

                cancelOrder.execute(
                        payRef,
                        userID,
                        password,
                        "Reversal",
                        merchantId,
                        amount,
                        pMethod,
                        deviceSerial);

            }
        });

        if(pMethod.equalsIgnoreCase("ALIPAYTHOFFL") || errMsg.equalsIgnoreCase("unknown")) {
            btnMainMenu.setVisibility(View.GONE);
            btnCheck.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            btnTryAgain.setVisibility(View.GONE);
            wxPaymentStatus.setText("Pending");
        }
    }

    public void scanbackBBL(
            String successcode,
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
            String cardNo,
            String printText,
            String payBankId,
            String batchNo,
            String sysTraceNo,
            String host,
            String tid,
            String mid,
            String partnerTrxId,
            String approvalCode) {

        if (pMethod.equals("UNIONPAY") || pMethod.equalsIgnoreCase("GRABPAY") || pMethod.equalsIgnoreCase("LINEPAY")) {
            Intent intent = new Intent(ScanQR_Failure.this, PresentQR_result.class);
            intent.putExtra("successCode", successcode);
            intent.putExtra("payMethod", pMethod);
            intent.putExtra("merName", mername);
            intent.putExtra("merRequestAmt", amount);
            intent.putExtra("merchantRef", merchant_ref_no);

            startActivity(intent);
        } else {
            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
            toneGen1.startTone(ToneGenerator.TONE_PROP_PROMPT, 300);

            incMerRef();
            Toast.makeText(ScanQR_Failure.this, R.string.scan_success, Toast.LENGTH_SHORT).show();
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
            intent.putExtra("printText", printText);
            intent.putExtra("payBankId", payBankId);
            intent.putExtra("batchNo", batchNo);
            intent.putExtra("host", host);
            intent.putExtra("sysTraceNo", sysTraceNo);
            intent.putExtra("MId", mid);
            intent.putExtra("TId", tid);
            intent.putExtra("partnerTrxId", partnerTrxId);
            intent.putExtra("approvalCode", approvalCode);

            System.out.println("OTTO-----" + "Payment Tab success->   successcod:" + successcode + ",Ref:" + merchant_ref_no + ",PayRef:" + PayRef + ",amount:" + amount + ",merRequestAmt:" + merRequestAmt + ",surcharge:" + Surcharge + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode + ",merchantid:" + merchantid + ",mername:" + mername + ",payType:" + payType + ",pMethod:" + pMethod + ",operatorId:" + operatorId + ",cardNo:" + cardNo);
            intent.setClass(ScanQR_Failure.this, ScanQR_Success.class);

            setResult(RESULT_OK);

            startActivity(intent);
            finish();
        }
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

    public String getPrefPayGate() {
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate, Constants.default_paygate);
        return prefpaygate;
    }

    public void scanback(String successcode,
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

        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        toneGen1.startTone(ToneGenerator.TONE_PROP_PROMPT, 300);

        incMerRef();
        Toast.makeText(ScanQR_Failure.this, R.string.scan_success, Toast.LENGTH_SHORT).show();
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
        intent.setClass(ScanQR_Failure.this, ScanQR_Success.class);

        setResult(RESULT_OK);

        startActivity(intent);
        finish();
    }

    public void scanBBL() {
        Toast.makeText(ScanQR_Failure.this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(ScanQR_Failure.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void scanfail(){
        Toast.makeText(ScanQR_Failure.this, "Result Failed", Toast.LENGTH_SHORT).show();
    }

    public void scanToastMsg(String msg){
        Toast.makeText(ScanQR_Failure.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void incMerRef() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("refCounter", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("refCounter", curCounter += 1);
        edit.commit();
    }

    private String autoBBLQRBatchID() {

        SharedPreferences pref = getSharedPreferences(Constants.BBLQR_BATCHNO_COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("BBLBatchCounter", 0);

        int runningNum = curCounter +1;
        int length = String.valueOf(curCounter).length();
        String digit ="000000";
        String batchID = digit.substring(0, digit.length() - length) + runningNum;

        return batchID;
    }

    private String autoBBLQRTraceNo() {

        SharedPreferences pref = getSharedPreferences(Constants.BBLQR_TRACENO_COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("BBLTraceCounter", 0);

        int runningNum = curCounter +1;
        int length = String.valueOf(curCounter).length();
        String digit ="000000";
        String batchID = digit.substring(0, digit.length() - length) + runningNum;

        return batchID;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

