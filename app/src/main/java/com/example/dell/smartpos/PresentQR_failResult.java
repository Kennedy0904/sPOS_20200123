package com.example.dell.smartpos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.smartpos.Scanner.Fail_word;

import java.text.DecimalFormat;
import java.util.Date;

public class PresentQR_failResult extends AppCompatActivity {

    String partnerlogo = "";
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

    public TextView MerName;
    public TextView tvPayRef;
    public TextView failmsg;
    public TextView failadvise;
    public TextView showAmount;
    public TextView showCurrcode;
    public TextView showPayMethod;

    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;

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
        setTitle(R.string.present_qr);
        setContentView(R.layout.activity_present_qr_fail_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

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


        //---------for autologout---------//
        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout, false);
        lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
        Log.d("OTTO", "init Fail_scan CheckLogout:" + CheckLogout + ",lastUpdateTime:" + lastUpdateTime);
        //---------for autologout---------//

        final Intent intent = getIntent();
//        merchantId = intent.getStringExtra(Constants.MERID)==null?"":intent.getStringExtra(Constants.MERID);
//        success_code = intent.getStringExtra(Constants.SUCCESS_CODE)==null?"":intent.getStringExtra(Constants.SUCCESS_CODE);
//        merchant_ref_no = intent.getStringExtra(Constants.MERCHANT_REF)==null?"":intent.getStringExtra(Constants.MERCHANT_REF);
//        payRef = intent.getStringExtra(Constants.PAYREF)==null?"":intent.getStringExtra(Constants.PAYREF);
//        amount = intent.getStringExtra(Constants.AMOUNT)==null?"":intent.getStringExtra(Constants.AMOUNT);
//        Surcharge = intent.getStringExtra(Constants.SURCHARGE)==null?"":intent.getStringExtra(Constants.SURCHARGE);
//        Currcode = intent.getStringExtra(Constants.CURRCODE)==null?"":intent.getStringExtra(Constants.CURRCODE);
//        TXTime = intent.getStringExtra(Constants.TXTIME)==null?"":intent.getStringExtra(Constants.TXTIME);
//        errMsg = intent.getStringExtra(Constants.ERRMSG)==null?"":Fail_word.failreason( intent.getStringExtra(Constants.ERRMSG), Fail_scan.this );
//        errAdvise = intent.getStringExtra(Constants.ERRMSG)==null?"":Fail_word.failadvise( intent.getStringExtra(Constants.ERRMSG), Fail_scan.this );
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
        errMsg = intent.getStringExtra(Constants.ERRMSG) == null ? "" : Fail_word.failreason(intent.getStringExtra(Constants.ERRMSG), PresentQR_failResult.this);
        payType = intent.getStringExtra(Constants.PAYTYPE) == null ? "" : intent.getStringExtra(Constants.PAYTYPE);
        pMethod = intent.getStringExtra(Constants.PAYMETHOD) == null ? "" : intent.getStringExtra(Constants.PAYMETHOD);
        operatorId = intent.getStringExtra(Constants.OPERATORID) == null ? "" : intent.getStringExtra(Constants.OPERATORID);
        cardNo = intent.getStringExtra(Constants.CARDNO) == null ? "" : intent.getStringExtra(Constants.CARDNO);
        hideSurcharge = intent.getStringExtra(Constants.pref_hideSurcharge) == null ? "F" : intent.getStringExtra(Constants.pref_hideSurcharge);
        errAdvise = intent.getStringExtra(Constants.ERRMSG) == null ? "" : Fail_word.failadvise(intent.getStringExtra(Constants.ERRMSG), PresentQR_failResult.this);

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
        showAmount.setText(amount);
        showCurrcode.setText(Currcode);
        showPayMethod.setText(pMethod);

        if(pMethod.equalsIgnoreCase("FPS")){
            setTitle(R.string.fps_menu);
            step1.setText(R.string.enter_amount);
            step2.setText(R.string.generate_qr);
            step3.setText(R.string.payment_result);
            step4.setVisibility(View.GONE);
            findViewById(R.id.circle4).setVisibility(View.GONE);
            findViewById(R.id.line3).setVisibility(View.GONE);
            findViewById(R.id.gap3).setVisibility(View.GONE);

            step2.setTypeface(null, Typeface.BOLD);
            step3.setTypeface(null, Typeface.BOLD);
        }

        Button btnMainMenu = (Button) findViewById(R.id.btnMainMenu);
        btnMainMenu.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(PresentQR_failResult.this, MainActivity.class);
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
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate, Constants.default_paygate);
        String prefpartnerlogopaygate = prefpaygate;
        String baseUrl = PayGate.getURL_partnerlogo(prefpartnerlogopaygate) + partnerlogo;
//        ImageView partnerlogoview = (ImageView)findViewById(R.id.partnerlogo);
//        PartnerLogoUtil.setImageToImageView(partnerlogoview,baseUrl,partnerlogo);
        //--------set partnerlogo-----------//
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

