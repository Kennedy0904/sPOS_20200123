package com.example.dell.smartpos.CardModule.tradepaypw;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;

import com.example.dell.smartpos.CardModule.jemv.clssentrypoint.trans.ClssEntryPoint;
import com.example.dell.smartpos.CardModule.jemv.clssjspeedy.ClssJSpeedy;
import com.example.dell.smartpos.CardModule.tradepaypw.abl.core.utils.PanUtils;
import com.example.dell.smartpos.CardModule.tradepaypw.utils.Utils;
import com.example.dell.smartpos.CardModule.tradepaypw.view.dialog.CustomAlertDialog;
import com.example.dell.smartpos.CardModule.tradepaypw.view.dialog.InputPwdDialog;
import com.example.dell.smartpos.CardPayment_2;
import com.example.dell.smartpos.CardSchemeValidation;
import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.MainActivity;
import com.example.dell.smartpos.R;
import com.pax.dal.entity.EReaderType;
import com.pax.dal.exceptions.PedDevException;
import com.pax.jemv.clcommon.KernType;
import com.pax.jemv.clcommon.RetCode;
import com.pax.jemv.clcommon.TransactionPath;

import java.util.HashMap;

import static com.example.dell.smartpos.CardModule.tradepaypw.utils.Utils.bcd2Str;

public class ConsumeActivity extends Activity {

    private static final String TAG = "ConsumeActivity";

    private String amount;
    private int result;
    private String pan;
    private int isOnlinePin;
    private int offlinePinLeftTimes;

    //    private SoftKeyboardPasswordStyle soft_keyboard_view;
    private Handler handler = new Handler();
    private InputPwdDialog dialog = null;

    private CustomAlertDialog promptDialog;
    //private boolean isFirstStart = true;// 判断界面是否第一次加载

    private static ConsumeActivity instance;

    public static ConsumeActivity getInstance() {
        if (instance == null) {
            instance = new ConsumeActivity();
        }
        return instance;
    }

    private String currCode;
    private String merID;
    private String merName;
    private String merRef;
    private String payMethod;
    private String payType;
    private String entryMode;
    private String epYear;
    private String epMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume);

        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        result = intent.getIntExtra("result", 0);
        isOnlinePin = intent.getIntExtra("isOnlinePin", 1);
        //Log.i("ConsumeActivity", "isOnlinePin = " + isOnlinePin );
        offlinePinLeftTimes = intent.getIntExtra("offlinePinLeftTimes", 0);
        //Log.i("ConsumeActivity", "offlinePinLeftTimes = " + offlinePinLeftTimes );
        pan = intent.getStringExtra("pan");

        currCode = intent.getStringExtra(Constants.CURRCODE);
        merID = intent.getStringExtra(Constants.MERID);
        merName = intent.getStringExtra(Constants.MERNAME);
        merRef = intent.getStringExtra(Constants.MERCHANT_REF);
        payMethod = intent.getStringExtra(Constants.PAYMETHOD);
        payType = intent.getStringExtra(Constants.PAYTYPE);
        entryMode = intent.getStringExtra(Constants.ENTRYMODE);
        epYear = intent.getStringExtra(Constants.EXPMONTH);
        epMonth = intent.getStringExtra(Constants.EXPYEAR);

