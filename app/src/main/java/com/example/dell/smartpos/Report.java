package com.example.dell.smartpos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.smartpos.Printer.K9.PrintUtilK9;
import com.example.dell.smartpos.Printer.PAX.GetObj;
import com.example.dell.smartpos.Printer.PrintUtil;

import org.apache.commons.lang3.builder.CompareToBuilder;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import eu.erikw.PullToRefreshListView;

public class Report extends AppCompatActivity implements ReportInterface {

    boolean CheckLogout = false;//logout flag
    String lang;
    Date lastUpdateTime;//lastest operation time

    private ReportAdapter adapter;
    private PullToRefreshListView lvCommonListView;
    String date1;
    String date2;
    String reportType;

    Spinner spinner;
    String filterStatus;
    String refno;
    String merchantId;

    String userID;
    String currency = "";

    int pageNo = Constants.CURRENT_PAGE_NO;
    int totalPage = 0;
    int perPage = 10;

    String[] b;
    String[] c;
    String[] ref;

    LinearLayout linearResult;

    RelativeLayout reportFooter;
    RelativeLayout loadingPanel;

    Button printReport;
    ProgressDialog progressDialog;
    TextView tvnone;
    ProgressBar pageLoading;
    ArrayList<Record> record_data = new ArrayList<Record>();

    private String user;
    private String password;
    private String merName;

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

    String label_trx_date = "";
    String label_amt = "";
    String label_merRef = "";
    String label_paymentRef = "";
    String label_qr_number = "";
    String label_invoice_no = "";


    //ALIPAYHKOFFL
    int ALIPAYHKOFFLTotalTrx = 0;
    double ALIPAYHKOFFLTotalAmnt = 0;
    ArrayList<Record> ALIPAYHKOFFLrecordArray;

    //ALIPAYCNOFFL
    int ALIPAYCNOFFLTotalTrx = 0;
    double ALIPAYCNOFFLTotalAmnt = 0;
    ArrayList<Record> ALIPAYCNOFFLrecordArray;

    //ALIPAYOFFL
    int ALIPAYOFFLTotalTrx = 0;
    double ALIPAYOFFLTotalAmnt = 0;
    ArrayList<Record> ALIPAYOFFLrecordArray;

    // BOOSTOFFL
    int BOOSTOFFLTotalTrx = 0;
    double BOOSTOFFLTotalAmnt = 0;
    ArrayList<Record> BOOSTOFFLrecordArray;

    // GCASHOFFL 20190911
    int GCASHOFFLTotalTrx = 0;
    double GCASHOFFLTotalAmnt = 0;
    ArrayList<Record> GCASHOFFLrecordArray;

    // GRABPAYOFFL 20190912
    int GRABPAYOFFLTotalTrx = 0;
    double GRABPAYOFFLTotalAmnt = 0;
    ArrayList<Record> GRABPAYOFFLrecordArray;

    // MASTER
    int MasterTotalTrx = 0;
    double masterTotalAmnt = 0;
    ArrayList<Record> masterrecordArray;

    // OEPAYOFFL
    int OEPAYOFFLTotalTrx = 0;
    double OEPAYOFFLTotalAmnt = 0;
    ArrayList<Record> OEPAYOFFLrecordArray;

    // PROMPTPAYOFFL 20190906
    int PROMPTPAYOFFLTotalTrx = 0;
    double PROMPTPAYOFFLTotalAmnt = 0;
    ArrayList<Record> PROMPTPAYOFFLrecordArray;

    // UNIONPAYOFFL 20190906
    int UNIONPAYOFFLTotalTrx = 0;
    double UNIONPAYOFFLTotalAmnt = 0;
    ArrayList<Record> UNIONPAYOFFLrecordArray;

    // VISA
    int VisaTotalTrx = 0;
    double visaTotalAmnt = 0;
    ArrayList<Record> visarecordArray;

    // WECHATOFFL
    int WECHATOFFLTotalTrx = 0;
    double WECHATOFFLTotalAmnt = 0;
    ArrayList<Record> WECHATOFFLrecordArray;

    // WECHATHKOFFL
    int WECHATHKOFFLTotalTrx = 0;
    double WECHATHKOFFLTotalAmnt = 0;
    ArrayList<Record> WECHATHKOFFLrecordArray;

