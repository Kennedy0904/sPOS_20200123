package com.example.dell.smartpos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.smartpos.Scanner.CameraCapture;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ScanQRPayment_2 extends AppCompatActivity {

	private RelativeLayout coupon;

	private TextView step1;
	private TextView step2;
	private TextView step3;
	private TextView step4;

	public TextView txtAmount = null;
	public TextView txtCurrency = null;
	public TextView MerchantRef = null;

	private ImageView couponApplied;

	boolean surC = false; //surcharge calution function
	boolean mdr_less_equal_0 = false; //mdr less than or equal to 0

	boolean A = false;
	boolean ACN = false;
	boolean AHK = false;
	boolean W = false;
	boolean WHK = false;
	boolean ATH = false;
	boolean UP = false;
	boolean MW = false;
	boolean GP = false;
	boolean LP = false;
	boolean B = false;
	boolean GC = false;

	public int timeout = 0;
	String currCode = "";
	String currCode1 = "";
	String merchantName = "";
	String payType = "";
	String payMethod = "";
	String hideSurcharge = "";
	String merchant_id = "";
	//--Edited 25/07/18 by KJ--//
	String merchantId = "";
	String discount = "";
	String promoCode = "";

    private AlertDialog alert;
    //--done Edited 25/07/18 by KJ--//

    Toast numberpadtoast = null;

    RecyclerView recyclerView;

    private static int SCANNING_REQUEST_CODE;

    //*******************scan para**************************//
    //----for Wechat----//
    ProgressDialog progressDialog_WECHAT;
    private final static int WX_SCANNIN_GREQUEST_CODE = 1;
    private final static String pMothodWX = "WECHATOFFL";
    //----for Wechat----//

    //----for Alipay----//
    ProgressDialog progressDialog_ALIPAY;
    private final static int A_SCANNIN_GREQUEST_CODE = 2;
    private final static String pMothodA = "ALIPAYOFFL";
    //----for Alipay----//

    //----for AlipayCN----//
    ProgressDialog progressDialog_ALIPAYCN;
    private final static int ACN_SCANNIN_GREQUEST_CODE = 3;
    private final static String pMothodACN = "ALIPAYCNOFFL";
    //----for AlipayCN----//

    //----for AlipayHK----//
    ProgressDialog progressDialog_ALIPAYHK;
    private final static int AHK_SCANNIN_GREQUEST_CODE = 4;
    private final static String pMothodAHK = "ALIPAYHKOFFL";
    //----for AlipayHK----//

    //----for UnionPay----//
    ProgressDialog progressDialog_UnionPay;
    private final static int UNIONPAY_SCANNIN_GREQUEST_CODE = 5;
    private final static String pMothodUNIPAY = "UNIONPAYOFFL";
    //----for UnionPay----//

    //----for GrabPay----//
    ProgressDialog progressDialog_GrabPay;
    private final static int GRABPAY_SCANNIN_GREQUEST_CODE = 6;
    private final static String pMothodGrabPay = "GRABPAYOFFL";
    //----for GrabPay----//

    //----for LinePay----//
    private final static int LINEPAY_SCANNIN_GREQUEST_CODE = 7;
    private final static String pMothodLINEPAY = "LINEPAY";
    //----for LinePay----//

    //----for Wechathk----//
    ProgressDialog progressDialog_WECHATHK;
    private final static int WXHK_SCANNIN_GREQUEST_CODE = 8;
    private final static String pMothodWXHK = "WECHATHKOFFL";
    //----for Wechathk----//

    //----for AlipayHK----//
    ProgressDialog progressDialog_ALIPAYTH;
    private final static int ATH_SCANNIN_GREQUEST_CODE = 9;
    private final static String pMothodATH = "ALIPAYTHOFFL";
    //----for AlipayHK----//

    //----for MasterWallet----//
    private final static int MasterWallet_SCANNIN_GREQUEST_CODE = 10;
    private final static String pMothodMaster = "MASTERWALLET";
    //----for MasterWallet----//

    //----for Boost----//
    ProgressDialog progressDialog_Boost;
    private final static int B_SCANNIN_GREQUEST_CODE = 11;
    private final static String pMothodB = "BOOSTOFFL";
    //----for Boost----//

    //----for GCash----//
    ProgressDialog progressDialog_GCash;
    private final static int GC_SCANNIN_GREQUEST_CODE = 12;
    private final static String pMothodGC = "GCASHOFFL";
    //----for GCash----//

    //*******************scan para**************************//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.scan_qr);
        setContentView(R.layout.activity_qrpayment_2);

        final Intent paymentIntent = getIntent();
        merchant_id = paymentIntent.getStringExtra(Constants.MERID);
        discount = paymentIntent.getStringExtra("coupon");

        System.out.println("Discount: "+ discount);

        step1 = (TextView) findViewById(R.id.progress_step_1);
        step2 = (TextView) findViewById(R.id.progress_step_2);
        step3 = (TextView) findViewById(R.id.progress_step_3);
        step4 = (TextView) findViewById(R.id.progress_step_4);

        step1.setText(R.string.enter_amount);
        step2.setText(R.string.select_qr_payment);
        step3.setText(R.string.read_qr);
        step4.setText(R.string.payment_result);

        step2.setTypeface(null, Typeface.BOLD);

        //*******************layout init**************************//
        //----merchant textview start----//
        MerchantRef = (TextView) findViewById(R.id.edtMerchantRef);
        txtAmount = (TextView) findViewById(R.id.edtAmount);
        txtCurrency = (TextView) findViewById(R.id.transactionreport_currCode);
        couponApplied = (ImageView) findViewById(R.id.ic_coupon);
        coupon = (RelativeLayout) findViewById(R.id.coupon);

        //*******************set data****************************//


        if (mdr_less_equal_0 || !surC) {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            String amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra(Constants.PRE_AMOUNT)));
            txtAmount.setText(amount);
        } else {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            String amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra(Constants.PRE_MDR_AMOUNT)));
            txtAmount.setText(amount);
        }

        if(discount != null){
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            String amount = formatter.format(Double.parseDouble(paymentIntent.getStringExtra("afterDiscountAmount")));
            txtAmount.setText(amount);
            couponApplied.setImageResource(R.drawable.ic_used_coupon);
            promoCode = paymentIntent.getStringExtra("promoCode");
        }

        currCode = paymentIntent.getStringExtra(Constants.CURRCODE);
        currCode1 = paymentIntent.getStringExtra(Constants.CURRENCY);
        txtCurrency.setText(currCode);
        MerchantRef.setText(paymentIntent.getStringExtra(Constants.MERCHANT_REF));
        hideSurcharge = paymentIntent.getStringExtra(Constants.pref_hideSurcharge);
        payMethod = paymentIntent.getStringExtra(Constants.PAYMETHODLIST);
        payType = paymentIntent.getStringExtra(Constants.PAYTYPE);

        System.out.println("---scan qr: pmtd- " + payMethod);

        String[] payMethod_ALIPAY = {"ALIPAYOFFL"};
        String[] payMethod_ALIPAYHK = {"ALIPAYHKOFFL"};
        String[] payMethod_ALIPAYCN = {"ALIPAYCNOFFL"};
        String[] payMethod_WECHATOFFL = {"WECHATOFFL"};
        String[] payMethod_WECHATHKOFFL = {"WECHATHKOFFL"};
        String[] payMethod_UNIONPAY = {"UNIONPAYOFFL"};
        String[] payMethod_MASTERPASS = {"MasterPass"};
        String[] payMethod_ALIPAYTH = {"ALIPAYTHOFFL"};
        String[] payMethod_BOOST = {"BOOSTOFFL"};
        String[] payMethod_GCASH = {"GCASHOFFL"};
        String[] payMethod_GrabPay = {"GRABPAYOFFL"};

        A = containstr(payMethod, payMethod_ALIPAY);
        AHK = containstr(payMethod, payMethod_ALIPAYHK);
        ACN = containstr(payMethod, payMethod_ALIPAYCN);
        W = containstr(payMethod, payMethod_WECHATOFFL);
        UP = containstr(payMethod, payMethod_UNIONPAY);
        MW = containstr(payMethod, payMethod_MASTERPASS);
        ATH = containstr(payMethod, payMethod_ALIPAYTH);
        WHK = containstr(payMethod, payMethod_WECHATHKOFFL);
        B = containstr(payMethod, payMethod_BOOST);
        GC = containstr(payMethod, payMethod_GCASH);
        GP = containstr(payMethod, payMethod_GrabPay);

        if (merchant_id.equals("560200335") || merchant_id.equals("560200303") /*|| merchant_id.equals("88143639") || merchant_id.equals("85003895") || merchant_id.equals("76067317")*/) {
            A = true;
            ACN = true;
            AHK = true;
            ATH = true;
            W = true;
            WHK = true;
            UP = true;
            MW = true;
            LP = true;
            GP = true;
            B = true;
            GC = true;

        } else {
            coupon.setVisibility(View.GONE);
        }

        ArrayList<QRItem> list = new ArrayList<>();
        QRItem qrAlipay = new QRItem(R.drawable.ic_alipay, getString(R.string.alipay), A);
        QRItem qrAlipayHK = new QRItem(R.drawable.ic_alipayhk, getString(R.string.alipayhk), AHK);
        QRItem qrAlipayTH = new QRItem(R.drawable.ic_alipayth, getString(R.string.alipayth), ATH);
        QRItem qrBoost = new QRItem(R.drawable.ic_boost, getString(R.string.boost), B);
        QRItem qrGrabPay = new QRItem(R.drawable.ic_grabpay, getString(R.string.grabpay), GP);
        QRItem qrLinePay = new QRItem(R.drawable.ic_linepay, getString(R.string.linepay), LP);
        QRItem qrMasterWallet = new QRItem(R.drawable.ic_masterwallet, getString(R.string.masterwallet), MW);
        QRItem qrUnionPay = new QRItem(R.drawable.ic_unionpay, getString(R.string.unionpay), UP);
        QRItem qrWechat = new QRItem(R.drawable.ic_wechatpay, getString(R.string.wechatpay), W);
        QRItem qrWechatHK = new QRItem(R.drawable.ic_wechatpayhk, getString(R.string.wechatpayhk), WHK);
        QRItem qrGCash = new QRItem(R.drawable.ic_gcash, getString(R.string.gcash),
                GC);

        if (merchant_id.equals("560200335") || merchant_id.equals("560200303") /*|| merchant_id.equals("88143639") || merchant_id.equals("85003895") || merchant_id.equals("76067317")*/) {
//           list.add(qrAlipayTH);
            list.add(qrAlipay);
            list.add(qrAlipayHK);
            list.add(qrBoost);
            list.add(qrGrabPay);
            list.add(qrLinePay);
            list.add(qrMasterWallet);
            list.add(qrUnionPay);
            list.add(qrWechat);
            list.add(qrWechatHK);
        } else if(merchant_id.substring(0, 2).equals("56")) {
        	// China Country
            list.add(qrAlipay);
            list.add(qrAlipayHK);
            list.add(qrUnionPay);
            list.add(qrWechat);
            list.add(qrWechatHK);
        } else if(merchant_id.substring(0, 2).equals("85")) {
        	// Malaysia Country
            list.add(qrAlipay);
            list.add(qrBoost);
            list.add(qrGrabPay);
            list.add(qrUnionPay);
            list.add(qrWechat);
        } else if(merchant_id.substring(0, 2).equals("88")) {
        	// Hong Kong Country
            list.add(qrAlipay);
            list.add(qrAlipayHK);
            list.add(qrUnionPay);
            list.add(qrWechat);
            list.add(qrWechatHK);
        } else if (merchant_id.substring(0, 2).equals("18")) {
            // Philippines Country
            list.add(qrAlipay);
            list.add(qrGCash);
            list.add(qrGrabPay);
            list.add(qrUnionPay);
            list.add(qrWechat);
        } else if (merchant_id.substring(0, 2).equals("76")) {
            // Thailand Country
            list.add(qrAlipay);
            list.add(qrGrabPay);
            list.add(qrUnionPay);
            list.add(qrWechat);
        } else if (merchant_id.substring(0, 2).equals("12")) {
            // Singapore Country
            list.add(qrAlipay);
            list.add(qrGrabPay);
            list.add(qrUnionPay);
            list.add(qrWechat);
        }


        if (list.isEmpty()) {
            showTextToast(getString(R.string.paymethod_not_available));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //--Edited 25/07/18 by KJ--//
        DesEncrypter encrypt;
        try {
            encrypt = new DesEncrypter(merchantName);
            merchantId = encrypt.encrypt(merchant_id);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        String whereargs[] = {merchantId};

        SharedPreferences pref = this.getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);

        final String alipayTimeout = pref.getString(Constants.pref_alipay_timeout, Constants.default_payment_timeout);
        final String alipayHKTimeout = pref.getString(Constants.pref_alipayhk_timeout, Constants.default_payment_timeout);
        final String wechatTimeout = pref.getString(Constants.pref_wechat_timeout, Constants.default_payment_timeout);
        final String wechatHKTimeout = pref.getString(Constants.pref_wechathk_timeout, Constants.default_payment_timeout);
        final String unionpayTimeout = pref.getString(Constants.pref_unionpay_timeout, Constants.default_payment_timeout);
        final String boostTimeout = pref.getString(Constants.pref_boost_timeout, Constants.default_payment_timeout);
        final String grabpayTimeout = pref.getString(Constants.pref_grabpay_timeout, Constants.default_payment_timeout);
        final String gcashTimeout = pref.getString(Constants.pref_gcash_timeout,
                Constants.default_payment_timeout);

        //--Edited 25/07/18 by KJ--//


        recyclerView = (RecyclerView) findViewById(R.id.grid_scan);
        recyclerView.setAdapter(new QRAdapter(list, new QRAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(QRItem item) {

                String payMethod = item.getQrName();

                if(item.isActive()){
                    Intent intent = new Intent();
                    intent.setClass(ScanQRPayment_2.this, CameraCapture.class);
                    Log.d("OTTO", "mdr_less_equal_0:" + mdr_less_equal_0);
                    Log.d("OTTO", "!surC:" + (!surC));

                    if (mdr_less_equal_0 || !surC) {

                        intent.putExtra("amount", txtAmount.getText().toString());
                        intent.putExtra("MerRequestAmt", txtAmount.getText().toString());
                        intent.putExtra("Surcharge", "0");
                        intent.putExtra("Mdr", "0");
                        intent.putExtra("surC", "F");
                    } else {

                        intent.putExtra("amount", txtAmount.getText().toString());
                        intent.putExtra("MerRequestAmt", txtAmount.getText().toString());
                        intent.putExtra("Surcharge", getIntent().getStringExtra(Constants.PRE_SURCHARGE));
                        intent.putExtra("Mdr", getIntent().getStringExtra(Constants.PRE_MDR));
                        intent.putExtra("surC", "T");
                    }

                    if (payMethod.equalsIgnoreCase(getString(R.string.alipay))) {

                        timeout = Integer.parseInt(alipayTimeout);
                        SCANNING_REQUEST_CODE = A_SCANNIN_GREQUEST_CODE;
                        if (A) {
                            intent.putExtra("pMothod", pMothodA);
                        } else if (ACN) {
                            intent.putExtra("pMothod", pMothodACN);
                        }

                    } else if (payMethod.equalsIgnoreCase(getString(R.string.alipayhk))) {

                        timeout = Integer.parseInt(alipayHKTimeout);
                        SCANNING_REQUEST_CODE = AHK_SCANNIN_GREQUEST_CODE;
                        intent.putExtra("pMothod", pMothodAHK);

                    } else if (payMethod.equalsIgnoreCase(getString(R.string.alipayth))) {

//                        timeout = Integer.parseInt(alipayHKTimeout);
                        SCANNING_REQUEST_CODE = ATH_SCANNIN_GREQUEST_CODE;
                        intent.putExtra("pMothod", pMothodATH);

                    } else if (payMethod.equalsIgnoreCase(getString(R.string.boost))) {

                        timeout = Integer.parseInt(boostTimeout);
                        SCANNING_REQUEST_CODE = B_SCANNIN_GREQUEST_CODE;
                        intent.putExtra("pMothod", pMothodB);

                    } else if (payMethod.equalsIgnoreCase(getString(R.string.gcash))) {

                        timeout = Integer.parseInt(gcashTimeout);
                        SCANNING_REQUEST_CODE = GC_SCANNIN_GREQUEST_CODE;
                        intent.putExtra("pMothod", pMothodGC);

                    } else if (payMethod.equalsIgnoreCase(getString(R.string.grabpay))) {

                        timeout = Integer.parseInt(grabpayTimeout);
                        SCANNING_REQUEST_CODE = GRABPAY_SCANNIN_GREQUEST_CODE;
                        intent.putExtra("pMothod", pMothodGrabPay);

                    } else if (payMethod.equalsIgnoreCase(getString(R.string.linepay))) {

//                        timeout = Integer.parseInt(alipayHKTimeout);
                        SCANNING_REQUEST_CODE = LINEPAY_SCANNIN_GREQUEST_CODE;
                        intent.putExtra("pMothod", pMothodLINEPAY);

                    } else if (payMethod.equalsIgnoreCase(getString(R.string.masterwallet))) {

//                        timeout = Integer.parseInt(wechatTimeout);
                        SCANNING_REQUEST_CODE = MasterWallet_SCANNIN_GREQUEST_CODE;
                        intent.putExtra("pMothod", pMothodMaster);

                    } else if(payMethod.equalsIgnoreCase(getString(R.string.unionpay))) {

                        timeout = Integer.parseInt(unionpayTimeout);
                        SCANNING_REQUEST_CODE = UNIONPAY_SCANNIN_GREQUEST_CODE;
                        intent.putExtra("pMothod", pMothodUNIPAY);

                    } else if (payMethod.equalsIgnoreCase(getString(R.string.wechatpay))) {

                        timeout = Integer.parseInt(wechatTimeout);
                        SCANNING_REQUEST_CODE = WX_SCANNIN_GREQUEST_CODE;
                        intent.putExtra("pMothod", pMothodWX);

                    } else if (payMethod.equalsIgnoreCase(getString(R.string.wechatpayhk))) {

                        timeout = Integer.parseInt(wechatHKTimeout);
                        SCANNING_REQUEST_CODE = WXHK_SCANNIN_GREQUEST_CODE;
                        intent.putExtra("pMothod", pMothodWXHK);
                    }

                    intent.putExtra("timeout", timeout);
                    //--done Edited 25/07/18 by KJ--//
                    intent.putExtra("currCode", currCode);
                    intent.putExtra("currCode1", currCode1);
                    intent.putExtra("MerchantRef", MerchantRef.getText().toString());
                    intent.putExtra("hideSurcharge", hideSurcharge);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, SCANNING_REQUEST_CODE);

                } else {
                    showTextToast(getString(R.string.paymethod_not_support));
                }
            }
        }));
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanQRPayment_2.this, DialogCoupon.class);
                intent.putExtra("currCode", currCode);
                intent.putExtra(Constants.CURRENCY, currCode1);
                intent.putExtra(Constants.MERCHANT_REF, MerchantRef.getText().toString());
                intent.putExtra("hideSurcharge", hideSurcharge);
                intent.putExtra("payMethodList", payMethod);
                intent.putExtra(Constants.MERNAME, getIntent().getStringExtra(Constants.MERNAME));
                intent.putExtra("payType", payType);
                intent.putExtra("discount", discount);
                intent.putExtra("paymentMethod", "scanqr");
                intent.putExtra("amount", getIntent().getStringExtra(Constants.PRE_AMOUNT));
                intent.putExtra(Constants.PRE_MDR, getIntent().getStringExtra(Constants.PRE_MDR));
                intent.putExtra(Constants.PRE_SURCHARGE, getIntent().getStringExtra(Constants.PRE_SURCHARGE));
                intent.putExtra(Constants.MERID,merchant_id);

                if(discount != null){
                    intent.putExtra("promoCode", promoCode);
                }

                intent.putExtra("MerRequestAmt", getIntent().getStringExtra(Constants.PRE_AMOUNT));
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

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

    private void showTextToast(String msg) {
        if (numberpadtoast == null) {
            numberpadtoast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            numberpadtoast.setText(msg);
        }
        numberpadtoast.show();
    }

    public String getPrefPayGate() {
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate, Constants.default_paygate);
        return prefpaygate;
    }

    //for scanning function
    //----scan feedback----//
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //-----------------Wechat start-----------------------------
            case WX_SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String auth_code = bundle.getString("auth_code");
                    String amount = bundle.getString("amount");
                    String merRequestAmt = bundle.getString("MerRequestAmt");
                    String Surcharge = bundle.getString("Surcharge");
                    String currCode = bundle.getString("currcode1");
                    String MerchantRef = bundle.getString("MerchantRef");
                    String pMethodWechat = bundle.getString("pMothod");

                    // Show what you get from scanning
                    String feedBackData = "auth_code: " + auth_code + ", amount: " + amount + ", " + "merRequestAmt: " + merRequestAmt + ", Surcharge: " + Surcharge + ", currcode: " + currCode + ", MerchantRef: " + MerchantRef + ", pMothod: " + pMethodWechat;
                    System.out.println("Wechat ----- feedBackData: " + feedBackData);

                    progressDialog_WECHAT = new ProgressDialog(ScanQRPayment_2.this);
                    progressDialog_WECHAT.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                    progressDialog_WECHAT.setCancelable(false);
                    QRPay weChat = new QRPay(ScanQRPayment_2.this, progressDialog_WECHAT, getPrefPayGate(), false);
                    Intent intsubmit = getIntent();
                    String merchantId = intsubmit.getStringExtra(Constants.MERID);
                    String merName = intsubmit.getStringExtra(Constants.MERNAME);
                    //-------------------get userID/operatorId start-------------------//
                    String userID = null;
                    String merchantId1 = intsubmit.getStringExtra(Constants.MERID);
                    String merName1 = intsubmit.getStringExtra(Constants.MERNAME);
                    String orgMerchantId = merchantId1;
                    DesEncrypter encrypt;
                    String encMerchantId = "";
                    DatabaseHelper db = new DatabaseHelper(this);
                    try {
                        encrypt = new DesEncrypter(merName1);
                        encMerchantId = encrypt.encrypt(orgMerchantId);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    String whereargs[] = {encMerchantId};
                    String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
                    db.close();
                    if (dbuserID != null) {
                        try {
                            DesEncrypter encrypter = new DesEncrypter(merName1);
                            userID = encrypter.decrypt(dbuserID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //-------------------get userID/operatorId end-------------------//
                    String useSurcharge = "F";
                    if (surC) {
                        useSurcharge = "T";
                    }
                    weChat.execute(amount, merRequestAmt, Surcharge, currCode, MerchantRef, auth_code, pMethodWechat, merchantId, merName, userID, useSurcharge);
                }
                break;
            //-----------------Wechat end-----------------------------

            //-----------------WechatHK start-----------------------------
            case WXHK_SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String auth_code = bundle.getString("auth_code");
                    String amount = bundle.getString("amount");
                    String merRequestAmt = bundle.getString("MerRequestAmt");
                    String Surcharge = bundle.getString("Surcharge");
                    String currCode = bundle.getString("currcode1");
                    String MerchantRef = bundle.getString("MerchantRef");
                    String pMethodWechatHK = bundle.getString("pMothod");

                    // Show what you get from scanning
                    String feedBackData = "auth_code: " + auth_code + ", amount: " + amount + ", " + "merRequestAmt: " + merRequestAmt + ", Surcharge: " + Surcharge + ", currcode: " + currCode + ", MerchantRef: " + MerchantRef + ", pMothod: " + pMethodWechatHK;
                    System.out.println("WechatHK ----- feedBackData: " + feedBackData);

                    progressDialog_WECHATHK = new ProgressDialog(ScanQRPayment_2.this);
                    progressDialog_WECHATHK.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                    progressDialog_WECHATHK.setCancelable(false);
                    QRPay weChatHK = new QRPay(ScanQRPayment_2.this, progressDialog_WECHATHK, getPrefPayGate(), false);
                    Intent intsubmit = getIntent();
                    String merchantId = intsubmit.getStringExtra(Constants.MERID);
                    String merName = intsubmit.getStringExtra(Constants.MERNAME);
                    //-------------------get userID/operatorId start-------------------//
                    String userID = null;
                    String merchantId1 = intsubmit.getStringExtra(Constants.MERID);
                    String merName1 = intsubmit.getStringExtra(Constants.MERNAME);
                    String orgMerchantId = merchantId1;
                    DesEncrypter encrypt;
                    String encMerchantId = "";
                    DatabaseHelper db = new DatabaseHelper(this);
                    try {
                        encrypt = new DesEncrypter(merName1);
                        encMerchantId = encrypt.encrypt(orgMerchantId);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    String whereargs[] = {encMerchantId};
                    String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
                    db.close();
                    if (dbuserID != null) {
                        try {
                            DesEncrypter encrypter = new DesEncrypter(merName1);
                            userID = encrypter.decrypt(dbuserID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //-------------------get userID/operatorId end-------------------//
                    String useSurcharge = "F";
                    if (surC) {
                        useSurcharge = "T";
                    }
                    weChatHK.execute(amount, merRequestAmt, Surcharge, currCode, MerchantRef, auth_code, pMethodWechatHK, merchantId, merName, userID, useSurcharge);
                } else {
                    if (!CameraCapture.errorMessage.equals("1")) {
                        Toast.makeText(ScanQRPayment_2.this, R.string.trx_timeout, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            //-----------------WechatHK end-----------------------------

            //-----------------Alipay start-----------------------------
            case A_SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String auth_code = bundle.getString("auth_code");
                    String amount = bundle.getString("amount");
                    String merRequestAmt = bundle.getString("MerRequestAmt");
                    String Surcharge = bundle.getString("Surcharge");
                    String currCode = bundle.getString("currcode1");
                    String MerchantRef = bundle.getString("MerchantRef");
                    String pMethodAlipay = bundle.getString("pMothod");

                    // Show what you get from scanning
                    String feedBackData = "auth_code: " + auth_code + ", amount: " + amount + ", " + "merRequestAmt: " + merRequestAmt + ", Surcharge: " + Surcharge + ", currcode: " + currCode + ", MerchantRef: " + MerchantRef + ", pMothod: " + pMethodAlipay;
                    System.out.println("Alipay ----- feedBackData: " + feedBackData);

                    progressDialog_ALIPAY = new ProgressDialog(ScanQRPayment_2.this);
                    progressDialog_ALIPAY.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                    progressDialog_ALIPAY.setCancelable(false);

                    QRPay alipay = new QRPay(ScanQRPayment_2.this, progressDialog_ALIPAY, getPrefPayGate(), false);
                    Intent intsubmit = getIntent();
                    String merchantId = intsubmit.getStringExtra(Constants.MERID);
                    String merName = intsubmit.getStringExtra(Constants.MERNAME);
                    //-------------------get userID/operatorId start-------------------//
                    String userID = null;
                    String merchantId1 = intsubmit.getStringExtra(Constants.MERID);
                    String merName1 = intsubmit.getStringExtra(Constants.MERNAME);
                    String orgMerchantId = merchantId1;
                    DesEncrypter encrypt;
                    String encMerchantId = "";
                    DatabaseHelper db = new DatabaseHelper(this);
                    try {
                        encrypt = new DesEncrypter(merName1);
                        encMerchantId = encrypt.encrypt(orgMerchantId);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    String whereargs[] = {encMerchantId};
                    String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
                    //                    String deviceSerial = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_DEVICE_SERIALNO, Constants.DB_MERCHANT_ID, whereargs);
                    //                    String softwareVersion = Constants.current_VersionCode;

                    db.close();
                    if (dbuserID != null) {
                        try {
                            DesEncrypter encrypter = new DesEncrypter(merName1);
                            userID = encrypter.decrypt(dbuserID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //-------------------get userID/operatorId end-------------------//
                    String useSurcharge = "F";
                    if (surC) {
                        useSurcharge = "T";
                    }
                    alipay.execute(amount,
                            merRequestAmt,
                            Surcharge,
                            currCode,
                            MerchantRef,
                            auth_code,
                            pMethodAlipay,
                            merchantId,
                            merName,
                            userID,
                            useSurcharge);
                } else {
                    if (!CameraCapture.errorMessage.equals("1")) {
                        Toast.makeText(ScanQRPayment_2.this, R.string.trx_timeout, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            //-----------------Alipay end-----------------------------

            //-----------------AlipayHK start-----------------------------
            case AHK_SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String auth_code = bundle.getString("auth_code");
                    String amount = bundle.getString("amount");
                    String merRequestAmt = bundle.getString("MerRequestAmt");
                    String Surcharge = bundle.getString("Surcharge");
                    String currCode = bundle.getString("currcode1");
                    String MerchantRef = bundle.getString("MerchantRef");
                    String pMethodAlipayHK = bundle.getString("pMothod");

                    // Show what you get from scanning
                    String feedBackData = "auth_code: " + auth_code + ", amount: " + amount + ", " + "merRequestAmt: " + merRequestAmt + ", Surcharge: " + Surcharge + ", currcode: " + currCode + ", MerchantRef: " + MerchantRef + ", pMothod: " + pMethodAlipayHK;
                    System.out.println("AlipayHK ----- feedBackData: " + feedBackData);

                    progressDialog_ALIPAYHK = new ProgressDialog(ScanQRPayment_2.this);
                    progressDialog_ALIPAYHK.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                    progressDialog_ALIPAYHK.setCancelable(false);
                    QRPay alipayHK = new QRPay(ScanQRPayment_2.this, progressDialog_ALIPAYHK, getPrefPayGate(), false);
                    Intent intsubmit = getIntent();
                    String merchantId = intsubmit.getStringExtra(Constants.MERID);
                    String merName = intsubmit.getStringExtra(Constants.MERNAME);
                    //-------------------get userID/operatorId start-------------------//
                    String userID = null;
                    String merchantId1 = intsubmit.getStringExtra(Constants.MERID);
                    String merName1 = intsubmit.getStringExtra(Constants.MERNAME);
                    String orgMerchantId = merchantId1;
                    DesEncrypter encrypt;
                    String encMerchantId = "";
                    DatabaseHelper db = new DatabaseHelper(this);
                    try {
                        encrypt = new DesEncrypter(merName1);
                        encMerchantId = encrypt.encrypt(orgMerchantId);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    String whereargs[] = {encMerchantId};
                    String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
                    db.close();
                    if (dbuserID != null) {
                        try {
                            DesEncrypter encrypter = new DesEncrypter(merName1);
                            userID = encrypter.decrypt(dbuserID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //-------------------get userID/operatorId end-------------------//
                    String useSurcharge = "F";
                    if (surC) {
                        useSurcharge = "T";
                    }

                    alipayHK.execute(amount, merRequestAmt, Surcharge, currCode, MerchantRef, auth_code, pMethodAlipayHK, merchantId, merName, userID, useSurcharge);
                } else {
                    if (!CameraCapture.errorMessage.equals("1")) {
                        if (!CameraCapture.errorMessage.equals("1")) {
                            Toast.makeText(ScanQRPayment_2.this, R.string.trx_timeout, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            //-----------------AlipayHK end-----------------------------

            //-----------------AlipayCN start-----------------------------
            case ACN_SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String auth_code = bundle.getString("auth_code");
                    String amount = bundle.getString("amount");
                    String merRequestAmt = bundle.getString("MerRequestAmt");
                    String Surcharge = bundle.getString("Surcharge");
                    String currCode = bundle.getString("currcode1");
                    String MerchantRef = bundle.getString("MerchantRef");
                    String pMethodAlipayCN = bundle.getString("pMothod");

                    // Show what you get from scanning
                    String feedBackData = "auth_code: " + auth_code + ", amount: " + amount + ", " + "merRequestAmt: " + merRequestAmt + ", Surcharge: " + Surcharge + ", currcode: " + currCode + ", MerchantRef: " + MerchantRef + ", pMothod: " + pMethodAlipayCN;
                    System.out.println("AlipayCN ----- feedBackData: " + feedBackData);

                    progressDialog_ALIPAYCN = new ProgressDialog(ScanQRPayment_2.this);
                    progressDialog_ALIPAYCN.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                    progressDialog_ALIPAYCN.setCancelable(false);
                    QRPay alipay = new QRPay(ScanQRPayment_2.this, progressDialog_ALIPAYCN, getPrefPayGate(), false);
                    Intent intsubmit = getIntent();
                    String merchantId = intsubmit.getStringExtra(Constants.MERID);
                    String merName = intsubmit.getStringExtra(Constants.MERNAME);
                    //-------------------get userID/operatorId start-------------------//
                    String userID = null;
                    String merchantId1 = intsubmit.getStringExtra(Constants.MERID);
                    String merName1 = intsubmit.getStringExtra(Constants.MERNAME);
                    String orgMerchantId = merchantId1;
                    DesEncrypter encrypt;
                    String encMerchantId = "";
                    DatabaseHelper db = new DatabaseHelper(this);
                    try {
                        encrypt = new DesEncrypter(merName1);
                        encMerchantId = encrypt.encrypt(orgMerchantId);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    String whereargs[] = {encMerchantId};
                    String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
                    db.close();
                    if (dbuserID != null) {
                        try {
                            DesEncrypter encrypter = new DesEncrypter(merName1);
                            userID = encrypter.decrypt(dbuserID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //-------------------get userID/operatorId end-------------------//
                    String useSurcharge = "F";
                    if (surC) {
                        useSurcharge = "T";
                    }
                    alipay.execute(amount, merRequestAmt, Surcharge, currCode, MerchantRef, auth_code, pMethodAlipayCN, merchantId, merName, userID, useSurcharge);
                } else {
                    if (!CameraCapture.errorMessage.equals("1")) {
                        if (!CameraCapture.errorMessage.equals("1")) {
                            Toast.makeText(ScanQRPayment_2.this, R.string.trx_timeout, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            //-----------------AlipayCN end-----------------------------

            //-----------------UnionPay start-----------------------------
            case UNIONPAY_SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String auth_code = bundle.getString("auth_code");
                    String amount = bundle.getString("amount");
                    String merRequestAmt = bundle.getString("MerRequestAmt");
                    String Surcharge = bundle.getString("Surcharge");
                    String currCode = bundle.getString("currcode1");
                    String MerchantRef = bundle.getString("MerchantRef");
                    String pMethodUnionPay = bundle.getString("pMothod");

                    // Show what you get from scanning
                    String feedBackData = "auth_code: " + auth_code + ", amount: " + amount + ", " + "merRequestAmt: " + merRequestAmt + ", Surcharge: " + Surcharge + ", currcode: " + currCode + ", MerchantRef: " + MerchantRef + ", pMothod: " + pMethodUnionPay;
                    System.out.println("UnionPay ----- feedBackData: " + feedBackData);

                    progressDialog_UnionPay = new ProgressDialog(ScanQRPayment_2.this);
                    progressDialog_UnionPay.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                    progressDialog_UnionPay.setCancelable(false);

                    QRPay unionpay = new QRPay(ScanQRPayment_2.this, progressDialog_UnionPay,
                            getPrefPayGate(), false);
                    Intent intsubmit = getIntent();
                    String merchantId = intsubmit.getStringExtra(Constants.MERID);
                    String merName = intsubmit.getStringExtra(Constants.MERNAME);
                    //-------------------get userID/operatorId start-------------------//
                    String userID = null;
                    String merchantId1 = intsubmit.getStringExtra(Constants.MERID);
                    String merName1 = intsubmit.getStringExtra(Constants.MERNAME);
                    String orgMerchantId = merchantId1;
                    DesEncrypter encrypt;
                    String encMerchantId = "";
                    DatabaseHelper db = new DatabaseHelper(this);
                    try {
                        encrypt = new DesEncrypter(merName1);
                        encMerchantId = encrypt.encrypt(orgMerchantId);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    String whereargs[] = {encMerchantId};
                    String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
                    //String deviceSerial = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_DEVICE_SERIALNO, Constants.DB_MERCHANT_ID, whereargs);
                    //String softwareVersion = Constants.current_VersionCode;

                    db.close();
                    if (dbuserID != null) {
                        try {
                            DesEncrypter encrypter = new DesEncrypter(merName1);
                            userID = encrypter.decrypt(dbuserID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //-------------------get userID/operatorId end-------------------//
                    String useSurcharge = "F";
                    if (surC) {
                        useSurcharge = "T";
                    }
                    unionpay.execute(amount, merRequestAmt, Surcharge, currCode, MerchantRef, auth_code, pMethodUnionPay, merchantId, merName, userID, useSurcharge);
                } else {
                    if (!CameraCapture.errorMessage.equals("1")) {
                        Toast.makeText(ScanQRPayment_2.this, R.string.trx_timeout, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            //-----------------UnionPay end-----------------------------

            //-----------------Grabpay start-----------------------------
            case GRABPAY_SCANNIN_GREQUEST_CODE:

                if (resultCode == RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    String auth_code = bundle.getString("auth_code");
                    String amount = bundle.getString("amount");
                    String merRequestAmt = bundle.getString("MerRequestAmt");
                    String Surcharge = bundle.getString("Surcharge");
                    String currCode = bundle.getString("currcode1");
                    String MerchantRef = bundle.getString("MerchantRef");
                    String pMethodGrabPay = bundle.getString("pMothod");

                    // Show what you get from scanning
                    String feedBackData = "auth_code: " + auth_code + ", amount: " + amount + ", " + "merRequestAmt: " + merRequestAmt + ", Surcharge: " + Surcharge + ", currcode: " + currCode + ", MerchantRef: " + MerchantRef + ", pMothod: " + pMethodGrabPay;
                    System.out.println("GrabPay ----- feedBackData: " + feedBackData);

                    progressDialog_GrabPay = new ProgressDialog(ScanQRPayment_2.this);
                    progressDialog_GrabPay.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                    progressDialog_GrabPay.setCancelable(false);
                    QRPay grabpay = new QRPay(ScanQRPayment_2.this, progressDialog_GrabPay, getPrefPayGate(), false);
                    Intent intsubmit = getIntent();
                    String merchantId = intsubmit.getStringExtra(Constants.MERID);
                    String merName = intsubmit.getStringExtra(Constants.MERNAME);
                    //-------------------get userID/operatorId start-------------------//
                    String userID = null;
                    String merchantId1 = intsubmit.getStringExtra(Constants.MERID);
                    String merName1 = intsubmit.getStringExtra(Constants.MERNAME);
                    String orgMerchantId = merchantId1;
                    DesEncrypter encrypt;
                    String encMerchantId = "";
                    DatabaseHelper db = new DatabaseHelper(this);
                    try {
                        encrypt = new DesEncrypter(merName1);
                        encMerchantId = encrypt.encrypt(orgMerchantId);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    String whereargs[] = {encMerchantId};
                    String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
                    db.close();
                    if (dbuserID != null) {
                        try {
                            DesEncrypter encrypter = new DesEncrypter(merName1);
                            userID = encrypter.decrypt(dbuserID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //-------------------get userID/operatorId end-------------------//
                    String useSurcharge = "F";
                    if (surC) {
                        useSurcharge = "T";
                    }
                    grabpay.execute(amount, merRequestAmt, Surcharge, currCode, MerchantRef, auth_code, pMethodGrabPay, merchantId, merName, userID, useSurcharge);
                } else {
                    if (!CameraCapture.errorMessage.equals("1")) {
                        Toast.makeText(ScanQRPayment_2.this, R.string.trx_timeout, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            //-----------------GrabPay end-----------------------------

            //-----------------LinePay start-----------------------------
            case LINEPAY_SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String auth_code = bundle.getString("auth_code");
                    String amount = bundle.getString("amount");
                    String merRequestAmt = bundle.getString("MerRequestAmt");
                    String Surcharge = bundle.getString("Surcharge");
                    String currCode = bundle.getString("currcode1");
                    String MerchantRef = bundle.getString("MerchantRef");
                    String pMethodLinePay = bundle.getString("pMothod");
                    //show what u get from scaning
                    String feedbackdate = "auth_code:" +
                            auth_code + " , amount:" +
                            amount + " ,merRequestAmt" +
                            merRequestAmt + " , Surcharge:" +
                            Surcharge + " , currcode:" +
                            currCode + " , MerchantRef:" +
                            MerchantRef + " , pMothod:" +
                            pMethodLinePay;
                    System.out.println("OTTO-----feedbackdate:" + feedbackdate);

                    progressDialog_UnionPay = new ProgressDialog(ScanQRPayment_2.this);
                    progressDialog_UnionPay.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                    progressDialog_UnionPay.setCancelable(false);

                    String useSurcharge = "F";
                    if (surC) {
                        useSurcharge = "T";
                    }

                    if (merchant_id.equals("560200335") || merchant_id.equals("560200303")) {
                        if (merchant_id.equals("560200335")) {
                            scanback("0", "12345678", "12345678", amount,
                                    "0",
                                    "0",
                                    "HKD",
                                    "2018-09-14 16:25:21.0",
                                    "Transaction completed",
                                    "560200335",
                                    "FPS Demo",
                                    "N",
                                    "LINEPAY",
                                    "admin",
                                    "288298592317829807");
                        } else {
                            scanfail("1", "560200303", "APCNGZ", "N",
                                    "LINEPAY",
                                    "admin",
                                    "281297055316129753",
                                    "12345678",
                                    "12345678",
                                    amount,
                                    amount,
                                    "0",
                                    "RMB",
                                    "",
                                    "Your account doesn't support the payment method",
                                    auth_code);
                        }
                    }
                } else {
                    if (!CameraCapture.errorMessage.equals("1")) {
                        Toast.makeText(ScanQRPayment_2.this, R.string.trx_timeout, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            //-----------------LinePay end-----------------------------

            //-----------------AlipayTH start-----------------------------
            case ATH_SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String auth_code = bundle.getString("auth_code");
                    String amount = bundle.getString("amount");
                    String merRequestAmt = bundle.getString("MerRequestAmt");
                    String Surcharge = bundle.getString("Surcharge");
                    String currCode = bundle.getString("currcode1");
                    String MerchantRef = bundle.getString("MerchantRef");
                    String pMethodAlipayTH = bundle.getString("pMothod");

                    // Show what you get from scanning
                    String feedBackData = "auth_code: " + auth_code + ", amount: " + amount + ", " + "merRequestAmt: " + merRequestAmt + ", Surcharge: " + Surcharge + ", currcode: " + currCode + ", MerchantRef: " + MerchantRef + ", pMothod: " + pMethodAlipayTH;
                    System.out.println("AlipayTH ----- feedBackData: " + feedBackData);

                    progressDialog_ALIPAYTH = new ProgressDialog(ScanQRPayment_2.this);
                    progressDialog_ALIPAYTH.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                    progressDialog_ALIPAYTH.setCancelable(false);
                    QRPay alipayTH = new QRPay(ScanQRPayment_2.this, progressDialog_ALIPAYTH, getPrefPayGate(), false);
                    Intent intsubmit = getIntent();
                    String merchantId = intsubmit.getStringExtra(Constants.MERID);
                    String merName = intsubmit.getStringExtra(Constants.MERNAME);
                    //-------------------get userID/operatorId start-------------------//
                    String userID = null;
                    String merchantId1 = intsubmit.getStringExtra(Constants.MERID);
                    String merName1 = intsubmit.getStringExtra(Constants.MERNAME);
                    String orgMerchantId = merchantId1;
                    DesEncrypter encrypt;
                    String encMerchantId = "";
                    DatabaseHelper db = new DatabaseHelper(this);
                    try {
                        encrypt = new DesEncrypter(merName1);
                        encMerchantId = encrypt.encrypt(orgMerchantId);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    String whereargs[] = {encMerchantId};
                    String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);

                    String deviceSerial = Build.SERIAL;

                    db.close();
                    if (dbuserID != null) {
                        try {
                            DesEncrypter encrypter = new DesEncrypter(merName1);
                            userID = encrypter.decrypt(dbuserID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //-------------------get userID/operatorId end-------------------//
                    String useSurcharge = "F";
                    if (surC) {
                        useSurcharge = "T";
                    }
                    alipayTH.execute(amount, merRequestAmt, Surcharge, currCode, MerchantRef, auth_code, pMethodAlipayTH, merchantId, merName, userID, useSurcharge, autoBBLQRTraceNo(), autoBBLQRBatchID(), deviceSerial);
                } else {
                    if (!CameraCapture.errorMessage.equals("1")) {
                        if (!CameraCapture.errorMessage.equals("1")) {
                            Toast.makeText(ScanQRPayment_2.this, R.string.trx_timeout, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            //-----------------AlipayTH end-----------------------------

            //-----------------Boost start-----------------------------
            case B_SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String auth_code = bundle.getString("auth_code");
                    String amount = bundle.getString("amount");
                    String merRequestAmt = bundle.getString("MerRequestAmt");
                    String Surcharge = bundle.getString("Surcharge");
                    String currCode = bundle.getString("currcode1");
                    String MerchantRef = bundle.getString("MerchantRef");
                    String pMethodBoost = bundle.getString("pMothod");

                    // Show what you get from scanning
                    String feedBackData = "auth_code: " + auth_code + ", amount: " + amount + ", " + "merRequestAmt: " + merRequestAmt + ", Surcharge: " + Surcharge + ", currcode: " + currCode + ", MerchantRef: " + MerchantRef + ", pMothod: " + pMethodBoost;
                    System.out.println("Boost ----- feedBackData: " + feedBackData);

                    progressDialog_Boost = new ProgressDialog(ScanQRPayment_2.this);
                    progressDialog_Boost.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                    progressDialog_Boost.setCancelable(false);
                    QRPay boostPay = new QRPay(ScanQRPayment_2.this, progressDialog_Boost, getPrefPayGate(), false);
                    Intent intsubmit = getIntent();
                    String merchantId = intsubmit.getStringExtra(Constants.MERID);
                    String merName = intsubmit.getStringExtra(Constants.MERNAME);
                    //-------------------get userID/operatorId start-------------------//
                    String userID = null;
                    String merchantId1 = intsubmit.getStringExtra(Constants.MERID);
                    String merName1 = intsubmit.getStringExtra(Constants.MERNAME);
                    String orgMerchantId = merchantId1;
                    DesEncrypter encrypt;
                    String encMerchantId = "";
                    DatabaseHelper db = new DatabaseHelper(this);
                    try {
                        encrypt = new DesEncrypter(merName1);
                        encMerchantId = encrypt.encrypt(orgMerchantId);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    String whereargs[] = {encMerchantId};
                    String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
                    db.close();
                    if (dbuserID != null) {
                        try {
                            DesEncrypter encrypter = new DesEncrypter(merName1);
                            userID = encrypter.decrypt(dbuserID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //-------------------get userID/operatorId end-------------------//
                    String useSurcharge = "F";
                    if (surC) {
                        useSurcharge = "T";
                    }
                    boostPay.execute(amount, merRequestAmt, Surcharge, currCode, MerchantRef, auth_code, pMethodBoost, merchantId, merName, userID, useSurcharge);
                } else {
                    if (!CameraCapture.errorMessage.equals("1")) {
                        Toast.makeText(ScanQRPayment_2.this, R.string.trx_timeout, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            //-----------------Boost end-----------------------------

            //-----------------GCash start-----------------------------
            case GC_SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String auth_code = bundle.getString("auth_code");
                    String amount = bundle.getString("amount");
                    String merRequestAmt = bundle.getString("MerRequestAmt");
                    String Surcharge = bundle.getString("Surcharge");
                    String currCode = bundle.getString("currcode1");
                    String MerchantRef = bundle.getString("MerchantRef");
                    String pMethodGCash = bundle.getString("pMothod");

                    // Show what you get from scanning
                    String feedBackData = "auth_code: " + auth_code + ", amount: " + amount + ", " + "merRequestAmt: " + merRequestAmt + ", Surcharge: " + Surcharge + ", currcode: " + currCode + ", MerchantRef: " + MerchantRef + ", pMothod: " + pMethodGCash;
                    System.out.println("GCash ----- feedBackData: " + feedBackData);

                    progressDialog_GCash = new ProgressDialog(ScanQRPayment_2.this);
                    progressDialog_GCash.setMessage(getString(R.string.please_wait) + "\n\n" + getString(R.string.reminder));
                    progressDialog_GCash.setCancelable(false);
                    QRPay GCashPay = new QRPay(ScanQRPayment_2.this, progressDialog_GCash,
                            getPrefPayGate(), false);
                    Intent intsubmit = getIntent();
                    String merchantId = intsubmit.getStringExtra(Constants.MERID);
                    String merName = intsubmit.getStringExtra(Constants.MERNAME);
                    //-------------------get userID/operatorId start-------------------//
                    String userID = null;
                    String merchantId1 = intsubmit.getStringExtra(Constants.MERID);
                    String merName1 = intsubmit.getStringExtra(Constants.MERNAME);
                    String orgMerchantId = merchantId1;
                    DesEncrypter encrypt;
                    String encMerchantId = "";
                    DatabaseHelper db = new DatabaseHelper(this);
                    try {
                        encrypt = new DesEncrypter(merName1);
                        encMerchantId = encrypt.encrypt(orgMerchantId);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    String whereargs[] = {encMerchantId};
                    String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
                    db.close();
                    if (dbuserID != null) {
                        try {
                            DesEncrypter encrypter = new DesEncrypter(merName1);
                            userID = encrypter.decrypt(dbuserID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //-------------------get userID/operatorId end-------------------//
                    String useSurcharge = "F";
                    if (surC) {
                        useSurcharge = "T";
                    }
                    GCashPay.execute(amount, merRequestAmt, Surcharge, currCode, MerchantRef, auth_code, pMethodGCash, merchantId, merName, userID, useSurcharge);
                } else {
                    if (!CameraCapture.errorMessage.equals("1")) {
                        Toast.makeText(ScanQRPayment_2.this, R.string.trx_timeout, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            //-----------------GCash end-----------------------------

        }

    }
    //----scan feedback----//

    public void scanback(String successcode,
                         String merchant_ref_no,
                         String PayRef,
                         String amount,
                         String merRequestAmt,
                         String Surcharge,
                         String Currcode,
                         String Time,
                         String errcode,
                         String merchantid,
                         String mername,
                         String payType,
                         String pMethod,
                         String operatorId,
                         String cardNo) {

        if (/*pMethod.equals("UNIONPAY") || */pMethod.equalsIgnoreCase("GRABPAY") || pMethod.equalsIgnoreCase("LINEPAY")) {
            Intent intent = new Intent(ScanQRPayment_2.this, PresentQR_result.class);
            intent.putExtra("successCode", successcode);
            intent.putExtra("payMethod", pMethod);
            intent.putExtra("merName", mername);
            intent.putExtra("merRequestAmt", amount);
            intent.putExtra("merchantRef", merchant_ref_no);

            startActivity(intent);
        } else {
            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
            toneGen1.startTone(ToneGenerator.TONE_PROP_PROMPT, 300);

            incMerRef();
            Toast.makeText(ScanQRPayment_2.this, R.string.scan_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();

            if (!"".equals(amount)) {
                amount = String.valueOf(Double.valueOf(amount));
            }
            if (!"".equals(Surcharge)) {
                Surcharge = String.valueOf(Double.valueOf(Surcharge));
            }
            if (!"".equals(merRequestAmt)) {
                merRequestAmt = String.valueOf(Double.valueOf(merRequestAmt));
            }

            Log.d("OTTO", "scanback amount:" + amount);
            Log.d("OTTO", "scanback surcharge:" + Surcharge);
            Log.d("OTTO", "scanback merRequestAmt:" + merRequestAmt);
            intent.putExtra(Constants.SUCCESS_CODE, successcode);
            intent.putExtra(Constants.MERCHANT_REF, merchant_ref_no);
            intent.putExtra(Constants.PAYREF, PayRef);
            intent.putExtra(Constants.AMOUNT, amount);
            intent.putExtra(Constants.MERREQUESTAMT, merRequestAmt);
            intent.putExtra(Constants.SURCHARGE, Surcharge);
            intent.putExtra(Constants.CURRCODE, Currcode);
            intent.putExtra(Constants.TXTIME, Time);
            intent.putExtra(Constants.ERRMSG, errcode);
            intent.putExtra(Constants.MERID, merchantid);
            intent.putExtra(Constants.MERNAME, mername);
            intent.putExtra(Constants.PAYTYPE, payType);
            intent.putExtra(Constants.PAYMETHOD, pMethod);
            intent.putExtra(Constants.OPERATORID, operatorId);
            intent.putExtra(Constants.CARDNO, cardNo);
            intent.putExtra(Constants.pref_hideSurcharge, hideSurcharge);
            System.out.println("OTTO-----" + "Payment Tab success->   successcod:" + successcode + ",Ref:" + merchant_ref_no + ",PayRef:" + PayRef + ",amount:" + amount + ",merRequestAmt:" + merRequestAmt + ",surcharge:" + Surcharge + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode + ",merchantid:" + merchantid + ",mername:" + mername + ",payType:" + payType + ",pMethod:" + pMethod + ",operatorId:" + operatorId + ",cardNo:" + cardNo);
            intent.setClass(ScanQRPayment_2.this, ScanQR_Success.class);

            setResult(RESULT_OK);

            startActivity(intent);
            finish();
        }
    }

    public void scanbackBBL(
            String successcode,
            String merchant_ref_no,
            String PayRef,
            String amount,
            String merRequestAmt,
            String Surcharge,
            String Currcode,
            String Time,
            String errcode,
            String merchantid,
            String mername,
            String payType,
            String pMethod,
            String operatorId,
            String cardNo,
            String printText,
            String payBankId,
            String batchNo,
            String sysTraceNo,
            String host,
            String tid,
            String mid,
            String partnerTrxId,
            String approvalCode) {

        if (pMethod.equals("UNIONPAY") || pMethod.equalsIgnoreCase("GRABPAY") || pMethod.equalsIgnoreCase("LINEPAY")) {
            Intent intent = new Intent(ScanQRPayment_2.this, PresentQR_result.class);
            intent.putExtra("successCode", successcode);
            intent.putExtra("payMethod", pMethod);
            intent.putExtra("merName", mername);
            intent.putExtra("merRequestAmt", amount);
            intent.putExtra("merchantRef", merchant_ref_no);

            startActivity(intent);
        } else {
            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
            toneGen1.startTone(ToneGenerator.TONE_PROP_PROMPT, 300);

            incMerRef();
            Toast.makeText(ScanQRPayment_2.this, R.string.scan_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();

            if (!"".equals(amount)) {
                amount = String.valueOf(Double.valueOf(amount));
            }
            if (!"".equals(Surcharge)) {
                Surcharge = String.valueOf(Double.valueOf(Surcharge));
            }
            if (!"".equals(merRequestAmt)) {
                merRequestAmt = String.valueOf(Double.valueOf(merRequestAmt));
            }

            Log.d("OTTO", "scanback amount:" + amount);
            Log.d("OTTO", "scanback surcharge:" + Surcharge);
            Log.d("OTTO", "scanback merRequestAmt:" + merRequestAmt);
            intent.putExtra(Constants.SUCCESS_CODE, successcode);
            intent.putExtra(Constants.MERCHANT_REF, merchant_ref_no);
            intent.putExtra(Constants.PAYREF, PayRef);
            intent.putExtra(Constants.AMOUNT, amount);
            intent.putExtra(Constants.MERREQUESTAMT, merRequestAmt);
            intent.putExtra(Constants.SURCHARGE, Surcharge);
            intent.putExtra(Constants.CURRCODE, Currcode);
            intent.putExtra(Constants.TXTIME, Time);
            intent.putExtra(Constants.ERRMSG, errcode);
            intent.putExtra(Constants.MERID, merchantid);
            intent.putExtra(Constants.MERNAME, mername);
            intent.putExtra(Constants.PAYTYPE, payType);
            intent.putExtra(Constants.PAYMETHOD, pMethod);
            intent.putExtra(Constants.OPERATORID, operatorId);
            intent.putExtra(Constants.CARDNO, cardNo);
            intent.putExtra(Constants.pref_hideSurcharge, hideSurcharge);
            intent.putExtra("printText", printText);
            intent.putExtra("payBankId", payBankId);
            intent.putExtra("batchNo", batchNo);
            intent.putExtra("host", host);
            intent.putExtra("sysTraceNo", sysTraceNo);
            intent.putExtra("MId", mid);
            intent.putExtra("TId", tid);
            intent.putExtra("partnerTrxId", partnerTrxId);
            intent.putExtra("approvalCode", approvalCode);

            System.out.println("OTTO-----" + "Payment Tab success->   successcod:" + successcode + ",Ref:" + merchant_ref_no + ",PayRef:" + PayRef + ",amount:" + amount + ",merRequestAmt:" + merRequestAmt + ",surcharge:" + Surcharge + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode + ",merchantid:" + merchantid + ",mername:" + mername + ",payType:" + payType + ",pMethod:" + pMethod + ",operatorId:" + operatorId + ",cardNo:" + cardNo);
            intent.setClass(ScanQRPayment_2.this, ScanQR_Success.class);

            setResult(RESULT_OK);

            startActivity(intent);
            finish();
        }
    }

    public void scanfail(String successcode,
                         String merchantid,
                         String mername,
                         String paytype,
                         String pMethod,
                         String operatorid,
                         String cardno,
                         String merchant_ref_no,
                         String PayRef,
                         String amount,
                         String merRequestAmt,
                         String Surcharge,
                         String Currcode,
                         String Time,
                         String errcode,
                         String authCode) {

        if (amount != null) {
            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
            amount = formatter.format(Double.parseDouble(amount));
        }

        if (pMethod.equals("UNIONPAY") || pMethod.equalsIgnoreCase("GRABPAY") || pMethod.equalsIgnoreCase("LINEPAY")) {
            Intent intent = new Intent(ScanQRPayment_2.this, PresentQR_failResult.class);
            intent.putExtra("successCode", successcode);
            intent.putExtra("payMethod", pMethod);
            intent.putExtra("merName", mername);
            intent.putExtra("merRequestAmt", amount);
            intent.putExtra("merchantRef", merchant_ref_no);

            startActivity(intent);
        } else {
            incMerRef();

            Intent intent = new Intent();

            intent.putExtra(Constants.MERID, merchantid);
            intent.putExtra(Constants.MERNAME, mername);
            intent.putExtra(Constants.PAYTYPE, paytype);
            intent.putExtra(Constants.PAYMETHOD, pMethod);
            intent.putExtra(Constants.OPERATORID, operatorid);
            intent.putExtra(Constants.CARDNO, cardno);
            intent.putExtra(Constants.SUCCESS_CODE, successcode);
            intent.putExtra(Constants.MERCHANT_REF, merchant_ref_no);
            intent.putExtra(Constants.PAYREF, PayRef);
            intent.putExtra(Constants.AMOUNT, amount);
            intent.putExtra(Constants.MERREQUESTAMT, merRequestAmt);
            intent.putExtra(Constants.SURCHARGE, Surcharge);
            intent.putExtra(Constants.CURRCODE, Currcode);
            intent.putExtra(Constants.TXTIME, Time);
            intent.putExtra(Constants.ERRMSG, errcode);
            intent.putExtra(Constants.pref_hideSurcharge, hideSurcharge);
            intent.putExtra(Constants.ERRMSG, errcode);

            System.out.println("OTTO-----" + "Payment Tab fail->   successcod:" + successcode + ",merchantid:" + merchantid + ",mername:" + mername + ",paytype:" + paytype + ",pMethod:" + pMethod + ",operatorid:" + operatorid + ",cardno:" + cardno + ",Ref:" + merchant_ref_no + ",PayRef:" + PayRef + ",amount:" + amount + ",merRequestAmt:" + merRequestAmt + ",surcharge:" + Surcharge + ",Currcode" + Currcode + ",TxTime:" + Time + ",errcode:" + errcode);
            intent.setClass(ScanQRPayment_2.this, ScanQR_Failure.class);

            setResult(RESULT_OK);

            startActivity(intent);
            finish();
        }
    }


    public void incMerRef() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("refCounter", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("refCounter", curCounter += 1);
        edit.commit();
    }

    private String autoBBLQRBatchID() {

        SharedPreferences pref = getSharedPreferences(Constants.BATCH_COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("batchCounter", 0);

        int runningNum = curCounter + 1;
        int length = String.valueOf(curCounter).length();
        String digit = "000000";
        String batchID = digit.substring(0, digit.length() - length) + runningNum;

        System.out.println("BBL Batch No: "+batchID);

        return batchID;
    }

    private String autoBBLQRTraceNo() {

        SharedPreferences pref = getSharedPreferences(Constants.BBLQR_TRACENO_COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("BBLTraceCounter", 0);

        int runningNum = curCounter + 1;
        int length = String.valueOf(curCounter).length();
        String digit = "000000";
        String batchID = digit.substring(0, digit.length() - length) + runningNum;

        return batchID;
    }
}
