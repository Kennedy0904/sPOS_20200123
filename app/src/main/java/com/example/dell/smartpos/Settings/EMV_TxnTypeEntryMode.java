package com.example.dell.smartpos.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;

public class EMV_TxnTypeEntryMode extends AppCompatActivity {

    private Button btnOkEntryMode;

    private static String temp_entry_mode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_txn_entry_mode);
        this.setTitle(R.string.txn_type_entry_mode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout chip_layout = (LinearLayout) findViewById(R.id.chip_layout);
        LinearLayout swipe_layout = (LinearLayout) findViewById(R.id.swipe_layout);
        LinearLayout manualKeyIn_layout = (LinearLayout) findViewById(R.id.manualKeyIn_layout);
        LinearLayout fallback_layout = (LinearLayout) findViewById(R.id.fallback_layout);
        LinearLayout contactless_layout = (LinearLayout) findViewById(R.id.contactless_layout);

        btnOkEntryMode = (Button) findViewById(R.id.btnOkEntryMode);

        initView();

        chip_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                    final Intent intent = new Intent(EMV_TxnTypeEntryMode.this, EMV_TransactionType.class);
                intent.putExtra("title", "entryMode");
                    intent.putExtra("EMV_type","chip");
                    startActivity(intent);
            }
        });

        swipe_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                    final Intent intent = new Intent(EMV_TxnTypeEntryMode.this, EMV_TransactionType.class);
                intent.putExtra("title", "entryMode");
                    intent.putExtra("EMV_type","swipe");
                    startActivity(intent);
            }
        });

        manualKeyIn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                    final Intent intent = new Intent(EMV_TxnTypeEntryMode.this, EMV_TransactionType.class);
                intent.putExtra("title", "entryMode");
                    intent.putExtra("EMV_type","manual_key_in");
                    startActivity(intent);
            }
        });

        fallback_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                    final Intent intent = new Intent(EMV_TxnTypeEntryMode.this, EMV_TransactionType.class);
                intent.putExtra("title", "entryMode");
                    intent.putExtra("EMV_type","fallback");
                    startActivity(intent);
            }
        });

        contactless_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                    final Intent intent = new Intent(EMV_TxnTypeEntryMode.this, EMV_TransactionType.class);
                intent.putExtra("title", "entryMode");
                    intent.putExtra("EMV_type","contactless");
                    startActivity(intent);
            }
        });


        btnOkEntryMode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }

        });
    }

    private void initView() {
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);

        if (prefsettings.getString(Constants.entry_mode, "").equals("")) {
            findViewById(R.id.msg).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.msg).setVisibility(View.GONE);
        }

        if (prefsettings.getString(Constants.entry_mode, "").toLowerCase().contains("-chip-")) {
            findViewById(R.id.chip_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.chip_layout).setVisibility(View.GONE);
        }

        if (prefsettings.getString(Constants.entry_mode, "").toLowerCase().contains("-swipe-")) {
            findViewById(R.id.swipe_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.swipe_layout).setVisibility(View.GONE);
        }

        if (prefsettings.getString(Constants.entry_mode, "").toLowerCase().contains("-manual_key_in-")) {
            findViewById(R.id.manualKeyIn_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.manualKeyIn_layout).setVisibility(View.GONE);
        }

        if (prefsettings.getString(Constants.entry_mode, "").toLowerCase().contains("-fallback-")) {
            findViewById(R.id.fallback_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.fallback_layout).setVisibility(View.GONE);
        }

        if (prefsettings.getString(Constants.entry_mode, "").toLowerCase().contains("-contactless-")) {
            findViewById(R.id.contactless_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.contactless_layout).setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
