package com.example.dell.smartpos;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.dell.smartpos.CardModule.tradepaypw.SwingCardActivity;

import static android.content.Context.MODE_PRIVATE;

/**************************************
 ** Validation Card Payment 20191126 **
 **************************************/

public class CardValidation {

    /**
     * Validation Params 20191126
     */

    private static SwingCardActivity activity;
    private static Context context;

    //Entry Mode Available
    String allEntryMode;
    public boolean entryMode_chip = false;
    public boolean entryMode_swipe = false;
    public boolean entryMode_fallback = false;
    public boolean entryMode_contactless = false;

    //Transaction Type available for each entry mode
    String txnType_chip;
    String txnType_swipe;
    String txnType_fallback;
    String txnType_contactless;
    boolean chip_onlineSales = false;
    boolean chip_preAuth = false;
    boolean swipe_onlineSales = false;
    boolean swipe_preAuth = false;
    boolean fallback_onlineSales = false;
    boolean fallback_preAuth = false;
    boolean contactless_onlineSales = false;
    boolean contactless_preAuth = false;

    static SharedPreferences prefsettings;

    private static String TAG = "CardSettingsValidation";
    public static CardValidation instance;

    public CardValidation(SwingCardActivity activity){
        this.activity = activity;
        prefsettings = activity.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
    }

    public CardValidation(Context context){
        this.context = context;
        prefsettings = activity.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
    }

    // Initial entry mode available
    private void initEntryMode(){

        allEntryMode = prefsettings.getString(Constants.entry_mode, "").toLowerCase();

        if (allEntryMode.contains("-chip-")) {
            entryMode_chip = true;
        }

        if (allEntryMode.contains("-swipe-")) {
            entryMode_swipe = true;
        }

        if (allEntryMode.contains("-fallback-")) {
            entryMode_fallback = true;
        }

        if (allEntryMode.contains("-contactless-")) {
            entryMode_contactless = true;
        }
    }

    // Initial transaction type available for each entry mode
    private void initEntryModeTxnType(){

        if(entryMode_chip){
            txnType_chip = prefsettings.getString(Constants.chip_txn_type, "").toLowerCase();

            if (txnType_chip.contains("-onsale-")) {
                chip_onlineSales = true;
            }

            if (txnType_chip.contains("-preauth-")) {
                chip_preAuth = true;
            }
        }

        if(entryMode_swipe){
            txnType_swipe = prefsettings.getString(Constants.swipe_txn_type, "").toLowerCase();

            if (txnType_swipe.contains("-onsale-")) {
                swipe_onlineSales = true;
            }

            if (txnType_swipe.contains("-preauth-")) {
                swipe_preAuth = true;
            }
        }

        if(entryMode_fallback){
            txnType_fallback = prefsettings.getString(Constants.fallback_txn_type, "").toLowerCase();

            if (txnType_fallback.contains("-onsale-")) {
                fallback_onlineSales = true;
            }

            if (txnType_fallback.contains("-preauth-")) {
                fallback_preAuth = true;
            }
        }

        if(entryMode_contactless){
            txnType_contactless = prefsettings.getString(Constants.contactless_txn_type, "").toLowerCase();

            if (txnType_contactless.contains("-onsale-")) {
                contactless_onlineSales = true;
            }

            if (txnType_contactless.contains("-preauth-")) {
                contactless_preAuth = true;
            }
        }
    }

