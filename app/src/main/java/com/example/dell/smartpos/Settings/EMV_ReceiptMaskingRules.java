package com.example.dell.smartpos.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.smartpos.R;

public class EMV_ReceiptMaskingRules extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_receipt_masking_rules);
        this.setTitle(R.string.receipt_masking_rules);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout online_sale_layout = (LinearLayout) findViewById(R.id.online_sale_layout);
        final LinearLayout online_pan = (LinearLayout) findViewById(R.id.online_pan);
        final LinearLayout online_expiry = (LinearLayout) findViewById(R.id.online_expiry);
        LinearLayout pre_auth_layout = (LinearLayout) findViewById(R.id.pre_auth_layout);
        final LinearLayout pre_auth_pan = (LinearLayout) findViewById(R.id.pre_auth_pan);
        final LinearLayout pre_auth_expiry = (LinearLayout) findViewById(R.id.pre_auth_expiry);

        final TextView online_sale_arrow = (TextView) findViewById(R.id.online_sale_arrow);
        final TextView pre_auth_arrow = (TextView) findViewById(R.id.pre_auth_arrow);


        online_sale_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (online_pan.getVisibility() == View.GONE) {
                    online_pan.setVisibility(View.VISIBLE);
                    online_expiry.setVisibility(View.VISIBLE);
                    pre_auth_pan.setVisibility(View.GONE);
                    pre_auth_expiry.setVisibility(View.GONE);
                    online_sale_arrow.setRotation(90);
                    pre_auth_arrow.setRotation(0);
                } else if (online_pan.getVisibility() == View.VISIBLE) {
                    online_pan.setVisibility(View.GONE);
                    online_expiry.setVisibility(View.GONE);
                    online_sale_arrow.setRotation(0);
                }
            }
        });

        pre_auth_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (pre_auth_pan.getVisibility() == View.GONE) {
                    online_pan.setVisibility(View.GONE);
                    online_expiry.setVisibility(View.GONE);
                    pre_auth_pan.setVisibility(View.VISIBLE);
                    pre_auth_expiry.setVisibility(View.VISIBLE);
                    online_sale_arrow.setRotation(0);
                    pre_auth_arrow.setRotation(90);
                } else if (pre_auth_pan.getVisibility() == View.VISIBLE) {
                    pre_auth_pan.setVisibility(View.GONE);
                    pre_auth_expiry.setVisibility(View.GONE);
                    pre_auth_arrow.setRotation(0);
                }
            }
        });


        online_pan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ReceiptMaskingRules.this, EMV_DisplayCardScheme.class);
                intent.putExtra("maskingType", "pan");
                intent.putExtra("txnType", "online");
                startActivity(intent);
            }
        });

        online_expiry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ReceiptMaskingRules.this, EMV_DisplayCardScheme.class);
                intent.putExtra("maskingType", "expiry");
                intent.putExtra("txnType", "online");
                startActivity(intent);
            }
        });

        pre_auth_pan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ReceiptMaskingRules.this, EMV_DisplayCardScheme.class);
                intent.putExtra("maskingType", "pan");
                intent.putExtra("txnType", "pre_auth");
                startActivity(intent);
            }
        });

        pre_auth_expiry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Intent intent = new Intent(EMV_ReceiptMaskingRules.this, EMV_DisplayCardScheme.class);
                intent.putExtra("maskingType", "expiry");
                intent.putExtra("txnType", "pre_auth");
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
