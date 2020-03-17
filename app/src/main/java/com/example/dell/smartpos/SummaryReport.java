package com.example.dell.smartpos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.smartpos.Printer.K9.PrintUtilK9;
import com.example.dell.smartpos.Printer.PAX.GetObj;
import com.example.dell.smartpos.Printer.PrintUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class SummaryReport extends AppCompatActivity {

    //===KM LIEW summry report printing feature 30/5/2018===
    boolean CheckLogout = false;//logout flag
    String lang;
    Date lastUpdateTime;//lastest operation time
    String currency = "";
    String merID = "";
    String merName = "";
    String currcode = "";
    String userID;
    String date1;
    String date2;

    private String user;
    private String password;
    double totalAmount = 0;
    double totalrefund = 0;
    double totalvoid = 0;
    double totalRequestRefund = 0;

    int pageNo = Constants.CURRENT_PAGE_NO;
    int perPage = 10;

    // ALIPAYHKOFFL
    int totalTrxALIPAYHKOFFL = 0;
    double ALIPAYHKOFFLAmnt = 0;
    int totalRefundALIPAYHKOFFLTrx = 0;
    double totalRefundALIPAYHKOFFLAmnt = 0;
    int totalRequestRefundALIPAYHKOFFLTrx = 0;
    double totalRequestRefundALIPAYHKOFFLAmnt = 0;
    int totalVoidALIPAYHKOFFLTrx = 0;
    double totalVoidALIPAYHKOFFLAmnt = 0;

    // ALIPAYCNOFFL
    int totalTrxALIPAYCNOFFL = 0;
    double ALIPAYCNOFFLAmnt = 0;
    int totalRefundALIPAYCNOFFLTrx = 0;
    double totalRefundALIPAYCNOFFLAmnt = 0;
    int totalRequestRefundALIPAYCNOFFLTrx = 0;
    double totalRequestRefundALIPAYCNOFFLAmnt = 0;
    int totalVoidALIPAYCNOFFLTrx = 0;
    double totalVoidALIPAYCNOFFLAmnt = 0;

    // ALIPAYOFFL
    int totalTrxALIPAYOFFL = 0;
    double ALIPAYOFFLAmnt = 0;
    int totalRefundALIPAYOFFLTrx = 0;
    double totalRefundALIPAYOFFLAmnt = 0;
    int totalRequestRefundALIPAYOFFLTrx = 0;
    double totalRequestRefundALIPAYOFFLAmnt = 0;
    int totalVoidALIPAYOFFLTrx = 0;
    double totalVoidALIPAYOFFLAmnt = 0;

    // BOOSTOFFL
    int totalTrxBOOSTOFFL = 0;
    double BOOSTOFFLAmnt = 0;
    int totalRefundBOOSTOFFLTrx = 0;
    double totalRefundBOOSTOFFLAmnt = 0;
    int totalRequestRefundBOOSTOFFLTrx = 0;
    double totalRequestRefundBOOSTOFFLAmnt = 0;
    int totalVoidBOOSTOFFLTrx = 0;
    double totalVoidBOOSTOFFLAmnt = 0;

    // GCASHOFFL
    int totalTrxGCASHOFFL = 0;
    double GCASHOFFLAmnt = 0;
    int totalRefundGCASHOFFLTrx = 0;
    double totalRefundGCASHOFFLAmnt = 0;
    int totalRequestRefundGCASHOFFLTrx = 0;
    double totalRequestRefundGCASHOFFLAmnt = 0;
    int totalVoidGCASHOFFLTrx = 0;
    double totalVoidGCASHOFFLAmnt = 0;

    // GRABPAYOFFL
    int totalTrxGRABPAYOFFL = 0;
    double GRABPAYOFFLAmnt = 0;
    int totalRefundGRABPAYOFFLTrx = 0;
    double totalRefundGRABPAYOFFLAmnt = 0;
    int totalRequestRefundGRABPAYOFFLTrx = 0;
    double totalRequestRefundGRABPAYOFFLAmnt = 0;
    int totalVoidGRABPAYOFFLTrx = 0;
    double totalVoidGRABPAYOFFLAmnt = 0;

    // MASTER
    int totalTrxMaster = 0;
    double masterAmnt = 0;
    int totalRefundMasterTrx = 0;
    double totalRefundMasterAmnt = 0;
    int totalRequestRefundMasterTrx = 0;
    double totalRequestRefundMasterAmnt = 0;
    int totalVoidMasterTrx = 0;
    double totalVoidMasterAmnt = 0;

    // OEPAYOFFL
    int totalTrxOEPAYOFFL = 0;
    double OEPAYOFFLAmnt = 0;
    int totalRefundOEPAYOFFLTrx = 0;
    double totalRefundOEPAYOFFLAmnt = 0;
    int totalRequestRefundOEPAYOFFLTrx = 0;
    double totalRequestRefundOEPAYOFFLAmnt = 0;
    int totalVoidOEPAYOFFLTrx = 0;
    double totalVoidOEPAYOFFLAmnt = 0;

    // PROMPTPAYOFFL
    int totalTrxPROMPTPAYOFFL = 0;
    double PROMPTPAYOFFLAmnt = 0;
    int totalRefundPROMPTPAYOFFLTrx = 0;
    double totalRefundPROMPTPAYOFFLAmnt = 0;
    int totalRequestRefundPROMPTPAYOFFLTrx = 0;
    double totalRequestRefundPROMPTPAYOFFLAmnt = 0;
    int totalVoidPROMPTPAYOFFLTrx = 0;
    double totalVoidPROMPTPAYOFFLAmnt = 0;

    // UNIONPAYOFFL
    int totalTrxUNIONPAYOFFL = 0;
    double UNIONPAYOFFLAmnt = 0;
    int totalRefundUNIONPAYOFFLTrx = 0;
    double totalRefundUNIONPAYOFFLAmnt = 0;
    int totalRequestRefundUNIONPAYOFFLTrx = 0;
    double totalRequestRefundUNIONPAYOFFLAmnt = 0;
    int totalVoidUNIONPAYOFFLTrx = 0;
    double totalVoidUNIONPAYOFFLAmnt = 0;

    // VISA
    int totalTrxVisa = 0;
    double visaAmnt = 0;
    int totalRefundVisaTrx = 0;
    double totalRefundVisaAmnt = 0;
    int totalRequestRefundVisaTrx = 0;
    double totalRequestRefundVisaAmnt = 0;
    int totalVoidVisaTrx = 0;
    double totalVoidVisaAmnt = 0;

    // WECHATOFFL
    int totalTrxWECHATOFFL = 0;
    double WECHATOFFLAmnt = 0;
    int totalRefundWECHATOFFLTrx = 0;
    double totalRefundWECHATOFFLAmnt = 0;
    int totalRequestRefundWECHATOFFLTrx = 0;
    double totalRequestRefundWECHATOFFLAmnt = 0;
    int totalVoidWECHATOFFLTrx = 0;
    double totalVoidWECHATOFFLAmnt = 0;

    // WECHATHKOFFL
    int totalTrxWECHATHKOFFL = 0;
    double WECHATHKOFFLAmnt = 0;
    int totalRefundWECHATHKOFFLTrx = 0;
    double totalRefundWECHATHKOFFLAmnt = 0;
    int totalRequestRefundWECHATHKOFFLTrx = 0;
    double totalRequestRefundWECHATHKOFFLAmnt = 0;
    int totalVoidWECHATHKOFFLTrx = 0;
    double totalVoidWECHATHKOFFLAmnt = 0;

    // WECHATONL
    int totalTrxWECHATONL = 0;
    double WECHATONLAmnt = 0;
    int totalRefundWECHATONLTrx = 0;
    double totalRefundWECHATONLAmnt = 0;
    int totalRequestRefundWECHATONLTrx = 0;
    double totalRequestRefundWECHATONLAmnt = 0;
    int totalVoidWECHATONLTrx = 0;
    double totalVoidWECHATONLAmnt = 0;

    //for print function
    String printeraddress = "";
    String printername = "";
    PrintUtil printUtil = null;
    Map<String, String> report_value = null;
    Map<String, String> label = null;
    GetObj getObj = null;

    //label
    String label_print_at = "";
    String label_from = "";
    String label_to = "";
    String label_merName = "";
    String label_title = "";
    String label_header = "";
    String label_footer = "";
    String label_grand_total_trx = "";
    String label_grand_total_refund = "";
    String label_grand_total_request_refund = "";
    String label_grand_total_void = "";

    String label_ALIPAYHKOFFL = "";
    String label_ALIPAYCNOFFL = "";
    String label_ALIPAYOFFL = "";
    String label_BOOSTOFFL = "";
    String label_GCASHOFFL = "";
    String label_GRABPAYOFFL = "";
    String label_master = "";
    String label_OEPAYOFFL = "";
    String label_PROMPTPAYOFFL = "";
    String label_UNIONPAYOFFL = "";
    String label_visa = "";
    String label_WECHATOFFL = "";
    String label_WECHATHKOFFL = "";
    String label_WECHATONL = "";

    String label_total_trx = "";
    String label_total_amnt = "";
    String label_total_refund_trx = "";
    String label_total_refund_amnt = "";
    String label_total_request_refund_trx = "";
    String label_total_request_refund_amnt = "";
    String label_total_void_trx = "";
    String label_total_void_amnt = "";

    private Button print_button;
    private ProgressDialog progressDialog;
    RelativeLayout loadingPanel;
    LinearLayout grandtotal;
    LinearLayout paylist;
    TextView tvnone;
    RelativeLayout listfooter;

    ArrayList<Record> record_data = new ArrayList<Record>();

    String deviceName;
    PrintUtilK9 printUtilK9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_report);
        setTitle(R.string.summary_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---------for autologout---------//
        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout, false);
        lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
        Log.d("KM", "init SummaryReport CheckLogout:" + CheckLogout + ",lastUpdateTime:" + lastUpdateTime);
        //---------for autologout---------//

        // Check language
        GlobalFunction.changeLanguage(SummaryReport.this);

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

        //---------for print func---------//
        deviceName = android.os.Build.MODEL;
        System.out.println("deviceName: " + deviceName);
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

        //Initialize K9 printer
        if(deviceName.equalsIgnoreCase("K9")){
            printUtilK9 = new PrintUtilK9(SummaryReport.this);
            printUtilK9.bindService();
        }
        //---------for print func---------//

        Intent intent = getIntent();
        merID = intent.getStringExtra(Constants.MERID);
        merName = intent.getStringExtra(Constants.MERNAME);
        currcode = intent.getStringExtra(Constants.CURRCODE);
        date1 = intent.getStringExtra("fromDate");
        date2 = intent.getStringExtra("toDate");

        getApiAccount();

        TextView tv1 = (TextView) findViewById(R.id.txtReportdate1);
        TextView tv2 = (TextView) findViewById(R.id.txtReportdate2);
        tv1.setText(date1);
        tv2.setText(date2);

        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        grandtotal = (LinearLayout) findViewById(R.id.grandtotal);
        paylist = (LinearLayout) findViewById(R.id.pay_list);
        tvnone = (TextView) findViewById(R.id.empty_view);
        listfooter = (RelativeLayout) findViewById(R.id.listfooter);

        loading();
        if (!isOnline()) {
            Handler handlerTimer = new Handler();
            handlerTimer.postDelayed(new Runnable() {
                public void run() {
                    // do something
                    tapToRetry();
                }
            }, 2000);
        } else {
            getData();
        }

        tvnone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                loading();
                progressDialog = new ProgressDialog(SummaryReport.this);
                String strpageno = Integer.toString(pageNo);
                Constants.CURRENT_PAGE_NO = pageNo;
                LoadXML loadxml = new LoadXML(SummaryReport.this, progressDialog, getPrefPayGate());
                loadxml.execute(date1, date2, strpageno);
            }
        });

        print_button = (Button) findViewById(R.id.print_report);
        print_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label_title = getString(R.string.summary_report);

                label_merName = getString(R.string.merName_label);
                label_from = getString(R.string.from);
                label_to = getString(R.string.to);
                label_print_at = getString(R.string.print_at_label);

                label_header = getString(R.string.grand_Total);
                label_footer = getString(R.string.report_end);
                label_grand_total_trx = getString(R.string.Transaction);
                label_grand_total_refund = getString(R.string.Refund);
                label_grand_total_request_refund = getString(R.string.RequestRefund);
                label_grand_total_void = getString(R.string.Void);

                label_ALIPAYHKOFFL = getString(R.string.ALIPAYHKOFFL_label);
                label_ALIPAYCNOFFL = getString(R.string.ALIPAYCNOFFL_label);
                label_ALIPAYOFFL = getString(R.string.ALIPAYOFFL_label);
                label_BOOSTOFFL = getString(R.string.BOOSTOFFL_label);
                label_GCASHOFFL = getString(R.string.GCASHOFFL_label);
                label_GRABPAYOFFL = getString(R.string.GRABPAYOFFL_label);
                label_master = getString(R.string.master_label);
                label_OEPAYOFFL = getString(R.string.OEPAYOFFL_label);
                label_PROMPTPAYOFFL = getString(R.string.PROMPTPAYOFFL_label);
                label_UNIONPAYOFFL = getString(R.string.UNIONPAYOFFL_label);
                label_visa = getString(R.string.visa_label);
                label_WECHATOFFL = getString(R.string.WECHATOFFL_label);
                label_WECHATHKOFFL = getString(R.string.WECHATHKOFFL_label);
                label_WECHATONL = getString(R.string.WECHATONL_label);

                label_total_trx = getString(R.string.print_total_transaction_label);
                label_total_amnt = getString(R.string.print_total_amount_label);
                label_total_refund_trx = getString(R.string.print_total_refund_trx_label);
                label_total_refund_amnt = getString(R.string.print_total_refund_amnt_label);
                label_total_request_refund_trx = getString(R.string.print_total_request_refund_trx_label);
                label_total_request_refund_amnt = getString(R.string.print_total_request_refund_amnt_label);
                label_total_void_trx = getString(R.string.print_total_void_trx_label);
                label_total_void_amnt = getString(R.string.print_total_void_amnt_label);

                label = new TreeMap<String, String>();
                label.put("Title", label_title);

                //====Merchant Information===//
                label.put("MerNameLabel", label_merName);
                label.put("FromDateLabel", label_from);
                label.put("ToDateLabel", label_to);
                //============================//

                label.put("Header", label_header);
                label.put("Footer", label_footer);
                label.put("GrandTotalTrxLabel", label_grand_total_trx);
                label.put("GrandTotalRefundLabel", label_grand_total_refund);
                label.put("GrandTotalRequestRefundLabel", label_grand_total_request_refund);
                label.put("GrandTotalVoidLabel", label_grand_total_void);

                label.put("ALIPAYHKOFFL", label_ALIPAYHKOFFL);
                label.put("ALIPAYCNOFFL", label_ALIPAYCNOFFL);
                label.put("ALIPAYOFFL", label_ALIPAYOFFL);
                label.put("BOOSTOFFL", label_BOOSTOFFL);
                label.put("GCASHOFFL", label_GCASHOFFL);
                label.put("GRABPAYOFFL", label_GRABPAYOFFL);
                label.put("MASTER", label_master);
                label.put("OEPAYOFFL", label_OEPAYOFFL);
                label.put("PROMPTPAYOFFL", label_PROMPTPAYOFFL);
                label.put("UNIONPAYOFFL", label_UNIONPAYOFFL);
                label.put("VISA", label_visa);
                label.put("WECHATOFFL", label_WECHATOFFL);
                label.put("WECHATHKOFFL", label_WECHATHKOFFL);
                label.put("WECHATONL", label_WECHATONL);

                label.put("TotalTrxLabel", label_total_trx);
                label.put("TotalAmntLabel", label_total_amnt);
                label.put("TotalRefundTrxLabel", label_total_refund_trx);
                label.put("TotalRefundAmntLabel", label_total_refund_amnt);
                label.put("TotalRequestRefundTrxLabel", label_total_request_refund_trx);
                label.put("TotalRequestRefundAmntLabel", label_total_request_refund_amnt);

                label.put("TotalTrxLabel", label_total_trx);
                label.put("TotalAmntLabel", label_total_amnt);
                label.put("TotalVoidTrxLabel", label_total_void_trx);
                label.put("TotalVoidAmntLabel", label_total_void_amnt);

                String noTxn = getString(R.string.noTxnFound);
                label.put("NoTxnFound", noTxn);

                report_value = new TreeMap<String, String>();
                report_value.put("MerName", merName);
                report_value.put("From_date", date1);
                report_value.put("To_date", date2);
                report_value.put("currCode", currency);

                report_value.put("GrandTotalAmnt", String.format("%,.2f", totalAmount));
                report_value.put("GrandTotalRefundAmnt", String.format("%,.2f", totalrefund));
                report_value.put("GrandTotalRequestRefundAmnt", String.format("%,.2f", totalRequestRefund));
                report_value.put("GrandTotalVoidAmnt", String.format("%,.2f", totalvoid));

                report_value.put("TotalTrxALIPAYHKOFFL", Integer.toString(totalTrxALIPAYHKOFFL));
                report_value.put("TotalAmntALIPAYHKOFFL", String.format("%,.2f", ALIPAYHKOFFLAmnt));
                report_value.put("TotalRefundTrxALIPAYHKOFFL", Integer.toString(totalRefundALIPAYHKOFFLTrx));
                report_value.put("TotalRefundAmntALIPAYHKOFFL", String.format("%,.2f", totalRefundALIPAYHKOFFLAmnt));
                report_value.put("TotalRequestRefundTrxALIPAYHKOFFL", Integer.toString(totalRequestRefundALIPAYHKOFFLTrx));
                report_value.put("TotalRequestRefundAmntALIPAYHKOFFL", String.format("%,.2f", totalRequestRefundALIPAYHKOFFLAmnt));
                report_value.put("TotalVoidTrxALIPAYHKOFFL", Integer.toString(totalVoidALIPAYHKOFFLTrx));
                report_value.put("TotalVoidAmntALIPAYHKOFFL", String.format("%,.2f", totalVoidALIPAYHKOFFLAmnt));

                report_value.put("TotalTrxALIPAYCNOFFL", Integer.toString(totalTrxALIPAYCNOFFL));
                report_value.put("TotalAmntALIPAYCNOFFL", String.format("%,.2f", ALIPAYCNOFFLAmnt));
                report_value.put("TotalRefundTrxALIPAYCNOFFL", Integer.toString(totalRefundALIPAYCNOFFLTrx));
                report_value.put("TotalRefundAmntALIPAYCNOFFL", String.format("%,.2f", totalRefundALIPAYCNOFFLAmnt));
                report_value.put("TotalRequestRefundTrxALIPAYCNOFFL", Integer.toString(totalRequestRefundALIPAYCNOFFLTrx));
                report_value.put("TotalRequestRefundAmntALIPAYCNOFFL", String.format("%,.2f", totalRequestRefundALIPAYCNOFFLAmnt));
                report_value.put("TotalVoidTrxALIPAYCNOFFL", Integer.toString(totalVoidALIPAYCNOFFLTrx));
                report_value.put("TotalVoidAmntALIPAYCNOFFL", String.format("%,.2f", totalVoidALIPAYCNOFFLAmnt));

                report_value.put("TotalTrxALIPAYOFFL", Integer.toString(totalTrxALIPAYOFFL));
                report_value.put("TotalAmntALIPAYOFFL", String.format("%,.2f", ALIPAYOFFLAmnt));
                report_value.put("TotalRefundTrxALIPAYOFFL", Integer.toString(totalRefundALIPAYOFFLTrx));
                report_value.put("TotalRefundAmntALIPAYOFFL", String.format("%,.2f", totalRefundALIPAYOFFLAmnt));
                report_value.put("TotalRequestRefundTrxALIPAYOFFL", Integer.toString(totalRequestRefundALIPAYOFFLTrx));
                report_value.put("TotalRequestRefundAmntALIPAYOFFL", String.format("%,.2f", totalRequestRefundALIPAYOFFLAmnt));
                report_value.put("TotalVoidTrxALIPAYOFFL", Integer.toString(totalVoidALIPAYOFFLTrx));
                report_value.put("TotalVoidAmntALIPAYOFFL", String.format("%,.2f", totalVoidALIPAYOFFLAmnt));

                report_value.put("TotalTrxBOOSTOFFL", Integer.toString(totalTrxBOOSTOFFL));
                report_value.put("TotalAmntBOOSTOFFL", String.format("%,.2f", BOOSTOFFLAmnt));
                report_value.put("TotalRefundTrxBOOSTOFFL", Integer.toString(totalRefundBOOSTOFFLTrx));
                report_value.put("TotalRefundAmntBOOSTOFFL", String.format("%,.2f", totalRefundBOOSTOFFLAmnt));
                report_value.put("TotalRequestRefundTrxBOOSTOFFL", Integer.toString(totalRequestRefundBOOSTOFFLTrx));
                report_value.put("TotalRequestRefundAmntBOOSTOFFL", String.format("%,.2f", totalRequestRefundBOOSTOFFLAmnt));
                report_value.put("TotalVoidTrxBOOSTOFFL", Integer.toString(totalVoidBOOSTOFFLTrx));
                report_value.put("TotalVoidAmntBOOSTOFFL", String.format("%,.2f", totalVoidBOOSTOFFLAmnt));

                report_value.put("TotalTrxGCASHOFFL", Integer.toString(totalTrxGCASHOFFL));
                report_value.put("TotalAmntGCASHOFFL", String.format("%,.2f", GCASHOFFLAmnt));
                report_value.put("TotalRefundTrxGCASHOFFL", Integer.toString(totalRefundGCASHOFFLTrx));
                report_value.put("TotalRefundAmntGCASHOFFL", String.format("%,.2f", totalRefundGCASHOFFLAmnt));
                report_value.put("TotalRequestRefundTrxGCASHOFFL", Integer.toString(totalRequestRefundGCASHOFFLTrx));
                report_value.put("TotalRequestRefundAmntGCASHOFFL", String.format("%,.2f", totalRequestRefundGCASHOFFLAmnt));
                report_value.put("TotalVoidTrxGCASHOFFL", Integer.toString(totalVoidGCASHOFFLTrx));
                report_value.put("TotalVoidAmntGCASHOFFL", String.format("%,.2f", totalVoidGCASHOFFLAmnt));

                report_value.put("TotalTrxGRABPAYOFFL", Integer.toString(totalTrxGRABPAYOFFL));
                report_value.put("TotalAmntGRABPAYOFFL", String.format("%,.2f", GRABPAYOFFLAmnt));
                report_value.put("TotalRefundTrxGRABPAYOFFL", Integer.toString(totalRefundGRABPAYOFFLTrx));
                report_value.put("TotalRefundAmntGRABPAYOFFL", String.format("%,.2f", totalRefundGRABPAYOFFLAmnt));
                report_value.put("TotalRequestRefundTrxGRABPAYOFFL", Integer.toString(totalRequestRefundGRABPAYOFFLTrx));
                report_value.put("TotalRequestRefundAmntGRABPAYOFFL", String.format("%,.2f", totalRequestRefundGRABPAYOFFLAmnt));
                report_value.put("TotalVoidTrxGRABPAYOFFL", Integer.toString(totalVoidGRABPAYOFFLTrx));
                report_value.put("TotalVoidAmntGRABPAYOFFL", String.format("%,.2f", totalVoidGRABPAYOFFLAmnt));

                report_value.put("TotalTrxMASTER", Integer.toString((totalTrxMaster)));
                report_value.put("TotalAmntMASTER", String.format("%,.2f", masterAmnt));
                report_value.put("TotalRefundTrxMASTER", Integer.toString(totalRefundMasterTrx));
                report_value.put("TotalRefundAmntMASTER", String.format("%,.2f", totalRefundMasterAmnt));
                report_value.put("TotalRequestRefundTrxMASTER", Integer.toString(totalRequestRefundMasterTrx));
                report_value.put("TotalRequestRefundAmntMASTER", String.format("%,.2f", totalRequestRefundMasterAmnt));
                report_value.put("TotalVoidTrxMASTER", Integer.toString(totalVoidMasterTrx));
                report_value.put("TotalVoidAmntMASTER", String.format("%,.2f", totalVoidMasterAmnt));

                report_value.put("TotalTrxOEPAYOFFL", Integer.toString(totalTrxOEPAYOFFL));
                report_value.put("TotalAmntOEPAYOFFL", String.format("%,.2f", OEPAYOFFLAmnt));
                report_value.put("TotalRefundTrxOEPAYOFFL", Integer.toString(totalRefundOEPAYOFFLTrx));
                report_value.put("TotalRefundAmntOEPAYOFFL", String.format("%,.2f", totalRefundOEPAYOFFLAmnt));
                report_value.put("TotalRequestRefundTrxOEPAYOFFL", Integer.toString(totalRequestRefundOEPAYOFFLTrx));
                report_value.put("TotalRequestRefundAmntOEPAYOFFL", String.format("%,.2f", totalRequestRefundOEPAYOFFLAmnt));
                report_value.put("TotalVoidTrxOEPAYOFFL", Integer.toString(totalVoidOEPAYOFFLTrx));
                report_value.put("TotalVoidAmntOEPAYOFFL", String.format("%,.2f", totalVoidOEPAYOFFLAmnt));

                report_value.put("TotalTrxPROMPTPAYOFFL", Integer.toString(totalTrxPROMPTPAYOFFL));
                report_value.put("TotalAmntPROMPTPAYOFFL", String.format("%,.2f", PROMPTPAYOFFLAmnt));
                report_value.put("TotalRefundTrxPROMPTPAYOFFL", Integer.toString(totalRefundPROMPTPAYOFFLTrx));
                report_value.put("TotalRefundAmntPROMPTPAYOFFL", String.format("%,.2f", totalRefundPROMPTPAYOFFLAmnt));
                report_value.put("TotalRequestRefundTrxPROMPTPAYOFFL", Integer.toString(totalRequestRefundPROMPTPAYOFFLTrx));
                report_value.put("TotalRequestRefundAmntPROMPTPAYOFFL", String.format("%,.2f", totalRequestRefundPROMPTPAYOFFLAmnt));
                report_value.put("TotalVoidTrxPROMPTPAYOFFL", Integer.toString(totalVoidPROMPTPAYOFFLTrx));
                report_value.put("TotalVoidAmntPROMPTPAYOFFL", String.format("%,.2f", totalVoidPROMPTPAYOFFLAmnt));

                report_value.put("TotalTrxUNIONPAYOFFL", Integer.toString(totalTrxUNIONPAYOFFL));
                report_value.put("TotalAmntUNIONPAYOFFL", String.format("%,.2f", UNIONPAYOFFLAmnt));
                report_value.put("TotalRefundTrxUNIONPAYOFFL", Integer.toString(totalRefundUNIONPAYOFFLTrx));
                report_value.put("TotalRefundAmntUNIONPAYOFFL", String.format("%,.2f", totalRefundUNIONPAYOFFLAmnt));
                report_value.put("TotalRequestRefundTrxUNIONPAYOFFL", Integer.toString(totalRequestRefundUNIONPAYOFFLTrx));
                report_value.put("TotalRequestRefundAmntUNIONPAYOFFL", String.format("%,.2f", totalRequestRefundUNIONPAYOFFLAmnt));
                report_value.put("TotalVoidTrxUNIONPAYOFFL", Integer.toString(totalVoidUNIONPAYOFFLTrx));
                report_value.put("TotalVoidAmntUNIONPAYOFFL", String.format("%,.2f", totalVoidUNIONPAYOFFLAmnt));

                report_value.put("TotalTrxVISA", Integer.toString((totalTrxVisa)));
                report_value.put("TotalAmntVISA", String.format("%,.2f", visaAmnt));
                report_value.put("TotalRefundTrxVISA", Integer.toString(totalRefundVisaTrx));
                report_value.put("TotalRefundAmntVISA", String.format("%,.2f", totalRefundVisaAmnt));
                report_value.put("TotalRequestRefundTrxVISA", Integer.toString(totalRequestRefundVisaTrx));
                report_value.put("TotalRequestRefundAmntVISA", String.format("%,.2f", totalRequestRefundVisaAmnt));
                report_value.put("TotalVoidTrxVISA", Integer.toString(totalVoidVisaTrx));
                report_value.put("TotalVoidAmntVISA", String.format("%,.2f", totalVoidVisaAmnt));

                report_value.put("TotalTrxWECHATOFFL", Integer.toString(totalTrxWECHATOFFL));
                report_value.put("TotalAmntWECHATOFFL", String.format("%,.2f", WECHATOFFLAmnt));
                report_value.put("TotalRefundTrxWECHATOFFL", Integer.toString(totalRefundWECHATOFFLTrx));
                report_value.put("TotalRefundAmntWECHATOFFL", String.format("%,.2f", totalRefundWECHATOFFLAmnt));
                report_value.put("TotalRequestRefundTrxWECHATOFFL", Integer.toString(totalRequestRefundWECHATOFFLTrx));
                report_value.put("TotalRequestRefundAmntWECHATOFFL", String.format("%,.2f", totalRequestRefundWECHATOFFLAmnt));
                report_value.put("TotalVoidTrxWECHATOFFL", Integer.toString(totalVoidWECHATOFFLTrx));
                report_value.put("TotalVoidAmntWECHATOFFL", String.format("%,.2f", totalVoidWECHATOFFLAmnt));

                report_value.put("TotalTrxWECHATHKOFFL", Integer.toString(totalTrxWECHATHKOFFL));
                report_value.put("TotalAmntWECHATHKOFFL", String.format("%,.2f", WECHATHKOFFLAmnt));
                report_value.put("TotalRefundTrxWECHATHKOFFL", Integer.toString(totalRefundWECHATHKOFFLTrx));
                report_value.put("TotalRefundAmntWECHATHKOFFL", String.format("%,.2f", totalRefundWECHATHKOFFLAmnt));
                report_value.put("TotalRequestRefundTrxWECHATHKOFFL", Integer.toString(totalRequestRefundWECHATHKOFFLTrx));
                report_value.put("TotalRequestRefundAmntWECHATHKOFFL", String.format("%,.2f", totalRequestRefundWECHATHKOFFLAmnt));
                report_value.put("TotalVoidTrxWECHATHKOFFL", Integer.toString(totalVoidWECHATHKOFFLTrx));
                report_value.put("TotalVoidAmntWECHATHKOFFL", String.format("%,.2f", totalVoidWECHATHKOFFLAmnt));

                report_value.put("TotalTrxWECHATONL", Integer.toString(totalTrxWECHATONL));
                report_value.put("TotalAmntWECHATONL", String.format("%,.2f", WECHATONLAmnt));
                report_value.put("TotalRefundTrxWECHATONL", Integer.toString(totalRefundWECHATONLTrx));
                report_value.put("TotalRefundAmntWECHATONL", String.format("%,.2f", totalRefundWECHATONLAmnt));
                report_value.put("TotalRequestRefundTrxWECHATONL", Integer.toString(totalRequestRefundWECHATONLTrx));
                report_value.put("TotalRequestRefundAmntWECHATONL", String.format("%,.2f", totalRequestRefundWECHATONLAmnt));
                report_value.put("TotalVoidTrxWECHATONL", Integer.toString(totalVoidWECHATONLTrx));
                report_value.put("TotalVoidAmntWECHATONL", String.format("%,.2f", totalVoidWECHATONLAmnt));

                Bitmap bitmap = null;

                printUtil = new PrintUtil(SummaryReport.this, printeraddress, printername, label, report_value, bitmap);
                printUtil.sendSummaryReport();
            }
        });
    }

    public void getApiAccount() {
        //GET API FROM DB
        Intent intent = getIntent();
        DatabaseHelper db = new DatabaseHelper(SummaryReport.this);
        String orgMerchantId = merID;
        String encMerchantId = "";
        DesEncrypter encrypt;
        try {
            encrypt = new DesEncrypter(merName);
            encMerchantId = encrypt.encrypt(orgMerchantId);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        String whereargs[] = {encMerchantId};
        String dbuser = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_API_ID, Constants.DB_MERCHANT_ID, whereargs);
        String dbpassword = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_API_PW, Constants.DB_MERCHANT_ID, whereargs);
        String dbuserid = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
        db.close();
        if (dbuser != null) {
            try {
                DesEncrypter encrypter = new DesEncrypter(merName);
                user = encrypter.decrypt(dbuser);
                password = encrypter.decrypt(dbpassword);
                userID = encrypter.decrypt(dbuserid);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(SummaryReport.this, R.string.apiID_pw_notset, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @SuppressLint("DefaultLocale")
    private class LoadXML extends AsyncTask<String, Void, ArrayList<Record>> {
        private ProgressDialog progressDialog;
        private SummaryReport activity;
        private String payGate;

        private String baseUrl = null;
        private String result = "";
        private ArrayList<Record> record_data = new ArrayList<Record>();

        public LoadXML(SummaryReport activity, ProgressDialog progressDialog, String payGate) {
            this.activity = activity;
            this.progressDialog = progressDialog;
            this.payGate = payGate;
        }


        @Override
        protected ArrayList<Record> doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String d1 = arg0[0].replace("/", "") + "000000";
            String d2 = arg0[1].replace("/", "") + "235959";

            //getAPIAct

            NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", merID);
            NameValuePair loginIdNVPair = new BasicNameValuePair("loginId", user);
            NameValuePair passwordNVPair = new BasicNameValuePair("password", password);
            NameValuePair startDateNVPair = new BasicNameValuePair("startDate", d1);
            NameValuePair pageNoNVPair = new BasicNameValuePair("pageNo", arg0[2]);
//            NameValuePair pageRecordsNVPair = new BasicNameValuePair("pageRecords",String.valueOf(perPage));
            NameValuePair endDateNVPair = new BasicNameValuePair("endDate", d2);
            NameValuePair mobileNVPair = new BasicNameValuePair("enableMobile", "T");
            NameValuePair sortOrderNVpair = new BasicNameValuePair("sortOrder", "desc");
            NameValuePair operatorIdNVPair = new BasicNameValuePair("operatorId", "");
//				NameValuePair channelTypeNVPair = new BasicNameValuePair("channelType","MPOS");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(merchantIdNVPair);
            nameValuePairs.add(loginIdNVPair);
            nameValuePairs.add(passwordNVPair);
            nameValuePairs.add(startDateNVPair);
            nameValuePairs.add(endDateNVPair);
            nameValuePairs.add(mobileNVPair);
            nameValuePairs.add(pageNoNVPair);
//            nameValuePairs.add(pageRecordsNVPair);
            nameValuePairs.add(sortOrderNVpair);
            nameValuePairs.add(operatorIdNVPair);
//		        nameValuePairs.add(channelTypeNVPair);

            try {
                baseUrl = PayGate.getURL_genTxtXMLMPOS(payGate);
                URL url = new URL(baseUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
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

                try {
                    reader = new BufferedReader(new InputStreamReader(in));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        result = result + line;
                    }
                    try {
                        System.out.println("KJ---" + result);
                        String text = result;
                        InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
                        XML2Record xml2Record = new XML2Record();
                        android.util.Xml.parse(is, Xml.Encoding.UTF_8, xml2Record);
                        List<Record> records = xml2Record.getRecords();
                        record_data = new ArrayList<Record>();
                        int i = 0;
                        Record recordData[] = new Record[records.size()];
                        for (Record record : records) {
                            if (Double.parseDouble(record.getamt()) > 0.0) {
                                recordData[i] = new Record(record.PayRef(), record.merref(),
                                        record.getOrderdate(), record.currency(), record.getamt(),
                                        record.getSurcharge(), record.getMerRequestAmt(),
                                        record.remark(), record.orderstatus(), merName,
                                        record.getPayType(), record.getPaymethod(),
                                        record.getPayBankId(), record.getCardNo(),
                                        record.getCardHolder(), record.getSettle(),
                                        record.getBankId(),
                                        record.getSettleTime(), record.getBatchNo(),
                                        record.getTraceNo(), record.getInvoiceNo());
                                record_data.add(recordData[i]);

                                i++;
                            }
                        } //end for loop

                    } catch (Exception e) {
                        e.printStackTrace();
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return record_data;
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onPostExecute(ArrayList<Record> record_data) {
            if (record_data == null) {
                activity.networkError();
            }
            if (record_data.size() != 0) {

                for (Record record : record_data) {
                    currency = record.currency();
                    merName = record.getMerName();
                    String paymentMethod = record.getPaymethod();
                    String status = record.status();
                    double amount = Double.parseDouble(record.getamt());

                    if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Accepted_Adj")) {
                        totalAmount += amount;
                    }

                    if (status.equalsIgnoreCase("Refunded") || status.equalsIgnoreCase("PartialRefunded")) {
                        totalrefund += amount;
                    } else if (status.equalsIgnoreCase("Voided")) {
                        totalvoid += amount;
                    } else if (status.equalsIgnoreCase("RequestRefund")) {
                        totalRequestRefund += amount;
                    }

                    if (paymentMethod.equalsIgnoreCase("ALIPAYHKOFFL")) {
                        // ALIPAYHKOFFL
                        if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Accepted_Adj")) {
                            totalTrxALIPAYHKOFFL++;
                            ALIPAYHKOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Refunded") || status.equalsIgnoreCase("PartialRefunded")) {
                            totalRefundALIPAYHKOFFLTrx++;
                            totalRefundALIPAYHKOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("RequestRefund")) {
                            totalRequestRefundALIPAYHKOFFLTrx++;
                            totalRequestRefundALIPAYHKOFFLAmnt += amount;
                        }
                    } else if (paymentMethod.equalsIgnoreCase("ALIPAYCNOFFL")) {
                        // ALIPAYCNOFFL
                        if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Accepted_Adj")) {
                            totalTrxALIPAYCNOFFL++;
                            ALIPAYCNOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Refunded") || status.equalsIgnoreCase("PartialRefunded")) {
                            totalRefundALIPAYCNOFFLTrx++;
                            totalRefundALIPAYCNOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("RequestRefund")) {
                            totalRequestRefundALIPAYCNOFFLTrx++;
                            totalRequestRefundALIPAYCNOFFLAmnt += amount;
                        }
                    } else if (paymentMethod.equalsIgnoreCase("ALIPAYOFFL")) {
                        // ALIPAYOFFL
                        if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Accepted_Adj")) {
                            totalTrxALIPAYOFFL++;
                            ALIPAYOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Refunded") || status.equalsIgnoreCase("PartialRefunded")) {
                            totalRefundALIPAYOFFLTrx++;
                            totalRefundALIPAYOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("RequestRefund")) {
                            totalRequestRefundALIPAYOFFLTrx++;
                            totalRequestRefundALIPAYOFFLAmnt += amount;
                        }
                    } else if (paymentMethod.equalsIgnoreCase("BOOSTOFFL")) {
                        // BOOSTOFFL
                        if (status.equalsIgnoreCase("Accepted")) {
                            totalTrxBOOSTOFFL++;
                            BOOSTOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Refunded")) {
                            totalRefundBOOSTOFFLTrx++;
                            totalRefundBOOSTOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Voided")) {
                            totalVoidBOOSTOFFLTrx++;
                            totalVoidBOOSTOFFLAmnt += amount;
                        }
                    } else if (paymentMethod.equalsIgnoreCase("GCASHOFFL")) {
                        // GCASHOFFL
                        if (status.equalsIgnoreCase("Accepted")) {
                            totalTrxGCASHOFFL++;
                            GCASHOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Refunded")) {
                            totalRefundGCASHOFFLTrx++;
                            totalRefundGCASHOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Voided")) {
                            totalVoidGCASHOFFLTrx++;
                            totalVoidGCASHOFFLAmnt += amount;
                        }
                    } else if (paymentMethod.equalsIgnoreCase("GRABPAYOFFL")) {
                        // GRABPAYOFFL
                        if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase(
                                "Accepted_Adj")) {
                            totalTrxGRABPAYOFFL++;
                            GRABPAYOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Refunded") || status.equalsIgnoreCase("PartialRefunded")) {
                            totalRefundGRABPAYOFFLTrx++;
                            totalRefundGRABPAYOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Voided")) {
                            totalVoidGRABPAYOFFLTrx++;
                            totalVoidGRABPAYOFFLAmnt += amount;
                        }
                    } else if (paymentMethod.equalsIgnoreCase("MASTER")) {
                        // MASTER
                        if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Accepted_Adj")) {
                            totalTrxMaster++;
                            masterAmnt += amount;
                        } else if (status.equalsIgnoreCase("Voided")) {
                            totalVoidMasterTrx++;
                            totalVoidMasterAmnt += amount;
                        }
                    } else if (paymentMethod.equalsIgnoreCase("OEPAYOFFL")) {
                        // OEPAYOFFL
                        if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Accepted_Adj")) {
                            totalTrxOEPAYOFFL++;
                            OEPAYOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Refunded") || status.equalsIgnoreCase("PartialRefunded")) {
                            totalRefundOEPAYOFFLTrx++;
                            totalRefundOEPAYOFFLAmnt += amount;
                        }
                    } else if (paymentMethod.equalsIgnoreCase("PROMPTPAYOFFL")) {
                        // PROMPTPAYOFFL
                        if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Accepted_Adj")) {
                            totalTrxPROMPTPAYOFFL++;
                            PROMPTPAYOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Voided")) {
                            totalVoidPROMPTPAYOFFLTrx++;
                            totalVoidPROMPTPAYOFFLAmnt += amount;
                        }
                    }  if (paymentMethod.equalsIgnoreCase("UNIONPAYOFFL")) {
                        // UNIONPAYOFFL
                        if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Accepted_Adj")) {
                            totalTrxUNIONPAYOFFL++;
                            UNIONPAYOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Voided")) {
                            totalVoidUNIONPAYOFFLTrx++;
                            totalVoidUNIONPAYOFFLAmnt += amount;
                        }
                    } else if (paymentMethod.equalsIgnoreCase("VISA")) {
                        // VISA
                        if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Accepted_Adj")) {
                            totalTrxVisa++;
                            visaAmnt += amount;
                        } else if (status.equalsIgnoreCase("Voided")) {
                            totalVoidVisaTrx++;
                            totalVoidVisaAmnt += amount;
                        }
                    } else if (paymentMethod.equalsIgnoreCase("WECHATOFFL")) {
                        // WECHATOFFL
                        if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Accepted_Adj")) {
                            totalTrxWECHATOFFL++;
                            WECHATOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Refunded") || status.equalsIgnoreCase("PartialRefunded")) {
                            totalRefundWECHATOFFLTrx++;
                            totalRefundWECHATOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("RequestRefund")) {
                            totalRequestRefundWECHATOFFLTrx++;
                            totalRequestRefundWECHATOFFLAmnt += amount;
                        }
                    } else if (paymentMethod.equalsIgnoreCase("WECHATHKOFFL")) {
                        // WECHATHKOFFL
                        if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Accepted_Adj")) {
                            totalTrxWECHATHKOFFL++;
                            WECHATHKOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Refunded") || status.equalsIgnoreCase("PartialRefunded")) {
                            totalRefundWECHATHKOFFLTrx++;
                            totalRefundWECHATHKOFFLAmnt += amount;
                        } else if (status.equalsIgnoreCase("RequestRefund")) {
                            totalRequestRefundWECHATHKOFFLTrx++;
                            totalRequestRefundWECHATHKOFFLAmnt += amount;
                        }
                    } else if (paymentMethod.equalsIgnoreCase("WECHATONL")) {
                        // WECHATONL
                        if (status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Accepted_Adj")) {
                            totalTrxWECHATONL++;
                            WECHATONLAmnt += amount;
                        } else if (status.equalsIgnoreCase("Refunded") || status.equalsIgnoreCase("PartialRefunded")) {
                            totalRefundWECHATONLTrx++;
                            totalRefundWECHATONLAmnt += amount;
                        } else if (status.equalsIgnoreCase("RequestRefund")) {
                            totalRequestRefundWECHATONLTrx++;
                            totalRequestRefundWECHATONLAmnt += amount;
                        }
                    }
                }

                resetRes();
                activity.loadData(merName, record_data);
                listfooter.setVisibility(RelativeLayout.VISIBLE);

            } else {
                if (result.toLowerCase().contains("invalid login") || result.toLowerCase().contains("invalid password")) {
                    if (result.equalsIgnoreCase("invalid login name")) {
                        result = getString(R.string.invalid_login_name);
                    } else if (result.equalsIgnoreCase("invalid password")) {
                        result = getString(R.string.invalid_password);
                    }
                    activity.noResFound(result);
                } else {
                    activity.noResFound(getString(R.string.noTxnFound));
                }
            }

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public String getPrefPayGate() {
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate, Constants.default_paygate);
        return prefpaygate;
    }

    private void getData() {
        progressDialog = new ProgressDialog(SummaryReport.this);
        progressDialog.setCancelable(false);
        String strpageno = Integer.toString(pageNo);
        Constants.CURRENT_PAGE_NO = pageNo;
        SummaryReport.LoadXML loadxml = new SummaryReport.LoadXML(SummaryReport.this, progressDialog, getPrefPayGate());
        loadxml.execute(date1, date2, strpageno);
    }

    private void loadData(String merName, ArrayList<Record> record_data2) {

        TextView trxAmount = (TextView) findViewById(R.id.total_trx_amount);
        TextView totalvoidAmount = (TextView) findViewById(R.id.total_void_amount);
        TextView totalRefundAmount = (TextView) findViewById(R.id.total_refund_amount);
        TextView totalRequestRefundAmount = (TextView) findViewById(R.id.total_request_refund_amount);

        trxAmount.setText(currency + " " + String.format("%,.2f", totalAmount));
        totalRefundAmount.setText(currency + " " + String.format("%,.2f", totalrefund));
        totalRequestRefundAmount.setText(currency + " " + String.format("%,.2f", totalRequestRefund));
        totalvoidAmount.setText(currency + " " + String.format("%,.2f", totalvoid));

        LinearLayout alipayhkoffl = (LinearLayout) findViewById(R.id.alipayhkoffl_layout);
        LinearLayout alipaycnoffl = (LinearLayout) findViewById(R.id.alipaycnoffl_layout);
        LinearLayout alipayoffl = (LinearLayout) findViewById(R.id.alipayoffl_layout);
        LinearLayout boostoffl = (LinearLayout) findViewById(R.id.boostoffl_layout);
        LinearLayout gcashoffl = (LinearLayout) findViewById(R.id.gcashoffl_layout);
        LinearLayout grabpayoffl = (LinearLayout) findViewById(R.id.grabpayoffl_layout);
        LinearLayout master = (LinearLayout) findViewById(R.id.master_layout);
        LinearLayout oepayoffl = (LinearLayout) findViewById(R.id.oepayoffl_layout);
        LinearLayout promptpayoffl = (LinearLayout) findViewById(R.id.promptpayoffl_layout);
        LinearLayout unionpayoffl = (LinearLayout) findViewById(R.id.unionpayoffl_layout);
        LinearLayout visa = (LinearLayout) findViewById(R.id.visa_layout);
        LinearLayout wechatoffl = (LinearLayout) findViewById(R.id.wechatoffl_layout);
        LinearLayout wechathkoffl = (LinearLayout) findViewById(R.id.wechathkoffl_layout);
        LinearLayout wechatonl = (LinearLayout) findViewById(R.id.wechatonl_layout);

        // ALIPAYHKOFFL
        if (totalTrxALIPAYHKOFFL == 0 && totalRefundALIPAYHKOFFLTrx == 0 && totalRequestRefundALIPAYHKOFFLTrx == 0 && totalVoidALIPAYHKOFFLTrx == 0) {
            alipayhkoffl.setVisibility(View.GONE);
        } else {
            alipayhkoffl.setVisibility(View.VISIBLE);
            TextView totalALIPAYHKOFFLTrx = (TextView) findViewById(R.id.total_alipayhkoffl_trx);
            TextView totalALIPAYHKOFFLAmnt = (TextView) findViewById(R.id.total_alipayhkoffl_amt);
            TextView totalALIPAYHKOFFLRefundTrx = (TextView) findViewById(R.id.total_alipayhkoffl_refund_trx);
            TextView totalALIPAYHKOFFLRefundAmnt = (TextView) findViewById(R.id.alipayhkoffl_total_refund_amnt);
            TextView totalALIPAYHKOFFLRequestRefundTrx = (TextView) findViewById(R.id.total_alipayhkoffl_request_refund_trx);
            TextView totalALIPAYHKOFFLRequestRefundAmnt = (TextView) findViewById(R.id.alipayhkoffl_total_request_refund_amnt);
            TextView totalALIPAYHKOFFLVoidTrx = (TextView) findViewById(R.id.total_alipayhkoffl_void_trx);
            TextView totalALIPAYHKOFFLVoidAmnt = (TextView) findViewById(R.id.alipayhkoffl_total_void_amnt);

            totalALIPAYHKOFFLTrx.setText(Integer.toString(totalTrxALIPAYHKOFFL));
            totalALIPAYHKOFFLAmnt.setText(currency + " " + String.format("%,.2f", ALIPAYHKOFFLAmnt));
            totalALIPAYHKOFFLRefundTrx.setText(Integer.toString(totalRefundALIPAYHKOFFLTrx));
            totalALIPAYHKOFFLRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundALIPAYHKOFFLAmnt));
            totalALIPAYHKOFFLRequestRefundTrx.setText(Integer.toString(totalRequestRefundALIPAYHKOFFLTrx));
            totalALIPAYHKOFFLRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundALIPAYHKOFFLAmnt));
            totalALIPAYHKOFFLVoidTrx.setText(Integer.toString(totalVoidALIPAYHKOFFLTrx));
            totalALIPAYHKOFFLVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidALIPAYHKOFFLAmnt));
        }

        // ALIPAYCNOFFL
        if (totalTrxALIPAYCNOFFL == 0 && totalRefundALIPAYCNOFFLTrx == 0 && totalRequestRefundALIPAYCNOFFLTrx == 0 && totalVoidALIPAYCNOFFLTrx == 0) {
            alipaycnoffl.setVisibility(View.GONE);
        } else {
            alipaycnoffl.setVisibility(View.VISIBLE);
            TextView totalALIPAYCNOFFLTrx = (TextView) findViewById(R.id.total_alipaycnoffl_trx);
            TextView totalALIPAYCNOFFLAmnt = (TextView) findViewById(R.id.total_alipaycnoffl_amt);
            TextView totalALIPAYCNOFFLRefundTrx = (TextView) findViewById(R.id.total_alipaycnoffl_refund_trx);
            TextView totalALIPAYCNOFFLRefundAmnt = (TextView) findViewById(R.id.alipaycnoffl_total_refund_amnt);
            TextView totalALIPAYCNOFFLRequestRefundTrx = (TextView) findViewById(R.id.total_alipaycnoffl_request_refund_trx);
            TextView totalALIPAYCNOFFLRequestRefundAmnt = (TextView) findViewById(R.id.alipaycnoffl_total_request_refund_amnt);
            TextView totalALIPAYCNOFFLVoidTrx = (TextView) findViewById(R.id.total_alipaycnoffl_void_trx);
            TextView totalALIPAYCNOFFLVoidAmnt = (TextView) findViewById(R.id.alipaycnoffl_total_void_amnt);

            totalALIPAYCNOFFLTrx.setText(Integer.toString(totalTrxALIPAYCNOFFL));
            totalALIPAYCNOFFLAmnt.setText(currency + " " + String.format("%,.2f", ALIPAYCNOFFLAmnt));
            totalALIPAYCNOFFLRefundTrx.setText(Integer.toString(totalRefundALIPAYCNOFFLTrx));
            totalALIPAYCNOFFLRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundALIPAYCNOFFLAmnt));
            totalALIPAYCNOFFLRequestRefundTrx.setText(Integer.toString(totalRequestRefundALIPAYCNOFFLTrx));
            totalALIPAYCNOFFLRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundALIPAYCNOFFLAmnt));
            totalALIPAYCNOFFLVoidTrx.setText(Integer.toString(totalVoidALIPAYCNOFFLTrx));
            totalALIPAYCNOFFLVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidALIPAYCNOFFLAmnt));
        }

        // ALIPAYOFFL
        if (totalTrxALIPAYOFFL == 0 && totalRefundALIPAYOFFLTrx == 0 && totalRequestRefundALIPAYOFFLTrx == 0 && totalVoidALIPAYOFFLTrx == 0) {
            alipayoffl.setVisibility(View.GONE);
        } else {
            alipayoffl.setVisibility(View.VISIBLE);
            TextView totalALIPAYOFFLTrx = (TextView) findViewById(R.id.total_alipayoffl_trx);
            TextView totalALIPAYOFFLAmnt = (TextView) findViewById(R.id.total_alipayoffl_amt);
            TextView totalALIPAYOFFLRefundTrx = (TextView) findViewById(R.id.total_alipayoffl_refund_trx);
            TextView totalALIPAYOFFLRefundAmnt = (TextView) findViewById(R.id.alipayoffl_total_refund_amnt);
            TextView totalALIPAYOFFLRequestRefundTrx = (TextView) findViewById(R.id.total_alipayoffl_request_refund_trx);
            TextView totalALIPAYOFFLRequestRefundAmnt = (TextView) findViewById(R.id.alipayoffl_total_request_refund_amnt);
            TextView totalALIPAYOFFLVoidTrx = (TextView) findViewById(R.id.total_alipayoffl_void_trx);
            TextView totalALIPAYOFFLVoidAmnt = (TextView) findViewById(R.id.alipayoffl_total_void_amnt);

            totalALIPAYOFFLTrx.setText(Integer.toString(totalTrxALIPAYOFFL));
            totalALIPAYOFFLAmnt.setText(currency + " " + String.format("%,.2f", ALIPAYOFFLAmnt));
            totalALIPAYOFFLRefundTrx.setText(Integer.toString(totalRefundALIPAYOFFLTrx));
            totalALIPAYOFFLRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundALIPAYOFFLAmnt));
            totalALIPAYOFFLRequestRefundTrx.setText(Integer.toString(totalRequestRefundALIPAYOFFLTrx));
            totalALIPAYOFFLRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundALIPAYOFFLAmnt));
            totalALIPAYOFFLVoidTrx.setText(Integer.toString(totalVoidALIPAYOFFLTrx));
            totalALIPAYOFFLVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidALIPAYOFFLAmnt));
        }

        // BOOSTOFFL
        if (totalTrxBOOSTOFFL == 0 && totalRefundBOOSTOFFLTrx == 0 && totalRequestRefundBOOSTOFFLTrx == 0 && totalVoidBOOSTOFFLTrx == 0) {
            boostoffl.setVisibility(View.GONE);
        } else {
            boostoffl.setVisibility(View.VISIBLE);
            TextView totalBOOSTOFFLTrx = (TextView) findViewById(R.id.total_boostoffl_trx);
            TextView totalBOOSTOFFLAmnt = (TextView) findViewById(R.id.total_boostoffl_amt);
            TextView totalBOOSTOFFLRefundTrx = (TextView) findViewById(R.id.total_boostoffl_refund_trx);
            TextView totalBOOSTOFFLRefundAmnt = (TextView) findViewById(R.id.boostoffl_total_refund_amnt);
            TextView totalBOOSTOFFLRequestRefundTrx = (TextView) findViewById(R.id.total_boostoffl_request_refund_trx);
            TextView totalBOOSTOFFLRequestRefundAmnt = (TextView) findViewById(R.id.boostoffl_total_request_refund_amnt);
            TextView totalBOOSTOFFLVoidTrx = (TextView) findViewById(R.id.total_boostoffl_void_trx);
            TextView totalBOOSTOFFLVoidAmnt = (TextView) findViewById(R.id.boostoffl_total_void_amnt);

            totalBOOSTOFFLTrx.setText(Integer.toString(totalTrxBOOSTOFFL));
            totalBOOSTOFFLAmnt.setText(currency + " " + String.format("%,.2f", BOOSTOFFLAmnt));
            totalBOOSTOFFLRefundTrx.setText(Integer.toString(totalRefundBOOSTOFFLTrx));
            totalBOOSTOFFLRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundBOOSTOFFLAmnt));
            totalBOOSTOFFLRequestRefundTrx.setText(Integer.toString(totalRequestRefundBOOSTOFFLTrx));
            totalBOOSTOFFLRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundBOOSTOFFLAmnt));
            totalBOOSTOFFLVoidTrx.setText(Integer.toString(totalVoidBOOSTOFFLTrx));
            totalBOOSTOFFLVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidBOOSTOFFLAmnt));
        }

        // GCASHOFFL
        if (totalTrxGCASHOFFL == 0 && totalRefundGCASHOFFLTrx == 0 && totalRequestRefundGCASHOFFLTrx == 0 && totalVoidGCASHOFFLTrx == 0) {
            gcashoffl.setVisibility(View.GONE);
        } else {
            gcashoffl.setVisibility(View.VISIBLE);
            TextView totalGCASHOFFLTrx = (TextView) findViewById(R.id.total_gcashoffl_trx);
            TextView totalGCASHOFFLAmnt = (TextView) findViewById(R.id.total_gcashoffl_amt);
            TextView totalGCASHOFFLRefundTrx = (TextView) findViewById(R.id.total_gcashoffl_refund_trx);
            TextView totalGCASHOFFLRefundAmnt = (TextView) findViewById(R.id.total_gcashoffl_refund_amnt);
            TextView totalGCASHOFFLRequestRefundTrx = (TextView) findViewById(R.id.total_gcashoffl_request_refund_trx);
            TextView totalGCASHOFFLRequestRefundAmnt = (TextView) findViewById(R.id.total_gcashoffl_request_refund_amnt);
            TextView totalGCASHOFFLVoidTrx = (TextView) findViewById(R.id.total_gcashoffl_void_trx);
            TextView totalGCASHOFFLVoidAmnt = (TextView) findViewById(R.id.total_gcashoffl_void_amnt);

            totalGCASHOFFLTrx.setText(Integer.toString(totalTrxGCASHOFFL));
            totalGCASHOFFLAmnt.setText(currency + " " + String.format("%,.2f", GCASHOFFLAmnt));
            totalGCASHOFFLRefundTrx.setText(Integer.toString(totalRefundGCASHOFFLTrx));
            totalGCASHOFFLRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundGCASHOFFLAmnt));
            totalGCASHOFFLRequestRefundTrx.setText(Integer.toString(totalRequestRefundGCASHOFFLTrx));
            totalGCASHOFFLRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundBOOSTOFFLAmnt));
            totalGCASHOFFLVoidTrx.setText(Integer.toString(totalVoidGCASHOFFLTrx));
            totalGCASHOFFLVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidGCASHOFFLAmnt));
        }

        // GRABPAYOFFL
        if (totalTrxGRABPAYOFFL == 0 && totalRefundGRABPAYOFFLTrx == 0 && totalRequestRefundGRABPAYOFFLTrx == 0 && totalVoidGRABPAYOFFLTrx == 0) {
            grabpayoffl.setVisibility(View.GONE);
        } else {
            grabpayoffl.setVisibility(View.VISIBLE);
            TextView totalGRABPAYOFFLTrx = (TextView) findViewById(R.id.total_grabpayoffl_trx);
            TextView totalGRABPAYOFFLAmnt = (TextView) findViewById(R.id.total_grabpayoffl_amt);
            TextView totalGRABPAYOFFLRefundTrx = (TextView) findViewById(R.id.total_grabpayoffl_refund_trx);
            TextView totalGRABPAYOFFLRefundAmnt = (TextView) findViewById(R.id.total_grabpayoffl_refund_amnt);
            TextView totalGRABPAYOFFLRequestRefundTrx = (TextView) findViewById(R.id.total_grabpayoffl_request_refund_trx);
            TextView totalGRABPAYOFFLRequestRefundAmnt = (TextView) findViewById(R.id.total_grabpayoffl_request_refund_amnt);
            TextView totalGRABPAYOFFLVoidTrx = (TextView) findViewById(R.id.total_grabpayoffl_void_trx);
            TextView totalGRABPAYOFFLVoidAmnt = (TextView) findViewById(R.id.total_grabpayoffl_void_amnt);

            totalGRABPAYOFFLTrx.setText(Integer.toString(totalTrxGRABPAYOFFL));
            totalGRABPAYOFFLAmnt.setText(currency + " " + String.format("%,.2f", GRABPAYOFFLAmnt));
            totalGRABPAYOFFLRefundTrx.setText(Integer.toString(totalRefundGRABPAYOFFLTrx));
            totalGRABPAYOFFLRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundGRABPAYOFFLAmnt));
            totalGRABPAYOFFLRequestRefundTrx.setText(Integer.toString(totalRequestRefundGRABPAYOFFLTrx));
            totalGRABPAYOFFLRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundGRABPAYOFFLAmnt));
            totalGRABPAYOFFLVoidTrx.setText(Integer.toString(totalVoidGRABPAYOFFLTrx));
            totalGRABPAYOFFLVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidGRABPAYOFFLAmnt));
        }

        // MASTER
        if (totalTrxMaster == 0 && totalRefundMasterTrx == 0 && totalRequestRefundMasterTrx == 0 && totalVoidMasterTrx == 0) {
            master.setVisibility(master.GONE);
        } else {

            master.setVisibility(master.VISIBLE);
            TextView totalMasterTrx = (TextView) findViewById(R.id.total_master_trx);
            TextView totalMasterAmnt = (TextView) findViewById(R.id.total_master_amt);
            TextView totalMasterRefundTrx = (TextView) findViewById(R.id.total_master_refund_trx);
            TextView totalMasterRefundAmnt = (TextView) findViewById(R.id.total_master_refund_amnt);
            TextView totalMasterRequestRefundTrx = (TextView) findViewById(R.id.total_master_request_refund_trx);
            TextView totalMasterRequestRefundAmnt = (TextView) findViewById(R.id.total_master_request_refund_amnt);
            TextView totalMasterVoidTrx = (TextView) findViewById(R.id.total_master_void_trx);
            TextView totalMasterVoidAmnt = (TextView) findViewById(R.id.total_master_void_amnt);

            totalMasterTrx.setText(Integer.toString(totalTrxMaster));
            totalMasterAmnt.setText(currency + " " + String.format("%,.2f", masterAmnt));
            totalMasterRefundTrx.setText(Integer.toString(totalRefundVisaTrx));
            totalMasterRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundMasterAmnt));
            totalMasterRequestRefundTrx.setText(Integer.toString(totalRequestRefundMasterTrx));
            totalMasterRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundMasterAmnt));
            totalMasterVoidTrx.setText(Integer.toString(totalVoidMasterTrx));
            totalMasterVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidMasterAmnt));

        }

        // OEPAYOFFL
        if (totalTrxOEPAYOFFL == 0 && totalRefundOEPAYOFFLTrx == 0 && totalRequestRefundOEPAYOFFLTrx == 0 && totalVoidOEPAYOFFLTrx == 0) {
            oepayoffl.setVisibility(View.GONE);
        } else {
            oepayoffl.setVisibility(View.VISIBLE);
            TextView totalOEPAYOFFLTrx = (TextView) findViewById(R.id.total_oepayoffl_trx);
            TextView totalOEPAYOFFLAmnt = (TextView) findViewById(R.id.total_oepayoffl_amt);
            TextView totalOEPAYOFFLRefundTrx = (TextView) findViewById(R.id.total_oepayoffl_refund_trx);
            TextView totalOEPAYOFFLRefundAmnt = (TextView) findViewById(R.id.oepayoffl_total_refund_amnt);
            TextView totalOEPAYOFFLRequestRefundTrx = (TextView) findViewById(R.id.total_oepayoffl_request_refund_trx);
            TextView totalOEPAYOFFLRequestRefundAmnt = (TextView) findViewById(R.id.oepayoffl_total_request_refund_amnt);
            TextView totalOEPAYOFFLVoidTrx = (TextView) findViewById(R.id.total_oepayoffl_void_trx);
            TextView totalOEPAYOFFLVoidAmnt = (TextView) findViewById(R.id.oepayoffl_total_void_amnt);

            totalOEPAYOFFLTrx.setText(Integer.toString(totalTrxOEPAYOFFL));
            totalOEPAYOFFLAmnt.setText(currency + " " + String.format("%,.2f", OEPAYOFFLAmnt));
            totalOEPAYOFFLRefundTrx.setText(Integer.toString(totalRefundOEPAYOFFLTrx));
            totalOEPAYOFFLRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundOEPAYOFFLAmnt));
            totalOEPAYOFFLRequestRefundTrx.setText(Integer.toString(totalRequestRefundOEPAYOFFLTrx));
            totalOEPAYOFFLRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundOEPAYOFFLAmnt));
            totalOEPAYOFFLVoidTrx.setText(Integer.toString(totalVoidOEPAYOFFLTrx));
            totalOEPAYOFFLVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidOEPAYOFFLAmnt));
        }

        // PROMPTPAYOFFL
        if (totalTrxPROMPTPAYOFFL == 0 && totalRefundPROMPTPAYOFFLTrx == 0 && totalRequestRefundPROMPTPAYOFFLTrx == 0 && totalVoidPROMPTPAYOFFLTrx == 0) {
            promptpayoffl.setVisibility(View.GONE);
        } else {
            promptpayoffl.setVisibility(View.VISIBLE);
            TextView totalPROMPTPAYOFFLTrx = (TextView) findViewById(R.id.total_promptpayoffl_trx);
            TextView totalPROMPTPAYOFFLAmnt = (TextView) findViewById(R.id.total_promptpayoffl_amt);
            TextView totalPROMPTPAYOFFLRefundTrx = (TextView) findViewById(R.id.total_promptpayoffl_refund_trx);
            TextView totalPROMPTPAYOFFLRefundAmnt = (TextView) findViewById(R.id.total_promptpayoffl_refund_amnt);
            TextView totalPROMPTPAYOFFLRequestRefundTrx = (TextView) findViewById(R.id.total_promptpayoffl_request_refund_trx);
            TextView totalPROMPTPAYOFFLRequestRefundAmnt = (TextView) findViewById(R.id.total_promptpayoffl_request_refund_amnt);
            TextView totalPROMPTPAYOFFLVoidTrx = (TextView) findViewById(R.id.total_promptpayoffl_void_trx);
            TextView totalPROMPTPAYOFFLVoidAmnt = (TextView) findViewById(R.id.total_promptpayoffl_void_amnt);

            totalPROMPTPAYOFFLTrx.setText(Integer.toString(totalTrxPROMPTPAYOFFL));
            totalPROMPTPAYOFFLAmnt.setText(currency + " " + String.format("%,.2f", PROMPTPAYOFFLAmnt));
            totalPROMPTPAYOFFLRefundTrx.setText(Integer.toString(totalRefundPROMPTPAYOFFLTrx));
            totalPROMPTPAYOFFLRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundPROMPTPAYOFFLAmnt));
            totalPROMPTPAYOFFLRequestRefundTrx.setText(Integer.toString(totalRequestRefundPROMPTPAYOFFLTrx));
            totalPROMPTPAYOFFLRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundPROMPTPAYOFFLAmnt));
            totalPROMPTPAYOFFLVoidTrx.setText(Integer.toString(totalVoidPROMPTPAYOFFLTrx));
            totalPROMPTPAYOFFLVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidPROMPTPAYOFFLAmnt));
        }

        // UNIONPAYOFFL
        if (totalTrxUNIONPAYOFFL == 0 && totalRefundUNIONPAYOFFLTrx == 0 && totalRequestRefundUNIONPAYOFFLTrx == 0 && totalVoidUNIONPAYOFFLTrx == 0) {
            unionpayoffl.setVisibility(View.GONE);
        } else {
            unionpayoffl.setVisibility(View.VISIBLE);
            TextView totalUNIONPAYOFFLTrx = (TextView) findViewById(R.id.total_unionpayoffl_trx);
            TextView totalUNIONPAYOFFLAmnt = (TextView) findViewById(R.id.total_unionpayoffl_amt);
            TextView totalUNIONPAYOFFLRefundTrx = (TextView) findViewById(R.id.total_unionpayoffl_refund_trx);
            TextView totalUNIONPAYOFFLRefundAmnt = (TextView) findViewById(R.id.total_unionpayoffl_refund_amnt);
            TextView totalUNIONPAYOFFLRequestRefundTrx = (TextView) findViewById(R.id.total_unionpayoffl_request_refund_trx);
            TextView totalUNIONPAYOFFLRequestRefundAmnt = (TextView) findViewById(R.id.total_unionpayoffl_request_refund_amnt);
            TextView totalUNIONPAYOFFLVoidTrx = (TextView) findViewById(R.id.total_unionpayoffl_void_trx);
            TextView totalUNIONPAYOFFLVoidAmnt = (TextView) findViewById(R.id.total_unionpayoffl_void_amnt);

            totalUNIONPAYOFFLTrx.setText(Integer.toString(totalTrxUNIONPAYOFFL));
            totalUNIONPAYOFFLAmnt.setText(currency + " " + String.format("%,.2f", UNIONPAYOFFLAmnt));
            totalUNIONPAYOFFLRefundTrx.setText(Integer.toString(totalRefundUNIONPAYOFFLTrx));
            totalUNIONPAYOFFLRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundPROMPTPAYOFFLAmnt));
            totalUNIONPAYOFFLRequestRefundTrx.setText(Integer.toString(totalRequestRefundUNIONPAYOFFLTrx));
            totalUNIONPAYOFFLRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundUNIONPAYOFFLAmnt));
            totalUNIONPAYOFFLVoidTrx.setText(Integer.toString(totalVoidUNIONPAYOFFLTrx));
            totalUNIONPAYOFFLVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidUNIONPAYOFFLAmnt));
        }

        if (totalTrxVisa == 0 && totalRefundVisaTrx == 0 && totalRequestRefundVisaTrx == 0 && totalVoidVisaTrx == 0) {
            visa.setVisibility(visa.GONE);
        } else {
            //VISA
            visa.setVisibility(visa.VISIBLE);
            TextView totalVisaTrx = (TextView) findViewById(R.id.total_visa_trx);
            TextView totalVisaAmnt = (TextView) findViewById(R.id.total_visa_amt);
            TextView totalVisaRefundTrx = (TextView) findViewById(R.id.total_visa_refund_trx);
            TextView totalVisaRefundAmnt = (TextView) findViewById(R.id.total_visa_refund_amnt);
            TextView totalVisaRequestRefundTrx = (TextView) findViewById(R.id.total_visa_request_refund_trx);
            TextView totalVisaRequestRefundAmnt = (TextView) findViewById(R.id.total_visa_request_refund_amnt);
            TextView totalVisaVoidTrx = (TextView) findViewById(R.id.total_visa_void_trx);
            TextView totalVisaVoidAmnt = (TextView) findViewById(R.id.total_visa_void_amnt);

            totalVisaTrx.setText(Integer.toString(totalTrxVisa));
            totalVisaAmnt.setText(currency + " " + String.format("%,.2f", visaAmnt));
            totalVisaRefundTrx.setText(Integer.toString(totalRefundVisaTrx));
            totalVisaRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundVisaAmnt));
            totalVisaRequestRefundTrx.setText(Integer.toString(totalRequestRefundVisaTrx));
            totalVisaRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundVisaAmnt));
            totalVisaVoidTrx.setText(Integer.toString(totalVoidVisaTrx));
            totalVisaVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidVisaAmnt));
        }

        // WECHATOFFL
        if (totalTrxWECHATOFFL == 0 && totalRefundWECHATOFFLTrx == 0 && totalRequestRefundWECHATOFFLTrx == 0 && totalVoidWECHATOFFLTrx == 0) {
            wechatoffl.setVisibility(View.GONE);
        } else {

            wechatoffl.setVisibility(View.VISIBLE);
            TextView totalWECHATOFFLTrx = (TextView) findViewById(R.id.total_wechatoffl_trx);
            TextView totalWECHATOFFLAmnt = (TextView) findViewById(R.id.total_wechatoffl_amt);
            TextView totalWECHATOFFLRefundTrx = (TextView) findViewById(R.id.total_wechatoffl_refund_trx);
            TextView totalWECHATOFFLPayRefundAmnt = (TextView) findViewById(R.id.total_wechatoffl_refund_amnt);
            TextView totalWECHATOFFLRequestRefundTrx = (TextView) findViewById(R.id.total_wechatoffl_request_refund_trx);
            TextView totalWECHATOFFLRequestRefundAmnt = (TextView) findViewById(R.id.total_wechatoffl_request_refund_amnt);
            TextView totalWECHATOFFLVoidTrx = (TextView) findViewById(R.id.total_wechatoffl_void_trx);
            TextView totalWECHATOFFLVoidAmnt = (TextView) findViewById(R.id.total_wechatoffl_void_amnt);

            totalWECHATOFFLTrx.setText(Integer.toString(totalTrxWECHATOFFL));
            totalWECHATOFFLAmnt.setText(currency + " " + String.format("%,.2f", WECHATOFFLAmnt));
            totalWECHATOFFLRefundTrx.setText(Integer.toString(totalRefundWECHATOFFLTrx));
            totalWECHATOFFLPayRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundWECHATOFFLAmnt));
            totalWECHATOFFLRequestRefundTrx.setText(Integer.toString(totalRequestRefundWECHATOFFLTrx));
            totalWECHATOFFLRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundWECHATOFFLAmnt));
            totalWECHATOFFLVoidTrx.setText(Integer.toString(totalVoidWECHATOFFLTrx));
            totalWECHATOFFLVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidWECHATOFFLAmnt));
        }

        // WECHATHKOFFL
        if (totalTrxWECHATHKOFFL == 0 && totalRefundWECHATHKOFFLTrx == 0 && totalRequestRefundWECHATHKOFFLTrx == 0 && totalVoidWECHATHKOFFLTrx == 0) {
            wechathkoffl.setVisibility(View.GONE);
        } else {

            wechathkoffl.setVisibility(View.VISIBLE);
            TextView totalWECHATHKOFFLTrx = (TextView) findViewById(R.id.total_wechathkoffl_trx);
            TextView totalWECHATHKOFFLAmnt = (TextView) findViewById(R.id.total_wechathkoffl_amt);
            TextView totalWECHATHKOFFLRefundTrx = (TextView) findViewById(R.id.total_wechathkoffl_refund_trx);
            TextView totalWECHATHKOFFLPayRefundAmnt = (TextView) findViewById(R.id.total_wechathkoffl_refund_amnt);
            TextView totalWECHATHKOFFLRequestRefundTrx = (TextView) findViewById(R.id.total_wechathkoffl_request_refund_trx);
            TextView totalWECHATHKOFFLRequestRefundAmnt = (TextView) findViewById(R.id.total_wechathkoffl_request_refund_amnt);
            TextView totalWECHATHKOFFLVoidTrx = (TextView) findViewById(R.id.total_wechathkoffl_void_trx);
            TextView totalWECHATHKOFFLVoidAmnt = (TextView) findViewById(R.id.total_wechathkoffl_void_amnt);

            totalWECHATHKOFFLTrx.setText(Integer.toString(totalTrxWECHATHKOFFL));
            totalWECHATHKOFFLAmnt.setText(currency + " " + String.format("%,.2f", WECHATHKOFFLAmnt));
            totalWECHATHKOFFLRefundTrx.setText(Integer.toString(totalRefundWECHATHKOFFLTrx));
            totalWECHATHKOFFLPayRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundWECHATHKOFFLAmnt));
            totalWECHATHKOFFLRequestRefundTrx.setText(Integer.toString(totalRequestRefundWECHATHKOFFLTrx));
            totalWECHATHKOFFLRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundWECHATHKOFFLAmnt));
            totalWECHATHKOFFLVoidTrx.setText(Integer.toString(totalVoidWECHATHKOFFLTrx));
            totalWECHATHKOFFLVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidWECHATHKOFFLAmnt));
        }

        // WECHATONL
        if (totalTrxWECHATONL == 0 && totalRefundWECHATONLTrx == 0 && totalRequestRefundWECHATONLTrx == 0 && totalVoidWECHATONLTrx == 0) {
            wechatonl.setVisibility(View.GONE);
        } else {
            wechatonl.setVisibility(View.VISIBLE);
            TextView totalWECHATONLTrx = (TextView) findViewById(R.id.total_wechatonl_trx);
            TextView totalWECHATONLAmnt = (TextView) findViewById(R.id.total_wechatonl_amt);
            TextView totalWECHATONLRefundTrx = (TextView) findViewById(R.id.total_wechatonl_refund_trx);
            TextView totalWECHATONLRefundAmnt = (TextView) findViewById(R.id.total_wechatonl_refund_amnt);
            TextView totalWECHATONLRequestRefundTrx = (TextView) findViewById(R.id.total_wechatonl_request_refund_trx);
            TextView totalWECHATONLRequestRefundAmnt = (TextView) findViewById(R.id.total_wechatonl_request_refund_amnt);
            TextView totalWECHATONLVoidTrx = (TextView) findViewById(R.id.total_wechatonl_void_trx);
            TextView totalWECHATONLVoidAmnt = (TextView) findViewById(R.id.total_wechatonl_void_amnt);

            totalWECHATONLTrx.setText(Integer.toString(totalTrxWECHATONL));
            totalWECHATONLAmnt.setText(currency + " " + String.format("%,.2f", WECHATONLAmnt));
            totalWECHATONLRefundTrx.setText(Integer.toString(totalRefundWECHATONLTrx));
            totalWECHATONLRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRefundWECHATONLAmnt));
            totalWECHATONLRequestRefundTrx.setText(Integer.toString(totalRequestRefundWECHATONLTrx));
            totalWECHATONLRequestRefundAmnt.setText(currency + " " + String.format("%,.2f", totalRequestRefundWECHATONLAmnt));
            totalWECHATONLVoidTrx.setText(Integer.toString(totalVoidWECHATONLTrx));
            totalWECHATONLVoidAmnt.setText(currency + " " + String.format("%,.2f", totalVoidWECHATONLAmnt));
        }

        if (totalTrxALIPAYHKOFFL == 0 && totalRefundALIPAYHKOFFLTrx == 0 && totalRequestRefundALIPAYHKOFFLTrx == 0 && totalVoidALIPAYHKOFFLTrx == 0
                && totalTrxALIPAYCNOFFL == 0 && totalRefundALIPAYCNOFFLTrx == 0 && totalRequestRefundALIPAYCNOFFLTrx == 0 && totalVoidALIPAYCNOFFLTrx == 0
                && totalTrxALIPAYOFFL == 0 && totalRefundALIPAYOFFLTrx == 0 && totalRequestRefundALIPAYOFFLTrx == 0 && totalVoidALIPAYOFFLTrx == 0
                && totalTrxBOOSTOFFL == 0 && totalRefundBOOSTOFFLTrx == 0 && totalRequestRefundBOOSTOFFLTrx == 0 && totalVoidBOOSTOFFLTrx == 0
                && totalTrxGCASHOFFL == 0 && totalRefundGCASHOFFLTrx == 0 && totalRequestRefundGCASHOFFLTrx == 0 && totalVoidGCASHOFFLTrx == 0
                && totalTrxGRABPAYOFFL == 0 && totalRefundGRABPAYOFFLTrx == 0 && totalRequestRefundGRABPAYOFFLTrx == 0 && totalVoidGRABPAYOFFLTrx == 0
                && totalTrxMaster == 0 && totalRefundMasterTrx == 0 && totalRequestRefundMasterTrx == 0 && totalVoidMasterTrx == 0
                && totalTrxOEPAYOFFL == 0 && totalRefundOEPAYOFFLTrx == 0 && totalRequestRefundOEPAYOFFLTrx == 0 && totalVoidOEPAYOFFLTrx == 0
                && totalTrxPROMPTPAYOFFL == 0 && totalRefundPROMPTPAYOFFLTrx == 0 && totalRequestRefundPROMPTPAYOFFLTrx == 0 && totalVoidPROMPTPAYOFFLTrx == 0
                && totalTrxUNIONPAYOFFL == 0 && totalRefundUNIONPAYOFFLTrx == 0 && totalRequestRefundUNIONPAYOFFLTrx == 0 && totalVoidUNIONPAYOFFLTrx == 0
                && totalTrxVisa == 0 && totalRefundVisaTrx == 0 && totalRequestRefundVisaTrx == 0 && totalVoidVisaTrx == 0
                && totalTrxWECHATOFFL == 0 && totalRefundWECHATOFFLTrx == 0 && totalRequestRefundWECHATOFFLTrx == 0 && totalVoidWECHATOFFLTrx == 0
                && totalTrxWECHATHKOFFL == 0 && totalRefundWECHATHKOFFLTrx == 0 && totalRequestRefundWECHATHKOFFLTrx == 0 && totalVoidWECHATHKOFFLTrx == 0
                && totalTrxWECHATONL == 0 && totalRefundWECHATONLTrx == 0 && totalRequestRefundWECHATONLTrx == 0 && totalVoidWECHATONLTrx == 0 ) {
            tvnone.setVisibility(View.VISIBLE);
            paylist.setVisibility(View.GONE);
        }
    }

    public void loading() {

        loadingPanel.setVisibility(View.VISIBLE);
        grandtotal.setVisibility(View.GONE);
        tvnone.setVisibility(View.GONE);
        listfooter.setVisibility(View.GONE);
    }

    public void resetRes() {
        loadingPanel.setVisibility(RelativeLayout.GONE);
        tvnone.setVisibility(TextView.GONE);
        grandtotal.setVisibility(View.VISIBLE);
        listfooter.setVisibility(View.VISIBLE);
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void tapToRetry() {
        loadingPanel.setVisibility(View.GONE);
        tvnone.setVisibility(TextView.VISIBLE);
        tvnone.setText(R.string.tap_to_retry);
        tvnone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = getIntent();
                startActivity(intent);
                finish();
            }
        });
    }

    public void networkError() {

        loadingPanel.setVisibility(RelativeLayout.GONE);
        tvnone.setVisibility(TextView.VISIBLE);
        tvnone.setText(R.string.network_error);
        listfooter.setVisibility(RelativeLayout.GONE);
    }

    public void noResFound(String error) {
        currency = CurrCode.getName(currcode);

        loadingPanel.setVisibility(RelativeLayout.GONE);
        grandtotal.setVisibility(RelativeLayout.VISIBLE);

        TextView trxAmount = (TextView) findViewById(R.id.total_trx_amount);
        TextView totalvoidAmount = (TextView) findViewById(R.id.total_void_amount);
        TextView totalRefundAmount = (TextView) findViewById(R.id.total_refund_amount);
        TextView totalRequestRefundAmount = (TextView) findViewById(R.id.total_request_refund_amount);

        trxAmount.setText(currency + " " + String.format("%,.2f", totalAmount));
        totalRefundAmount.setText(currency + " " + String.format("%,.2f", totalrefund));
        totalRequestRefundAmount.setText(currency + " " + String.format("%,.2f", totalRequestRefund));
        totalvoidAmount.setText(currency + " " + String.format("%,.2f", totalvoid));
        tvnone.setVisibility(TextView.VISIBLE);
        if (!error.equals("")) {
            tvnone.setText(error);
        } else {
            tvnone.setText(R.string.noTxnFound);
        }
        paylist.setVisibility(RelativeLayout.GONE);
        listfooter.setVisibility(RelativeLayout.VISIBLE);
    }
    //==============================================//

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
