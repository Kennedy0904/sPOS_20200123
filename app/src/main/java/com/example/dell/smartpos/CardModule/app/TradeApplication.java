package com.example.dell.smartpos.CardModule.app;

import android.app.Application;
import android.util.Log;

import com.example.dell.smartpos.GlobalFunction;
import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.example.dell.smartpos.CardModule.tradepaypw.utils.FileParse;
import com.example.dell.smartpos.CardModule.tradepaypw.utils.FileUtils;

//import com.pax.gl.IGL;
//import com.pax.gl.convert.IConvert;
//import com.pax.gl.impl.GLProxy;

/**
 * Created by chenld on 2017/3/13.
 */

public class TradeApplication extends Application {
    private static final String TAG = "TradeApplication";
    private static TradeApplication tradeApplication;
    public final static String APP_VERSION = "V1.00.03_20171027";

    // 获取IPPI常用接口
    private static IDAL dal;
    //public static IGL gl;
    private static IConvert convert;

    public static IDAL getDal() {
        return dal;
    }

    public static IConvert getConvert() {
        return convert;
    }

    public static TradeApplication getInstance() {
        return tradeApplication;
    }

    @Override
    public void onCreate() {
        if (GlobalFunction.getDeviceMan().equalsIgnoreCase("PAX")) {
            super.onCreate();
            TradeApplication.tradeApplication = this;
            init();
        }
    }

    private void init() {
        // 获取IPPI常用接口
        NeptuneLiteUser neptuneLiteUser = NeptuneLiteUser.getInstance();
        try {
            if (dal == null) {
                dal = neptuneLiteUser.getDal(this.getApplicationContext());
                Log.i("FinancialApplication:", "dalProxyClient finished.");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("dalProxyClient", e.getMessage());
        }
        //gl = new GLProxy(tradeApplication).getGL();;
        //convert = gl.getConvert();new ConverterImp();
        convert = new ConverterImp();

        FileParse.parseAidFromAssets(this, "aid.ini");

        FileParse.parseCapkFromAssets(this, "capk.ini");

        Log.i(TAG, "init: ");
    }

    static {
        if (GlobalFunction.getDeviceMan().equalsIgnoreCase("PAX")) {
            System.loadLibrary("F_DEVICE_LIB_PayDroid");
            System.loadLibrary("F_PUBLIC_LIB_PayDroid");
            System.loadLibrary("F_EMV_LIB_PayDroid");
            System.loadLibrary("F_ENTRY_LIB_PayDroid");
            System.loadLibrary("F_MC_LIB_PayDroid");
            System.loadLibrary("F_WAVE_LIB_PayDroid");
            System.loadLibrary("F_AE_LIB_PayDroid");
            System.loadLibrary("F_QPBOC_LIB_PayDroid");
            System.loadLibrary("F_DPAS_LIB_PayDroid");
            System.loadLibrary("F_JCB_LIB_PayDroid");
            System.loadLibrary("F_PURE_LIB_PayDroid");
            System.loadLibrary("JNI_EMV_v101");
            System.loadLibrary("JNI_ENTRY_v103");
            System.loadLibrary("JNI_MC_v100");
            System.loadLibrary("JNI_WAVE_v100");
            System.loadLibrary("JNI_AE_v101");
            System.loadLibrary("JNI_QPBOC_v100");
            System.loadLibrary("JNI_DPAS_v100");
            System.loadLibrary("JNI_JCB_v100");
            System.loadLibrary("JNI_PURE_v100");
        }
    }

}
