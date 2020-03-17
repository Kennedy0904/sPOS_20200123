package com.example.dell.smartpos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class InstallmentPayment2 extends AppCompatActivity {

    private RelativeLayout citibank;
    private RelativeLayout publicBank;
    private RelativeLayout maybank;
    private RelativeLayout scb;
    private RelativeLayout cimbBank;
    private RelativeLayout ocbc;
    private RelativeLayout uob;
    private RelativeLayout hongLeong;
    private RelativeLayout hsbc;

    private RelativeLayout coupon;

    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;

    public TextView txtAmount = null;
    public TextView txtCurrency = null;
    public TextView MerchantRef = null;

    private ImageView couponApplied;

    boolean surC = false; //surcharge calution function
    boolean mdr_less_equal_0 = false; //mdr less than or equal to 0

    public int timeout = 0;
    String currCode = "";
    String currCode1 = "";
    String merchantName = "";
    String payType = "";
    String payMethod = "";
    String hideSurcharge = "";
    String merchant_id = "";
    //--Edited 25/07/18 by KJ--//
    String merchantId = "";
    String discount = "";
    String promoCode = "";
    String amount = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installment_payment2);

        setTitle(R.string.installment_menu);

        final Intent paymentIntent = getIntent();
        merchant_id = paymentIntent.getStringExtra(Constants.MERID);
        discount = paymentIntent.getStringExtra("coupon");

        setContentView(R.layout.activity_installment_payment2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        step1 = (TextView) findViewById(R.id.progress_step_1);
        step2 = (TextView) findViewById(R.id.progress_step_2);
        step3 = (TextView) findViewById(R.id.progress_step_3);
        step4 = (TextView) findViewById(R.id.progress_step_4);

        step1.setText(R.string.enter_amount);
        step2.setText(R.string.select_bank);
        step3.setText(R.string.read_card);
        step4.setText(R.string.payment_result);

        step2.setTypeface(null, Typeface.BOLD);

        MerchantRef = (TextView) findViewById(R.id.edtMerchantRef);
        txtAmount = (TextView) findViewById(R.id.edtAmount);
        txtCurrency = (TextView) findViewById(R.id.transactionreport_currCode);
        couponApplied = (ImageView) findViewById(R.id.ic_coupon);


        if (mdr_less_equal_0 || !surC) {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            String amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra(Constants.PRE_AMOUNT)));
            txtAmount.setText(amount);
        } else {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            String amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra(Constants.PRE_MDR_AMOUNT)));
            txtAmount.setText(amount);
        }


        if(discount != null){
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            String amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra("afterDiscountAmount")));
            txtAmount.setText(amount);
            couponApplied.setImageResource(R.drawable.ic_used_coupon);
            promoCode = paymentIntent.getStringExtra("promoCode");
        }

        currCode = paymentIntent.getStringExtra(Constants.CURRCODE);
        currCode1 = paymentIntent.getStringExtra(Constants.CURRENCY);
        txtCurrency.setText(currCode);
        MerchantRef.setText(paymentIntent.getStringExtra(Constants.MERCHANT_REF));
        hideSurcharge = paymentIntent.getStringExtra(Constants.pref_hideSurcharge);
        payMethod = paymentIntent.getStringExtra(Constants.PAYMETHODLIST);
        payType = paymentIntent.getStringExtra(Constants.PAYTYPE);

        coupon = (RelativeLayout) findViewById(R.id.coupon);

        citibank = (RelativeLayout) findViewById(R.id.citiBank);
        publicBank = (RelativeLayout) findViewById(R.id.publicBank);
        maybank = (RelativeLayout) findViewById(R.id.maybank);
        scb = (RelativeLayout) findViewById(R.id.scb);
        cimbBank = (RelativeLayout) findViewById(R.id.cimb);
        ocbc = (RelativeLayout) findViewById(R.id.ocbc);
        uob = (RelativeLayout) findViewById(R.id.uob);
        hongLeong = (RelativeLayout) findViewById(R.id.hongLeong);
        hsbc = (RelativeLayout) findViewById(R.id.hsbc);

        citibank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstallmentPayment2.this, DialogTenure.class);
                intent.putExtra("currCode", currCode);
                intent.putExtra("currCode1", currCode1);
                intent.putExtra(Constants.MERCHANT_REF, MerchantRef.getText().toString());
                intent.putExtra("hideSurcharge", hideSurcharge);
                intent.putExtra("payMethodList", payMethod);
                intent.putExtra("payType", payType);
                intent.putExtra("discount", discount);
                intent.putExtra("paymentMethod", "installment");
                intent.putExtra("amount", txtAmount.getText().toString());
                intent.putExtra("MerRequestAmt", txtAmount.getText().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.MERID,merchant_id);
                intent.putExtra("bank","Citi Bank");

                if(discount != null){
                    intent.putExtra("promoCode", promoCode);
                }

                startActivityForResult(intent, 1);
            }
        });


        publicBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstallmentPayment2.this, DialogTenure.class);
                intent.putExtra("currCode", currCode);
                intent.putExtra("currCode1", currCode1);
                intent.putExtra(Constants.MERCHANT_REF, MerchantRef.getText().toString());
                intent.putExtra("hideSurcharge", hideSurcharge);
                intent.putExtra("payMethodList", payMethod);
                intent.putExtra("payType", payType);
                intent.putExtra("discount", discount);
                intent.putExtra("paymentMethod", "installment");
                intent.putExtra("amount", txtAmount.getText().toString());
                intent.putExtra("MerRequestAmt", txtAmount.getText().toString());
                intent.putExtra(Constants.MERID,merchant_id);
                intent.putExtra("bank","Public Bank");

                if(discount != null){
                    intent.putExtra("promoCode", promoCode);
                }

                startActivityForResult(intent, 1);
            }
        });

        maybank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstallmentPayment2.this, DialogTenure.class);
                intent.putExtra("currCode", currCode);
                intent.putExtra("currCode1", currCode1);
                intent.putExtra(Constants.MERCHANT_REF, MerchantRef.getText().toString());
                intent.putExtra("hideSurcharge", hideSurcharge);
                intent.putExtra("payMethodList", payMethod);
                intent.putExtra("payType", payType);
                intent.putExtra("discount", discount);
                intent.putExtra("paymentMethod", "installment");
                intent.putExtra("amount", txtAmount.getText().toString());
                intent.putExtra("MerRequestAmt", txtAmount.getText().toString());
                intent.putExtra(Constants.MERID,merchant_id);
                intent.putExtra("bank","Maybank");

                if(discount != null){
                    intent.putExtra("promoCode", promoCode);
                }

                startActivityForResult(intent, 1);
            }
        });

        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstallmentPayment2.this, DialogCoupon.class);
                intent.putExtra("currCode", currCode);
                intent.putExtra("currCode1", currCode1);
                intent.putExtra(Constants.MERCHANT_REF, MerchantRef.getText().toString());
                intent.putExtra("hideSurcharge", hideSurcharge);
                intent.putExtra("payMethodList", payMethod);
                intent.putExtra("payType", payType);
                intent.putExtra("discount", discount);
                intent.putExtra("paymentMethod", "installment");
                intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
                intent.putExtra(Constants.MERID,merchant_id);

                if(discount != null){
                    intent.putExtra("promoCode", promoCode);
                }

                intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
