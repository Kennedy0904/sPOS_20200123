package com.example.dell.smartpos;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class KBankQR extends AsyncTask<String, Void, QRCode> {

    private static final String TAG = "KBankQR: ";

    private ProgressDialog progressDialog;
    private PresentQRPayment_3 activity;
    private String payGate;
    private HashMap<String, String> map = null;
    private String baseUrl = null;
    private String result = "";
    private String logresult = "";
    Date expiry_time = null;

    private String payType = "";
    private String pMethod = "";
    private String operatorId = "";
    private String cardNo = "";
    private String Surcharge = "";
    private String MerRequestAmt = "";

    String ref = "";
    String successcode = "";
    String base64QR = "";
    String token = "";
    String orderId = "";
    String documentTime;
    String expiryTime;

    private Bitmap bitmap;
    private QRCode qrCode = new QRCode();

    public KBankQR(PresentQRPayment_3 activity, ProgressDialog progressDialog, String PayGate) {
        this.activity = activity;
        this.progressDialog = progressDialog;
        this.payGate = PayGate;
    }

    protected void onPreExecute() {
        progressDialog.show();
    }


    @Override
    protected QRCode doInBackground(String... arg) {

        String argMerchantId = arg[0];
        String argAmount = arg[1];
        String argMerRequestAmt = arg[2];
        String argCurrCode = arg[3];
        String argMerRef = arg[4];
        String argPayType = arg[5];
        String argPayMethod = arg[6];
        String argOperatorId = arg[7];

        pMethod = argPayMethod;

        NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", argMerchantId);
        NameValuePair amountNVPair = new BasicNameValuePair("amount", argAmount);
        NameValuePair merRequestAmtNVPair = new BasicNameValuePair("merRequestAmt", argMerRequestAmt);
        NameValuePair currCodeNVPair = new BasicNameValuePair("currCode", argCurrCode);
        NameValuePair orderRefNVPair = new BasicNameValuePair("orderRef", argMerRef);
        NameValuePair payTypeNVPair = new BasicNameValuePair("payType", argPayType);
        NameValuePair pMethodNVPair = new BasicNameValuePair("pMethod", argPayMethod);
        NameValuePair operatorIdNVPair = new BasicNameValuePair("operatorId", argOperatorId);
        NameValuePair langNVPair = new BasicNameValuePair("lang", "E");
        NameValuePair channelTypeNVPair = new BasicNameValuePair("channelType", "MPOS");
        NameValuePair presentQRNVPair = new BasicNameValuePair("presentQR", "T");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(merchantIdNVPair);
        nameValuePairs.add(amountNVPair);
        nameValuePairs.add(merRequestAmtNVPair);
        nameValuePairs.add(currCodeNVPair);
        nameValuePairs.add(orderRefNVPair);
        nameValuePairs.add(payTypeNVPair);
        nameValuePairs.add(pMethodNVPair);
        nameValuePairs.add(operatorIdNVPair);
        nameValuePairs.add(langNVPair);
        nameValuePairs.add(channelTypeNVPair);
        nameValuePairs.add(presentQRNVPair);

        try {
            baseUrl = PayGate.getURL_PayComp(payGate);
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
            Log.d(TAG,"-----" + "manualQuery:" + PayGate.getQuery(nameValuePairs));
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

                    Log.d(TAG,"Result: " + result);

                    map = PayGate.split(result);
                    String PayRef = map.get("PayRef");
                    String errcode = map.get("errMsg");
                    String successcode = map.get("successcode");
                    String QRCode = map.get("QRCode");
                    String QRRef = map.get("Ord");
                    String QRRef2 = map.get("QRRef");
                    String TxTime = map.get("TxTime");

                    qrCode.setOrderId(PayRef);
                    qrCode.setQRRef(QRRef);
                    qrCode.setQRRef2(QRRef2);
                    qrCode.setCreatedTime(TxTime);

                    if (successcode == null) {
                        logresult += "1 successcode:null,PayRef:" + PayRef + "," +
                                "errcode" + errcode + "------amount:" + argAmount + ",currCode:" + argCurrCode + "," +
                                "orderRef:" + argMerRef + ",pMethod:" + argPayMethod + "  ******  ";
                    } else {
                        if (successcode.equals("0")) {
                            try {

                                if(pMethod.equalsIgnoreCase("PROMPTPAYOFFL")){
                                    byte[] decodedString = android.util.Base64.decode(QRCode, Base64.DEFAULT);
                                    bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                }else{
                                    bitmap = activity.TextToImageEncode(QRCode);
                                }

                                qrCode.setBitmapQR(bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String amount = map.get("Amt");
                            String Currcode = map.get("Cur");
                            String pMethod = map.get("PayMethod");
                            logresult += "\r\n" + "2 successcode:" + successcode + "," +
                                    "PayRef:" + PayRef + ",amount:" + amount + "," +
                                    "Currcode:" + Currcode + "," +
                                    "PayMethod:" + pMethod + "," +
                                    "errcode" + errcode + "------amount:" + argAmount + ",currCode:" + argCurrCode + "," +
                                    "orderRef:" + argMerRef + ",pMethod:" + argPayMethod + "  ******  ";

                        } else {
                            logresult += "\r\n" + "5 successcode:" + successcode + "," +
                                    "PayRef:" + PayRef + ",errcode" + errcode + "------amount:" + argAmount + ",currCode:" + argCurrCode + "," +
                                    "orderRef:" + argMerRef + ",pMethod:" + argPayMethod + "  ******  ";
                        }
                    }
                }

//                Log.d(TAG,"Result: " + result);
                Log.d(TAG,"Log Result: " + logresult);

            } catch (Exception e) {
                logresult += "\r\n" + "while failed catch," + e + "  ******  ";
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        logresult += "\r\n" + "while finally failed," + e + "  ******  ";
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            result = "successcode=2&Ref=&PayRef=&Amt=&Cur=&prc=-9&src=-9&Ord=&Holder=&AuthId=&TxTime=&errMsg=Http Request Error";
            e.printStackTrace();
        }

        return qrCode;
    }

    @Override
    protected void onPostExecute(QRCode qrCode) {

        if (bitmap != null) {
            activity.displayQR(qrCode.getBitmapQR(), qrCode.getOrderId(), qrCode.getQRRef(), qrCode.getQRRef2());
            activity.setTimeoutTimer();
        }else {
            activity.showQRError();
        }

        progressDialog.dismiss();
    }
}
