package com.example.dell.smartpos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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


public class CancelOrder extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private String payGate;
    private HashMap<String, String> map = null;
    private String baseUrl = null;
    private String result = "";
    private String logresult = "";

    private String payRef;
    private String loginId;
    private String password;
    private String actionType;
    private String merchantId;
    private String amount;
    private String payMethod;

    private Boolean retry = false;

    private ScanQRPayment_2 activity;
    private ScanQR_Failure retryActivity;

    public CancelOrder(ScanQRPayment_2 activity, ProgressDialog progressDialog, String PayGate, boolean retry) {
        this.activity = activity;
        this.progressDialog = progressDialog;
        this.payGate = PayGate;
        this.retry = retry;
    }

    public CancelOrder(ScanQR_Failure activity, ProgressDialog progressDialog, String PayGate, boolean retry) {
        this.retryActivity = activity;
        this.progressDialog = progressDialog;
        this.payGate = PayGate;
        this.retry = retry;
    }

    protected void onPreExecute() {
        System.out.println("----progressDialog.show1");
        progressDialog.show();
        System.out.println("----progressDialog.show2");
    }

    @Override
    protected String doInBackground(String... arg0) {
        String argPayref = arg0[0];
        String argLoginId = arg0[1];
        String argpassword = arg0[2];
        String argActionType = arg0[3];
        String argMerchantId = arg0[4];
        String argAmount = arg0[5];
        String argPaymethod = arg0[6];
        String sn = arg0[7];

        payRef = argPayref;
        loginId = argLoginId;
        password = argpassword;
        actionType = argActionType;
        merchantId = argMerchantId;
        amount = argAmount;
        payMethod = argPaymethod;

        NameValuePair payRefNVpair = new BasicNameValuePair("payRef", arg0[0]);
        NameValuePair loginidNVpair = new BasicNameValuePair("loginId", arg0[1]);
        NameValuePair passwordNVpair = new BasicNameValuePair("password", arg0[2]);
        NameValuePair actionTypeNVpair = new BasicNameValuePair("actionType", arg0[3]);
        NameValuePair merchantIdNVpair = new BasicNameValuePair("merchantId", arg0[4]);
        NameValuePair amountNVpair = new BasicNameValuePair("amount", arg0[5]);
        NameValuePair payMethodNVpair = new BasicNameValuePair("payMethod", arg0[6]);
        NameValuePair snNVpair = new BasicNameValuePair("serialNo",sn);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(merchantIdNVpair);
        nameValuePairs.add(payRefNVpair);
        nameValuePairs.add(loginidNVpair);
        nameValuePairs.add(passwordNVpair);
        nameValuePairs.add(actionTypeNVpair);
        nameValuePairs.add(amountNVpair);
        nameValuePairs.add(payMethodNVpair);
        nameValuePairs.add(snNVpair);

        try {

            baseUrl = PayGate.getURL_orderAPI(payGate);

            System.out.println("-----" + "payGate:" + payGate + " , baseUrl:" + baseUrl);
            URL url = new URL(baseUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            TrustModifier.relaxHostChecking(con);
            con.setReadTimeout(30000);// readtimeout:4min  decide by serivce response
            con.setConnectTimeout(25000);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(PayGate.getQuery(nameValuePairs));
            System.out.println("-----" + "manualQuery:" + PayGate.getQuery(nameValuePairs));
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
                }

                logresult += "\r\n" + "while null" + "  ******  ";
            } catch (Exception e) {
                    logresult += "\r\n" + "while failed catch," + e + "  ******  ";
                    e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        logresult += "\r\n" + "while finally try" + "  ******  ";
                        reader.close();
                    } catch (IOException e) {
                        logresult += "\r\n" + "while finally failed," + e + "  ******  ";
                        e.printStackTrace();
                    }
                }
                logresult += "\r\n" + "while finally" + "  ******  ";
            }
        } catch (Exception e) {
            logresult += "\r\n" + "call api failed," + e + "  ******  ";
            result = "successcode=2&Ref=&PayRef=&Amt=&Cur=&prc=-9&src=-9&Ord=&Holder=&AuthId=&TxTime=&errMsg=Http Request Error";
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        map = PayGate.split(result);
        String successcode = map.get("resultCode");

        if (successcode == null) {
            retryActivity.scanToastMsg("Fail Reversal");
        } else {
            if (successcode.equals("0")) {
                retryActivity.scanBBL();
            } else {
                retryActivity.scanToastMsg("Fail Reversal");
            }
        }
        System.out.println("----progressDialog.dismiss1");
        progressDialog.dismiss();
        System.out.println("----progressDialog.dismiss2");
    }

}
