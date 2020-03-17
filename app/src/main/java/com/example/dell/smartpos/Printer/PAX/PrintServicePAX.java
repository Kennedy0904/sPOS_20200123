package com.example.dell.smartpos.Printer.PAX;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.GlobalFunction;
import com.example.dell.smartpos.R;
import com.example.dell.smartpos.Record;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class PrintServicePAX {

    public final static int IMAGE_SIZE = 360;
    private Context context = null;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice device = null;
    private static BluetoothSocket bluetoothSocket = null;
    private static OutputStream outputStream = null;
    private OutputStreamWriter mWriter = null;
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private boolean isConnection = false;

    public PrintServicePAX(Context context) {
        this.context = context;
    }

    public PrintServicePAX(Context context, String deviceAddress) {
        super();
        this.context = context;
        Log.d("OTTO","Context:3::"+this.context+",,"+context);

        if(deviceAddress != null && !"".equals(deviceAddress)) {
            this.device = this.bluetoothAdapter.getRemoteDevice(deviceAddress);
        }
    }

    //    /**
    //     * 连接蓝牙设备
    //     */
    //    public boolean connect() {
    //        if (!this.isConnection) {
    //            try {
    //                bluetoothSocket = this.device.createRfcommSocketToServiceRecord(uuid);
    //                bluetoothSocket.connect();
    //                outputStream = bluetoothSocket.getOutputStream();
    //                mWriter = new OutputStreamWriter(outputStream, "GBK");
    //                this.isConnection = true;
    //                if (this.bluetoothAdapter.isDiscovering()) {
    //                    Log.d("OTTO","关闭适配器！");
    //                    this.bluetoothAdapter.isDiscovering();
    //                }
    //            } catch (Exception e) {
    //                Log.d("OTTO","connect() 连接失败！----" + e);
    //                Toast.makeText(this.context, context.getString(R.string.bluetooth_connect_fail), Toast.LENGTH_SHORT).show();
    //                this.isConnection = false;
    //                return false;
    //            }
    //            Toast.makeText(this.context, this.device.getName() + context.getString(R.string.bluetooth_connect_success), Toast.LENGTH_SHORT).show();
    //            return true;
    //        } else {
    //            return true;
    //        }
    //    }

    public void sendPAX(final Context context, final Map<String,String> info, final Map<String,String> product, final Bitmap bitmap) {
        if (PrinterFunction.getInstance().getStatus().equalsIgnoreCase("Out of paper ")) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.outofpaper_error)
                    .setMessage(R.string.insert_new_paper)
                    .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String status = PrinterFunction.getInstance().getStatus();
                            if (status.equalsIgnoreCase("Success ")) {
                                printPAX(context, info, product, bitmap);
                            }else{
                                new AlertDialog.Builder(context)
                                        .setMessage(R.string.outofpaper_error)
                                        .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                            }
                        }
                    })
                    .setNegativeButton(context.getString(R.string.cancel), null)
                    .show();
        } else {
            printPAX(context, info, product, bitmap);
        }
    }

    private void printPAX(Context context, Map<String,String> info, Map<String,String> product, Bitmap bitmap){

        PrinterFunction.getInstance().init();

        if(GlobalFunction.getLanguage(context).equals(Constants.LANG_THAI)){
            PrinterFunction.getInstance().startThaiPrinting();
        }

        //        Toast.makeText(this.context, PrinterFunction.getInstance().getStatus(), Toast.LENGTH_SHORT).show();

        //        if(bitmap == null) {
        //            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_paydollar2);
        //        }

        PrinterFunction.getInstance().printBitmap(compressPic(bitmap));

        spaceSet("0", "10");

        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_24,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);
        printNextLine(1);


        //        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_32,
        //        (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);

        //            double merNameCenter = (24 - info.get("MerchantName").length()) / 2;
        //            String merNameFormat = "%" + merNameCenter + "s" + "" + "%" + info.get("MerchantName").length() + "s" + "" + "%" + merNameCenter + "s";
        //            PrinterFunction.getInstance().printStr(String.format(merNameFormat, "", info.get("MerchantName"), ""), null);
        printCenterHeader(info.get("MerchantName"));
        printNextLine(1);

        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_24,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);

        if (!GlobalFunction.getAddress1(context).equals("")) {
            printCenter(GlobalFunction.getAddress1(context));
            printNextLine(1);
        }

        if (!GlobalFunction.getAddress2(context).equals("")) {
            printCenter(GlobalFunction.getAddress2(context));
            printNextLine(1);
        }

        if (!GlobalFunction.getAddress3(context).equals("")) {
            printCenter(GlobalFunction.getAddress3(context));
            printNextLine(1);
        }

        printNextLine(1);

        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_24,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);

        printTwoColumn(info.get("PaymentType"), product.get("PayType"));
        printNextLine(1);

        printTwoColumn(info.get("PayMethod"), product.get("PayMethod"));
        printNextLine(1);

        printTwoColumn(info.get("PayStatus"), product.get("PayStatus"));
        printNextLine(1);

        printTwoColumn(info.get("Operator"), product.get("OperatorNumber"));
        printNextLine(1);

        printNextLine(1);

        printTwoColumn(info.get("TransactionDate"), product.get("TransactionDate"));
        printNextLine(1);

        printTwoColumn(info.get("CardNo"), product.get("CardNo"));
        printNextLine(1);


        printTwoColumn(info.get("MerchantRef"), product.get("MerchantRef"));
        printNextLine(1);

        printTwoColumn(info.get("PaymentRef"), product.get("PaymentRef"));
        printNextLine(1);

        printTwoColumn(info.get("TotalAmount"),  product.get("CurrCode") + " " + product.get("Amount"));
        printNextLine(2);


        SharedPreferences prefsettings = context.getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
        String SurCalstat = (prefsettings.getString(Constants.pref_surcharge_calculation, Constants.default_surcharge_calculation));
        SharedPreferences pref_sur = context.getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String mdr = pref_sur.getString(Constants.mer_mdr, "");
        if ((product.get("Surcharge") != null) || (!"".equals(product.get("Surcharge")))) {
            if ("OFF".equals(SurCalstat) || mdr == null || "".equals(mdr)) {
                Log.d("OTTO", "PrintService OFF null empty");
            } else if ("OFF".equals(SurCalstat) || Double.valueOf(mdr) <= 0) {
                Log.d("OTTO", "PrintService OFF or <0");
            } else if ("ON".equals(SurCalstat) && Double.valueOf(mdr) > 0) {
                if ("".equals(product.get("Surcharge")) || product.get("Surcharge") == null) {
                    Log.d("OTTO", "PrintService surcharge null empty");
                } else if (Double.valueOf(product.get("Surcharge")) <= 0) {
                    Log.d("OTTO", "PrintService surcharge <0");
                } else if ((Double.valueOf(product.get("Surcharge")) > 0)) {
                    Log.d("OTTO", "PrintService surcharge >0");
                    printTwoColumn(info.get("Surcharge"), product.get("CurrCode") + " " + product.get("Surcharge"));
                    printNextLine(1);

                    printTwoColumn(info.get("MerRequestAmt"), product.get("CurrCode") + " " + product.get("MerRequestAmt"));
                    printNextLine(1);
                } else {
                    Log.d("OTTO", "PrintService surcharge else");
                }
            } else {
                Log.d("OTTO", "PrintService else");
            }
        }

        printText(makelinefeed(info.get("note"), info.get("isCN")));
        printNextLine(1);

        printDashLine();
        printNextLine(1);

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = date.format(new Date());

        printCenter(now);
        printNextLine(1);

        String copy = "";
        if (info.get("copy").equalsIgnoreCase(context.getString(R.string.print_customerCopy))) {

            copy = "=== " + context.getString(R.string.print_customerCopy) + " ===";

        } else if (info.get("copy").equalsIgnoreCase(context.getString(R.string.print_merchantCopy))) {
            copy = "=== " + context.getString(R.string.print_merchantCopy) + " ===";
        }

        printCenter(copy);
        printNextLine(1);
        printCenter("***" + info.get("Title") + "***");
        printNextLine(5);

        PrinterFunction.getInstance().start();
    }


    public void sendSummaryReportPAX(final Context context, final Map<String,String> info, final Map<String,String> product) {

        if (PrinterFunction.getInstance().getStatus().equalsIgnoreCase("Out of paper ")) {

            new AlertDialog.Builder(context)
                    .setTitle(R.string.outofpaper_error)
                    .setMessage(R.string.insert_new_paper)
                    .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String status = PrinterFunction.getInstance().getStatus();

                            if (status.equalsIgnoreCase("Success ")) {
                                printSummaryReportPAX(context, info, product);
                            }else{
                                new AlertDialog.Builder(context)
                                        .setMessage(R.string.outofpaper_error)
                                        .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                            }
                        }
                    })
                    .setNegativeButton(context.getString(R.string.cancel), null)
                    .show();

        } else {
            printSummaryReportPAX(context, info, product);
        }
    }

    public void printSummaryReportPAX(Context context, Map<String,String> label, Map<String,String>value) {

        PrinterFunction.getInstance().init();

        if(GlobalFunction.getLanguage(context).equals(Constants.LANG_THAI)){
            PrinterFunction.getInstance().startThaiPrinting();
        }

        spaceSet("0", "10");

//        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_32,
//                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);

        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_24,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);

        String title = label.get("Title");
        printCenterHeader(title);
        printNextLine(1);

        printCenter(value.get("MerName"));
        printNextLine(1);

        printTwoColumn(label.get("FromDateLabel"), value.get("From_date"));
        printNextLine(1);

        printTwoColumn(label.get("ToDateLabel"), value.get("To_date"));
        printNextLine(1);

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        printText(date.format(new Date()));
        printNextLine(1);
        printDashLine();

        printCenter(label.get("Header"));
        printNextLine(1);

        printTwoColumn(label.get("GrandTotalTrxLabel"), value.get("currCode")+ " " +value.get("GrandTotalAmnt"));
        printNextLine(1);

        printTwoColumn(label.get("GrandTotalRefundLabel"), value.get("currCode")+ " " +value.get("GrandTotalRefundAmnt"));
        printNextLine(1);

        printTwoColumn(label.get("GrandTotalRequestRefundLabel"), value.get("currCode")+ " " +value.get("GrandTotalRequestRefundAmnt"));
        printNextLine(1);

        printTwoColumn(label.get("GrandTotalVoidLabel"),value.get("currCode")+ " " +value.get("GrandTotalVoidAmnt"));
        printNextLine(1);
        printDashLine();

        if(value.get("GrandTotalAmnt").equals("0.00") && value.get("GrandTotalRefundAmnt").equals("0.00") && value.get("GrandTotalRequestRefundAmnt").equals("0.00") && value.get("GrandTotalVoidAmnt").equals("0.00")){
            printCenter(label.get("NoTxnFound"));
            printNextLine(1);
            printDashLine();
        }

        // ALIPAYCNOFFL
        if(!value.get("TotalTrxALIPAYCNOFFL").equals("0")) {
            printCenter(label.get("ALIPAYCNOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYCNOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYCNOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxALIPAYCNOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntALIPAYCNOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxALIPAYCNOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntALIPAYCNOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxALIPAYCNOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntALIPAYCNOFFL"));

            printNextLine(1);
            printDashLine();
        }

        // ALIPAYHKOFFL
        if(!value.get("TotalTrxALIPAYHKOFFL").equals("0")) {
            printCenter(label.get("ALIPAYHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxALIPAYHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntALIPAYHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxALIPAYHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"),  value.get("currCode") + " " + value.get("TotalRequestRefundAmntALIPAYHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxALIPAYHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntALIPAYHKOFFL"));

            printNextLine(1);
            printDashLine();
        }

        // ALIPAYOFFL
        if(!value.get("TotalTrxALIPAYOFFL").equals("0")) {
            printCenter(label.get("ALIPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxALIPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntALIPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxALIPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntALIPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxALIPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntALIPAYOFFL"));

            printNextLine(1);
            printDashLine();
        }

        // BOOSTOFFL
        if(!value.get("TotalTrxBOOSTOFFL").equals("0")){
            printCenter(label.get("BOOSTOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxBOOSTOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode")+ " " +value.get("TotalAmntBOOSTOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxBOOSTOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRefundAmntBOOSTOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxBOOSTOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRequestRefundAmntBOOSTOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxBOOSTOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode")+ " " +value.get("TotalVoidAmntBOOSTOFFL"));

            printNextLine(1);
            printDashLine();
        }

        // GCASHOFFL
        if(!value.get("TotalTrxGCASHOFFL").equals("0")){
            printCenter(label.get("GCASHOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxGCASHOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode")+ " " +value.get("TotalAmntGCASHOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxGCASHOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRefundAmntGCASHOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxGCASHOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRequestRefundAmntGCASHOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxGCASHOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode")+ " " +value.get("TotalVoidAmntGCASHOFFL"));

            printNextLine(1);
            printDashLine();
        }

        // GRABPAYOFFL
        if(!value.get("TotalTrxGRABPAYOFFL").equals("0")){
            printCenter(label.get("GRABPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxGRABPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode")+ " " +value.get("TotalAmntGRABPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxGRABPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRefundAmntGRABPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxGRABPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRequestRefundAmntGRABPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxGRABPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode")+ " " +value.get("TotalVoidAmntGRABPAYOFFL"));

            printNextLine(1);
            printDashLine();
        }

        // MASTER
        if(!value.get("TotalTrxMASTER").equals("0")){
            printCenter(label.get("MASTER"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxMASTER"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode")+ " " +value.get("TotalAmntMASTER"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxMASTER"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntMASTER"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxMASTER"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"),  value.get("currCode") + " " + value.get("TotalRequestRefundAmntMASTER"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxMASTER"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntMASTER"));

            printNextLine(1);
            printDashLine();
        }

        // OEPAYOFFL
        if(!value.get("TotalTrxOEPAYOFFL").equals("0")){
            printCenter(label.get("OEPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxOEPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode")+ " " +value.get("TotalAmntOEPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxOEPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRefundAmntOEPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxOEPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRequestRefundAmntOEPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxOEPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode")+ " " +value.get("TotalVoidAmntOEPAYOFFL"));

            printNextLine(1);
            printDashLine();
        }

        // PROMPTPAYOFFL
        if(!value.get("TotalTrxPROMPTPAYOFFL").equals("0")){
            printCenter(label.get("PROMPTPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxPROMPTPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode")+ " " +value.get("TotalAmntPROMPTPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxPROMPTPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRefundAmntPROMPTPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxPROMPTPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRequestRefundAmntPROMPTPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxPROMPTPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode")+ " " +value.get("TotalVoidAmntPROMPTPAYOFFL"));

            printNextLine(1);
            printDashLine();
        }

        // UNIONPAYOFFL
        if(!value.get("TotalTrxUNIONPAYOFFL").equals("0")){
            printCenter(label.get("UNIONPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxUNIONPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode")+ " " +value.get("TotalAmntUNIONPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxUNIONPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRefundAmntUNIONPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxUNIONPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"), "");
            printNextLine(1);
            printTwoColumn("", value.get("currCode")+ " " +value.get("TotalRequestRefundAmntUNIONPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxUNIONPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode")+ " " +value.get("TotalVoidAmntUNIONPAYOFFL"));

            printNextLine(1);
            printDashLine();
        }

        // VISA
        if(!value.get("TotalTrxVISA").equals("0")){
            printCenter(label.get("VISA"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxVISA"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode")+ " " +value.get("TotalAmntVISA"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxVISA"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntVISA"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxVISA"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntVISA"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxVISA"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntVISA"));

            printNextLine(1);
            printDashLine();
        }

        // WECHATHKOFFL
        if (!value.get("TotalTrxWECHATHKOFFL").equals("0") || !value.get("TotalRefundTrxWECHATHKOFFL").equals("0") || !value.get("TotalRequestRefundTrxWECHATHKOFFL").equals("0") || !value.get("TotalVoidTrxWECHATHKOFFL").equals("0")) {
            printCenter(label.get("WECHATHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxWECHATHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntWECHATHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxWECHATHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntWECHATHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxWECHATHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntWECHATHKOFFL"));

            printNextLine(1);
            printDashLine();
        }

        // WECHATOFFL
        if(!value.get("TotalTrxWECHATOFFL").equals("0")) {
            printCenter(label.get("WECHATOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxWECHATOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRefundAmntWECHATOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxWECHATOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode") + " " + value.get("TotalRequestRefundAmntWECHATOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxWECHATOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode") + " " + value.get("TotalVoidAmntWECHATOFFL"));

            printNextLine(1);
            printDashLine();
        }

        // WECHATONL
        if(!value.get("TotalTrxWECHATONL").equals("0")){
            printCenter(label.get("WECHATONL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATONL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode")+ " " +value.get("TotalAmntWECHATONL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundTrxLabel"), value.get("TotalRefundTrxWECHATONL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRefundAmntWECHATONL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundTrxLabel"), value.get("TotalRequestRefundTrxWECHATONL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalRequestRefundAmntLabel"), value.get("currCode")+ " " +value.get("TotalRequestRefundAmntWECHATONL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidTrxLabel"), value.get("TotalVoidTrxWECHATONL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalVoidAmntLabel"), value.get("currCode")+ " " +value.get("TotalVoidAmntWECHATONL"));

            printNextLine(1);
            printDashLine();
        }

        printNextLine(1);
        printCenter("==="+label.get("Footer")+"===");

        printNextLine(5);

        PrinterFunction.getInstance().start();

    }

    public void sendReportPAX(final Context context, final Map<String,String> label, final Map<String,String>value,
                              final ArrayList<Record> ALIPAYCNOFFLRecordsArray,
                              final ArrayList<Record> ALIPAYHKOFFLRecordsArray,
                              final ArrayList<Record> ALIPAYOFFLRecordsArray,
                              final ArrayList<Record> BOOSTOFFLRecordsArray,
                              final ArrayList<Record> GCASHOFFLRecordsArray,
                              final ArrayList<Record> GRABPAYOFFLRecordsArray,
                              final ArrayList<Record> masterRecordsArray,
                              final ArrayList<Record> OEPAYOFFLRecordsArray,
                              final ArrayList<Record> PROMPTPAYOFFLRecordsArray,
                              final ArrayList<Record> UNIONPAYOFFLRecordsArray,
                              final ArrayList<Record> visaRecordsArray,
                              final ArrayList<Record> WECHATHKOFFLRecordsArray,
                              final ArrayList<Record> WECHATOFFLRecordsArray,
                              final ArrayList<Record> WECHATONLRecordsArray){

        printReportPAX(context,label, value,
                ALIPAYHKOFFLRecordsArray,
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
                WECHATOFFLRecordsArray,
                WECHATONLRecordsArray);

        if (PrinterFunction.getInstance().getStatus().equalsIgnoreCase("Out of paper ")) {

            new AlertDialog.Builder(context)
                    .setTitle(R.string.outofpaper_error)
                    .setMessage(R.string.insert_new_paper)
                    .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String status = PrinterFunction.getInstance().getStatus();

                            if (status.equalsIgnoreCase("Success ")) {
                                printReportPAX(context,label, value,
                                        ALIPAYHKOFFLRecordsArray,
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
                                        WECHATONLRecordsArray);
                            }else{
                                new AlertDialog.Builder(context)
                                        .setMessage(R.string.outofpaper_error)
                                        .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                            }
                        }
                    })
                    .setNegativeButton(context.getString(R.string.cancel), null)
                    .show();

        } else {
            //Toast.makeText(this.context, R.string.bluetooth_connect_fail, Toast.LENGTH_SHORT).show();
        }
    }

    /** Transaction Report, Void Report, Refund Report, Request Refund Report all printed here!! **/
    public void printReportPAX(Context context, Map<String,String> label, Map<String,String>value,
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
                               ArrayList<Record> WECHATONLRecordsArray)  {
        //
        PrinterFunction.getInstance().init();

        spaceSet("0", "10");

        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_24,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);

        String title = label.get("Title");
        printCenterHeader(title);
        printNextLine(1);

        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_24,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);

        printCenter(value.get("MerName"));
        printNextLine(2);

        printTwoColumn(label.get("FromDateLabel"), value.get("From_date"));
        printNextLine(1);

        printTwoColumn(label.get("ToDateLabel"), value.get("To_date"));
        printNextLine(1);

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        printText(date.format(new Date()));
        printNextLine(1);
        printDashLine();

        if (value.get("TotalTrxALIPAYCNOFFL").equals("0")
                && value.get("TotalTrxALIPAYHKOFFL").equals("0")
                && value.get("TotalTrxALIPAYOFFL").equals("0")
                && value.get("TotalTrxBOOSTOFFL").equals("0")
                && value.get("TotalTrxGCASHOFFL").equals("0")
                && value.get("TotalTrxGRABPAYOFFL").equals("0")
                && value.get("TotalTrxMASTER").equals("0")
                && value.get("TotalTrxOEPAYOFFL").equals("0")
                && value.get("TotalTrxPROMPTPAYOFFL").equals("0")
                && value.get("TotalTrxUNIONPAYOFFL").equals("0")
                && value.get("TotalTrxVISA").equals("0")
                && value.get("TotalTrxWECHATOFFL").equals("0")
                && value.get("TotalTrxWECHATHKOFFL").equals("0")
                && value.get("TotalTrxWECHATONL").equals("0") ) {
            printCenter(label.get("NoTxnFound"));
            printNextLine(1);
            printDashLine();
        }

        // ALIPAYCNOFFL
        if (!value.get("TotalTrxALIPAYCNOFFL").equals("0")) {
            printCenter(label.get("ALIPAYCNOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYCNOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYCNOFFL"));
            printNextLine(1);
            printDashLine();

            printQrContent(label, ALIPAYCNOFFLRecordsArray);
        }

        // ALIPAYHKOFFL
        if (!value.get("TotalTrxALIPAYHKOFFL").equals("0")) {
            printCenter(label.get("ALIPAYHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYHKOFFL"));
            printNextLine(1);
            printDashLine();

            printQrContent(label, ALIPAYHKOFFLRecordsArray);
        }

        // ALIPAYOFFL
        if (!value.get("TotalTrxALIPAYOFFL").equals("0")) {
            printCenter(label.get("ALIPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxALIPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntALIPAYOFFL"));
            printNextLine(1);
            printDashLine();

            printQrContent(label, ALIPAYOFFLRecordsArray);
        }

        // BOOSTOFFL
        if (!value.get("TotalTrxBOOSTOFFL").equals("0")) {
            printCenter(label.get("BOOSTOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxBOOSTOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntBOOSTOFFL"));
            printNextLine(1);
            printDashLine();

            printQrContent(label, BOOSTOFFLRecordsArray);
        }

        // GCASHOFFL
        if (!value.get("TotalTrxGCASHOFFL").equals("0")) {
            printCenter(label.get("GCASHOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxGCASHOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGCASHOFFL"));
            printNextLine(1);
            printDashLine();

            printQrContent(label, GCASHOFFLRecordsArray);
        }

        // GRABPAYOFFL
        if (!value.get("TotalTrxGRABPAYOFFL").equals("0")) {
            printCenter(label.get("GRABPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxGRABPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntGRABPAYOFFL"));
            printNextLine(1);
            printDashLine();

            printQrContent(label, GRABPAYOFFLRecordsArray);
        }

        // MASTER
        if (!value.get("TotalTrxMASTER").equals("0")) {
            printCenter(label.get("MASTER"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxMASTER"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntMASTER"));
            printNextLine(1);
            printDashLine();

            printCardContent(label, masterRecordsArray);
        }

        // OEPAYOFFL
        if (!value.get("TotalTrxOEPAYOFFL").equals("0")) {
            printCenter(label.get("OEPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxOEPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntOEPAYOFFL"));
            printNextLine(1);
            printDashLine();

            printQrContent(label, OEPAYOFFLRecordsArray);
        }

        // PROMPTPAYOFFL
        if (!value.get("TotalTrxPROMPTPAYOFFL").equals("0")) {
            printCenter(label.get("PROMPTPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxPROMPTPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntPROMPTPAYOFFL"));
            printNextLine(1);
            printDashLine();

            printQrContent(label, PROMPTPAYOFFLRecordsArray);
        }

        // UNIONPAYOFFL
        if (!value.get("TotalTrxUNIONPAYOFFL").equals("0")) {
            printCenter(label.get("UNIONPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxUNIONPAYOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntUNIONPAYOFFL"));
            printNextLine(1);
            printDashLine();

            printCardContent(label, UNIONPAYOFFLRecordsArray);
        }

        // VISA
        if (!value.get("TotalTrxVISA").equals("0")) {
            printCenter(label.get("VISA"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxVISA"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntVISA"));
            printNextLine(1);
            printDashLine();

            printCardContent(label, visaRecordsArray);
        }

        if (!value.get("TotalTrxWECHATHKOFFL").equals("0")) {
            printCenter(label.get("WECHATHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATHKOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATHKOFFL"));
            printNextLine(1);
            printDashLine();

            printQrContent(label, WECHATHKOFFLRecordsArray);
        }

        // WECHATOFFL
        if (!value.get("TotalTrxWECHATOFFL").equals("0")) {
            printCenter(label.get("WECHATOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATOFFL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATOFFL"));
            printNextLine(1);
            printDashLine();

            printQrContent(label, WECHATOFFLRecordsArray);
        }

        // WECHATONL
        if (!value.get("TotalTrxWECHATONL").equals("0")) {
            printCenter(label.get("WECHATONL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalTrxLabel"), value.get("TotalTrxWECHATONL"));
            printNextLine(1);
            printTwoColumn(label.get("TotalAmntLabel"), value.get("currCode") + " " + value.get("TotalAmntWECHATONL"));
            printNextLine(1);
            printDashLine();

            printQrContent(label, WECHATONLRecordsArray);
        }

        //printNextLine(1);
        printCenter("===" + label.get("Footer") + "===");
        printNextLine(5);
        PrinterFunction.getInstance().start();
    }


    public void printQrContent(Map<String, String> label, ArrayList<Record> records) {

        for(Record record: records) {
            String trxDate = record.getOrderdate();
            String datetrx = trxDate.substring(0,8);
            String timetrx = trxDate.substring(8,14);
            StringBuilder sbdate = new StringBuilder(datetrx);
            sbdate.insert(2, "/");
            sbdate.insert(5,"/");
            StringBuilder sbtime = new StringBuilder(timetrx);
            sbtime.insert(2, ":");
            sbtime.insert(5, ":");

            String currency = record.currency();
            String amount = record.getamt();
            String merRef = record.merref();
            String paymentRef = record.PayRef();
            String qr = record.getCardNo();

            printTwoColumn(label.get("TransactionDate"), sbdate.toString() + " "  + sbtime.toString());
            printTwoColumn(label.get("Amount"), currency + " " + amount);
            printTwoColumn(label.get("MerRef"), merRef);
            printTwoColumn(label.get("PaymentRef"), paymentRef);
            printTwoColumn(label.get("QRNumber"), qr);
            printNextLine(1);
            printDashLine();
            printNextLine(1);
        }
    }

    public void printCardContent(Map<String, String> label, ArrayList<Record> records) {

        for (Record record : records) {
            String trxDate = record.getOrderdate();
            String datetrx = trxDate.substring(0, 8);
            String timetrx = trxDate.substring(8, 14);
            StringBuilder sbdate = new StringBuilder(datetrx);
            sbdate.insert(2, "/");
            sbdate.insert(5, "/");
            StringBuilder sbtime = new StringBuilder(timetrx);
            sbtime.insert(2, ":");
            sbtime.insert(5, ":");

            String currency = record.currency();
            String amount = record.getamt();
            String merRef = record.merref();
            String paymentRef = record.PayRef();
            String invoiceNo = record.getInvoiceNo();

            printTwoColumn(label.get("TransactionDate"), sbdate.toString() + " " + sbtime.toString());
            printTwoColumn(label.get("Amount"), currency + " " + amount);
            printTwoColumn(label.get("MerRef"), merRef);
            printTwoColumn(label.get("PaymentRef"), paymentRef);
            printTwoColumn(label.get("InvoiceNo"), invoiceNo);
            printNextLine(1);
            printDashLine();
            printNextLine(1);

        }
    }


    //Print Card Payment  Receipt
    public void printCardReceiptPAX(Context context, Map<String,String> label, Map<String,String>value, Bitmap bitmap, Bitmap signature) {

        PrinterFunction.getInstance().init();
        Log.d("TransactionPrint", "Start Printing");
        PrinterFunction.getInstance().printBitmap(compressPic(bitmap));
        printNextLine(1);

        spaceSet("0", "10");

        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_24,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);
        printCenter("*** " + value.get("Title") + " ***");
        printNextLine(2);

        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_32,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);

        //Merchant Info Section
        //        printCenterHeader(value.get("MerName"));
        printNextLine(1);

        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_24,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);

        printText(value.get("MerAddress"));
        printNextLine(1);

        printTwoColumn(label.get("TerminalID"), value.get("TerminalID"));
        printNextLine(1);

        printTwoColumn(label.get("MerID"), value.get("MerID"));
        printNextLine(1);
        printNextLine(1);

        //Payment Info Section
        printTwoColumn(label.get("PayMethod"), value.get("PayMethod") + " (" + value.get("EntryMode") + ")");
        printNextLine(1);

        printTwoColumn(label.get("TrxType"), value.get("TrxType"));
        printNextLine(1);

        printTwoColumn(label.get("CardNo"), value.get("CardNo"));
        printNextLine(1);

        printTwoColumn(label.get("epDate"), value.get("epDate"));
        printNextLine(1);

        printTwoColumn(label.get("CardHolder"), value.get("CardHolder"));
        printNextLine(1);

        printTwoColumn(label.get("MerRef"), value.get("MerRef"));
        printNextLine(1);

        printTwoColumn(label.get("BatchNo"), value.get("BatchNo"));
        printNextLine(1);

        printTwoColumn(label.get("TraceNo"), value.get("TraceNo"));
        printNextLine(1);

        printTwoColumn(label.get("TrxTime"), value.get("TrxTime"));
        printNextLine(1);

        printTwoColumn(label.get("RRN"), value.get("RRN"));
        printNextLine(1);

        printTwoColumn(label.get("ApproveCode"), value.get("ApproveCode"));
        printNextLine(1);

        printTwoColumn(label.get("AppName"), value.get("AppName"));
        printNextLine(1);

        printTwoColumn(label.get("AID"), value.get("AID"));
        printNextLine(1);

        printTwoColumn(label.get("TC"), value.get("TC"));
        printNextLine(1);

        printTwoColumn(label.get("TrxID"), value.get("TrxID"));
        printNextLine(1);

        //Total Amount Section
        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_32,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);
        printTwoColumnLargeText(label.get("TotalAmnt"), value.get("currCode") + " " + value.get("TotalAmnt"));
        printNextLine(2);

        //Footer Section
        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_24,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);

        if(value.get("CVMResult").equalsIgnoreCase(Constants.cvmResult_OnlinePIN)){
            printCenter(context.getString(R.string.print_noSignature));
            printNextLine(1);
            printDashLine();

        }else if(value.get("CVMResult").equalsIgnoreCase(Constants.cvmResult_Signature)){

            if(signature != null){
                PrinterFunction.getInstance().printBitmap(compressPic(signature));
            }else{
                printNextLine(3);
            }
            printDashLine();
            printCenter(context.getString(R.string.print_Signature));

        }else if(value.get("CVMResult").equalsIgnoreCase(Constants.cvmResult_NoCVM)){
            printCenter(context.getString(R.string.print_noPIN));
            printNextLine(1);
            printCenter(context.getString(R.string.print_noSignature));
            printNextLine(1);
            printDashLine();
        }

        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_8_16,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);
        printCenterSmallText(context.getString(R.string.print_DisclaimerMsg1));
        printNextLine(1);
        printCenterSmallText(context.getString(R.string.print_DisclaimerMsg2));
        printNextLine(2);

        PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_24,
                (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);
        printCenter("=== "+value.get("Footer")+"  ===");
        printNextLine(1);

        printCenter(label.get("Version") + " " + value.get("Version"));
        printNextLine(5);

        PrinterFunction.getInstance().start();
    }

    public void printLastSettlementPAX(Context context, JSONObject resultObject, FrameLayout progressBar) {

        try {
            JSONArray resultArray;
            resultArray = resultObject.getJSONArray("data");

            PrinterFunction.getInstance().init();

            if(GlobalFunction.getLanguage(context).equals(Constants.LANG_THAI)){
                PrinterFunction.getInstance().startThaiPrinting();
            }

            spaceSet("0", "10");

            PrinterFunction.getInstance().fontSet((EFontTypeAscii) EFontTypeAscii.FONT_16_24,
                    (EFontTypeExtCode) EFontTypeExtCode.FONT_24_24);

            printCenterHeader(context.getString(R.string.print_last_settlement_report));
            printNextLine(1);

            printDoubleDashLine();
            printNextLine(1);

            printTwoColumn(context.getString(R.string.date) + resultObject.optString("date", ""),
                    context.getString(R.string.time) + resultObject.optString("time", ""));
            printNextLine(1);

            printTwoColumn(context.getString(R.string.mid) + resultObject.optString("mid", ""),
                    context.getString(R.string.print_tid) + resultObject.optString("tid", ""));
            printNextLine(1);

            printText(context.getString(R.string.batch_no) + resultObject.optString("batchNo", ""));
            printNextLine(1);

            printText(context.getString(R.string.host) + resultObject.optString("host", ""));
            printNextLine(2);

            if (resultArray != null && resultArray.length() > 0) {
                for (int i = 0; i<resultArray.length(); i++) {

                    JSONObject json = new JSONObject(resultArray.optString(i));
                    String method = json.optString("method", "");
                    String saleTxn = json.optString("saleTxn", "");
                    String saleAmount = json.optString("saleAmount", "");
                    String refundTxn = json.optString("refundTxn", "");
                    String refundAmount = json.optString("refundAmount", "");
                    String voidTxn = json.optString("voidTxn", "");
                    String voidAmount = json.optString("voidAmount","");
                    String voidRefundTxn = json.optString("voidRefundTxn", "");
                    String voidRefundAmount = json.optString("voidRefundAmount","");
                    String offlineTxn = json.optString("offlineTxn", "");
                    String offlineAmount = json.optString("offlineAmount","");
                    String preAuthTxn = json.optString("preAuthTxn", "");
                    String preAuthAmount = json.optString("preAuthAmount","");
                    String compCancelTxn = json.optString("compCancelTxn", "");
                    String compCancelAmount = json.optString("compCancelAmount","");
                    String totalTxn = json.optString("totalTxn","");

                    String totalAmount = json.optString("totalAmount","");

                    printCenterHeader(method);
					printNextLine(1);

					printThreeColumn(context.getString(R.string.transaction_2),
							context.getString(R.string.count),
							context.getString(R.string.amount_2));

					printThreeColumn(context.getString(R.string.sale), saleTxn, saleAmount);
                    printThreeColumn(context.getString(R.string.refund), refundTxn, refundAmount);
                    printThreeColumn(context.getString(R.string.voided_sale), voidTxn, voidAmount);
                    printThreeColumn(context.getString(R.string.voided_refund), voidRefundTxn, voidRefundAmount);
                    printThreeColumn(context.getString(R.string.offline), offlineTxn, offlineAmount);
                    printThreeColumn(context.getString(R.string.preauth_comp), preAuthTxn, preAuthAmount);
                    printThreeColumn(context.getString(R.string.comp_cancel), compCancelTxn, compCancelAmount);
                    printNextLine(1);
                    printDashLine();
                    printNextLine(1);
                    printThreeColumn(context.getString(R.string.print_total), totalTxn, totalAmount);
                    printNextLine(2);

                    System.out.println("bbb" + method);

                }
            }

            printNextLine(3);

            Handler handler = null;
            Runnable runObj = null;

            runObj = new Runnable() {
                public void run() {
                    PrinterFunction.getInstance().start();
                    progressBar.setVisibility(View.GONE);
                }
            };
            handler = new Handler();
            handler.postDelayed(runObj, 1000);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //---------------------------printer layout things--------------------------------//

    public void printNextLine(int line){
        for(int i=1; i<=line; i++){
            PrinterFunction.getInstance().printStr("\n", null);
        }
    }

    public void spaceSet(String wordSpace, String lineSpace){
        PrinterFunction.getInstance().spaceSet(Byte.parseByte(wordSpace),
                Byte.parseByte(lineSpace));
    }

    public void printDashLine(){
        PrinterFunction.getInstance().printStr("--------------------------------", null);
    }

    public void printDoubleDashLine(){
        PrinterFunction.getInstance().printStr("================================", null);
    }

    public void printText(String text){
        PrinterFunction.getInstance().printStr(text, null);
    }

    public void printCenter(String text){
        int textLength = countWordLength(text);

//        System.out.println("textaaaa " + text);
//        System.out.println("textLengthaaaa " + textLength);

        if ((32 - textLength) / 2 == 0) {
            printText(text);
        } else {
            double dblCenter = (32 - textLength) / 2;
//            System.out.println("dblCenteraaaa " + dblCenter);

            String centerFormat = "%" + dblCenter + "s" + "" + "%" + text.length() + "s" + "" + "%" + dblCenter + "s";
            PrinterFunction.getInstance().printStr(String.format(centerFormat, "", text, ""), null);
        }

    }

    public void printCenterHeader(String text){

        int textLength = countWordLength(text);

        if ((32 - textLength) / 2 == 0) {
            printText(text);
        } else {
            double headerCenter = (32 - textLength) / 2;
            String centerFormat = "%" + headerCenter + "s" + "" + "%" + text.length() + "s" + "" + "%" + headerCenter + "s";
            PrinterFunction.getInstance().printStr(String.format(centerFormat, "", text, ""), null);
        }
    }

    public void printTwoColumn(String title, String content) {

        int totalLength = 32; // Total word count can fit into a line
        int titleLength = 0;
        int contentLength = 0;
        int spaceLength = 0;

        // Get word count length of title
        titleLength = countWordLength(title);

        // Get word count length of content
        contentLength = countWordLength(content);

        // Get space length between title and content
        spaceLength = totalLength - (titleLength + contentLength);

        // Total text word count in a line excludes space
        int textLength = titleLength + contentLength;

        // Total space/length for content in a line
        int contentSpace = totalLength - titleLength;

        StringBuffer sb = new StringBuffer();

        // If text cannot fit into a single line
        if (textLength > totalLength) {

            // Get how many lines required to fit the input text
            int loop = contentLength / (contentSpace);

            // Add a line for the remaining words
            if (contentLength % (contentSpace) != 0) {
                loop += 1;
            }

            List<String> strings = new ArrayList<String>();

            int index = 0;

            while (index < content.length()) {

                StringBuffer sbContent = new StringBuffer();

                // Append character to a string without exceed content space
                while ((index < content.length()) && countWordLength(sbContent.toString() + content.charAt(index)) <= contentSpace) {

                    sbContent.append(content.charAt(index));
                    index++;
                }

                // Add the string into a list
                strings.add(sbContent.toString());
                //                index += sb1.toString().length();
            }

            for (int count = 0; count < loop; count++) {

                // First line (title + content)
                if (count == 0) {

                    sb.append(title);

                    // In some cases, space is required even in the first line
                    // Example: "xxxx付" = 6 word counts, space allowed for content is only 5
                    // The output should be: " xxxx"
                    //                       "    付"
                    int j = 1;
                    while (j <= (totalLength - (titleLength + countWordLength(strings.get(count))))) {
                        sb.append(" ");
                        j++;
                    }

                    sb.append(strings.get(count));

                } else {

                    // Get space(s) for following lines (space + content)
                    int j = 1;

                    while (j <= (totalLength - countWordLength(strings.get(count)))) {
                        sb.append(" ");
                        j++;
                    }

                    sb.append(strings.get(count));
                }

                // Append a new line except for the last line
                if (count != loop - 1) {
                    sb.append("\n");
                }

                Log.d("Print Line " + loop, sb.toString());
            }
        } else {

            // For the case which text can fit into a single line
            sb.append(title);

            spaceLength = totalLength - (titleLength + contentLength);

            int j = 1;
            while (j <= spaceLength) {
                sb.append(" ");
                j++;
            }

            sb.append(content);

            //            String space = "";
            //            int j = 0;
            //            while(j < spaceLength){
            //                space += " ";
            //                j++;
            //            }
            //
            //            String text = title + space + content;
            //            PrinterFunction.getInstance().printStr(text, null);
        }

        PrinterFunction.getInstance().printStr(sb.toString(), null);
    }

    public void printThreeColumn(String title, String midContent, String endContent) {
        int totalLength = 32;
        int titleLength = 0;
        int midContentLength = 0;
        int midContentPlace = 0;
        int endContentLength = 0;
        int finalContentLength = 0;


        StringBuffer sb = new StringBuffer();

        titleLength = countWordLength(title);
        midContentLength = countWordLength(midContent);
        endContentLength = countWordLength(endContent);

        finalContentLength = finalContentLength + titleLength;

        sb.append(title);

        // Control the middle content place
        if(midContent.equalsIgnoreCase(context.getString(R.string.count))) {
            midContentPlace = 13;
        } else {
            midContentPlace = 15;
        }

        while(finalContentLength < midContentPlace) {
            sb.append(" ");
            finalContentLength++;
        }

        sb.append(midContent);
        finalContentLength = finalContentLength + midContentLength + endContentLength;


        while(finalContentLength != totalLength) {
            sb.append(" ");
            finalContentLength++;
        }

        sb.append(endContent);

        PrinterFunction.getInstance().printStr(sb.toString(), null);
    }

    public static String makelinefeed(String s, String isCN){
        String result = "";
        if("T".equals(isCN)) {
            result = makelinefeedCN(s);
        }else {
            result = makelinefeedEN(s);
        }
        return result;
    }

    public static String makelinefeedEN(String s) {
        String result = "";

        String[] str = s.split(" ");
        int len = 0;
        String res_str = "";

        for(int i=0;i<str.length;i++) {
            if((len + str[i].length())>32) {
                res_str = res_str + "\n" + str[i] + " ";
                len = str[i].length() + 1;
            }else {
                if((len + str[i].length() + 1)>=32){
                    res_str = res_str + str[i];
                    len = len + str[i].length();
                }else{
                    res_str = res_str + str[i] + " ";
                    len = len + str[i].length() + 1;
                }
            }
        }
        result = res_str;
        result = result.replace(", ",",");
        result = result.replace(". ",".");
        return result;
    }
    public static String makelinefeedCN(String s) {
        String result = "";
        result = s;
        return result;
    }

    private Bitmap compressPic(Bitmap bitmapOrg) {
        // 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        // 定义预转换成的图片的宽度和高度
        int newWidth = IMAGE_SIZE + 18;
        int newHeight = IMAGE_SIZE/2;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }

    public void printCenterSmallText(String text){
        double headerCenter = (48 - text.length()) / 2;
        String centerFormat = "%" + headerCenter + "s" + "" + "%" + text.length() + "s" + "" + "%" + headerCenter + "s";
        PrinterFunction.getInstance().printStr(String.format(centerFormat, "", text, ""), null);
    }

    public void printTwoColumnLargeText(String title, String content){
        String titleFormat = "%-" + title.length() + "s";
        String contentFormat = "%" + (24 - title.length()) + "s";
        // compose the complete format information
        String columnFormat = titleFormat + "" + contentFormat;

        PrinterFunction.getInstance().printStr(String.format(columnFormat, title, content), null);
    }

    // Return the actual length of words when printing in PAX (ascii = 1 space, CN = 2 spaces)
    public static int countWordLength(String word){

        int wordLength = 0;

        for (int i = 0; i < word.length(); ) {
            int codepoint = word.codePointAt(i);
            i += Character.charCount(codepoint);

            // If the character is a chinese character
            if (Character.UnicodeBlock.of(codepoint) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || Character.UnicodeBlock.of(codepoint) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || Character.UnicodeBlock.of(codepoint) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                    || Character.UnicodeBlock.of(codepoint) == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || Character.UnicodeBlock.of(codepoint) == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                    || Character.UnicodeBlock.of(codepoint) == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS) {
                wordLength += 2;
            } else {
                wordLength += 1;
            }
        }

        return wordLength;
    }
}