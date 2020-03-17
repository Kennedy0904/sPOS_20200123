package com.example.dell.smartpos.CardModule.tradepaypw;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;

import com.example.dell.smartpos.CardModule.app.TradeApplication;
import com.example.dell.smartpos.CardPaymentTask;
import com.example.dell.smartpos.CardPayment_Failure;
import com.example.dell.smartpos.CardPayment_Success;
import com.example.dell.smartpos.CardSchemeValidation;
import com.example.dell.smartpos.CardValidation;
import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;
import com.example.dell.smartpos.Signature;
import com.pax.dal.entity.EReaderType;
import com.pax.jemv.amex.api.ClssAmexApi;
import com.pax.jemv.clcommon.ByteArray;
import com.pax.jemv.clcommon.KernType;
import com.pax.jemv.clcommon.TransactionPath;

import com.pax.jemv.dpas.api.ClssDPASApi;
import com.pax.jemv.emv.api.EMVCallback;
import com.pax.jemv.jcb.api.ClssJCBApi;
import com.pax.jemv.paypass.api.ClssPassApi;
import com.pax.jemv.paywave.api.ClssWaveApi;
import com.pax.jemv.pure.api.ClssPUREApi;
import com.pax.jemv.qpboc.api.ClssPbocApi;

import com.example.dell.smartpos.CardModule.jemv.clssentrypoint.trans.ClssEntryPoint;
import com.example.dell.smartpos.CardModule.jemv.clssquickpass.trans.ClssQuickPass;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.example.dell.smartpos.CardModule.tradepaypw.utils.Utils.bcd2Str;

public class TradeResult{
    private static final String TAG = "TradeResult";

    private ClssEntryPoint entryPoint = ClssEntryPoint.getInstance();

    Activity activity;
    HashMap<String, String> map;

    //Card Scheme available
    String cardScheme;
    public static String visa_BIN = "";
    public static String master_BIN = "";
    public static String cup_BIN = "";
    public static String amex_BIN = "";
    public static String jcb_BIN = "";
    public static String diners_BIN = "";
    public static String cardSchemeRange = "";
    public static boolean visa_cardScheme = false;
    public static boolean master_cardScheme = false;;
    public static boolean cup_cardScheme = false;
    public static boolean amex_cardScheme = false;
    public static boolean jcb_cardScheme = false;
    public static boolean diners_cardScheme = false;
    boolean isVisaRange = false;
    boolean isMasterRange = false;
    boolean isAMEXRange = false;
    boolean isJCBRange = false;
    boolean isCUPRange = false;
    boolean isDinersRange = false;
    private static String[] rangeStart = new String[100];
    private static String[] rangeEnd = new String[100];

    //Receipt Masking Rule
    String visaPanMaskingRule;
    String visaExpiryMaskingRule;
    String masterPanMaskingRule;
    String masterExpiryMaskingRule;
    String cupPanMaskingRule;
    String cupExpiryMaskingRule;
    String amexPanMaskingRule;
    String amexExpiryMaskingRule;
    String jcbPanMaskingRule;
    String jcbExpiryMaskingRule;
    String dinersPanMaskingRule;
    String dinersExpiryMaskingRule;

    private static final String EMVDATA = "SwingCardEMVData";
    ProgressDialog progressDialog;

    //EMV Data
    String cardno = "";
    String holder = "";
    String expireMonth = "";
    String expireYear = "";
    String POSEntryMode = "";
    String ProcessingCode = "";
    String PANSeqNo = "";
    String POSCondtionCode = "";
    String Track2Data = "";;
    String EncryptedPIN = "";
    String EMVICCRelatedData = "";
    String CVV2Data ="";

    String amount;
    String merId;
    String merchantName;
    String payMethod;
    String currCode;
    public static String payType;
    String merRef;
    String entryMode;
    String surcharge;
    String operator;
    String channel;
    String hideSurcharge;
    String action;
    String traceNo;
    String batchNo;
    String terminalID;
    String pan;
    String epMonth;
    String epYear;
    String ARQC = null;
    String TVR = null;
    String AID = "";
    String AppLable = "";
    String AppName = "";
    String TSI = null;
    String TC = null;
    String ATC = null;
    String CURR = null;
    String AIP = null;
    String TXND = null;
    String TXNT = null;
    String IAD = null;
    String TCC = null;
    String CID = null;
    String UN = null;
    String PANsequence = null;
    String txnAmount = null;
    String otherAmount = null;
    String IST1 = null;
    String AID2 = null;
    String IAD2 = null;
    String TAVN = null;
    String ID = null;
    String TC2 = null;
    String CVM = null;
    String TT = null;
    String TSC = null;
    String TCC2 = null;
    String cardHolder = "";
    String CVM_Result = "";

    public TradeResult(Activity activity, HashMap<String, String> map){
        this.activity = activity;
        this.map = map;
    }

