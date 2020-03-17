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

public class EMV_TxnTypeCardScheme extends AppCompatActivity {

    private Button btnOkTxnCardScheme;

    private static String temp_card_scheme = "";
    private static Class nextClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_txn_card_scheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout visa_layout = (LinearLayout) findViewById(R.id.visa_layout);
        LinearLayout mc_layout = (LinearLayout) findViewById(R.id.mc_layout);
        LinearLayout chinaUnionPay_layout = (LinearLayout) findViewById(R.id.chinaUnionPay_layout);
        LinearLayout amex_layout = (LinearLayout) findViewById(R.id.amex_layout);
        LinearLayout jcb_layout = (LinearLayout) findViewById(R.id.jcb_layout);
        LinearLayout diners_layout = (LinearLayout) findViewById(R.id.diners_layout);

        btnOkTxnCardScheme = (Button) findViewById(R.id.btnOkTxnCardScheme);

        Intent intent = getIntent();
        final String getClass = intent.getStringExtra("class");

        if (getClass.equalsIgnoreCase("TAC")) {
            nextClass = EMV_TAC.class;
            this.setTitle(R.string.tac);
        } else if (getClass.equalsIgnoreCase("CVM")) {
            nextClass = EMV_CVM.class;
            this.setTitle(R.string.cvm);
        } else if (getClass.equalsIgnoreCase("TXN_TYPE")) {
            nextClass = EMV_TransactionType.class;
            this.setTitle(R.string.txn_type_card_scheme);
        }

        initView();

        visa_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_TxnTypeCardScheme.this, nextClass);
                intent.putExtra("title", "cardScheme");
                intent.putExtra("EMV_type", "visa");
                startActivity(intent);

            }
        });

        mc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_TxnTypeCardScheme.this, nextClass);
                intent.putExtra("title", "cardScheme");
                intent.putExtra("EMV_type", "master");
                startActivity(intent);

            }
        });

        chinaUnionPay_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_TxnTypeCardScheme.this, nextClass);
                intent.putExtra("title", "cardScheme");
                intent.putExtra("EMV_type", "cup");
                startActivity(intent);

            }
        });

        amex_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_TxnTypeCardScheme.this, nextClass);
                intent.putExtra("title", "cardScheme");
                intent.putExtra("EMV_type", "amex");
                startActivity(intent);

            }
        });

        jcb_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_TxnTypeCardScheme.this, nextClass);
                intent.putExtra("title", "cardScheme");
                intent.putExtra("EMV_type", "jcb");
                startActivity(intent);

            }
        });

        diners_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_TxnTypeCardScheme.this, nextClass);
                intent.putExtra("title", "cardScheme");
                intent.putExtra("EMV_type", "diners");
                startActivity(intent);

            }
        });


        btnOkTxnCardScheme.setOnClickListener(new View.OnClickListener() {
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

        String card_scheme = prefsettings.getString(Constants.card_scheme, "").toLowerCase();

        if (card_scheme.equals("")) {
            findViewById(R.id.msg).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.msg).setVisibility(View.GONE);
        }

        if (card_scheme.contains("-visa-")) {
            findViewById(R.id.visa_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.visa_layout).setVisibility(View.GONE);
        }

        if (card_scheme.contains("-mastercard-")) {
            findViewById(R.id.mc_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.mc_layout).setVisibility(View.GONE);
        }

        if (card_scheme.contains("-cup-")) {
            findViewById(R.id.chinaUnionPay_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.chinaUnionPay_layout).setVisibility(View.GONE);
        }

        if (card_scheme.contains("-amex-")) {
            findViewById(R.id.amex_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.amex_layout).setVisibility(View.GONE);
        }

        if (card_scheme.contains("-jcb-")) {
            findViewById(R.id.jcb_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.jcb_layout).setVisibility(View.GONE);
        }

        if (card_scheme.contains("-diners-")) {
            findViewById(R.id.diners_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.diners_layout).setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
