package com.example.dell.smartpos.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;

public class EMV_TransactionType extends AppCompatActivity {

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

    private TextView label_title;
    private TextView label_type;

    private static String EMV_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_transaction_type);
        this.setTitle(R.string.transaction_type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        EMV_type = intent.getStringExtra("EMV_type");


        label_title = (TextView) findViewById(R.id.label_title);
        label_type = (TextView) findViewById(R.id.label_type);

        if (title.equalsIgnoreCase("cardScheme")) {
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

        } else if (title.equalsIgnoreCase("entryMode")) {
            String temp = getString(R.string.entry_mode) + ":";
            label_title.setText(temp);

            if (EMV_type.equalsIgnoreCase("chip")) {
                label_type.setText(R.string.chip);
            } else if (EMV_type.equalsIgnoreCase("swipe")) {
                label_type.setText(R.string.entry_swipe);
            } else if (EMV_type.equalsIgnoreCase("manual_key_in")) {
                label_type.setText(R.string.manual_key_in);
            } else if (EMV_type.equalsIgnoreCase("fallback")) {
                label_type.setText(R.string.fallback);
            } else if (EMV_type.equalsIgnoreCase("contactless")) {
                label_type.setText(R.string.contactless);
            }
        }

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

        EMV_type += "_txn_type";

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
                } else {
                    temp_txn_type = "-";
                }

                if (cbEMV_TC_Upload.isChecked()) {
                    temp_txn_type += "-EMVTCUpload-";
                } else {
                    temp_txn_type += "-";
                }

                if (cbOfflineSale.isChecked()) {
                    temp_txn_type += "-offSale-";
                } else {
                    temp_txn_type += "-";
                }

                if (cbOnlineSale.isChecked()) {
                    temp_txn_type += "-onSale-";
                } else {
                    temp_txn_type += "-";
                }

                if (cbPreAuth.isChecked()) {
                    temp_txn_type += "-preAuth-";
                } else {
                    temp_txn_type += "-";
                }

                if (cbPreAuthCompletion.isChecked()) {
                    temp_txn_type += "-preAuthComp-";
                } else {
                    temp_txn_type += "-";
                }

                if (cbRefund.isChecked()) {
                    temp_txn_type += "-refund-";
                } else {
                    temp_txn_type += "-";
                }

                if (cbReversal.isChecked()) {
                    temp_txn_type += "-reversal-";
                } else {
                    temp_txn_type += "-";
                }

                if (cbSettlement.isChecked()) {
                    temp_txn_type += "-settlement-";
                } else {
                    temp_txn_type += "-";
                }

                if (cbSettlementBatchUpload.isChecked()) {
                    temp_txn_type += "-settlementBatchUp-";
                } else {
                    temp_txn_type += "-";
                }


                if (cbVoid.isChecked()) {
                    temp_txn_type += "-void-";
                } else {
                    temp_txn_type += "-";
                }

                if (cbVoidPreAuth.isChecked()) {
                    temp_txn_type += "-voidPreAuth-";
                } else {
                    temp_txn_type += "-";
                }

                if (cbVoidPreAuthCompletion.isChecked()) {
                    temp_txn_type += "-voidPreAuthComp-";
                } else {
                    temp_txn_type += "-";
                }

                if (cbVoidRefund.isChecked()) {
                    temp_txn_type += "-voidRefund-";
                } else {
                    temp_txn_type += "-";
                }

                edit.putString(EMV_type, temp_txn_type);
                edit.commit();

                System.out.println("---" + prefsettings.getString(Constants.available_entry_mode, ""));

                finish();
            }

        });
    }

    private void initView() {
        LinearLayout msg = (LinearLayout) findViewById(R.id.msg);
        LinearLayout adjustment = (LinearLayout) findViewById(R.id.adjustment);
        LinearLayout emv_tc_upload = (LinearLayout) findViewById(R.id.emv_tc_upload);
        LinearLayout offlineSale = (LinearLayout) findViewById(R.id.offlineSale);
        LinearLayout onlineSale = (LinearLayout) findViewById(R.id.onlineSale);
        LinearLayout pre_auth = (LinearLayout) findViewById(R.id.pre_auth);
        LinearLayout pre_auth_complete = (LinearLayout) findViewById(R.id.pre_auth_complete);
        LinearLayout refund = (LinearLayout) findViewById(R.id.refund);
        LinearLayout reversal = (LinearLayout) findViewById(R.id.reversal);
        LinearLayout settlement = (LinearLayout) findViewById(R.id.settlement);
        LinearLayout settlementBatchUpload = (LinearLayout) findViewById(R.id.settlementBatchUpload);
        LinearLayout txn_void = (LinearLayout) findViewById(R.id.txn_void);
        LinearLayout voidPreAuth = (LinearLayout) findViewById(R.id.voidPreAuth);
        LinearLayout voidPreAuthCompletion = (LinearLayout) findViewById(R.id.voidPreAuthCompletion);
        LinearLayout voidRefund = (LinearLayout) findViewById(R.id.voidRefund);

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);

        String txnType = prefsettings.getString(Constants.txn_type, "").toLowerCase();

        if (txnType.equals("")) {
            msg.setVisibility(View.VISIBLE);
        } else {
            msg.setVisibility(View.GONE);
        }

        if (txnType.contains("-adjustment-")) {
            adjustment.setVisibility(View.VISIBLE);
        } else {
            adjustment.setVisibility(View.GONE);
        }

        if (txnType.contains("-emvtcupload-")) {
            emv_tc_upload.setVisibility(View.VISIBLE);
        } else {
            emv_tc_upload.setVisibility(View.GONE);
        }

        if (txnType.contains("-offsale-")) {
            offlineSale.setVisibility(View.VISIBLE);
        } else {
            offlineSale.setVisibility(View.GONE);
        }

        if (txnType.contains("-onsale-")) {
            onlineSale.setVisibility(View.VISIBLE);
        } else {
            onlineSale.setVisibility(View.GONE);
        }

        if (txnType.contains("-preauth-")) {
            pre_auth.setVisibility(View.VISIBLE);
        } else {
            pre_auth.setVisibility(View.GONE);
        }

        if (txnType.contains("-preauthcomp-")) {
            pre_auth_complete.setVisibility(View.VISIBLE);
        } else {
            pre_auth_complete.setVisibility(View.GONE);
        }

        if (txnType.contains("-refund-")) {
            refund.setVisibility(View.VISIBLE);
        } else {
            refund.setVisibility(View.GONE);
        }

        if (txnType.contains("-reversal-")) {
            reversal.setVisibility(View.VISIBLE);
        } else {
            reversal.setVisibility(View.GONE);
        }

        if (txnType.contains("-settlement-")) {
            settlement.setVisibility(View.VISIBLE);
        } else {
            settlement.setVisibility(View.GONE);
        }

        if (txnType.contains("-settlementbatchup-")) {
            settlementBatchUpload.setVisibility(View.VISIBLE);
        } else {
            settlementBatchUpload.setVisibility(View.GONE);
        }

        if (txnType.contains("-void-")) {
            txn_void.setVisibility(View.VISIBLE);
        } else {
            txn_void.setVisibility(View.GONE);
        }

        if (txnType.contains("-voidpreauth-")) {
            voidPreAuth.setVisibility(View.VISIBLE);
        } else {
            voidPreAuth.setVisibility(View.GONE);
        }

        if (txnType.contains("-voidpreauthcomp-")) {
            voidPreAuthCompletion.setVisibility(View.VISIBLE);
        } else {
            voidPreAuthCompletion.setVisibility(View.GONE);
        }

        if (txnType.contains("-voidrefund-")) {
            voidRefund.setVisibility(View.VISIBLE);
        } else {
            voidRefund.setVisibility(View.GONE);
        }

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