    // WECHATONL
    int WECHATONLTotalTrx = 0;
    double WECHATONLTotalAmnt = 0;
    ArrayList<Record> WECHATONLrecordArray;

    String deviceName;
    PrintUtilK9 printUtilK9 = new PrintUtilK9(Report.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent intent = getIntent();
        reportType = intent.getStringExtra("reportType");

        if (reportType.equalsIgnoreCase("Transaction Report")) {
            setTitle(R.string.transaction_report);
            filterStatus = "Accepted,Accepted_Adj";
        } else if (reportType.equalsIgnoreCase("Refund Report")) {
            setTitle(R.string.refund_report);
            filterStatus = "Refunded,PartialRefunded";
        } else if (reportType.equalsIgnoreCase("Void Report")) {
            setTitle(R.string.void_report);
            filterStatus = "Voided";
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---------for autologout---------//
        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout, false);
        lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
        Log.d("KM", "init TransactionReport CheckLogout:" + CheckLogout + ",lastUpdateTime:" + lastUpdateTime);
        //---------for autologout---------//

        // Check language
        GlobalFunction.changeLanguage(Report.this);

        //---------for print func---------//
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

        deviceName = android.os.Build.MODEL;
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
            printUtilK9.bindService();
        }

        //---------for print func---------//

        date1 = intent.getStringExtra("fromDate");
        date2 = intent.getStringExtra("toDate");
        refno = intent.getStringExtra("refno");
        merchantId = intent.getStringExtra(Constants.MERID);

        getApiAccount();

        TextView tv1 = (TextView) findViewById(R.id.txtdate1);
        TextView tv2 = (TextView) findViewById(R.id.txtdate2);
        tv1.setText(date1);
        tv2.setText(date2);

        linearResult = (LinearLayout) findViewById(R.id.linearResult);
        reportFooter = (RelativeLayout) findViewById(R.id.reportFooter);
        lvCommonListView = (PullToRefreshListView) findViewById(R.id.reportListView);
        lvCommonListView.onRefreshComplete();
        tvnone = (TextView) findViewById(R.id.empty_view);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        pageLoading = (ProgressBar) findViewById(R.id.progressBar2);

        printReport = (Button) findViewById(R.id.print_report);
        printReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (reportType.equalsIgnoreCase("Transaction Report")) {
                    label_title = getString(R.string.transaction_report);
                } else if (reportType.equalsIgnoreCase("Refund Report")) {
                    label_title = getString(R.string.refund_report);
                } else if (reportType.equalsIgnoreCase("Void Report")) {
                    label_title = getString(R.string.void_report);
                }

                label_merName = getString(R.string.merName_label);
                label_from = getString(R.string.from);
                label_to = getString(R.string.to);
                label_print_at = getString(R.string.print_at_label);

                label_footer = getString(R.string.report_end);

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

                label_total_trx = getString(R.string.total_transaction_label);
                label_total_amnt = getString(R.string.total_amount_label);

                label_trx_date = getString(R.string.transaction_date_label);
                label_amt = getString(R.string.amount_label);
                label_merRef = getString(R.string.merchant_ref_label);
                label_paymentRef = getString(R.string.payment_ref_label);
                label_qr_number = getString(R.string.txn_number_label);
                label_invoice_no = getString(R.string.r_invoice_no);

                label = new TreeMap<String, String>();
                label.put("Title", label_title);

                //====Merchant Information===//
                label.put("MerNameLabel", label_merName);
                label.put("FromDateLabel", label_from);
                label.put("ToDateLabel", label_to);
                //============================//

                label.put("Header", label_header);
                label.put("Footer", label_footer);

                label.put("NoTxnFound", getString(R.string.noTxnFound));

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

                label.put("TransactionDate", label_trx_date);
                label.put("Amount", label_amt);
                label.put("MerRef", label_merRef);
                label.put("PaymentRef", label_paymentRef);
                label.put("QRNumber", label_qr_number);
                label.put("InvoiceNo", label_invoice_no);

                report_value = new TreeMap<String, String>();
                report_value.put("MerName", merName);
                report_value.put("From_date", date1);
                report_value.put("To_date", date2);
                report_value.put("currCode", currency);

