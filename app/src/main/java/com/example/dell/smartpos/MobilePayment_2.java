package com.example.dell.smartpos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MobilePayment_2 extends AppCompatActivity {

    private TextView txtStep2;
    private RelativeLayout ApplePay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_payment_2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        txtStep2 = (TextView) findViewById(R.id.progress_mobile_text_2);
        txtStep2.setTypeface(null, Typeface.BOLD);

        ApplePay = (RelativeLayout) findViewById(R.id.ApplePay);
        ApplePay.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(MobilePayment_2.this, MobilePayment_3.class);
                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        Intent intent = new Intent(MobilePayment_2.this, MainActivity.class);
        startActivity(intent);
        return true;
    }
}
