package com.example.dell.smartpos.Settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.GlobalFunction;
import com.example.dell.smartpos.R;

public class SettingsMerchant extends AppCompatActivity {

    ScrollView scroll;

    LinearLayout setting_language;
    LinearLayout setting_merRef;
    LinearLayout setting_password_protection;
    LinearLayout setting_sameday_refundQR;
    LinearLayout settingModulePW;
    LinearLayout email_setting_control;
    LinearLayout emailSetting;
    LinearLayout transaction_history_checking;

    TextView disLang;
    TextView merRef;
    TextView settingPwText;
    TextView supportEmail;
    TextView historyDays;

    Switch swSamedayRefundQRControl;
    Switch swOrderControl;

    Boolean setSettingPW = false;
    AlertDialog.Builder dialog;
    AlertDialog alertDialog;

    int dayTrxChecking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize language
        GlobalFunction.changeLanguage(SettingsMerchant.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_merchant);

        setTitle(R.string.settings_merchant);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // Initialize layout
        scroll = (ScrollView) findViewById(R.id.scroll_settings_merchant);

        setting_language = (LinearLayout) findViewById(R.id.setting_language);
        setting_merRef = (LinearLayout) findViewById(R.id.setting_merRef);
        setting_password_protection = (LinearLayout) findViewById(R.id.setting_password_protection);
        setting_sameday_refundQR = (LinearLayout) findViewById(R.id.setting_same_day_refundQR);
        settingModulePW = (LinearLayout) findViewById(R.id.setting_pw);
        email_setting_control = (LinearLayout) findViewById(R.id.email_setting_control);
        emailSetting = (LinearLayout) findViewById(R.id.emailSetting);
        transaction_history_checking = (LinearLayout) findViewById(R.id.transaction_history_checking);

        disLang = (TextView) findViewById(R.id.language);
        merRef = (TextView) findViewById(R.id.merRef);
        settingPwText = (TextView) findViewById(R.id.settingPwText);
        supportEmail = (TextView) findViewById(R.id.supportEmail);
        historyDays = (TextView) findViewById(R.id.historyDays);

        swSamedayRefundQRControl = (Switch) findViewById(R.id.samedayRefundQRControl);
        swOrderControl = (Switch) findViewById(R.id.orderControl);

        // Initialize settings
        final SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String settingMode = pref.getString(Constants.pref_setting_mode, Constants.default_setting_mode);

        final String[] merchantRef = {getResources().getString(R.string.auto), getResources().getString(R.string.manual)};
        final String[] language = {getResources().getString(R.string.english), getResources().getString(R.string.trad_chi), getResources().getString(R.string.simp_chi), getResources().getString(R.string.thai)};

        if (settingMode.equalsIgnoreCase("mode1")) {
            setting_merRef.setVisibility(View.VISIBLE);
            setting_password_protection.setVisibility(View.VISIBLE);
            setting_sameday_refundQR.setVisibility(View.VISIBLE);
            settingModulePW.setVisibility(View.VISIBLE);
            email_setting_control.setVisibility(View.VISIBLE);
            emailSetting.setVisibility(View.VISIBLE);
            transaction_history_checking.setVisibility(View.VISIBLE);

        } else if (settingMode.equalsIgnoreCase("mode2")) {
            setting_merRef.setVisibility(View.VISIBLE);
            setting_password_protection.setVisibility(View.VISIBLE);
            setting_sameday_refundQR.setVisibility(View.VISIBLE);
            settingModulePW.setVisibility(View.VISIBLE);
            email_setting_control.setVisibility(View.VISIBLE);
            emailSetting.setVisibility(View.VISIBLE);
            transaction_history_checking.setVisibility(View.VISIBLE);
        }

        // Get Settings Values in SharedPreference and Database and Display on TextViews
        String strLang = "";
        Log.d("disLang",pref.getString(Constants.pref_Lang, Constants.default_language));
        if (pref.getString(Constants.pref_Lang, Constants.default_language).equalsIgnoreCase(Constants.LANG_ENG)) {
            strLang = getString(R.string.english);
        } else if (pref.getString(Constants.pref_Lang, Constants.default_language).equalsIgnoreCase(Constants.LANG_SIMP)) {
            strLang = getString(R.string.simp_chi);
        } else if (pref.getString(Constants.pref_Lang, Constants.default_language).equalsIgnoreCase(Constants.LANG_TRAD)) {
            strLang = getString(R.string.trad_chi);
        } else if (pref.getString(Constants.pref_Lang, Constants.default_language).equalsIgnoreCase(Constants.LANG_THAI)) {
            strLang = getString(R.string.thai);
        }
        Log.d("disLang", strLang);
        disLang.setText(strLang);

