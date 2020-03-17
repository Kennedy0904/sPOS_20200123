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

public class BoostQR_Enquiry extends AsyncTask<String, Void, String> {
    private PresentQRPayment_3 activity;
    private ProgressDialog progressDialog;
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
    private String merName1 = "";


    public BoostQR_Enquiry(PresentQRPayment_3 activity, String PayGate) {
        this.activity = activity;
//        this.progressDialog = progressDialog;
        this.payGate = PayGate;
    }


    protected void onPreExecute() {
//        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... arg0) {
        System.out.println("---KJ do in background OctopusQR_Enquiry");

        String argmerid = arg0[0];
        String argorderid = arg0[1];
        String arguserid = arg0[2];
        String argmerName = arg0[3];

        payType = "N";
        operatorId = arguserid;
        merName1 = argmerName;

        NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", argmerid);
        NameValuePair orderIdNVPair = new BasicNameValuePair("orderId", argorderid);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(merchantIdNVPair);
        nameValuePairs.add(orderIdNVPair);

        try {
            System.out.println("---KJ do in background BoostQR_Enquiry: Enter paycompMPOS");
            baseUrl = PayGate.getURL_CheckBoostStatus(payGate);
            URL url = new URL(baseUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            TrustModifier.relaxHostChecking(con);
            con.setReadTimeout(30000);// readtimeout:4min  decide by serivce response
            con.setConnectTimeout(25000);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(PayGate.getQuery(nameValuePairs));
            writer.flush();
            writer.close();
            os.close();

            InputStream in = con.getInputStream();
            BufferedReader reader = null;

            String status = "";
            try {
                System.out.println("---KJ do in background BoostQR_Enquiry: get result from payCompMPOS");
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result = result + line;
                    map = PayGate.split(result);

                    String PayRef = map.get("PayRef");
                    String errcode = map.get("errMsg");
                    String successcode = map.get("successcode");


                    if (successcode == null) {
                        System.out.println("---Enquiry Boost result = null");

                    } else {
                        if (successcode.equals("0")) {
                            String amount = map.get("Amt");
                            String Currcode = map.get("Cur");
                            String pMethod = map.get("PayMethod");

                            String merchantid = map.get("merchantId");
                            String mername = map.get("merName");

                            String merchant_ref_no = map.get("Ref");
                            String Time = map.get("TxTime");

                            Currcode = CurrCode.getName(Currcode);

                        } else {

                        }
                    }
                }
                System.out.println("KJ---enquiry result:" + result);
                Log.d("result123", status);

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
            System.out.println("---KJ do in background BoostQR_Enquiry: error--" + e);
            result = "successcode=2&Ref=&PayRef=&Amt=" + "&Cur=&prc=-9&src=-9&Ord=&Holder=&AuthId=&TxTime=&errMsg=Http Request Error";
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("---KJ OctopusQR_Enquiry: onPostExecute");
        map = PayGate.split(result);

        String merchantid = map.get("merchantId");
        String mername = map.get("merName");

        String successcode = map.get("successcode");
        String receiptId = map.get("receiptID");
        String PayRef = map.get("PayRef");
        String errcode = map.get("errMsg");
        String merchant_ref_no = map.get("Ref");
        String amount = map.get("Amt");
        String Currcode = map.get("Cur");
        String Time = map.get("TxTime");
        String octopusStatus = map.get("Status");
        String prc = map.get("prc");
        String src = map.get("src");
        String Ord = map.get("Ord");
        String Holder = map.get("Holder");
        String AuthId = map.get("AuthId");
        String TxTime = map.get("TxTime");
        String errMsg = map.get("errMsg");
        pMethod = map.get("PayMethod");

        Currcode = CurrCode.getName(Currcode);

        if (successcode == null) {
            System.out.println("-----" + "successcod:null,Ref:" + merchant_ref_no + "," +
                    "PayRef:" + PayRef + ",amount:" + amount + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode);
            activity.fail(successcode, merchantid, mername, payType, pMethod, operatorId, cardNo,
                    merchant_ref_no, PayRef, amount, MerRequestAmt, Surcharge, Currcode, Time, errcode);
        } else {
            if (successcode.equals("0")) {
                System.out.println("-----" + "successcod:0,Ref:" + merchant_ref_no + "," +
                        "PayRef:" + PayRef + ",amount:" + amount + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode + ",pMethod:" + pMethod);

                activity.success(successcode, merchant_ref_no, PayRef, amount,
                        MerRequestAmt,
                        Surcharge,
                        Currcode,
                        Time,
                        errcode,
                        merchantid,
                        merName1,
                        payType,
                        pMethod,
                        operatorId,
                        receiptId);
            } else if(successcode.equals("1")){
                activity.fail(successcode, merchantid, mername, payType,
                        pMethod,
                        operatorId,
                        cardNo,
                        merchant_ref_no,
                        PayRef,
                        amount,
                        MerRequestAmt,
                        Surcharge,
                        Currcode,
                        Time,
                        errcode);
            }
        }
        System.out.println("----progressDialog.dismiss1");
//        progressDialog.dismiss();
        System.out.println("----progressDialog.dismiss2");

//        activity.handleOctopusStatus(status);
    }

}
