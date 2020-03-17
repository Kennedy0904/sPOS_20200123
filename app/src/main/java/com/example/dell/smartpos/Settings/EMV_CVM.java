package com.example.dell.smartpos.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;

public class EMV_CVM extends AppCompatActivity {

    private Button btnOkCVM;
    private CheckBox cbOnlinePIN;
    private CheckBox cbOfflinePIN;
    private CheckBox cbSign;
    private CheckBox cbNoCVM;

    private TextView label_title;
    private TextView label_type;

    private static String EMV_type = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_cvm);
        this.setTitle(R.string.cvm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        EMV_type = intent.getStringExtra("EMV_type");

        label_title = (TextView) findViewById(R.id.label_title);
        label_type = (TextView) findViewById(R.id.label_type);

        String temp = getString(R.string.card_scheme) + ":";
        label_title.setText(temp);

        if (EMV_type.equalsIgnoreCase("visa")) {
            label_type.setText(R.string.visa);
        } else if (EMV_type.equalsIgnoreCase("master")) {
            label_type.setText(R.string.MasterCard);
        } else if (EMV_type.equalsIgnoreCase("cup")) {
            label_type.setText(R.string.china_unionpay);
        } else if (EMV_type.equalsIgnoreCase("amex")) {
            label_type.setText(R.string.american_express);
        } else if (EMV_type.equalsIgnoreCase("jcb")) {
            label_type.setText(R.string.jcb);
        } else if (EMV_type.equalsIgnoreCase("diners")) {
            label_type.setText(R.string.diners_club);
        }

        cbOnlinePIN = (CheckBox) findViewById(R.id.cbOnlinePIN);
        cbOfflinePIN = (CheckBox) findViewById(R.id.cbOfflinePIN);
        cbSign = (CheckBox) findViewById(R.id.cbSign);
        cbNoCVM = (CheckBox) findViewById(R.id.cbNoCVM);

        btnOkCVM = (Button) findViewById(R.id.btnOkCVM);

        EMV_type += "_cvm";

        initView();

        btnOkCVM.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                        Constants.SETTINGS, MODE_PRIVATE);
                SharedPreferences.Editor edit = prefsettings.edit();

                String temp_cvm = "";

                if (cbOnlinePIN.isChecked()) {
                    temp_cvm = "-onlinepin-";
                } else {
                    temp_cvm = "";
                }

                if (cbOfflinePIN.isChecked()) {
                    temp_cvm += "-offlinepin-";
                } else {
                    temp_cvm += "";
                }

                if (cbSign.isChecked()) {
                    temp_cvm += "-sign-";
                } else {
                    temp_cvm += "";
                }

                if (cbNoCVM.isChecked()) {
                    temp_cvm += "-nocvm-";
                } else {
                    temp_cvm += "";
                }

                edit.putString(EMV_type, temp_cvm);
                edit.commit();

                finish();
            }

        });
    }

    private void initView() {
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);
        String avTAC = prefsettings.getString(EMV_type, "").toLowerCase();

        if (avTAC.contains("-onlinepin-")) {
            cbOnlinePIN.setChecked(true);
        } else {
            cbOnlinePIN.setChecked(false);
        }

        if (avTAC.contains("-offlinepin-")) {
            cbOfflinePIN.setChecked(true);
        } else {
            cbOfflinePIN.setChecked(false);
        }

        if (avTAC.contains("-sign-")) {
            cbSign.setChecked(true);
        } else {
            cbSign.setChecked(false);
        }

        if (avTAC.contains("-nocvm-")) {
            cbNoCVM.setChecked(true);
        } else {
            cbNoCVM.setChecked(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
