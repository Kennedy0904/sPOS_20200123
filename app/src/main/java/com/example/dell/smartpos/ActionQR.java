package com.example.dell.smartpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

public class ActionQR extends AsyncTask<String, Void, String> {

    ProgressDialog progressDialog;

    private static final String TAG = "ActionQR: ";

    private PresentQRPayment_3 activity;
    private HashMap<String, String> map = null;

    private String payGate;
    private String baseUrl = null;
    private String result = "";
    private String action = "";
    private Boolean timeout = false;

    private String originalOrderID = "";

    public ActionQR(PresentQRPayment_3 activity, String PayGate, String action) {
        this.activity = activity;
        this.payGate = PayGate;
        this.action = action;
    }

    protected void onPreExecute() {

        if (action.equalsIgnoreCase("Cancel")) {
            progressDialog = ProgressDialog.show(activity, "",
                    activity.getString(R.string.cancel_txn),true);
            progressDialog.setCancelable(false);
        }
    }

    @Override
    protected String doInBackground(String... arg) {

        System.out.println("send to paydollar");

        String argMerchantId = arg[0];
        String argOrderId = arg[1];
        String argPMethod = arg[2];
        String argAction = arg[3];

        action = argAction;
        originalOrderID = argOrderId;

        String argTimeout = "";
        if(action.equalsIgnoreCase("inquiry")){
            argTimeout = arg[4];

            if(argTimeout.equalsIgnoreCase("T")){
                timeout = true;
            }
        }

        NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", argMerchantId);
        NameValuePair orderIdNVPair = new BasicNameValuePair("orderId", argOrderId);
        NameValuePair pMethodNVPair = new BasicNameValuePair("pMethod", argPMethod);
        NameValuePair actionNVPair = new BasicNameValuePair("action", argAction);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(merchantIdNVPair);
        nameValuePairs.add(orderIdNVPair);
        nameValuePairs.add(pMethodNVPair);
        nameValuePairs.add(actionNVPair);

        try {
            if (argPMethod.equalsIgnoreCase("PROMPTPAYOFFL")) {
                baseUrl = PayGate.getURL_QRAction(payGate);
            } else if (argPMethod.equalsIgnoreCase("GRABPAYOFFL")) {
                baseUrl = PayGate.getURL_GrabAction(payGate);
            }
            URL url = new URL(baseUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            TrustModifier.relaxHostChecking(con);
            con.setReadTimeout(3000);// readtimeout:4min  decide by serivce response
            con.setConnectTimeout(3000);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            Log.d(TAG,"-----" + "Request Sent:" + PayGate.getQuery(nameValuePairs));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(PayGate.getQuery(nameValuePairs));
            writer.flush();
            writer.close();
            os.close();

            InputStream in = con.getInputStream();
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result = result + line;
                    map = PayGate.split(result);
                    String resultCode = map.get("resultCode");
                    String status = map.get("status");
                    String errCode = map.get("errCode");
                    String returnMsg = map.get("returnMsg");

                    if (resultCode == null) {
                        Log.d(TAG,"Action Failed: " + returnMsg);
                    } else {
                        Log.d(TAG,"Action Status:" + resultCode + " - " + status);
                    }

                }
            } catch (Exception e) {
                Log.d(TAG,"Exception: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.d(TAG,"IOException: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            Log.d(TAG,"Exception: " + e.getMessage());
            result = "resultCode=1&status=&errCode=&returnMsg=Http Request Error&orderId=&trxTime=";
            e.printStackTrace();
        }

        Log.d(TAG,"Result: " + result);
        return result;

    }

    @Override
    protected void onPostExecute(String result) {

        System.out.println("return from paydollar");


        // Close the progressDialog
        if ((progressDialog != null) && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        map = PayGate.split(result);
        String orderId = map.get("orderId") == null ? originalOrderID : map.get("orderId");
        String status = map.get("status");
        String trxTime = map.get("trxTime");
        String returnMsg = map.get("returnMsg");
        String bankRef = map.get("bankRef");

        if(action.equalsIgnoreCase("inquiry")){

            if(status != null){
                if(status.equalsIgnoreCase("Authorized") || status.equalsIgnoreCase("Success")){
                    activity.afterQR("0", orderId, trxTime, returnMsg, bankRef);
                }else if(status.equalsIgnoreCase("Failed")){
                    activity.afterQR("1", orderId, trxTime, returnMsg, bankRef);
                }else {
                    if(timeout){
                        Log.d(TAG, "Action Cancel1");
//                        activity.isQRCancelled = true;
                        activity.cancelQR(orderId);
                    }else{
                        Log.d(TAG, "Action Inquiry1");
                        activity.checkKBANKStatus(orderId);
                    }
                }
            }else{
                Log.d(TAG, "HTTP Request Inquiry Error");
                Log.d(TAG, "Timeout: " + String.valueOf(timeout));
                if(timeout){
                    Log.d(TAG, "Action Cancel2");
                    activity.isQRCancelled = true;
                    activity.cancelQR(orderId);
                }else{
                    Log.d(TAG, "Action Inquiry2");
                    activity.checkKBANKStatus(orderId);
                }
            }

        } else if(action.equalsIgnoreCase("cancel")) {
            Log.d("ActionQR", "123" + String.valueOf(activity.isQRCancelled));
            if(status != null){
                if (status.equalsIgnoreCase("SUCCESS")) {
                    Log.d(TAG, "Cancel Done");
//                    activity.isQRCancelled = true;
                    activity.cancelDone();
                } else {
                    Log.d(TAG, "Cancel Failed");
                    activity.cancelFailed();
                }
            } else {
                Log.d(TAG, "HTTP Request Cancel Error");
                activity.cancelFailed();
            }
        }
    }
}
