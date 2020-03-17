package com.example.dell.smartpos;

import android.app.ProgressDialog;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MerchantSettingsInfo extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private SettingsFragment fragment;
    private String payGate;
    private HashMap<String, String> map = null;
    private String baseUrl = null;
    private String result = "";
    private String logresult = "";

    private String payType = "";
    private String pMethod = "";
    private String operatorId = "";
    private String cardNo = "";
    private String Surcharge = "";
    private String MerRequestAmt = "";

    public MerchantSettingsInfo(SettingsFragment fragment, ProgressDialog progressDialog, String PayGate) {
        this.fragment = fragment;
        this.progressDialog = progressDialog;
        this.payGate = PayGate;
    }


    protected void onPreExecute() {
        System.out.println("----progressDialog.show1");
        progressDialog.show();
        System.out.println("----progressDialog.show2");
    }

    @Override
    protected String doInBackground(String... arg0) {
        //amount,currCode,MerchantRef,auth_code,pMethod,remark
        String argmerid = arg0[0];
        String argmername = arg0[1];

        System.out.println("-----Settings doInBackground...merchantId:" + argmerid);



        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", argmerid);
        NameValuePair merchantNameNVPair = new BasicNameValuePair("merchantName", argmername);
        nameValuePairs.add(merchantIdNVPair);
        nameValuePairs.add(merchantNameNVPair);


        try {
            String baseUrl = PayGate.getURL_settingInfo(payGate);
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
            logresult += "\r\n" + "call api failed," + e + "  ******  ";
            result = "successcode=2&Ref=&PayRef=&Amt=&Cur=&prc=-9&src=-9&Ord=&Holder=&AuthId=&TxTime=&errMsg=Http Request Error";
            e.printStackTrace();
        }

        System.out.println("-----" + "wechat feedback result:" + logresult);
        System.out.println("-----" + "result" + result);
        Log.d("MY", "-----" + "feedback result:" + logresult);
        Log.d("MY", "-----" + "result" + result);
        System.out.println("MY-----" + "MAP:" + map);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        map = PayGate.split(result);

        String merid = map.get("merID");
        String mername = map.get("merName");
        String lang = map.get("language");
        String merRefNo = map.get("merRefNo");
        String printMode = map.get("printMode");
        String settingsPW = map.get("settingsPW");
        String stationeryOrder = map.get("stationeryOrder");
        String orderEmail = map.get("orderEmail");
        String alipayTO = map.get("alipayTO");
        String wechatpayTO = map.get("wechatpayTO");


//        fragment.getMerchantSettingsInfo(merid, mername, lang, merRefNo, printMode,
//                settingsPW, stationeryOrder, orderEmail, alipayTO,
//                wechatpayTO);


        System.out.println("----progressDialog.dismiss1");
        progressDialog.dismiss();
        System.out.println("----progressDialog.dismiss2");
    }


}
