package com.example.dell.smartpos.Printer;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

import com.example.dell.smartpos.DesEncrypter;
import com.example.dell.smartpos.R;
import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.MainActivity;
import com.example.dell.smartpos.SettingsFragment;


import java.util.Date;

/**
 * Created by otto on 2017/7/18/0018.
 */

public class BluetoothSelect extends AppCompatActivity {

    //    String partnerlogo = "";
//    String merName="";

    Button btn_confirm;
    Button btn_search;
    ListView bondDevices;
    TextView selection;

    Context context = null;
    BluetoothUtil bluetoothUtil = null;

    String merchantId;
    String merchantName;
    private SettingsFragment settingsFragment;


    //---------for autologout---------//
    Handler CheckTimeOutHandler = new Handler();
    Date lastUpdateTime;//lastest operation time
    long timePeriod;//no operation time
    float SESSION_EXPIRED = Constants.SESSION_EXPIRY / 1000;//session expired time
    boolean CheckLogout = false;//logout flag
    long CheckInterval = 1000;//checking time intercal
    //---------for autologout---------//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetoothselect);
        setTitle(R.string.Printer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



//        //--------set partnerlogo-----------//
//        SharedPreferences partnerlogomerdetails = getApplicationContext().getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
//        String rawPartnerlogo = partnerlogomerdetails.getString(Constants.pref_Partnerlogo,"");
//        merName = partnerlogomerdetails.getString(Constants.MERNAME, "");
//        try {
//            DesEncrypter encrypter = new DesEncrypter(merName);
//            partnerlogo = encrypter.decrypt(rawPartnerlogo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
//        String prefpaygate = prefsettings.getString(Constants.pref_PayGate,Constants.default_paygate);
//        String prefpartnerlogopaygate = prefpaygate;
//        String baseUrl= PayGate.getURL_partnerlogo(prefpartnerlogopaygate)+partnerlogo;
//        ImageView partnerlogoview = (ImageView)findViewById(R.id.partnerlogo);
//        Button btn_logout = (Button)findViewById(R.id.btn_logout);
//        btn_logout.setVisibility(View.INVISIBLE);
//        PartnerLogoUtil.setImageToImageView(partnerlogoview,baseUrl,partnerlogo);
//        //--------set partnerlogo-----------//

        this.context = this;


        //---------for autologout---------//
        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout, false);
        lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
        Log.d("OTTO", "init BluetoothSelect CheckLogout:" + CheckLogout + ",lastUpdateTime:" + lastUpdateTime);
        //---------for autologout---------//

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_search = (Button) findViewById(R.id.btn_search);
        bondDevices = (ListView) findViewById(R.id.bondDevices);
        selection = (TextView) findViewById(R.id.selection);

        bluetoothUtil = new BluetoothUtil(this.context, bondDevices, btn_search, selection);

        if (bluetoothUtil.isOpen()) {
            bluetoothUtil.searchDevices();
        } else {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
            bluetoothUtil.searchDevices();
//            Toast.makeText(this.context, R.string.bluetooth_no_start, Toast.LENGTH_SHORT).show();
        }

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                if (bluetoothUtil.isOpen()) {
                    bluetoothUtil.searchDevices();
                } else {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, 1);
                    bluetoothUtil.searchDevices();
                    //            Toast.makeText(this.context, R.string.bluetooth_no_start, Toast.LENGTH_SHORT).show();
                }
            }
        });
        settingsFragment = new SettingsFragment();

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences merdetails = getApplicationContext().getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
                String rawMerId = (merdetails.getString(Constants.pref_MerID, ""));

                merchantName = merdetails.getString(Constants.MERNAME, "");
                try{

                    DesEncrypter encrypter = new DesEncrypter(merchantName);
                    merchantId = encrypter.decrypt(rawMerId);
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Constants.LIST_ACTIVITY = 3;
                Constants.tabSuccess = 0;
                Bundle settingsArgs = new Bundle();
                settingsArgs.putString(Constants.MERID, merchantId);
                settingsArgs.putString(Constants.MERNAME, merchantName);
                settingsFragment.setArguments(settingsArgs);
                setFragment(settingsFragment);
                setResult(RESULT_OK);
                finish();
            }
        });
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

    public void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(BluetoothSelect.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
