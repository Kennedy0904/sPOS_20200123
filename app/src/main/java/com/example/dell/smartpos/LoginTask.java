package com.example.dell.smartpos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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

public class LoginTask extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private Login activity;
    private String payGate;

    private String baseUrl = null;
    private String result = "";
    final int MYDIALOG = 1;
    String merchantId = null;
    String currCode = null;
    String amount = null;
    String orderRef = null;
    String lang = null;
    String payType = null;
    String pMethod = null;
    String cardNo = null;
    String securityCode = null;
    String epMonth = null;
    String epYear = null;
    String cardHolder = null;
    String vbvTransaction = null;
    InputStream inputStream = null;
    EditText merchantIdEditText = null;
    EditText password = null;
    EditText userID = null;

    String fullpath;
    private HashMap<String, String> map = null;

    public LoginTask(Login activity, ProgressDialog progressDialog, String payGate) {
        this.activity = activity;
        this.progressDialog = progressDialog;
        this.payGate = payGate;

    }

    @Override
    protected void onPreExecute() {
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                LoginTask.this.cancel(true);
            }
        });
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... arg0) {
        // TODO Auto-generated method stub
        NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", arg0[0]);
        NameValuePair userIDNVPair = new BasicNameValuePair("userId", arg0[1]);
        NameValuePair passwordNVPair = new BasicNameValuePair("password", arg0[2]);


        System.out.println("---" + merchantIdNVPair + " " + userIDNVPair + " " + passwordNVPair);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(merchantIdNVPair);
        nameValuePairs.add(userIDNVPair);
        nameValuePairs.add(passwordNVPair);

        try {
            baseUrl = PayGate.getURL_merInfo(payGate);
            URL url = new URL(baseUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
        Log.d("MPOS", "LoginResult: " + result);
        return result;
    }

    @Override
    protected void onPostExecute(String resultCode) {
        Log.d("debug", "debug");

        map = PayGate.split(result);
        resultCode = map.get("resultCode");
        if (progressDialog != null) {
            if (!activity.isFinishing()) {
                progressDialog.dismiss();
            }
        }
        if (resultCode == null) {
            //parse html error
            Document doc = Jsoup.parse(result);
            String error = doc.body().text();
            activity.showLoginError(error);
        } else {
            if (resultCode.equals("0")) {
                String Currcode = map.get("cur");
                String merName = map.get("merName");
                String payMethod = map.get("payMethod");
                String payBankId = map.get("payBankId");
                String channelType = map.get("channelType");
                String merClass = map.get("merClass");
                String amexOR = map.get("amexOnlineRefund");
                String visaOR = map.get("visaOnlineRefund");
                String masterOR = map.get("masterOnlineRefund");
                String jcbOR = map.get("jcbOnlineRefund");
                String enableSMS = map.get("enableMPOSMS");
                String hideSurcharge = map.get("hideSurcharge");
                String partnerlogo = map.get("partnerlogo");
                String apiId = map.get("adminId");
                String apipassword = map.get("apipassword");
                String bankKey = map.get("bankKey");
                String bankTerIdArray = map.get("bankTerId");
                String address1 = map.get("address1");
                String address2 = map.get("address2");
                String address3 = map.get("address3");


                System.out.println("---logo-" + partnerlogo);

                if (enableSMS != null) {
                    if (enableSMS.equalsIgnoreCase("T")) {
                        enableSMS = "T";
                    } else {
                        enableSMS = "F";
                    }
                } else {
                    enableSMS = "F";
                }
                String rate = map.get("rate");
                String fixed = map.get("fixed");
                Log.d("OTTO11", "rate:" + rate + ", fixed:" + fixed);


                System.out.println("payBankId sdsdsd: " + payBankId);
                GlobalFunction.setPayBankIdArray(activity, payBankId);
                GlobalFunction.setCardPayBankId(activity, payBankId);
                GlobalFunction.setBankMerIdArray(activity, bankKey);
                GlobalFunction.setTerminalIdArray(activity, bankTerIdArray);
                GlobalFunction.setLoginStatus(activity, true);
                GlobalFunction.setAddress1(activity, address1);
                GlobalFunction.setAddress2(activity, address2);
                GlobalFunction.setAddress3(activity, address3);
                GlobalFunction.setMerLogo(activity, partnerlogo);

                activity.login(Currcode, merName, payMethod, payBankId, channelType, merClass,
                        amexOR, visaOR, masterOR, jcbOR, enableSMS, rate, fixed, hideSurcharge, partnerlogo, apiId, apipassword);
            } else if (resultCode.equals("-1")) {
                activity.showLoginError(map.get("errMsg"));
            }
        }
    }

}


