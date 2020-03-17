package com.example.dell.smartpos.Scanner;

import android.app.Activity;

import com.example.dell.smartpos.R;

/**
 * Created by otto on 2017/6/14/0014.
 */

public class Fail_word {

    public static String failreason(String reason, Activity activity){
        String rea = "";
        if(reason.equals("REFUND")){
            rea = activity.getString(R.string.rs_REFUND);
        }
        else if(reason.equals("NOTPAY")){
            rea = activity.getString(R.string.rs_NOTPAY);
        }
        else if(reason.equals("CLOSED")){
            rea = activity.getString(R.string.rs_CLOSED);
        }
        else if(reason.equals("REVOKED")){
            rea = activity.getString(R.string.rs_REVOKED);
        }
        else if(reason.equals("PAYERROR")){
            rea = activity.getString(R.string.rs_PAYERROR);
        }
        else if(reason.equals("SYSTEMERROR")){
            rea = activity.getString(R.string.rs_SYSTEMERROR);
        }
        else if(reason.equals("PARAM_ERROR")){
            rea = activity.getString(R.string.rs_PARAM_ERROR);
        }
        else if(reason.equals("ORDERPAID")){
            rea = activity.getString(R.string.rs_ORDERPAID);
        }
        else if(reason.equals("NOAUTH")){
            rea = activity.getString(R.string.rs_NOAUTH);
        }
        else if(reason.equals("AUTHCODEEXPIRE")){
            rea = activity.getString(R.string.rs_AUTHCODEEXPIRE);
        }
        else if(reason.equals("NOTENOUGH")){
            rea = activity.getString(R.string.rs_NOTENOUGH);
        }
        else if(reason.equals("NOTSUPORTCARD")){
            rea = activity.getString(R.string.rs_NOTSUPORTCARD);
        }
        else if(reason.equals("ORDERCLOSED")){
            rea = activity.getString(R.string.rs_ORDERCLOSED);
        }
        else if(reason.equals("ORDERREVERSED")){
            rea = activity.getString(R.string.rs_ORDERREVERSED);
        }
        else if(reason.equals("BANKERROR")){
            rea = activity.getString(R.string.rs_BANKERROR);
        }
        else if(reason.equals("USERPAYING")){
            rea = activity.getString(R.string.rs_USERPAYING);
        }
        else if(reason.equals("AUTH_CODE_ERROR")){
            rea = activity.getString(R.string.rs_AUTH_CODE_ERROR);
        }
        else if(reason.equals("AUTH_CODE_INVALID")){
            rea = activity.getString(R.string.rs_AUTH_CODE_INVALID);
        }
        else if(reason.equals("XML_FORMAT_ERROR")){
            rea = activity.getString(R.string.rs_XML_FORMAT_ERROR);
        }
        else if(reason.equals("REQUIRE_POST_METHOD")){
            rea = activity.getString(R.string.rs_REQUIRE_POST_METHOD);
        }
        else if(reason.equals("SIGNERROR")){
            rea = activity.getString(R.string.rs_SIGNERROR);
        }
        else if(reason.equals("LACK_PARAMS")){
            rea = activity.getString(R.string.rs_LACK_PARAMS);
        }
        else if(reason.equals("NOT_UTF8")){
            rea = activity.getString(R.string.rs_NOT_UTF8);
        }
        else if(reason.equals("BUYER_MISMATCH")){
            rea = activity.getString(R.string.rs_BUYER_MISMATCH);
        }
        else if(reason.equals("APPID_NOT_EXIST")){
            rea = activity.getString(R.string.rs_APPID_NOT_EXIST);
        }
        else if(reason.equals("MCHID_NOT_EXIST")){
            rea = activity.getString(R.string.rs_MCHID_NOT_EXIST);
        }
        else if(reason.equals("OUT_TRADE_NO_USED")){
            rea = activity.getString(R.string.rs_OUT_TRADE_NO_USED);
        }
        else if(reason.equals("APPID_MCHID_NOT_MATCH")){
            rea = activity.getString(R.string.rs_APPID_MCHID_NOT_MATCH);
        }
        else if(reason.equals("SYSTEM_ERROR")){
            rea = activity.getString(R.string.rs_SYSTEM_ERROR);
        }
        else if(reason.equals("ILLEGAL_SIGN")){
            rea = activity.getString(R.string.rs_ILLEGAL_SIGN);
        }
        else if(reason.equals("INVALID_PARAMETER")){
            rea = activity.getString(R.string.rs_INVALID_PARAMETER);
        }
        else if(reason.equals("ILLEGAL_ARGUMENT")){
            rea = activity.getString(R.string.rs_ILLEGAL_ARGUMENT);
        }
        else if(reason.equals("ILLEGAL_PARTNER")){
            rea = activity.getString(R.string.rs_ILLEGAL_PARTNER);
        }
        else if(reason.equals("ILLEGAL_EXTERFACE")){
            rea = activity.getString(R.string.rs_ILLEGAL_EXTERFACE);
        }
        else if(reason.equals("ILLEGAL_PARTNER_EXTERFACE")){
            rea = activity.getString(R.string.rs_ILLEGAL_PARTNER_EXTERFACE);
        }
        else if(reason.equals("ILLEGAL_SIGN_TYPE")){
            rea = activity.getString(R.string.rs_ILLEGAL_SIGN_TYPE);
        }
        else if(reason.equals("HAS_NO_PRIVILEGE")){
            rea = activity.getString(R.string.rs_HAS_NO_PRIVILEGE);
        }
        else if(reason.equals("TRADE_BUYER_NOT_MATCH")){
            rea = activity.getString(R.string.rs_TRADE_BUYER_NOT_MATCH);
        }
        else if(reason.equals("TRADE_HAS_CLOSE")){
            rea = activity.getString(R.string.rs_TRADE_HAS_CLOSE);
        }
        else if(reason.equals("TRADE_STATUS_ERROR")){
            rea = activity.getString(R.string.rs_TRADE_STATUS_ERROR);
        }
        else if(reason.equals("EXIST_FORBIDDEN_WORD")){
            rea = activity.getString(R.string.rs_EXIST_FORBIDDEN_WORD);
        }
        else if(reason.equals("SELLER_NOT_EXIST")){
            rea = activity.getString(R.string.rs_SELLER_NOT_EXIST);
        }
        else if(reason.equals("BUYER_NOT_EXIST")){
            rea = activity.getString(R.string.rs_BUYER_NOT_EXIST);
        }
        else if(reason.equals("BUYER_ENABLE_STATUS_FORBID")){
            rea = activity.getString(R.string.rs_BUYER_ENABLE_STATUS_FORBID);
        }
        else if(reason.equals("BUYER_SELLER_EQUAL")){
            rea = activity.getString(R.string.rs_BUYER_SELLER_EQUAL);
        }
        else if(reason.equals("CLIENT_VERSION_NOT_MATCH")){
            rea = activity.getString(R.string.rs_CLIENT_VERSION_NOT_MATCH);
        }
        else if(reason.equals("SOUNDWAVE_PARSER_FAIL")){
            rea = activity.getString(R.string.rs_SOUNDWAVE_PARSER_FAIL);
        }
        else if(reason.equals("CONTEXT_INCONSISTENT")){
            rea = activity.getString(R.string.rs_CONTEXT_INCONSISTENT);
        }
        else if(reason.equals("PRODUCT_AMOUNT_LIMIT_ERROR")){
            rea = activity.getString(R.string.rs_PRODUCT_AMOUNT_LIMIT_ERROR);
        }
        else if(reason.equals("BUYER_BALANCE_NOT_ENOUGH")){
            rea = activity.getString(R.string.rs_BUYER_BALANCE_NOT_ENOUGH);
        }
        else if(reason.equals("TOTAL_FEE_EXCEED")){
            rea = activity.getString(R.string.rs_TOTAL_FEE_EXCEED);
        }
        else if(reason.equals("BUYER_PAYMENT_AMOUNT_DAY_LIMIT_ERROR")){
            rea = activity.getString(R.string.rs_BUYER_PAYMENT_AMOUNT_DAY_LIMIT_ERROR);
        }
        else if(reason.equals("BUYER_PAYMENT_AMOUNT_MONTH_LIMIT_ERROR")){
            rea = activity.getString(R.string.rs_BUYER_PAYMENT_AMOUNT_MONTH_LIMIT_ERROR);
        }
        else if(reason.equals("ERROR_BUYER_CERTIFY_LEVEL_LIMIT")){
            rea = activity.getString(R.string.rs_ERROR_BUYER_CERTIFY_LEVEL_LIMIT);
        }
        else if(reason.equals("ERROR_SELLER_CERTIFY_LEVEL_LIMIT")){
            rea = activity.getString(R.string.rs_ERROR_SELLER_CERTIFY_LEVEL_LIMIT);
        }
        else if(reason.equals("BUYER_BANKCARD_BALANCE_NOT_ENOUGH")){
            rea = activity.getString(R.string.rs_BUYER_BANKCARD_BALANCE_NOT_ENOUGH);
        }
        else if(reason.equals("PAYMENT_FAIL")){
            rea = activity.getString(R.string.rs_PAYMENT_FAIL);
        }
        else if(reason.equals("MOBILE_PAYMENT_SWITCH_OFF")){
            rea = activity.getString(R.string.rs_MOBILE_PAYMENT_SWITCH_OFF);
        }
        else if(reason.equals("USER_FACE_PAYMENT_SWITCH_OFF")){
            rea = activity.getString(R.string.rs_USER_FACE_PAYMENT_SWITCH_OFF);
        }
        else if(reason.equals("ERROR_BALANCE_PAYMENT_DISABLE")){
            rea = activity.getString(R.string.rs_ERROR_BALANCE_PAYMENT_DISABLE);
        }
        else if(reason.equals("EXCHANGE_AMOUNT_OR_CURRENCY_ERROR")){
            rea = activity.getString(R.string.rs_EXCHANGE_AMOUNT_OR_CURRENCY_ERROR);
        }
        else if(reason.equals("aop.ACQ.SYSTEM_ERROR")){
            rea = activity.getString(R.string.rs_aop_ACQ_SYSTEM_ERROR);
        }
        else if(reason.equals("aop.ACQ.INVALID_PARAMETER")){
            rea = activity.getString(R.string.rs_aop_ACQ_INVALID_PARAMETER);
        }
        else if(reason.equals("aop.ACQ.ACCESS_FORBIDDEN")){
            rea = activity.getString(R.string.rs_aop_ACQ_ACCESS_FORBIDDEN);
        }
        else if(reason.equals("aop.ACQ.EXIST_FORBIDDEN_WORD")){
            rea = activity.getString(R.string.rs_aop_ACQ_EXIST_FORBIDDEN_WORD);
        }
        else if(reason.equals("aop.ACQ.PARTNER_ERROR")){
            rea = activity.getString(R.string.rs_aop_ACQ_PARTNER_ERROR);
        }
        else if(reason.equals("aop.ACQ.TOTAL_FEE_EXCEED")){
            rea = activity.getString(R.string.rs_aop_ACQ_TOTAL_FEE_EXCEED);
        }
        else if(reason.equals("aop.ACQ.PAYMENT_AUTH_CODE_INVALID")){
            rea = activity.getString(R.string.rs_aop_ACQ_PAYMENT_AUTH_CODE_INVALID);
        }
        else if(reason.equals("aop.ACQ.CONTEXT_INCONSISTENT")){
            rea = activity.getString(R.string.rs_aop_ACQ_CONTEXT_INCONSISTENT);
        }
        else if(reason.equals("aop.ACQ.TRADE_HAS_SUCCESS")){
            rea = activity.getString(R.string.rs_aop_ACQ_TRADE_HAS_SUCCESS);
        }
        else if(reason.equals("aop.ACQ.TRADE_HAS_CLOSE")){
            rea = activity.getString(R.string.rs_aop_ACQ_TRADE_HAS_CLOSE);
        }
        else if(reason.equals("aop.ACQ.BUYER_BALANCE_NOT_ENOUGH")){
            rea = activity.getString(R.string.rs_aop_ACQ_BUYER_BALANCE_NOT_ENOUGH);
        }
        else if(reason.equals("aop.ACQ.BUYER_BANKCARD_BALANCE_NOT_ENOUGH")){
            rea = activity.getString(R.string.rs_aop_ACQ_BUYER_BANKCARD_BALANCE_NOT_ENOUGH);
        }
        else if(reason.equals("aop.ACQ.ERROR_BALANCE_PAYMENT_DISABLE")){
            rea = activity.getString(R.string.rs_aop_ACQ_ERROR_BALANCE_PAYMENT_DISABLE);
        }
        else if(reason.equals("aop.ACQ.BUYER_SELLER_EQUAL")){
            rea = activity.getString(R.string.rs_aop_ACQ_BUYER_SELLER_EQUAL);
        }
        else if(reason.equals("aop.ACQ.TRADE_BUYER_NOT_MATCH")){
            rea = activity.getString(R.string.rs_aop_ACQ_TRADE_BUYER_NOT_MATCH);
        }
        else if(reason.equals("aop.ACQ.BUYER_ENABLE_STATUS_FORBID")){
            rea = activity.getString(R.string.rs_aop_ACQ_BUYER_ENABLE_STATUS_FORBID);
        }
        else if(reason.equals("aop.ACQ.PULL_MOBILE_CASHIER_FAIL")){
            rea = activity.getString(R.string.rs_aop_ACQ_PULL_MOBILE_CASHIER_FAIL);
        }
        else if(reason.equals("aop.ACQ.MOBILE_PAYMENT_SWITCH_OFF")){
            rea = activity.getString(R.string.rs_aop_ACQ_MOBILE_PAYMENT_SWITCH_OFF);
        }
        else if(reason.equals("aop.ACQ.PAYMENT_FAIL")){
            rea = activity.getString(R.string.rs_aop_ACQ_PAYMENT_FAIL);
        }
        else if(reason.equals("aop.ACQ.BUYER_PAYMENT_AMOUNT_DAY_LIMIT_ERROR")){
            rea = activity.getString(R.string.rs_aop_ACQ_BUYER_PAYMENT_AMOUNT_DAY_LIMIT_ERROR);
        }
        else if(reason.equals("aop.ACQ.BEYOND_PAY_RESTRICTION")){
            rea = activity.getString(R.string.rs_aop_ACQ_BEYOND_PAY_RESTRICTION);
        }
        else if(reason.equals("aop.ACQ.BEYOND_PER_RECEIPT_RESTRICTION")){
            rea = activity.getString(R.string.rs_aop_ACQ_BEYOND_PER_RECEIPT_RESTRICTION);
        }
        else if(reason.equals("aop.ACQ.BUYER_PAYMENT_AMOUNT_MONTH_LIMIT_ERROR")){
            rea = activity.getString(R.string.rs_aop_ACQ_BUYER_PAYMENT_AMOUNT_MONTH_LIMIT_ERROR);
        }
        else if(reason.equals("aop.ACQ.SELLER_BEEN_BLOCKED")){
            rea = activity.getString(R.string.rs_aop_ACQ_SELLER_BEEN_BLOCKED);
        }
        else if(reason.equals("aop.ACQ.ERROR_BUYER_CERTIFY_LEVEL_LIMIT")){
            rea = activity.getString(R.string.rs_aop_ACQ_ERROR_BUYER_CERTIFY_LEVEL_LIMIT);
        }
        else if(reason.equals("aop.ACQ.PAYMENT_REQUEST_HAS_RISK")){
            rea = activity.getString(R.string.rs_aop_ACQ_PAYMENT_REQUEST_HAS_RISK);
        }
        else if(reason.equals("aop.ACQ.NO_PAYMENT_INSTRUMENTS_AVAILABLE")){
            rea = activity.getString(R.string.rs_aop_ACQ_NO_PAYMENT_INSTRUMENTS_AVAILABLE);
        }
        else if(reason.equals("aop.ACQ.USER_FACE_PAYMENT_SWITCH_OFF")){
            rea = activity.getString(R.string.rs_aop_ACQ_USER_FACE_PAYMENT_SWITCH_OFF);
        }
        else if(reason.equals("aop.ACQ.INVALID_STORE_ID")){
            rea = activity.getString(R.string.rs_aop_ACQ_INVALID_STORE_ID);
        }
        else if(reason.equals("aop.ACQ.SUB_MERCHANT_CREATE_FAIL")){
            rea = activity.getString(R.string.rs_aop_ACQ_SUB_MERCHANT_CREATE_FAIL);
        }
        else if(reason.equals("aop.ACQ.SUB_MERCHANT_TYPE_INVALID")){
            rea = activity.getString(R.string.rs_aop_ACQ_SUB_MERCHANT_TYPE_INVALID);
        }else if(reason.equals("unknown")){
            rea = "unknown";
        }
        /** First-Data **/
        else if (reason.equals("FDMS-ERR-CODE:-1")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_1);
        }
        else if (reason.equals("FDMS-ERR-CODE:-2")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_2);
        }
        else if (reason.equals("FDMS-ERR-CODE:-3")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_3);
        }
        else if (reason.equals("FDMS-ERR-CODE:-4")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_4);
        }
        else if (reason.equals("FDMS-ERR-CODE:-5")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_5);
        }
        else if (reason.equals("FDMS-ERR-CODE:-6")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_6);
        }
        else if (reason.equals("FDMS-ERR-CODE:-7")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_7);
        }
        else if (reason.equals("FDMS-ERR-CODE:-8")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_8);
        }
        else if (reason.equals("FDMS-ERR-CODE:-9")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_9);
        }
        else if (reason.equals("FDMS-ERR-CODE:-10")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_10);
        }
        else if (reason.equals("FDMS-ERR-CODE:-11")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_11);
        }
        else if (reason.equals("FDMS-ERR-CODE:-12")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_12);
        }
        else if (reason.equals("FDMS-ERR-CODE:-13")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_13);
        }
        else if (reason.equals("FDMS-ERR-CODE:-14")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_14);
        }
        else if (reason.equals("FDMS-ERR-CODE:-15")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_15);
        }
        else if (reason.equals("FDMS-ERR-CODE:-16")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_16);
        }
        else if (reason.equals("FDMS-ERR-CODE:-17")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_17);
        }
        else if (reason.equals("FDMS-ERR-CODE:-18")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_18);
        }
        else if (reason.equals("FDMS-ERR-CODE:-19")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_19);
        }
        else if (reason.equals("FDMS-ERR-CODE:-20")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_20);
        }
        else if (reason.equals("FDMS-ERR-CODE:-21")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_21);
        }
        else if (reason.equals("FDMS-ERR-CODE:-22")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_22);
        }
        else if (reason.equals("FDMS-ERR-CODE:-23")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_23);
        }
        else if (reason.equals("FDMS-ERR-CODE:-24")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_24);
        }
        else if (reason.equals("FDMS-ERR-CODE:-25")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_25);
        }
        else if (reason.equals("FDMS-ERR-CODE:-26")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_26);
        }
        else if (reason.equals("FDMS-ERR-CODE:-27")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_27);
        }
        else if (reason.equals("FDMS-ERR-CODE:-28")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_28);
        }
        else if (reason.equals("FDMS-ERR-CODE:-29")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_29);
        }
        else if (reason.equals("FDMS-ERR-CODE:-30")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_30);
        }
        else if (reason.equals("FDMS-ERR-CODE:-31")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_31);
        }
        else if (reason.equals("FDMS-ERR-CODE:-32")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_32);
        }
        else if (reason.equals("FDMS-ERR-CODE:-33")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_33);
        }
        else if (reason.equals("FDMS-ERR-CODE:-34")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_34);
        }
        else if (reason.equals("FDMS-ERR-CODE:-35")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_35);
        }
        else if (reason.equals("FDMS-ERR-CODE:-36")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_36);
        }
        else if (reason.equals("FDMS-ERR-CODE:-37")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_37);
        }
        else if (reason.equals("FDMS-ERR-CODE:-38")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_38);
        }
        else if (reason.equals("FDMS-ERR-CODE:-39")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_39);
        }
        else if (reason.equals("FDMS-ERR-CODE:-40")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_40);
        }
        else if (reason.equals("FDMS-ERR-CODE:-41")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_41);
        }
        else if (reason.equals("FDMS-ERR-CODE:-42")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_42);
        }
        else if (reason.equals("FDMS-ERR-CODE:-43")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_43);
        }
        else if (reason.equals("FDMS-ERR-CODE:-44")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_44);
        }
        else if (reason.equals("FDMS-ERR-CODE:-45")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_45);
        }
        else if (reason.equals("FDMS-ERR-CODE:-46")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_46);
        }
        else if (reason.equals("FDMS-ERR-CODE:-47")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_47);
        }
        else if (reason.equals("FDMS-ERR-CODE:-48")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_48);
        }
        else if (reason.equals("FDMS-ERR-CODE:-49")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_49);
        }
        else if (reason.equals("FDMS-ERR-CODE:-50")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_50);
        }
        else if (reason.equals("FDMS-ERR-CODE:-51")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_51);
        }
        else if (reason.equals("FDMS-ERR-CODE:-52")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_52);
        }
        else if (reason.equals("FDMS-ERR-CODE:-53")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_53);
        }
        else if (reason.equals("FDMS-ERR-CODE:-54")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_54);
        }
        else if (reason.equals("FDMS-ERR-CODE:-55")) {
            rea = activity.getString(R.string.FDMS_ERR_CODE_55);
        }
        /** End of First-Data **/
        else{
            rea = activity.getString(R.string.rs_SYSTEMERROR);
        }
    return rea;
    }

    public static String failadvise(String reason, Activity activity){
        String rea = "";
        if(reason.equals("REFUND")){
            rea = activity.getString(R.string.sol_REFUND);
        }
        else if(reason.equals("NOTPAY")){
            rea = activity.getString(R.string.sol_NOTPAY);
        }
        else if(reason.equals("CLOSED")){
            rea = activity.getString(R.string.sol_CLOSED);
        }
        else if(reason.equals("REVOKED")){
            rea = activity.getString(R.string.sol_REVOKED);
        }
        else if(reason.equals("PAYERROR")){
            rea = activity.getString(R.string.sol_PAYERROR);
        }
        else if(reason.equals("SYSTEMERROR")){
            rea = activity.getString(R.string.sol_SYSTEMERROR);
        }
        else if(reason.equals("PARAM_ERROR")){
            rea = activity.getString(R.string.sol_PARAM_ERROR);
        }
        else if(reason.equals("ORDERPAID")){
            rea = activity.getString(R.string.sol_ORDERPAID);
        }
        else if(reason.equals("NOAUTH")){
            rea = activity.getString(R.string.sol_NOAUTH);
        }
        else if(reason.equals("AUTHCODEEXPIRE")){
            rea = activity.getString(R.string.sol_AUTHCODEEXPIRE);
        }
        else if(reason.equals("NOTENOUGH")){
            rea = activity.getString(R.string.sol_NOTENOUGH);
        }
        else if(reason.equals("NOTSUPORTCARD")){
            rea = activity.getString(R.string.sol_NOTSUPORTCARD);
        }
        else if(reason.equals("ORDERCLOSED")){
            rea = activity.getString(R.string.sol_ORDERCLOSED);
        }
        else if(reason.equals("ORDERREVERSED")){
            rea = activity.getString(R.string.sol_ORDERREVERSED);
        }
        else if(reason.equals("BANKERROR")){
            rea = activity.getString(R.string.sol_BANKERROR);
        }
        else if(reason.equals("USERPAYING")){
            rea = activity.getString(R.string.sol_USERPAYING);
        }
        else if(reason.equals("AUTH_CODE_ERROR")){
            rea = activity.getString(R.string.sol_AUTH_CODE_ERROR);
        }
        else if(reason.equals("AUTH_CODE_INVALID")){
            rea = activity.getString(R.string.sol_AUTH_CODE_INVALID);
        }
        else if(reason.equals("XML_FORMAT_ERROR")){
            rea = activity.getString(R.string.sol_XML_FORMAT_ERROR);
        }
        else if(reason.equals("REQUIRE_POST_METHOD")){
            rea = activity.getString(R.string.sol_REQUIRE_POST_METHOD);
        }
        else if(reason.equals("SIGNERROR")){
            rea = activity.getString(R.string.sol_SIGNERROR);
        }
        else if(reason.equals("LACK_PARAMS")){
            rea = activity.getString(R.string.sol_LACK_PARAMS);
        }
        else if(reason.equals("NOT_UTF8")){
            rea = activity.getString(R.string.sol_NOT_UTF8);
        }
        else if(reason.equals("BUYER_MISMATCH")){
            rea = activity.getString(R.string.sol_BUYER_MISMATCH);
        }
        else if(reason.equals("APPID_NOT_EXIST")){
            rea = activity.getString(R.string.sol_APPID_NOT_EXIST);
        }
        else if(reason.equals("MCHID_NOT_EXIST")){
            rea = activity.getString(R.string.sol_MCHID_NOT_EXIST);
        }
        else if(reason.equals("OUT_TRADE_NO_USED")){
            rea = activity.getString(R.string.sol_OUT_TRADE_NO_USED);
        }
        else if(reason.equals("APPID_MCHID_NOT_MATCH")){
            rea = activity.getString(R.string.sol_APPID_MCHID_NOT_MATCH);
        }
        else if(reason.equals("SYSTEM_ERROR")){
            rea = activity.getString(R.string.sol_SYSTEM_ERROR);
        }
        else if(reason.equals("ILLEGAL_SIGN")){
            rea = activity.getString(R.string.sol_ILLEGAL_SIGN);
        }
        else if(reason.equals("INVALID_PARAMETER")){
            rea = activity.getString(R.string.sol_INVALID_PARAMETER);
        }
        else if(reason.equals("ILLEGAL_ARGUMENT")){
            rea = activity.getString(R.string.sol_ILLEGAL_ARGUMENT);
        }
        else if(reason.equals("ILLEGAL_PARTNER")){
            rea = activity.getString(R.string.sol_ILLEGAL_PARTNER);
        }
        else if(reason.equals("ILLEGAL_EXTERFACE")){
            rea = activity.getString(R.string.sol_ILLEGAL_EXTERFACE);
        }
        else if(reason.equals("ILLEGAL_PARTNER_EXTERFACE")){
            rea = activity.getString(R.string.sol_ILLEGAL_PARTNER_EXTERFACE);
        }
        else if(reason.equals("ILLEGAL_SIGN_TYPE")){
            rea = activity.getString(R.string.sol_ILLEGAL_SIGN_TYPE);
        }
        else if(reason.equals("HAS_NO_PRIVILEGE")){
            rea = activity.getString(R.string.sol_HAS_NO_PRIVILEGE);
        }
        else if(reason.equals("TRADE_BUYER_NOT_MATCH")){
            rea = activity.getString(R.string.sol_TRADE_BUYER_NOT_MATCH);
        }
        else if(reason.equals("TRADE_HAS_CLOSE")){
            rea = activity.getString(R.string.sol_TRADE_HAS_CLOSE);
        }
        else if(reason.equals("TRADE_STATUS_ERROR")){
            rea = activity.getString(R.string.sol_TRADE_STATUS_ERROR);
        }
        else if(reason.equals("EXIST_FORBIDDEN_WORD")){
            rea = activity.getString(R.string.sol_EXIST_FORBIDDEN_WORD);
        }
        else if(reason.equals("SELLER_NOT_EXIST")){
            rea = activity.getString(R.string.sol_SELLER_NOT_EXIST);
        }
        else if(reason.equals("BUYER_NOT_EXIST")){
            rea = activity.getString(R.string.sol_BUYER_NOT_EXIST);
        }
        else if(reason.equals("BUYER_ENABLE_STATUS_FORBID")){
            rea = activity.getString(R.string.sol_BUYER_ENABLE_STATUS_FORBID);
        }
        else if(reason.equals("BUYER_SELLER_EQUAL")){
            rea = activity.getString(R.string.sol_BUYER_SELLER_EQUAL);
        }
        else if(reason.equals("CLIENT_VERSION_NOT_MATCH")){
            rea = activity.getString(R.string.sol_CLIENT_VERSION_NOT_MATCH);
        }
        else if(reason.equals("SOUNDWAVE_PARSER_FAIL")){
            rea = activity.getString(R.string.sol_SOUNDWAVE_PARSER_FAIL);
        }
        else if(reason.equals("CONTEXT_INCONSISTENT")){
            rea = activity.getString(R.string.sol_CONTEXT_INCONSISTENT);
        }
        else if(reason.equals("PRODUCT_AMOUNT_LIMIT_ERROR")){
            rea = activity.getString(R.string.sol_PRODUCT_AMOUNT_LIMIT_ERROR);
        }
        else if(reason.equals("BUYER_BALANCE_NOT_ENOUGH")){
            rea = activity.getString(R.string.sol_BUYER_BALANCE_NOT_ENOUGH);
        }
        else if(reason.equals("TOTAL_FEE_EXCEED")){
            rea = activity.getString(R.string.sol_TOTAL_FEE_EXCEED);
        }
        else if(reason.equals("BUYER_PAYMENT_AMOUNT_DAY_LIMIT_ERROR")){
            rea = activity.getString(R.string.sol_BUYER_PAYMENT_AMOUNT_DAY_LIMIT_ERROR);
        }
        else if(reason.equals("BUYER_PAYMENT_AMOUNT_MONTH_LIMIT_ERROR")){
            rea = activity.getString(R.string.sol_BUYER_PAYMENT_AMOUNT_MONTH_LIMIT_ERROR);
        }
        else if(reason.equals("ERROR_BUYER_CERTIFY_LEVEL_LIMIT")){
            rea = activity.getString(R.string.sol_ERROR_BUYER_CERTIFY_LEVEL_LIMIT);
        }
        else if(reason.equals("ERROR_SELLER_CERTIFY_LEVEL_LIMIT")){
            rea = activity.getString(R.string.sol_ERROR_SELLER_CERTIFY_LEVEL_LIMIT);
        }
        else if(reason.equals("BUYER_BANKCARD_BALANCE_NOT_ENOUGH")){
            rea = activity.getString(R.string.sol_BUYER_BANKCARD_BALANCE_NOT_ENOUGH);
        }
        else if(reason.equals("PAYMENT_FAIL")){
            rea = activity.getString(R.string.sol_PAYMENT_FAIL);
        }
        else if(reason.equals("MOBILE_PAYMENT_SWITCH_OFF")){
            rea = activity.getString(R.string.sol_MOBILE_PAYMENT_SWITCH_OFF);
        }
        else if(reason.equals("USER_FACE_PAYMENT_SWITCH_OFF")){
            rea = activity.getString(R.string.sol_USER_FACE_PAYMENT_SWITCH_OFF);
        }
        else if(reason.equals("ERROR_BALANCE_PAYMENT_DISABLE")){
            rea = activity.getString(R.string.sol_ERROR_BALANCE_PAYMENT_DISABLE);
        }
        else if(reason.equals("EXCHANGE_AMOUNT_OR_CURRENCY_ERROR")){
            rea = activity.getString(R.string.sol_EXCHANGE_AMOUNT_OR_CURRENCY_ERROR);
        }
        else if(reason.equals("aop.ACQ.SYSTEM_ERROR")){
            rea = activity.getString(R.string.sol_aop_ACQ_SYSTEM_ERROR);
        }
        else if(reason.equals("aop.ACQ.INVALID_PARAMETER")){
            rea = activity.getString(R.string.sol_aop_ACQ_INVALID_PARAMETER);
        }
        else if(reason.equals("aop.ACQ.ACCESS_FORBIDDEN")){
            rea = activity.getString(R.string.sol_aop_ACQ_ACCESS_FORBIDDEN);
        }
        else if(reason.equals("aop.ACQ.EXIST_FORBIDDEN_WORD")){
            rea = activity.getString(R.string.sol_aop_ACQ_EXIST_FORBIDDEN_WORD);
        }
        else if(reason.equals("aop.ACQ.PARTNER_ERROR")){
            rea = activity.getString(R.string.sol_aop_ACQ_PARTNER_ERROR);
        }
        else if(reason.equals("aop.ACQ.TOTAL_FEE_EXCEED")){
            rea = activity.getString(R.string.sol_aop_ACQ_TOTAL_FEE_EXCEED);
        }
        else if(reason.equals("aop.ACQ.PAYMENT_AUTH_CODE_INVALID")){
            rea = activity.getString(R.string.sol_aop_ACQ_PAYMENT_AUTH_CODE_INVALID);
        }
        else if(reason.equals("aop.ACQ.CONTEXT_INCONSISTENT")){
            rea = activity.getString(R.string.sol_aop_ACQ_CONTEXT_INCONSISTENT);
        }
        else if(reason.equals("aop.ACQ.TRADE_HAS_SUCCESS")){
            rea = activity.getString(R.string.sol_aop_ACQ_TRADE_HAS_SUCCESS);
        }
        else if(reason.equals("aop.ACQ.TRADE_HAS_CLOSE")){
            rea = activity.getString(R.string.sol_aop_ACQ_TRADE_HAS_CLOSE);
        }
        else if(reason.equals("aop.ACQ.BUYER_BALANCE_NOT_ENOUGH")){
            rea = activity.getString(R.string.sol_aop_ACQ_BUYER_BALANCE_NOT_ENOUGH);
        }
        else if(reason.equals("aop.ACQ.BUYER_BANKCARD_BALANCE_NOT_ENOUGH")){
            rea = activity.getString(R.string.sol_aop_ACQ_BUYER_BANKCARD_BALANCE_NOT_ENOUGH);
        }
        else if(reason.equals("aop.ACQ.ERROR_BALANCE_PAYMENT_DISABLE")){
            rea = activity.getString(R.string.sol_aop_ACQ_ERROR_BALANCE_PAYMENT_DISABLE);
        }
        else if(reason.equals("aop.ACQ.BUYER_SELLER_EQUAL")){
            rea = activity.getString(R.string.sol_aop_ACQ_BUYER_SELLER_EQUAL);
        }
        else if(reason.equals("aop.ACQ.TRADE_BUYER_NOT_MATCH")){
            rea = activity.getString(R.string.sol_aop_ACQ_TRADE_BUYER_NOT_MATCH);
        }
        else if(reason.equals("aop.ACQ.BUYER_ENABLE_STATUS_FORBID")){
            rea = activity.getString(R.string.sol_aop_ACQ_BUYER_ENABLE_STATUS_FORBID);
        }
        else if(reason.equals("aop.ACQ.PULL_MOBILE_CASHIER_FAIL")){
            rea = activity.getString(R.string.sol_aop_ACQ_PULL_MOBILE_CASHIER_FAIL);
        }
        else if(reason.equals("aop.ACQ.MOBILE_PAYMENT_SWITCH_OFF")){
            rea = activity.getString(R.string.sol_aop_ACQ_MOBILE_PAYMENT_SWITCH_OFF);
        }
        else if(reason.equals("aop.ACQ.PAYMENT_FAIL")){
            rea = activity.getString(R.string.sol_aop_ACQ_PAYMENT_FAIL);
        }
        else if(reason.equals("aop.ACQ.BUYER_PAYMENT_AMOUNT_DAY_LIMIT_ERROR")){
            rea = activity.getString(R.string.sol_aop_ACQ_BUYER_PAYMENT_AMOUNT_DAY_LIMIT_ERROR);
        }
        else if(reason.equals("aop.ACQ.BEYOND_PAY_RESTRICTION")){
            rea = activity.getString(R.string.sol_aop_ACQ_BEYOND_PAY_RESTRICTION);
        }
        else if(reason.equals("aop.ACQ.BEYOND_PER_RECEIPT_RESTRICTION")){
            rea = activity.getString(R.string.sol_aop_ACQ_BEYOND_PER_RECEIPT_RESTRICTION);
        }
        else if(reason.equals("aop.ACQ.BUYER_PAYMENT_AMOUNT_MONTH_LIMIT_ERROR")){
            rea = activity.getString(R.string.sol_aop_ACQ_BUYER_PAYMENT_AMOUNT_MONTH_LIMIT_ERROR);
        }
        else if(reason.equals("aop.ACQ.SELLER_BEEN_BLOCKED")){
            rea = activity.getString(R.string.sol_aop_ACQ_SELLER_BEEN_BLOCKED);
        }
        else if(reason.equals("aop.ACQ.ERROR_BUYER_CERTIFY_LEVEL_LIMIT")){
            rea = activity.getString(R.string.sol_aop_ACQ_ERROR_BUYER_CERTIFY_LEVEL_LIMIT);
        }
        else if(reason.equals("aop.ACQ.PAYMENT_REQUEST_HAS_RISK")){
            rea = activity.getString(R.string.sol_aop_ACQ_PAYMENT_REQUEST_HAS_RISK);
        }
        else if(reason.equals("aop.ACQ.NO_PAYMENT_INSTRUMENTS_AVAILABLE")){
            rea = activity.getString(R.string.sol_aop_ACQ_NO_PAYMENT_INSTRUMENTS_AVAILABLE);
        }
        else if(reason.equals("aop.ACQ.USER_FACE_PAYMENT_SWITCH_OFF")){
            rea = activity.getString(R.string.sol_aop_ACQ_USER_FACE_PAYMENT_SWITCH_OFF);
        }
        else if(reason.equals("aop.ACQ.INVALID_STORE_ID")){
            rea = activity.getString(R.string.sol_aop_ACQ_INVALID_STORE_ID);
        }
        else if(reason.equals("aop.ACQ.SUB_MERCHANT_CREATE_FAIL")){
            rea = activity.getString(R.string.sol_aop_ACQ_SUB_MERCHANT_CREATE_FAIL);
        }
        else if(reason.equals("aop.ACQ.SUB_MERCHANT_TYPE_INVALID")){
            rea = activity.getString(R.string.sol_aop_ACQ_SUB_MERCHANT_TYPE_INVALID);
        }
        else if (reason.equals("FDMS-ERR-CODE:-1")) {
            rea = activity.getString(R.string.FDMS_ADVISE_CODE_1);
        }
        else if (reason.equals("FDMS-ERR-CODE:-22")) {
            rea = activity.getString(R.string.FDMS_ADVISE_CODE_22);
        }
        else if (reason.equals("FDMS-ERR-CODE:-33")) {
            rea = activity.getString(R.string.FDMS_ADVISE_CODE_33);
        }
        else if (reason.equals("FDMS-ERR-CODE:-37")) {
            rea = activity.getString(R.string.FDMS_ADVISE_CODE_37);
        }
        else if (reason.equals("FDMS-ERR-CODE:-39")) {
            rea = activity.getString(R.string.FDMS_ADVISE_CODE_39);
        }
        else{
            rea = activity.getString(R.string.sol_SYSTEMERROR);
        }
    return rea;
    }
}
