package com.example.dell.smartpos.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.CurrCode;
import com.example.dell.smartpos.R;

import java.text.DecimalFormat;

public class EMV_ContactlessTxnLimit extends AppCompatActivity {

    private static final int INPUT_LIMIT_REQUEST_CODE = 0;
    String currCode = "";
    String currCode1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_contactless_txn_limit);
        this.setTitle(R.string.contactless_limit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout visa_layout = (LinearLayout) findViewById(R.id.visa_layout);
        final LinearLayout visa_floor_limit = (LinearLayout) findViewById(R.id.visa_floor_limit);
        final LinearLayout visa_cvm_limit = (LinearLayout) findViewById(R.id.visa_cvm_limit);
        final LinearLayout visa_txn_limit = (LinearLayout) findViewById(R.id.visa_txn_limit);
        LinearLayout mc_layout = (LinearLayout) findViewById(R.id.mc_layout);
        final LinearLayout mc_floor_limit = (LinearLayout) findViewById(R.id.mc_floor_limit);
        final LinearLayout mc_cvm_limit = (LinearLayout) findViewById(R.id.mc_cvm_limit);
        final LinearLayout mc_txn_limit = (LinearLayout) findViewById(R.id.mc_txn_limit);
        LinearLayout chinaUnionPay_layout = (LinearLayout) findViewById(R.id.chinaUnionPay_layout);
        final LinearLayout cup_floor_limit = (LinearLayout) findViewById(R.id.cup_floor_limit);
        final LinearLayout cup_cvm_limit = (LinearLayout) findViewById(R.id.cup_cvm_limit);
        final LinearLayout cup_txn_limit = (LinearLayout) findViewById(R.id.cup_txn_limit);
        LinearLayout amex_layout = (LinearLayout) findViewById(R.id.amex_layout);
        final LinearLayout amex_floor_limit = (LinearLayout) findViewById(R.id.amex_floor_limit);
        final LinearLayout amex_cvm_limit = (LinearLayout) findViewById(R.id.amex_cvm_limit);
        final LinearLayout amex_txn_limit = (LinearLayout) findViewById(R.id.amex_txn_limit);
        LinearLayout jcb_layout = (LinearLayout) findViewById(R.id.jcb_layout);
        final LinearLayout jcb_floor_limit = (LinearLayout) findViewById(R.id.jcb_floor_limit);
        final LinearLayout jcb_cvm_limit = (LinearLayout) findViewById(R.id.jcb_cvm_limit);
        final LinearLayout jcb_txn_limit = (LinearLayout) findViewById(R.id.jcb_txn_limit);
        LinearLayout diners_layout = (LinearLayout) findViewById(R.id.diners_layout);
        final LinearLayout diners_floor_limit = (LinearLayout) findViewById(R.id.diners_floor_limit);
        final LinearLayout diners_cvm_limit = (LinearLayout) findViewById(R.id.diners_cvm_limit);
        final LinearLayout diners_txn_limit = (LinearLayout) findViewById(R.id.diners_txn_limit);

        final TextView visa_floor_limit_amount = (TextView) findViewById(R.id.visa_floor_limit_amount);
        final TextView visa_cvm_limit_amount = (TextView) findViewById(R.id.visa_cvm_limit_amount);
        final TextView visa_txn_limit_amount = (TextView) findViewById(R.id.visa_txn_limit_amount);
        final TextView mc_floor_limit_amount = (TextView) findViewById(R.id.mc_floor_limit_amount);
        final TextView mc_cvm_limit_amount = (TextView) findViewById(R.id.mc_cvm_limit_amount);
        final TextView mc_txn_limit_amount = (TextView) findViewById(R.id.mc_txn_limit_amount);
        final TextView cup_floor_limit_amount = (TextView) findViewById(R.id.cup_floor_limit_amount);
        final TextView cup_cvm_limit_amount = (TextView) findViewById(R.id.cup_cvm_limit_amount);
        final TextView cup_txn_limit_amount = (TextView) findViewById(R.id.cup_txn_limit_amount);
        final TextView amex_floor_limit_amount = (TextView) findViewById(R.id.amex_floor_limit_amount);
        final TextView amex_cvm_limit_amount = (TextView) findViewById(R.id.amex_cvm_limit_amount);
        final TextView amex_txn_limit_amount = (TextView) findViewById(R.id.amex_txn_limit_amount);
        final TextView jcb_floor_limit_amount = (TextView) findViewById(R.id.jcb_floor_limit_amount);
        final TextView jcb_cvm_limit_amount = (TextView) findViewById(R.id.jcb_cvm_limit_amount);
        final TextView jcb_txn_limit_amount = (TextView) findViewById(R.id.jcb_txn_limit_amount);
        final TextView diners_floor_limit_amount = (TextView) findViewById(R.id.diners_floor_limit_amount);
        final TextView diners_cvm_limit_amount = (TextView) findViewById(R.id.diners_cvm_limit_amount);
        final TextView diners_txn_limit_amount = (TextView) findViewById(R.id.diners_txn_limit_amount);

        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

        currCode1 = getIntent().getStringExtra(Constants.CURRCODE);
        currCode = CurrCode.getName(currCode1);

        visa_floor_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.visa_floor_limit, Constants.default_visa_floor_limit))));
        visa_cvm_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.visa_cvm_limit, Constants.default_visa_cvm_limit))));
        visa_txn_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.visa_transaction_limit, Constants.default_visa_transaction_limit))));
        mc_floor_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.mc_floor_limit, Constants.default_mc_floor_limit))));
        mc_cvm_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.mc_cvm_limit, Constants.default_mc_cvm_limit))));
        mc_txn_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.mc_transaction_limit, Constants.default_mc_transaction_limit))));
        cup_floor_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.cup_floor_limit, Constants.default_cup_floor_limit))));
        cup_cvm_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.cup_cvm_limit, Constants.default_cup_cvm_limit))));
        cup_txn_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.cup_transaction_limit, Constants.default_cup_transaction_limit))));
        amex_floor_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.amex_floor_limit, Constants.default_amex_floor_limit))));
        amex_cvm_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.amex_cvm_limit, Constants.default_amex_cvm_limit))));
        amex_txn_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.amex_transaction_limit, Constants.default_amex_transaction_limit))));
        jcb_floor_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.jcb_floor_limit, Constants.default_jcb_floor_limit))));
        jcb_cvm_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.jcb_cvm_limit, Constants.default_jcb_cvm_limit))));
        jcb_txn_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.jcb_transaction_limit, Constants.default_jcb_transaction_limit))));
        diners_floor_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.diners_floor_limit, Constants.default_diners_floor_limit))));
        diners_cvm_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.diners_cvm_limit, Constants.default_diners_cvm_limit))));
        diners_txn_limit_amount.setText(currCode + " " + formatter.format(Double.parseDouble(prefsettings.getString(Constants.diners_transaction_limit, Constants.default_diners_transaction_limit))));


        visa_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (visa_floor_limit.getVisibility() == View.GONE) {
                    setVisaVisibility("VISIBLE");
                    setMCVisibility("GONE");
                    setCUPVisibility("GONE");
                    setAMEXVisibility("GONE");
                    setJCBVisibility("GONE");
                    setDinersVisibility("GONE");
                } else if (visa_floor_limit.getVisibility() == View.VISIBLE) {
                    setVisaVisibility("GONE");
                }
            }
        });

        mc_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mc_floor_limit.getVisibility() == View.GONE) {
                    setVisaVisibility("GONE");
                    setMCVisibility("VISIBLE");
                    setCUPVisibility("GONE");
                    setAMEXVisibility("GONE");
                    setJCBVisibility("GONE");
                    setDinersVisibility("GONE");

                } else if (mc_floor_limit.getVisibility() == View.VISIBLE) {
                    setMCVisibility("GONE");
                }

            }
        });

        chinaUnionPay_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (cup_floor_limit.getVisibility() == View.GONE) {
                    setVisaVisibility("GONE");
                    setMCVisibility("GONE");
                    setCUPVisibility("VISIBLE");
                    setAMEXVisibility("GONE");
                    setJCBVisibility("GONE");
                    setDinersVisibility("GONE");

                } else if (cup_floor_limit.getVisibility() == View.VISIBLE) {
                    setCUPVisibility("GONE");
                }

            }
        });

        amex_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (amex_floor_limit.getVisibility() == View.GONE) {
                    setVisaVisibility("GONE");
                    setMCVisibility("GONE");
                    setCUPVisibility("GONE");
                    setAMEXVisibility("VISIBLE");
                    setJCBVisibility("GONE");
                    setDinersVisibility("GONE");
                } else if (amex_floor_limit.getVisibility() == View.VISIBLE) {
                    setAMEXVisibility("GONE");
                }

            }
        });

        jcb_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (jcb_floor_limit.getVisibility() == View.GONE) {
                    setVisaVisibility("GONE");
                    setMCVisibility("GONE");
                    setCUPVisibility("GONE");
                    setAMEXVisibility("GONE");
                    setJCBVisibility("VISIBLE");
                    setDinersVisibility("GONE");
                } else if (jcb_floor_limit.getVisibility() == View.VISIBLE) {
                    setJCBVisibility("GONE");
                }

            }
        });

        diners_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (diners_floor_limit.getVisibility() == View.GONE) {
                    setVisaVisibility("GONE");
                    setMCVisibility("GONE");
                    setCUPVisibility("GONE");
                    setAMEXVisibility("GONE");
                    setJCBVisibility("GONE");
                    setDinersVisibility("VISIBLE");
                } else if (diners_floor_limit.getVisibility() == View.VISIBLE) {
                    setDinersVisibility("GONE");
                }
            }
        });

        visa_floor_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "Visa");
                intent.putExtra("limitType", "floor");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
