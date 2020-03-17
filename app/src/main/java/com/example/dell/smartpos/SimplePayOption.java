package com.example.dell.smartpos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class SimplePayOption extends AppCompatActivity {


    //private EditText remark = null;
    public Integer ischecked = 0;
    String currCode = "";
    String currCode1 = "";
    String merId = "";
    String merchantName = "";
    String payType = "";
    String payMethod = "";
    String hideSurcharge = "";

    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;

    private Button btnGenPayLink;
    private Button btnGenQr;

    public TextView txtAmount = null;
    public TextView txtCurrency = null;
    public TextView MerchantRef = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_pay_option);

        setTitle(R.string.simplepay_menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent paymentIntent = getIntent();
        final Intent PaymentDetailIntent = new Intent(this, PaymentDetails.class);
        merId = paymentIntent.getStringExtra(Constants.MERID);

        step1 = (TextView) findViewById(R.id.progress_step_1);
        step2 = (TextView) findViewById(R.id.progress_step_2);
        step3 = (TextView) findViewById(R.id.progress_step_3);
        step4 = (TextView) findViewById(R.id.progress_step_4);

        btnGenPayLink = (Button)findViewById(R.id.btn_gen_link);
        btnGenQr = (Button) findViewById(R.id.btn_gen_qr);

        setTitle(R.string.simplepay_menu);
        step1.setText(R.string.enter_amount);
        step2.setText(R.string.select_payment);
        step3.setText(R.string.payment_info);
        step4.setText(R.string.payment_result);

        step2.setTypeface(null, Typeface.BOLD);

        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        final String amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra(Constants.PRE_AMOUNT)));

        //*******************layout init**************************//
        //----merchant textview start----//
        MerchantRef = (TextView) findViewById(R.id.edtMerchantRef);
        txtAmount = (TextView) findViewById(R.id.edtAmount);
        txtCurrency = (TextView) findViewById(R.id.transactionreport_currCode);

        currCode = paymentIntent.getStringExtra(Constants.CURRCODE);
        currCode1 = paymentIntent.getStringExtra(Constants.CURRENCY);
        txtCurrency.setText(currCode);
        MerchantRef.setText(paymentIntent.getStringExtra(Constants.MERCHANT_REF));
        hideSurcharge = paymentIntent.getStringExtra(Constants.pref_hideSurcharge);
        merchantName = paymentIntent.getStringExtra(Constants.MERNAME);

        txtAmount.setText(amount);

        btnGenPayLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PaymentDetailIntent.putExtra("currCode", currCode);
                PaymentDetailIntent.putExtra("amount", amount);
                startActivity(PaymentDetailIntent);
            }
        });

        btnGenQr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent qrIntent = new Intent(SimplePayOption.this, Face2FacePaymentDetails.class);
                qrIntent.putExtra("currCode", currCode);
                qrIntent.putExtra("amount", amount);
                startActivity(qrIntent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
