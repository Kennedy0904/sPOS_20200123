package com.example.dell.smartpos;

import android.app.ProgressDialog;
import android.content.Context;
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
import java.util.ArrayList;
import java.util.List;

public class CardPaymentSettlementTask extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private Context context;
    private String payGate;
    private SettlementFragment fragment;

    private  String baseUrl=null;
    private  String result="";

    private Exception exception;

    public CardPaymentSettlementTask(Context context, String payGate, SettlementFragment fragment){
        this.context = context;
        this.payGate = payGate;
        this.fragment = fragment;
    }


    @Override
    protected void onPreExecute(){
        progressDialog = ProgressDialog.show(context, "", "Processing Settlement...",
                true);
    }

    @Override
    protected String doInBackground(String... arg0) {

        NameValuePair processingCodeNVPair=new BasicNameValuePair("processingCode",arg0[0]);
        NameValuePair orderRefNVPair=new BasicNameValuePair("orderRef",arg0[1]);
        NameValuePair batchNoNVPair=new BasicNameValuePair("batchNo",arg0[2]);
        NameValuePair batchTotalNVPair=new BasicNameValuePair("batchTotal",arg0[3]);
        NameValuePair merchantIdNVPair=new BasicNameValuePair("merchantId",arg0[4]);
        NameValuePair loginIdNVPair=new BasicNameValuePair("loginId", arg0[5]);
        NameValuePair passwordNVPair=new BasicNameValuePair("password", arg0[6]);
        NameValuePair actionNVPair=new BasicNameValuePair("actionType",arg0[7]);

        List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
        nameValuePairs.add(processingCodeNVPair);
        nameValuePairs.add(orderRefNVPair);
        nameValuePairs.add(batchNoNVPair);
        nameValuePairs.add(batchTotalNVPair);
        nameValuePairs.add(merchantIdNVPair);
        nameValuePairs.add(loginIdNVPair);
        nameValuePairs.add(passwordNVPair);
        nameValuePairs.add(actionNVPair);

        try {
            baseUrl= "https://test2.paydollar.com/b2cDemo/eng/merchant/api/settlementApi.jsp";
            URL url = new URL (baseUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
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

            System.out.println("-----" + "SettlementQuery:" + PayGate.getQuery(nameValuePairs));

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
                        exception = e;
                    }
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("CardPayment", "Connection Error: " +  e);

        }
        Log.d("CardPayment", "SettlementResult: " + result);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {

        if ((progressDialog != null) && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        Toast.makeText(context, "Settlement successfully", Toast.LENGTH_SHORT).show();
        //fragment.incBatchNo();
        //fragment.resetCardTraceNo();
    }
}
