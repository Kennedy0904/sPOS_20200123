package com.example.dell.smartpos;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;


import com.example.dell.smartpos.CardModule.tradepaypw.TradeResult;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class CustomConfirmationDialog extends Dialog {

    private HashMap<String, String> map;

    String amount;
    String currCode;
    String currency;
    String pMethod;
    String merRef;
    String merID;
    String payType;
    String operator;
    String merName;
    String epMonth;
    String epYear;
    String cardNo;
    String payMethod;

    private View progressBar2;
    private View progressBar3;

    TextView confirmGetMerName = null;
    TextView confirmDisplayAmount = null;
    TextView confirmGetAmount = null;
    TableRow surchargeTableRow = null;
    TextView confirmDisplaySurcharge = null;
    TextView confirmGetSurcharge = null;
    TextView confirmCurrCode = null;
    TextView confirmDisplayCardNo = null;
    TextView confirmGetCardNo = null;
    TextView confirmDisplayCardHolder = null;
    TextView confirmGetCardHolder = null;
    TextView confirmDisplayDate = null;
    TextView confirmGetDate = null;
    TextView confirmPMethod = null;
    TextView confirmGetPMethod = null;
    TextView confirmDisplayMerRef = null;
    TextView confirmGetMerRef = null;
    TextView confirmDisplayRemark = null;
    TextView confirmGetRemark = null;
    TextView confirmDisplayPayType = null;
    TextView confirmGetPayType = null;

    public CustomConfirmationDialog(Context context, HashMap map) {
        super(context);

        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }

        this.map = map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_confirmation_dialog);
        setCancelable(false);

        amount = map.get(Constants.AMOUNT);
        currCode = map.get(Constants.CURRCODE);
        merRef = map.get(Constants.MERCHANT_REF);
        merID = map.get(Constants.MERID);
        payType = map.get(Constants.PAYTYPE) == null ? "N" : map.get(Constants.PAYTYPE);
        operator = map.get(Constants.OPERATORID) == null ? "" : map.get(Constants.OPERATORID);
        merName = map.get(Constants.MERNAME) == null ? "" : map.get(Constants.MERNAME);
        epMonth = map.get(Constants.EXPMONTH);
        epYear = map.get(Constants.EXPYEAR);
        cardNo = map.get(Constants.CARDNO);
        payMethod = map.get(Constants.PAYMETHOD);

        currency = CurrCode.getName(currCode);

        confirmGetMerName =(TextView) findViewById(R.id.confirmGetMerName);
        confirmGetMerName.setText(merName);

        confirmDisplayAmount = (TextView) findViewById(R.id.confirmDisplayAmount);
        confirmDisplayAmount.setText(R.string.c_amount);
        confirmGetAmount=(TextView) findViewById(R.id.confirmGetAmount);
        confirmGetAmount.setText(currCode + " " + amount);

        surchargeTableRow = (TableRow) findViewById(R.id.logout_33);
        confirmDisplaySurcharge = (TextView) findViewById(R.id.confirmDisplaySurcharge);
        confirmDisplaySurcharge.setText(R.string.print_surcharge);
        confirmGetSurcharge=(TextView) findViewById(R.id.confirmGetSurcharge);
//        confirmGetSurcharge.setText(currCode + " " + surcharge);

        confirmDisplayCardNo = (TextView) findViewById(R.id.confirmDisplayCardNo);
        confirmDisplayCardNo.setText(R.string.r_card_no);
        confirmGetCardNo=(TextView) findViewById(R.id.confirmGetCardNo);

        String middle = ((cardNo != null && cardNo.length() >= 14) ?
                cardNo.substring(6,cardNo.length()-4): "");
        middle = middle.replaceAll(".", "*");
        String f6 =((cardNo != null && cardNo.length() >= 14) ?
                cardNo.substring(0,6): "");
        String l4  =((cardNo != null && cardNo.length() >= 14) ?
                cardNo.substring(cardNo.length()-4): "");

        String maskedcardNo = f6+middle+l4;
        confirmGetCardNo.setText(maskedcardNo);

        confirmDisplayCardHolder = (TextView) findViewById(R.id.confirmDisplayCardHolder);
        confirmDisplayCardHolder.setText(R.string.r_card_holder);
        confirmGetCardHolder = (TextView) findViewById(R.id.confirmGetCardHolder);
