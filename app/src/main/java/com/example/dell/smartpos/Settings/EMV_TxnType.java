package com.example.dell.smartpos.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;

public class EMV_TxnType extends AppCompatActivity {

    private Button btnOkTxnType;
    private CheckBox cbAdjustment;
    private CheckBox cbEMV_TC_Upload;
    private CheckBox cbOfflineSale;
    private CheckBox cbOnlineSale;
    private CheckBox cbPreAuth;
    private CheckBox cbPreAuthCompletion;
    private CheckBox cbRefund;
    private CheckBox cbReversal;
    private CheckBox cbSettlement;
    private CheckBox cbSettlementBatchUpload;
    private CheckBox cbVoid;
    private CheckBox cbVoidPreAuth;
    private CheckBox cbVoidPreAuthCompletion;
    private CheckBox cbVoidRefund;

    private static String EMV_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_txn_type);
        this.setTitle(R.string.transaction_type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent settingIntent = getIntent();
        EMV_type = settingIntent.getStringExtra("EMV_type");
        EMV_type = "txn_type";

        cbAdjustment = (CheckBox) findViewById(R.id.cbAdjustment);
        cbEMV_TC_Upload = (CheckBox) findViewById(R.id.cbEMV_TC_Upload);
        cbOfflineSale = (CheckBox) findViewById(R.id.cbOfflineSale);
        cbOnlineSale = (CheckBox) findViewById(R.id.cbOnlineSale);
        cbPreAuth = (CheckBox) findViewById(R.id.cbPreAuth);
        cbPreAuthCompletion = (CheckBox) findViewById(R.id.cbPreAuthCompletion);
        cbRefund = (CheckBox) findViewById(R.id.cbRefund);
        cbReversal = (CheckBox) findViewById(R.id.cbReversal);
        cbSettlement = (CheckBox) findViewById(R.id.cbSettlement);
        cbSettlementBatchUpload = (CheckBox) findViewById(R.id.cbSettlementBatchUpload);
        cbVoid = (CheckBox) findViewById(R.id.cbVoid);
        cbVoidPreAuth = (CheckBox) findViewById(R.id.cbVoidPreAuth);
        cbVoidPreAuthCompletion = (CheckBox) findViewById(R.id.cbVoidPreAuthCompletion);
        cbVoidRefund = (CheckBox) findViewById(R.id.cbVoidRefund);

        btnOkTxnType = (Button) findViewById(R.id.btnOkTxnType);

        initView();


        btnOkTxnType.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                        Constants.SETTINGS, MODE_PRIVATE);
                SharedPreferences.Editor edit = prefsettings.edit();

                String temp_txn_type;
                temp_txn_type = "";

                if (cbAdjustment.isChecked()) {
                    temp_txn_type = "-adjustment-";
                }

                if (cbEMV_TC_Upload.isChecked()) {
                    temp_txn_type += "-EMVTCUpload-";
                }

                if (cbOfflineSale.isChecked()) {
                    temp_txn_type += "-offSale-";
                }

                if (cbOnlineSale.isChecked()) {
                    temp_txn_type += "-onSale-";
                }

                if (cbPreAuth.isChecked()) {
                    temp_txn_type += "-preAuth-";
                }

                if (cbPreAuthCompletion.isChecked()) {
                    temp_txn_type += "-preAuthComp-";
                }

                if (cbRefund.isChecked()) {
                    temp_txn_type += "-refund-";
                }

                if (cbReversal.isChecked()) {
                    temp_txn_type += "-reversal-";
                }

                if (cbSettlement.isChecked()) {
                    temp_txn_type += "-settlement-";
                }

                if (cbSettlementBatchUpload.isChecked()) {
                    temp_txn_type += "-settlementBatchUp-";
                }


                if (cbVoid.isChecked()) {
                    temp_txn_type += "-void-";
                }

                if (cbVoidPreAuth.isChecked()) {
                    temp_txn_type += "-voidPreAuth-";
                }

                if (cbVoidPreAuthCompletion.isChecked()) {
                    temp_txn_type += "-voidPreAuthComp-";
                }

                if (cbVoidRefund.isChecked()) {
                    temp_txn_type += "-voidRefund-";
                }

                edit.putString(EMV_type, temp_txn_type);
                edit.commit();

                finish();
            }

        });
    }

    private void initView() {
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);



        String avTxntype = prefsettings.getString(EMV_type, "").toLowerCase();

        if (avTxntype.contains("-adjustment-")) {
            cbAdjustment.setChecked(true);
        } else {
            cbAdjustment.setChecked(false);
        }

        if (avTxntype.contains("-emvtcupload-")) {
            cbEMV_TC_Upload.setChecked(true);
        } else {
            cbEMV_TC_Upload.setChecked(false);
        }

        if (avTxntype.contains("-offsale-")) {
            cbOfflineSale.setChecked(true);
        } else {
            cbOfflineSale.setChecked(false);
        }

        if (avTxntype.contains("-onsale-")) {
            cbOnlineSale.setChecked(true);
        } else {
            cbOnlineSale.setChecked(false);
        }

        if (avTxntype.contains("-preauth-")) {
            cbPreAuth.setChecked(true);
        } else {
            cbPreAuth.setChecked(false);
        }

        if (avTxntype.contains("-preauthcomp-")) {
            cbPreAuthCompletion.setChecked(true);
        } else {
            cbPreAuthCompletion.setChecked(false);
        }

        if (avTxntype.contains("-refund-")) {
            cbRefund.setChecked(true);
        } else {
            cbRefund.setChecked(false);
        }

        if (avTxntype.contains("-reversal-")) {
            cbReversal.setChecked(true);
        } else {
            cbReversal.setChecked(false);
        }

        if (avTxntype.contains("-settlement-")) {
            cbSettlement.setChecked(true);
        } else {
            cbSettlement.setChecked(false);
        }

        if (avTxntype.contains("-settlementbatchup-")) {
            cbSettlementBatchUpload.setChecked(true);
        } else {
            cbSettlementBatchUpload.setChecked(false);
        }

        if (avTxntype.contains("-void-")) {
            cbVoid.setChecked(true);
        } else {
            cbVoid.setChecked(false);
        }

        if (avTxntype.contains("-voidpreauth-")) {
            cbVoidPreAuth.setChecked(true);
        } else {
            cbVoidPreAuth.setChecked(false);
        }

        if (avTxntype.contains("-voidpreauthcomp-")) {
            cbVoidPreAuthCompletion.setChecked(true);
        } else {
            cbVoidPreAuthCompletion.setChecked(false);
        }

        if (avTxntype.contains("-voidrefund-")) {
            cbVoidRefund.setChecked(true);
        } else {
            cbVoidRefund.setChecked(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