    public void initData() {

        String arqc = null;
        String tvr = null;
        String aid = null;
        String appLable = null;
        String appName = null;
        String tsi = null;
        String tc = null;
        String atc = null;
        int iRet;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ByteArray byteArray = new ByteArray();
        Log.i(TAG, "entryPoint.getOutParam().ucKernType = " + entryPoint.getOutParam().ucKernType);
        if (SwingCardActivity.getReadType() == EReaderType.PICC) {
            if (entryPoint.getOutParam().ucKernType == KernType.KERNTYPE_MC) {

                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x26}, (byte) 2, 10, byteArray);
                byte[] a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                arqc = bcd2Str(a);
                iRet = ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x95}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tvr = bcd2Str(a);
                Log.i("Clss_TLV_MC iRet 0x95", Integer.toString(iRet));
                Log.i("Clss_GetTLV_MC TVR 0x95", tvr + "");
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x4F}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                aid = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x50}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appLable = new String(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x12}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appName = new String(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9B}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tsi = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x26}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tc = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x36}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                atc = bcd2Str(a);

                // EMV related data
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x5F, 0x2A}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                CURR = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x82}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                AIP = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x84}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                AID2 = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x95}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TVR = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9A}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TXND = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9C}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TXNT = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x02}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                txnAmount = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x03}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                otherAmount = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x09}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TAVN = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x10}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                IAD = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x1A}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TCC = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x1E}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                ID = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x26}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                ARQC = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x27}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                CID = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x33}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TC2 = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x34}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                CVM_Result = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x35}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TT = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x36}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                ATC = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x37}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                UN = bcd2Str(a);
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x9F, 0x41}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TSC = bcd2Str(a);
                // PANSequence (5F34)
                ClssPassApi.Clss_GetTLVDataList_MC(new byte[]{(byte) 0x5F, 0x34}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                PANsequence = bcd2Str(a);

            } else if (entryPoint.getOutParam().ucKernType == KernType.KERNTYPE_VIS) {

                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F26, byteArray);
                byte[] a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                arqc = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x95, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tvr = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x4F, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                aid = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x50, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appLable = new String(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F12, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appName = new String(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9B, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tsi = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F26, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tc = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F36, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                atc = bcd2Str(a);

                // EMV Related Data
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x5F2A, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                CURR = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x82, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                AIP = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x84, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                AID2 = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x95, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TVR = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9A, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TXND = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9C, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TXNT = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F02, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                txnAmount = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F03, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                otherAmount = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F09, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TAVN = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                IAD = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F1A, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TCC = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F1E, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                ID = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F26, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                ARQC = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F27, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                CID = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F33, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TC2 = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F34, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                CVM_Result = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F35, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TT = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F36, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                ATC = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F37, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                UN = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x9F41, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                TSC = bcd2Str(a);
                ClssWaveApi.Clss_GetTLVData_Wave((short) 0x5F34, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                PANsequence = bcd2Str(a);

            } else if (entryPoint.getOutParam().ucKernType == KernType.KERNTYPE_AE) {

                ClssAmexApi.Clss_GetTLVData_AE((short) 0x9F26, byteArray);
                byte[] a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                arqc = bcd2Str(a);
                ClssAmexApi.Clss_GetTLVData_AE((short) 0x95, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tvr = bcd2Str(a);
                ClssAmexApi.Clss_GetTLVData_AE((short) 0x4F, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                aid = bcd2Str(a);
                ClssAmexApi.Clss_GetTLVData_AE((short) 0x50, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appLable = new String(a);
                ClssAmexApi.Clss_GetTLVData_AE((short) 0x9F12, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appName = new String(a);
                ClssAmexApi.Clss_GetTLVData_AE((short) 0x9B, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tsi = bcd2Str(a);
                ClssAmexApi.Clss_GetTLVData_AE((short) 0x9F26, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tc = bcd2Str(a);
                ClssAmexApi.Clss_GetTLVData_AE((short) 0x9F36, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                atc = bcd2Str(a);

            } else if (entryPoint.getOutParam().ucKernType == KernType.KERNTYPE_ZIP) {
                ClssDPASApi.Clss_GetTLVDataList_DPAS(new byte[]{(byte) 0x9F, 0x26}, (byte) 2, 10, byteArray);
                byte[] a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                arqc = bcd2Str(a);
                ClssDPASApi.Clss_GetTLVDataList_DPAS(new byte[]{(byte) 0x95}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tvr = bcd2Str(a);
                ClssDPASApi.Clss_GetTLVDataList_DPAS(new byte[]{(byte) 0x4F}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                aid = bcd2Str(a);
                ClssDPASApi.Clss_GetTLVDataList_DPAS(new byte[]{(byte) 0x50}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appLable = new String(a);
                ClssDPASApi.Clss_GetTLVDataList_DPAS(new byte[]{(byte) 0x9F, 0x12}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appName = new String(a);
                ClssDPASApi.Clss_GetTLVDataList_DPAS(new byte[]{(byte) 0x9B}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tsi = bcd2Str(a);
                ClssDPASApi.Clss_GetTLVDataList_DPAS(new byte[]{(byte) 0x9F, 0x26}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tc = bcd2Str(a);
                ClssDPASApi.Clss_GetTLVDataList_DPAS(new byte[]{(byte) 0x9F, 0x36}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                atc = bcd2Str(a);
            } else if ((entryPoint.getOutParam().ucKernType == KernType.KERNTYPE_PBOC) &&
                    (ClssQuickPass.getInstance().getTransPath() == TransactionPath.CLSS_VISA_QVSDC)) {
                ClssPbocApi.Clss_GetTLVData_Pboc((short) 0x9F26, byteArray);
                byte[] a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                arqc = bcd2Str(a);
                ClssPbocApi.Clss_GetTLVData_Pboc((short) 0x95, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tvr = bcd2Str(a);
                ClssPbocApi.Clss_GetTLVData_Pboc((short) 0x4F, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                aid = bcd2Str(a);
                ClssPbocApi.Clss_GetTLVData_Pboc((short) 0x50, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appLable = new String(a);
                ClssPbocApi.Clss_GetTLVData_Pboc((short) 0x9F12, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appName = new String(a);
                ClssPbocApi.Clss_GetTLVData_Pboc((short) 0x9B, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tsi = bcd2Str(a);
                ClssPbocApi.Clss_GetTLVData_Pboc((short) 0x9F26, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tc = bcd2Str(a);
                ClssPbocApi.Clss_GetTLVData_Pboc((short) 0x9F36, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                atc = bcd2Str(a);

            } else if (entryPoint.getOutParam().ucKernType == KernType.KERNTYPE_JCB) {

                ClssJCBApi.Clss_GetTLVDataList_JCB(new byte[]{(byte) 0x9F, 0x26}, (byte) 2, 10, byteArray);
                byte[] a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                arqc = bcd2Str(a);
                byteArray = new ByteArray();
                iRet = ClssJCBApi.Clss_GetTLVDataList_JCB(new byte[]{(byte) 0x95}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tvr = bcd2Str(a);
                Log.i("Clss_TLV_MC iRet 0x95", Integer.toString(iRet));
                Log.i("Clss_GetTLV_MC TVR 0x95", tvr + "");
                byteArray = new ByteArray();
                ClssJCBApi.Clss_GetTLVDataList_JCB(new byte[]{(byte) 0x4F}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                aid = bcd2Str(a);
                byteArray = new ByteArray();
                ClssJCBApi.Clss_GetTLVDataList_JCB(new byte[]{(byte) 0x50}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appLable = new String(a);
                byteArray = new ByteArray();
                ClssJCBApi.Clss_GetTLVDataList_JCB(new byte[]{(byte) 0x9F, 0x12}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appName = new String(a);
                byteArray = new ByteArray();
                ClssJCBApi.Clss_GetTLVDataList_JCB(new byte[]{(byte) 0x9B}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tsi = bcd2Str(a);
                byteArray = new ByteArray();
                ClssJCBApi.Clss_GetTLVDataList_JCB(new byte[]{(byte) 0x9F, 0x26}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tc = bcd2Str(a);
                byteArray = new ByteArray();
                ClssJCBApi.Clss_GetTLVDataList_JCB(new byte[]{(byte) 0x9F, 0x36}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                atc = bcd2Str(a);

                // PANSequence (5F34)
                ClssJCBApi.Clss_GetTLVDataList_JCB(new byte[]{(byte) 0x5F, 0x34}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                PANsequence = bcd2Str(a);

            } else if (entryPoint.getOutParam().ucKernType == KernType.KERNTYPE_PURE) {
                ClssPUREApi.Clss_GetTLVDataList_PURE(new byte[]{(byte) 0x9F, 0x26}, (byte) 2, 10, byteArray);
                byte[] a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                arqc = bcd2Str(a);
                byteArray = new ByteArray();
                iRet = ClssPUREApi.Clss_GetTLVDataList_PURE(new byte[]{(byte) 0x95}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tvr = bcd2Str(a);
                Log.i("Clss_TLV_MC iRet 0x95", Integer.toString(iRet));
                Log.i("Clss_GetTLV_MC TVR 0x95", tvr + "");
                byteArray = new ByteArray();
                ClssPUREApi.Clss_GetTLVDataList_PURE(new byte[]{(byte) 0x4F}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                aid = bcd2Str(a);
                byteArray = new ByteArray();
                ClssPUREApi.Clss_GetTLVDataList_PURE(new byte[]{(byte) 0x50}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appLable = new String(a);
                byteArray = new ByteArray();
                ClssPUREApi.Clss_GetTLVDataList_PURE(new byte[]{(byte) 0x9F, 0x12}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                appName = new String(a);
                byteArray = new ByteArray();
                ClssPUREApi.Clss_GetTLVDataList_PURE(new byte[]{(byte) 0x9B}, (byte) 1, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tsi = bcd2Str(a);
                byteArray = new ByteArray();
                ClssPUREApi.Clss_GetTLVDataList_PURE(new byte[]{(byte) 0x9F, 0x26}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                tc = bcd2Str(a);
                byteArray = new ByteArray();
                ClssPUREApi.Clss_GetTLVDataList_PURE(new byte[]{(byte) 0x9F, 0x36}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                atc = bcd2Str(a);

                // PANSequence (5F34)
                ClssPUREApi.Clss_GetTLVDataList_PURE(new byte[]{(byte) 0x5F, 0x34}, (byte) 2, 10, byteArray);
                a = new byte[byteArray.length];
                System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
                PANsequence = bcd2Str(a);
            }
        }

        if ((SwingCardActivity.getReadType() == EReaderType.ICC) ||
                ((ClssEntryPoint.getInstance().getOutParam().ucKernType == KernType.KERNTYPE_PBOC) && (ClssQuickPass.getInstance().getTransPath() == TransactionPath.CLSS_VISA_VSDC))) { // contact
            EMVCallback.EMVGetTLVData((short) 0x9F26, byteArray);
            byte[] a = new byte[byteArray.length];
            System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
            arqc = bcd2Str(a);
            EMVCallback.EMVGetTLVData((short) 0x95, byteArray);
            a = new byte[byteArray.length];
            System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
            tvr = bcd2Str(a);
            EMVCallback.EMVGetTLVData((short) 0x4F, byteArray);
            a = new byte[byteArray.length];
            System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
            aid = bcd2Str(a);
            EMVCallback.EMVGetTLVData((short) 0x50, byteArray);
            a = new byte[byteArray.length];
            System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
            appLable = bcd2Str(a);
            EMVCallback.EMVGetTLVData((short) 0x9F12, byteArray);
            a = new byte[byteArray.length];
            System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
            appName = bcd2Str(a);
            EMVCallback.EMVGetTLVData((short) 0x9B, byteArray);
            a = new byte[byteArray.length];
            System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
            tsi = bcd2Str(a);
            EMVCallback.EMVGetTLVData((short) 0x9F26, byteArray);
            a = new byte[byteArray.length];
            System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
            tc = bcd2Str(a);
            EMVCallback.EMVGetTLVData((short) 0x9F36, byteArray);
            a = new byte[byteArray.length];
            System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
            atc = bcd2Str(a);

            // PAN Sequence
            EMVCallback.EMVGetTLVData((short) 0x5F34, byteArray);
            a = new byte[byteArray.length];
            System.arraycopy(byteArray.data, 0, a, 0, byteArray.length);
            PANsequence = bcd2Str(a);
        }

        // Validate Card Scheme (PICC)
        CardSchemeValidation validation = new CardSchemeValidation(activity);
        int BIN = Integer.valueOf(map.get(Constants.CARDNO).substring(0,6));
        payMethod = validation.checkCardScheme(BIN);
        if(payMethod != null){
            boolean validCardScheme = validation.validateCardScheme(payMethod, map.get(Constants.PAYTYPE));
            if(validCardScheme){
                postCardData();
            }
        }
    }

    public String getPrefPayGate() {
        SharedPreferences prefsettings = activity
                .getSharedPreferences("Settings", MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate,
                Constants.default_paygate);
        return prefpaygate;
    }

    public String calcDE55FieldLength(String data){

        int datalength = data.length() / 2;

        String hexData = Integer.toHexString(datalength);

        if(hexData.length() == 1){
            hexData = String.format("%02d", Integer.parseInt(hexData));
        }

        return  hexData;
    }

    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

    //Check card scheme available
    private void checkCardScheme(){

        SharedPreferences prefsettings = activity.getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);
        cardScheme = prefsettings.getString(Constants.card_scheme, "").toLowerCase();

        if (cardScheme.contains("-visa-")) {
            visa_cardScheme = true;
            visa_BIN = prefsettings.getString(Constants.visa_bin_range, Constants.default_visa_bin_range);
        }

        if (cardScheme.contains("-mastercard-")) {
            master_cardScheme = true;
            master_BIN = prefsettings.getString(Constants.mc_bin_range, Constants.default_mc_bin_range);
        }

        if (cardScheme.contains("-cup-")) {
            cup_cardScheme = true;
            cup_BIN = prefsettings.getString(Constants.cup_bin_range, Constants.default_cup_bin_range);
        }

        if (cardScheme.contains("-amex-")) {
            amex_cardScheme = true;
            amex_BIN = prefsettings.getString(Constants.amex_bin_range, Constants.default_amex_bin_range);
        }

        if (cardScheme.contains("-jcb-")) {
            jcb_cardScheme = true;
            jcb_BIN = prefsettings.getString(Constants.jcb_bin_range, Constants.default_jcb_bin_range);
        }

        if (cardScheme.contains("-diners-")) {
            diners_cardScheme = true;
            diners_BIN = prefsettings.getString(Constants.diners_bin_range, Constants.default_diners_bin_range);
        }
    }

    private String cardScheme(String BIN){
        int intBIN = Integer.valueOf(BIN);
        String cardscheme = null;
        checkCardScheme();
        isVisaRange = cardSchemeRange(visa_BIN, intBIN);
        isMasterRange = cardSchemeRange(master_BIN, intBIN);
        isCUPRange = cardSchemeRange(cup_BIN, intBIN);
        isAMEXRange = cardSchemeRange(amex_BIN, intBIN);
        isJCBRange = cardSchemeRange(jcb_BIN, intBIN);
        isDinersRange = cardSchemeRange(diners_BIN, intBIN);

        if(isVisaRange){
            cardscheme = "VISA";
        }else if(isMasterRange){
            cardscheme = "Master";
        }else if(isCUPRange){
            cardscheme = "CUP";
        }else if(isAMEXRange){
            cardscheme = "AMEX";
        }else if(isJCBRange){
            cardscheme = "JCB";
        }else if(isDinersRange){
            cardscheme = "Diners";
        }
        return cardscheme;
    }

    private boolean cardSchemeRange(String range, int BIN){

        boolean inRange = false;

        Log.d("CardSchemeRange", "Card Scheme Range ----- " + range);
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

    protected void postCardData(){

        if ((progressDialog != null) && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        activity.runOnUiThread(new Runnable() {
            public void run() {

                CardPaymentTask cardPaymentTask = new CardPaymentTask(TradeResult.this,
                        getPrefPayGate(), progressDialog);

//        String secretKey = "demo1234";
                amount = map.get(Constants.AMOUNT);
                cardno = map.get(Constants.CARDNO);
                holder = hexToAscii(cardHolder).trim();
                merId = map.get(Constants.MERID);
                merchantName = map.get(Constants.MERNAME);
                merRef = map.get(Constants.MERCHANT_REF);
                payMethod = cardScheme(cardno.substring(0, 6));
                payType = map.get(Constants.PAYTYPE);

                String currCode = map.get(Constants.CURRCODE);
                entryMode = map.get(Constants.ENTRYMODE);
                epMonth = map.get(Constants.EXPMONTH);
                epYear =  map.get(Constants.EXPYEAR);
//                if (holder.equals("/") || holder.isEmpty()) {
//                    holder = "YU CHUN HO";
//                }

                ProcessingCode = "000000";
                POSCondtionCode = "00";
//                Track2Data = trackData2;
//Log.d("12345", payMethod);
//                if (payType.equalsIgnoreCase("N")) {
//                    payType = "Sale";
//                }else if (payType.equalsIgnoreCase("H")){
//                    payType = "Auth";
//                }

                if(entryMode.equalsIgnoreCase("C")){
                    POSEntryMode = "0051";
                    PANSeqNo = "00"+PANsequence;
                    EMVICCRelatedData = "01415F2A02015682025C008407A0000000031010950502400000009A031809199C01009F020600000000009" +
                            "99F03060000000000999F0902008C9F100706010A036024009F1A0201569F1E0830383230333430319F26080E9CCEC9DDC0A" +
                            "91F9F2701409F3303E0F8C89F34031E03009F3501229F3602007C9F3704F4D07FA59F410400E0F8C85F2A0208405F340101";
                    EncryptedPIN = GetPinEmv.getInstance().getPinData();
                    if(EncryptedPIN == null){
                        EncryptedPIN = "";
                    }
                }else if(entryMode.equalsIgnoreCase("M")){
                    POSEntryMode = "0012";

                }else if(entryMode.equalsIgnoreCase("S")){
                    POSEntryMode = "0022";

                }else if(entryMode.equalsIgnoreCase("F")){
                    POSEntryMode = "0801";

                }else if(entryMode.equalsIgnoreCase("T")){
                    EMVICCRelatedData = Constants.TransactionCurrencyCode + calcDE55FieldLength(CURR) + CURR
                            + Constants.ApplicationInterchangeProfile + calcDE55FieldLength(AIP) + AIP
                            + Constants.DedicatedFileName + calcDE55FieldLength(AID2) + AID2
                            + Constants.TerminalVerificationResults + calcDE55FieldLength(TVR) + TVR
                            + Constants.TransactionDate + calcDE55FieldLength(TXND) + TXND
                            + Constants.TransactionType + calcDE55FieldLength(TXNT) + TXNT
                            + Constants.AmountAuthorised + calcDE55FieldLength(txnAmount) + txnAmount
                            + Constants.AmountOther + calcDE55FieldLength(otherAmount) + otherAmount
                            + Constants.ApplicationVersionNumber + calcDE55FieldLength(TAVN) + TAVN
                            + Constants.IssuerApplicationData + calcDE55FieldLength(IAD) + IAD
                            + Constants.TerminalCountryCode + calcDE55FieldLength(TCC) + TCC
                            + Constants.InterfaceDeviceSerialNumber + calcDE55FieldLength(ID) + ID
                            + Constants.ApplicationCryptogram + calcDE55FieldLength(ARQC) + ARQC
                            + Constants.CryptogramInformationData + calcDE55FieldLength(CID) + CID
                            + Constants.TerminalCapabilities + calcDE55FieldLength(TC2) + TC2
                            + Constants.CVMResults + calcDE55FieldLength(CVM_Result) + CVM_Result
                            + Constants.TerminalType + calcDE55FieldLength(TT) + TT
                            + Constants.ApplicationTransactionCounter + calcDE55FieldLength(ATC) + ATC
                            + Constants.UnpredictableNumber + calcDE55FieldLength(UN) + UN
                            + Constants.TransactionSequenceCounter + calcDE55FieldLength(TSC) + TSC
                            + Constants.ApplicationPANSequenceNumber + calcDE55FieldLength(PANsequence) + PANsequence;

                    Log.d("EMVICCRelatedData-Tap", EMVICCRelatedData);

                    POSEntryMode = "0000";

                }

//        String cardHolderName = "02";
//        String cvv = "02";
//        String emv = "02";
//
//        String sendData = cardno + "&" + epMonth + "&" + epYear + "&" + cardHolderName + "&" + cvv +"&" + emv;
//        String encryptedCardNo = EncryptUtil.encrypt(sendData, secretKey);

                surcharge = "0";
                operator = "admin";
                channel = "MPOS";
                hideSurcharge = "F";

                Log.d("CardPayment", "Start the task");
//                cardPaymentTask.execute(merId, merchantName, currCode, amount, amount, "0", payType, merRef, pMethod, cardno, holder, expireMonth, expireYear, "admin", "MPOS", "F");

                Log.d(EMVDATA, "---PAN=" + cardno);
                Log.d(EMVDATA, "---ProcessingCode=" + ProcessingCode);
                Log.d(EMVDATA, "---Amount=" + amount);
                Log.d(EMVDATA, "---SystemTraceNumber=" + merRef);
                Log.d(EMVDATA, "---POSEntryMode=" + POSEntryMode);
                Log.d(EMVDATA, "---PANSequenceNumber=" + PANSeqNo);
                Log.d(EMVDATA, "---POSConditionCode=" + POSCondtionCode);
                Log.d(EMVDATA, "---Track2Data=" + Track2Data);
                Log.d(EMVDATA, "---EncryptedPIN=" + EncryptedPIN);
                Log.d(EMVDATA, "---EMVICCRelatedData=" + EMVICCRelatedData);
                Log.d(EMVDATA, "---InvoiceRef=" + merRef);

                Log.d(EMVDATA, "---MerchantID=" + merId);
                Log.d(EMVDATA, "---MerchantName=" + merchantName);
                Log.d(EMVDATA, "---Currcode=" + currCode);
                Log.d(EMVDATA, "---MerReqAmount=" + amount);
                Log.d(EMVDATA, "---Surcharge=" + surcharge);
                Log.d(EMVDATA, "---PayType=" + payType);
                Log.d(EMVDATA, "---PayMethod=" + payMethod);
                Log.d(EMVDATA, "---CardHolder=" + holder);
                Log.d(EMVDATA, "---ExpMonth=" + epMonth);
                Log.d(EMVDATA, "---ExpYear=" + epYear);
                Log.d(EMVDATA, "---Operator=" + operator);
                Log.d(EMVDATA, "---Channel=" + channel);
                Log.d(EMVDATA, "---HideSurcharge=" + hideSurcharge);

                cardPaymentTask.execute(cardno, ProcessingCode, amount, merRef, traceNo, POSEntryMode, PANSeqNo, POSCondtionCode, Track2Data, EncryptedPIN, EMVICCRelatedData, merRef,
                        merId, merchantName, currCode, amount, surcharge, payType, payMethod, holder, epMonth, epYear, CVV2Data, operator, channel, hideSurcharge);

            }
        });
    }

    public void cardSuccess(String merID,
                            String merName,
                            String successcode,
                            String merRef,
                            String payRef,
                            String traceNo,
                            String amount,
                            String merRequestAmt,
                            String surcharge,
                            String currCode,
                            String trxTime,
                            String payType,
                            String pMethod,
                            String cardNo,
                            String cardHolder,
                            String epMonth,
                            String epYear,
                            String operatorId,
                            String errcode) {

//        incMerRef();
//        incTraceNo();

        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");

        if (!"".equals(amount)) {
            amount = formatter.format(Double.parseDouble(amount));
        }
        if (!"".equals(surcharge)) {
            surcharge = formatter.format(Double.parseDouble(surcharge));
        }
        if (!"".equals(merRequestAmt)) {
            merRequestAmt = formatter.format(Double.parseDouble(merRequestAmt));
        }

        SharedPreferences prefsettings = activity.getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);
        visaPanMaskingRule = prefsettings.getString(Constants.online_visa_unmask_pan, Constants.default_online_visa_unmask_pan);
        visaExpiryMaskingRule = prefsettings.getString(Constants.online_visa_unmask_expiry, Constants.default_online_visa_unmask_expiry);
        masterPanMaskingRule = prefsettings.getString(Constants.online_mc_unmask_pan, Constants.default_online_mc_unmask_pan);
        masterExpiryMaskingRule = prefsettings.getString(Constants.online_mc_unmask_expiry, Constants.default_online_mc_unmask_expiry);

        if(pMethod.equalsIgnoreCase("VISA")){
            if(visaPanMaskingRule.split("_")[0].equalsIgnoreCase("1"))
            {
                String firstNthChar = visaPanMaskingRule.split("_")[1];
                String lastNthChar = visaPanMaskingRule.split("_")[2];

                String front = ((cardNo != null && cardNo.length() >= 14) ?
                        cardNo.substring(0, Integer.valueOf(firstNthChar)): "");

                String last = ((cardNo != null && cardNo.length() >= 14) ?
                        cardNo.substring(cardNo.length()- Integer.valueOf(lastNthChar)): "");

                String middle = ((cardNo != null && cardNo.length() >= 14) ?
                        cardNo.substring(Integer.valueOf(firstNthChar), cardNo.length()- Integer.valueOf(lastNthChar)): "");

                middle = middle.replaceAll(".", "*");
//                front = front.replaceAll(".", "*");
//                last = last.replaceAll(".", "*");

                cardNo = front + middle + last;
            }

            if(visaExpiryMaskingRule.split("_")[0].equalsIgnoreCase("1")){

                String epYearMasking = visaExpiryMaskingRule.split("_")[1];
                String epMonthMasking = visaExpiryMaskingRule.split("_")[2];

                if (epYearMasking.equalsIgnoreCase("1")) {

                    epYear = epYear.replaceAll(".", "*");
                }

                if (epMonthMasking.equalsIgnoreCase("1")) {

                    epMonth = epMonth.replaceAll(".", "*");

                }
            }
        }else if(pMethod.equalsIgnoreCase("MASTER")){

            if(masterPanMaskingRule.split("_")[0].equalsIgnoreCase("1"))
            {
                String firstNthChar = masterPanMaskingRule.split("_")[1];
                String lastNthChar = masterPanMaskingRule.split("_")[2];

                String front = ((cardNo != null && cardNo.length() >= 14) ?
                        cardNo.substring(0, Integer.valueOf(firstNthChar)): "");

                String last = ((cardNo != null && cardNo.length() >= 14) ?
                        cardNo.substring(cardNo.length()- Integer.valueOf(lastNthChar)): "");

                String middle = ((cardNo != null && cardNo.length() >= 14) ?
                        cardNo.substring(Integer.valueOf(firstNthChar), cardNo.length()- Integer.valueOf(lastNthChar)): "");

                middle = middle.replaceAll(".", "*");
//                front = front.replaceAll(".", "*");
//                last = last.replaceAll(".", "*");

                cardNo = front + middle + last;
            }

            if(masterExpiryMaskingRule.split("_")[0].equalsIgnoreCase("1")){

                String epYearMasking = masterExpiryMaskingRule.split("_")[1];
                String epMonthMasking = masterExpiryMaskingRule.split("_")[2];

                if (epYearMasking.equalsIgnoreCase("1")) {

                    epYear = epYear.replaceAll(".", "*");
                }

                if (epMonthMasking.equalsIgnoreCase("1")) {

                    epMonth = epMonth.replaceAll(".", "*");

                }
            }
        }

        String strCVMResult = "";
        //CVM RESULT

        if(CVM_Result != null && !CVM_Result.isEmpty()){

            String cvmResult = CVM_Result.substring(0,2);
            Log.d("CVM RESULT", cvmResult);

            if(cvmResult.equalsIgnoreCase("42"))
            {
                strCVMResult = Constants.cvmResult_OnlinePIN;

            }else if(cvmResult.equalsIgnoreCase("1E"))
            {
                strCVMResult = Constants.cvmResult_Signature;
            }else if(cvmResult.equalsIgnoreCase("1F"))
            {
                strCVMResult = Constants.cvmResult_NoCVM;
            }
        }else{
            strCVMResult = Constants.cvmResult_Signature;
        }

        Intent intent = null;
        if(entryMode.equalsIgnoreCase("M")){
            intent = new Intent(activity, Signature.class);
        }else{
            intent = new Intent(activity, CardPayment_Success.class);
        }
        intent.putExtra(Constants.MERID, merID);
        intent.putExtra(Constants.MERNAME, merName);
        intent.putExtra(Constants.SUCCESS_CODE, successcode);
        intent.putExtra(Constants.MERCHANT_REF, merRef);
        intent.putExtra(Constants.PAYREF, payRef);
        intent.putExtra(Constants.TRACE_NO, traceNo);
        intent.putExtra(Constants.BATCH_NO, batchNo);
        intent.putExtra(Constants.TERMINAL_ID, terminalID);
        intent.putExtra(Constants.AMOUNT, amount);
        intent.putExtra(Constants.MERREQUESTAMT, merRequestAmt);
        intent.putExtra(Constants.SURCHARGE, surcharge);
        intent.putExtra(Constants.CURRCODE, currCode);
        intent.putExtra(Constants.TXTIME, trxTime);
        intent.putExtra(Constants.PAYTYPE, payType);
        intent.putExtra(Constants.PAYMETHOD, pMethod);
        intent.putExtra(Constants.CARDNO, cardNo);
        intent.putExtra(Constants.CARDHOLDER, cardHolder);
        intent.putExtra(Constants.EXPMONTH, epMonth);
        intent.putExtra(Constants.EXPYEAR, epYear);
        intent.putExtra(Constants.OPERATORID, operatorId);
        intent.putExtra(Constants.ERRMSG, errcode);

//        intent.putExtra(Constants.pref_hideSurcharge, getIntent().getStringExtra(Constants.pref_hideSurcharge));
        intent.putExtra(Constants.AID, AID);
        intent.putExtra(Constants.AppName, AppName);
        intent.putExtra(Constants.EntryMode, entryMode);
        intent.putExtra(Constants.CVMResult, strCVMResult);
//        System.out.println("OTTO-----" + "Payment Tab success->   successcod:" + successcode + ",Ref:" + merchant_ref_no + ",PayRef:" + PayRef + ",amount:" + amount + ",merRequestAmt:" + merRequestAmt + ",surcharge:" + Surcharge + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode + ",merchantid:" + merchantid + ",mername:" + mername + ",payType:" + payType + ",pMethod:" + pMethod + ",operatorId:" + operatorId + ",cardNo:" + cardNo);

        activity.startActivity(intent);
        activity.finish();
    }

    public void cardFailed(String merID,
                           String merName,
                           String successcode,
                           String merRef,
                           String payRef,
                           String traceNo,
                           String amount,
                           String merRequestAmt,
                           String surcharge,
                           String currCode,
                           String payType,
                           String pMethod,
                           String cardNo,
                           String cardHolder,
                           String epMonth,
                           String epYear,
                           String operatorId,
                           String prc,
                           String src,
                           String errcode){

//        incMerRef();
//        incTraceNo();

        Intent intent = new Intent(activity, CardPayment_Failure.class);

        if (amount != null) {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            amount = formatter.format(Double.parseDouble(amount));
        }

        intent.putExtra(Constants.MERNAME, merName);
        intent.putExtra(Constants.SUCCESS_CODE, successcode);
        intent.putExtra(Constants.MERCHANT_REF, merRef);
        intent.putExtra(Constants.PAYREF, payRef);
        intent.putExtra(Constants.TRACE_NO, traceNo);
        intent.putExtra(Constants.BATCH_NO, batchNo);
        intent.putExtra(Constants.TERMINAL_ID, terminalID);
        intent.putExtra(Constants.AMOUNT, amount);
        intent.putExtra(Constants.MERREQUESTAMT, merRequestAmt);
        intent.putExtra(Constants.SURCHARGE, surcharge);
        intent.putExtra(Constants.CURRCODE, currCode);
        intent.putExtra(Constants.PAYTYPE, payType);
        intent.putExtra(Constants.PAYMETHOD, pMethod);
        intent.putExtra(Constants.CARDNO, cardNo);
        intent.putExtra(Constants.CARDHOLDER, cardHolder);
        intent.putExtra(Constants.EXPMONTH, epMonth);
        intent.putExtra(Constants.EXPYEAR, epYear);
        intent.putExtra(Constants.OPERATORID, operatorId);
        intent.putExtra(Constants.ERRMSG, errcode);
//        intent.putExtra(Constants.pref_hideSurcharge, activity.getIntent().getStringExtra(Constants.pref_hideSurcharge));
        intent.putExtra(Constants.AID, AID);
        intent.putExtra(Constants.AppName, AppName);
        intent.putExtra(Constants.EntryMode, entryMode);
//        System.out.println("OTTO-----" + "Payment Tab failed->   successcod:" + successcode + ",Ref:" + merchant_ref_no + ",PayRef:" + PayRef + ",amount:" + amount + ",merRequestAmt:" + merRequestAmt + ",surcharge:" + Surcharge + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode + ",merchantid:" + merchantid + ",mername:" + mername + ",payType:" + payType + ",pMethod:" + pMethod + ",operatorId:" + operatorId + ",cardNo:" + cardNo);

        activity.startActivity(intent);
        activity.finish();
    }
}
