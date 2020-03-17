package com.example.dell.smartpos.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.GlobalFunction;
import com.example.dell.smartpos.PaymentTimeoutSetting;
import com.example.dell.smartpos.R;

public class SettingsEMVConfig extends AppCompatActivity {

    private String currCode;
    String cardPayBankId = "";

    public SettingsEMVConfig() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_emv_config);
        setTitle(R.string.settings_emv_config);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout setting_cardScheme = (LinearLayout) findViewById(R.id.setting_cardScheme);
        LinearLayout setting_entryMode = (LinearLayout) findViewById(R.id.setting_entryMode);
        LinearLayout setting_txnType = (LinearLayout) findViewById(R.id.setting_txnType);
        LinearLayout setting_contactless_txn_limit = (LinearLayout) findViewById(R.id.setting_contactless_txn_limit);
        LinearLayout setting_CVM = (LinearLayout) findViewById(R.id.setting_CVM);
        LinearLayout setting_emv_attempt = (LinearLayout) findViewById(R.id.setting_emv_attempt);
        LinearLayout setting_receipt_masking = (LinearLayout) findViewById(R.id.setting_receipt_masking);
        LinearLayout setting_TAC = (LinearLayout) findViewById(R.id.setting_TAC);
        LinearLayout setting_txn_card = (LinearLayout) findViewById(R.id.setting_txn_card);
        LinearLayout setting_txn_entry = (LinearLayout) findViewById(R.id.setting_txn_entry);

        currCode = getIntent().getStringExtra(Constants.CURRCODE);
        cardPayBankId = GlobalFunction.getCardPayBankId(SettingsEMVConfig.this);

        // Hide EMV Config for First-Data
        if (cardPayBankId.equalsIgnoreCase(Constants.FIRST_DATA)) {
            setting_cardScheme.setVisibility(View.GONE);
            setting_entryMode.setVisibility(View.GONE);
            setting_contactless_txn_limit.setVisibility(View.GONE);
            setting_CVM.setVisibility(View.GONE);
            setting_emv_attempt.setVisibility(View.GONE);
            setting_receipt_masking.setVisibility(View.GONE);
            setting_TAC.setVisibility(View.GONE);
            setting_txn_card.setVisibility(View.GONE);
            setting_txn_entry.setVisibility(View.GONE);
        }

        //When Click Actions
        setting_cardScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SettingsEMVConfig.this, EMV_CardScheme.class);
                startActivity(intent);
            }
        });

        setting_entryMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SettingsEMVConfig.this, EMV_EntryMode.class);
                startActivity(intent);
            }
        });

        setting_txnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SettingsEMVConfig.this, EMV_TxnType.class);
                startActivity(intent);
            }
        });

        setting_contactless_txn_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsEMVConfig.this, EMV_ContactlessTxnLimit.class);
                intent.putExtra(Constants.CURRCODE, currCode);
                startActivity(intent);
            }
        });

        setting_emv_attempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsEMVConfig.this, EMV_CardDataReadAttempt.class);
                startActivity(intent);
            }
        });

        setting_CVM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SettingsEMVConfig.this, EMV_TxnTypeCardScheme.class);
                intent.putExtra("class", "CVM");
                startActivity(intent);
            }
        });

        setting_TAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SettingsEMVConfig.this, EMV_TxnTypeCardScheme.class);
                intent.putExtra("class", "TAC");
                startActivity(intent);
            }
        });

        setting_receipt_masking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsEMVConfig.this, EMV_ReceiptMaskingRules.class);
                startActivity(intent);
            }
        });

        setting_txn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SettingsEMVConfig.this, EMV_TxnTypeCardScheme.class);
                intent.putExtra("class", "TXN_TYPE");

                startActivity(intent);
            }
        });

        setting_txn_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsEMVConfig.this, EMV_TxnTypeEntryMode.class);
                startActivity(intent);
            }
        });
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