                report_value.put("TotalTrxALIPAYHKOFFL", Integer.toString(ALIPAYHKOFFLTotalTrx));
                report_value.put("TotalAmntALIPAYHKOFFL", String.format("%,.2f", ALIPAYHKOFFLTotalAmnt));

                report_value.put("TotalTrxALIPAYCNOFFL", Integer.toString(ALIPAYCNOFFLTotalTrx));
                report_value.put("TotalAmntALIPAYCNOFFL", String.format("%,.2f", ALIPAYCNOFFLTotalAmnt));

                report_value.put("TotalTrxALIPAYOFFL", Integer.toString(ALIPAYOFFLTotalTrx));
                report_value.put("TotalAmntALIPAYOFFL", String.format("%,.2f", ALIPAYOFFLTotalAmnt));

                report_value.put("TotalTrxBOOSTOFFL", Integer.toString(BOOSTOFFLTotalTrx));
                report_value.put("TotalAmntBOOSTOFFL", String.format("%,.2f", BOOSTOFFLTotalAmnt));

                report_value.put("TotalTrxGCASHOFFL", Integer.toString(GCASHOFFLTotalTrx));
                report_value.put("TotalAmntGCASHOFFL", String.format("%,.2f", GCASHOFFLTotalAmnt));

                report_value.put("TotalTrxGRABPAYOFFL", Integer.toString(GRABPAYOFFLTotalTrx));
                report_value.put("TotalAmntGRABPAYOFFL", String.format("%,.2f", GRABPAYOFFLTotalAmnt));

                report_value.put("TotalTrxMASTER", Integer.toString((MasterTotalTrx)));
                report_value.put("TotalAmntMASTER", String.format("%,.2f", masterTotalAmnt));

                report_value.put("TotalTrxOEPAYOFFL", Integer.toString(OEPAYOFFLTotalTrx));
                report_value.put("TotalAmntOEPAYOFFL", String.format("%,.2f", OEPAYOFFLTotalAmnt));

                report_value.put("TotalTrxPROMPTPAYOFFL", Integer.toString(PROMPTPAYOFFLTotalTrx));
                report_value.put("TotalAmntPROMPTPAYOFFL", String.format("%,.2f", PROMPTPAYOFFLTotalAmnt));

                report_value.put("TotalTrxUNIONPAYOFFL", Integer.toString((UNIONPAYOFFLTotalTrx)));
                report_value.put("TotalAmntUNIONPAYOFFL", String.format("%,.2f", UNIONPAYOFFLTotalAmnt));

                report_value.put("TotalTrxVISA", Integer.toString((VisaTotalTrx)));
                report_value.put("TotalAmntVISA", String.format("%,.2f", visaTotalAmnt));

                report_value.put("TotalTrxWECHATOFFL", Integer.toString(WECHATOFFLTotalTrx));
                report_value.put("TotalAmntWECHATOFFL", String.format("%,.2f", WECHATOFFLTotalAmnt));

                report_value.put("TotalTrxWECHATHKOFFL", Integer.toString(WECHATHKOFFLTotalTrx));
                report_value.put("TotalAmntWECHATHKOFFL", String.format("%,.2f", WECHATHKOFFLTotalAmnt));

                report_value.put("TotalTrxWECHATONL", Integer.toString(WECHATONLTotalTrx));
                report_value.put("TotalAmntWECHATONL", String.format("%,.2f", WECHATONLTotalAmnt));

