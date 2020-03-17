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

public class OctopusQR_Enquiry extends AsyncTask<String, Void, String> {
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


    public OctopusQR_Enquiry(PresentQRPayment_3 activity, String PayGate) {
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


        java.util.Date currentDate_we = new java.util.Date();
        SimpleDateFormat formatter_we = new SimpleDateFormat("ddMMyyyykkmmss");
        String epMonth = (formatter_we.format(currentDate_we)).substring(2, 4);
        String epYear = (formatter_we.format(currentDate_we)).substring(4, 8);

        String gatewayRef = arg0[0];
        String businessDate = arg0[1];
        String argamount = arg0[2];
        String argcurrCode = arg0[3];
        String argmerRequestAmt = arg0[4];
        String argmerid = arg0[5];
        String argmerName = arg0[6];
        String argmerref = arg0[7];
        String argpaymethod = arg0[8];
        String arguserid = arg0[9];
        String argsurcharge = arg0[10];
        String argorderid = arg0[11];
        String argexpirytime = arg0[12];

        payType = "N";
        pMethod = argpaymethod;
        operatorId = arguserid;
        merName1 = argmerName;

        System.out.println("---KJ order id=" + argorderid + ";PGref=" + gatewayRef + ";amount=" + argamount);
        NameValuePair gatewayRefNVPair = new BasicNameValuePair("gatewayRef", gatewayRef);
        NameValuePair businessDateNVPair = new BasicNameValuePair("businessDate", businessDate);
        NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", argmerid);
        NameValuePair currCodeNVPair = new BasicNameValuePair("currCode", argcurrCode);
        NameValuePair amountNVPair = new BasicNameValuePair("amount", argamount);
        NameValuePair merRequestAmtNVPair = new BasicNameValuePair("merRequestAmt", argmerRequestAmt);
        NameValuePair langNVPair = new BasicNameValuePair("lang", "E");
        NameValuePair payTypeNVPair = new BasicNameValuePair("payType", "N");
        NameValuePair orderRefNVPair = new BasicNameValuePair("orderRef", argmerref);
        NameValuePair pMethodNVPair = new BasicNameValuePair("pMethod", argpaymethod);
        NameValuePair epMonthNVPair = new BasicNameValuePair("epMonth", epMonth);
        NameValuePair epYearNVPair = new BasicNameValuePair("epYear", epYear);
        NameValuePair cardHolderNVPair = new BasicNameValuePair("cardHolder", "OEPAYOFFL");
        NameValuePair cardNoNVPair = new BasicNameValuePair("cardNo", "4518354303130007");
        NameValuePair vbvTransactionNVPair = new BasicNameValuePair("vbvTransaction", "");
        NameValuePair secureHashNVPair = new BasicNameValuePair("secureHash", "");
        NameValuePair operatorIdNVPair = new BasicNameValuePair("operatorId", arguserid);
        NameValuePair channelTypeNVPair = new BasicNameValuePair("channelType", "MPOS");
        NameValuePair useSurchageTypeNVPair = new BasicNameValuePair("useSurcharge", argsurcharge);
        NameValuePair orderIdNVPair = new BasicNameValuePair("orderId", argorderid);
        NameValuePair requestNVPair = new BasicNameValuePair("octopusRequest", "enquiry");
        NameValuePair expiryMinuteNVPair = new BasicNameValuePair("expiryMinute", argexpirytime);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(gatewayRefNVPair);
        nameValuePairs.add(businessDateNVPair);
        nameValuePairs.add(merchantIdNVPair);
        nameValuePairs.add(currCodeNVPair);
        nameValuePairs.add(amountNVPair);
        nameValuePairs.add(merRequestAmtNVPair);
        nameValuePairs.add(orderRefNVPair);
        nameValuePairs.add(langNVPair);
        nameValuePairs.add(payTypeNVPair);
        nameValuePairs.add(epMonthNVPair);
        nameValuePairs.add(epYearNVPair);
        nameValuePairs.add(pMethodNVPair);
        nameValuePairs.add(cardHolderNVPair);
        nameValuePairs.add(cardNoNVPair);
        nameValuePairs.add(vbvTransactionNVPair);
        nameValuePairs.add(secureHashNVPair);
        nameValuePairs.add(operatorIdNVPair);
        nameValuePairs.add(channelTypeNVPair);
        nameValuePairs.add(useSurchageTypeNVPair);
        nameValuePairs.add(orderIdNVPair);
        nameValuePairs.add(requestNVPair);
        nameValuePairs.add(expiryMinuteNVPair);

        try {
            System.out.println("---KJ do in background OctopusQR_Enquiry: Enter paycompMPOS");
            baseUrl = PayGate.getURL_PayComp(payGate);
//            baseUrl = "https://test.paydollar.com/b2cDemo/OctopusEnquiry.jsp";
//            baseUrl = "http://192.168.1.106:8080/octopus/OctopusEnquiry.jsp";
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
                System.out.println("---KJ do in background OctopusQR_Enquiry: get result from payCompMPOS");
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result = result + line;
                    map = PayGate.splitOctopus(result);

                    String PayRef = map.get("PayRef");
                    String errcode = map.get("errMsg");
                    String successcode = map.get("successcode");


                    if (successcode == null) {
                        System.out.println("---Enquiry Octopus result = null");

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
            System.out.println("---KJ do in background OctopusQR_Enquiry: error--" + e);
            result = "successcode=2&Ref=&PayRef=&Amt=" + argamount + "&Cur=&prc=-9&src=-9&Ord=&Holder=&AuthId=&TxTime=&errMsg=Http Request Error";
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("---KJ OctopusQR_Enquiry: onPostExecute");
        map = PayGate.splitOctopus(result);

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

        Currcode = CurrCode.getName(Currcode);

        if (successcode == null) {
            System.out.println("-----" + "successcod:null,Ref:" + merchant_ref_no + "," +
                    "PayRef:" + PayRef + ",amount:" + amount + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode);
            activity.fail(successcode, merchantid, mername, payType, pMethod, operatorId, cardNo,
                    merchant_ref_no, PayRef, amount, MerRequestAmt, Surcharge, Currcode, Time, errcode);
        } else {
            if (successcode.equals("0")) {
                System.out.println("-----" + "successcod:" + successcode + "," +
                        "Ref:" + merchant_ref_no + ",PayRef:" + PayRef + ",amount:" + amount + "," +
                        "Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode);
                System.out.println("-----Scanback--merchant_ref_no:" + merchant_ref_no);
                System.out.println("-----Scanback--payref:" + PayRef);
                System.out.println("-----Scanback--amount:" + amount);
                System.out.println("-----Scanback--MerRequestAmt:" + MerRequestAmt);
                System.out.println("-----Scanback--Surcharge:" + Surcharge);
                System.out.println("-----Scanback--Currcode:" + Currcode);
                System.out.println("-----Scanback--Time:" + Time);
                System.out.println("-----Scanback--errcode:" + errcode);
                System.out.println("-----Scanback--merchantid;" + merchantid);
                System.out.println("-----Scanback--mername:" + mername);
                System.out.println("-----Scanback--payType:" + payType);
                System.out.println("-----Scanback--pMethod:" + pMethod);
                System.out.println("-----Scanback--operatorId:" + operatorId);
                System.out.println("-----Scanback--cardNo:" + cardNo);

                if (src.equals("0") && prc.equals("0")) {
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
                } else if (prc.equals("9") && src.equals("94")) {
                    activity.cancelDone();
                }


            } else {
                if (prc.equals("9") && src.equals("94")) {
                    activity.cancelDone();
                } else {
                    System.out.println("----KJ-fail-" + "successcod:" + successcode + ",Ref:" + merchant_ref_no + "," +
                            "PayRef:" + PayRef + ",amount:" + amount + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode);
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
        }
        System.out.println("----progressDialog.dismiss1");
//        progressDialog.dismiss();
        System.out.println("----progressDialog.dismiss2");

//        activity.handleOctopusStatus(status);
    }

}
