package com.example.dell.smartpos.Printer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.Printer.K9.PrintServiceK9;
import com.example.dell.smartpos.Printer.K9.PrintServiceThaiK9;
import com.example.dell.smartpos.Printer.PAX.PrintServicePAX;
import com.example.dell.smartpos.Printer.Summi.PrintService;
import com.example.dell.smartpos.Record;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PrintUtil {

    private FrameLayout progressBar;
    // SUNMI Print Service
    private PrintService printService = null;

    // PAX Print Service
    private PrintServicePAX printServicePAX = null;

    // K9 Print Service
    private PrintServiceK9 printServiceK9 = null;
    private PrintServiceThaiK9 printServiceThaiK9 = null;

    private Context context;
    private Map<String,String> info;
    private Map<String,String> product;
    private Bitmap bitmap;
    private ArrayList<Record> ALIPAYHKOFFLRecordsArray,
            ALIPAYCNOFFLRecordsArray,
            ALIPAYOFFLRecordsArray,
            BOOSTOFFLRecordsArray,
            GCASHOFFLRecordsArray,
            GRABPAYOFFLRecordsArray,
            masterRecordsArray,
            OEPAYOFFLRecordsArray,
            PROMPTPAYOFFLRecordsArray,
            UNIONPAYOFFLRecordsArray,
            visaRecordsArray,
            WECHATOFFLRecordsArray,
            WECHATHKOFFLRecordsArray,
            WECHATONLRecordsArray;

    private String deviceMan = android.os.Build.MANUFACTURER;
    private String deviceAddress = null;
    private String deviceName = null;
    private String lang;

    private Bitmap signature;

    private JSONObject resultObject;

    /** Last Settlement Report Constructor **/
    public PrintUtil(Context context, JSONObject resultObject, FrameLayout progressBar) {
		this.context = context;
        this.printServicePAX = new PrintServicePAX(context);
        this.resultObject = resultObject;
        this.progressBar = progressBar;
    }

    public PrintUtil(Context context, String deviceAddress, String deviceName, Map<String,String> info, Map<String,String> product, Bitmap bitmap) {
        super();
        this.context = context;
        this.deviceAddress = deviceAddress;
        this.deviceName = deviceName;
        this.printService = new PrintService(this.context, this.deviceAddress);
        this.info = info;
        this.product = product;
        this.bitmap = bitmap;
        this.printServicePAX = new PrintServicePAX(this.context, this.deviceAddress);
        this.printServiceK9 = new PrintServiceK9(context);
        this.printServiceThaiK9 = new PrintServiceThaiK9(context);
    }

    public PrintUtil(Context context, String deviceAddress, String deviceName, Map<String,String> info, Map<String,String> product, Bitmap bitmap, Bitmap signature) {
        super();
        this.context = context;
        this.deviceAddress = deviceAddress;
        this.deviceName = deviceName;
        this.printService = new PrintService(this.context, this.deviceAddress);
        this.info = info;
        this.product = product;
        this.bitmap = bitmap;
        this.signature = signature;
        this.printServicePAX = new PrintServicePAX(this.context, this.deviceAddress);
    }

    public PrintUtil(Context context, String deviceAddress, String deviceName, Map<String,String> info, Map<String,String> product,
                     ArrayList<Record> ALIPAYHKOFFLRecordsArray,
                     ArrayList<Record> ALIPAYCNOFFLRecordsArray,
                     ArrayList<Record> ALIPAYOFFLRecordsArray,
                     ArrayList<Record> BOOSTOFFLRecordsArray,
                     ArrayList<Record> GCASHOFFLRecordsArray,
                     ArrayList<Record> GRABPAYOFFLRecordsArray,
                     ArrayList<Record> masterRecordsArray,
                     ArrayList<Record> OEPAYOFFLRecordsArray,
                     ArrayList<Record> PROMPTPAYOFFLRecordsArray,
                     ArrayList<Record> UNIONPAYOFFLRecordsArray,
                     ArrayList<Record> visaRecordsArray,
                     ArrayList<Record> WECHATOFFLRecordsArray,
                     ArrayList<Record> WECHATHKOFFLRecordsArray,
                     ArrayList<Record> WECHATONLRecordsArray) {
        super();
        this.context = context;
        this.deviceAddress = deviceAddress;
        this.deviceName = deviceName;
        this.printService = new PrintService(this.context, this.deviceAddress);
        this.info = info;
        this.product = product;
        this.ALIPAYHKOFFLRecordsArray = ALIPAYHKOFFLRecordsArray;
        this.ALIPAYCNOFFLRecordsArray = ALIPAYCNOFFLRecordsArray;
        this.ALIPAYOFFLRecordsArray = ALIPAYOFFLRecordsArray;
        this.BOOSTOFFLRecordsArray = BOOSTOFFLRecordsArray;
        this.GCASHOFFLRecordsArray = GCASHOFFLRecordsArray;
        this.GRABPAYOFFLRecordsArray = GRABPAYOFFLRecordsArray;
        this.masterRecordsArray = masterRecordsArray;
        this.OEPAYOFFLRecordsArray = OEPAYOFFLRecordsArray;
        this.PROMPTPAYOFFLRecordsArray = PROMPTPAYOFFLRecordsArray;
        this.UNIONPAYOFFLRecordsArray = UNIONPAYOFFLRecordsArray;
        this.visaRecordsArray = visaRecordsArray;
        this.WECHATOFFLRecordsArray = WECHATOFFLRecordsArray;
        this.WECHATHKOFFLRecordsArray = WECHATHKOFFLRecordsArray;
        this.WECHATONLRecordsArray = WECHATONLRecordsArray;
        this.printServicePAX = new PrintServicePAX(this.context, this.deviceAddress);
        this.printServiceK9 = new PrintServiceK9(context);
        this.printServiceThaiK9 = new PrintServiceThaiK9(context);
    }

    private void initView() {
        // 一上来就先连接蓝牙设备

        boolean flag = false;

        if (deviceMan.equalsIgnoreCase("SUNMI")){
            flag = this.printService.connect();
        } else if (deviceMan.equalsIgnoreCase("PAX")){
            // call "this.printServicePAX.connect()" will show "printer connection failed"
            // for PAX not require to check connection
            //flag = this.printServicePAX.connect();
            flag = true;
        } else {
            flag = this.printServiceK9.connect();
        }

        if (!flag) {
            // 连接失败
            Log.d("OTTO","连接失败");
        } else {
            // 连接成功
            Log.d("OTTO","连接成功");
        }

        lang = getLanguage(context);
    }

    public void sends(){

        initView();

        if(deviceMan.equalsIgnoreCase("SUNMI")){
            this.printService.send(context,info,product,bitmap);
        }else if(deviceMan.equalsIgnoreCase("PAX")){
            this.printServicePAX.sendPAX(context, info, product, bitmap);
        }else if(deviceMan.equalsIgnoreCase("Centerm")) {
            if (lang.equalsIgnoreCase(Constants.LANG_THAI)) {
                this.printServiceThaiK9.printReceipt(context,info,product,bitmap);
            } else {
                this.printServiceK9.printReceipt(context,info,product,bitmap);
            }
        }
    }

    public void sendAlipayTH(){
        initView();

        if(deviceMan.equalsIgnoreCase("SUNMI")){
            this.printService.sendAlipayTHreceipt(context,info,product,bitmap);
        }else if(deviceMan.equalsIgnoreCase("PAX")){
            this.printServicePAX.sendPAX(context, info, product, bitmap);
        }
    }

    //===KM LIEW summry report printing feature 30/5/2018===
    public void sendSummaryReport(){
        initView();
        System.out.println("[PrintUtil] sendSummaryReport deviceMan: " + deviceMan);
        if(deviceMan.equalsIgnoreCase("SUNMI")){
            this.printService.printSummaryReport(context,info,product);
        }else if(deviceMan.equalsIgnoreCase("PAX")){
            this.printServicePAX.sendSummaryReportPAX(context, info, product);
        }else if(deviceMan.equalsIgnoreCase("Centerm")) {
            if (lang.equalsIgnoreCase(Constants.LANG_THAI)) {
                this.printServiceThaiK9.printSummaryReport(context, info, product);
            } else {
                this.printServiceK9.printSummaryReport(context, info, product);
            }
        }

    }
    //===================================//

    public void sendReport(){
        initView();

        if(deviceMan.equalsIgnoreCase("SUNMI")){
            this.printService.printReportSUNMI(context, info, product,
                    ALIPAYCNOFFLRecordsArray,
                    ALIPAYHKOFFLRecordsArray,
                    ALIPAYOFFLRecordsArray,
                    BOOSTOFFLRecordsArray,
                    GCASHOFFLRecordsArray,
                    GRABPAYOFFLRecordsArray,
                    masterRecordsArray,
                    OEPAYOFFLRecordsArray,
                    PROMPTPAYOFFLRecordsArray,
                    UNIONPAYOFFLRecordsArray,
                    visaRecordsArray,
                    WECHATHKOFFLRecordsArray,
                    WECHATOFFLRecordsArray,
                    WECHATONLRecordsArray);
        }else if(deviceMan.equalsIgnoreCase("PAX")) {
            this.printServicePAX.sendReportPAX(context, info, product,
                    ALIPAYCNOFFLRecordsArray,
                    ALIPAYHKOFFLRecordsArray,
                    ALIPAYOFFLRecordsArray,
                    BOOSTOFFLRecordsArray,
                    GCASHOFFLRecordsArray,
                    GRABPAYOFFLRecordsArray,
                    masterRecordsArray,
                    OEPAYOFFLRecordsArray,
                    PROMPTPAYOFFLRecordsArray,
                    UNIONPAYOFFLRecordsArray,
                    visaRecordsArray,
                    WECHATHKOFFLRecordsArray,
                    WECHATOFFLRecordsArray,
                    WECHATONLRecordsArray);
        }else if(deviceMan.equalsIgnoreCase("Centerm")){
            if (lang.equalsIgnoreCase(Constants.LANG_THAI)) {
                this.printServiceThaiK9.printReportCenterm(context, info, product,
                        ALIPAYCNOFFLRecordsArray,
                        ALIPAYHKOFFLRecordsArray,
                        ALIPAYOFFLRecordsArray,
                        BOOSTOFFLRecordsArray,
                        GCASHOFFLRecordsArray,
                        GRABPAYOFFLRecordsArray,
                        masterRecordsArray,
                        OEPAYOFFLRecordsArray,
                        PROMPTPAYOFFLRecordsArray,
                        UNIONPAYOFFLRecordsArray,
                        visaRecordsArray,
                        WECHATHKOFFLRecordsArray,
                        WECHATOFFLRecordsArray,
                        WECHATONLRecordsArray);
            } else {
                this.printServiceK9.printReportCenterm(context, info, product,
                        ALIPAYCNOFFLRecordsArray,
                        ALIPAYHKOFFLRecordsArray,
                        ALIPAYOFFLRecordsArray,
                        BOOSTOFFLRecordsArray,
                        GCASHOFFLRecordsArray,
                        GRABPAYOFFLRecordsArray,
                        masterRecordsArray,
                        OEPAYOFFLRecordsArray,
                        PROMPTPAYOFFLRecordsArray,
                        UNIONPAYOFFLRecordsArray,
                        visaRecordsArray,
                        WECHATHKOFFLRecordsArray,
                        WECHATOFFLRecordsArray,
                        WECHATONLRecordsArray);
            }
        }
    }

    public void sendCardPaymentReceipt(){
        initView();
        if(deviceMan.equalsIgnoreCase("SUNMI")){
//            this.printService.printRefundReport(context,info,product, ALIPAYHKOFFLRecordsArray, ALIPAYCNOFFLRecordsArray, ALIPAYOFFLRecordsArray,
//                    WECHATOFFLRecordsArray, WECHATONLRecordsArray, visaRecordsArray, masterRecordsArray);
        }else if(deviceMan.equalsIgnoreCase("PAX")){
            this.printServicePAX.printCardReceiptPAX(context, info, product, bitmap, signature);
        }
    }

    public void sendSettlement() {
        initView();

        if (deviceMan.equalsIgnoreCase("SUNMI")) {
            this.printService.sendSettlementReport(context,info,product,bitmap);
        } else if(deviceMan.equalsIgnoreCase("PAX")) {
            this.printServicePAX.sendPAX(context, info, product, bitmap);
        }
    }

    public void sendLastSettlement() {
        initView();

        if (deviceMan.equalsIgnoreCase("SUNMI")) {
            this.printService.sendSettlementReport(context,info,product,bitmap);
        } else if (deviceMan.equalsIgnoreCase("PAX")) {
            this.printServicePAX.printLastSettlementPAX(context, resultObject, progressBar);
        }
    }

    // Find language by sharePreferences
    public String getLanguage(Context context) {
        SharedPreferences pref_lang = context.getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String lang = pref_lang.getString(Constants.pref_Lang, "");
        return lang;
    }
}