//        soft_keyboard_view.setOnItemClickListener(new SoftKeyboardPasswordStyle.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, int index) {
//                //工作在子线程
//                if (index == KeyEvent.KEYCODE_ENTER) {
//
//                    ShowSuccessDialog();
//                } else if (index == Constants.KEY_EVENT_CANCEL) {
//                    Intent intent1 = new Intent(ConsumeActivity.this, MainActivity.class);
//                    startActivity(intent1);
//                }
//            }
//
//
//        });

        if (isOnlinePin == 1)
            initInputPwdDialog();
        else
            initInputOfflPwdDialog();


    }

    // 当页面加载完成之后再执行弹出键盘的动作
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            if (isFirstStart) {
//                initInputPwdDialog();
//                isFirstStart = false;
//            }
//        }
//    }

    private void initInputPwdDialog() {
        Log.i("InputPwdDialog", "creat  initInputPwdDialog");
        dialog = new InputPwdDialog(this, handler, getString(R.string.entry_card_password),
                getString(R.string.without_password));
        //dialog.setTimeout();
        dialog.setcvmType(isOnlinePin);
        dialog.setCancelable(true);
        dialog.setPwdListener(new InputPwdDialog.OnPwdListener() {
            @Override
            public void onSucc(String data) {
                Log.i("InputPwdDialog", "dialog OnSucc");
                if (data == null)
                    GetPinEmv.getInstance().setPinResult(RetCode.EMV_NO_PASSWORD);
                else {
                    GetPinEmv.getInstance().setPinResult(RetCode.EMV_OK);
                    GetPinEmv.getInstance().setPinData(data);
                }
                if (SwingCardActivity.getReadType() == EReaderType.ICC) {
                    dialog.dismiss();

                    ImplEmv.pinEnterReady();
                    //ShowSuccessDialog();
                    finish();
                    //Intent intent1 = new Intent(ConsumeActivity.this, SwingCardActivity.class);
                    //startActivity(intent1);
                } else {
                    ShowSuccessDialog();
                    dialog.dismiss();
                    finish();
                }

            }

            @Override
            public void onErr() {
                Log.i("InputPwdDialog", "dialog onErr");
                dialog.dismiss();
                ImplEmv.pinEnterReady();
                if (SwingCardActivity.getReadType() == EReaderType.ICC) {
                    finish();
                } else {
                    Intent intent1 = new Intent(ConsumeActivity.this, MainActivity.class);
                    startActivity(intent1);
                }

            }
        });
        dialog.show();
        dialog.inputOnlinePin(PanUtils.getPanBlock(getIntent().getStringExtra("pan"), PanUtils.EPanMode.X9_8_WITH_PAN));
    }

    private void initInputOfflPwdDialog() {
        Log.i("initInputOfflPwdDialog", "creat  initInputOfflPwdDialog");
        dialog = new InputPwdDialog(this, handler, getString(R.string.entry_card_password),
                getString(R.string.without_password));
        Log.i("InputPwdDialog", "new InputPwdDialog");
        dialog.setcvmType(isOnlinePin);
        Log.i("InputPwdDialog", " setcvmType");
        //dialog.setTimeout();
        dialog.setCancelable(true);
        Log.i("InputPwdDialog", " setCancelable");
        dialog.setPwdListener(new InputPwdDialog.OnPwdListener() {
            @Override
            public void onSucc(String data) {
                Log.i("initInputOfflPwdDialog", "dialog OnSucc");
                if (data == null)
                    GetPinEmv.getInstance().setPinResult(RetCode.EMV_OK);
                else {
                    GetPinEmv.getInstance().setPinResult(RetCode.EMV_OK);
                    //GetPinEmv.getInstance().setPinData(data);
                }
                dialog.dismiss();
                finish();
            }

            @Override
            public void onErr() {
                Log.i("initInputOfflPwdDialog", "dialog onErr");
                dialog.dismiss();
                //MainActivity.pinEnterReady();
                //Intent intent1 = new Intent(ConsumeActivity.this, MainActivity.class);
                //startActivity(intent1);
                finish();
            }
        });
        dialog.show();
        Log.i("InputPwdDialog", " show");
        try {
            dialog.inputOfflinePlainPin();
        } catch (PedDevException e) {
            Log.i("inputOfflinePlainPin", "e :" + e.getErrCode());
            if (dialog != null)
                dialog.dismiss();
            e.printStackTrace();
        }
    }

    private void ShowSuccessDialog() {
        Log.i("InputPwdDialog", "ShowSuccessDialog");
        toTradeResultActivity();
    }

    private void toTradeResultActivity() {
        while (true) {
            if (promptDialog == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("InputPwdDialog", "toTradeResultActivity online Processing");
                        promptDialog = new CustomAlertDialog(ConsumeActivity.this, CustomAlertDialog.PROGRESS_TYPE);

                        promptDialog.show();
                        promptDialog.setCancelable(false);
                        promptDialog.setTitleText(getString(R.string.prompt_online));

                    }
                });
            } else {
                Log.i("InputPwdDialog", "promptDialog dismiss");
                promptDialog.dismiss();
                break;
            }
            SystemClock.sleep(3000);
        }

        if (ClssEntryPoint.getInstance().getOutParam().ucKernType == KernType.KERNTYPE_JCB) {
            if (ClssJSpeedy.getInstance().getTransPath() == TransactionPath.CLSS_JCB_EMV) {
                byte[] sIssuerScript = Utils.str2Bcd("9F1804AABBCCDD86098424000004AABBCCDD");
                int sgScriptLen = 18;
                ClssJSpeedy.getInstance().jcbFlowComplete(sIssuerScript, sgScriptLen);
            }
        }


        ImplEmv.pinEnterReady();
/*        Intent intent = new Intent(this, TradeResultActivity.class);
        intent.putExtra("amount", amount);
        intent.putExtra("pan", getIntent().getStringExtra("pan"));
        startActivity(intent);*/

        /**********************
         ** After ICC Success **
         ***********************/
        HashMap<String, String> map = new HashMap();
        map.put(Constants.CARDNO, pan);
        map.put(Constants.AMOUNT,  amount);
        map.put(Constants.CURRCODE, currCode);
        map.put(Constants.MERID, merID);
        map.put(Constants.MERNAME, merName);
        map.put(Constants.MERCHANT_REF, merRef);
        map.put(Constants.PAYTYPE, payType);
        map.put(Constants.ENTRYMODE, entryMode);
        map.put(Constants.EXPYEAR, epYear);
        map.put(Constants.EXPMONTH, epMonth);
        Log.i(TAG, epMonth);
        Log.i(TAG, "Start TAP TradeResult");
        TradeResult tradeResult = new TradeResult(ConsumeActivity.this, map);
        tradeResult.initData();
    }

    @Override
    protected void onStop() {
        handler.removeCallbacksAndMessages(null);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }


}
