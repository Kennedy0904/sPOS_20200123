package com.example.dell.smartpos.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.GlobalFunction;
import com.example.dell.smartpos.R;

public class ReceiptPrintingMode extends AppCompatActivity {
    private LinearLayout txnUnsuccessfulLayout;
    private RadioGroup PrintModeGroup;
    private Button btnOK;
    private Switch cbPrintUnsuccessReceiptQR, cbPrintUnsuccessReceiptCard;
    private static String printmode = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_printing_mode);
        setTitle(R.string.receipt_printing);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txnUnsuccessfulLayout = (LinearLayout) findViewById(R.id.txnUnsuccessfulLayout);
        cbPrintUnsuccessReceiptQR = (Switch) findViewById(R.id.cbPrintUnsuccessReceiptQR);
        cbPrintUnsuccessReceiptCard = (Switch) findViewById(R.id.cbPrintUnsuccessReceiptCard);
        PrintModeGroup = (RadioGroup) findViewById(R.id.PrintModeGroup);
        btnOK = (Button) findViewById(R.id.btnOK);

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);
        Boolean print_unsuccess_receiptQR = prefsettings.getBoolean(Constants.pref_control_printUnsuccessfulReceiptQR, false);
        Boolean print_unsuccess_receiptCard = prefsettings.getBoolean(Constants.pref_control_printUnsuccessfulReceiptCard, false);

        if (GlobalFunction.getCardPayBankId(ReceiptPrintingMode.this).equalsIgnoreCase("First" +
                "-Data")) {
            txnUnsuccessfulLayout.setVisibility(View.INVISIBLE);
        }

        if (print_unsuccess_receiptQR) {
            cbPrintUnsuccessReceiptQR.setChecked(true);
        } else {
            cbPrintUnsuccessReceiptQR.setChecked(false);
        }

        if (print_unsuccess_receiptCard) {
            cbPrintUnsuccessReceiptCard.setChecked(true);
        } else {
            cbPrintUnsuccessReceiptCard.setChecked(false);
        }

        printmode = prefsettings.getString(Constants.pref_PrintMode, Constants.default_printmode);

        if (printmode.equals(Constants.PRINT_MODE1)) {
            PrintModeGroup.check(R.id.mode1);
        } else if (printmode.equals(Constants.PRINT_MODE2)) {
            PrintModeGroup.check(R.id.mode2);
        } else {
            PrintModeGroup.check(R.id.mode3);
        }

        PrintModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mode1:
                        printmode = Constants.PRINT_MODE1;
                        break;
                    case R.id.mode2:
                        printmode = Constants.PRINT_MODE2;
                        break;
                    case R.id.mode3:
                        printmode = Constants.PRINT_MODE3;
                        break;
                }
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                        Constants.SETTINGS, MODE_PRIVATE);
                SharedPreferences.Editor edit = prefsettings.edit();

                edit.putString(Constants.pref_PrintMode, printmode);

                if (cbPrintUnsuccessReceiptQR.isChecked()) {
                    edit.putBoolean(Constants.pref_control_printUnsuccessfulReceiptQR, true);
                } else {
                    edit.putBoolean(Constants.pref_control_printUnsuccessfulReceiptQR, false);
                }

                if (cbPrintUnsuccessReceiptCard.isChecked()) {
                    edit.putBoolean(Constants.pref_control_printUnsuccessfulReceiptCard, true);
                } else {
                    edit.putBoolean(Constants.pref_control_printUnsuccessfulReceiptCard, false);
                }

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
