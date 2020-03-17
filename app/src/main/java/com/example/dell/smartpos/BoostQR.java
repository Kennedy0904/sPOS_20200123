package com.example.dell.smartpos;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BoostQR extends AsyncTask<String, Void, TaskParams> {

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

    private TaskParams taskParams;

    public BoostQR(PresentQRPayment_3 activity, ProgressDialog progressDialog, String PayGate) {
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
    protected TaskParams doInBackground(String... arg0) {
        System.out.println("---KJ do in background BoostQR");
        //amount,currCode,MerchantRef,auth_code,pMethod,remark
        String businessDate = arg0[0];
        String argamount = arg0[1];
        String argcurrCode = arg0[2];
        String argmerRequestAmt = arg0[3];
        String argmerid = arg0[4];
        String argmerref = arg0[5];
        String argpaymethod = arg0[6];
        String arguserid = arg0[7];
        String argsurcharge = arg0[8];
        String argexpirytime = arg0[9];



        java.util.Date currentDate_we = new java.util.Date();
        SimpleDateFormat formatter_we = new SimpleDateFormat("ddMMyyyykkmmss");
        String epMonth = (formatter_we.format(currentDate_we)).substring(2, 4);
        String epYear = (formatter_we.format(currentDate_we)).substring(4, 8);

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
        NameValuePair cardHolderNVPair = new BasicNameValuePair("cardHolder", argpaymethod);
        NameValuePair cardNoNVPair = new BasicNameValuePair("cardNo", "4518354303130007");
        NameValuePair vbvTransactionNVPair = new BasicNameValuePair("vbvTransaction", "");
        NameValuePair secureHashNVPair = new BasicNameValuePair("secureHash", "");
        NameValuePair operatorIdNVPair = new BasicNameValuePair("operatorId", arguserid);
        NameValuePair channelTypeNVPair = new BasicNameValuePair("channelType", "MPOS");
        NameValuePair useSurchageTypeNVPair = new BasicNameValuePair("useSurcharge", argsurcharge);
        NameValuePair expiryMinuteNVPair = new BasicNameValuePair("expiryMinute", argexpirytime);
        NameValuePair requestNVPair = new BasicNameValuePair("boostRequest", "");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(businessDateNVPair);
        nameValuePairs.add(merchantIdNVPair);
        nameValuePairs.add(currCodeNVPair);
        nameValuePairs.add(amountNVPair);
        nameValuePairs.add(merRequestAmtNVPair);
        nameValuePairs.add(orderRefNVPair);
        nameValuePairs.add(langNVPair);
        nameValuePairs.add(payTypeNVPair);
        nameValuePairs.add(pMethodNVPair);
        nameValuePairs.add(epMonthNVPair);
        nameValuePairs.add(epYearNVPair);
        nameValuePairs.add(cardHolderNVPair);
        nameValuePairs.add(cardNoNVPair);
        nameValuePairs.add(vbvTransactionNVPair);
        nameValuePairs.add(secureHashNVPair);
        nameValuePairs.add(operatorIdNVPair);
        nameValuePairs.add(channelTypeNVPair);
        nameValuePairs.add(useSurchageTypeNVPair);
        nameValuePairs.add(expiryMinuteNVPair);
        nameValuePairs.add(requestNVPair);

        try {
            System.out.println("---KJ do in background BoostQR: entering payCompMPOS");
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
            writer.flush();
            writer.close();
            os.close();

            InputStream in = con.getInputStream();
            BufferedReader reader = null;

            try {
                System.out.println("---KJ do in background BoostQR: get result from payCompMPOS");
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result = result + line;
                    map = PayGate.splitOctopus(result);
                    successcode = map.get("successcode");

                    if (successcode.equalsIgnoreCase("0")) {
                        base64QR = map.get("base64QR");
                        token = map.get("token");
                        ref = map.get("Ord");
                        orderId = map.get("PayRef");
                        System.out.println("---KJ order id=" + orderId + ";PGref=" + ref);
                    }

                }
                Log.d("result123", result);
                Log.d("result123URL", base64QR);
                Log.d("result123token", token);

                if (!token.equals("")) {
                    Calendar date = Calendar.getInstance();
                    long t = date.getTimeInMillis();
                    expiry_time = new Date(t + (Integer.parseInt(argexpirytime) * 60000));
                    expiryTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S+'08:00'").format(expiry_time.getTime());
                    System.out.println("---KJ do in background BoostQR: expiry time=" + expiry_time.toString());
                    Bitmap bitmap = activity.generateBoostQR(base64QR, orderId, ref);

                    taskParams = new TaskParams();

                    taskParams.setBitmapQR(bitmap);
                    taskParams.setToken(token);
                    taskParams.setOrderId(orderId);
                    taskParams.setGatewayRef(ref);
                    taskParams.setExpiryTime(expiryTime);
                } else {
                    System.out.println("---KJ do in background BoostQR: empty token");
                    taskParams = new TaskParams();

                    taskParams.setToken("");
                    taskParams.setExpiryTime("");
                    taskParams.setOrderId("");
                }

            } catch (Exception e) {
                System.out.println("---KJ do in background OctopusQR: get result Error--" + e);
                logresult += "\r\n" + "while failed catch," + e + "  ******  ";
                e.printStackTrace();

                taskParams = new TaskParams();

                taskParams.setToken("");
                taskParams.setExpiryTime("");
                taskParams.setOrderId("");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        logresult += "\r\n" + "while finally failed," + e + "  ******  ";
                        e.printStackTrace();

                        taskParams = new TaskParams();

                        taskParams.setToken("");
                        taskParams.setExpiryTime("");
                        taskParams.setOrderId("");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("---KJ do in background OctopusQR: call payCompMPOS error--" + e);
            taskParams = new TaskParams();

            taskParams.setToken("");
            taskParams.setExpiryTime("");
            taskParams.setOrderId("");
            logresult += "\r\n" + "call api failed," + e + "  ******  ";
            result = "successcode=2&Ref=&PayRef=&Amt=&Cur=&prc=-9&src=-9&Ord=&Holder=&AuthId=&TxTime=&errMsg=Http Request Error";
            e.printStackTrace();
        }

        return taskParams;
    }

    @Override
    protected void onPostExecute(TaskParams taskParams) {
        System.out.println("KJ---");
        if(taskParams.getToken() == null){
            activity.displayBoostQR(null, null, null, null);
        } else {
            if(!taskParams.getToken().equals("")){
                String token = taskParams.getToken();
                String expiryTime = taskParams.getExpiryTime();
                String orderId = taskParams.getOrderId();
                String gatewayRef = taskParams.getGatewayRef();
                Bitmap bitmap = taskParams.getBitmapQR();
                System.out.println("---KJ order id=" + orderId + ";PGref=" + gatewayRef);
                activity.displayBoostQR(bitmap, token, expiryTime, orderId);
            } else {
                activity.showQRError();
            }
        }

        progressDialog.dismiss();
    }


}
