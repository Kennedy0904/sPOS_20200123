package com.example.dell.smartpos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class CardSchemeValidation {

    //Card Scheme supported by merchant
    public static boolean merchant_visa = false;
    public static boolean merchant_master = false;
    public static boolean merchant_cup = false;
    public static boolean merchant_amex = false;
    public static boolean merchant_jcb = false;
    public static boolean merchant_diners = false;
    String[] payMethod_Visa = {"VISA"};
    String[] payMethod_Master = {"Master"};
    String[] payMethod_CUP = {"CUP"};
    String[] payMethod_AMEX = {"AMEX"};
    String[] payMethod_JCB = {"JCB"};
    String[] payMethod_Diners = {"Diners"};

    //Card Scheme Available
    String cardScheme;
    public String visa_BIN = "";
    public String master_BIN = "";
    public String cup_BIN = "";
    public String amex_BIN = "";
    public String jcb_BIN = "";
    public String diners_BIN = "";
    public boolean visa_cardScheme = false;
    public boolean master_cardScheme = false;
    public boolean cup_cardScheme = false;
    public boolean amex_cardScheme = false;
    public boolean jcb_cardScheme = false;
    public boolean diners_cardScheme = false;
    boolean isVisaRange = false;
    boolean isMasterRange = false;
    boolean isAMEXRange = false;
    boolean isJCBRange = false;
    boolean isCUPRange = false;
    boolean isDinersRange = false;
    private static String[] rangeStart = new String[100];
    private static String[] rangeEnd = new String[100];

    //Transaction Type available for each card scheme
    String txnType_visa;
    String txnType_master;
    String txnType_cup;
    String txnType_amex;
    String txnType_jcb;
    String txnType_diners;
    public boolean visa_onlineSales = false;
    public boolean visa_preAuth = false;
    public boolean master_onlineSales = false;
    public boolean master_preAuth = false;
    public boolean cup_onlineSales = false;
    public boolean cup_preAuth = false;
    public boolean amex_onlineSales = false;
    public boolean amex_preAuth = false;
    public boolean jcb_onlineSales = false;
    public boolean jcb_preAuth = false;
    public boolean diners_onlineSales = false;
    public boolean diners_preAuth = false;

    Activity activity;
    Context context;
    SharedPreferences prefsettings;

    private static String TAG = "CardSchemeValidation";
    public static CardSchemeValidation instance;

    public CardSchemeValidation(Activity activity){
        this.activity = activity;
        prefsettings = activity.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
    }

    // Initial card scheme available
    public void initCardScheme(){

        cardScheme = prefsettings.getString(Constants.card_scheme, "").toLowerCase();

        if (cardScheme.contains("-visa-")) {
            visa_cardScheme = true;
        }

        if (cardScheme.contains("-mastercard-")) {
            master_cardScheme = true;
        }

        if (cardScheme.contains("-cup-")) {
            cup_cardScheme = true;
        }

        if (cardScheme.contains("-amex-")) {
            amex_cardScheme = true;
        }

        if (cardScheme.contains("-jcb-")) {
            jcb_cardScheme = true;
        }

        if (cardScheme.contains("-diners-")) {
            diners_cardScheme = true;
        }
    }

    // Initial transaction type for each card scheme
    private void initCardSchemeTrxType(){

        txnType_visa = prefsettings.getString(Constants.visa_txn_type, "").toLowerCase();
        txnType_master = prefsettings.getString(Constants.mastercard_txn_type, "").toLowerCase();
        txnType_cup = prefsettings.getString(Constants.cup_txn_type, "").toLowerCase();
        txnType_amex = prefsettings.getString(Constants.amex_txn_type, "").toLowerCase();
        txnType_jcb = prefsettings.getString(Constants.jcb_txn_type, "").toLowerCase();
        txnType_diners = prefsettings.getString(Constants.diners_txn_type, "").toLowerCase();

        if(visa_cardScheme){

            if (txnType_visa.contains("-onsale-")) {
                visa_onlineSales = true;
            }

            if (txnType_visa.contains("-preauth-")) {
                visa_preAuth = true;
            }
        }

        if(master_cardScheme){

            if (txnType_master.contains("-onsale-")) {
                master_onlineSales = true;
            }

            if (txnType_master.contains("-preauth-")) {
                master_preAuth = true;
            }
        }

        if(cup_cardScheme){

            if (txnType_cup.contains("-onsale-")) {
                cup_onlineSales = true;
            }

            if (txnType_cup.contains("-preauth-")) {
                cup_preAuth = true;
            }
        }

        if(amex_cardScheme){

            if (txnType_amex.contains("-onsale-")) {
                amex_onlineSales = true;
            }

            if (txnType_amex.contains("-preauth-")) {
                amex_preAuth = true;
            }
        }

        if(jcb_cardScheme){

            if (txnType_jcb.contains("-onsale-")) {
                jcb_onlineSales = true;
            }

            if (txnType_jcb.contains("-preauth-")) {
                jcb_preAuth = true;
            }
        }

        if(diners_cardScheme){

            if (txnType_diners.contains("-onsale-")) {
                diners_onlineSales = true;
            }

            if (txnType_diners.contains("-preauth-")) {
                diners_preAuth = true;
            }
        }
    }

    // Initial card scheme BIN range
    private void initCardSchemeRange(){
        visa_BIN = prefsettings.getString(Constants.visa_bin_range, Constants.default_visa_bin_range);
        master_BIN = prefsettings.getString(Constants.mc_bin_range, Constants.default_mc_bin_range);
        cup_BIN = prefsettings.getString(Constants.cup_bin_range, Constants.default_cup_bin_range);
        amex_BIN = prefsettings.getString(Constants.amex_bin_range, Constants.default_amex_bin_range);
        jcb_BIN = prefsettings.getString(Constants.jcb_bin_range, Constants.default_jcb_bin_range);
        diners_BIN = prefsettings.getString(Constants.diners_bin_range, Constants.default_diners_bin_range);
    }

    // Identify Card Scheme
    public String checkCardScheme(int BIN){

        String payMethod = null;

        initCardSchemeRange();

        isVisaRange = cardSchemeRange(visa_BIN, BIN);
        isMasterRange = cardSchemeRange(master_BIN, BIN);
        isCUPRange = cardSchemeRange(cup_BIN, BIN);
        isAMEXRange = cardSchemeRange(amex_BIN, BIN);
        isJCBRange = cardSchemeRange(jcb_BIN, BIN);
        isDinersRange = cardSchemeRange(diners_BIN, BIN);

        if(isVisaRange){
            payMethod = activity.getString(R.string.paymethod_visa);
        } else if(isMasterRange){
            payMethod = activity.getString(R.string.paymethod_master);
        } else if(isCUPRange){
            payMethod = activity.getString(R.string.paymethod_cup);
        } else if(isAMEXRange){
            payMethod = activity.getString(R.string.paymethod_amex);
        } else if(isJCBRange){
            payMethod = activity.getString(R.string.paymethod_jcb);
        } else if(isDinersRange){
            payMethod = activity.getString(R.string.paymethod_diners);
        }

        return payMethod;
    }

    // Compare BIN with card scheme range in settings
    private boolean cardSchemeRange(String range, int BIN){

        boolean inRange = false;

        Log.d(TAG, "Card Scheme Range ----- " + range);
        if (range.length() > 0 ) {
            for (int i = 0; i < range.split("-R-").length; i++) {
                rangeStart[i] = "";
                rangeEnd[i] = "";

                if (range.split("-R-")[i].split("_").length > 1) {
                    rangeStart[i] = range.split("-R-")[i].split("_")[0];
                    rangeEnd[i] = range.split("-R-")[i].split("_")[1];
                }
                System.out.println("------" + i + " " + rangeStart[i] + " " + rangeEnd[i]);

                int range1 = Integer.valueOf(rangeStart[i]);
                int range2 = Integer.valueOf(rangeEnd[i]);

                if(BIN >= range1 && BIN <= range2)
                {
                    inRange = true;
                    break;
                }else{
                    inRange = false;
                }
            }
        }
        return inRange;
    }

    // Validate Card Scheme
    public boolean validateCardScheme(String cardScheme, String payType) {

        initCardScheme();
        initCardSchemeTrxType();

        boolean cardSchemeResult = false;

        Log.d(TAG, "cardScheme: " + cardScheme);
        Log.d(TAG, "payType: " + payType);

        if (cardScheme.equalsIgnoreCase(activity.getString(R.string.paymethod_visa))) {

            if (visa_cardScheme) {

                if (payType.equalsIgnoreCase("N")) {

                    if (visa_onlineSales) {
                        cardSchemeResult = true;
                    } else {
                        displayErrDialog(activity.getString(R.string.card_scheme_onsales_not_supported));
                    }
                } else if(payType.equalsIgnoreCase("H")) {
                    if (visa_preAuth) {
                        cardSchemeResult = true;
                    } else {
                        displayErrDialog(activity.getString(R.string.card_scheme_preauth_not_supported));
                    }
                }
            }else {
                displayErrDialog(activity.getString(R.string.card_scheme_not_supported));
            }

        } else if(cardScheme.equalsIgnoreCase(activity.getString(R.string.paymethod_master))) {

            if(master_cardScheme){

                if (payType.equalsIgnoreCase("N")) {

                    if (master_onlineSales) {
                        cardSchemeResult = true;
                    } else {
                        displayErrDialog(activity.getString(R.string.card_scheme_onsales_not_supported));
                    }
                }else if (payType.equalsIgnoreCase("H")) {
                    if (master_preAuth) {
                        cardSchemeResult = true;
                    } else {
                        displayErrDialog(activity.getString(R.string.card_scheme_preauth_not_supported));
                    }
                }
            }else {
                displayErrDialog(activity.getString(R.string.card_scheme_not_supported));
            }

        } else if(cardScheme.equalsIgnoreCase(activity.getString(R.string.paymethod_cup))) {

            if(cup_cardScheme){

                if (payType.equalsIgnoreCase("N")) {

                    if (cup_onlineSales) {
                        cardSchemeResult = true;
                    } else {
                        displayErrDialog(activity.getString(R.string.card_scheme_onsales_not_supported));
                    }
                } else if(payType.equalsIgnoreCase("H")) {
                    if (cup_preAuth) {
                        cardSchemeResult = true;
                    } else {
                        displayErrDialog(activity.getString(R.string.card_scheme_preauth_not_supported));
                    }
                }
            }else {
                displayErrDialog(activity.getString(R.string.card_scheme_not_supported));
            }

        } else if(cardScheme.equalsIgnoreCase(activity.getString(R.string.paymethod_amex))) {

            if(amex_cardScheme){

                if (payType.equalsIgnoreCase("N")) {

                    if (amex_onlineSales) {
                        cardSchemeResult = true;
                    } else {
                        displayErrDialog(activity.getString(R.string.card_scheme_onsales_not_supported));
                    }
                } else if(payType.equalsIgnoreCase("H")) {
                    if (amex_preAuth) {
                        cardSchemeResult = true;
                    } else {
                        displayErrDialog(activity.getString(R.string.card_scheme_preauth_not_supported));
                    }
                }
            }else {
                displayErrDialog(activity.getString(R.string.card_scheme_not_supported));
            }

        } else if(cardScheme.equalsIgnoreCase(activity.getString(R.string.paymethod_jcb))) {

            if(jcb_cardScheme){

                if (payType.equalsIgnoreCase("N")) {

                    if (jcb_onlineSales) {
                        cardSchemeResult = true;
                    } else {
                        displayErrDialog(activity.getString(R.string.card_scheme_onsales_not_supported));
                    }
                } else if(payType.equalsIgnoreCase("H")) {
                    if (jcb_preAuth) {
                        cardSchemeResult = true;
                    } else {
                        displayErrDialog(activity.getString(R.string.card_scheme_preauth_not_supported));
                    }
                }
            }else {
                displayErrDialog(activity.getString(R.string.card_scheme_not_supported));
            }

        } else if(cardScheme.equalsIgnoreCase(activity.getString(R.string.paymethod_diners))) {

            if(diners_cardScheme){

                if (payType.equalsIgnoreCase("N")) {

                    if (diners_onlineSales) {
                        cardSchemeResult = true;
                    } else {
                        displayErrDialog(activity.getString(R.string.card_scheme_onsales_not_supported));
                    }
                } else if(payType.equalsIgnoreCase("H")) {
                    if (diners_preAuth) {
                        cardSchemeResult = true;
                    } else {
                        displayErrDialog(activity.getString(R.string.card_scheme_preauth_not_supported));
                    }
                }
            }else {
                displayErrDialog(activity.getString(R.string.card_scheme_not_supported));
            }
        }else {
            displayErrDialog(activity.getString(R.string.card_scheme_not_supported));
        }

        Log.d(TAG, "cardSchemeResult: " + cardSchemeResult);
        return cardSchemeResult;
    }

    private void displayErrDialog(final String message){

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.alert))
                        .setIcon(R.drawable.ic_warning_24dp)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                activity.finish();
                            }
                        })
                        .show();
            }
        });
    }
}
