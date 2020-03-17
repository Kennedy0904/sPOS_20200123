package com.example.dell.smartpos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MobilePayment_3 extends AppCompatActivity {

    private TextView txtStep2, txtStep3;
    private RelativeLayout tap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_payment_3);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        txtStep2 = (TextView) findViewById(R.id.progress_mobile_text_2);
        txtStep3 = (TextView) findViewById(R.id.progress_mobile_text_3);

        txtStep2.setTypeface(null, Typeface.BOLD);
        txtStep3.setTypeface(null, Typeface.BOLD);

        tap = (RelativeLayout) findViewById(R.id.tap);
        tap.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(MobilePayment_3.this, MobilePayment_Success.class);
                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        Intent intent = new Intent(MobilePayment_3.this, MainActivity.class);
        startActivity(intent);
        return true;
    }
}
