package com.example.dell.smartpos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.smartpos.CardModule.tradepaypw.SwingCardActivity;


import java.text.DecimalFormat;
import java.util.HashMap;

public class CardPayment_2 extends SwingCardActivity {

    private LinearLayout installmentLayout;

    private TextView txtStep2;
    private TextView txtAmount;
    private TextView txtCurrency;
    private TextView txtMerchantRef;
    private TextView paymentType;
    private TextView txtInstallment;

    boolean surC = false; //surcharge calution function
    boolean mdr_less_equal_0 = false; //mdr less than or equal to 0

    public static String amount = "";
    public static String mdr_amount = "";
    public static String surcharge = "";
    public static String currCode = "";
    public static String currCode1 = "";
    public static String merID = "";
    public static String merchantName = "";
    public static String merRef;
    public static String payType = "";
    public static String payMethodList = "";
    public static String operatorID = "";
    public static String hideSurcharge = "";
    public static String remark = "";
    public static String traceNo;
    public static String batchNo = "";
    public static String terminalID = "";
    String tenure = "";

    HashMap<String, String> map = new HashMap<String, String>();
    ViewPager  cardViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment_2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle(R.string.card_payment);

        //*******************layout init**************************//
        txtStep2 = (TextView) findViewById(R.id.progress_card_text_2);
        txtStep2.setTypeface(null, Typeface.BOLD);
        txtMerchantRef = (TextView) findViewById(R.id.refno);
        txtAmount = (TextView) findViewById(R.id.edtAmount);
        txtCurrency = (TextView) findViewById(R.id.txtCurrCode);
        paymentType = (TextView) findViewById(R.id.paymentType);
        txtInstallment = (TextView) findViewById(R.id.txtInstallment);
        installmentLayout = (LinearLayout) findViewById(R.id.installment);
        cardViewPager = (ViewPager) findViewById(R.id.cardViewPager);
        cardViewPager.setAdapter(new CardPagerAdapter(getSupportFragmentManager()));

        //*******************set data****************************//
        final Intent paymentIntent = getIntent();

        amount = paymentIntent.getStringExtra(Constants.PRE_AMOUNT);
        mdr_amount = paymentIntent.getStringExtra(Constants.PRE_MDR_AMOUNT);
        surcharge = paymentIntent.getStringExtra(Constants.PRE_SURCHARGE);
        currCode = paymentIntent.getStringExtra(Constants.CURRCODE);
        currCode1 = paymentIntent.getStringExtra(Constants.CURRENCY);
        merRef = paymentIntent.getStringExtra(Constants.MERCHANT_REF);
        payType = paymentIntent.getStringExtra(Constants.PAYTYPE);
        hideSurcharge = paymentIntent.getStringExtra(Constants.pref_hideSurcharge);
        merchantName = paymentIntent.getStringExtra(Constants.MERNAME);
        merID = paymentIntent.getStringExtra(Constants.MERID);
        payMethodList = paymentIntent.getStringExtra(Constants.PAYMETHODLIST);
        operatorID = paymentIntent.getStringExtra(Constants.OPERATORID);
//        remark = paymentIntent.getStringExtra(Constants.REMARK);
//        traceNo = paymentIntent.getStringExtra(Constants.TRACE_NO);
//        batchNo = paymentIntent.getStringExtra(Constants.BATCH_NO);
//        terminalID = paymentIntent.getStringExtra(Constants.TERMINAL_ID);

        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        String formatAmount = formatter.format(Double.parseDouble(amount));
        String formatMdrAmount = formatter.format(Double.parseDouble(mdr_amount));
        if (mdr_less_equal_0 || !surC) {
            txtAmount.setText(formatAmount);
        } else {
            txtAmount.setText(formatMdrAmount);
        }

        String formatSurcharge = formatter.format(Double.parseDouble(surcharge));
        txtCurrency.setText(currCode);
        txtMerchantRef.setText(merRef);
        if(payType.equalsIgnoreCase("N")){
            paymentType.setText("Sales");
        }else if(payType.equalsIgnoreCase("H")) {
            paymentType.setText("Authorize");
        }
        
        // Added Phase 2 20191127
        // Start Manual Process
        map.put(Constants.MERID, merID);
        map.put(Constants.MERNAME, merchantName);
        map.put(Constants.MERCHANT_REF, merRef);
        map.put(Constants.AMOUNT, amount);
        map.put(Constants.CURRCODE, currCode1);
        map.put(Constants.PAYTYPE, payType);
        map.put(Constants.OPERATORID, operatorID);
        map.put(Constants.PRE_MDR_AMOUNT, mdr_amount);
        map.put(Constants.PRE_SURCHARGE, surcharge);
        map.put(Constants.pref_hideSurcharge, hideSurcharge);

        // Start SwingCard Process
        initProcess();

/*        if(paymentType != null){
            tenure = paymentIntent.getStringExtra("tenure");
            installmentLayout.setVisibility(View.VISIBLE);
            txtInstallment.setText(tenure);
            setTitle(R.string.installment_menu);

            new Timer().schedule(new TimerTask(){
                public void run() {
                    Intent intent = new Intent(CardPayment_2.this, installmentSuccess.class);
                    intent.putExtra(Constants.CURRCODE, currCode);
                    intent.putExtra("tenure", tenure);
                    intent.putExtra(Constants.MERCHANT_REF, merRef);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
                    startActivity(intent);

                    finish();
                }
            }, 5000 );
        }*/
    }

    private class CardPagerAdapter extends FragmentPagerAdapter {

        public CardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return Card_Insert_Swipe_Tap_Fragment.newInstance(map);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return Card_Manual_Fragment.newInstance(map);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        Intent intent = new Intent(CardPayment_2.this, MainActivity.class);
        startActivity(intent);
        return true;
    }
}
