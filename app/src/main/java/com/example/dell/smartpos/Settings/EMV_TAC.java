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

public class EMV_TAC extends AppCompatActivity {

    private Button btnOkTAC;
    private CheckBox cbDenial;
    private CheckBox cbOnline;
    private CheckBox cbDefault;

    private TextView label_title;
    private TextView label_type;

    private static String EMV_type = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_tac);
        this.setTitle(R.string.tac);
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

        cbDenial = (CheckBox) findViewById(R.id.cbDenial);
        cbOnline = (CheckBox) findViewById(R.id.cbOnline);
        cbDefault = (CheckBox) findViewById(R.id.cbDefault);

        btnOkTAC = (Button) findViewById(R.id.btnOkTAC);

        EMV_type += "_tac";

        initView();

        btnOkTAC.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                        Constants.SETTINGS, MODE_PRIVATE);
                SharedPreferences.Editor edit = prefsettings.edit();

                String temp_tac;
                temp_tac = "";

                if (cbDenial.isChecked()) {
                    temp_tac = "-denial-";
                } else {
                    temp_tac = "";
                }

                if (cbOnline.isChecked()) {
                    temp_tac += "-online-";
                } else {
                    temp_tac += "";
                }

                if (cbDefault.isChecked()) {
                    temp_tac += "-default-";
                } else {
                    temp_tac += "";
                }

                edit.putString(EMV_type, temp_tac);
                edit.commit();

                finish();
            }

        });

    }

    private void initView(){
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);
        String avTAC = prefsettings.getString(EMV_type, "").toLowerCase();

        if (avTAC.contains("-denial-")) {
            cbDenial.setChecked(true);
        } else {
            cbDenial.setChecked(false);
        }

        if (avTAC.contains("-online-")) {
            cbOnline.setChecked(true);
        } else {
            cbOnline.setChecked(false);
        }

        if (avTAC.contains("-default-")) {
            cbDefault.setChecked(true);
        } else {
            cbDefault.setChecked(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
