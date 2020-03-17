package com.example.dell.smartpos.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.GlobalFunction;
import com.example.dell.smartpos.PaymentTimeoutSetting;
import com.example.dell.smartpos.R;

public class SettingsTransaction extends AppCompatActivity {

    LinearLayout setting_printMode;
    LinearLayout setting_paymentTimeout;

    String payMethod;
    String cardPayBankId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_transaction);

        setTitle(R.string.settings_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setting_printMode = (LinearLayout) findViewById(R.id.setting_printingMode);
        setting_paymentTimeout = (LinearLayout) findViewById(R.id.setting_paymentTimeout);

        // Initialize settings
        Intent intent = getIntent();
        payMethod = intent.getStringExtra(Constants.PAYMETHODLIST);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String settingMode = pref.getString(Constants.pref_setting_mode, Constants.default_setting_mode);

        if (settingMode.equalsIgnoreCase("mode3")) {
            setting_paymentTimeout.setVisibility(View.GONE);
//            surcharge_calculation.setVisibility(View.VISIBLE);
        }

        cardPayBankId = GlobalFunction.getCardPayBankId(SettingsTransaction.this);

        // Hide Receipt Printing for First-Data
//        if (cardPayBankId.equalsIgnoreCase("First-Data")) {
//            setting_printMode.setVisibility(View.GONE);
//        }

        setting_printMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SettingsTransaction.this, ReceiptPrintingMode.class);
                startActivity(intent);

//                dialog = new AlertDialog.Builder(SettingsTransaction.this);
//                dialog.setTitle(R.string.select_printing_mode);
//                dialog.setItems(printingMode, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int which) {
//                        String printmode = "";
//                        switch (which) {
//                            case 0:
//                                printmode = Constants.PRINT_MODE1;
//                                break;
//                            case 1:
//                                printmode = Constants.PRINT_MODE2;
//                                break;
//                            case 2:
//                                printmode = Constants.PRINT_MODE3;
//                                break;
//                            default:
//                        }
//                        TextView printMode = (TextView) findViewById(R.id.receiptPrinting);
//                        printMode.setText(printingMode[which]);
//                        SharedPreferences pref = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
//                        SharedPreferences.Editor edit = pref.edit();
//                        edit.putString(Constants.pref_PrintMode, printmode);
//                        edit.commit();
////                        restartActivity();
//                    }
//                });
//                alertDialog = dialog.create();
//                alertDialog.show();
            }
        });

        setting_paymentTimeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent timeout = new Intent(SettingsTransaction.this, PaymentTimeoutSetting.class);
                timeout.putExtra(Constants.MERID, getIntent().getStringExtra(Constants.MERID));
                timeout.putExtra(Constants.MERNAME, getIntent().getStringExtra(Constants.MERNAME));
                timeout.putExtra(Constants.PAYMETHODLIST, payMethod);
                startActivity(timeout);
            }
        });
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
