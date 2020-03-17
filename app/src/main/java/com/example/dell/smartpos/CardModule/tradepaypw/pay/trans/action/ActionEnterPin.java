/*
 * ============================================================================
 * COPYRIGHT
 *              Pax CORPORATION PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or
 *   nondisclosure agreement with Pax Corporation and may not be copied
 *   or disclosed except in accordance with the terms in that agreement.
 *      Copyright (C) 2016 - ? Pax Corporation. All rights reserved.
 * Module Date: 2016-11-25
 * Module Author: Steven.W
 * Description:
 *
 * ============================================================================
 */
package com.example.dell.smartpos.CardModule.tradepaypw.pay.trans.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.example.dell.smartpos.CardModule.tradepaypw.ConsumeActivity;
import com.example.dell.smartpos.CardModule.tradepaypw.abl.core.AAction;
import com.example.dell.smartpos.CardPayment_2;
import com.example.dell.smartpos.CardSchemeValidation;

public class ActionEnterPin extends AAction {
    private Context context;
    private String title;
    private String pan;
    private String header;
    private String subHeader;
    private String totalAmount;
    private String offlinePinLeftTimes;
    private boolean isOnlinePin;
    //private EEnterPinType enterPinType;

    private Activity activity;

    public ActionEnterPin(ActionStartListener listener) {
        super(listener);
    }

    /**
     * 脱机pin时返回的结果
     *
     * @author Steven.W
     */
    public static class OfflinePinResult {
        // SW1 SW2
        byte[] respOut;
        int ret;

        public byte[] getRespOut() {
            return respOut;
        }

        public void setRespOut(byte[] respOut) {
            this.respOut = respOut;
        }

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }
    }

//    public void setParam(Context context, String pan, boolean onlinePin, String totalAmount, String offlinePinLeftTimes) {
//        this.context = context;
//        this.pan = pan;
//        this.isOnlinePin = onlinePin;
//        //this.header = header;
//        //this.subHeader = subHeader;
//        this.totalAmount = totalAmount;
//        this.offlinePinLeftTimes = offlinePinLeftTimes; //AET-81
//        //this.enterPinType = enterPinType;
//    }

    public void setParam(Activity activity, String pan, boolean onlinePin, String totalAmount, String offlinePinLeftTimes) {
        this.activity = activity;
        this.pan = pan;
        this.isOnlinePin = onlinePin;
        //this.header = header;
        //this.subHeader = subHeader;
        this.totalAmount = totalAmount;
        this.offlinePinLeftTimes = offlinePinLeftTimes; //AET-81
        //this.enterPinType = enterPinType;
    }

    public enum EEnterPinType {
        ONLINE_PIN, // 联机pin
        OFFLINE_PLAIN_PIN, // 脱机明文pin
        OFFLINE_CIPHER_PIN, // 脱机密文pin
        OFFLINE_PCI_MODE, //JEMV PCI MODE, no callback for offline pin
    }

    @Override
    protected void process() {

        CardSchemeValidation validation = new CardSchemeValidation(activity);
        int BIN = Integer.valueOf(pan.substring(0,6));

        String payMethod = validation.checkCardScheme(BIN);
        if(payMethod != null){
            boolean validCardScheme = validation.validateCardScheme(payMethod, CardPayment_2.payType);

            if(validCardScheme){
                Intent intent = new Intent(activity, ConsumeActivity.class);
                //intent.putExtra(EUIParamKeys.NAV_TITLE.toString(), title);
                //intent.putExtra(EUIParamKeys.PROMPT_1.toString(), header);
                //intent.putExtra(EUIParamKeys.PROMPT_2.toString(), subHeader);
                intent.putExtra("amount", totalAmount);
                if (isOnlinePin == true)
                    intent.putExtra("isOnlinePin", 1);
                else
                    intent.putExtra("isOnlinePin", 0);
                intent.putExtra("offlinePinLeftTimes", Integer.valueOf(offlinePinLeftTimes));
                intent.putExtra("pan", pan);
                //intent.putExtra(EUIParamKeys.TIP_AMOUNT.toString(), tipAmount); //AET-81
                //intent.putExtra(EUIParamKeys.ENTERPINTYPE.toString(), enterPinType);
                //intent.putExtra(EUIParamKeys.PANBLOCK.toString(), PanUtils.getPanBlock(pan, EPanMode.X9_8_WITH_PAN));
                //intent.putExtra(EUIParamKeys.SUPPORTBYPASS.toString(), isSupportBypass);
                activity.startActivity(intent);
            }
        }

    }

}
