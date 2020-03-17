package com.example.dell.smartpos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PresentQRPayment_2 extends AppCompatActivity {

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

    boolean mV = false;
    boolean oP = false;
    boolean pP = false;
    boolean mW = false;
    boolean pme = false;
    boolean pnow = false;
    boolean bharat = false;
    boolean b = false;
    boolean gP = false;

    //private EditText remark = null;
    public Integer ischecked = 0;
    String currCode = "";
    String currCode1 = "";
    String merId = "";
    String merchantName = "";
    String payType = "";
    String payMethod = "";
    String operatorId = "";
    String hideSurcharge = "";
    String discount = "";
    String promoCode = "";

    String[] payMethod_MVISA = {"MVISA"};
    String[] payMethod_OLEPAY = {"OEPAYOFFL"};
    String[] payMethod_PROMPTPAY = {"PROMPTPAYOFFL"};
    String[] payMethod_MASTERWALLET = {"MASTERPASS"};
    String[] payMethod_BOOST = {"BOOSTOFFL"};
    String[] payMethod_GrabPay = {"GRABPAYOFFL"};

    Toast numberpadtoast = null;

    public static final int SIGNATURE_ACTIVITY = 1;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_qrpayment);

        final Intent paymentIntent = getIntent();
        merId = paymentIntent.getStringExtra(Constants.MERID);
        discount = paymentIntent.getStringExtra("coupon");

        setTitle(R.string.present_qr);

        step1 = (TextView) findViewById(R.id.progress_step_1);
        step2 = (TextView) findViewById(R.id.progress_step_2);
        step3 = (TextView) findViewById(R.id.progress_step_3);
        step4 = (TextView) findViewById(R.id.progress_step_4);

        step1.setText(R.string.enter_amount);
        step2.setText(R.string.select_qr_payment);
        step3.setText(R.string.generate_qr);
        step4.setText(R.string.payment_result);

        step2.setTypeface(null, Typeface.BOLD);

        //*******************layout init**************************//
        //----merchant textview start----//
        MerchantRef = (TextView) findViewById(R.id.edtMerchantRef);
        txtAmount = (TextView) findViewById(R.id.edtAmount);
        txtCurrency = (TextView) findViewById(R.id.transactionreport_currCode);
        couponApplied = (ImageView) findViewById(R.id.ic_coupon);

        //*******************set data****************************//


        if (mdr_less_equal_0 || !surC) {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            String amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra(Constants.PRE_AMOUNT)));
            txtAmount.setText(amount);
        } else {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            String amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra(Constants.PRE_MDR_AMOUNT)));
            txtAmount.setText(amount);
        }


        if (discount != null) {
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
        merchantName = paymentIntent.getStringExtra(Constants.MERNAME);
        payMethod = paymentIntent.getStringExtra(Constants.PAYMETHODLIST);
        operatorId = paymentIntent.getStringExtra(Constants.OPERATORID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        mV = containstr(payMethod, payMethod_MVISA);
//        ACN = containstr(payMethod, payMethod_ALIPAYCN);
        oP = containstr(payMethod, payMethod_OLEPAY);
        pP = containstr(payMethod, payMethod_PROMPTPAY);
        mW = containstr(payMethod, payMethod_MASTERWALLET);
        b = containstr(payMethod, payMethod_BOOST);
        gP = containstr(payMethod, payMethod_GrabPay);

        if (merId.equals("560200335") || merId.equals("560200303")) {
            pP = true;
            mV = true;
            oP = true;
            gP = true;
            mW = true;
            pme = true;
            pnow = true;
            bharat = true;
            b = true;
        }

        ArrayList<QRItem> list = new ArrayList<>();
        QRItem qrBharat = new QRItem(R.drawable.ic_bharatpay, getString(R.string.bharatpay), bharat);
        QRItem qrBoost = new QRItem(R.drawable.ic_boost, getString(R.string.boost), b);
        QRItem qrGrabPay = new QRItem(R.drawable.ic_grabpay, getString(R.string.grabpay), gP);
        QRItem qrMasterWallet = new QRItem(R.drawable.ic_masterwallet, getString(R.string.masterwallet), mW);
        QRItem qrmVisa = new QRItem(R.drawable.ic_mvisa, getString(R.string.mvisa), mV);
        QRItem qrOlePay = new QRItem(R.drawable.ic_olepay, getString(R.string.olepay), oP);
        QRItem qrPayme = new QRItem(R.drawable.ic_payme, getString(R.string.payme), pme);
        QRItem qrPayNow = new QRItem(R.drawable.ic_paynow, getString(R.string.paynow), pnow);
        QRItem qrPromptPay = new QRItem(R.drawable.ic_promptpay, getString(R.string.promptpay), pP);

        if (merId.equals("560200335") || merId.equals("560200303")) {
            list.add(qrBharat);
            list.add(qrBoost);
            list.add(qrGrabPay);
            list.add(qrMasterWallet);
            list.add(qrmVisa);
            list.add(qrOlePay);
            list.add(qrPayme);
            list.add(qrPayNow);
            list.add(qrPromptPay);
        } else if (merId.substring(0, 2).equals("85")) {
            // Malaysia Country
            list.add(qrBoost);
            list.add(qrGrabPay);
        } else if (merId.substring(0, 2).equals("76")) {
            // Thailand Country
            list.add(qrGrabPay);
            list.add(qrPromptPay);
        } else if (merId.substring(0, 2).equals("88")) {
            // Hong Kong Country
            list.add(qrOlePay);
        } else if (merId.substring(0, 2).equals("12")) {
            // Singapore Country
            list.add(qrGrabPay);
        } else if (merId.substring(0, 2).equals("18")) {
            // Philippines Country
            list.add(qrGrabPay);
        }

        if (list.isEmpty()) {
            showTextToast(getString(R.string.paymethod_not_available));
        }

        recyclerView = (RecyclerView) findViewById(R.id.grid);
        recyclerView.setAdapter(new QRAdapter(list, new QRAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(QRItem item) {

                if (item.isActive()) {
                    Intent intent = new Intent();
                    intent.setClass(PresentQRPayment_2.this, PresentQRPayment_3.class);

                    if (mdr_less_equal_0 || !surC) {

                        intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
                        intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
                        intent.putExtra("Surcharge", "0");
                        intent.putExtra("Mdr", "0");
                        intent.putExtra("surC", "F");
                    } else {

                        intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_MDR_AMOUNT));
                        intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
                        intent.putExtra("Surcharge", getIntent().getStringExtra(Constants.PRE_SURCHARGE));
                        intent.putExtra("Mdr", getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra("surC", "T");
                    }

                    intent.putExtra("currCode", currCode);
                    intent.putExtra("currCode1", currCode1);
                    intent.putExtra(Constants.PAYTYPE, getIntent().getStringExtra(Constants.PAYTYPE));
                    intent.putExtra("MerchantRef", MerchantRef.getText().toString());
                    intent.putExtra("hideSurcharge", hideSurcharge);
                    intent.putExtra(Constants.MERNAME, merchantName);
                    intent.putExtra(Constants.MERID, merId);
                    intent.putExtra("payMethod", item.getQrName());
                    intent.putExtra(Constants.OPERATORID, operatorId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    showTextToast(getString(R.string.paymethod_not_support));
                }

            }
        }));
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));



