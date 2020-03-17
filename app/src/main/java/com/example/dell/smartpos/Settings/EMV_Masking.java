package com.example.dell.smartpos.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;

public class EMV_Masking extends AppCompatActivity {

    private RadioGroup displayFull;
    private RadioGroup unmaskYear;
    private RadioGroup unmaskMonth;
    private Button btnOK;
    private Button addFirstNth;
    private Button minusFirstNth;
    private Button addLastNth;
    private Button minusLastNth;

    private static String dispFull = "";
    private static String unmask_year = "";
    private static String unmask_month = "";
    private static String unmask_first_nth_char = "";
    private static String unmask_last_nth_char = "";
    private static String retrievedData = "";
    private static String setData = "";
    private static String constant = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_masking);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String maskingType = intent.getStringExtra("maskingType");
        final String txnType = intent.getStringExtra("txnType");
        final String cardScheme = intent.getStringExtra("cardScheme");

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);
        final SharedPreferences.Editor edit = prefsettings.edit();

        if (maskingType.equalsIgnoreCase("pan")) {
            setTitle(R.string.pan_masking_rules);

            if (txnType.equalsIgnoreCase("online")) {
                if (cardScheme.equalsIgnoreCase("visa")) {
                    retrievedData = prefsettings.getString(Constants.online_visa_unmask_pan, Constants.default_online_visa_unmask_pan);
                    constant = Constants.online_visa_unmask_pan;
                } else if (cardScheme.equalsIgnoreCase("mc")) {
                    retrievedData = prefsettings.getString(Constants.online_mc_unmask_pan, Constants.default_online_mc_unmask_pan);
                    constant = Constants.online_mc_unmask_pan;
                } else if (cardScheme.equalsIgnoreCase("cup")) {
                    retrievedData = prefsettings.getString(Constants.online_cup_unmask_pan, Constants.default_online_cup_unmask_pan);
                    constant = Constants.online_cup_unmask_pan;
                } else if (cardScheme.equalsIgnoreCase("amex")) {
                    retrievedData = prefsettings.getString(Constants.online_amex_unmask_pan, Constants.default_online_amex_unmask_pan);
                    constant = Constants.online_amex_unmask_pan;
                } else if (cardScheme.equalsIgnoreCase("jcb")) {
                    retrievedData = prefsettings.getString(Constants.online_jcb_unmask_pan, Constants.default_online_jcb_unmask_pan);
                    constant = Constants.online_jcb_unmask_pan;
                } else if (cardScheme.equalsIgnoreCase("diners")) {
                    retrievedData = prefsettings.getString(Constants.online_diners_unmask_pan, Constants.default_online_diners_unmask_pan);
                    constant = Constants.online_diners_unmask_pan;
                }
            } else if (txnType.equalsIgnoreCase("pre_auth")) {
                if (cardScheme.equalsIgnoreCase("visa")) {
                    retrievedData = prefsettings.getString(Constants.pre_auth_visa_unmask_pan, Constants.default_pre_auth_visa_unmask_pan);
                    constant = Constants.pre_auth_visa_unmask_pan;
                } else if (cardScheme.equalsIgnoreCase("mc")) {
                    retrievedData = prefsettings.getString(Constants.pre_auth_mc_unmask_pan, Constants.default_pre_auth_mc_unmask_pan);
                    constant = Constants.pre_auth_mc_unmask_pan;
                } else if (cardScheme.equalsIgnoreCase("cup")) {
                    retrievedData = prefsettings.getString(Constants.pre_auth_cup_unmask_pan, Constants.default_pre_auth_cup_unmask_pan);
                    constant = Constants.pre_auth_cup_unmask_pan;
                } else if (cardScheme.equalsIgnoreCase("amex")) {
                    retrievedData = prefsettings.getString(Constants.pre_auth_amex_unmask_pan, Constants.default_pre_auth_amex_unmask_pan);
                    constant = Constants.pre_auth_amex_unmask_pan;
                } else if (cardScheme.equalsIgnoreCase("jcb")) {
                    retrievedData = prefsettings.getString(Constants.pre_auth_jcb_unmask_pan, Constants.default_pre_auth_jcb_unmask_pan);
                    constant = Constants.pre_auth_jcb_unmask_pan;
                } else if (cardScheme.equalsIgnoreCase("diners")) {
                    retrievedData = prefsettings.getString(Constants.pre_auth_diners_unmask_pan, Constants.default_pre_auth_diners_unmask_pan);
                    constant = Constants.pre_auth_diners_unmask_pan;
                }
            }
        } else if (maskingType.equalsIgnoreCase("expiry")) {
            setTitle(R.string.exp_date_masking_rules);

            if (txnType.equalsIgnoreCase("online")) {
                if (cardScheme.equalsIgnoreCase("visa")) {
                    retrievedData = prefsettings.getString(Constants.online_visa_unmask_expiry, Constants.default_online_visa_unmask_expiry);
                    constant = Constants.online_visa_unmask_expiry;
                } else if (cardScheme.equalsIgnoreCase("mc")) {
                    retrievedData = prefsettings.getString(Constants.online_mc_unmask_expiry, Constants.default_online_mc_unmask_expiry);
                    constant = Constants.online_mc_unmask_expiry;
                } else if (cardScheme.equalsIgnoreCase("cup")) {
                    retrievedData = prefsettings.getString(Constants.online_cup_unmask_expiry, Constants.default_online_cup_unmask_expiry);
                    constant = Constants.online_cup_unmask_expiry;
                } else if (cardScheme.equalsIgnoreCase("amex")) {
                    retrievedData = prefsettings.getString(Constants.online_amex_unmask_expiry, Constants.default_online_amex_unmask_expiry);
                    constant = Constants.online_amex_unmask_expiry;
                } else if (cardScheme.equalsIgnoreCase("jcb")) {
                    retrievedData = prefsettings.getString(Constants.online_jcb_unmask_expiry, Constants.default_online_jcb_unmask_expiry);
                    constant = Constants.online_jcb_unmask_expiry;
                } else if (cardScheme.equalsIgnoreCase("diners")) {
                    retrievedData = prefsettings.getString(Constants.online_diners_unmask_expiry, Constants.default_online_diners_unmask_expiry);
                    constant = Constants.online_diners_unmask_expiry;
                }
            } else if (txnType.equalsIgnoreCase("pre_auth")) {
                if (cardScheme.equalsIgnoreCase("visa")) {
                    retrievedData = prefsettings.getString(Constants.pre_auth_visa_unmask_expiry, Constants.default_pre_auth_visa_unmask_expiry);
                    constant = Constants.pre_auth_visa_unmask_expiry;
                } else if (cardScheme.equalsIgnoreCase("mc")) {
                    retrievedData = prefsettings.getString(Constants.pre_auth_mc_unmask_expiry, Constants.default_pre_auth_mc_unmask_expiry);
                    constant = Constants.pre_auth_mc_unmask_expiry;
                } else if (cardScheme.equalsIgnoreCase("cup")) {
                    retrievedData = prefsettings.getString(Constants.pre_auth_cup_unmask_expiry, Constants.default_pre_auth_cup_unmask_expiry);
                    constant = Constants.pre_auth_cup_unmask_expiry;
                } else if (cardScheme.equalsIgnoreCase("amex")) {
                    retrievedData = prefsettings.getString(Constants.pre_auth_amex_unmask_expiry, Constants.default_pre_auth_amex_unmask_expiry);
                    constant = Constants.pre_auth_amex_unmask_expiry;
                } else if (cardScheme.equalsIgnoreCase("jcb")) {
                    retrievedData = prefsettings.getString(Constants.pre_auth_jcb_unmask_expiry, Constants.default_pre_auth_jcb_unmask_expiry);
                    constant = Constants.pre_auth_jcb_unmask_expiry;
                } else if (cardScheme.equalsIgnoreCase("diners")) {
                    retrievedData = prefsettings.getString(Constants.pre_auth_diners_unmask_expiry, Constants.default_pre_auth_diners_unmask_expiry);
                    constant = Constants.pre_auth_diners_unmask_expiry;
                }
            }
        }

        final LinearLayout unmask_expiry = (LinearLayout) findViewById(R.id.unmask_expiry);
        final LinearLayout unmask_pan = (LinearLayout) findViewById(R.id.unmask_pan);
        final TextView firstNthChar = (TextView) findViewById(R.id.firstNthChar);
        final TextView lastNthChar = (TextView) findViewById(R.id.lastNthChar);
        displayFull = (RadioGroup) findViewById(R.id.DisplayFullChoice);
        unmaskMonth = (RadioGroup) findViewById(R.id.unmaskMonth);
        unmaskYear = (RadioGroup) findViewById(R.id.unmaskYear);
        addFirstNth = (Button) findViewById(R.id.addFirstNth);
        minusFirstNth = (Button) findViewById(R.id.minusFirstNth);
        addLastNth = (Button) findViewById(R.id.addLastNth);
        minusLastNth = (Button) findViewById(R.id.minusLastNth);
        btnOK = (Button) findViewById(R.id.btnOK);
        final RadioButton month_yes = (RadioButton) findViewById(R.id.month_yes);
        final RadioButton month_no = (RadioButton) findViewById(R.id.month_no);
        final RadioButton year_yes = (RadioButton) findViewById(R.id.year_yes);
        final RadioButton year_no = (RadioButton) findViewById(R.id.year_no);
        final RadioButton yes = (RadioButton) findViewById(R.id.yes);
        final RadioButton no = (RadioButton) findViewById(R.id.no);

        if (retrievedData.split("_")[0].equalsIgnoreCase("1")) {
            displayFull.check(R.id.no);
            if (maskingType.equalsIgnoreCase("pan")) {
                unmask_pan.setVisibility(View.VISIBLE);
                firstNthChar.setText(retrievedData.split("_")[1]);
                lastNthChar.setText(retrievedData.split("_")[2]);
            } else if (maskingType.equalsIgnoreCase("expiry")) {
                unmask_expiry.setVisibility(View.VISIBLE);

                if (retrievedData.split("_")[1].equalsIgnoreCase("1")) {
                    unmaskYear.check(R.id.year_yes);
                } else if (retrievedData.split("_")[1].equalsIgnoreCase("0")) {
                    unmaskYear.check(R.id.year_no);
                }

                if (retrievedData.split("_")[2].equalsIgnoreCase("1")) {
                    unmaskMonth.check(R.id.month_yes);
                } else if (retrievedData.split("_")[2].equalsIgnoreCase("0")) {
                    unmaskMonth.check(R.id.month_no);
                }
            }
        } else {
            displayFull.check(R.id.yes);
            unmaskYear.check(R.id.year_no);
            unmaskMonth.check(R.id.month_no);
            unmask_pan.setVisibility(View.GONE);
            unmask_expiry.setVisibility(View.GONE);
        }

        displayFull.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yes:
                        if (maskingType.equalsIgnoreCase("pan")) {
                            unmask_pan.setVisibility(View.GONE);
                        } else if (maskingType.equalsIgnoreCase("expiry")) {
                            unmask_expiry.setVisibility(View.GONE);
                        }
                        dispFull = "0";
                        break;
                    case R.id.no:
                        if (maskingType.equalsIgnoreCase("pan")) {
                            unmask_pan.setVisibility(View.VISIBLE);
                        } else if (maskingType.equalsIgnoreCase("expiry")) {
                            unmask_expiry.setVisibility(View.VISIBLE);
                        }
                        dispFull = "0";
                        break;
                }
            }
        });

        unmaskMonth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.month_yes:
                        unmask_month = "1";
                        break;
                    case R.id.month_no:
                        unmask_month = "0";
                        break;
                }
            }
        });

        unmaskYear.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.year_yes:
                        unmask_year = "1";
                        break;
                    case R.id.year_no:
                        unmask_year = "0";
                        break;
                }
            }
        });

        addFirstNth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String qtyString = firstNthChar.getText().toString();
                int qty = Integer.parseInt(qtyString) + 1;

                if (qtyString.equals("5")) {
                    firstNthChar.setText(Integer.toString(5));
                } else {
                    firstNthChar.setText(Integer.toString(qty));
                }
            }

        });

        minusFirstNth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String qtyString = firstNthChar.getText().toString();
                int qty = Integer.parseInt(qtyString) - 1;

                if (qtyString.equals("0")) {
                    firstNthChar.setText(Integer.toString(0));
                } else {
                    firstNthChar.setText(Integer.toString(qty));
                }

            }

        });

        addLastNth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String qtyString = lastNthChar.getText().toString();
                int qty = Integer.parseInt(qtyString) + 1;

                if (qtyString.equals("5")) {
                    lastNthChar.setText(Integer.toString(5));
                } else {
                    lastNthChar.setText(Integer.toString(qty));
                }
            }

        });

        minusLastNth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String qtyString = lastNthChar.getText().toString();
                int qty = Integer.parseInt(qtyString) - 1;

                if (qtyString.equals("0")) {
                    lastNthChar.setText(Integer.toString(0));
                } else {
                    lastNthChar.setText(Integer.toString(qty));
                }

            }

        });

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (no.isChecked()) {
                    dispFull = "1";
                }

                if (yes.isChecked()){
                    dispFull = "0";
                }

                if (month_no.isChecked()) {
                    unmask_month = "0";
                }

                if (month_yes.isChecked()){
                    unmask_month = "1";
                }

                if (year_no.isChecked()) {
                    unmask_year = "0";
                }

                if (year_yes.isChecked()){
                    unmask_year = "1";
                }

                if (maskingType.equalsIgnoreCase("pan")) {
                    if (dispFull.equalsIgnoreCase("0")) {
                        setData = "0_0_0";
                    } else if (dispFull.equalsIgnoreCase("1")) {
                        setData = "1_" + firstNthChar.getText().toString() + "_" + lastNthChar.getText().toString();
                    }
                } else if (maskingType.equalsIgnoreCase("expiry")) {
                    if (dispFull.equalsIgnoreCase("0")) {
                        setData = "0_0_0";
                    } else if (dispFull.equalsIgnoreCase("1")) {
                        setData = "1_" + unmask_year + "_" + unmask_month;
                    }

                }

                edit.putString(constant, setData);
                edit.apply();

                finish();
            }

        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
