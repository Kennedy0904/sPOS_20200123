package com.example.dell.smartpos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class SuccessSimplePay extends AppCompatActivity {

    private TextView txtPhone1;
    private TextView txtPhone2;
    private TextView txtEmail;
    private TextView txtRemarks;
    private TextView txtCurrCode;
    private TextView txtAmount;
    private Button btnCancel;

    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;


    String phone1;
    String phone2;
    String email;
    String remarks;
    String currCode;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_simple_pay);


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

        final Intent paymentIntent = getIntent();
        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra("amount")));
        currCode = paymentIntent.getStringExtra("currCode");
        phone1 = paymentIntent.getStringExtra("phone1");
        phone2 = paymentIntent.getStringExtra("phone2");
        email = paymentIntent.getStringExtra("email");
        remarks  = paymentIntent.getStringExtra("remarks");

        txtPhone1 = (TextView) findViewById(R.id.txtPhone1);
        txtPhone2 = (TextView) findViewById(R.id.txtPhone2);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtRemarks = (TextView) findViewById(R.id.txtRemarks);
        txtCurrCode = (TextView) findViewById(R.id.txtCurrCode);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        btnCancel = (Button) findViewById(R.id.btnDone);

        txtPhone1.setText(phone1);
        txtPhone2.setText(phone2);
        txtCurrCode.setText(currCode);
        txtAmount.setText(amount);
        txtEmail.setText(email);
        txtRemarks.setText(remarks);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentNew = new Intent();
                intentNew.setClass(SuccessSimplePay.this, MainActivity.class);
                intentNew.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentNew);
            }
        });
    }
}
