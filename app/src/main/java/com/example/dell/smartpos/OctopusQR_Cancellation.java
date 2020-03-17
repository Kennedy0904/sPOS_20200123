package com.example.dell.smartpos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OctopusQR_Cancellation extends AsyncTask<String, Void, String> {
    private ProgressDialog progressDialog;
    private PresentQRPayment_3 activity;
    private String payGate;
    private HashMap<String, String> map = null;
    private String baseUrl = null;
    private String result = "";

    private String payType = "";
    private String pMethod = "";
    private String operatorId = "";
    private String cardNo = "";
    private String Surcharge = "";
    private String MerRequestAmt = "";


    public OctopusQR_Cancellation(PresentQRPayment_3 activity, ProgressDialog progressDialog, String PayGate) {
        this.activity = activity;
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
        System.out.println("---KJ do in background OctopusQR_Cancellation");

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
        String argmerref = arg0[6];
        String argpaymethod = arg0[7];
        String arguserid = arg0[8];
        String argsurcharge = arg0[9];
        String argorderid = arg0[10];
        String argexpirytime = arg0[11];

        payType = "N";
        pMethod = argpaymethod;
        operatorId = arguserid;

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
        NameValuePair cardHolderNVPair = new BasicNameValuePair("cardHolder", "OEPAYOFFL");
        NameValuePair cardNoNVPair = new BasicNameValuePair("cardNo", "4518354303130007");
        NameValuePair epMonthNVPair = new BasicNameValuePair("epMonth", epMonth);
        NameValuePair epYearNVPair = new BasicNameValuePair("epYear", epYear);
        NameValuePair vbvTransactionNVPair = new BasicNameValuePair("vbvTransaction", "");
        NameValuePair secureHashNVPair = new BasicNameValuePair("secureHash", "");
        NameValuePair operatorIdNVPair = new BasicNameValuePair("operatorId", arguserid);
        NameValuePair channelTypeNVPair = new BasicNameValuePair("channelType", "MPOS");
        NameValuePair useSurchageTypeNVPair = new BasicNameValuePair("useSurcharge", argsurcharge);
        NameValuePair orderIdNVPair = new BasicNameValuePair("orderId", argorderid);
        NameValuePair requestNVPair = new BasicNameValuePair("octopusRequest", "cancel");
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
        nameValuePairs.add(pMethodNVPair);
        nameValuePairs.add(cardHolderNVPair);
        nameValuePairs.add(cardNoNVPair);
        nameValuePairs.add(epMonthNVPair);
        nameValuePairs.add(epYearNVPair);
        nameValuePairs.add(vbvTransactionNVPair);
        nameValuePairs.add(secureHashNVPair);
        nameValuePairs.add(operatorIdNVPair);
        nameValuePairs.add(channelTypeNVPair);
        nameValuePairs.add(useSurchageTypeNVPair);
        nameValuePairs.add(orderIdNVPair);
        nameValuePairs.add(requestNVPair);
        nameValuePairs.add(expiryMinuteNVPair);

        try {
            System.out.println("---KJ do in background OctopusQR_Cancellation: enter payCompMPOS");
            baseUrl = PayGate.getURL_PayComp(payGate);
//            baseUrl = "https://test.paydollar.com/b2cDemo/OctopusCancel.jsp";
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
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result = result + line;
                    map = PayGate.splitOctopus(result);
                    status = map.get("status");
                }
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
            result = "successcode=2&Ref=&PayRef=&Amt=&Cur=&prc=-9&src=-9&Ord=&Holder=&AuthId=&TxTime=&errMsg=Http Request Error";
            System.out.println("KJ---error cancel:" + e);
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("---KJ OctopusQR_Cancellation: onPostExecute");
        map = PayGate.splitOctopus(result);

        String merchantid = map.get("merchantId");
        String mername = map.get("merName");

        String successcode = map.get("successcode");

        String PayRef = map.get("PayRef");
        String receiptId = map.get("receiptID");
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

                if (src.equals("50") && prc.equals("50")) {

                    //pop error message : cannot be cancelled
                    Toast.makeText(this.activity, R.string.cannot_cancel,
                            Toast.LENGTH_LONG).show();
                   /* activity.success(successcode, merchant_ref_no, PayRef, amount,
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
                            receiptId);*/
                } else if ((src.equals("94") && prc.equals("1")) || (src.equals("94") && prc.equals("9"))) {
                    System.out.println("---KJ-cancel success");
                    activity.cancelDone();
                } else if ((src.equals("100") && prc.equals("8")) || (src.equals("100") && prc.equals("6"))) {
                    System.out.println("---KJ-cancel fail");
                    Toast.makeText(this.activity, R.string.cannot_cancel,
                            Toast.LENGTH_LONG).show();
                    activity.continueEnquiry();

                } else {
                    //fail transaction
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

            } else {
                System.out.println("-----" + "successcod:" + successcode + ",Ref:" + merchant_ref_no + "," +
                        "PayRef:" + PayRef + ",amount:" + amount + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode);

                //pop error message : cannot be cancelled
                Toast.makeText(this.activity, R.string.cannot_cancel,
                        Toast.LENGTH_LONG).show();
            }
        }
        System.out.println("----progressDialog.dismiss1");
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        System.out.println("----progressDialog.dismiss2");

    }

}

