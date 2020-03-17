package com.example.dell.smartpos;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MobilePayment_Failure extends AppCompatActivity {

    private TextView txtStep2, txtStep3, txtStep4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_payment__failure);

        txtStep2 = (TextView) findViewById(R.id.progress_mobile_text_2);
        txtStep3 = (TextView) findViewById(R.id.progress_mobile_text_3);
        txtStep4 = (TextView) findViewById(R.id.progress_mobile_text_4);

        txtStep2.setTypeface(null, Typeface.BOLD);
        txtStep3.setTypeface(null, Typeface.BOLD);
        txtStep4.setTypeface(null, Typeface.BOLD);
    }
}
