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


public class QRTrxInq extends AsyncTask<String, Void, String> {

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
    private String authCode = "";

    private String argbatchno = "";
    private String argdeviceserial = "";
    private String argSoftwareVersion = "";

    private Boolean retry = false;

    private ScanQRPayment_2 activity;
    private ScanQR_Failure retryActivity;

    public QRTrxInq(ScanQRPayment_2 activity, ProgressDialog progressDialog, String PayGate, boolean retry) {
        this.activity = activity;
        this.progressDialog = progressDialog;
        this.payGate = PayGate;
        this.retry = retry;
    }

    public QRTrxInq(ScanQR_Failure activity, ProgressDialog progressDialog, String PayGate, boolean retry) {
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
        String argamount = arg0[0];
        String argmerRequestAmt = arg0[1];
        String argsurcharge = arg0[2];
        String argcurrCode = arg0[3];
        String argmerchantRef = arg0[4];
        String argauthcode = arg0[5];
        String argpmethod = arg0[6];
        String argmerchantId = arg0[7];
        String argmerName = arg0[8];
        String argoperatorId = arg0[9];
        String arguseC = arg0[10];
        argbatchno = arg0[11];
        argdeviceserial = arg0[12];
        argSoftwareVersion = arg0[13];


        String payRef = arg0[14];

        System.out.println("-----QR doInBackground...amount:" + argamount + "," +
                "argmerRequestAmt:" + argmerRequestAmt + "," +
                "surcharge:" + argsurcharge + "," +
                "currCode:" + argcurrCode + "," +
                "merchantRef:" + argmerchantRef + "," +
                "argauthcode:" + argauthcode + "," +
                "pmethod:" + argpmethod + "," +
                "merchantId:" + argmerchantId + "," +
                "merName:" + argmerName + "," +
                "operatorId:" + argoperatorId + "," +
                "useSurcharge:" + arguseC);
        Log.d("QR", "----- doInBackground...amount:" + argamount + "," +
                "argmerRequestAmt:" + argmerRequestAmt + "," +
                "surcharge:" + argsurcharge + "," +
                "currCode:" + argcurrCode + "," +
                "merchantRef:" + argmerchantRef + "," +
                "argauthcode:" + argauthcode + "," +
                "pmethod:" + argpmethod + "," +
                "merchantId:" + argmerchantId + "," +
                "merName:" + argmerName + "," +
                "operatorId:" + argoperatorId + "," +
                "useSurcharge:" + arguseC);

        payType = "N";
        pMethod = argpmethod;
        operatorId = argoperatorId;
        cardNo = argauthcode;
        Surcharge = argsurcharge;
        MerRequestAmt = argmerRequestAmt;
        authCode = argauthcode;

        argcurrCode = CurrCode.getCode(argcurrCode);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            java.util.Date currentDate_we = new java.util.Date();
            SimpleDateFormat formatter_we = new SimpleDateFormat("ddMMyyyykkmmss");
            String epMonth = (formatter_we.format(currentDate_we)).substring(2, 4);
            String epYear = (formatter_we.format(currentDate_we)).substring(4, 8);

            NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", argmerchantId);
            NameValuePair currCodeNVPair = new BasicNameValuePair("currCode", argcurrCode);
            NameValuePair amountNVPair = new BasicNameValuePair("amount", argamount);
            NameValuePair merRequestAmtNVPair = new BasicNameValuePair("merRequestAmt", argmerRequestAmt);
            NameValuePair surchargeNVPair = new BasicNameValuePair("surcharge", argsurcharge);
            NameValuePair langNVPair = new BasicNameValuePair("lang", "E");
            NameValuePair payTypeNVPair = new BasicNameValuePair("payType", "N");
            NameValuePair orderRefNVPair = new BasicNameValuePair("orderRef", argmerchantRef);
            NameValuePair pMethodNVPair = new BasicNameValuePair("pMethod", argpmethod);
            NameValuePair cardNoNVPair = new BasicNameValuePair("cardNo", argauthcode);
            NameValuePair epMonthNVPair = new BasicNameValuePair("epMonth", epMonth);
            NameValuePair epYearNVPair = new BasicNameValuePair("epYear", epYear);
            NameValuePair cardHolderNVPair = new BasicNameValuePair("cardHolder",
                    argpmethod.equalsIgnoreCase("ALIPAYHKOFFL") ? "AlipayHKOFFL" :
                            argpmethod.equalsIgnoreCase("ALIPAYOFFL") ? "AlipayOFFL" :
                                    argpmethod.equalsIgnoreCase("ALIPAYTHOFFL") ? "ALIPAYTHOFFL" :
                                            argpmethod.equalsIgnoreCase("WECHATOFFL") ? "WECHATOFFLINE" :
                                                    argpmethod.equalsIgnoreCase("BOOSTOFFL") ? "BOOSTOFFL" : "");
            NameValuePair vbvTransactionNVPair = new BasicNameValuePair("vbvTransaction", "");
            NameValuePair secureHashNVPair = new BasicNameValuePair("secureHash", "");
            NameValuePair operatorIdNVPair = new BasicNameValuePair("operatorId", argoperatorId);
            NameValuePair channelTypeNVPair = new BasicNameValuePair("channelType", "MPOS");
            NameValuePair useSurchageTypeNVPair = new BasicNameValuePair("useSurcharge", arguseC);
            NameValuePair payRefNVpair = new BasicNameValuePair("orderId", payRef);


            NameValuePair requestNVPair = new BasicNameValuePair("bblRequest", "queryTrx");
            nameValuePairs.add(requestNVPair);


        if(argbatchno.isEmpty() || argbatchno != null){
            NameValuePair batchNvPair = new BasicNameValuePair("batchNo", argbatchno);
            nameValuePairs.add(batchNvPair);
        }


            NameValuePair serialNvPair = new BasicNameValuePair("serialNo", argdeviceserial);
            NameValuePair sVersionNvPair = new BasicNameValuePair("softwareVersion", argSoftwareVersion);

            nameValuePairs.add(sVersionNvPair);
            nameValuePairs.add(serialNvPair);
            nameValuePairs.add(merchantIdNVPair);
            nameValuePairs.add(currCodeNVPair);
            nameValuePairs.add(amountNVPair);
            nameValuePairs.add(merRequestAmtNVPair);
            nameValuePairs.add(surchargeNVPair);
            nameValuePairs.add(orderRefNVPair);
            nameValuePairs.add(langNVPair);
            nameValuePairs.add(payTypeNVPair);
            nameValuePairs.add(pMethodNVPair);
            nameValuePairs.add(cardNoNVPair);
            nameValuePairs.add(epMonthNVPair);
            nameValuePairs.add(epYearNVPair);
            nameValuePairs.add(cardHolderNVPair);
            nameValuePairs.add(vbvTransactionNVPair);
            nameValuePairs.add(secureHashNVPair);
            nameValuePairs.add(operatorIdNVPair);
            nameValuePairs.add(channelTypeNVPair);
            nameValuePairs.add(useSurchageTypeNVPair);
            nameValuePairs.add(payRefNVpair);

        try {

            baseUrl = PayGate.getURL_PayComp(payGate);

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
                    String PayRef = map.get("PayRef");
                    String errcode = map.get("errMsg");
                    String successcode = map.get("successcode");
                    if (successcode == null) {
                        logresult += "1 successcode:null,PayRef:" + PayRef + "," +
                                "errcode" + errcode + "------amount:" + argamount + ",currCode:" + argcurrCode + "," +
                                "orderRef:" + argmerchantRef + ",auth_code:" + argauthcode + "," +
                                "pMethod:" + argpmethod + "  ******  ";
                    } else {
                        if (successcode.equals("0")) {
                            String amount = map.get("Amt");
                            String Currcode = map.get("Cur");
                            String pMethod = map.get("PayMethod");
                            logresult += "\r\n" + "2 successcode:" + successcode + "," +
                                    "PayRef:" + PayRef + ",amount:" + amount + "," +
                                    "Currcode:" + Currcode + "," +
                                    "PayMethod:" + pMethod + "," +
                                    "errcode" + errcode + "------amount:" + argamount + "," +
                                    "currCode:" + argcurrCode + ",orderRef:" + argmerchantRef + "," +
                                    "auth_code:" + argauthcode + ",pMethod:" + argpmethod + "  ******  ";
                        } else {
                            logresult += "\r\n" + "5 successcode:" + successcode + "," +
                                    "PayRef:" + PayRef + ",errcode" + errcode + "------amount:" + argamount + "," +
                                    "currCode:" + argcurrCode + ",orderRef:" + argmerchantRef + "," +
                                    "auth_code:" + argauthcode + ",pMethod:" + argpmethod + "  ******  ";
                        }
                    }
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

        System.out.println("-----" + "wechat feedback result:" + logresult);
        System.out.println("-----" + "result" + result);
        result += "&merchantId=" + argmerchantId + "&merName=" + argmerName;
        Log.d("MY", "-----" + "feedback result:" + logresult);
        Log.d("MY", "-----" + "result" + result);
        System.out.println("MY-----" + "MAP:" + map);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        map = PayGate.split(result);

        String merchantid = map.get("merchantId");
        String mername = map.get("merName");

        String successcode = map.get("successcode");

        String PayRef = map.get("PayRef");
        String errcode = map.get("errMsg");
        String merchant_ref_no = map.get("Ref");
        String amount = map.get("Amt");
        String Currcode = map.get("Cur");
        String Time = map.get("TxTime");
        String partnerTrxId = map.get("Ord");
        String approvalCode = map.get("AuthId");

        String printText = map.get("printText");

        Currcode = CurrCode.getName(Currcode);

        if (printText != null) {
            String batchNo = map.get("batchNo");
            String systemTraceNo = map.get("batchNo");
            String host = map.get("host");
            String tid = map.get("TId");
            String mid = map.get("MId");

            retryActivity.scanbackBBL(
                    successcode,
                    merchant_ref_no,
                    PayRef, amount,
                    amount,
                    Surcharge,
                    Currcode,
                    Time,
                    errcode,
                    merchantid,
                    mername,
                    payType,
                    pMethod,
                    operatorId,
                    cardNo,
                    printText,
                    "BBL",
                    batchNo,
                    systemTraceNo,
                    host,
                    tid,
                    mid,
                    partnerTrxId,
                    approvalCode);
        } else {

            if (successcode == null) {
                System.out.println("-----" + "successcod:null,Ref:" + merchant_ref_no + "," +
                        "PayRef:" + PayRef + ",amount:" + amount + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode);
                if (!retry) {
                    activity.scanfail(successcode, merchantid, mername, payType, pMethod, operatorId, cardNo,
                            merchant_ref_no, PayRef, amount, MerRequestAmt, Surcharge, Currcode, Time, errcode, authCode);
                } else {
                    retryActivity.scanfail();
                }
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

                    if (!retry) {
                        activity.scanback(successcode, merchant_ref_no, PayRef, amount,
                                MerRequestAmt,
                                Surcharge,
                                Currcode,
                                Time,
                                errcode,
                                merchantid,
                                mername,
                                payType,
                                pMethod,
                                operatorId,
                                cardNo);
                    } else {
                        retryActivity.scanback(successcode, merchant_ref_no, PayRef, amount,
                                MerRequestAmt,
                                Surcharge,
                                Currcode,
                                Time,
                                errcode,
                                merchantid,
                                mername,
                                payType,
                                pMethod,
                                operatorId,
                                cardNo);
                    }

                } else {
                    System.out.println("-----" + "successcod:" + successcode + ",Ref:" + merchant_ref_no + "," +
                            "PayRef:" + PayRef + ",amount:" + amount + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode);

                    if (!retry) {
                        activity.scanfail(successcode, merchantid, mername, payType,
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
                                errcode,
                                authCode);
                    } else {
                        retryActivity.scanfail();
                    }
                }
            }
        }
        System.out.println("----progressDialog.dismiss1");
        progressDialog.dismiss();
        System.out.println("----progressDialog.dismiss2");
    }

}
