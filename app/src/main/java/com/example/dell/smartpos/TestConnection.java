package com.example.dell.smartpos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestConnection extends AppCompatActivity {

    boolean connected = false;
    int info = 0;
    String type = "";

    private Button checkIntrnetButton;
    private TextView signalMessage;
    private TextView signalLabel;
    private TextView signalStatus;
    private ImageView imageView;
    private LinearLayout signalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_connection);
        this.setTitle(R.string.testConnection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkIntrnetButton = (Button)findViewById(R.id.checkInternetButton);
        signalMessage = (TextView)findViewById(R.id.signalMessage);
        signalLabel = (TextView)findViewById(R.id.signalLabel);
        signalStatus = (TextView)findViewById(R.id.signalStatus);
        imageView = (ImageView)findViewById(R.id.signalImg);
        signalLayout = (LinearLayout)findViewById(R.id.signalLayout);

        checkIntrnetButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                checkConnection();

                String signal = "";
                if(connected){
                    if(type.equals("WIFI")){
                        signal = wifiInternetStrength();

                        //signal ="No Internet";
                        if(signal.equalsIgnoreCase("Excellent") ||
                                signal.equalsIgnoreCase("Good")){
                            imageView.setImageResource(R.drawable.excellent_signal);
                            signalMessage.setText(R.string.internetConnected);
                            signalLayout.setVisibility(View.VISIBLE);
                            signalStatus.setText(R.string.excellent);
                            signalStatus.setTextColor(getResources().getColor(R.color.green));
                        }else if(signal.equalsIgnoreCase("Moderate")){
                            imageView.setImageResource(R.drawable.moderate);
                            signalMessage.setText(R.string.internetConnected);
                            signalLayout.setVisibility(View.VISIBLE);
                            signalStatus.setText(R.string.moderate);
                            signalStatus.setTextColor(getResources().getColor(R.color.orange));
                        }
                        else if(signal.equalsIgnoreCase("Poor")){
                            imageView.setImageResource(R.drawable.weak);
                            signalMessage.setText(R.string.internetConnected);
                            signalLayout.setVisibility(View.VISIBLE);
                            signalStatus.setText(R.string.poor);
                            signalStatus.setTextColor(getResources().getColor(R.color.red));
                        }
                        else if(signal.equalsIgnoreCase("No Internet")){
                            imageView.setImageResource(R.drawable.no_signal);
                            signalMessage.setText(R.string.notConnected);
                            signalMessage.setTextColor(getResources().getColor(R.color.red));
                        }

                    }else{
                        signal = mobileNetwork(info);

                        //signal ="No Internet";
                        if(signal.equalsIgnoreCase("Excellent")){
                            imageView.setImageResource(R.drawable.excellent_signal);
                            signalMessage.setText(R.string.internetConnected);
                            signalLayout.setVisibility(View.VISIBLE);
                            signalStatus.setText(R.string.excellent);
                            signalStatus.setTextColor(getResources().getColor(R.color.green));
                        }else if(signal.equalsIgnoreCase("Moderate")){
                            imageView.setImageResource(R.drawable.moderate);
                            signalMessage.setText(R.string.internetConnected);
                            signalLayout.setVisibility(View.VISIBLE);
                            signalStatus.setText(R.string.moderate);
                            signalStatus.setTextColor(getResources().getColor(R.color.orange));
                        }
                        else if(signal.equalsIgnoreCase("Poor")){
                            imageView.setImageResource(R.drawable.weak);
                            signalMessage.setText(R.string.internetConnected);
                            signalLayout.setVisibility(View.VISIBLE);
                            signalStatus.setText(R.string.poor);
                            signalStatus.setTextColor(getResources().getColor(R.color.red));
                        }
                        else if(signal.equalsIgnoreCase("No Internet")){
                            imageView.setImageResource(R.drawable.no_signal);
                            signalMessage.setText(R.string.notConnected);
                            signalLayout.setVisibility(View.GONE);
                            signalMessage.setTextColor(getResources().getColor(R.color.red));
                        }
                    }
                }else{
                    imageView.setImageResource(R.drawable.no_signal);
                    signalMessage.setText(R.string.notConnected);
                    signalLayout.setVisibility(View.GONE);
                    signalMessage.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });
    }

    public String wifiInternetStrength(){
        final WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 5;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);

        String signal ="";

        switch (level)
        {
            case 0:
                //Log.d("LEVEL","None");
                signal = "No Internet";
                break;

            case 1:
                //Log.d("LEVEL","None");
                signal = "No Internet";
                break;

            case 2:
                //Log.d("LEVEL","Poor");
                signal = "Poor";
                break;

            case 3:
                //Log.d("LEVEL","Moderate");
                signal = "Moderate";
                break;

            case 4:

                //Log.d("LEVEL","Good");
                signal = "Good";
                break;

            case 5:

                //Log.d("LEVEL","Excellent");
                signal = "Excellent";
                break;
        }

        return signal;
    }

    public String  mobileNetwork(int info){

        String signal ="";
        switch(info){
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                // return false; // ~ 50-100 kbps
                signal = "Poor";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                //  return false; // ~ 14-64 kbps
                signal = "Poor";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                // return false; // ~ 50-100 kbps
                signal = "Poor";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                // return true; // ~ 400-1000 kbps
                signal = "Moderate";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                // return true; // ~ 600-1400 kbps
                signal = "Moderate";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                // return false; // ~ 100 kbps
                signal = "Poor";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                // return true; // ~ 2-14 Mbps
                signal = "Excellent";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                //return true; // ~ 700-1700 kbps
                signal = "Moderate";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                // return true; // ~ 1-23 Mbps
                signal = "Excellent";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                //return true; // ~ 400-7000 kbps
                signal = "Moderate";
                break;
            /*
             * Above API level 7, make sure to set android:targetSdkVersion
             * to appropriate level to use these
             */
            case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                //  return true; // ~ 1-2 Mbps
                signal = "Excellent";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                // return true; // ~ 5 Mbps
                signal = "Excellent";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                // return true; // ~ 10-20 Mbps
                signal = "Excellent";
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                // return false; // ~25 kbps
                signal = "Poor";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                // return true; // ~ 10+ Mbps
                signal = "Excellent";
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                signal = "No Internet";
        }

        return signal;
    }


    public void checkConnection(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network

            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            info = activeNetworkInfo.getSubtype();
            type = activeNetworkInfo.getTypeName();

            connected = true;
        }
        else {
            connected = false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