    // Validate Entry Mode
    public boolean checkEntryMode(String entryMode, String payType){

        initEntryMode();
        initEntryModeTxnType();

        boolean entryModeResult = false;

        Log.d(TAG, "entryMode: " + entryMode);
        Log.d(TAG, "payType: " + payType);

        if (entryMode.equalsIgnoreCase("C")) {

            if(entryMode_chip){

                if (payType.equalsIgnoreCase("N")) {

                    if (chip_onlineSales) {
                        entryModeResult = true;
                    } else {
                        activity.displayErrDialog(activity.getString(R.string.emv_onsales_not_supported));
                    }
                }
                if (payType.equalsIgnoreCase("H")) {
                    if (chip_preAuth) {
                        entryModeResult = true;
                    } else {
                        activity.displayErrDialog(activity.getString(R.string.emv_preauth_not_supported));
                    }
                }
            }else {
                activity.displayErrDialog(activity.getString(R.string.entryMode_chip_not_supported));
            }
        }

        if (entryMode.equals("S")) {

            if (entryMode_swipe) {

                if (payType.equalsIgnoreCase("N")) {

                    if (swipe_onlineSales) {
                        entryModeResult = true;
                    } else {
                        activity.displayErrDialog(activity.getString(R.string.emv_onsales_not_supported));
                    }
                }

                if (payType.equalsIgnoreCase("H")) {

                    if (swipe_preAuth) {
                        entryModeResult = true;
                    } else {
                        activity.displayErrDialog(activity.getString(R.string.emv_preauth_not_supported));
                    }
                }
            } else {
                activity.displayErrDialog(activity.getString(R.string.entryMode_swipe_not_supported));
            }
        }

        if (entryMode.equals("T")) {

            if (entryMode_contactless) {

                if (payType.equalsIgnoreCase("N")) {

                    if (contactless_onlineSales) {
                        entryModeResult = true;
                    } else {
                        activity.displayErrDialog(activity.getString(R.string.emv_onsales_not_supported));
                    }
                }

                if (payType.equalsIgnoreCase("H")) {

                    if (contactless_preAuth) {
                        entryModeResult = true;
                    } else {
                        activity.displayErrDialog(activity.getString(R.string.emv_preauth_not_supported));
                    }
                }
            } else {
                activity.displayErrDialog(activity.getString(R.string.entryMode_contactless_not_supported));
            }
        }

        if (entryMode.equals("F")) {

            if (entryMode_fallback) {

                if (payType.equalsIgnoreCase("N")) {

                    if (fallback_onlineSales) {
                        entryModeResult = true;
                    } else {
                        activity.displayErrDialog(activity.getString(R.string.emv_onsales_not_supported));
                    }
                }

                if (payType.equalsIgnoreCase("H")) {

                    if (fallback_preAuth) {
                        entryModeResult = true;
                    } else {
                        activity.displayErrDialog(activity.getString(R.string.emv_preauth_not_supported));
                    }
                }
            } else {
                activity.displayErrDialog(activity.getString(R.string.entryMode_fallback_not_supported));
            }
        }

        return entryModeResult;
    }

    // Validate Entry Attempts
    public int checkEntryAttempts(String entryMode) {

        int attempts = 0;

        if (entryMode.equalsIgnoreCase("C")) {
            attempts = prefsettings.getInt("chip_attempt", Constants.default_chip_attempt);
        } else if (entryMode.equalsIgnoreCase("T")) {
            attempts = prefsettings.getInt("contactless_attempt", Constants.default_contactless_attempt);
        }

        return attempts;
    }



//    //Check Card Scheme supported by merchant
//    private void checkMerchantCardScheme(String payMethodList){
//
//        merchant_visa = containstr(payMethodList, payMethod_Visa);
//        merchant_master = containstr(payMethodList, payMethod_Master);
//        merchant_cup = containstr(payMethodList, payMethod_CUP);
//        merchant_amex = containstr(payMethodList, payMethod_AMEX);
//        merchant_jcb = containstr(payMethodList, payMethod_JCB);
//        merchant_diners = containstr(payMethodList, payMethod_Diners);
//
//        if(merId.equals("560200335")){
//            merchant_visa = true;
//            merchant_master = true;
//            merchant_cup = true;
//            merchant_amex = true;
//            merchant_jcb = true;
//            merchant_diners = true;
//        }
//    }

