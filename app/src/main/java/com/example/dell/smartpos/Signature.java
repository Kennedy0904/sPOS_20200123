package com.example.dell.smartpos;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Signature Activity. Captures Signature on canvas.
 * Uses Zip4j library for storing signature img in a password-protected zip file.
 * Uploads zip file with retry count=3
 */
public class Signature extends AppCompatActivity {
    LinearLayout mContent;
    String partnerlogo = "";
    signature mSignature;
    Button mClear, mGetSign, mCancel;
    public static String tempDir;
    public int count = 1;
    public String current = null;
    public String prefPaygate;
    String filename;
    String fileN;
    String uploadFileName;
    String merName="";
    int retryCount=0;
    String MasMerRef;
    String password;
    String fname;
    String base64;
    private Bitmap mBitmap;
    View mView;
    File mypath;
    TextView showamount;
    TextView showamount2;
    String currName;
    String currCode;
    private TextView showcurr;
    Context context;
    ContextWrapper cw;
    private ProgressBar loading;

    //---------for autologout---------//
    Handler CheckTimeOutHandler = new Handler();
    Date lastUpdateTime;//lastest operation time
    long timePeriod;//no operation time
    float SESSION_EXPIRED = Constants.SESSION_EXPIRY/1000;//session expired time
    boolean CheckLogout = false;//logout flag
    long CheckInterval = 1000;//checking time intercal
    //---------for autologout---------//

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //---------for autologout---------//
        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout,false);
        lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
        Log.d("OTTO","init Signature CheckLogout:"+CheckLogout+",lastUpdateTime:"+lastUpdateTime);
        //---------for autologout---------//

        // Check language
        GlobalFunction.changeLanguage(Signature.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        setTitle(R.string.signature);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //---change to landscape mode---
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //--------set partnerlogo-----------//
        SharedPreferences partnerlogomerdetails = getApplicationContext().getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
        String rawPartnerlogo = partnerlogomerdetails.getString(Constants.pref_Partnerlogo,"");
        merName = partnerlogomerdetails.getString(Constants.MERNAME, "");
        try {
            DesEncrypter encrypter = new DesEncrypter(merName);
            partnerlogo = encrypter.decrypt(rawPartnerlogo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//		String prefpaygate = prefsettings.getString(Constants.pref_PayGate,Constants.default_paygate);
//		String prefpartnerlogopaygate = prefpaygate;
//		String baseUrl=PayGate.getURL_partnerlogo(prefpartnerlogopaygate)+partnerlogo;
//		ImageView partnerlogoview = (ImageView)findViewById(R.id.partnerlogo);
//		PartnerLogoUtil.setImageToImageView(partnerlogoview,baseUrl,partnerlogo);
        //--------set partnerlogo-----------//

        mContent = (LinearLayout) findViewById(R.id.linearlayout);
        mSignature = new signature(this, null);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        mClear = (Button) findViewById(R.id.clearsign);
        mGetSign = (Button) findViewById(R.id.savesign);
        mGetSign.setEnabled(false);
        mView = mContent;
        Intent intent4 = getIntent();
        merName = intent4.getStringExtra(Constants.MERNAME);
        TextView showMerName = (TextView) findViewById(R.id.showMerName);
        showMerName.setText(merName);

        currName = intent4.getStringExtra(Constants.CURRCODE);
        currCode = CurrCode.getCode(currName);

        showcurr = (TextView)findViewById(R.id.showCurr);
        showcurr.setText(currName);
        showamount2 = (TextView)findViewById(R.id.showAmount);

        String amount = intent4.getStringExtra(Constants.AMOUNT);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(CurrCode.getExponent(currCode));
        nf.setMinimumFractionDigits(CurrCode.getExponent(currCode));

        showamount2.setText(nf.format(Double.parseDouble(amount)));


        loading = (ProgressBar)findViewById(R.id.sigloading);
        loading.setVisibility(View.GONE);
        mClear.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
            }
        });

        mGetSign.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                mView.setDrawingCacheEnabled(true);
                mSignature.save(mView);
                Bundle b = new Bundle();
                b.putString("base64",base64);
                b.putString("status", "done");
                Intent intent5 = new Intent();
                intent5.putExtras(b);

                UploadTask uploadTask = new UploadTask(Signature.this,getPrefPayGate());
                uploadTask.execute();
                retryCount++;
            }});
    }

    public String getPrefPayGate(){
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
        String prefpaygate = prefsettings.getString("PayGate", Constants.default_paygate);
        return prefpaygate;
    }


    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v) {

            Intent intent4 = getIntent();
            SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
            String prefpaygate = prefsettings.getString("PayGate", Constants.default_paygate);

            MasMerRef= PayGate.getMasterMerRef(prefpaygate);
            fname = intent4.getStringExtra(Constants.PAYREF);
            String merchantId = intent4.getStringExtra(Constants.MERID);
            String bankref = intent4.getStringExtra(Constants.BANK_REFNO);
            String authcode = intent4.getStringExtra(Constants.AUTHID);

            fileN = MasMerRef+"_"+fname + ".jpg"; // IMG FIlENAME
            String zipFileName =MasMerRef+"_"+fname+".zip"; // ZIP FILENAME
            password=fname + merchantId + authcode + bankref; //PASSWORD


            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(mContent.getWidth(),
                        mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(mBitmap);

            //saving to cache
            File cachedirectory = getBaseContext().getCacheDir();

            File image = new File(cachedirectory,fileN);
            try {
                OutputStream fos = new FileOutputStream(image);
                v.draw(canvas);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

                String fileToZip = cachedirectory.toString()+"/"+fileN;

                String zipname= "/"+ zipFileName;

                ZipFile(cachedirectory.toString()+zipname, fileToZip, password);
                filename = cachedirectory.toString()+zipname;

                fos.flush();
                fos.close();
            } catch (Exception e) {
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }




    @Override
    public void onBackPressed() {
        //disable back button
    }

    public void ZipFolder(String zipfile, String folderpath, String password){
        try{
            ZipFile zipFile = new ZipFile(zipfile);
            ZipParameters parameters = new ZipParameters();

            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(password);
            zipFile.addFolder(folderpath, parameters);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ZipFile(String zipfile, String filepath, String password){
        try{
            ZipFile zipFile = new ZipFile(zipfile);
            File fileToAdd = new File(filepath);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(password);
            zipFile.addFile(fileToAdd, parameters);
        }catch(Exception e){

        }
    }

    // Upload Signature Zip AsyncTask
    public class UploadTask extends AsyncTask<String, Void, String> {


        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        private Signature activity;
        private String payGate;
        String result;
        public UploadTask(Signature activity, String payGate)
        {
            this.activity = activity;
            this.payGate = payGate;

        }
        @Override
        protected void onPreExecute(){
            mGetSign.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try{
                String uploadUrl = PayGate.getURL_fileUpload(payGate);
                URL url = new URL(uploadUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url
                        .openConnection();
                TrustModifier.relaxHostChecking(httpURLConnection);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                DataOutputStream dos = new DataOutputStream(httpURLConnection
                        .getOutputStream());
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                        + filename.substring(filename.lastIndexOf("/") + 1)
                        + "\"" + end);
                dos.writeBytes(end);
                FileInputStream fis = new FileInputStream(filename);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1)
                {
                    dos.write(buffer, 0, count);
                }
                fis.close();
                dos.writeBytes(end);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                dos.flush();
                InputStream is = httpURLConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                result = br.readLine();

                dos.close();
                is.close();
            }catch (Exception e){
            }
            return result;
        }

        @Override
        protected void onPostExecute(String Result){
            Log.d("Success", result+"123");
            loading.setVisibility(View.GONE);
            if(result!=null){
                if(result.equalsIgnoreCase("Sucess")){
                    mGetSign.setEnabled(true);
                    activity.success();
                }else{
                    mGetSign.setEnabled(true);
                    activity.retryUpload();
                }
            }else{
                activity.retryUpload();
            }
        }
    }

    public void success() {
        // TODO Auto-generated method stub
        Log.d("Success", "Sucess");
        Intent getIntent = getIntent();
        Intent successIntent = new Intent();
        successIntent.putExtra(Constants.MERID,getIntent.getStringExtra(Constants.MERID));
        successIntent.putExtra(Constants.MERNAME, getIntent.getStringExtra(Constants.MERNAME));
        successIntent.putExtra(Constants.AMOUNT, getIntent.getStringExtra(Constants.AMOUNT));
        successIntent.putExtra(Constants.SURCHARGE, getIntent.getStringExtra(Constants.SURCHARGE));
        successIntent.putExtra(Constants.MERREQUESTAMT, getIntent.getStringExtra(Constants.MERREQUESTAMT));
        successIntent.putExtra(Constants.CURRCODE, getIntent.getStringExtra(Constants.CURRCODE));
        successIntent.putExtra(Constants.PAYMETHOD, getIntent.getStringExtra(Constants.PAYMETHOD));
        successIntent.putExtra(Constants.CARDNO, getIntent.getStringExtra(Constants.CARDNO));
        successIntent.putExtra(Constants.EXPMONTH,getIntent.getStringExtra(Constants.EXPMONTH));
        successIntent.putExtra(Constants.EXPYEAR,getIntent.getStringExtra(Constants.EXPYEAR));
        successIntent.putExtra(Constants.CARDHOLDER,getIntent.getStringExtra(Constants.CARDHOLDER));
//		successIntent.putExtra(Constants.BANK_REFNO, getIntent.getStringExtra(Constants.BANK_REFNO));
        successIntent.putExtra(Constants.TXTIME, getIntent.getStringExtra(Constants.TXTIME));
        successIntent.putExtra(Constants.PAYREF, getIntent.getStringExtra(Constants.PAYREF));
        successIntent.putExtra(Constants.MERCHANT_REF, getIntent.getStringExtra(Constants.MERCHANT_REF));
        successIntent.putExtra(Constants.PAYTYPE, getIntent.getStringExtra(Constants.PAYTYPE));
        successIntent.putExtra(Constants.TRACE_NO, getIntent.getStringExtra(Constants.TRACE_NO));
        successIntent.putExtra(Constants.BATCH_NO, getIntent.getStringExtra(Constants.BATCH_NO));
        successIntent.putExtra(Constants.TERMINAL_ID, getIntent.getStringExtra(Constants.TERMINAL_ID));
        successIntent.putExtra(Constants.CVMResult, getIntent.getStringExtra(Constants.CVMResult));
        successIntent.putExtra(Constants.EntryMode, getIntent.getStringExtra(Constants.EntryMode));
        successIntent.putExtra(Constants.ERRMSG, getIntent.getStringExtra(Constants.ERRMSG));
        successIntent.putExtra(Constants.FILE_NAME, fileN);
        successIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.d("Success", "Sucess");
        successIntent.setClass(Signature.this, CardPayment_Success.class);
        startActivity(successIntent);
        finish();
    }

    public void retryUpload() {
        // TODO Auto-generated method stub
        if(retryCount==3){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        else{
            retryCount++;
            UploadTask uploadTask = new UploadTask(Signature.this, getPrefPayGate());
            uploadTask.execute();
        }
    }

    //-------------------------------------auto logout-------------------------------------//
    /**
     * Time counter Thread
     */
    private Runnable checkTimeOutTask = new Runnable() {

        public void run() {
            Date timeNow = new Date(System.currentTimeMillis());
            //calculate no operation time
            /*curTime - lastest opt time = no opt time*/
            timePeriod = timeNow.getTime() - lastUpdateTime.getTime();

            /*converted into second*/
            float timePeriodSecond = ((float) timePeriod / 1000);

            if(CheckLogout){
                /* do logout */
                Log.d("OTTO","做登出操作"+this.getClass());
                //logout flag change to true
                CheckLogout = true;
//                autologout();
            }else{
                if( timePeriodSecond > SESSION_EXPIRED ){
                    /* do logout */
                    Log.d("OTTO","做登出操作"+this.getClass());
                    //logout flag change to true
                    CheckLogout = true;
//                    autologout();
                }else{
                    Log.d("OTTO", "没有超过规定时长"+this.getClass());
                }
            }

            if(!CheckLogout) {
                //check no opt time per 'CheckInterval' sencond
                CheckTimeOutHandler.postDelayed(checkTimeOutTask, CheckInterval);
            }
        }
    };

    //listen touch movement
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("OTTO","onTouchEvent checklogout:"+CheckLogout);
        if( !CheckLogout ) {
            updateUserActionTime();
        }
        return super.dispatchTouchEvent(event);
    }

    //reset no opt time and lastest opt time when user opt
    public void updateUserActionTime() {
        Date timeNow = new Date(System.currentTimeMillis());
        timePeriod = timeNow.getTime() - lastUpdateTime.getTime();
        lastUpdateTime.setTime(timeNow.getTime());
    }

//    public void autologout(){
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        SharedPreferences.Editor edit = pref.edit();
//        edit.putBoolean(Constants.CheckLogout,true);
//        edit.commit();
//    }

    @Override
    protected void onResume() {
        //start check timeout thread when activity show
        CheckTimeOutHandler.postAtTime(checkTimeOutTask, CheckInterval);
        super.onResume();
    }

    @Override
    protected void onPause() {
        /*activity不可见的时候取消线程*/
        //stop check timeout thread when activity no show
        CheckTimeOutHandler.removeCallbacks(checkTimeOutTask);
        super.onPause();
    }
    //-------------------------------------auto logout-------------------------------------//

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