//        if (merId.equals("560200335") || merId.equals("560200303") || merId.equals("85003895")) {
//            mVisa.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mV) {
//                        Intent intent = new Intent();
//                        intent.setClass(PresentQRPayment_2.this, PresentQRPayment_3.class);
//
//                        if (mdr_less_equal_0 || !surC) {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", "0");
//                            intent.putExtra("Mdr", "0");
//                            intent.putExtra("surC", "F");
//                        } else {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_MDR_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", getIntent().getStringExtra(Constants.PRE_SURCHARGE));
//                            intent.putExtra("Mdr", getIntent().getStringExtra(Constants.PRE_MDR));
//                            intent.putExtra("surC", "T");
//                        }
//
//                        intent.putExtra("currCode", currCode);
//                        intent.putExtra("currCode1", currCode1);
//                        intent.putExtra(Constants.PAYTYPE, getIntent().getStringExtra(Constants.PAYTYPE));
//                        intent.putExtra("MerchantRef", MerchantRef.getText().toString());
//                        intent.putExtra("hideSurcharge", hideSurcharge);
//                        intent.putExtra(Constants.MERNAME, merchantName);
//                        intent.putExtra(Constants.MERID, merId);
//                        intent.putExtra("payMethod", "mVisa");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    } else {
//                        showTextToast(getString(R.string.paymethod_not_support));
//                    }
//                }
//            });
//
//            promptPay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (pP) {
//                        Intent intent = new Intent();
//                        intent.setClass(PresentQRPayment_2.this, PresentQRPayment_3.class);
//
//                        if (mdr_less_equal_0 || !surC) {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", "0");
//                            intent.putExtra("Mdr", "0");
//                            intent.putExtra("surC", "F");
//                        } else {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_MDR_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", getIntent().getStringExtra(Constants.PRE_SURCHARGE));
//                            intent.putExtra("Mdr", getIntent().getStringExtra(Constants.PRE_MDR));
//                            intent.putExtra("surC", "T");
//                        }
//
//                        intent.putExtra("currCode", currCode);
//                        intent.putExtra("currCode1", currCode1);
//                        intent.putExtra(Constants.PAYTYPE, getIntent().getStringExtra(Constants.PAYTYPE));
//                        intent.putExtra("MerchantRef", MerchantRef.getText().toString());
//                        intent.putExtra("hideSurcharge", hideSurcharge);
//                        intent.putExtra(Constants.MERNAME, merchantName);
//                        intent.putExtra(Constants.MERID, merId);
//                        intent.putExtra("payMethod", "PROMPTPAYOFFL");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                        startActivity(intent);
//                    } else {
//                        showTextToast(getString(R.string.paymethod_not_support));
//                    }
//                }
//            });
//
//            masterwallet.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mW) {
//                        Intent intent = new Intent();
//                        intent.setClass(PresentQRPayment_2.this, PresentQRPayment_3.class);
//
//                        if (mdr_less_equal_0 || !surC) {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", "0");
//                            intent.putExtra("Mdr", "0");
//                            intent.putExtra("surC", "F");
//                        } else {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_MDR_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", getIntent().getStringExtra(Constants.PRE_SURCHARGE));
//                            intent.putExtra("Mdr", getIntent().getStringExtra(Constants.PRE_MDR));
//                            intent.putExtra("surC", "T");
//                        }
//
//                        intent.putExtra("currCode", currCode);
//                        intent.putExtra("currCode1", currCode1);
//                        intent.putExtra(Constants.PAYTYPE, getIntent().getStringExtra(Constants.PAYTYPE));
//                        intent.putExtra("MerchantRef", MerchantRef.getText().toString());
//                        intent.putExtra("hideSurcharge", hideSurcharge);
//                        intent.putExtra(Constants.MERNAME, merchantName);
//                        intent.putExtra(Constants.MERID, merId);
//                        intent.putExtra("payMethod", "MasterWallet");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        if (mdr_less_equal_0 || !surC) {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", "0");
//                            intent.putExtra("Mdr", "0");
//                            intent.putExtra("surC", "F");
//                        } else {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_MDR_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", getIntent().getStringExtra(Constants.PRE_SURCHARGE));
//                            intent.putExtra("Mdr", getIntent().getStringExtra(Constants.PRE_MDR));
//                            intent.putExtra("surC", "T");
//                        }
//
//                        intent.putExtra("currCode", currCode);
//                        intent.putExtra("currCode1", currCode1);
//                        intent.putExtra(Constants.PAYTYPE, getIntent().getStringExtra(Constants.PAYTYPE));
//                        intent.putExtra("MerchantRef", MerchantRef.getText().toString());
//                        intent.putExtra("hideSurcharge", hideSurcharge);
//                        intent.putExtra(Constants.MERNAME, merchantName);
//                        intent.putExtra(Constants.MERID, merId);
//                        intent.putExtra("payMethod", "masterWallet");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                        startActivity(intent);
//                    } else {
//                        showTextToast(getString(R.string.paymethod_not_support));
//                    }
//                }
//            });
//
//            payme.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (pme) {
//                        Intent intent = new Intent();
//                        intent.setClass(PresentQRPayment_2.this, PresentQRPayment_3.class);
//
//                        if (mdr_less_equal_0 || !surC) {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", "0");
//                            intent.putExtra("Mdr", "0");
//                            intent.putExtra("surC", "F");
//                        } else {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_MDR_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", getIntent().getStringExtra(Constants.PRE_SURCHARGE));
//                            intent.putExtra("Mdr", getIntent().getStringExtra(Constants.PRE_MDR));
//                            intent.putExtra("surC", "T");
//                        }
//
//                        intent.putExtra("currCode", currCode);
//                        intent.putExtra("currCode1", currCode1);
//                        intent.putExtra(Constants.PAYTYPE, getIntent().getStringExtra(Constants.PAYTYPE));
//                        intent.putExtra("MerchantRef", MerchantRef.getText().toString());
//                        intent.putExtra("hideSurcharge", hideSurcharge);
//                        intent.putExtra(Constants.MERNAME, merchantName);
//                        intent.putExtra(Constants.MERID, merId);
//                        intent.putExtra("payMethod", "PayMe");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    } else {
//                        showTextToast(getString(R.string.paymethod_not_support));
//                    }
//                }
//            });
//
//            paynow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (pnow) {
//                        Intent intent = new Intent();
//                        intent.setClass(PresentQRPayment_2.this, PresentQRPayment_3.class);
//
//                        if (mdr_less_equal_0 || !surC) {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", "0");
//                            intent.putExtra("Mdr", "0");
//                            intent.putExtra("surC", "F");
//                        } else {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_MDR_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", getIntent().getStringExtra(Constants.PRE_SURCHARGE));
//                            intent.putExtra("Mdr", getIntent().getStringExtra(Constants.PRE_MDR));
//                            intent.putExtra("surC", "T");
//                        }
//
//                        intent.putExtra("currCode", currCode);
//                        intent.putExtra("currCode1", currCode1);
//                        intent.putExtra(Constants.PAYTYPE, getIntent().getStringExtra(Constants.PAYTYPE));
//                        intent.putExtra("MerchantRef", MerchantRef.getText().toString());
//                        intent.putExtra("hideSurcharge", hideSurcharge);
//                        intent.putExtra(Constants.MERNAME, merchantName);
//                        intent.putExtra(Constants.MERID, merId);
//                        intent.putExtra("payMethod", "PayNow");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    } else {
//                        showTextToast(getString(R.string.paymethod_not_support));
//                    }
//                }
//            });
//
//            bharatpay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (bharat) {
//                        Intent intent = new Intent();
//                        intent.setClass(PresentQRPayment_2.this, PresentQRPayment_3.class);
//
//                        if (mdr_less_equal_0 || !surC) {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", "0");
//                            intent.putExtra("Mdr", "0");
//                            intent.putExtra("surC", "F");
//                        } else {
//
//                            intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_MDR_AMOUNT));
//                            intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                            intent.putExtra("Surcharge", getIntent().getStringExtra(Constants.PRE_SURCHARGE));
//                            intent.putExtra("Mdr", getIntent().getStringExtra(Constants.PRE_MDR));
//                            intent.putExtra("surC", "T");
//                        }
//
//                        intent.putExtra("currCode", currCode);
//                        intent.putExtra("currCode1", currCode1);
//                        intent.putExtra(Constants.PAYTYPE, getIntent().getStringExtra(Constants.PAYTYPE));
//                        intent.putExtra("MerchantRef", MerchantRef.getText().toString());
//                        intent.putExtra("hideSurcharge", hideSurcharge);
//                        intent.putExtra(Constants.MERNAME, merchantName);
//                        intent.putExtra(Constants.MERID, merId);
//                        intent.putExtra("payMethod", "BharatPay");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    } else {
//                        showTextToast(getString(R.string.paymethod_not_support));
//                    }
//                }
//            });
//
//            coupon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(PresentQRPayment_2.this, DialogCoupon.class);
//                    intent.putExtra("currCode", currCode);
//                    intent.putExtra("currCode1", currCode1);
//                    intent.putExtra(Constants.PAYTYPE, getIntent().getStringExtra(Constants.PAYTYPE));
//                    intent.putExtra(Constants.MERCHANT_REF, MerchantRef.getText().toString());
//                    intent.putExtra("hideSurcharge", hideSurcharge);
//                    intent.putExtra("payMethodList", payMethod);
//                    intent.putExtra("payType", payType);
//                    intent.putExtra("discount", discount);
//                    intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                    intent.putExtra(Constants.MERID,merId);
//                    intent.putExtra("paymentMethod", "presentQr");
//
//                    if(discount != null){
//                        intent.putExtra("promoCode", promoCode);
//                    }
//
//                    intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                    startActivityForResult(intent, 1);
//                }
//            });
//        } else {
//            if (merId.substring(0, 3).equals("850")) {
//                boost.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (b) {
//                            Intent intent = new Intent();
//                            intent.setClass(PresentQRPayment_2.this, PresentQRPayment_3.class);
//
//                            if (mdr_less_equal_0 || !surC) {
//
//                                intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                                intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                                intent.putExtra("Surcharge", "0");
//                                intent.putExtra("Mdr", "0");
//                                intent.putExtra("surC", "F");
//                            } else {
//
//                                intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_MDR_AMOUNT));
//                                intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
//                                intent.putExtra("Surcharge", getIntent().getStringExtra(Constants.PRE_SURCHARGE));
//                                intent.putExtra("Mdr", getIntent().getStringExtra(Constants.PRE_MDR));
//                                intent.putExtra("surC", "T");
//                            }
//
//                            intent.putExtra("currCode", currCode);
//                            intent.putExtra("currCode1", currCode1);
//                            intent.putExtra(Constants.PAYTYPE, getIntent().getStringExtra(Constants.PAYTYPE));
//                            intent.putExtra("MerchantRef", MerchantRef.getText().toString());
//                            intent.putExtra("hideSurcharge", hideSurcharge);
//                            intent.putExtra(Constants.MERNAME, merchantName);
//                            intent.putExtra(Constants.MERID, merId);
//                            intent.putExtra(Constants.PAYMETHOD, pMethodBOOST);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                        } else {
//                            showTextToast(getString(R.string.paymethod_not_support));
//                        }
//                    }
//                });
//            }
//        }


    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public String getPrefPayGate() {
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate, Constants.default_paygate);
        return prefpaygate;
    }

    private void showTextToast(String msg) {
        if (numberpadtoast == null) {
            numberpadtoast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            numberpadtoast.setText(msg);
        }
        numberpadtoast.show();
    }

    public void incMerRef() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("refCounter", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("refCounter", curCounter += 1);
        edit.commit();
    }

    //for findind which payMethod the account has
    public static boolean containstr(String str, String[] s) {
        boolean flag = false;
        for (int i = 0; i < s.length; i++) {
            if (str.contains(s[i])) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String autoMerRef = prefsettings.getString(Constants.pref_MerRef, "");

        if (autoMerRef.equals(Constants.MERREF_AUTO)) {
            MerchantRef.setText(autoRef());
        }
    }

    private String autoRef() {
        //=====KM LIEW 25/05/2018====
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("refCounter", 0);

        int runningNum = curCounter + 1;
        int length = String.valueOf(curCounter).length();
        String digit = "000000";
        String MerchantRef = digit.substring(0, digit.length() - length) + runningNum;

        return MerchantRef;
        //============================//>
    }

    private List<String> getSampleDataSet() {
        List strings = new ArrayList();
        strings.add("one");
        strings.add("two");
        strings.add("three");
        strings.add("four");
        strings.add("five");
        strings.add("six");

        return strings;
    }
}