//                startActivity(intent);
            }
        });

        visa_cvm_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "Visa");
                intent.putExtra("limitType", "cvm");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        visa_txn_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "Visa");
                intent.putExtra("limitType", "txn");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        mc_floor_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "MasterCard");
                intent.putExtra("limitType", "floor");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        mc_cvm_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "MasterCard");
                intent.putExtra("limitType", "cvm");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        mc_txn_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "MasterCard");
                intent.putExtra("limitType", "txn");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        cup_floor_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "China UnionPay");
                intent.putExtra("limitType", "floor");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        cup_cvm_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "China UnionPay");
                intent.putExtra("limitType", "cvm");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        cup_txn_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "China UnionPay");
                intent.putExtra("limitType", "txn");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        amex_floor_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "American Express");
                intent.putExtra("limitType", "floor");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        amex_cvm_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "American Express");
                intent.putExtra("limitType", "cvm");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        amex_txn_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "American Express");
                intent.putExtra("limitType", "txn");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        jcb_floor_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "JCB");
                intent.putExtra("limitType", "floor");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        jcb_cvm_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "JCB");
                intent.putExtra("limitType", "cvm");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        jcb_txn_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "JCB");
                intent.putExtra("limitType", "txn");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        diners_floor_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "Diners Club/Discover");
                intent.putExtra("limitType", "floor");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        diners_cvm_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "Diners Club/Discover");
                intent.putExtra("limitType", "cvm");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });

        diners_txn_limit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ContactlessTxnLimit.this, EMV_InputLimit.class);
                intent.putExtra("cardScheme", "Diners Club/Discover");
                intent.putExtra("limitType", "txn");
                startActivityForResult(intent, INPUT_LIMIT_REQUEST_CODE);
            }
        });


    }

    private void setVisaVisibility(String visible) {

        if (visible.equalsIgnoreCase("gone")) {
            findViewById(R.id.visa_floor_limit).setVisibility(View.GONE);
            findViewById(R.id.visa_cvm_limit).setVisibility(View.GONE);
            findViewById(R.id.visa_txn_limit).setVisibility(View.GONE);
            findViewById(R.id.visa_arrow).setRotation(0);
        } else if (visible.equalsIgnoreCase("visible")) {
            findViewById(R.id.visa_floor_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.visa_cvm_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.visa_txn_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.visa_arrow).setRotation(90);
        }
    }

    private void setMCVisibility(String visible) {

        if (visible.equalsIgnoreCase("gone")) {
            findViewById(R.id.mc_floor_limit).setVisibility(View.GONE);
            findViewById(R.id.mc_cvm_limit).setVisibility(View.GONE);
            findViewById(R.id.mc_txn_limit).setVisibility(View.GONE);
            findViewById(R.id.mc_arrow).setRotation(0);
        } else if (visible.equalsIgnoreCase("visible")) {
            findViewById(R.id.mc_floor_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.mc_cvm_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.mc_txn_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.mc_arrow).setRotation(90);
        }
    }

    private void setCUPVisibility(String visible) {

        if (visible.equalsIgnoreCase("gone")) {
            findViewById(R.id.cup_floor_limit).setVisibility(View.GONE);
            findViewById(R.id.cup_cvm_limit).setVisibility(View.GONE);
            findViewById(R.id.cup_txn_limit).setVisibility(View.GONE);
            findViewById(R.id.cup_arrow).setRotation(0);
        } else if (visible.equalsIgnoreCase("visible")) {
            findViewById(R.id.cup_floor_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.cup_cvm_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.cup_txn_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.cup_arrow).setRotation(90);
        }
    }

    private void setAMEXVisibility(String visible) {

        if (visible.equalsIgnoreCase("gone")) {
            findViewById(R.id.amex_floor_limit).setVisibility(View.GONE);
            findViewById(R.id.amex_cvm_limit).setVisibility(View.GONE);
            findViewById(R.id.amex_txn_limit).setVisibility(View.GONE);
            findViewById(R.id.amex_arrow).setRotation(0);
        } else if (visible.equalsIgnoreCase("visible")) {
            findViewById(R.id.amex_floor_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.amex_cvm_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.amex_txn_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.amex_arrow).setRotation(90);
        }
    }

    private void setJCBVisibility(String visible) {

        if (visible.equalsIgnoreCase("gone")) {
            findViewById(R.id.jcb_floor_limit).setVisibility(View.GONE);
            findViewById(R.id.jcb_cvm_limit).setVisibility(View.GONE);
            findViewById(R.id.jcb_txn_limit).setVisibility(View.GONE);
            findViewById(R.id.jcb_arrow).setRotation(0);
        } else if (visible.equalsIgnoreCase("visible")) {
            findViewById(R.id.jcb_floor_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.jcb_cvm_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.jcb_txn_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.jcb_arrow).setRotation(90);
        }
    }

    private void setDinersVisibility(String visible) {

        if (visible.equalsIgnoreCase("gone")) {
            findViewById(R.id.diners_floor_limit).setVisibility(View.GONE);
            findViewById(R.id.diners_cvm_limit).setVisibility(View.GONE);
            findViewById(R.id.diners_txn_limit).setVisibility(View.GONE);
            findViewById(R.id.diners_arrow).setRotation(0);
        } else if (visible.equalsIgnoreCase("visible")) {
            findViewById(R.id.diners_floor_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.diners_cvm_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.diners_txn_limit).setVisibility(View.VISIBLE);
            findViewById(R.id.diners_arrow).setRotation(90);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (INPUT_LIMIT_REQUEST_CODE): {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    boolean reset = data.getBooleanExtra("reset", false);

                    final TextView visa_floor_limit_amount = (TextView) findViewById(R.id.visa_floor_limit_amount);
                    final TextView visa_cvm_limit_amount = (TextView) findViewById(R.id.visa_cvm_limit_amount);
                    final TextView visa_txn_limit_amount = (TextView) findViewById(R.id.visa_txn_limit_amount);
                    final TextView mc_floor_limit_amount = (TextView) findViewById(R.id.mc_floor_limit_amount);
                    final TextView mc_cvm_limit_amount = (TextView) findViewById(R.id.mc_cvm_limit_amount);
                    final TextView mc_txn_limit_amount = (TextView) findViewById(R.id.mc_txn_limit_amount);
                    final TextView cup_floor_limit_amount = (TextView) findViewById(R.id.cup_floor_limit_amount);
                    final TextView cup_cvm_limit_amount = (TextView) findViewById(R.id.cup_cvm_limit_amount);
                    final TextView cup_txn_limit_amount = (TextView) findViewById(R.id.cup_txn_limit_amount);
                    final TextView amex_floor_limit_amount = (TextView) findViewById(R.id.amex_floor_limit_amount);
                    final TextView amex_cvm_limit_amount = (TextView) findViewById(R.id.amex_cvm_limit_amount);
                    final TextView amex_txn_limit_amount = (TextView) findViewById(R.id.amex_txn_limit_amount);
                    final TextView jcb_floor_limit_amount = (TextView) findViewById(R.id.jcb_floor_limit_amount);
                    final TextView jcb_cvm_limit_amount = (TextView) findViewById(R.id.jcb_cvm_limit_amount);
                    final TextView jcb_txn_limit_amount = (TextView) findViewById(R.id.jcb_txn_limit_amount);
                    final TextView diners_floor_limit_amount = (TextView) findViewById(R.id.diners_floor_limit_amount);
                    final TextView diners_cvm_limit_amount = (TextView) findViewById(R.id.diners_cvm_limit_amount);
                    final TextView diners_txn_limit_amount = (TextView) findViewById(R.id.diners_txn_limit_amount);

                    DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
                    SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);


                    if (reset) {
                        visa_floor_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.visa_floor_limit, Constants.default_visa_floor_limit))));
                        visa_cvm_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.visa_cvm_limit, Constants.default_visa_cvm_limit))));
                        visa_txn_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.visa_transaction_limit, Constants.default_visa_transaction_limit))));
                        mc_floor_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.mc_floor_limit, Constants.default_mc_floor_limit))));
                        mc_cvm_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.mc_cvm_limit, Constants.default_mc_cvm_limit))));
                        mc_txn_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.mc_transaction_limit, Constants.default_mc_transaction_limit))));
                        cup_floor_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.cup_floor_limit, Constants.default_cup_floor_limit))));
                        cup_cvm_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.cup_cvm_limit, Constants.default_cup_cvm_limit))));
                        cup_txn_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.cup_transaction_limit, Constants.default_cup_transaction_limit))));
                        amex_floor_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.amex_floor_limit, Constants.default_amex_floor_limit))));
                        amex_cvm_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.amex_cvm_limit, Constants.default_amex_cvm_limit))));
                        amex_txn_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.amex_transaction_limit, Constants.default_amex_transaction_limit))));
                        jcb_floor_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.jcb_floor_limit, Constants.default_jcb_floor_limit))));
                        jcb_cvm_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.jcb_cvm_limit, Constants.default_jcb_cvm_limit))));
                        jcb_txn_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.jcb_transaction_limit, Constants.default_jcb_transaction_limit))));
                        diners_floor_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.diners_floor_limit, Constants.default_diners_floor_limit))));
                        diners_cvm_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.diners_cvm_limit, Constants.default_diners_cvm_limit))));
                        diners_txn_limit_amount.setText(formatter.format(Double.parseDouble(prefsettings.getString(Constants.diners_transaction_limit, Constants.default_diners_transaction_limit))));

                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
