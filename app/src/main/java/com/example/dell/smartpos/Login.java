package com.example.dell.smartpos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Login extends AppCompatActivity {

    private final static String API_TABLE = "Settings";

    AlertDialog.Builder dialog;
    AlertDialog alertDialog;

    String payGate = Constants.default_paygate;
    String apiSUR_CAL = "";
    String selLang = "";
    String curLang = "";
    Boolean rememberMe;
    Boolean spinFlag = false;

    AutoCompleteTextView etMerchantID, etUserID;
    EditText etPassword;
    CheckBox keepLogin;
    TextView reset;
    ProgressDialog progressDialog;

    Boolean toastUpdate = false;
    Boolean toastError = false;
    Boolean toastInvalidMPOS = false;
    String resultError;

    private AlertDialog alert, alertMPOS;
    AlertDialog updateAlert;
    private AlertDialog updateAlert2;

    private Gallery gallery;
    private ImageAdapter imageAdapter;

    // Change image sequence will cause incorrect pointing to paygate services
    private int[] resIds = new int[]{R.drawable.logo_siampay, R.drawable.logo_paydollar, R.drawable.logo_pesopay};
    //private int[] resIds = new int[]{R.drawable.logo_paydollar};
    private Button btnLogin;

    private final static String MERCHANT_ID = "merID";
    String merchantName;

    Boolean checkKey = true;
    private final int TRIGGER_SEARCH = 1;
    private final long SEARCH_TRIGGER_DELAY_IN_MS = 1000; // delay time in auto-filled

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        // Hide the bottom navigation button if FD apps installed
        if (GlobalFunction.isFDMS_BASE24Installed(getPackageManager())) {
            GlobalFunction.disablePaxNavigationButton(Login.this);
			GlobalFunction.killFDMSBackgroundSevices(Login.this);
		}

        int Permission_All = 1;

        String[] Permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, };
        if(!hasPermissions(this, Permissions)){
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }

        // Device screen density
        System.out.println("Density: " + getResources().getDisplayMetrics().density);

        setContentView(R.layout.activity_login);

        SharedPreferences pref_lang = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);

        // Check language
        curLang = GlobalFunction.getLanguage(Login.this);

        SharedPreferences merDetails = getApplicationContext()
                .getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
        merchantName = merDetails.getString(Constants.pref_MerName, "");

        autoLogin(Constants.SESSION_EXPIRY);
        Log.d("OTTO", "CheckUpdate 00000 savedInstanceState0;" + savedInstanceState);
        if (savedInstanceState != null)
            // check new version is available
            Log.d("OTTO", "CheckUpdate 00000 savedInstanceState1;" + savedInstanceState);
        //        checkingUpdateApp(savedInstanceState);
        Log.d("OTTO", "CheckUpdate 00000 savedInstanceState2;" + savedInstanceState);

        initViews();
        String deviceName = android.os.Build.MODEL;
        Log.d("deviceName", deviceName);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                Constants.SESSION_DATA, MODE_PRIVATE);

        rememberMe = pref.getBoolean("remember", false);

        // if rememberMe is true in sharedpref
        if (rememberMe) {
            // get saved MerId and userId
            String prefmID = pref.getString(Constants.pref_MerID, "");
            String prefuID = pref.getString(Constants.pref_UserID, "");
            String encmerchantId = "";
            String encUserId = "";

            // decrypt mid, uid
            try {
                DesEncrypter encrypter = new DesEncrypter(merchantName);
                encmerchantId = encrypter.decrypt(prefmID);
                encUserId = encrypter.decrypt(prefuID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            etUserID.setText(encUserId);
            etMerchantID.setText(encmerchantId);
            keepLogin.setChecked(true);
        }

        // for autoComplete editText
        autoCompleteEditText();
        initGalleryOption();
        initLanguageOption();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences prefsettings = getApplicationContext()
                        .getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

                // SET PAYGATE
                SharedPreferences.Editor editsettings = prefsettings.edit();
                editsettings.putString(Constants.pref_PayGate, payGate);
                editsettings.commit();

                String merchantID = etMerchantID.getText().toString();
                String password = etPassword.getText().toString();
                String userID = etUserID.getText().toString();

                if (TextUtils.isEmpty(merchantID) || TextUtils.isEmpty(password) || TextUtils.isEmpty(userID)) {
                    Toast.makeText(Login.this, getString(R.string.please_fill),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isOnline()) {
                    InputMethodManager im = (InputMethodManager) Login.this
                            .getApplicationContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getWindow().getDecorView()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    progressDialog = new ProgressDialog(Login.this);
                    progressDialog.setMessage(getString(R.string.logging));
                    progressDialog.setCancelable(true);
                    progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            btnLogin.setEnabled(true);
                        }
                    });

                    mLockScreenRotation();
                    btnLogin.setEnabled(false);
                    toastError = false;

                    // Execute AsyncTask for Logging in
                    LoginTask loginTask = new LoginTask(Login.this,
                            progressDialog, getPrefPayGate());
                    loginTask.execute(merchantID, userID, password);

                } else {
                    // if no internet connection, show alertDialog
                    showAlertNoConnection();
                }
            }

        });



        etMerchantID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etUserID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        if (savedInstanceState != null) {
            updatesAlertDialog(savedInstanceState);
        }

        reset.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences(
                        Constants.SESSION_DATA, MODE_PRIVATE);

                rememberMe = pref.getBoolean("remember", false);

                if (rememberMe) {

                    String prefmID = pref.getString(Constants.pref_MerID, "");
                    String prefuID = pref.getString(Constants.pref_UserID, "");
                    String encmerchantId = "";
                    String encUserId = "";

                    try {
                        DesEncrypter encrypter = new DesEncrypter(merchantName);
                        encmerchantId = encrypter.decrypt(prefmID);
                        encUserId = encrypter.decrypt(prefuID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    etUserID.setText(encUserId);
                    etMerchantID.setText(encmerchantId);
                    etPassword.setText("");
                    keepLogin.setChecked(true);

                } else {

                    clear();
                    keepLogin.setChecked(false);

                }
            }

        });
    }


    /**
     * Initializes window format and load Lang
     */
    public void init() {
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

        // Check language
        GlobalFunction.changeLanguage(Login.this);
    }

    /**
     * Initializes views
     */
    public void initViews() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etMerchantID = (AutoCompleteTextView) findViewById(R.id.merchantID);
        etPassword = (EditText) findViewById(R.id.password);
        etUserID = (AutoCompleteTextView) findViewById(R.id.userID);
        keepLogin = (CheckBox) findViewById(R.id.keepLogin);
        reset = (TextView) findViewById(R.id.reset);

        ImageView viewInfo = (ImageView) findViewById(R.id.information);

        clear();

        viewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInfo = new Intent();
                intentInfo.setClass(Login.this, Terms.class);
                startActivity(intentInfo);
            }
        });

    }

    /**
     * Method for AutoLogin
     *
     * @param timeExpiry specifies timeframe of auto-login
     */
    public void autoLogin(long timeExpiry) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                Constants.SESSION_DATA, MODE_PRIVATE);
        if (GlobalFunction.getLoginStatus(Login.this)) {
            SharedPreferences pcheck = getApplicationContext()
                    .getSharedPreferences("CHECK_UPDATE", MODE_PRIVATE);
            SharedPreferences.Editor edit = pcheck.edit();
            edit.putBoolean("Check_done", false);
            edit.commit();
            String enctime = "";
            try {
                DesEncrypter enc = new DesEncrypter(merchantName);
                enctime = enc.decrypt(pref.getString("time", ""));
            } catch (Exception e1) {
                enctime = "0";
                checkKey = false;
                e1.printStackTrace();

            }

            Calendar cal = Calendar.getInstance();
            Long curtime = cal.getTimeInMillis();
            Long timeexp = Long.parseLong(enctime);
            long elapsedtime = curtime - timeexp;
            if (elapsedtime <= timeExpiry) {
                SharedPreferences merdetails = getApplicationContext()
                        .getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
                String merchantId = "";
                String merchantName = merdetails.getString(
                        Constants.pref_MerName, "");
                String currCode = "";
                DatabaseHelper db = new DatabaseHelper(this);
                String rawMerId = merdetails
                        .getString(Constants.pref_MerID, "");
                String whereargs[] = {rawMerId};
                String rawCurrCode = db.getSingleData(Constants.DB_TABLE_ID,
                        Constants.DB_CURRCODE, Constants.DB_MERCHANT_ID,
                        whereargs);
                db.close();

                DesEncrypter encrypter;
                try {
                    encrypter = new DesEncrypter(merchantName);
                    merchantId = encrypter.decrypt(merdetails.getString(
                            Constants.pref_MerID, ""));
                    // merchantName =
                    // encrypter.decrypt(merdetails.getString(Constants.pref_MerName,
                    // ""));
                    currCode = encrypter.decrypt(rawCurrCode);

                } catch (Exception e) {
                    checkKey = false;
                    e.printStackTrace();
                }

                if (checkKey) {
                    if (!(merchantId.equals("") || merchantName.equals("")
                            || currCode == null || currCode.equals(""))) {

                        Calendar c = Calendar.getInstance();

                        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm");
                        String currentTime = df.format(c.getTime());
                        Intent intentLogin = new Intent();
                        String lastLogin = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_LAST_LOGIN, Constants.DB_MERCHANT_ID, whereargs);
                        if (lastLogin != null) {
                            edit = pref.edit();
                            edit.putString(Constants.pref_last_login, lastLogin);
                            edit.commit();
                        } else {
                            edit = pref.edit();
                            edit.putString(Constants.pref_last_login, currentTime);
                            edit.commit();
                        }

                        db.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Constants.DB_LAST_LOGIN, currentTime);
                        String whereClause = MERCHANT_ID + "=?";
                        db.update(Constants.DB_TABLE_ID, values, whereClause, whereargs);
                        db.close();
                        intentLogin.setClass(Login.this, MainActivity.class);
                        intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Login.this.startActivity(intentLogin);
                        finish();
                    }
                }
            }
        }
    }

    public void autoCompleteEditText() {
        DatabaseHelper db = new DatabaseHelper(this);
        String[] mIDlist = null;
        String[] uIDlist = null;
        if (!merchantName.equals("")) {
            try {
                mIDlist = db.getRecords(Constants.DB_TABLE_ID,
                        Constants.DB_MERCHANT_ID, merchantName);
                uIDlist = db.getRecords(Constants.DB_TABLE_ID,
                        Constants.DB_USER_ID, merchantName);
                ArrayAdapter<String> mIDadapter = new ArrayAdapter<String>(
                        this, R.layout.spinner_dropdown_layout_autocomplete,
                        mIDlist);
                ArrayAdapter<String> uIDadapter = new ArrayAdapter<String>(
                        this, R.layout.spinner_dropdown_layout_autocomplete,
                        uIDlist);
                etMerchantID.setAdapter(mIDadapter);
                etUserID.setAdapter(uIDadapter);
            } catch (Exception e) {
                checkKey = false;
                e.printStackTrace();
            } finally {
                db.close();
            }

        }
        // Handler for auto-complete userId when mID is entered
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (checkKey) {
                    if (msg.what == TRIGGER_SEARCH) {
                        triggerSearch();
                    }
                }
            }
        };

        etMerchantID.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                handler.removeMessages(TRIGGER_SEARCH);
                handler.sendEmptyMessageDelayed(TRIGGER_SEARCH,
                        SEARCH_TRIGGER_DELAY_IN_MS);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }
        });

        etMerchantID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    if (rememberMe) {
                        DatabaseHelper db2 = new DatabaseHelper(Login.this);
                        if (etMerchantID.getText().toString().equals("")) {
                            return false;
                        }
                        String enctext = "";
                        try {
                            DesEncrypter encrypter = new DesEncrypter(merchantName);
                            enctext = encrypter.encrypt(etMerchantID.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String whereargs[] = {enctext};
                        String fillUserID = "";
                        String encfillUserID = "";
                        try {
                            fillUserID = db2.getSingleData(
                                    Constants.DB_TABLE_ID,
                                    Constants.DB_USER_ID,
                                    Constants.DB_MERCHANT_ID,
                                    whereargs);
                            if (fillUserID != null) {
                                DesEncrypter encrypter = new DesEncrypter(
                                        merchantName);
                                encfillUserID = encrypter
                                        .decrypt(fillUserID);
                                etUserID.setText(encfillUserID);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        db2.close();
                        if (etMerchantID == null) {
                            fillUserID = "";
                        }
                    }
                }
                return false;
            }
        });

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnLogin.performClick();
                }
                return false;
            }

        });
    }

    /**
     * Method for searching db for auto-complete edittext
     */
    public void triggerSearch() {
        if (rememberMe) {
            DatabaseHelper db2 = new DatabaseHelper(Login.this);
            String enctext = "";
            try {
                DesEncrypter encrypter = new DesEncrypter(merchantName);
                enctext = encrypter.encrypt(etMerchantID.getText()
                        .toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String whereargs[] = {enctext};
            String fillUserID = "";
            String encfillUserID = "";
            try {
                fillUserID = db2.getSingleData(Constants.DB_TABLE_ID,
                        Constants.DB_USER_ID, Constants.DB_MERCHANT_ID,
                        whereargs);
                if (fillUserID != null) {
                    DesEncrypter encrypter = new DesEncrypter(merchantName);
                    encfillUserID = encrypter.decrypt(fillUserID);
                    etUserID.setText(encfillUserID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            db2.close();
            if (etMerchantID == null) {
                fillUserID = "";
            }
        }
    }

    // Gallery for Payment Gateway options
    public void initGalleryOption() {

        gallery = (Gallery) findViewById(R.id.gallery_gateway);
        imageAdapter = new ImageAdapter(this);
        gallery.setHorizontalFadingEdgeEnabled(false);
        gallery.setAdapter(imageAdapter);

        // ADDED BY ERIC 20190816
        // Set pointing PayGate services by SharedPreferences
        if (getPrefPayGate().equalsIgnoreCase(Constants.pg_siampay)) {
            // Pointing to SiamPay
            gallery.setSelection(0);
        } else if (getPrefPayGate().equalsIgnoreCase(Constants.pg_paydollar)) {
            // Pointing to PayDollar
            gallery.setSelection(1);
        } else if (getPrefPayGate().equalsIgnoreCase(Constants.pg_pesopay)) {
            // Pointing to PesoPay
            gallery.setSelection(2);
        } else {
            // Set default pointing to PayDollar
            gallery.setSelection(1);
        }
        // END OF SET PAYGATE SERVICES

        //
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
            }
        });

        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long arg3) {
                int curPos = (pos % resIds.length);
                switch (curPos) {
                    case 0:
                        payGate = Constants.pg_siampay;
                        break;
                    case 1:
                        payGate = Constants.pg_paydollar;
                        break;
                    case 2:
                        payGate = Constants.pg_pesopay;
                        break;
                    default:
                        payGate = Constants.default_paygate;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });
    }


    public class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;

        public ImageAdapter(Context context) {
            mContext = context;
            TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);
            mGalleryItemBackground = typedArray.getResourceId(
                    R.styleable.Gallery_android_galleryItemBackground, 0);
        }

        public int getCount() {
            return resIds.length;
        }

        //        public int getCount() {
        //            return Integer.MAX_VALUE;
        //        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(resIds[position % resIds.length]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            imageView.setPadding(5, 5, 5, 5);
            RelativeLayout borderImg = new RelativeLayout(mContext);
            borderImg.addView(imageView);
            return borderImg;
        }
    }

    public void clear() {
        etMerchantID.setText("");
        etPassword.setText("");
        etUserID.setText("");
    }


    public String getPrefPayGate() {
        SharedPreferences prefsettings = getApplicationContext()
                .getSharedPreferences("Settings", MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate,
                Constants.default_paygate);
        return prefpaygate;
    }

    /**
     * Save InstanceState for Alert Dialog for Error Messages (configChanges)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("toastMsg", toastError);
        outState.putString("resultError", resultError);
        outState.putBoolean("toastUpdate", toastUpdate);
        outState.putBoolean("toastInvalidMPOS", toastInvalidMPOS);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (alert != null) {
            alert.dismiss();
        }
        if (updateAlert != null) {
            updateAlert.dismiss();
        }
        if (updateAlert2 != null) {
            updateAlert2.dismiss();
        }
        if (alertMPOS != null) {
            alertMPOS.dismiss();
        }
    }

    /**
     * Method for getting DeviceId of Android Device
     *
     * @param ctx
     * @return
     */
    @SuppressLint("NewApi")
    public static String getDeviceId(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);

        String tmDevice ="";

        String androidId = Settings.Secure.getString(ctx.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String serial = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO)
            serial = Build.SERIAL;
        if (tmDevice != null)
            return tmDevice;
        if (androidId != null)
            return androidId;
        if (serial != null)
            return serial;
        // other alternatives (i.e. Wi-Fi MAC, Bluetooth MAC, etc.)

        return null;
    }

    public String getDeviceSerialNo(){

        String serialNo = Build.SERIAL;

        return serialNo;
    }

    // Updates Language in context
    public void changeLang(String lang) {
        Locale myLocale = null;
        if (lang.equalsIgnoreCase(Constants.LANG_ENG)) {
            myLocale = Locale.ENGLISH;
        } else if (lang.equalsIgnoreCase(Constants.LANG_TRAD)) {
            myLocale = Locale.TRADITIONAL_CHINESE;
        } else if (lang.equalsIgnoreCase(Constants.LANG_SIMP)) {
            myLocale = Locale.SIMPLIFIED_CHINESE;
        } else if (lang.equalsIgnoreCase(Constants.LANG_THAI)) {
            myLocale = new Locale("th", "TH");
        }
        Configuration config = new Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(Constants.pref_Lang, lang);
        edit.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Method for checking internet connectivity
     *
     * @return boolean whether connected or not
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    /**
     * show Dialog for LoginError, Default is "Network Error" if result Error is
     * null
     *
     * @param result
     */
    public void showLoginError(String result) {
        String error_msg = "";
        btnLogin.setEnabled(true);
        if (result.equals("")) {
            result = getString(R.string.network_error);
        }
        mEnableScreenRotation();
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle(getString(R.string.error));

        if (result.equalsIgnoreCase("Parameter Merchant Id Incorrect")) {
            error_msg = getString(R.string.error1);
        } else if (result.equalsIgnoreCase("No this user")) {
            error_msg = getString(R.string.error2);
        } else if (result.equalsIgnoreCase("Invalid password")) {
            error_msg = getString(R.string.error3);
        } else {
            error_msg = getString(R.string.error4);
        }

        builder.setMessage(error_msg);
        builder.setPositiveButton(getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        toastError = false;
                    }
                });
        alert = builder.create();
        alert.setCancelable(false);
        toastError = true;
        resultError = result;
        if (!((Activity) this).isFinishing()) {
            alert.show();
        }
    }

    public void updatesAlertDialog(Bundle savedInstanceState) {
        // Get SavedInstanceState for Config Changes (rotateScreens)
        if (savedInstanceState.getBoolean("toastMsg")
                && !savedInstanceState.getString("resultError").equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setTitle(getString(R.string.error));
            builder.setMessage(savedInstanceState.getString("resultError"));
            builder.setPositiveButton(getString(R.string.confirm),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            toastError = false;
                        }
                    });
            alert = builder.create();
            alert.setCancelable(false);
            toastError = true;
            resultError = "";
            if (!((Activity) this).isFinishing()) {
                alert.show();
            }
        }

        if (savedInstanceState.getBoolean("toastInvalidMPOS")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setTitle(getString(R.string.error));
            builder.setMessage(R.string.account_doesn_t_support_mpos);
            builder.setPositiveButton(getString(R.string.s_confirm),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            toastInvalidMPOS = false;
                        }
                    });
            alertMPOS = builder.create();
            alertMPOS.setCancelable(false);
            toastInvalidMPOS = true;
            resultError = "";
            if (!((Activity) this).isFinishing()) {
                alertMPOS.show();
            }
        }
    }


    public void showAlertNoConnection() {
        AlertDialog.Builder noConn = new AlertDialog.Builder(Login.this);
        noConn.setTitle(R.string.error);
        noConn.setMessage(getString(R.string.check_internet));
        noConn.setPositiveButton(getString(R.string.s_confirm),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = noConn.create();
        alert.setCancelable(false);
        alert.show();
    }


    // Enable Device Screen Rotation Change
    private void mEnableScreenRotation() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }


    // Prevents Device From Screen Rotation
    private void mLockScreenRotation() {
        switch (this.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }


    private Boolean checkIfFirstTime(String merId) {
        DatabaseHelper db = new DatabaseHelper(this);

        String whereargs[] = {merId};
        int result = db.checkFirstTime(Constants.DB_TABLE_ID, Constants.DB_MERCHANT_ID, Constants.DB_MERCHANT_ID, whereargs);
        boolean isfieldexist = db.isFieldExist(Constants.DB_TABLE_ID, Constants.DB_SUR_CAL, Constants.DB_MERCHANT_ID, whereargs);
        Log.d("OTTO", "login.java is fieldexist:" + isfieldexist);
        if (isfieldexist) {
            Log.d("OTTO", "login.java has DB_SUR_CAL");
        } else {
            boolean alterfield = db.alterField(merId, Constants.DB_TABLE_ID, Constants.DB_SUR_CAL, Constants.DB_MERCHANT_ID, whereargs);
            Log.d("OTTO", "login.java alterfield:" + alterfield);
        }
        db.close();
        if (result == 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Method for Login Successful
     *
     * @param Currcode
     * @param merName
     * @param payMethod
     * @param channelType
     * @param merClass
     * @param amexOR
     * @param visaOR
     * @param masterOR
     * @param jcbOR
     * @param enableSMS
     */
    public void login(String Currcode, String merName, String payMethod, String payBankId,
                      String channelType, String merClass, String amexOR, String visaOR,
                      String masterOR, String jcbOR, String enableSMS, String rate, String fixed, String hideSurcharge, String partnerlogo, String apiId, String apipassword) {
        btnLogin.setEnabled(true);
        mEnableScreenRotation();

        // Check if MPOS is included in channelType
        if (!(channelType.indexOf("MPOS") < 0)) {
            // MPOS is supported, continue login
            toastInvalidMPOS = false;
            if (keepLogin.isChecked() == true) {

                // Save settings
                Calendar cal = Calendar.getInstance();
                long time = cal.getTimeInMillis();
                SharedPreferences pref = getApplicationContext()
                        .getSharedPreferences(Constants.SESSION_DATA,
                                MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                String encmerchantId = "";
                String encUserId = "";
                String encTime = "";
                String txtmID = etMerchantID.getText().toString();
                String txtuID = etUserID.getText().toString();

                // Encrypt Values
                try {
                    DesEncrypter encrypter = new DesEncrypter(merName);
                    encmerchantId = encrypter.encrypt(txtmID);
                    encUserId = encrypter.encrypt(txtuID);
                    encTime = encrypter.encrypt(String.valueOf(time));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                edit.putBoolean("user_logged_in", true);
                edit.putBoolean("remember", true);
                edit.putString(Constants.pref_MerID, encmerchantId);
                edit.putString(Constants.pref_UserID, encUserId);
                edit.putString("time", encTime);
                edit.commit();
            } else {
                SharedPreferences pref = getApplicationContext()
                        .getSharedPreferences(Constants.SESSION_DATA,
                                MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.clear();
                edit.commit();
            }

            String deviceID = "" + getDeviceId(this);
            String deviceSerial = getDeviceSerialNo();
            String encMer_ID = "";
            String encUser_ID = "";
            String encMerClass = "";
            String encCurrCode = "";
            String encPayMethod = "";
            String encRate = "";
            String encFixed = "";
            String enchideSurcharge = "F";
            String encpartnerlogo = "";
            String encryptedAPIid = "";
            String encryptedAPIpw = "";

            DatabaseHelper db = new DatabaseHelper(this);

            try {
                DesEncrypter encrypter = new DesEncrypter(merName);
                encMer_ID = encrypter.encrypt(etMerchantID.getText()
                        .toString());
                encUser_ID = encrypter.encrypt(etUserID.getText().toString());
                encMerClass = encrypter.encrypt(merClass);
                encCurrCode = encrypter.encrypt(Currcode);
                //                encPayMethod = encrypter.encrypt(payMethod+",ALIPAYTHOFFL");
                encPayMethod = encrypter.encrypt(payMethod);
                encRate = encrypter.encrypt(rate);
                encFixed = encrypter.encrypt(fixed);
                enchideSurcharge = encrypter.encrypt(hideSurcharge);
                encryptedAPIid = encrypter.encrypt(apiId);
                encryptedAPIpw = encrypter.encrypt(apipassword);
                encpartnerlogo = encrypter.encrypt(partnerlogo);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ContentValues values = new ContentValues();
            values.put(Constants.DB_MERCHANT_ID, encMer_ID);
            values.put(Constants.DB_USER_ID, encUser_ID);
            values.put(Constants.DB_MER_CLASS, encMerClass);
            values.put(Constants.DB_DEVICE_ID, deviceID);
            values.put(Constants.DB_DEVICE_SERIALNO, deviceSerial);
            Log.d("Serial", deviceSerial);
            values.put(Constants.DB_CURRCODE, encCurrCode);
            values.put(Constants.DB_ENABLE_SMS, enableSMS);
            values.put(Constants.DB_API_ID, encryptedAPIid);
            values.put(Constants.DB_API_PW, encryptedAPIpw);

            String whereClause = MERCHANT_ID + "=?";
            String whereargs[] = {encMer_ID};

            // Update + insert values to Database
            db.upsert(Constants.DB_TABLE_ID, values, whereClause, whereargs);
            apiSUR_CAL = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_SUR_CAL, Constants.DB_MERCHANT_ID, whereargs);
            db.close();

            SharedPreferences merdetails = getApplicationContext().getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
            SharedPreferences.Editor edit = merdetails.edit();
            String encConVisaOR = "";
            String encConAmexOR = "";
            String encConMasterOR = "";
            String encConJcbOR = "";
            try {
                DesEncrypter encrypter = new DesEncrypter(merName);
                encConVisaOR = encrypter.encrypt(Constants.pref_visaOR);
                encConAmexOR = encrypter.encrypt(Constants.pref_amexOR);
                encConMasterOR = encrypter.encrypt(Constants.pref_masterOR);
                encConJcbOR = encrypter.encrypt(Constants.pref_jcbOR);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // edit.putString(Constants.pref_MerName, encMerName);
            edit.putString(Constants.pref_MerName, merName);
            edit.putString(Constants.pref_MerID, encMer_ID);
            edit.putString(Constants.pref_CurrCode, Currcode);
            edit.putString(Constants.pref_pMethod, encPayMethod);
            edit.putString(Constants.pref_Rate, encRate);
            edit.putString(Constants.pref_Fixed, encFixed);
            edit.putString(Constants.pref_hideSurcharge, enchideSurcharge);
            //edit.putString(Constants.pref_Partnerlogo, encpartnerlogo);
            edit.putString(encConVisaOR, visaOR);
            edit.putString(encConAmexOR, amexOR);
            edit.putString(encConMasterOR, masterOR);
            edit.putString(encConJcbOR, jcbOR);
            //Store login password
            System.out.println("---inputPW=" + etPassword.getText().toString() );
            edit.putString(Constants.pref_loginPassword, etPassword.getText().toString());
            edit.commit();

            checkIfFirstTime(encMer_ID);
            if (!checkIfFirstTime(encMer_ID)) {
                Constants.LIST_ACTIVITY = 3; // Change default tab to settings
                Toast.makeText(Login.this, getString(R.string.input_settings),
                        Toast.LENGTH_LONG).show();
            } else { // Show Default Tab
                Constants.LIST_ACTIVITY = 0;
            }
            if (apiSUR_CAL == null) {
                Constants.LIST_ACTIVITY = 3;
            }
            // Start Home Class Activity
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm");
            String currentTime = df.format(c.getTime());
            Intent intentLogin = new Intent();
            String lastLogin = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_LAST_LOGIN, Constants.DB_MERCHANT_ID, whereargs);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
            if (lastLogin != null) {
                edit = pref.edit();
                edit.putString(Constants.pref_last_login, lastLogin);
                edit.commit();
            } else {
                edit = pref.edit();
                edit.putString(Constants.pref_last_login, currentTime);
                edit.commit();
            }

            db.getWritableDatabase();
            values = new ContentValues();
            values.put(Constants.DB_LAST_LOGIN, currentTime);
            whereClause = MERCHANT_ID + "=?";
            db.update(Constants.DB_TABLE_ID, values, whereClause, whereargs);
            db.close();
            intentLogin.setClass(Login.this, MainActivity.class);
            intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Login.this.startActivity(intentLogin);
            finish();
        } else {
            // MPOS is not supported, show alert dialog
            toastInvalidMPOS = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setTitle(getString(R.string.error));
            builder.setMessage(R.string.account_doesn_t_support_mpos);
            builder.setPositiveButton(getString(R.string.s_confirm),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            toastInvalidMPOS = false;
                        }
                    });
            alertMPOS = builder.create();
            alertMPOS.setCancelable(false);
            resultError = "";
            if (!((Activity) this).isFinishing()) {
                alertMPOS.show();
            }
        }
    }

//    public void initautologout() {
//        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        boolean CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout, false);
////        boolean IsAutoLogout = Sessionpref.getBoolean(Constants.IsAutoLogout, false);
////        Log.d("OTTO", "Login CheckLogout:" + CheckLogout + ",IsAutoLogout;" + IsAutoLogout);
////        if (CheckLogout && IsAutoLogout) {
////            Toast.makeText(Login.this, R.string.session_timeout, Toast.LENGTH_LONG).show();
////            Log.d("OTTO", "initlogout:" + CheckLogout);
////        }
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        SharedPreferences.Editor edit = pref.edit();
//        edit.putBoolean(Constants.CheckLogout, false);
//        edit.putBoolean(Constants.IsAutoLogout, false);
//        edit.commit();
//    }

    public void initLanguageOption() {
        // Spinner language
        final String[] arrlanguage = {
                getResources().getString(R.string.english),
                getResources().getString(R.string.trad_chi),
                getResources().getString(R.string.simp_chi),
                getResources().getString(R.string.thai)};
        Spinner spinLang = (Spinner) findViewById(R.id.spinner_language);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Login.this,
                R.layout.spinner_dropdown_layout_item, arrlanguage);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinLang.setAdapter(adapter);

        int setPos = 0;
        if (curLang.equals(Constants.LANG_ENG)) {
            setPos = 0;
        } else if (curLang.equals(Constants.LANG_TRAD)) {
            setPos = 1;
        } else if (curLang.equals(Constants.LANG_SIMP)) {
            setPos = 2;
        } else if (curLang.equalsIgnoreCase(Constants.LANG_THAI)) {
            setPos = 3;
        }

        spinLang.setSelection(setPos);
        spinLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long arg3) {
                if (pos == 0) {
                    selLang = Constants.LANG_ENG;
                } else if (pos == 1) {
                    selLang = Constants.LANG_TRAD;
                } else if (pos == 2) {
                    selLang = Constants.LANG_SIMP;
                } else if (pos == 3) {
                    selLang = Constants.LANG_THAI;
                }

                // Update only if language is changed
                if (curLang.equals(selLang)) {
                    spinFlag = false;
                } else {
                    spinFlag = true;
                }

                if (spinFlag == true) {
                    // Set language
                    GlobalFunction.setLanguage(Login.this, selLang);

                    // Change language
                    GlobalFunction.changeLanguage(Login.this);
                    restartActivity();
                    return;
                }
                curLang = selLang;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void restartActivity() {
        Intent intent = new Intent();
        intent.setClass(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    public static boolean hasPermissions(Context context, String... permissions){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED){
                    return  false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.exit_app))
                .setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!GlobalFunction.isFDMS_BASE24Installed(getPackageManager())) {
                            // If not installed FDMS_BASE24, can exit apps
                            android.os.Process.killProcess(android.os.Process.myPid());
                            finish();
                        } else {
							// Else run the following code to enter password and exit
							exitAppsPasswordChecking();
						}
                        // else run the following code to enter password and exit
                    }
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

	private void exitAppsPasswordChecking() {
		// Enter password to exit
		if (GlobalFunction.isFDMS_BASE24Installed(getPackageManager())) {
			dialog = new AlertDialog.Builder(this);
			dialog.setTitle(getString(R.string.input_PW));

			// EditText
			final EditText inputPassword = new EditText(Login.this);
			inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			inputPassword.setHint(getString(R.string.password));

			dialog.setView(inputPassword);
			dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String password = inputPassword.getText().toString().trim();
					if (password.equals(Constants.admin_PW)) {
						// Correct password
						GlobalFunction.enablePaxNavigationButton(Login.this);
						android.os.Process.killProcess(android.os.Process.myPid());
						finish();
					} else {
						// Wrong password
						Toast.makeText(Login.this, R.string.error3, Toast.LENGTH_SHORT).show();
					}
				}
			});

			alertDialog = dialog.create();
			alertDialog.show();
		}
	}
}