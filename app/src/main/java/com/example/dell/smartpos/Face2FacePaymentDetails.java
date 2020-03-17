package com.example.dell.smartpos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.FaceDetector;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Face2FacePaymentDetails extends AppCompatActivity {

    private Button btnSubmit;
    private Button btnReset;

    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;

    private EditText txtRemarks;

    private TextView currency;
    private TextView edtAmount;

    private LinearLayout layout ;

    String currCode;
    String remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face2_face_payment_details);

        setTitle(R.string.simplepay_menu);

        final Intent paymentIntent = getIntent();
        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        final String amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra("amount")));
        currCode = paymentIntent.getStringExtra("currCode");

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnReset = (Button) findViewById(R.id.btnReset);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        step1 = (TextView) findViewById(R.id.progress_step_1);
        step2 = (TextView) findViewById(R.id.progress_step_2);
        step3 = (TextView) findViewById(R.id.progress_step_3);
        step4 = (TextView) findViewById(R.id.progress_step_4);
        layout = (LinearLayout) findViewById(R.id.layout) ;

        currency = (TextView) findViewById(R.id.currency);
        edtAmount = (TextView) findViewById(R.id.edtAmount);
        txtRemarks = (EditText) findViewById(R.id.txtRemarks);

        layout = (LinearLayout) findViewById(R.id.layout) ;

        currency.setText(currCode);
        edtAmount.setText(amount);

        step1.setText(R.string.enter_amount);
        step2.setText(R.string.select_payment);
        step3.setText(R.string.payment_info);
        step4.setText(R.string.payment_result);

        step2.setTypeface(null, Typeface.BOLD);
        step3.setTypeface(null, Typeface.BOLD);

        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Face2FacePaymentDetails.this, SimplePayQrResult.class);
                intent.putExtra("currCode", currCode);
                intent.putExtra("amount", amount);
                intent.putExtra("remarks", txtRemarks.getText().toString());
                startActivityForResult(intent, 1);
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
               txtRemarks.setText("");
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
