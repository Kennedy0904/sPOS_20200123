package com.example.dell.smartpos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DialogActivity extends Activity {

    String settle;
    String merName;
    String merchantId;
    String merRef;
    String payRef;
    String date;
    String amount;
    String currency;
    String type;
    String refbutton;
    String actionType;
    String user;
    String password;
    String merRequestAmt;
    String Surcharge;
    String payMethod;
    String payBankId;
    //String payBankName;
    String status;
    EditText edtnewAmt;
    Button confirm;
    TextView tvwCurrCode;
    double refundAmt;
    double doublemerRequestAmt;

    AlertDialog.Builder dialog;

    private ProgressBar loading;
    static final String actCapture = "Capture";
    static final String actVoid = "Void";
    static final String actRefund = "OnlineRefund";
    static final String actOnlineRefund = "OnlineRefund";
    static final String actOnlinePartialRefund = "OnlinePartialRefund";
    static final String actRequestRefund = "RequestRefund";

    LinearLayout actResult;
    private RelativeLayout resSuc;
    private RelativeLayout resFail;

    Button resOk;
    private RelativeLayout inputamount;
    private LinearLayout void_info;
    private LinearLayout dlgButtons;

    Boolean canExit = true;
    private Button cancel;

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

        //---------for autologout---------//
        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout, false);
        lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
        Log.d("OTTO", "init DialogActivity CheckLogout:" + CheckLogout + ",lastUpdateTime:" + lastUpdateTime);
        //---------for autologout---------//

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

        // Check language
        GlobalFunction.changeLanguage(DialogActivity.this);

        setContentView(R.layout.activity_dialog);
        Intent intent = getIntent();
        merName = intent.getStringExtra(Constants.MERNAME);
        merchantId = intent.getStringExtra(Constants.MERID);
        refbutton = intent.getStringExtra("button");
        type = intent.getStringExtra("type");
        merRef = intent.getStringExtra(Constants.MERCHANT_REF);
        payRef = intent.getStringExtra(Constants.PAYREF);
        date = intent.getStringExtra(Constants.DATE);
        amount = intent.getStringExtra(Constants.AMOUNT);
        currency = intent.getStringExtra(Constants.CURR);
        settle = intent.getStringExtra(Constants.SETTLE);
        merRequestAmt = intent.getStringExtra(Constants.MERREQUESTAMT) == null ? "0" : intent.getStringExtra(Constants.MERREQUESTAMT);
        Surcharge = intent.getStringExtra(Constants.SURCHARGE) == null ? "0" : intent.getStringExtra(Constants.SURCHARGE);
        payMethod = intent.getStringExtra(Constants.PAYMETHOD) == null ? "" : intent.getStringExtra(Constants.PAYMETHOD);
        payBankId = intent.getStringExtra(Constants.PAYBANKID) == null ? "" : intent.getStringExtra(Constants.PAYBANKID);
        //payBankName = intent.getStringExtra(Constants.PAYBANKNAME) == null ? "" :intent.getStringExtra(Constants.PAYBANKNAME);
        status = intent.getStringExtra(Constants.STATUS) == null ? "" : intent.getStringExtra(Constants.STATUS);
        actionType = type;
        dlgButtons = (LinearLayout) findViewById(R.id.dlgButtons);
        TextView action = (TextView) findViewById(R.id.txtAction);
        inputamount = (RelativeLayout) findViewById(R.id.inputamount);
        tvwCurrCode = (TextView) findViewById(R.id.dlgCurrCode);
        actResult = (LinearLayout) findViewById(R.id.action_result);
        actResult.setVisibility(View.GONE);
        resSuc = (RelativeLayout) findViewById(R.id.res_suc);
        resFail = (RelativeLayout) findViewById(R.id.res_fail);
        resOk = (Button) findViewById(R.id.res_OK);

        loading = (ProgressBar) findViewById(R.id.ico_loading);

        void_info = (LinearLayout) findViewById(R.id.void_info);

        edtnewAmt = (EditText) findViewById(R.id.edtnewAmount);
        edtnewAmt.setKeyListener(DigitsKeyListener.getInstance(false, true));
        edtnewAmt.post(new Runnable() {
            @Override
            public void run() {
                edtnewAmt.setSelection(edtnewAmt.getText().length());
            }
        });

        confirm = (Button) findViewById(R.id.dialogConfirm);
        dlgButtons.setVisibility(View.VISIBLE);
        if (type.equals(actCapture)) {
            inputamount.setVisibility(LinearLayout.VISIBLE);
            tvwCurrCode.setText(currency);
            void_info.setVisibility(LinearLayout.GONE);
            action.setText(getString(R.string.capture_transaction));
            edtnewAmt.setText(amount);
        } else if (type.equals(actRefund)) {
            inputamount.setVisibility(LinearLayout.VISIBLE);
            tvwCurrCode.setText(currency);
            void_info.setVisibility(LinearLayout.GONE);
            action.setText(getString(R.string.refund_transaction));
            edtnewAmt.setText(amount);

        } else if (type.equals(actVoid)) {
            inputamount.setVisibility(LinearLayout.GONE);
            void_info.setVisibility(LinearLayout.VISIBLE);
            TextView txtamount = (TextView) findViewById(R.id.txtAmount);
            TextView txtCurrcode = (TextView) findViewById(R.id.edtCurrCode);

            TextView txtmerRef = (TextView) findViewById(R.id.edtMerRef);
            TextView txtpayRef = (TextView) findViewById(R.id.edtPayRef);
            TextView txtdate = (TextView) findViewById(R.id.edtDate);

            txtamount.setText(amount);
            txtCurrcode.setText(currency);
            txtmerRef.setText(merRef);
            txtpayRef.setText(payRef);
            txtdate.setText(date);
            action.setText(getString(R.string.void_transaction));

        }
        cancel = (Button) findViewById(R.id.dialogCancel);

        // GET API FROM DB
        DatabaseHelper db = new DatabaseHelper(this);
        String orgMerchantId = merchantId;
        String encMerchantId = "";
        DesEncrypter encrypt;
        try {
            encrypt = new DesEncrypter(merName);
            encMerchantId = encrypt.encrypt(orgMerchantId);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        String whereargs[] = {encMerchantId};
        String dbuser = db.getSingleData(Constants.DB_TABLE_ID,
                Constants.DB_API_ID, Constants.DB_MERCHANT_ID, whereargs);
        String dbpassword = db.getSingleData(Constants.DB_TABLE_ID,
                Constants.DB_API_PW, Constants.DB_MERCHANT_ID, whereargs);
        db.close();
        if (dbuser != null && dbpassword != null) {
            try {
                DesEncrypter encrypter = new DesEncrypter(merName);
                user = encrypter.decrypt(dbuser);
                password = encrypter.decrypt(dbpassword);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, getString(R.string.apiID_pw_notset),
                    Toast.LENGTH_LONG).show();
        }

        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //Hide Soft Keyboard
                System.out.println("---pMethod:" + payMethod);
                if (!type.equals(actVoid)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }


                loading.setVisibility(View.VISIBLE);
                if (type.equals(actRefund)) {
                    if ("".equals(edtnewAmt.getText().toString())) {
                        Toast.makeText(DialogActivity.this, getString(R.string.set_amount), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        return;
                    }
                    refundAmt = Double.parseDouble((edtnewAmt.getText().toString().replaceAll(",", "")));
                    double origAmt = Double.parseDouble(amount.replaceAll(",", ""));
                    doublemerRequestAmt = 0;
                    double doubleSucharge = Double.parseDouble(Surcharge);
                    try {
                        doublemerRequestAmt = Double.parseDouble(merRequestAmt);
                    } catch (Exception e) {
                        doublemerRequestAmt = 0;
                    }
                    if ((doublemerRequestAmt > 0) && (refundAmt > doublemerRequestAmt)) {
                        Toast.makeText(DialogActivity.this, getString(R.string.merRequestAmt_error), Toast.LENGTH_SHORT).show();
                        if ("SUYUPAY".equals(payBankId)) {
                            actionType = actRequestRefund;
                        } else {
                            actionType = actOnlinePartialRefund;
                        }
                        loading.setVisibility(View.GONE);
                        return;
                    } else if ((doublemerRequestAmt > 0) && (refundAmt < doublemerRequestAmt)) {
                        if ("SUYUPAY".equals(payBankId)) {
                            actionType = actRequestRefund;
                        } else {
                            actionType = actOnlinePartialRefund;
                        }
                    } else if ((doublemerRequestAmt > 0) && (refundAmt == doublemerRequestAmt)) {
                        if (status.equals(getString(R.string.accepted))) {
                            if (doubleSucharge > 0) {
                                if ("SUYUPAY".equals(payBankId)) {
                                    actionType = actRequestRefund;
                                } else {
                                    actionType = actOnlinePartialRefund;
                                }
                            } else {
                                if ("SUYUPAY".equals(payBankId)) {
                                    actionType = actRequestRefund;
                                } else {
                                    actionType = actOnlineRefund;
                                }
                            }
                        } else {
                            if ("SUYUPAY".equals(payBankId)) {
                                actionType = actRequestRefund;
                            } else {
                                actionType = actOnlinePartialRefund;
                            }
                        }
                    } else {
                        if (refundAmt > origAmt) {
                            Toast.makeText(DialogActivity.this,
                                    getString(R.string.amount_error),
                                    Toast.LENGTH_SHORT).show();
                            actionType = actRefund;
                            return;
                        } else if (refundAmt == origAmt) {
                            if ("SUYUPAY".equals(payBankId)) {
                                actionType = actRequestRefund;
                            } else {
                                actionType = actOnlineRefund;
                            }
                        } else if (refundAmt < origAmt) {
                            if ("SUYUPAY".equals(payBankId)) {
                                actionType = actRequestRefund;
                            } else {
                                actionType = actOnlinePartialRefund;
                            }
                        }
                    }
                }


                EditText edtnewAmt = (EditText) findViewById(R.id.edtnewAmount);

                VoidCapTask voidCapTask = new VoidCapTask(DialogActivity.this,
                        getPrefPayGate());
                voidCapTask.execute(payRef, user, password, actionType,
                        merchantId, edtnewAmt.getText().toString(), payMethod);
                String edtna = edtnewAmt.getText().toString();
                Log.d("OTTO", "edtna:" + edtna);
                confirm.setEnabled(false);
                cancel.setEnabled(false);

            }

        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }

        });

    }

    public String getPrefPayGate() {
        SharedPreferences prefsettings = getApplicationContext()
                .getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate,
                Constants.default_paygate);
        return prefpaygate;
    }

    public class VoidCapTask extends AsyncTask<String, Void, String> {

        private DialogActivity activity;
        private String payGate;
        private HashMap<String, String> map = null;
        private String baseUrl;

        private String result = "";
        InputStream inputStream = null;

        public VoidCapTask(DialogActivity activity, String payGate) {
            this.activity = activity;
            this.payGate = payGate;
        }

        @Override
        protected void onPreExecute() {
            confirm.setEnabled(false);
            cancel.setEnabled(false);
            canExit = false;
        }

        @Override
        protected String doInBackground(String... arg0) {
            // pass argument(payref, loginid,password,actiontype,merchantID,amount)
            NameValuePair payRefNVpair = new BasicNameValuePair("payRef", arg0[0]);
            NameValuePair loginidNVpair = new BasicNameValuePair("loginId", arg0[1]);
            NameValuePair passwordNVpair = new BasicNameValuePair("password", arg0[2]);
            NameValuePair actionTypeNVpair = new BasicNameValuePair("actionType", arg0[3]);
            NameValuePair merchantIdNVpair = new BasicNameValuePair("merchantId", arg0[4]);
            NameValuePair amountNVpair = new BasicNameValuePair("amount", arg0[5]);
            NameValuePair payMethodNVpair = new BasicNameValuePair("payMethod", arg0[6]);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(merchantIdNVpair);
            nameValuePairs.add(payRefNVpair);
            nameValuePairs.add(loginidNVpair);
            nameValuePairs.add(passwordNVpair);
            nameValuePairs.add(actionTypeNVpair);
            nameValuePairs.add(amountNVpair);
            nameValuePairs.add(payMethodNVpair);

            try {
                baseUrl = PayGate.getURL_orderAPI(payGate);

                URL url = new URL(baseUrl);
                HttpURLConnection con = (HttpURLConnection) url
                        .openConnection();
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
                            canExit = true;
                        }
                    }
                }

            } catch (Exception e) {
                canExit = true;
                e.printStackTrace();
            }

            System.out.println("DialogActivity POST result: " + result);
            return result;
        }

        protected void onPostExecute(String resultCode) {
            canExit = true;
            loading.setVisibility(View.GONE);
            map = PayGate.split(result);
            String resultcode = map.get("resultCode");
            String returncode = map.get("returnCode");
            String error = map.get("errMsg");
            String printText =  map.get("printText");

            confirm.setEnabled(true);
            cancel.setEnabled(true);
            if (resultcode != null) {

                if(printText != null){
                    if (resultcode.equals("0")) {
                        String amount = map.get("amt");
                        String orderStatus = map.get("orderStatus");
                        String bankId = map.get("payBankId");
                        String batchNo = map.get("batchNo");
                        String systemTraceNo = map.get("sysTraceNo");
                        String bankMid = map.get("bankMid");
                        String bankTid = map.get("bankTid");
                        String bankRef = map.get("bankRef");
                        String host = map.get("host");
                        String voidTime = map.get("voidTime");
                        String authId = map.get("authId");

                        activity.successBBL(
                                Double.toString(refundAmt),
                                orderStatus,
                                amount,
                                printText,
                                bankId,
                                batchNo,
                                systemTraceNo,
                                bankMid,
                                bankTid,
                                bankRef,
                                host,
                                voidTime,
                                authId);

                    } else {
                        activity.showConfirmError(error);
                    }
                } else {
                    if (resultcode.equals("0")) {
                        String amount = map.get("amt");
                        String orderStatus = map.get("orderStatus");
                        activity.success(Double.toString(refundAmt), orderStatus, amount);

                    } else {
                        activity.showConfirmError(error);
                    }
                }

            } else if (returncode != null) {
                if (returncode.substring(0, 1).equals("0")) {
                    activity.success(Double.toString(refundAmt), getString(R.string.accepted_adj), Double.toString(refundAmt));
                } else {
                    activity.showConfirmError("");
                }
            } else {
                activity.showConfirmError("");
            }

        }

    }

    public void showConfirmError(String error) {
        // TODO Auto-generated method stub

        void_info.setVisibility(LinearLayout.GONE);
        inputamount.setVisibility(LinearLayout.GONE);
        actResult.setVisibility(View.VISIBLE);
        resSuc.setVisibility(View.GONE);
        resFail.setVisibility(View.VISIBLE);
        resOk.setVisibility(View.VISIBLE);
        dlgButtons.setVisibility(View.GONE);

        TextView txtErrorMsg = (TextView) findViewById(R.id.txtErrorMsg);
        txtErrorMsg.setText(error);

        resOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

    }

    public void sendReceiptSuccess() {
        // TODO Auto-generated method stub
        confirm.setEnabled(true);
        cancel.setEnabled(true);
        finish();
    }

    public void success(final String amount2, final String orderStatus, final String amount3) {
        // TODO Auto-generated method stub

        inputamount.setVisibility(LinearLayout.GONE);
        void_info.setVisibility(LinearLayout.GONE);
        actResult.setVisibility(View.VISIBLE);
        resFail.setVisibility(View.GONE);
        resSuc.setVisibility(View.VISIBLE);
        resOk.setVisibility(View.VISIBLE);
        dlgButtons.setVisibility(View.GONE);

        resOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                inputamount.setVisibility(LinearLayout.GONE);
                void_info.setVisibility(LinearLayout.GONE);
                Intent returnIntent = new Intent();
				returnIntent.putExtra("oriAmount", amount);
                returnIntent.putExtra("amt", amount2);
                returnIntent.putExtra("amt2", amount3);
                returnIntent.putExtra("newStatus", orderStatus);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    public void successBBL(
            final String amount2,
            final String orderStatus,
            final String amount3,
            final String printText,
            final String bankId,
            final String batchNo,
            final String systemTraceNo,
            final String bankMid,
            final String bankTid,
            final String bankRef,
            final String host,
            final String voidTime,
            final String authId) {
        // TODO Auto-generated method stub

        inputamount.setVisibility(LinearLayout.GONE);
        void_info.setVisibility(LinearLayout.GONE);
        actResult.setVisibility(View.VISIBLE);
        resFail.setVisibility(View.GONE);
        resSuc.setVisibility(View.VISIBLE);
        resOk.setVisibility(View.VISIBLE);
        dlgButtons.setVisibility(View.GONE);

        resOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                inputamount.setVisibility(LinearLayout.GONE);
                void_info.setVisibility(LinearLayout.GONE);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("amt", amount2);
                returnIntent.putExtra("amt2", amount3);
                returnIntent.putExtra("newStatus", orderStatus);
                returnIntent.putExtra("printText", printText);
                returnIntent.putExtra("bankId", bankId);
                returnIntent.putExtra("batchNo", batchNo);
                returnIntent.putExtra("sysTraceNo", systemTraceNo);
                returnIntent.putExtra("bankMid", bankMid);
                returnIntent.putExtra("bankTid", bankTid);
                returnIntent.putExtra("bankRef", bankRef);
                returnIntent.putExtra("host", host);
                returnIntent.putExtra("voidTime", voidTime);
                returnIntent.putExtra("authId", authId);

                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (canExit) {
            finish();
            super.onBackPressed();
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
}