//        confirmGetCardHolder.setText(editTextcardName.getText().toString());
        confirmDisplayDate = (TextView) findViewById(R.id.confirmDisplayDate);
        confirmDisplayDate.setText(R.string.r_expiry);
        confirmGetDate=(TextView) findViewById(R.id.confirmGetDate);
        confirmGetDate.setText(epMonth+"/"+epYear);
        confirmPMethod=(TextView) findViewById(R.id.confirmPMethod);
        confirmPMethod.setText(R.string.c_payment_method);
        confirmGetPMethod=(TextView) findViewById(R.id.confirmGetPMethod);
        String pMethod = payMethod;

        confirmGetPMethod.setText(pMethod);
        confirmDisplayMerRef = (TextView) findViewById(R.id.confirmDisplayMerRef);
        confirmDisplayMerRef.setText(R.string.r_mer_ref);
        confirmGetMerRef=(TextView) findViewById(R.id.confirmGetMerRef);
        confirmGetMerRef.setText(merRef);
        confirmDisplayRemark= (TextView) findViewById(R.id.confirmDisplayRemark);
        confirmDisplayRemark.setText(R.string.r_remark);
        confirmDisplayPayType=(TextView) findViewById(R.id.confirmDisplayPayType);
        confirmDisplayPayType.setText(R.string.r_pay_type);
        confirmGetPayType=(TextView) findViewById(R.id.confirmGetPayType);

        if (payType.equals("N")){
            confirmGetPayType.setText(R.string.sale);
        }else {
            confirmGetPayType.setText(R.string.authorize);
        }
//        confirmGetRemark= (TextView) findViewById(R.id.confirmGetRemark);
//        String Remark = remark;
//        if("".equals(Remark)){
//            confirmGetRemark.setText("");
//        }else{
//            confirmGetRemark.setText(remark);
//        }
//
        SharedPreferences prefsettings = getContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);
        String SurCalstat = (prefsettings.getString(Constants.pref_surcharge_calculation, Constants.default_surcharge_calculation));
        SharedPreferences pref_sur = getContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String mdr = pref_sur.getString(Constants.mer_mdr,"");
        if("OFF".equals(SurCalstat)||mdr==null||"".equals(mdr)){
            surchargeTableRow.setVisibility(GONE);
            confirmDisplaySurcharge.setVisibility(GONE);
            confirmGetSurcharge.setVisibility(GONE);
        }else if("OFF".equals(SurCalstat)||Double.valueOf(mdr)<=0){
            surchargeTableRow.setVisibility(GONE);
            confirmDisplaySurcharge.setVisibility(GONE);
            confirmGetSurcharge.setVisibility(GONE);
        }else if("ON".equals(SurCalstat)&&Double.valueOf(mdr)>0){
            surchargeTableRow.setVisibility(VISIBLE);
            confirmDisplaySurcharge.setVisibility(VISIBLE);
            confirmGetSurcharge.setVisibility(VISIBLE);
        }else{
            surchargeTableRow.setVisibility(GONE);
            confirmDisplaySurcharge.setVisibility(GONE);
            confirmGetSurcharge.setVisibility(GONE);
        }

        Button btnCancel = (Button) findViewById(R.id.c_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                progressBar2 = (View) findViewById(R.id.progressbar2);
//                progressBar2.setVisibility(VISIBLE);
//
//                progressBar3 = (View) findViewById(R.id.progressbar3);
//                progressBar3.setVisibility(GONE);

                dismiss();
            }
        });

        Button btnConfirm = (Button) findViewById(R.id.c_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postData();

            }
        });
    }

    public String getPrefPayGate() {
        SharedPreferences prefsettings = getContext()
                .getSharedPreferences("Settings", MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate,
                Constants.default_paygate);
        return prefpaygate;
    }

    private void postData(){
        /**********************
         ** After Mag Success **
         ***********************/
        HashMap<String, String> map = new HashMap();
        map.put(Constants.CARDNO, cardNo);
        map.put(Constants.EXPMONTH, epMonth);
        map.put(Constants.EXPYEAR, epYear);
        map.put(Constants.AMOUNT, amount);
        map.put(Constants.CURRCODE, currCode);
        map.put(Constants.MERID, merID);
        map.put(Constants.MERNAME, merName);
        map.put(Constants.MERCHANT_REF, merRef);
        map.put(Constants.PAYTYPE, payType);
        map.put(Constants.ENTRYMODE, "M");

//        Log.i(TAG, "Start MANUAL TradeResult");
        TradeResult tradeResult = new TradeResult(getOwnerActivity(), map);
        tradeResult.initData();
    }

}
