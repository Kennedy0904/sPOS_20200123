package com.example.dell.smartpos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class PaymentDetails extends AppCompatActivity {

    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;
    private Button btnSubmit;
    private Button btnReset;

    private EditText txtPhone1;
    private EditText txtPhone2;
    private EditText txtEmail;
    private EditText txtRemarks;

    private LinearLayout layout ;

    AlertDialog.Builder dialog;
    AlertDialog alertDialog;

    String currCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        setTitle(R.string.simplepay_menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent paymentIntent = getIntent();
        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        final String amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra("amount")));
        currCode = paymentIntent.getStringExtra("currCode");

        step1 = (TextView) findViewById(R.id.progress_step_1);
        step2 = (TextView) findViewById(R.id.progress_step_2);
        step3 = (TextView) findViewById(R.id.progress_step_3);
        step4 = (TextView) findViewById(R.id.progress_step_4);
        layout = (LinearLayout) findViewById(R.id.layout) ;

        txtPhone1 = (EditText) findViewById(R.id.txtPhone1);
        txtPhone2 = (EditText) findViewById(R.id.txtPhone2);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtRemarks = (EditText) findViewById(R.id.txtRemarks);

        btnSubmit = (Button) findViewById(R.id.btnSubmit) ;
        btnReset = (Button) findViewById(R.id.btnReset);

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
                Intent intent = new Intent(PaymentDetails.this, DialogPaymentDetails.class);
                intent.putExtra("currCode", currCode);
                intent.putExtra("amount", amount);
                intent.putExtra("phone1", txtPhone1.getText().toString());
                intent.putExtra("phone2", txtPhone2.getText().toString());
                intent.putExtra("email", txtEmail.getText().toString());
                intent.putExtra("remarks", txtRemarks.getText().toString());
                startActivityForResult(intent, 1);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                txtPhone1.setText("");
                txtPhone2.setText("");
                txtEmail.setText("");
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