    //Check transaction type for each card scheme
//    private void checkCardSchemeTrxType(){
//
//        txnType_visa = prefsettings.getString(Constants.visa_txn_type, "").toLowerCase();
//        txnType_master = prefsettings.getString(Constants.mastercard_txn_type, "").toLowerCase();
//        txnType_cup = prefsettings.getString(Constants.cup_txn_type, "").toLowerCase();
//        txnType_amex = prefsettings.getString(Constants.amex_txn_type, "").toLowerCase();
//        txnType_jcb = prefsettings.getString(Constants.jcb_txn_type, "").toLowerCase();
//        txnType_diners = prefsettings.getString(Constants.diners_txn_type, "").toLowerCase();
//
//        if(visa_cardScheme){
//
//            if (txnType_visa.contains("-onsale-")) {
//                visa_onlineSales = true;
//            }
//
//            if (txnType_visa.contains("-preauth-")) {
//                visa_preAuth = true;
//            }
//        }
//
//        if(master_cardScheme){
//
//            if (txnType_master.contains("-onsale-")) {
//                master_onlineSales = true;
//            }
//
//            if (txnType_master.contains("-preauth-")) {
//                master_preAuth = true;
//            }
//        }
//
//        if(cup_cardScheme){
//
//            if (txnType_cup.contains("-onsale-")) {
//                cup_onlineSales = true;
//            }
//
//            if (txnType_cup.contains("-preauth-")) {
//                cup_preAuth = true;
//            }
//        }
//
//        if(amex_cardScheme){
//
//            if (txnType_amex.contains("-onsale-")) {
//                amex_onlineSales = true;
//            }
//
//            if (txnType_amex.contains("-preauth-")) {
//                amex_preAuth = true;
//            }
//        }
//
//        if(jcb_cardScheme){
//
//            if (txnType_jcb.contains("-onsale-")) {
//                jcb_onlineSales = true;
//            }
//
//            if (txnType_jcb.contains("-preauth-")) {
//                jcb_preAuth = true;
//            }
//        }
//
//        if(diners_cardScheme){
//
//            if (txnType_diners.contains("-onsale-")) {
//                diners_onlineSales = true;
//            }
//
//            if (txnType_diners.contains("-preauth-")) {
//                diners_preAuth = true;
//            }
//        }
//    }

//    //Check card scheme available
//    private void checkCardScheme(){
//
//        cardScheme = prefsettings.getString(Constants.card_scheme, "").toLowerCase();
//
//        if (cardScheme.contains("-visa-")) {
//            visa_cardScheme = true;
//            visa_BIN = prefsettings.getString(Constants.visa_bin_range, Constants.default_visa_bin_range);
//        }
//
//        if (cardScheme.contains("-mastercard-")) {
//            master_cardScheme = true;
//            master_BIN = prefsettings.getString(Constants.mc_bin_range, Constants.default_mc_bin_range);
//        }
//
//        if (cardScheme.contains("-cup-")) {
//            cup_cardScheme = true;
//            cup_BIN = prefsettings.getString(Constants.cup_bin_range, Constants.default_cup_bin_range);
//        }
//
//        if (cardScheme.contains("-amex-")) {
//            amex_cardScheme = true;
//            amex_BIN = prefsettings.getString(Constants.amex_bin_range, Constants.default_amex_bin_range);
//        }
//
//        if (cardScheme.contains("-jcb-")) {
//            jcb_cardScheme = true;
//            jcb_BIN = prefsettings.getString(Constants.jcb_bin_range, Constants.default_jcb_bin_range);
//        }
//
//        if (cardScheme.contains("-diners-")) {
//            diners_cardScheme = true;
//            diners_BIN = prefsettings.getString(Constants.diners_bin_range, Constants.default_diners_bin_range);
//        }
//    }

//    private void checkContactlessTrxLimit(double trxAmnt){
//
//        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
//                Constants.SETTINGS, MODE_PRIVATE);
//
//        visa_trx_limit = Double.parseDouble(prefsettings.getString(Constants.visa_transaction_limit, Constants.default_visa_transaction_limit));
//        if(trxAmnt < visa_trx_limit){
//            visaTrxLimit = true;
//        }
//
//        visa_cvm_limit = Double.parseDouble(prefsettings.getString(Constants.visa_cvm_limit, Constants.default_visa_cvm_limit));
//        if(trxAmnt < visa_cvm_limit){
//            visaCVMLimit = true;
//        }
//
//        visa_floor_limit = Double.parseDouble(prefsettings.getString(Constants.visa_floor_limit, Constants.default_visa_floor_limit));
//        if(trxAmnt < visa_floor_limit){
//            visaFloorLimit = true;
//        }
//    }

//    private  void checkMerchantCardScheme(){
//
//        merchant_visa = containstr(payMethod, payMethod_Visa);
//        merchant_master = containstr(payMethod, payMethod_Master);
//        merchant_cup = containstr(payMethod, payMethod_CUP);
//        merchant_amex = containstr(payMethod, payMethod_AMEX);
//        merchant_jcb = containstr(payMethod, payMethod_JCB);
//        merchant_diners = containstr(payMethod, payMethod_Diners);
//
//        if(merID.equals("560200335")){
//            merchant_visa = true;
//            merchant_master = true;
//            merchant_cup = true;
//            merchant_amex = true;
//            merchant_jcb = true;
//            merchant_diners = true;
//        }
//    }

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

    /*End Validation*/
}


