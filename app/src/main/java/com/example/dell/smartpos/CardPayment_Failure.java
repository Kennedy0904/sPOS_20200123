package com.example.dell.smartpos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.smartpos.Scanner.Fail_word;

import static android.view.View.VISIBLE;

public class CardPayment_Failure extends AppCompatActivity {

    LinearLayout invoiceNoLayout;
    private TextView txtStep1, txtStep2, txtStep3, txtStep4;
    private Button btnMainMenu, btnTryAgain;



    private String success_code;
    private String merchantId;
    private String merName;
    private String payRef;
    private String merRef;
    private String traceNo;
    private String amount;
    private String Surcharge;
    private String Currcode;
    private String errMsg;
    private String errAdvise;
    private String merrequestamt;
    private String payType;
    private String pMethod;
    private String operatorId;
    private String cardNo;
    private String hideSurcharge;
    private String batchNo;
    private String terminalID;
    private String invoiceNo;
    private String payBankId;

    private TextView tvMerName;
    private TextView tvPayRef;
    private TextView tvMerRef;
    private TextView tvTerminalID;
    private TextView tvTraceNo;
    private TextView tvBatchNo;
    private TextView tvCardNo;
    private TextView tvAmount;
    private TextView tvPayMethod;
    private TextView tvCurrcode;
    private TextView tvFailMsg;
    private TextView tvFailAdvice;
    private TextView tvStatus;
    private TextView tvInvoiceNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment__failure);
        setTitle(R.string.card_payment);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        txtStep1 = (TextView) findViewById(R.id.progress_card_text_1);
        txtStep2 = (TextView) findViewById(R.id.progress_card_text_2);
        txtStep3 = (TextView) findViewById(R.id.progress_card_text_3);
        txtStep4 = (TextView) findViewById(R.id.progress_card_text_4);

        txtStep1.setTypeface(null, Typeface.BOLD);
        txtStep2.setTypeface(null, Typeface.BOLD);
        txtStep3.setTypeface(null, Typeface.BOLD);
        txtStep4.setTypeface(null, Typeface.BOLD);

        Intent intent = getIntent();
        merchantId = intent.getStringExtra(Constants.MERID) == null ? "" : intent.getStringExtra(Constants.MERID);
        merName = intent.getStringExtra(Constants.MERNAME) == null ? "" : intent.getStringExtra(Constants.MERNAME);
        success_code = intent.getStringExtra(Constants.SUCCESS_CODE) == null ? "" : intent.getStringExtra(Constants.SUCCESS_CODE);
        payRef = intent.getStringExtra(Constants.PAYREF) == null ? "" : intent.getStringExtra(Constants.PAYREF);
        merRef = intent.getStringExtra(Constants.MERCHANT_REF) == null ? "" : intent.getStringExtra(Constants.MERCHANT_REF);
        traceNo = intent.getStringExtra(Constants.TRACE_NO) == null ? "" : intent.getStringExtra(Constants.TRACE_NO);
        amount = intent.getStringExtra(Constants.AMOUNT) == null ? "" : intent.getStringExtra(Constants.AMOUNT);
        merrequestamt = intent.getStringExtra(Constants.MERREQUESTAMT) == null ? "" : intent.getStringExtra(Constants.MERREQUESTAMT);
        Surcharge = intent.getStringExtra(Constants.SURCHARGE) == null ? "" : intent.getStringExtra(Constants.SURCHARGE);
        Currcode = intent.getStringExtra(Constants.CURRCODE) == null ? "" : intent.getStringExtra(Constants.CURRCODE);
        errMsg = intent.getStringExtra(Constants.ERRMSG) == null ? "" : Fail_word.failreason(intent.getStringExtra(Constants.ERRMSG), CardPayment_Failure.this);
        payType = intent.getStringExtra(Constants.PAYTYPE) == null ? "" : intent.getStringExtra(Constants.PAYTYPE);
        pMethod = intent.getStringExtra(Constants.PAYMETHOD) == null ? "" : intent.getStringExtra(Constants.PAYMETHOD);
        operatorId = intent.getStringExtra(Constants.OPERATORID) == null ? "" : intent.getStringExtra(Constants.OPERATORID);
        cardNo = intent.getStringExtra(Constants.CARDNO) == null ? "" : intent.getStringExtra(Constants.CARDNO);
        hideSurcharge = intent.getStringExtra(Constants.pref_hideSurcharge) == null ? "F" : intent.getStringExtra(Constants.pref_hideSurcharge);
        errAdvise = intent.getStringExtra(Constants.ERRMSG) == null ? "" : Fail_word.failadvise(intent.getStringExtra(Constants.ERRMSG), CardPayment_Failure.this);
        batchNo = intent.getStringExtra(Constants.BATCH_NO) == null ? "" : intent.getStringExtra(Constants.BATCH_NO);
        terminalID = intent.getStringExtra(Constants.TERMINAL_ID) == null ? "" : intent.getStringExtra(Constants.TERMINAL_ID);
        invoiceNo = intent.getStringExtra(Constants.INVOICE_NO) == null ? "" : intent.getStringExtra(Constants.INVOICE_NO);
        payBankId = intent.getStringExtra(Constants.PAYBANKID) == null ? "" : intent.getStringExtra(Constants.PAYBANKID);

        //        System.out.println("CardPaymentFailed---" + "success_code:" + success_code + "," + "merchantId:" + merchantId + "," + "merName:" + merName + "," + "merchant_ref_no:" + merchant_ref_no + "," + "payRef:" + payRef + "," + "amount:" + amount + "," + "Surcharge:" + Surcharge + "," + "Currcode:" + Currcode + "," + "TXTime:" + TXTime + "," + "errMsg:" + errMsg + "," + "errAdvise:" + errAdvise + "," + "merrequestamt:" + merrequestamt + "," + "payType:" + payType + "," + "pMethod:" + pMethod + "," + "operatorId:" + operatorId + "," + "cardNo:" + cardNo + "," + "hideSurcharge:" + hideSurcharge);

        invoiceNoLayout = (LinearLayout) findViewById(R.id.invoiceNoLayout);

        btnMainMenu = (Button) findViewById(R.id.btnMainMenu);

        tvMerName = (TextView) findViewById(R.id.cardMerName);
        tvPayRef = (TextView) findViewById(R.id.cardPayRef);
        tvMerRef = (TextView) findViewById(R.id.cardMerRef);
        tvTerminalID = (TextView) findViewById(R.id.cardTerminalID);
        tvTraceNo = (TextView) findViewById(R.id.cardTraceNo);
        tvBatchNo = (TextView) findViewById(R.id.cardBatchNo);
        tvCardNo = (TextView) findViewById(R.id.cardNo);
        tvAmount = (TextView) findViewById(R.id.cardAmount);
        tvCurrcode = (TextView) findViewById(R.id.cardCurrcode);
        tvPayMethod = (TextView) findViewById(R.id.cardPaymentMethod);
        tvStatus = (TextView) findViewById(R.id.cardPaymentStatus);
        tvFailMsg = (TextView) findViewById(R.id.cardfailreason);
        tvFailAdvice = (TextView) findViewById(R.id.cardfailadvise);
        tvInvoiceNo = (TextView) findViewById(R.id.cardInvoiceNo);


        tvMerName.setText(merName);
        tvPayRef.setText(payRef);
        tvMerRef.setText(merRef);
        tvTerminalID.setText(terminalID);
        tvTraceNo.setText(traceNo);
        tvBatchNo.setText(batchNo);
        tvCardNo.setText(cardNo);
        tvFailMsg.setText(errMsg);
        tvFailAdvice.setText(errAdvise);
        tvAmount.setText(amount);
        tvCurrcode.setText(Currcode);
        tvPayMethod.setText(pMethod);
        tvStatus.setText(getString(R.string.rejected));

        /** Control the Invoice No Layout **/
        if (payBankId.equalsIgnoreCase(Constants.FIRST_DATA)) {
            invoiceNoLayout.setVisibility(VISIBLE);
            tvInvoiceNo.setText(invoiceNo);
        }

        btnMainMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(CardPayment_Failure.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        });

        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                finish();
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        Intent intent = new Intent(CardPayment_Failure.this, MainActivity.class);
        startActivity(intent);
        return true;
    }
}
