package com.example.dell.smartpos.Scanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;
import com.example.dell.smartpos.Scanner.camera.CameraManager;
import com.example.dell.smartpos.Scanner.decoding.CaptureActivityHandler;
import com.example.dell.smartpos.Scanner.decoding.InactivityTimer;
import com.example.dell.smartpos.Scanner.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

public class CameraCapture extends AppCompatActivity implements Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    //    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private String amount;
    private String merRequestAmt;
    private String surcharge;
    private String mdr;
    private String currcode1;
    private String currCode;
    private String merchantRef;
    private String pMothod;
    private String lang;
    private String remark;
    private String hideSurcharge;
    private String surC;

    //--Edited 25/07/18 by KJ--//
    private int timeout;

    private android.app.AlertDialog alert;

    public static String errorMessage = "0";
    //--done Edited 25/07/18 by KJ--//

    private TextView step1;
    private TextView step2;
    private TextView step3;
    private TextView step4;

    //---------for autologout---------//
    Handler CheckTimeOutHandler = new Handler();
    Date lastUpdateTime;//lastest operation time
    long timePeriod;//no operation time
    float SESSION_EXPIRED = Constants.SESSION_EXPIRY / 1000;//session expired time
    boolean CheckLogout = false;//logout flag
    long CheckInterval = 1000;//checking time intercal
    //---------for autologout---------//

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.scan_qr);

        step1 = (TextView) findViewById(R.id.progress_step_1);
        step2 = (TextView) findViewById(R.id.progress_step_2);
        step3 = (TextView) findViewById(R.id.progress_step_3);
        step4 = (TextView) findViewById(R.id.progress_step_4);

        step1.setText(R.string.enter_amount);
        step2.setText(R.string.select_qr_payment);
        step3.setText(R.string.read_qr);
        step4.setText(R.string.payment_result);

        step2.setTypeface(null, Typeface.BOLD);
        step3.setTypeface(null, Typeface.BOLD);

        errorMessage = "0";

        //---------for autologout---------//
        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout, false);
        lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
        Log.d("OTTO", "init CameraCapture CheckLogout:" + CheckLogout + ",lastUpdateTime:" + lastUpdateTime);
        //---------for autologout---------//

        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        hasSurface = false;

        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        amount = amount.replaceAll(",", "");
        merRequestAmt = intent.getStringExtra("MerRequestAmt");
        merRequestAmt = merRequestAmt.replaceAll(",", "");
        surcharge = intent.getStringExtra("Surcharge");
        mdr = intent.getStringExtra("Mdr");
        currcode1 = intent.getStringExtra("currCode1");
        merchantRef = intent.getStringExtra("MerchantRef");
        currCode = intent.getStringExtra("currCode");
        pMothod = intent.getStringExtra("pMothod");
        remark = intent.getStringExtra("remark");
        hideSurcharge = intent.getStringExtra("hideSurcharge");
        surC = intent.getStringExtra("surC");
        //--Edited 25/07/18 by KJ--//
        timeout = intent.getIntExtra("timeout", Integer.parseInt(Constants.default_payment_timeout));

        inactivityTimer = new InactivityTimer(this, timeout);

        //--done Edited 25/07/18 by KJ--//
        SharedPreferences pref_lang = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);

        lang = pref_lang.getString(Constants.pref_Lang, Constants.default_language);

        if ("T".equals(hideSurcharge)) {
            viewfinderView.SetamountFun(merRequestAmt, currCode, mdr, hideSurcharge, surC, pMothod, lang);
        } else {
            viewfinderView.SetamountFun(amount, currCode, mdr, hideSurcharge, surC, pMothod, lang);
        }

        String feedbackdate = "amount:" + amount + ", merRequestAmt:" + merRequestAmt + ", surcharge:" + surcharge + ", Mdr:" + mdr + " , currcode1:" + currcode1 + " , currcode:" + currCode + " , MerchantRef:" + merchantRef + " , pMothod:" + pMothod + " , hideSurcharge:" + hideSurcharge;
        System.out.println("OTTO-----" + feedbackdate);
    }

    @Override
    protected void onDestroy() {
        errorMessage = "1";
        inactivityTimer.shutdown();

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        errorMessage = "1";
        finish();
    }

    //scanning result
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(CameraCapture.this, R.string.scan_failed, Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();

            this.setResult(RESULT_OK, resultIntent);
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("auth_code", resultString);
            bundle.putString("amount", amount);
            bundle.putString("MerRequestAmt", merRequestAmt);
            bundle.putString("Surcharge", surcharge);
            bundle.putString("currcode1", currcode1);
            bundle.putString("MerchantRef", merchantRef);
            bundle.putString("pMothod", pMothod);
            bundle.putString("remark", remark);
            bundle.putString("hideSurcharge", hideSurcharge);

            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
        }
        CameraCapture.this.finish();
    }



    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

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

            if (CheckLogout) {
                /* do logout */
                Log.d("OTTO", "做登出操作" + this.getClass());
                //logout flag change to true
                CheckLogout = true;
//                autologout();
            } else {
                if (timePeriodSecond > SESSION_EXPIRED) {
                    /* do logout */
                    Log.d("OTTO", "做登出操作" + this.getClass());
                    //logout flag change to true
                    CheckLogout = true;
//                    autologout();
                } else {
                    Log.d("OTTO", "没有超过规定时长" + this.getClass());
                }
            }

            if (!CheckLogout) {
                //check no opt time per 'CheckInterval' sencond
                CheckTimeOutHandler.postDelayed(checkTimeOutTask, CheckInterval);
            }
        }
    };

    //listen touch movement
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("OTTO", "onTouchEvent checklogout:" + CheckLogout);
        if (!CheckLogout) {
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

//    public void autologout() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        SharedPreferences.Editor edit = pref.edit();
//        edit.putBoolean(Constants.CheckLogout, true);
//        edit.commit();
//    }

    @Override
    protected void onResume() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
        //start check timeout thread when activity show
        CheckTimeOutHandler.postAtTime(checkTimeOutTask, CheckInterval);
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;


        }

        CameraManager.get().closeDriver();
        /*activity不可见的时候取消线程*/
        //stop check timeout thread when activity no show
        CheckTimeOutHandler.removeCallbacks(checkTimeOutTask);
        super.onPause();
    }
    //-------------------------------------auto logout-------------------------------------//

    @Override
    public boolean onSupportNavigateUp() {
        errorMessage = "1";
        finish();
        return true;
    }

}
