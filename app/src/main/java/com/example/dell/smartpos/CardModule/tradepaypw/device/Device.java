package com.example.dell.smartpos.CardModule.tradepaypw.device;


//import com.pax.ipp.service.aidl.Exceptions;

import android.util.Log;

import com.pax.dal.IPed;
import com.pax.dal.entity.DUKPTResult;
import com.pax.dal.entity.EBeepMode;
import com.pax.dal.entity.ECheckMode;
import com.pax.dal.entity.EDUKPTPinMode;
import com.pax.dal.entity.EPedKeyType;
import com.pax.dal.entity.EPedType;
import com.pax.dal.entity.EPinBlockMode;
import com.pax.dal.exceptions.PedDevException;
import com.example.dell.smartpos.CardModule.app.TradeApplication;
import com.example.dell.smartpos.CardModule.tradepaypw.pay.Constants;


public class Device {
    private static final String TAG = "Device";

    private Device() {

    }

    /**
     * beep 成功
     */
    public static void beepOk() {
        TradeApplication.getDal().getSys().beep(EBeepMode.FREQUENCE_LEVEL_3, 100);
        TradeApplication.getDal().getSys().beep(EBeepMode.FREQUENCE_LEVEL_4, 100);
        TradeApplication.getDal().getSys().beep(EBeepMode.FREQUENCE_LEVEL_5, 100);
    }

    /**
     * beep 失败
     */
    public static void beepErr() {
        TradeApplication.getDal().getSys().beep(EBeepMode.FREQUENCE_LEVEL_6, 200);
    }

    /**
     * beep 提示音
     */

    public static void beepPromt() {
        TradeApplication.getDal().getSys().beep(EBeepMode.FREQUENCE_LEVEL_6, 50);
    }

    /**
     * write TMK
     *
     * @param tmkIndex
     * @param tmkValue
     * @return
     */
    public static boolean writeTMK(byte[] tmkValue) {
        // write TMK
        try {
            TradeApplication.getDal().getPed(EPedType.INTERNAL).writeKey(EPedKeyType.TLK, (byte) 0,
                    EPedKeyType.TMK, Constants.INDEX_TMK,
                    tmkValue, ECheckMode.KCV_NONE, null);
            return true;
        } catch (PedDevException e) {
            Log.w("writeTMK", e);
        }
        return false;
    }

    public static boolean writeTPK(byte[] tpkValue, byte[] tpkKcv) {
        try {
            //int mKeyIndex = Utils.getMainKeyIndex(FinancialApplication.getSysParam().get(SysParam.NumberParam.MK_INDEX));
            ECheckMode checkMode = ECheckMode.KCV_ENCRYPT_0;
            if (tpkKcv == null || tpkKcv.length == 0) {
                checkMode = ECheckMode.KCV_NONE;
            }
            TradeApplication.getDal().getPed(EPedType.INTERNAL).writeKey(EPedKeyType.TMK, Constants.INDEX_TMK,
                    EPedKeyType.TPK, Constants.INDEX_TPK, tpkValue, checkMode, tpkKcv);
            return true;
        } catch (PedDevException e) {
            Log.w("writeTPK", e);
        }
        return false;
    }

    public static boolean writeTIKFuc(byte[] keyValue, byte[] ksn) {
        // write TIK
        try {
            TradeApplication.getDal().getPed(EPedType.INTERNAL).writeTIK(Constants.INDEX_TIK, (byte) 0,
                    keyValue, ksn, ECheckMode.KCV_NONE, null);
            return true;
        } catch (PedDevException e) {
            Log.w("writeTIKFuc", e);
        }
        return false;
    }


    /**
     * 计算pinblock(包括国密)
     *
     * @param panBlock
     * @return
     * @throws PedDevException
     */
    public static byte[] getPinBlock(String panBlock) throws PedDevException {
        IPed ped = TradeApplication.getDal().getPed(EPedType.INTERNAL);
//        String supportSm = TradeApplication.sysParam.get(SysParam.SUPPORT_SM);
//        if (supportSm.equals(SysParam.Constant.YES)) { // 国密
//            return ped.getPinBlockSM4(Constants.INDEX_TPK, "0,4,5,6,7,8,9,10,11,12", panBlock.getBytes(),
//                    EPinBlockMode.ISO9564_0, 60 * 1000);
//        } else {
//            return ped.getPinBlock(Constants.INDEX_TPK, "0,4,5,6,7,8,9,10,11,12", panBlock.getBytes(),
//                    EPinBlockMode.ISO9564_0, 60 * 1000);
//        }
        //IPed ped = FinancialApplication.getDal().getPed(EPedType.INTERNAL);

        //ped.setKeyboardLayoutLandscape(true);
        return ped.getPinBlock(Constants.INDEX_TPK, "0,4,5,6,7,8,9,10,11,12", panBlock.getBytes(),
                EPinBlockMode.ISO9564_0, 60 * 1000);
    }

    public static DUKPTResult getDUKPTPin(String panBlock) throws PedDevException {
        IPed ped = TradeApplication.getDal().getPed(EPedType.INTERNAL);

        return ped.getDUKPTPin(Constants.INDEX_TIK, "0,4,5,6,7,8,9,10,11,12", panBlock.getBytes(),
                EDUKPTPinMode.ISO9564_0_INC, 60 * 1000);
    }


}
