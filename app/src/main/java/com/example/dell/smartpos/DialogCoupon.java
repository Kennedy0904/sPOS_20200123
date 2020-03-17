package com.example.dell.smartpos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class DialogCoupon extends Activity {

    private Button btnApply;
    private Button btnConfirm;
    private Button btnCancel;

    private EditText txtEditCouponCode;
    private TextView txtAmtCurrCode;
    private TextView txtAmount;
    private TextView txtDiscountCurrCode;
    private TextView txtDiscountAmount;
    private TextView txtAfterCurrCode;
    private TextView txtAfterAmount;

    String currCode = "";
    String amount = "";
    String currCode1 = "";
    String merchantRef = "";
    String hideSurcharge = "";
    String payMethod = "";
    String payType = "";
    String merchant_id = "";
    boolean discount = false;
    String coupon = "";
    String promoCode = "";
    String paymentMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_coupon);

        btnApply = (Button) findViewById(R.id.btnApply);
        btnConfirm = (Button) findViewById(R.id.dialogConfirm);
        btnCancel = (Button) findViewById(R.id.dialogCancel);

        txtEditCouponCode = (EditText) findViewById(R.id.editCouponCode);
        txtAmtCurrCode = (TextView) findViewById(R.id.txtAmtCurrCode);
        txtAmount = (TextView) findViewById(R.id.txtAmt);
        txtDiscountCurrCode = (TextView) findViewById(R.id.txtDiscountCurrCode);
        txtDiscountAmount = (TextView) findViewById(R.id.txtDiscountAmount);
        txtAfterCurrCode = (TextView) findViewById(R.id.txtAfterCurrCode);
        txtAfterAmount = (TextView) findViewById(R.id.txtAfterAmount);


        final Intent paymentIntent = getIntent();
        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        final String amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra("amount")));
        currCode = paymentIntent.getStringExtra("currCode");
        currCode1 = paymentIntent.getStringExtra(Constants.CURRENCY);
        merchantRef = paymentIntent.getStringExtra(Constants.MERCHANT_REF);
        hideSurcharge = paymentIntent.getStringExtra(Constants.pref_hideSurcharge);
        payMethod = paymentIntent.getStringExtra(Constants.PAYMETHODLIST);
        payType = paymentIntent.getStringExtra(Constants.PAYTYPE);
        merchant_id = paymentIntent.getStringExtra(Constants.MERID);
        coupon = paymentIntent.getStringExtra("discount");
        paymentMethod = paymentIntent.getStringExtra("paymentMethod");


        System.out.println("Coupon Testing "+coupon);

        if(coupon != null){
            promoCode = paymentIntent.getStringExtra("promoCode");
            btnConfirm.setText("Remove");
            txtEditCouponCode.setText(promoCode);

            Double amt = Double.parseDouble(paymentIntent.getStringExtra("amount"));
            double discountAmt;
            String discountAmntString;
            double afterDiscountAmnt;
            String afterDiscountAmountString;

            if(promoCode.equals("PROMO20")){
                discountAmt = amt * 20/100;
                discountAmntString = formatter.format(Double.parseDouble(String.valueOf(discountAmt)));
                afterDiscountAmnt = amt - discountAmt;
                afterDiscountAmountString = formatter.format(Double.parseDouble(String.valueOf(afterDiscountAmnt)));
                txtDiscountAmount.setText(discountAmntString);
                txtAfterAmount.setText(afterDiscountAmountString);
                discount = true;
            }else if(promoCode.equals("PROMO50")){
                discountAmt = amt * 50/100;
                discountAmntString = formatter.format(Double.parseDouble(String.valueOf(discountAmt)));
                afterDiscountAmnt = amt - discountAmt;
                afterDiscountAmountString = formatter.format(Double.parseDouble(String.valueOf(afterDiscountAmnt)));
                txtDiscountAmount.setText(discountAmntString);
                txtAfterAmount.setText(afterDiscountAmountString);
                discount = true;
            }

        }


        txtAmtCurrCode.setText(currCode);
        txtDiscountCurrCode.setText(currCode);
        txtAfterCurrCode.setText(currCode);

        txtAmount.setText(amount);

        btnApply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
               String promoCode = txtEditCouponCode.getText().toString();
               DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
               Double amt = Double.parseDouble(amount);
               double discountAmt;
               String discountAmntString;
               double afterDiscountAmnt;
               String afterDiscountAmountString;

               if(promoCode.equals("PROMO20")){
                    discountAmt = amt * 20/100;
                    discountAmntString = formatter.format(Double.parseDouble(String.valueOf(discountAmt)));
                    afterDiscountAmnt = amt - discountAmt;
                    afterDiscountAmountString = formatter.format(Double.parseDouble(String.valueOf(afterDiscountAmnt)));
                    txtDiscountAmount.setText(discountAmntString);
                    txtAfterAmount.setText(afterDiscountAmountString);
                    discount = true;
                    coupon = null;
                    btnConfirm.setText("Confirm");

               }else if(promoCode.equals("PROMO50")){
                   discountAmt = amt * 50/100;
                   discountAmntString = formatter.format(Double.parseDouble(String.valueOf(discountAmt)));
                   afterDiscountAmnt = amt - discountAmt;
                   afterDiscountAmountString = formatter.format(Double.parseDouble(String.valueOf(afterDiscountAmnt)));
                   txtDiscountAmount.setText(discountAmntString);
                   txtAfterAmount.setText(afterDiscountAmountString);
                   discount = true;
                   coupon = null;
                   btnConfirm.setText("Confirm");
               }else{
                   Toast.makeText(getApplicationContext(), "Invalid coupon code.", Toast.LENGTH_LONG).show();
               }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(coupon != null){

                    if(paymentMethod.equals("scanqr")){
                        Intent intent = new Intent(DialogCoupon.this, ScanQRPayment_2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      /*  intent.putExtra("CurrCode", currCode);
                        intent.putExtra("currCode1", currCode1);
                        intent.putExtra(Constants.MERCHANT_REF, merchantRef);
                        intent.putExtra("hideSurcharge", hideSurcharge);
                        intent.putExtra("payMethodList", payMethod);
                        intent.putExtra("payType", payType);
                        intent.putExtra("pre_Amount", amount);
                        intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_MDR_AMOUNT));*/

                        intent.putExtra(Constants.MERCHANT_REF, merchantRef);
                        intent.putExtra(Constants.PRE_AMOUNT, amount);
                        intent.putExtra(Constants.PRE_MDR_AMOUNT, amount);
                        intent.putExtra(Constants.PRE_MDR, getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra(Constants.PRE_SURCHARGE, getIntent().getStringExtra(Constants.PRE_SURCHARGE));
                        intent.putExtra(Constants.CURRCODE, currCode);
                        intent.putExtra(Constants.CURRENCY, currCode1);
                        intent.putExtra(Constants.MERNAME, getIntent().getStringExtra(Constants.MERNAME));
                        intent.putExtra(Constants.MERID, getIntent().getStringExtra(Constants.MERID));
                        intent.putExtra(Constants.PAYMETHODLIST, payMethod);
                        intent.putExtra(Constants.pref_Rate, getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra(Constants.pref_hideSurcharge, hideSurcharge);
                        intent.putExtra(Constants.PAYTYPE, payType);

                        intent.putExtra(Constants.MERID,merchant_id);
                        startActivityForResult(intent, 1);
                    }else if(paymentMethod.equals("installment")){
                        Intent intent = new Intent(DialogCoupon.this, InstallmentPayment2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(Constants.MERCHANT_REF, merchantRef);
                        intent.putExtra(Constants.PRE_AMOUNT, amount);
                        intent.putExtra(Constants.PRE_MDR_AMOUNT, amount);
                        intent.putExtra(Constants.PRE_MDR, getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra(Constants.PRE_SURCHARGE, getIntent().getStringExtra(Constants.PRE_SURCHARGE));
                        intent.putExtra(Constants.CURRCODE, currCode);
                        intent.putExtra(Constants.CURRENCY, currCode1);
                        intent.putExtra(Constants.MERNAME, getIntent().getStringExtra(Constants.MERNAME));
                        intent.putExtra(Constants.MERID, getIntent().getStringExtra(Constants.MERID));
                        intent.putExtra(Constants.PAYMETHODLIST, payMethod);
                        intent.putExtra(Constants.pref_Rate, getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra(Constants.pref_hideSurcharge, hideSurcharge);
                        intent.putExtra(Constants.PAYTYPE, payType);

                        intent.putExtra(Constants.MERID,merchant_id);
                        startActivityForResult(intent, 1);
                    }else{
                        Intent intent = new Intent(DialogCoupon.this, PresentQRPayment_2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(Constants.MERCHANT_REF, merchantRef);
                        intent.putExtra(Constants.PRE_AMOUNT, amount);
                        intent.putExtra(Constants.PRE_MDR_AMOUNT, amount);
                        intent.putExtra(Constants.PRE_MDR, getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra(Constants.PRE_SURCHARGE, getIntent().getStringExtra(Constants.PRE_SURCHARGE));
                        intent.putExtra(Constants.CURRCODE, currCode);
                        intent.putExtra(Constants.CURRENCY, currCode1);
                        intent.putExtra(Constants.MERNAME, getIntent().getStringExtra(Constants.MERNAME));
                        intent.putExtra(Constants.MERID, getIntent().getStringExtra(Constants.MERID));
                        intent.putExtra(Constants.PAYMETHODLIST, payMethod);
                        intent.putExtra(Constants.pref_Rate, getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra(Constants.pref_hideSurcharge, hideSurcharge);
                        intent.putExtra(Constants.PAYTYPE, payType);

                        intent.putExtra(Constants.MERID,merchant_id);
                        startActivityForResult(intent, 1);
                    }

                }else{

                    if(paymentMethod.equals("scanqr")){
                        Intent intent = new Intent(DialogCoupon.this, ScanQRPayment_2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(Constants.MERCHANT_REF, merchantRef);
                        intent.putExtra(Constants.PRE_AMOUNT, amount);
                        intent.putExtra(Constants.PRE_MDR_AMOUNT, amount);
                        intent.putExtra(Constants.PRE_MDR, getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra(Constants.PRE_SURCHARGE, getIntent().getStringExtra(Constants.PRE_SURCHARGE));
                        intent.putExtra(Constants.CURRCODE, currCode);
                        intent.putExtra(Constants.CURRENCY, currCode1);
                        intent.putExtra(Constants.MERNAME, getIntent().getStringExtra(Constants.MERNAME));
                        intent.putExtra(Constants.MERID, getIntent().getStringExtra(Constants.MERID));
                        intent.putExtra(Constants.PAYMETHODLIST, payMethod);
                        intent.putExtra(Constants.pref_Rate, getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra(Constants.pref_hideSurcharge, hideSurcharge);
                        intent.putExtra(Constants.PAYTYPE, payType);

                        if(discount){
                            intent.putExtra("coupon", "true");
                            intent.putExtra("promoCode", txtEditCouponCode.getText().toString());
                            intent.putExtra("afterDiscountAmount", txtAfterAmount.getText().toString());
                        }

                        intent.putExtra(Constants.MERID,merchant_id);
                        startActivityForResult(intent, 1);
                    } else if (paymentMethod.equals("installment")){
                        Intent intent = new Intent(DialogCoupon.this, InstallmentPayment2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(Constants.MERCHANT_REF, merchantRef);
                        intent.putExtra(Constants.PRE_AMOUNT, amount);
                        intent.putExtra(Constants.PRE_MDR_AMOUNT, amount);
                        intent.putExtra(Constants.PRE_MDR, getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra(Constants.PRE_SURCHARGE, getIntent().getStringExtra(Constants.PRE_SURCHARGE));
                        intent.putExtra(Constants.CURRCODE, currCode);
                        intent.putExtra(Constants.CURRENCY, currCode1);
                        intent.putExtra(Constants.MERNAME, getIntent().getStringExtra(Constants.MERNAME));
                        intent.putExtra(Constants.MERID, getIntent().getStringExtra(Constants.MERID));
                        intent.putExtra(Constants.PAYMETHODLIST, payMethod);
                        intent.putExtra(Constants.pref_Rate, getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra(Constants.pref_hideSurcharge, hideSurcharge);
                        intent.putExtra(Constants.PAYTYPE, payType);

                        if(discount){
                            intent.putExtra("coupon", "true");
                            intent.putExtra("promoCode", txtEditCouponCode.getText().toString());
                            intent.putExtra("afterDiscountAmount", txtAfterAmount.getText().toString());
                        }

                        intent.putExtra(Constants.MERID,merchant_id);
                        startActivityForResult(intent, 1);
                    }else{
                        Intent intent = new Intent(DialogCoupon.this, PresentQRPayment_2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(Constants.MERCHANT_REF, merchantRef);
                        intent.putExtra(Constants.PRE_AMOUNT, amount);
                        intent.putExtra(Constants.PRE_MDR_AMOUNT, amount);
                        intent.putExtra(Constants.PRE_MDR, getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra(Constants.PRE_SURCHARGE, getIntent().getStringExtra(Constants.PRE_SURCHARGE));
                        intent.putExtra(Constants.CURRCODE, currCode);
                        intent.putExtra(Constants.CURRENCY, currCode1);
                        intent.putExtra(Constants.MERNAME, getIntent().getStringExtra(Constants.MERNAME));
                        intent.putExtra(Constants.MERID, getIntent().getStringExtra(Constants.MERID));
                        intent.putExtra(Constants.PAYMETHODLIST, payMethod);
                        intent.putExtra(Constants.pref_Rate, getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra(Constants.pref_hideSurcharge, hideSurcharge);
                        intent.putExtra(Constants.PAYTYPE, payType);

                        if(discount){
                            intent.putExtra("coupon", "true");
                            intent.putExtra("promoCode", txtEditCouponCode.getText().toString());
                            intent.putExtra("afterDiscountAmount", txtAfterAmount.getText().toString());
                        }

                        intent.putExtra(Constants.MERID,merchant_id);
                        startActivityForResult(intent, 1);
                    }
                }

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 finish();
            }
        });

    }
}
