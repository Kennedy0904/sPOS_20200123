package com.example.dell.smartpos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dell.smartpos.CardModule.tradepaypw.TradeResult;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CardPaymentTask extends AsyncTask<String, Void, String> {

//    private SwingCardActivity activity;
    private TradeResult activity;

    //    private CardPayment_ManualKeyIn activity1;
    private String payGate;
    private ProgressDialog progressDialog;

    private String cardNo;
    private String ProcessingCode;
    private String amount;
    private String merRef;
    private String traceNo;
    private String POSEntryMode;
    private String PANSeqNo  = "";
    private String POSCondtionCode;
    private String Track2Data;
    private String EnryptedPIN = "";
    private String EMVICCRelatedData = "";
    private String InvoiceRef;

    private String merId;
    private String encryptedMerID;
    private String merName;
    private String currCode;
    private String merRequestAmt;
    private String surcharge;
    private String payType;
    private String pMethod;
    private String cardHolder = "";
    private String epMonth = "";
    private String epYear = "";
    private String expireYear = "";
    private String CVV2Data = "";
    private String operatorId;
    private String channel;
    private String hideSurcharge;

    private  String baseUrl=null;
    private  String result="";

    private HashMap<String, String> map = null;

    private Exception exception;

    private int timeoutMinute;
    DatabaseHelper db;

    public CardPaymentTask(TradeResult activity, String payGate, ProgressDialog progressDialog){
        this.activity = activity;
        this.payGate = payGate;
        this.progressDialog = progressDialog;
//        db = new DatabaseHelper(activity);
    }

//    public CardPaymentTask(CardPayment_ManualKeyIn activity, String payGate, ProgressDialog progressDialog){
//        this.activity1 = activity;
//        this.payGate = payGate;
//        this.progressDialog = progressDialog;
//        db = new DatabaseHelper(activity);
//    }

    @Override
    protected void onPreExecute(){

//        if(activity1 != null){
//                    progressDialog.show();
//
//        }else{
//            progressDialog = ProgressDialog.show(activity, "", "Processing...",
//                    true);
//            progressDialog.setCancelable(false);
//        }

    }

    @Override
    protected String doInBackground(String... arg0) {

        cardNo = arg0[0];
        ProcessingCode = arg0[1];
        amount = arg0[2];
        merRef = arg0[3];
        traceNo = arg0[4];
        POSEntryMode = arg0[5];
        PANSeqNo = arg0[6];
        POSCondtionCode = arg0[7];
        Track2Data = arg0[8];
        EnryptedPIN = arg0[9];
        EMVICCRelatedData = arg0[10];
        InvoiceRef = arg0[11];
        merId = arg0[12];
        merName = arg0[13];
        currCode = arg0[14];
        merRequestAmt = arg0[15];
        surcharge = arg0[16];
        payType = arg0[17];
        pMethod = arg0[18];
        cardHolder = arg0[19];
        epMonth = arg0[20];
        epYear = arg0[21];
        CVV2Data = arg0[22];
        operatorId = arg0[23];
        channel = arg0[24];
        hideSurcharge = arg0[25];

        if(!epYear.isEmpty()){
//            Log.d("CardPaymentTask", "Year is empty");
            SimpleDateFormat formatter = new SimpleDateFormat("yy");
            Date date = null;
            try {
                date = formatter.parse(epYear);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formatter = new SimpleDateFormat("yyyy");
            expireYear = formatter.format(date);
        }

        DesEncrypter encrypt;
        try {
            encrypt = new DesEncrypter(merName);
            encryptedMerID = encrypt.encrypt(merId);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        String whereargs[] = {encryptedMerID};
//        String cardpaymentTimeout = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_CARDPAYMENT_TIMEOUT, Constants.DB_MERCHANT_ID, whereargs);

//        if(cardpaymentTimeout != null){
//            timeoutMinute = Integer.valueOf(cardpaymentTimeout);
//        }else
//        {
//            timeoutMinute = 3;
//        }

        Log.d("CardPaymentTask", "Timeout = " + String.valueOf(timeoutMinute));

        if(cardHolder.equalsIgnoreCase("")){
            cardHolder = "YU CHUN HO";
        }
        NameValuePair merchantIdNVPair=new BasicNameValuePair("merchantId", merId);
        NameValuePair merchantNameNVPair=new BasicNameValuePair("merName", merName);
        NameValuePair currCodeNVPair=new BasicNameValuePair("currCode", currCode);
        NameValuePair amountNVPair=new BasicNameValuePair("amount", amount);
        NameValuePair merRequestAmtNVPair = new BasicNameValuePair("merRequestAmt", merRequestAmt);
        NameValuePair surchargeNVPair = new BasicNameValuePair("surcharge", surcharge);
        NameValuePair payTypeNVPair=new BasicNameValuePair("payType", payType);
        NameValuePair merRefNVPair=new BasicNameValuePair("orderRef", merRef);
        NameValuePair pMethodNVPair=new BasicNameValuePair("pMethod", pMethod);
        NameValuePair cardNoNVPair=new BasicNameValuePair("cardNo", cardNo);
        NameValuePair cardHolderNVPair=new BasicNameValuePair("cardHolder", cardHolder);
        NameValuePair epMonthNVPair=new BasicNameValuePair("epMonth", epMonth);
        NameValuePair epYearNVPair=new BasicNameValuePair("epYear", expireYear);
        NameValuePair CVV2DataNVPair=new BasicNameValuePair("CVV2Data", CVV2Data);
        NameValuePair operatorIdNVPair = new BasicNameValuePair("operatorId", "admin");
        NameValuePair channelTypeNVPair = new BasicNameValuePair("channelType", "MPOS");
        NameValuePair useSurchageTypeNVPair = new BasicNameValuePair("useSurcharge", hideSurcharge);

        NameValuePair processingCodeNVPair=new BasicNameValuePair("processingCode", ProcessingCode);
        NameValuePair posEntryModeNVPair=new BasicNameValuePair("POSEntryMode", POSEntryMode);
        NameValuePair panSeqNoNVPair=new BasicNameValuePair("PANSeqNo", PANSeqNo);
        NameValuePair posConditionCodeNVPair=new BasicNameValuePair("POSCondtionCode", POSCondtionCode);
        NameValuePair track2DataIdNVPair = new BasicNameValuePair("track2Data", Track2Data);
        NameValuePair encryptedPINNVPair = new BasicNameValuePair("enryptedPIN", EnryptedPIN);
        NameValuePair EMVDataNVPair = new BasicNameValuePair("EMVData", EMVICCRelatedData);
        NameValuePair invoiceRefNVPair = new BasicNameValuePair("invoiceRef", InvoiceRef);
        NameValuePair batchNoNVPair = new BasicNameValuePair("batchNo", "00001");

        List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
        nameValuePairs.add(merchantIdNVPair);
        nameValuePairs.add(merchantNameNVPair);
        nameValuePairs.add(currCodeNVPair);
        nameValuePairs.add(amountNVPair);
        nameValuePairs.add(merRequestAmtNVPair);
        nameValuePairs.add(surchargeNVPair);
        nameValuePairs.add(payTypeNVPair);
        nameValuePairs.add(merRefNVPair);
        nameValuePairs.add(pMethodNVPair);
        nameValuePairs.add(cardNoNVPair);
        nameValuePairs.add(cardHolderNVPair);
        nameValuePairs.add(epMonthNVPair);
        nameValuePairs.add(epYearNVPair);
        nameValuePairs.add(CVV2DataNVPair);
        nameValuePairs.add(operatorIdNVPair);
        nameValuePairs.add(channelTypeNVPair);
        nameValuePairs.add(useSurchageTypeNVPair);
//        nameValuePairs.add(processingCodeNVPair);
//        nameValuePairs.add(posEntryModeNVPair);
//        nameValuePairs.add(panSeqNoNVPair);
//        nameValuePairs.add(posConditionCodeNVPair);
//        nameValuePairs.add(track2DataIdNVPair);
//        nameValuePairs.add(encryptedPINNVPair);
//        nameValuePairs.add(EMVDataNVPair);
//        nameValuePairs.add(invoiceRefNVPair);
//        nameValuePairs.add(batchNoNVPair);

        try {
            baseUrl="https://test2.paydollar.com/b2cDemo/eng/directPay/payCompMPOS.jsp";
            URL url = new URL (baseUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            TrustModifier.relaxHostChecking(con);
            con.setReadTimeout(30000);
            con.setConnectTimeout(25000);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);

            System.out.println("-----" + "TransactionQuery:" + PayGate.getQuery(nameValuePairs));

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(PayGate.getQuery(nameValuePairs));
            writer.flush();
            writer.close();
            os.close();

            InputStream in = con.getInputStream();
            BufferedReader reader = null;

            try{
                reader= new BufferedReader(new InputStreamReader(in));
                String line="";
                while((line=reader.readLine())!=null){
                    result=result+line;
                }

            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            exception = e;
            Log.d("CardPayment", "Connection Error: " +  e);
        }
        Log.d("CardPayment", "TransactionResult: " + result);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("CardPaymentTask", "Result: " +  s);

        map = PayGate.split(result);

        String successcode = map.get("successcode") == null ? "" : map.get("successcode");
        String merRef = map.get("Ref") == null ? "" : map.get("Ref");
        String payRef = map.get("PayRef") == null ? "" : map.get("PayRef");
//        String amount = map.get("Amt") == null ? "" : map.get("Amt");
        String currcode = map.get("Cur") == null ? "" : map.get("Cur");
        String cardHolder = map.get("Holder") == null ? "" : map.get("Holder");
        String trxTime = map.get("TxTime") == null ? "" : map.get("TxTime");
        String errcode = map.get("errMsg") == null ? "" : map.get("errMsg");
        String prc = map.get("prc") == null ? "" : map.get("prc");
        String src = map.get("src") == null ? "" : map.get("src");

        if(!currcode.isEmpty()){
            currcode = CurrCode.getName(currcode);
        }
        if (exception != null) {
            Log.d("CardPaymentTask", "Error: " +  exception);

//            Toast.makeText(activity, "Failed to connect server", Toast.LENGTH_SHORT).show();
//            activity.reversalCardPayment();
            activity.cardFailed(merId,
                    merName,
                    successcode,
                    merRef,
                    payRef,
                    traceNo,
                    amount,
                    merRequestAmt,
                    surcharge,
                    currcode,
                    payType,
                    pMethod,
                    cardNo,
                    cardHolder,
                    epMonth,
                    epYear,
                    operatorId,
                    prc,
                    src,
                    errcode
            );

        }else{

            if (successcode == null) {
//                System.out.println("-----" + "successcod:null,Ref:" + merchant_ref_no + "," +
//                        "PayRef:" + PayRef + ",amount:" + amount + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode);
//                activity.scanfail(successcode, merchantid, mername, payType, pMethod, operatorId, cardNo,
//                        merchant_ref_no, PayRef, amount, MerRequestAmt, Surcharge, Currcode, Time, errcode);
                activity.cardFailed(merId,
                        merName,
                        successcode,
                        merRef,
                        payRef,
                        traceNo,
                        amount,
                        merRequestAmt,
                        surcharge,
                        currcode,
                        payType,
                        pMethod,
                        cardNo,
                        cardHolder,
                        epMonth,
                        epYear,
                        operatorId,
                        prc,
                        src,
                        errcode
                );

            } else {
                if (successcode.equals("0")) {
                    System.out.println("-----" + "successcod:" + successcode + "," +
                            "Ref:" + merRef + ",PayRef:" + payRef + ",amount:" + amount + "," +
                            "Currcode:" + currcode + ",TxTime:" + trxTime + ", CardHolder:" + cardHolder + "," +
                            "epMonth:" + epMonth + "," + "epYear:" + epYear + ",errcode:" + POSEntryMode);

                    activity.cardSuccess(merId,
                            merName,
                            successcode,
                            merRef,
                            payRef,
                            traceNo,
                            amount,
                            merRequestAmt,
                            surcharge,
                            currcode,
                            trxTime,
                            payType,
                            pMethod,
                            cardNo,
                            cardHolder,
                            epMonth,
                            epYear,
                            operatorId,
                            errcode);

//                    if(POSEntryMode.equals("0012")){
//                        activity1.cardSuccess(merId,
//                                merName,
//                                successcode,
//                                merRef,
//                                payRef,
//                                traceNo,
//                                amount,
//                                merRequestAmt,
//                                surcharge,
//                                currcode,
//                                trxTime,
//                                payType,
//                                pMethod,
//                                cardNo,
//                                cardHolder,
//                                epMonth,
//                                epYear,
//                                operatorId,
//                                errcode
//                        );
//                    }else{
//                        activity.cardSuccess(merId,
//                                merName,
//                                successcode,
//                                merRef,
//                                payRef,
//                                traceNo,
//                                amount,
//                                merRequestAmt,
//                                surcharge,
//                                currcode,
//                                trxTime,
//                                payType,
//                                pMethod,
//                                cardNo,
//                                cardHolder,
//                                epMonth,
//                                epYear,
//                                operatorId,
//                                errcode
//                        );
//                    }

                } else {

//                    if(prc != null && src != null)
//                    {
//                        if(prc.equals("-2") && src.equals("-2")){
//                            Toast.makeText(activity, "Start Reversal", Toast.LENGTH_SHORT).show();
//                            activity.reversalCardPayment();
//                        }
//                    }else {
                    activity.cardFailed(merId,
                            merName,
                            successcode,
                            merRef,
                            payRef,
                            traceNo,
                            amount,
                            merRequestAmt,
                            surcharge,
                            currcode,
                            payType,
                            pMethod,
                            cardNo,
                            cardHolder,
                            epMonth,
                            epYear,
                            operatorId,
                            prc,
                            src,
                            errcode
                    );

//                    if(POSEntryMode.equals("0012")){
//                        Toast.makeText(activity1, "Connect to server successfully", Toast.LENGTH_SHORT).show();
//
//                        activity1.cardFailed(merId,
//                                merName,
//                                successcode,
//                                merRef,
//                                payRef,
//                                traceNo,
//                                amount,
//                                merRequestAmt,
//                                surcharge,
//                                currcode,
//                                payType,
//                                pMethod,
//                                cardNo,
//                                cardHolder,
//                                epMonth,
//                                epYear,
//                                operatorId,
//                                prc,
//                                src,
//                                errcode
//                        );
//                    }else{
//                        Toast.makeText(activity, "Connect to server successfully", Toast.LENGTH_SHORT).show();
//
//                        activity.cardFailed(merId,
//                                merName,
//                                successcode,
//                                merRef,
//                                payRef,
//                                traceNo,
//                                amount,
//                                merRequestAmt,
//                                surcharge,
//                                currcode,
//                                payType,
//                                pMethod,
//                                cardNo,
//                                cardHolder,
//                                epMonth,
//                                epYear,
//                                operatorId,
//                                prc,
//                                src,
//                                errcode
//                        );
//                    }

//                    }
                }
            }
        }

        if ((progressDialog != null) && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

}
