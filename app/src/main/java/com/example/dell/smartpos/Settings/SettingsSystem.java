package com.example.dell.smartpos.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.GlobalFunction;
import com.example.dell.smartpos.Printer.BluetoothSelect;
import com.example.dell.smartpos.R;

import java.util.Locale;

public class SettingsSystem extends AppCompatActivity {

    LinearLayout settingPrinter;
    LinearLayout versionNo;

    TextView connectedDevice;
    TextView sposVersion;

    //---------for printer---------//
    String printeraddress = "";
    String printername = "";
    //---------for printer---------//

    SharedPreferences pref;

    String deviceMan = android.os.Build.MANUFACTURER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_system);

        setTitle(R.string.settings_system);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize layout
        settingPrinter = (LinearLayout) findViewById(R.id.setting_printer);
        versionNo = (LinearLayout) findViewById(R.id.version_no);

        connectedDevice = (TextView) findViewById(R.id.connectedDevice);
        sposVersion = (TextView) findViewById(R.id.sposVersion);

        // Initialize settings
        pref = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);

        /** Hide the printer setting for PAX Terminal **/
        if (deviceMan.equalsIgnoreCase("PAX")) {
            settingPrinter.setVisibility(View.GONE);
        }

        // Check language
        GlobalFunction.changeLanguage(SettingsSystem.this);
        sposVersion.setText(Constants.current_VersionCode);

        // Initialize printer
        printeraddress = pref.getString(Constants.pref_printer_address, "");
        printername = pref.getString(Constants.pref_printer_name, "");
        if ("".equals(printeraddress) || printeraddress == null) {
            printeraddress = "";
        }
        if ("".equals(printername) || printername == null) {
            printername = "";
        }
        connectedDevice.setText(printername);

        // onCLick printer
        settingPrinter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SettingsSystem.this, BluetoothSelect.class);
                startActivity(intent);
            }
        });
    }

    //--Edited 25/07/18 by KJ--//
    @Override
    public void onResume() {
        super.onResume();

        printername = pref.getString(Constants.pref_printer_name, "");

        if ("".equals(printername) || printername == null) {
            printername = "";
        }
        connectedDevice.setText(printername);
    }
    //--done Edited 25/07/18 by KJ--//

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