        // Initialize merRef
        String strMerRef;
        if (pref.getString(Constants.pref_MerRef, Constants.default_merchantref).equalsIgnoreCase(Constants.MERREF_AUTO)) {
            strMerRef = getString(R.string.auto);
        } else {
            strMerRef = getString(R.string.manual);
        }
        merRef.setText(strMerRef);

        // Initialize same day refund for QR
        if (pref.getString(Constants.pref_control_samedayRefundQR, Constants.default_control_samedayRefundQR).equalsIgnoreCase(getString(R.string.on))){
            swSamedayRefundQRControl.setChecked(true);

        }else{
            swSamedayRefundQRControl.setChecked(false);
        }

        // Initialize system module password
        String settingPW = pref.getString(Constants.pref_Mer_SettingPW, Constants.default_settingPW);
        String settingPW2 = pref.getString(Constants.pref_Mer_SettingPW, Constants.default_settingPW_2);

        if (settingPW != null || settingPW2 != null) {

            String propw = "";
            for (int i = 0; i < settingPW.length(); i++) {
                propw = propw + "*";
            }

            if (settingPW.equals("Admin8188")) {
                settingPwText.setText(R.string.system_default);
            } else {
                settingPwText.setText(propw);
            }

            setSettingPW = true;
        } else {
            settingPwText.setText(getString(R.string.not_set));
            setSettingPW = false;
        }

        // Initialize Stationery Control
        if (pref.getString(Constants.pref_order_control, Constants.default_order_control).equalsIgnoreCase("ON")) {
            swOrderControl.setChecked(true);
            emailSetting.setVisibility(View.VISIBLE);

        } else {
            swOrderControl.setChecked(false);
            emailSetting.setVisibility(View.GONE);
        }

        // Initialize Stationery Email
        String orderEmail = pref.getString(Constants.pref_SupEmail, Constants.default_supportEmail);
        if (orderEmail.equalsIgnoreCase("")) {
            supportEmail.setText(orderEmail);
        } else {
            supportEmail.setText(orderEmail);
        }

        // Initialize Transaction History Limit
        historyDays.setText(GlobalFunction.getTxnHistoryLimit(SettingsMerchant.this) + " " + getString(R.string.day));


        // onClick language
        setting_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog = new AlertDialog.Builder(SettingsMerchant.this);
                dialog.setTitle(R.string.select_language);
                dialog.setItems(language, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int which) {
                        String prefLang;
                        switch (which) {
                            case 0:
                                prefLang = Constants.LANG_ENG;
                                break;
                            case 1:
                                prefLang = Constants.LANG_TRAD;
                                break;
                            case 2:
                                prefLang = Constants.LANG_SIMP;
                                break;
                            case 3:
                                prefLang = Constants.LANG_THAI;
                                break;
                            default:
                                prefLang = Constants.LANG_ENG;
                                break;
                        }
                        TextView disLang = (TextView) findViewById(R.id.language);
                        disLang.setText(language[which]);

                        // Set language
                        GlobalFunction.setLanguage(SettingsMerchant.this, prefLang);

                        // Check language
                        GlobalFunction.changeLanguage(SettingsMerchant.this);
                        restartActivity();
                    }
                });
                alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        // onClick merRef
        setting_merRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog = new AlertDialog.Builder(SettingsMerchant.this);
                dialog.setTitle(R.string.select_merchant_ref);
                dialog.setItems(merchantRef, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int which) {
                        String mref = "";
                        switch (which) {
                            case 0:
                                mref = Constants.MERREF_AUTO;
                                break;
                            case 1:
                                mref = Constants.MERREF_MANUAL;
                                break;
                            default:
                        }
                        TextView merRef = (TextView) findViewById(R.id.merRef);
                        merRef.setText(merchantRef[which]);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString(Constants.pref_MerRef, mref);
                        edit.commit();
                    }
                });
                alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        // onClick merRef
        setting_password_protection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
               Intent intent = new Intent(SettingsMerchant.this, PasswordProtection.class);
               startActivity(intent);
            }
        });