                printUtil = new PrintUtil(Report.this, printeraddress, printername, label, report_value,
                        ALIPAYHKOFFLrecordArray,
                        ALIPAYCNOFFLrecordArray,
                        ALIPAYOFFLrecordArray,
                        BOOSTOFFLrecordArray,
                        GCASHOFFLrecordArray,
                        GRABPAYOFFLrecordArray,
                        masterrecordArray,
                        OEPAYOFFLrecordArray,
                        PROMPTPAYOFFLrecordArray,
                        UNIONPAYOFFLrecordArray,
                        visarecordArray,
                        WECHATOFFLrecordArray,
                        WECHATHKOFFLrecordArray,
                        WECHATONLrecordArray);
                printUtil.sendReport();
            }
        });

        lvCommonListView.setTextFilterEnabled(true);
        lvCommonListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                progressDialog = new ProgressDialog(Report.this);
                progressDialog.setCancelable(false);
                String strpageno = Integer.toString(pageNo);
                Constants.CURRENT_PAGE_NO = pageNo;
                Report.LoadXML loadxml = new Report.LoadXML(Report.this, progressDialog, getPrefPayGate());
                loadxml.execute(date1, date2, strpageno);
            }
        });

        tvnone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                loading();
                progressDialog = new ProgressDialog(Report.this);
                String strpageno = Integer.toString(pageNo);
                Constants.CURRENT_PAGE_NO = pageNo;
                Report.LoadXML loadxml = new Report.LoadXML(Report.this, progressDialog, getPrefPayGate());
                loadxml.execute(date1, date2, strpageno);
            }
        });
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
    }


    public String getPrefPayGate() {
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate, Constants.default_paygate);
        return prefpaygate;
    }

    public void tapToRetry() {
        loadingPanel.setVisibility(View.GONE);
        tvnone.setVisibility(TextView.VISIBLE);
        tvnone.setText(R.string.tap_to_retry);
        tvnone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stubs

                Intent intent = getIntent();
                startActivity(intent);
                finish();
            }
        });
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

    private void getData() {
        // TODO Auto-generated method stub
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        String strpageno = Integer.toString(pageNo);
        LoadXML loadxml = new LoadXML(Report.this, progressDialog, getPrefPayGate());
        loadxml.execute(date1, date2, strpageno);
    }

    public void loadData(String merName, ArrayList<Record> record_data2) {
        resetRes();
        reportFooter.setVisibility(RelativeLayout.VISIBLE);
        adapter = new ReportAdapter(this, R.layout.report_item, record_data2,
                merName, this);
        lvCommonListView.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Report.this.finish();
//            TabGroupActivity parentActivity = (TabGroupActivity)getParent();
//            parentActivity.onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void getApiAccount() {
        //GET API FROM DB
        Intent his_list = getIntent();
        merName = his_list.getStringExtra(Constants.MERNAME);
//			String user="";
//			String password="";
        DatabaseHelper db = new DatabaseHelper(Report.this);
        String orgMerchantId = merchantId;
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
                    Toast.makeText(Report.this, R.string.apiID_pw_notset, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void setValues(TreeMap<String, String> reports,
                          ArrayList<Record> ALIPAYHKOFFLrecords,
                          ArrayList<Record> ALIPAYCNOFFLrecords,
                          ArrayList<Record> ALIPAYOFFLrecords,
                          ArrayList<Record> BOOSTOFFLrecords,
                          ArrayList<Record> GCASHOFFLrecords,
                          ArrayList<Record> GRABPAYOFFLrecords,
                          ArrayList<Record> masterRecords,
                          ArrayList<Record> OEPAYOFFLrecords,
                          ArrayList<Record> PROMPTPAYOFFLrecords,
                          ArrayList<Record> UNIONPAYOFFLrecords,
                          ArrayList<Record> visaRecords,
                          ArrayList<Record> WECHATOFFLrecords,
                          ArrayList<Record> WECHATHKOFFLrecords,
                          ArrayList<Record> WECHATONLrecords) {
        ALIPAYHKOFFLTotalTrx = Integer.parseInt(reports.get("ALIPAYHKOFFLTotalTrx"));
        ALIPAYHKOFFLTotalAmnt = Double.parseDouble(reports.get("ALIPAYHKOFFLTotalAmt"));

        ALIPAYCNOFFLTotalTrx = Integer.parseInt(reports.get("ALIPAYCNOFFLTotalTrx"));
        ALIPAYCNOFFLTotalAmnt = Double.parseDouble(reports.get("ALIPAYCNOFFLTotalAmt"));

        ALIPAYOFFLTotalTrx = Integer.parseInt(reports.get("ALIPAYOFFLTotalTrx"));
        ALIPAYOFFLTotalAmnt = Double.parseDouble(reports.get("ALIPAYOFFLTotalAmt"));

        BOOSTOFFLTotalTrx = Integer.parseInt(reports.get("BOOSTOFFLTotalTrx"));
        BOOSTOFFLTotalAmnt = Double.parseDouble(reports.get("BOOSTOFFLTotalAmt"));

        GCASHOFFLTotalTrx = Integer.parseInt(reports.get("GCASHOFFLTotalTrx"));
        GCASHOFFLTotalAmnt = Double.parseDouble(reports.get("GCASHOFFLTotalAmt"));

        GRABPAYOFFLTotalTrx = Integer.parseInt(reports.get("GRABPAYOFFLTotalTrx"));
        GRABPAYOFFLTotalAmnt = Double.parseDouble(reports.get("GRABPAYOFFLTotalAmt"));

        MasterTotalTrx = Integer.parseInt(reports.get("MasterTotalTrx"));
        masterTotalAmnt = Double.parseDouble(reports.get("MasterTotalAmt"));

        OEPAYOFFLTotalTrx = Integer.parseInt(reports.get("OEPAYOFFLTotalTrx"));
        OEPAYOFFLTotalAmnt = Double.parseDouble(reports.get("OEPAYOFFLTotalAmt"));

        PROMPTPAYOFFLTotalTrx = Integer.parseInt(reports.get("PROMPTPAYOFFLTotalTrx"));
        PROMPTPAYOFFLTotalAmnt = Double.parseDouble(reports.get("PROMPTPAYOFFLTotalAmt"));

        UNIONPAYOFFLTotalTrx = Integer.parseInt(reports.get("UNIONPAYOFFLTotalTrx"));
        UNIONPAYOFFLTotalAmnt = Double.parseDouble(reports.get("UNIONPAYOFFLTotalAmt"));

        VisaTotalTrx = Integer.parseInt(reports.get("VisaTotalTrx"));
        visaTotalAmnt = Double.parseDouble(reports.get("VisaTotalAmt"));

        WECHATOFFLTotalTrx = Integer.parseInt(reports.get("WECHATOFFLTotalTrx"));
        WECHATOFFLTotalAmnt = Double.parseDouble(reports.get("WECHATOFFLTotalAmt"));

        WECHATHKOFFLTotalTrx = Integer.parseInt(reports.get("WECHATHKOFFLTotalTrx"));
        WECHATHKOFFLTotalAmnt = Double.parseDouble(reports.get("WECHATHKOFFLTotalAmt"));

        ALIPAYHKOFFLrecordArray = ALIPAYHKOFFLrecords;
        ALIPAYCNOFFLrecordArray = ALIPAYCNOFFLrecords;
        ALIPAYOFFLrecordArray = ALIPAYOFFLrecords;
        BOOSTOFFLrecordArray = BOOSTOFFLrecords;
        GCASHOFFLrecordArray = GCASHOFFLrecords;
        GRABPAYOFFLrecordArray = GRABPAYOFFLrecords;
        masterrecordArray = masterRecords;
        OEPAYOFFLrecordArray = OEPAYOFFLrecords;
        PROMPTPAYOFFLrecordArray = PROMPTPAYOFFLrecords;
        UNIONPAYOFFLrecordArray = UNIONPAYOFFLrecords;
        visarecordArray = visaRecords;
        WECHATOFFLrecordArray = WECHATOFFLrecords;
        WECHATHKOFFLrecordArray = WECHATHKOFFLrecords;
        WECHATONLrecordArray = WECHATONLrecords;
    }

    @SuppressLint("DefaultLocale")
    private class LoadXML extends AsyncTask<String, Void, ArrayList<Record>> {
        private ProgressDialog progressDialog;
        private Report activity;
        private String payGate;

        private String baseUrl = null;
        private String result = "";
        private ArrayList<Record> record_data = new ArrayList<Record>();

        public LoadXML(Report activity, ProgressDialog progressDialog, String payGate) {
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

            NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", merchantId);
            NameValuePair loginIdNVPair = new BasicNameValuePair("loginId", user);
            NameValuePair passwordNVPair = new BasicNameValuePair("password", password);
            NameValuePair startDateNVPair = new BasicNameValuePair("startDate", d1);
            //        NameValuePair pageNoNVPair = new BasicNameValuePair("pageNo",arg0[2]);
            //        NameValuePair pageRecordsNVPair = new BasicNameValuePair("pageRecords",String.valueOf(perPage));
            NameValuePair endDateNVPair = new BasicNameValuePair("endDate", d2);
            NameValuePair mobileNVPair = new BasicNameValuePair("enableMobile", "T");
            NameValuePair sortOrderNVpair = new BasicNameValuePair("sortOrder", "asc");
            NameValuePair recordStatusNVPair = new BasicNameValuePair("orderStatus", filterStatus);
            NameValuePair operatorIdNVPair = new BasicNameValuePair("operatorId", "");
            //				NameValuePair channelTypeNVPair = new BasicNameValuePair("channelType","MPOS");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(merchantIdNVPair);
            nameValuePairs.add(loginIdNVPair);
            nameValuePairs.add(passwordNVPair);
            nameValuePairs.add(startDateNVPair);
            nameValuePairs.add(endDateNVPair);
            nameValuePairs.add(mobileNVPair);
            //        nameValuePairs.add(pageNoNVPair);
            //        nameValuePairs.add(pageRecordsNVPair);
            nameValuePairs.add(sortOrderNVpair);
            nameValuePairs.add(operatorIdNVPair);
            nameValuePairs.add(recordStatusNVPair);
            //		        nameValuePairs.add(channelTypeNVPair)  ;

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
                        String text = result;
                        System.out.println("[Report] result: " + result);
                        InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
                        XML2Record xml2Record = new XML2Record();
                        Xml.parse(is, Xml.Encoding.UTF_8, xml2Record);
                        List<Record> records = xml2Record.getRecords();
                        int i = 0;
                        Record recordData[] = new Record[records.size()];
                        for (Record record : records) {
                            if (Double.parseDouble(record.getamt()) > 0.0) {
                                currency = record.currency();
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
                                Log.d("pMethod", record.getPaymethod());
                                i++;
                            }
                        } //end for loop
                        Double total = Double.parseDouble((xml2Record.gettotalPage()));
                        totalPage = (int) Math.ceil(total / perPage);
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

                Collections.sort(record_data, new RecordComparator());

                activity.loadData(merName, record_data);
                lvCommonListView.onRefreshComplete();
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

    public class RecordComparator implements Comparator<Record> {

        @Override
        public int compare(Record r1, Record r2) {
            return new CompareToBuilder()
                    .append(r1.getPaymethod(), r2.getPaymethod()).toComparison();
        }
    }

    public void networkError() {
        // TODO Auto-generated method stub
        lvCommonListView.setVisibility(ListView.GONE);
        linearResult.setVisibility(View.GONE);
        loadingPanel.setVisibility(RelativeLayout.GONE);
        tvnone.setVisibility(TextView.VISIBLE);
        tvnone.setText(R.string.network_error);
        reportFooter.setVisibility(RelativeLayout.GONE);
    }

    public void noResFound(String error) {
        // TODO Auto-generated method stub
        lvCommonListView.setVisibility(ListView.GONE);
        linearResult.setVisibility(View.GONE);
        loadingPanel.setVisibility(RelativeLayout.GONE);
        tvnone.setVisibility(TextView.VISIBLE);
        if (!error.equals("")) {
            tvnone.setText(error);
        } else {
            tvnone.setText(R.string.noTxnFound);
        }
        reportFooter.setVisibility(RelativeLayout.VISIBLE);
    }

    public void loading() {

        loadingPanel.setVisibility(View.VISIBLE);
        linearResult.setVisibility(View.GONE);
        lvCommonListView.setVisibility(View.GONE);
        reportFooter.setVisibility(View.GONE);
        tvnone.setVisibility(View.GONE);
    }

    public void resetRes() {
        linearResult.setVisibility(View.VISIBLE);
        lvCommonListView.setVisibility(ListView.VISIBLE);
        loadingPanel.setVisibility(RelativeLayout.GONE);
        tvnone.setVisibility(TextView.GONE);
        reportFooter.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                pageNo = 1;
                String strpageno = Integer.toString(pageNo);
                LoadXML loadxml = new LoadXML(Report.this, progressDialog, getPrefPayGate());
                loadxml.execute(date1, date2, strpageno);
            }

            if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}