package com.example.dell.smartpos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class installmentSuccess extends AppCompatActivity {

    private TextView txtMerRef;
    private TextView txtCurrCode;
    private TextView txtAmount;
    private TextView txtInstallment;

    private Button btnOk;

    String currCode;
    String amount;
    String tenure;
    String merchantRef;

    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installment_success);
        setTitle(R.string.installment_menu);

        step1 = (TextView) findViewById(R.id.progress_step_1);
        step2 = (TextView) findViewById(R.id.progress_step_2);
        step3 = (TextView) findViewById(R.id.progress_step_3);
        step4 = (TextView) findViewById(R.id.progress_step_4);

        step1.setText(R.string.enter_amount);
        step2.setText(R.string.select_bank);
        step3.setText(R.string.read_card);
        step4.setText(R.string.payment_result);

        step2.setTypeface(null, Typeface.BOLD);
        step3.setTypeface(null, Typeface.BOLD);
        step4.setTypeface(null, Typeface.BOLD);

        final Intent tenureIntent = getIntent();
        merchantRef = tenureIntent.getStringExtra(Constants.MERCHANT_REF);
        currCode = tenureIntent.getStringExtra(Constants.CURRCODE);
        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        Log.d("1234", amount + "12");
        amount = formatter.format(Double.parseDouble(tenureIntent.getStringExtra("amount")));
        tenure = tenureIntent.getStringExtra("tenure");

        txtMerRef = (TextView) findViewById(R.id.txtMerRef);
        txtCurrCode = (TextView) findViewById(R.id.txtCurrCode);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtInstallment = (TextView) findViewById(R.id.txtInstallment);

        txtMerRef.setText(merchantRef);
        txtCurrCode.setText(currCode);
        txtAmount.setText(amount);
        txtInstallment.setText(tenure);

        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intentNew = new Intent();
                intentNew.setClass(installmentSuccess.this, MainActivity.class);
                intentNew.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentNew);
            }

        });

    }
}