//        // onClick same day refund QR (Linear Layout)
//        setting_sameday_refundQR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String checked = (pref.getString(Constants.pref_control_samedayRefundQR, Constants.default_control_samedayRefundQR));
//
//                if (checked.equalsIgnoreCase(getString(R.string.on))) {
//                    swSamedayRefundQRControl.setChecked(false);
//                    checked = getString(R.string.off);
//
//                }else if(checked.equalsIgnoreCase(getString(R.string.off))){
//                    swSamedayRefundQRControl.setChecked(true);
//                    checked = getString(R.string.on);
//                }
//
//                SharedPreferences.Editor edit = pref.edit();
//                edit.putString(Constants.pref_control_samedayRefundQR, checked);
//                edit.commit();
//            }
//        });

        // onClick same day refund QR (Switch)
        swSamedayRefundQRControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String checked;

                if (isChecked) {
                    checked = getString(R.string.on);
                }else{
                    checked = getString(R.string.off);
                }

                SharedPreferences.Editor edit = pref.edit();
                edit.putString(Constants.pref_control_samedayRefundQR, checked);
                edit.commit();
            }
        });


        // onClick setting module password
        settingModulePW.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                dialog = new AlertDialog.Builder(SettingsMerchant.this);
                dialog.setTitle(R.string.input_settingPW);
                //EditText
                final EditText inputSettingPassword = new EditText(SettingsMerchant.this);
                inputSettingPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                if (setSettingPW) {
                    inputSettingPassword.setHint(getString(R.string.unchanged));
                }
                dialog.setView(inputSettingPassword);
                dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String value = inputSettingPassword.getText().toString().trim();
                        if (!"".equals(value)) {
                            String provalue = value.replaceAll(".", "*");
                            settingPwText.setText(provalue);

                            //edit shared pref data
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString(Constants.pref_Mer_SettingPW, value);
                            edit.commit();
                        } else {
                        }
                    }
                });
                alertDialog = dialog.create();
                alertDialog.show();
            }
        });

