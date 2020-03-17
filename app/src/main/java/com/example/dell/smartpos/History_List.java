package com.example.dell.smartpos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

import static java.lang.Double.parseDouble;

public class History_List extends AppCompatActivity {

    private HistoryAdapter adapter;
    String lang;
    private PullToRefreshListView lvCommonListView;
    String date1;
    String date2;

    String filter;
    Spinner spinner;
    String filterStatus;
    String filterRef;
    String refno;
    String merchantId;

    String userID;
    Date fromDate;
    Date toDate;
    Boolean skipdate1 = false;
    Boolean skipdate2 = false;
    Boolean skipdate = false;

    int pageNo = Constants.CURRENT_PAGE_NO;
    int totalPage = 0;
    int perPage = 10;

    String[] b;
    String[] c;
    String[] ref;

    RelativeLayout listfooter;
    Button prev;
    Button next;
    ProgressDialog progressDialog;
    TextView tvnone;
    LinearLayout linearResult;
    RelativeLayout loadingPanel;
    ProgressBar pageLoading;
    ArrayList<Record> record_data = new ArrayList<Record>();

    private String user;
    private String password;
    private String merName;

    //---------for autologout---------//
    Handler CheckTimeOutHandler = new Handler();
    Date lastUpdateTime;//lastest operation time
    long timePeriod;//no operation time
    float SESSION_EXPIRED = Constants.SESSION_EXPIRY / 1000;//session expired time
    boolean CheckLogout = false;//logout flag
    long CheckInterval = 1000;//checking time intercal

    //---------for autologout---------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_list);

