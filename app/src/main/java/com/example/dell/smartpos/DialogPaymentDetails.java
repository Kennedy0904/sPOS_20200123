package com.example.dell.smartpos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class DialogPaymentDetails extends Activity {

    private TextView txtPhone1;
    private TextView txtPhone2;
    private TextView txtEmail;
    private TextView txtRemarks;
    private TextView txtCurrCode;
    private TextView txtAmount;
    private Button btnCancel;
    private Button btnSubmit;
    private TextView txtAction;


    String phone1;
    String phone2;
    String email;
    String remarks;
    String currCode;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_payment_details);

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
        txtCurrCode = (TextView) findViewById(R.id.txtCurCode);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        btnCancel = (Button) findViewById(R.id.dialogCancel);
        btnSubmit = (Button) findViewById(R.id.dialogConfirm);
        txtAction = (TextView) findViewById(R.id.txtAction);

        txtAction.setText(R.string.payment_details);

        txtPhone1.setText(phone1);
        txtPhone2.setText(phone2);
        txtCurrCode.setText(currCode);
        txtAmount.setText(amount);
        txtEmail.setText(email);
        txtRemarks.setText(remarks);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DialogPaymentDetails.this, SuccessSimplePay.class);
                intent.putExtra("currCode", currCode);
                intent.putExtra("amount", amount);
                intent.putExtra("phone1", txtPhone1.getText().toString());
                intent.putExtra("phone2", txtPhone2.getText().toString());
                intent.putExtra("email", txtEmail.getText().toString());
                intent.putExtra("remarks", txtRemarks.getText().toString());
                startActivityForResult(intent, 1);
            }
        });
    }
}