//        // onClick stationery control (Linear Layout)
//        email_setting_control.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String checked = (pref.getString(Constants.pref_order_control, Constants.default_order_control));
//
//                if (checked.equalsIgnoreCase(getString(R.string.on))) {
//                    swOrderControl.setChecked(false);
//                    emailSetting.setVisibility(View.GONE);
//                    checked = getString(R.string.off);
//
//                }else if(checked.equalsIgnoreCase(getString(R.string.off))){
//                    swOrderControl.setChecked(true);
//                    emailSetting.setVisibility(View.VISIBLE);
//                    checked = getString(R.string.on);
//
//                    // ScrollView
//                    scroll.post(new Runnable() {
//                        public void run() {
//                            scroll.smoothScrollTo(0, scroll.getBottom());
//                        }
//                    });
//                }
//                SharedPreferences.Editor edit = pref.edit();
//                edit.putString(Constants.pref_order_control, checked);
//                edit.commit();
//            }
//        });

        // onClick stationery control (Switch)
        swOrderControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String checked;

                if (isChecked) {
                    emailSetting.setVisibility(View.VISIBLE);
                    checked = getString(R.string.on);

                    // ScrollView
                    scroll.post(new Runnable() {
                        public void run() {
                            scroll.smoothScrollTo(0, scroll.getBottom());
                        }
                    });
                }else{
                    emailSetting.setVisibility(View.GONE);
                    checked = getString(R.string.off);
                }

                SharedPreferences.Editor edit = pref.edit();
                edit.putString(Constants.pref_order_control, checked);
                edit.commit();
            }
        });

        // onClick stationery email
        emailSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog = new AlertDialog.Builder(SettingsMerchant.this);
                dialog.setTitle(R.string.input_order_Email);
                //EditText
                final EditText inputSupportEmail = new EditText(SettingsMerchant.this);
                dialog.setView(inputSupportEmail);
                dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        TextView tv1 = (TextView) findViewById(R.id.supportEmail);
                        String value = inputSupportEmail.getText().toString();
                        if ("".equals(value)) {
                            tv1.setText(getString(R.string.not_set));
                        } else {
                            tv1.setText(value);
                        }
                        //edit shared pref data
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString(Constants.pref_SupEmail, value);
                        edit.commit();
                    }
                });
                alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        //Added on 20190923
        // onClick transactionChecking
        transaction_history_checking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                LayoutInflater mLayoutInflater = LayoutInflater.from(SettingsMerchant.this);
                View view = mLayoutInflater.inflate(R.layout.dialogbox_transaction_checking, null);

                Button addDay  = (Button) view.findViewById(R.id.addDayHistory);
                Button minusDay = (Button) view.findViewById(R.id.minusDayHistory);
                final TextView days =(TextView) view.findViewById(R.id.dayChecking);

                final SharedPreferences pref = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
                dayTrxChecking = pref.getInt(Constants.pref_transaction_checking, Constants.default_transaction_checking);
                days.setText(String.valueOf(dayTrxChecking));

                addDay.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dayTrxChecking < Constants.max_transaction_checking) {
                            dayTrxChecking = dayTrxChecking + 1;
                        }

                        days.setText(String.valueOf(dayTrxChecking));
                    }
                });

                minusDay.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dayTrxChecking > Constants.default_transaction_checking) {
                            dayTrxChecking = dayTrxChecking - 1;
                        }

                        days.setText(String.valueOf(dayTrxChecking));
                    }
                });

                addDay.setOnTouchListener(new View.OnTouchListener() {

                    private Handler mHandler;

                    @Override public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                if (mHandler != null) return true;
                                mHandler = new Handler();
                                mHandler.postDelayed(mAction, 150);
                                break;
                            case MotionEvent.ACTION_UP:
                                if (mHandler == null) return true;
                                mHandler.removeCallbacks(mAction);
                                mHandler = null;
                                break;
                        }
                        return false;
                    }

                    Runnable mAction = new Runnable() {
                        @Override public void run() {
                            if (dayTrxChecking < Constants.max_transaction_checking) {
                                dayTrxChecking = dayTrxChecking + 1;
                            }

                            days.setText(String.valueOf(dayTrxChecking));
                            mHandler.postDelayed(this, 150);
                        }
                    };
                });

                minusDay.setOnTouchListener(new View.OnTouchListener() {

                    private Handler mHandler;

                    @Override public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                if (mHandler != null) return true;
                                mHandler = new Handler();
                                mHandler.postDelayed(mAction, 150);
                                break;
                            case MotionEvent.ACTION_UP:
                                if (mHandler == null) return true;
                                mHandler.removeCallbacks(mAction);
                                mHandler = null;
                                break;
                        }
                        return false;
                    }

                    Runnable mAction = new Runnable() {
                        @Override public void run() {
                            if (dayTrxChecking > Constants.default_transaction_checking) {
                                dayTrxChecking = dayTrxChecking - 1;
                            }

                            days.setText(String.valueOf(dayTrxChecking));
                            mHandler.postDelayed(this, 150);
                        }
                    };
                });

                dialog = new AlertDialog.Builder(SettingsMerchant.this);
                dialog.setTitle(R.string.select_transaction_days);
                dialog.setView(view);
                dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        SharedPreferences.Editor edit = pref.edit();
                        edit.putInt(Constants.pref_transaction_checking, Integer.valueOf(days.getText().toString()));
                        edit.commit();

                        historyDays.setText(days.getText().toString() + " " + getString(R.string.day));

                    }
                });
                alertDialog = dialog.create();
                alertDialog.show();

//                dialog.setItems(transaction_days, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int which) {
//                        // TODO Auto-generated method stub
//
//                        TextView historyDays = (TextView) rootView.findViewById(R.id.historyDays);
//                        historyDays.setText(transaction_days[which]);
//                        SharedPreferences pref = getActivity().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
//                        SharedPreferences.Editor edit = pref.edit();
//                        edit.putInt(Constants.pref_transaction_checking, which + 1);
//                        edit.commit();
//                    }
//                });
//                alertDialog = dialog.create();
//                alertDialog.show();
            }
        });
    }

    private void restartActivity() {
        finish();
        startActivity(getIntent());
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
