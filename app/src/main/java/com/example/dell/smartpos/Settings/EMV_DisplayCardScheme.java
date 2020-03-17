package com.example.dell.smartpos.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.dell.smartpos.R;

public class EMV_DisplayCardScheme extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_display_card_scheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String maskingType = intent.getStringExtra("maskingType");
        final String txnType = intent.getStringExtra("txnType");

        if (maskingType.equalsIgnoreCase("pan")) {
            setTitle(R.string.pan_masking_rules);
        } else if (maskingType.equalsIgnoreCase("expiry")) {
            setTitle(R.string.exp_date_masking_rules);
        }

        LinearLayout visa_layout = (LinearLayout) findViewById(R.id.visa_layout);
        LinearLayout mc_layout = (LinearLayout) findViewById(R.id.mc_layout);
        LinearLayout chinaUnionPay_layout = (LinearLayout) findViewById(R.id.chinaUnionPay_layout);
        LinearLayout amex_layout = (LinearLayout) findViewById(R.id.amex_layout);
        LinearLayout jcb_layout = (LinearLayout) findViewById(R.id.jcb_layout);
        LinearLayout diners_layout = (LinearLayout) findViewById(R.id.diners_layout);

        visa_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_DisplayCardScheme.this, EMV_Masking.class);
                intent.putExtra("maskingType", maskingType);
                intent.putExtra("txnType", txnType);
                intent.putExtra("cardScheme", "visa");
                startActivity(intent);
            }
        });

        mc_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_DisplayCardScheme.this, EMV_Masking.class);
                intent.putExtra("maskingType", maskingType);
                intent.putExtra("txnType", txnType);
                intent.putExtra("cardScheme", "mc");
                startActivity(intent);
            }
        });

        chinaUnionPay_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_DisplayCardScheme.this, EMV_Masking.class);
                intent.putExtra("maskingType", maskingType);
                intent.putExtra("txnType", txnType);
                intent.putExtra("cardScheme", "cup");
                startActivity(intent);
            }
        });

        amex_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_DisplayCardScheme.this, EMV_Masking.class);
                intent.putExtra("maskingType", maskingType);
                intent.putExtra("txnType", txnType);
                intent.putExtra("cardScheme", "amex");
                startActivity(intent);
            }
        });

        jcb_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_DisplayCardScheme.this, EMV_Masking.class);
                intent.putExtra("maskingType", maskingType);
                intent.putExtra("txnType", txnType);
                intent.putExtra("cardScheme", "jcb");
                startActivity(intent);
            }
        });

        diners_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_DisplayCardScheme.this, EMV_Masking.class);
                intent.putExtra("maskingType", maskingType);
                intent.putExtra("txnType", txnType);
                intent.putExtra("cardScheme", "diners");
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
