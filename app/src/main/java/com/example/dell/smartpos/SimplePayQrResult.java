package com.example.dell.smartpos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class SimplePayQrResult extends AppCompatActivity {

    private TextView txtRemarks;
    private TextView txtCurrCode;
    private TextView txtAmount;

    private Button btnDone;

    String remarks;
    String currCode;
    String amount;

    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_pay_qr_result);

        final Intent paymentIntent = getIntent();
        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra("amount")));
        currCode = paymentIntent.getStringExtra("currCode");
        remarks  = paymentIntent.getStringExtra("remarks");

        step1 = (TextView) findViewById(R.id.progress_step_1);
        step2 = (TextView) findViewById(R.id.progress_step_2);
        step3 = (TextView) findViewById(R.id.progress_step_3);
        step4 = (TextView) findViewById(R.id.progress_step_4);

        setTitle(R.string.simplepay_menu);
        step1.setText(R.string.enter_amount);
        step2.setText(R.string.select_payment);
        step3.setText(R.string.payment_info);
        step4.setText(R.string.payment_result);

        step2.setTypeface(null, Typeface.BOLD);
        step3.setTypeface(null, Typeface.BOLD);
        step4.setTypeface(null, Typeface.BOLD);

        txtRemarks = (TextView) findViewById(R.id.txtRemarks);
        txtCurrCode = (TextView) findViewById(R.id.txtCurrCode);
        txtAmount = (TextView) findViewById(R.id.txtAmount);

        btnDone = (Button) findViewById(R.id.btnDone);

        txtCurrCode.setText(currCode);
        txtAmount.setText(amount);
        txtRemarks.setText(remarks);

        btnDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentNew = new Intent();
                intentNew.setClass(SimplePayQrResult.this, MainActivity.class);
                intentNew.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentNew);
            }
        });
    }
}
