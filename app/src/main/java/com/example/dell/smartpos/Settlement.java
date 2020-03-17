package com.example.dell.smartpos;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.smartpos.Printer.PAX.GetObj;
import com.example.dell.smartpos.Printer.PrintUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Settlement extends AppCompatActivity {

    String title = "";
    String merchantId;
    String merchantName = "";
    String actionType;
    String currCode;

    Button btnSetllement;
    TextView settlementTrx;
    TextView settlementtotalAmount;
    TextView settlementCurrcode;
    TextView settlementBatchNo;
    TextView refundAmont;
    TextView RefundTrx;
    TextView lastRefundAmt;
    TextView lastRefundTrx;
    RelativeLayout loadingPanel;
    LinearLayout settlementLayout;

    Button btnReprint;
    TextView lastSettlementBatchNo;
    TextView lastSettlementTime;
    TextView lastSettlementTrx;
    TextView lastSettlementAmt;
    TextView lastSettleCurrCode;
    TextView lastSettleRefundCurrCode;

    public static String customerCopy = "";
    public static String merchantCopy = "";

    String batchNo = "";
    String tid = "";
    String bankId = "";
    String host = "";

    String totalTrx="";
    String totalAmt="";


    String printeraddress = "";
    String printername = "";
    Map<String, String> info = null;
    Map<String, String> product = null;
    PrintUtil printUtil = null;
    GetObj getObj = null;
    String language = "";

    //print receipt
    String info_merchantName = "";
    String info_tid = "";
    String info_bankMid = "";
    String info_date = "";
    String info_time = "";
    String info_batch = "";
    String info_host = "";
    String info_nii = "";
    String info_note = "";
    String info_isCN = "";

    String value_tid = "";
    String value_bankMid = "";
    String value_batch = "";
    String value_host = "";
    String value_nii = "";
    //

    ProgressDialog progressDialog_settlement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);

        // Set title bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Locale locale = getResources().getConfiguration().locale;
        language = locale.getLanguage();

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);

        printeraddress = prefsettings.getString(Constants.pref_printer_address, "");
        printername = prefsettings.getString(Constants.pref_printer_name, "");
        if ("".equals(printeraddress) || printeraddress == null) {
            printeraddress = "";
        }
        if ("".equals(printername) || printername == null) {
            printername = "";
        }
        Log.d("OTTO", "device SETTEXT>>" + printername + ":" + printeraddress);

        //Initialize PAX Printer
        getObj = new GetObj(getApplicationContext());
        //---------for print func---------//

        final Intent settlementIntent = getIntent();
        title = settlementIntent.getStringExtra(Constants.PAYMENTOPTION);
        merchantId = settlementIntent.getStringExtra(Constants.MERID);
        merchantName = settlementIntent.getStringExtra(Constants.MERNAME);
        actionType = settlementIntent.getStringExtra("actionType");
        currCode = settlementIntent.getStringExtra(Constants.CURRCODE);
        setTitle(R.string.settlement);

        settlementBatchNo = (TextView)findViewById(R.id.settlementBatchNo);
        settlementTrx = (TextView)findViewById(R.id.settlementTrx);
        settlementtotalAmount = (TextView)findViewById(R.id.settlementAmount);
        settlementCurrcode = (TextView)findViewById(R.id.settlementCurrcode);
        RefundTrx = (TextView)findViewById(R.id.settlementRefundTrx);
        refundAmont = (TextView)findViewById(R.id.refundAmount);
        btnSetllement = (Button) findViewById(R.id.btnSettlement);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        settlementLayout = (LinearLayout) findViewById(R.id.layoutSettlement);

        String batchNo = autoBBLQRBatchID();

        settlementBatchNo.setText(batchNo);
        settlementCurrcode.setText(CurrCode.getName(currCode));
        progressDialog_settlement = new ProgressDialog(Settlement.this);

        lastSettlementBatchNo = (TextView) findViewById(R.id.lastSettlementBatchNo);
       /* lastSettlementTime = (TextView) findViewById(R.id.lastSettlementTime);*/
        lastSettlementTrx = (TextView) findViewById(R.id.lastSettlementTrx);
        lastSettlementAmt = (TextView) findViewById(R.id.lastSettlementAmount);
        lastSettleCurrCode = (TextView) findViewById(R.id.lastSettlementCurrcode);
        lastSettleRefundCurrCode = (TextView) findViewById(R.id.RefundCurrcode);
        lastRefundAmt = (TextView) findViewById(R.id.lastRefundAmount);
        lastRefundTrx =  (TextView) findViewById(R.id.lastRefundTrx);


        if(actionType.equalsIgnoreCase("queryTrx")){
            progressDialog_settlement = new ProgressDialog(Settlement.this);
            progressDialog_settlement.setMessage(getString(R.string.please_wait));
            progressDialog_settlement.setCancelable(false);
            QueryTask queryTask = new QueryTask(Settlement.this,progressDialog_settlement, getPrefPayGate());
            queryTask.execute(
                    merchantId,
                    "querySettlement");
        }

        btnSetllement.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                progressDialog_settlement = new ProgressDialog(Settlement.this);
                progressDialog_settlement.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.settlement_reminder));
                progressDialog_settlement.setCancelable(false);
                QueryTask queryTask = new QueryTask(Settlement.this,progressDialog_settlement, getPrefPayGate());
                queryTask.execute(
                        merchantId,
                        "settlement");
            }
        });
    }

    public class QueryTask extends AsyncTask<
            String,
            Void,
            String> {

        private String baseUrl2;
        private Settlement activity;
        private String payGate;
        private HashMap<String, String> map=null;

        private String result="";
        InputStream inputStream=null;

        private ProgressDialog progressDialog;

        public QueryTask(Settlement activity, ProgressDialog progressDialog, String payGate)
        {
            this.activity = activity;
            this.progressDialog = progressDialog;
            this.payGate = payGate;

        }

        protected void onPreExecute() {
            System.out.println("----progressDialog.show1");
            progressDialog.show();
            System.out.println("----progressDialog.show2");
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String merchantId = arg0[0];
            String type = arg0[1];

            String batchNo = autoBBLQRBatchID();

            NameValuePair merchantIdNVpair = new BasicNameValuePair("merchantId",merchantId);
            NameValuePair batchIdNVpair = new BasicNameValuePair("batchNo","000002");

            List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
            nameValuePairs.add(merchantIdNVpair);
            nameValuePairs.add(batchIdNVpair);

            try{

                if(type.equalsIgnoreCase("settlement")){
                    baseUrl2 = "https://test2.paydollar.com/b2cDemo/bblSettlement";
                    actionType = type;
                }else{
                    baseUrl2 = "https://test2.paydollar.com/b2cDemo/eng/merchant/api/settlementApi.jsp";
                }

                URL url = new URL(baseUrl2);
                HttpURLConnection con = (HttpURLConnection)
                        url.openConnection();
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
                        try{
                            reader.close();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            return result;

        }
        protected void onPostExecute(String result) {

            map= PayGate.split(result);

            String resultCode = map.get("resultCode");

            if(resultCode.equals("0")){

                if(actionType.equalsIgnoreCase("settlement")){
                    incBatchNo();
                    String tx = map.get("printText");
                    batchNo = map.get("batchNo");
                    tid = map.get("tid");
                    bankId = map.get("bankMid");
                    host = map.get("paymentMethod");

                    System.out.println("K Team "+ host);


                    printText(tx);

                    successResult();
                }else{
                    totalTrx=map.get("totalTrx");
                    totalAmt=map.get("totalAmt");
                    String batchNo = map.get("lastBatchNo");
                    String lastSettleTime = map.get("lastSettlementTime");
                    String lastSettleTrx = map.get("lastTotalTrx");
                    String lastSettleAmnt = map.get("lastTotalAmt");
                    String refundAmount = map.get("refundAmnt");
                    String refundTrx = map.get("refundTrx");
                    String lastRefundAmount = map.get("lastRefundAmnt");
                    String lastRefundTrx =  map.get("lastRefundTrx");


                    setSettlementData(
                            totalTrx,
                            totalAmt,
                            batchNo,
                            lastSettleTime,
                            lastSettleTrx,
                            lastSettleAmnt,
                            refundTrx,
                            refundAmount,
                            lastRefundAmount,
                            lastRefundTrx);
                }

            }else{
                String errorMsg=map.get("errorMsg");
                failResult(errorMsg);
            }

            System.out.println("----progressDialog.dismiss1");
            progressDialog.dismiss();
            System.out.println("----progressDialog.dismiss2");
        }


    }

    public String getPrefPayGate(){
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
        String prefpaygate = prefsettings.getString("PayGate", Constants.default_paygate);
        return prefpaygate;
    }

    public void setSettlementData(
            String totalTrx,
            String totalAmount,
            String batchNo,
            String lastSettleTime,
            String lastSettleTrx,
            String lastSettleAmt,
            String refundTrx,
            String refundAmount,
            String lastRefundAmount,
            String alstRefundTrx) {

        DecimalFormat df = new DecimalFormat("0.00");

        if(!totalAmount.equals("0.00") ||!lastRefundAmount.equals("0.00") ){
            double amount = Double.parseDouble(totalAmount);
            settlementTrx.setText(totalTrx);
            settlementtotalAmount.setText(df.format(amount));
            RefundTrx.setText(refundTrx);

            String refundAmt = refundAmount.substring(1);
            double refund = Double.parseDouble(refundAmt);
            refundAmont.setText(df.format(refund));
        }else{
            settlementTrx.setText("0");
            settlementtotalAmount.setText("0.00");
            RefundTrx.setText("0");
            refundAmont.setText("0.00");
            btnSetllement.setEnabled(false);
        }

        if(!lastSettleAmt.equals("null")){
            double amount = Double.parseDouble(lastSettleAmt);
           /* lastSettlementTime.setText(lastSettleTime);*/
            lastSettlementBatchNo.setText(batchNo);
            lastSettlementTrx.setText(lastSettleTrx);
            lastSettlementAmt.setText(df.format(amount));
            lastSettleCurrCode.setText(CurrCode.getName(currCode));
            lastRefundAmt.setText(lastRefundAmount);
            lastRefundTrx.setText(alstRefundTrx);
            lastSettleRefundCurrCode.setText(CurrCode.getName(currCode));
        }else{
            lastSettlementTime.setText("-");
            lastSettlementBatchNo.setText("-");
            lastSettlementTrx.setText("0");
            lastSettlementAmt.setText("0.00");
            btnReprint.setEnabled(false);
        }

        loadingPanel.setVisibility(RelativeLayout.GONE);
        settlementLayout.setVisibility(LinearLayout.VISIBLE);
    }

    public void failResult(String errMsg) {
        Toast.makeText(Settlement.this,errMsg, Toast.LENGTH_SHORT).show();
        loadingPanel.setVisibility(RelativeLayout.GONE);
        Intent intent = new Intent();
        intent.setClass(Settlement.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void successResult() {
        Toast.makeText(Settlement.this,"Success Settlement", Toast.LENGTH_SHORT).show();
        loadingPanel.setVisibility(RelativeLayout.GONE);
        printBBLSettlement();

        settlementTrx.setText("0");
        settlementtotalAmount.setText("0.00");
        RefundTrx.setText("0");
        refundAmont.setText("0.00");
        btnSetllement.setEnabled(false);

        Date currentTime = Calendar.getInstance().getTime();
        DateFormat sdf = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");

        DecimalFormat df = new DecimalFormat("0.00");
        double amount = Double.parseDouble(totalAmt);

      /*  lastSettlementTime.setText(sdf.format(currentTime.getTime()));*/
        lastSettlementBatchNo.setText(decBatchID());
        lastSettlementTrx.setText(totalTrx);
        lastSettlementAmt.setText(df.format(amount));

    }

    public void loading() {

        loadingPanel.setVisibility(View.VISIBLE);
    }

    public static String fromHexString(String hex) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }

    public static void printText(String tx) {

        List<String> txList = Arrays.asList(tx.split(","));
        customerCopy = "";
        merchantCopy = "";

        String print = "";
        String waitNextValue = "";
        int previousValue = 0;

        for(int i = 0; i < txList.size(); i++) {

            if(!waitNextValue.isEmpty()) {

                switch(previousValue) {
                    case 1:
                        printCondition("-", txList.get(i));

                        previousValue = 0;
                        waitNextValue = "";
                        break;
                    case 2:
                        printCondition(" ", txList.get(i));

                        previousValue = 0;
                        waitNextValue = "";
                        break;
                }
            }else {

                int switchVal = 0;

                if(txList.get(i).equalsIgnoreCase("03")) {
                    switchVal = 1;
                }else if(txList.get(i).equalsIgnoreCase("FB")) {
                    switchVal = 2;
                }else if(txList.get(i).equalsIgnoreCase("0A")) {
                    switchVal = 3;
                }else if(txList.get(i).equalsIgnoreCase("FC")) {
                    switchVal = 4;
                }

                switch(switchVal) {
                    case 1:
                        customerCopy += fromHexString(print);
                        merchantCopy += fromHexString(print);

                        print = "";
                        break;
                    case 2:
                        waitNextValue = "T";
                        previousValue = 1;

                        if(print.length() > 2) {
                            customerCopy += fromHexString(print);
                            merchantCopy += fromHexString(print);
                        }

                        print = "";
                        break;
                    case 3:
                        if(print.length() > 2) {
                            customerCopy += fromHexString(print);
                            merchantCopy += fromHexString(print);
                        }

                        customerCopy += "\n";
                        merchantCopy += "\n";

                        print = "";
                        break;

                    case 4:
                        waitNextValue = "T";
                        previousValue = 2;

                        if(print.length() > 2) {
                            customerCopy += fromHexString(print);
                            merchantCopy += fromHexString(print);
                        }

                        print = "";
                        break;

                    default:
                        print += txList.get(i);
                        break;
                }

            }
        }

    }

    public static void printCondition(
            String print,
            String length) {

        int len = Integer.parseInt(length)-2;

        for(int i = 0; i < len; i ++ ) {
            customerCopy += print;
            merchantCopy += print;
        }

    }

    public void printBBLSettlement() {



        String date = new SimpleDateFormat("MMM d, yyyy").format(new Date());
        String time = new SimpleDateFormat("hh:mm:ss").format(new Date());



        info_merchantName = merchantName;
        info_tid = getString(R.string.print_tid);
        info_bankMid = getString(R.string.print_merid);
        info_date = date;
        info_time = time;
        info_batch = getString(R.string.print_batch);
        info_host = getString(R.string.print_host);
        info_nii = getString(R.string.print_nii);
        info_note = getString(R.string.print_note);
        info_isCN = (!"en".equals(language)) ? "T" : "F";

        int midlength = 15 - bankId.length();

        if(midlength < 15){

            String add0 = "";
            for(int i = 0; i < midlength; i++){
                add0 += "0";
            }

            bankId = add0 + bankId;
        }

        value_tid = tid;
        value_bankMid = bankId;
        value_batch = batchNo;
        value_host = "ALIPAY";
        value_nii = "034";

        info = new TreeMap<String, String>();
        info.put("MerchantName", info_merchantName);
        info.put("info_tid", info_tid);
        info.put("info_bankMid", info_bankMid);
        info.put("info_date", info_date);
        info.put("info_time", info_time);
        info.put("info_batch", info_batch);
        info.put("info_host", info_host);
        info.put("info_nii", info_nii);
        info.put("note", info_note);
        info.put("isCN", info_isCN);


        product = new TreeMap<String, String>();
        product.put("value_tid", value_tid);
        product.put("value_bankMid", value_bankMid);
        product.put("value_batch", value_batch);
        product.put("value_host", value_host);
        product.put("value_nii", value_nii);
        product.put("payMethod", merchantCopy);

        // Merchant Logo
        Bitmap initbitmap = GlobalFunction.getMerLogo(Settlement.this);

        printUtil = new PrintUtil(Settlement.this, printeraddress, printername, info, product, initbitmap);
        printUtil.sendSettlement();

    }

    private String autoBBLQRBatchID() {

        SharedPreferences pref = this.getSharedPreferences(Constants.BATCH_COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("batchCounter", 0);

        int runningNum = curCounter + 1;
        int length = String.valueOf(curCounter).length();
        String digit = "000000";
        String batchID = digit.substring(0, digit.length() - length) + runningNum;

        return batchID;
    }

    private String decBatchID() {

        SharedPreferences pref = this.getSharedPreferences(Constants.BATCH_COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("batchCounter", 0);

        int runningNum = curCounter - 1;
        int length = String.valueOf(curCounter).length();
        String digit = "000000";
        String batchID = digit.substring(0, digit.length() - length) + runningNum;

        return batchID;
    }

    public void incBatchNo(){
        SharedPreferences pref = getSharedPreferences(Constants.BATCH_COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("batchCounter", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("batchCounter", curCounter +=1);
        System.out.println("Batch ID = " + String.valueOf(curCounter +=1));
        edit.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