        setTitle(R.string.transaction_history_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent his_list = getIntent();
        filterStatus = his_list.getStringExtra("filterStatus");
        date1 = his_list.getStringExtra("fromDate");
        date2 = his_list.getStringExtra("toDate");
        refno = his_list.getStringExtra("refno");
        filterRef = his_list.getStringExtra("refFilter");
        merchantId = his_list.getStringExtra(Constants.MERID);
        Log.d("filterStatus", filterStatus);
        Log.d("date1", date1);
        Log.d("date2", date2);
        Log.d("refno", refno);
        Log.d("filterRef", filterRef);

        getApiAccount();

        TextView tv1 = (TextView) findViewById(R.id.txtdate1);
        TextView tv2 = (TextView) findViewById(R.id.txtdate2);
        tv1.setText(date1);
        tv2.setText(date2);

        linearResult = (LinearLayout) findViewById(R.id.linearResult);
        listfooter = (RelativeLayout) findViewById(R.id.listfooter);
        lvCommonListView = (PullToRefreshListView) findViewById(R.id.listView1);
        lvCommonListView.onRefreshComplete();
        tvnone = (TextView) findViewById(R.id.empty_view);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        pageLoading = (ProgressBar) findViewById(R.id.progressBar2);

        prev = (Button) findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (pageNo >= 2) {
                    pageNo--;
                    Constants.CURRENT_PAGE_NO = pageNo;
                    loadPageText();
                    getData();
                }
            }

        });

        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (pageNo < totalPage) {
                    pageNo++;
                    loadPageText();
                    getData();
                    Constants.CURRENT_PAGE_NO = pageNo;
                }
            }
        });

        lvCommonListView.setTextFilterEnabled(true);
        lvCommonListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressDialog = new ProgressDialog(History_List.this);
                progressDialog.setCancelable(false);
                String strpageno = Integer.toString(pageNo);
                Constants.CURRENT_PAGE_NO = pageNo;
                LoadXML loadxml = new LoadXML(History_List.this, progressDialog, GlobalFunction.getPrefPayGate(History_List.this));
                loadxml.execute(date1, date2, strpageno);
            }
        });

        lvCommonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent his_list = getIntent();
                Intent intent = new Intent(History_List.this, HistoryDetails.class);
                Record pos = (Record) parent.getItemAtPosition(position + 1);
                System.out.println("KJ settle = " + pos.getSettle());
                intent.putExtra(Constants.MERID, his_list.getStringExtra(Constants.MERID));
                intent.putExtra(Constants.USERID, userID);
                intent.putExtra(Constants.MERNAME, pos.getMerName());
                intent.putExtra(Constants.CURR, pos.currency());
                intent.putExtra(Constants.AMOUNT, pos.getamt());
                intent.putExtra(Constants.SURCHARGE, pos.getSurcharge());
                intent.putExtra(Constants.MERREQUESTAMT, pos.getMerRequestAmt());
                intent.putExtra(Constants.PAYBANKID, pos.getBankId());
                //intent.putExtra(Constants.PAYBANKNAME, pos.getBankId());
                intent.putExtra(Constants.PAYMETHOD, pos.payMethod());
                intent.putExtra(Constants.PAYREF, pos.getPaymentRef());
                intent.putExtra(Constants.MERCHANT_REF, pos.getMerchantRef());
                intent.putExtra(Constants.CARDNO, pos.getCardNo());
                intent.putExtra(Constants.DATE, pos.getOrderdate());
                intent.putExtra(Constants.CARDHOLDER, pos.getCardHolder());
                intent.putExtra(Constants.STATUS, pos.status());
                intent.putExtra(Constants.REMARK, pos.remark());
                intent.putExtra(Constants.SETTLE, pos.getSettle());
                intent.putExtra(Constants.BATCH_NO, pos.getBatchNo());
                intent.putExtra(Constants.INVOICE_NO, pos.getInvoiceNo());
                intent.putExtra(Constants.TRACE_NO, pos.getTraceNo());


                startActivityForResult(intent, 1);
            }
        });

        tvnone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                loading();
                progressDialog = new ProgressDialog(History_List.this);
                String strpageno = Integer.toString(pageNo);
                Constants.CURRENT_PAGE_NO = pageNo;
                LoadXML loadxml = new LoadXML(History_List.this, progressDialog, GlobalFunction.getPrefPayGate(History_List.this));
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

    public void tapToRetry() {
        loadingPanel.setVisibility(View.GONE);
        tvnone.setVisibility(TextView.VISIBLE);
        tvnone.setText(R.string.tap_to_retry);
        tvnone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d("KM", "init SummaryReport CheckLogout:" + CheckLogout + ",lastUpdateTime:" + lastUpdateTime);
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

    protected void updatePageText() {
        TextView tv1 = (TextView) findViewById(R.id.pageNo);
        tv1.setVisibility(View.VISIBLE);
        pageLoading.setVisibility(View.GONE);
        String pageLabel = getString(R.string.page) + " " + pageNo + " " + getString(R.string.of) + " " + totalPage;
        tv1.setText(pageLabel);

        if (pageNo == 1) {
            prev.setEnabled(false);
        } else {
            prev.setEnabled(true);
        }

        if (pageNo == totalPage) {
            next.setEnabled(false);
        } else {
            next.setEnabled(true);
        }
    }

    public void loadPageText() {
        TextView tv1 = (TextView) findViewById(R.id.pageNo);
        tv1.setVisibility(View.GONE);
        pageLoading.setVisibility(View.VISIBLE);
    }

    private void getData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        String strpageno = Integer.toString(pageNo);
        LoadXML loadxml = new LoadXML(History_List.this, progressDialog, GlobalFunction.getPrefPayGate(History_List.this));
        loadxml.execute(date1, date2, strpageno);
    }

    public void loadData(String merName, ArrayList<Record> record_data2) {
        resetRes();
        filter = "merchaRef";
        listfooter.setVisibility(RelativeLayout.VISIBLE);
        adapter = new HistoryAdapter(this, R.layout.history_list_item, record_data2, filter, merName);
        lvCommonListView.setAdapter(adapter);
        updatePageText();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            History_List.this.finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void getApiAccount() {
        //GET API FROM DB
        Intent his_list = getIntent();
        merName = his_list.getStringExtra(Constants.MERNAME);
        DatabaseHelper db = new DatabaseHelper(History_List.this);
        String orgMerchantId = merchantId;
        String encMerchantId = "";
        DesEncrypter encrypt;
        try {
            encrypt = new DesEncrypter(merName);
            encMerchantId = encrypt.encrypt(orgMerchantId);
        } catch (Exception e2) {
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
                e.printStackTrace();
            }
        } else {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(History_List.this, R.string.apiID_pw_notset, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @SuppressLint("DefaultLocale")
    private class LoadXML extends AsyncTask<String, Void, ArrayList<Record>> {
        private ProgressDialog progressDialog;
        private History_List activity;
        private String payGate;

        private String baseUrl = null;
        private String result = "";
        private ArrayList<Record> record_data = new ArrayList<Record>();

        public LoadXML(History_List activity, ProgressDialog progressDialog, String payGate) {
            this.activity = activity;
            this.progressDialog = progressDialog;
            this.payGate = payGate;
        }


        @Override
        protected ArrayList<Record> doInBackground(String... arg0) {
            String d1 = arg0[0].replace("/", "") + "000000";
            String d2 = arg0[1].replace("/", "") + "235959";

            //getAPIAct

            NameValuePair merchantIdNVPair = new BasicNameValuePair("merchantId", merchantId);
            NameValuePair loginIdNVPair = new BasicNameValuePair("loginId", user);
            NameValuePair passwordNVPair = new BasicNameValuePair("password", password);
            NameValuePair startDateNVPair = new BasicNameValuePair("startDate", d1);
            NameValuePair pageNoNVPair = new BasicNameValuePair("pageNo", arg0[2]);
            NameValuePair pageRecordsNVPair = new BasicNameValuePair("pageRecords", String.valueOf(perPage));
            NameValuePair endDateNVPair = new BasicNameValuePair("endDate", d2);
            NameValuePair mobileNVPair = new BasicNameValuePair("enableMobile", "T");
            NameValuePair sortOrderNVpair = new BasicNameValuePair("sortOrder", "desc");
            NameValuePair operatorIdNVPair = new BasicNameValuePair("operatorId", "");

            Log.d("startDate", d1);
            Log.d("pageNo", arg0[2]);
            Log.d("pageRecords", String.valueOf(perPage));
            Log.d("endDate", d2);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(merchantIdNVPair);
            nameValuePairs.add(loginIdNVPair);
            nameValuePairs.add(passwordNVPair);
            nameValuePairs.add(startDateNVPair);
            nameValuePairs.add(endDateNVPair);
            nameValuePairs.add(mobileNVPair);
            nameValuePairs.add(pageNoNVPair);
            nameValuePairs.add(pageRecordsNVPair);
            nameValuePairs.add(sortOrderNVpair);
            nameValuePairs.add(operatorIdNVPair);

            if (!filterStatus.equalsIgnoreCase("All")) {
                NameValuePair recordStatusNVPair = new BasicNameValuePair("orderStatus", filterStatus);
                nameValuePairs.add(recordStatusNVPair);
            }

            if (!"".equals(refno)) {
                if (filterRef.equalsIgnoreCase("PayRef")) {
                    NameValuePair payRefNVPair = new BasicNameValuePair("payRef", refno);
                    nameValuePairs.add(payRefNVPair);
                } else if (filterRef.equalsIgnoreCase("MerRef")) {
                    NameValuePair orderRefNVPair = new BasicNameValuePair("orderRef", refno);
                    nameValuePairs.add(orderRefNVPair);
                }
            }

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
                        System.out.println("XML result: " + line);
                    }
                    try {
                        String text = result;
                        System.out.println("KJ---" + text);
                        InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
                        XML2Record xml2Record = new XML2Record();
                        android.util.Xml.parse(is, Xml.Encoding.UTF_8, xml2Record);
                        List<Record> records = xml2Record.getRecords();
                        int i = 0;
                        Record recordData[] = new Record[records.size()];
                        for (Record record : records) {
                            if (parseDouble(record.getamt()) >= 0.0 && !record.orderstatus().equals("Voided")) {
                                recordData[i] = new Record(record.PayRef(), record.merref(),
                                        record.getOrderdate(), record.currency(), record.getamt(),
                                        record.getSurcharge(), record.getMerRequestAmt(),
                                        record.remark(), record.orderstatus(), merName,
                                        record.getPayType(), record.getPaymethod(),
                                        record.getPayBankId(), record.getCardNo(),
                                        record.getCardHolder(), record.getSettle(),
                                        record.getBankId(), record.getSettleTime(), record.getBatchNo(),
                                        record.getTraceNo(), record.getInvoiceNo());
                                record_data.add(recordData[i]);
                                i++;
                            }
                            // Display the new created voided txn details
                            // rather then origin sale txn become voided txn
                            else if (parseDouble(record.getamt()) < 0.0 && record.orderstatus().equals("Voided")) {
                                recordData[i] = new Record(record.PayRef(), record.merref(),
                                        record.getOrderdate(), record.currency(),
                                        String.valueOf(parseDouble(record.getamt()) * -1),
                                        record.getSurcharge(), record.getMerRequestAmt(),
                                        record.remark(), record.orderstatus(), merName,
                                        record.getPayType(), record.getPaymethod(),
                                        record.getPayBankId(), record.getCardNo(),
                                        record.getCardHolder(), record.getSettle(),
                                        record.getBankId(), record.getSettleTime(), record.getBatchNo(),
                                        record.getTraceNo(), record.getInvoiceNo());
                                record_data.add(recordData[i]);
                                i++;
                            }
                        } //end for loop
                        Double total = parseDouble((xml2Record.gettotalPage()));
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
                    activity.noResFound(getString(R.string.noResFound));
                }
            }

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void networkError() {
        lvCommonListView.setVisibility(ListView.GONE);
        linearResult.setVisibility(View.GONE);
        loadingPanel.setVisibility(RelativeLayout.GONE);
        tvnone.setVisibility(TextView.VISIBLE);
        tvnone.setText(R.string.network_error);
        listfooter.setVisibility(RelativeLayout.GONE);
    }

    public void noResFound(String error) {
        lvCommonListView.setVisibility(ListView.GONE);
        linearResult.setVisibility(View.GONE);
        loadingPanel.setVisibility(RelativeLayout.GONE);
        tvnone.setVisibility(TextView.VISIBLE);
        if (!error.equals("")) {
            tvnone.setText(error);
        } else {
            tvnone.setText(R.string.noResFound);
        }
        listfooter.setVisibility(RelativeLayout.GONE);
    }

    public void loading() {

        loadingPanel.setVisibility(View.VISIBLE);
        linearResult.setVisibility(View.GONE);
        lvCommonListView.setVisibility(View.GONE);
        listfooter.setVisibility(View.GONE);
        tvnone.setVisibility(View.GONE);
    }

    public void resetRes() {
        linearResult.setVisibility(View.VISIBLE);
        lvCommonListView.setVisibility(ListView.VISIBLE);
        loadingPanel.setVisibility(RelativeLayout.GONE);
        tvnone.setVisibility(TextView.GONE);
        listfooter.setVisibility(View.VISIBLE);
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
                //pageNo = 1;
                String strpageno = Integer.toString(pageNo);
                LoadXML loadxml = new LoadXML(History_List.this, progressDialog, GlobalFunction.getPrefPayGate(History_List.this));
                loadxml.execute(date1, date2, strpageno);
            }

            if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    //--Edited 25/07/18 by KJ--//
    @Override
    protected void onPause() {
        /*activity不可见的时候取消线程*/
        //stop check timeout thread when activity no show
//        CheckTimeOutHandler.removeCallbacks(checkTimeOutTask);
        super.onPause();
        Constants.CURRENT_PAGE_NO = 1;
    }
    //--done Edited 25/07/18 by KJ--//
    //-------------------------------------auto logout-------------------------------------//

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (GlobalFunction.onKeyDownEvent(History_List.this, keyCode, event, getPackageManager())) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}


