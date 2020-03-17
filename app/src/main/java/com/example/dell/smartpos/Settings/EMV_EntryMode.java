package com.example.dell.smartpos.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;

public class EMV_EntryMode extends AppCompatActivity {

    private Button btnOkEntryMode;
    private CheckBox cbChip;
    private CheckBox cbSwipe;
    private CheckBox cbManualKeyIn;
    private CheckBox cbFallback;
    private CheckBox cbContactless;

    private static String temp_entry_mode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_entry_mode);
        this.setTitle(R.string.entry_mode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cbChip = (CheckBox) findViewById(R.id.cbChip);
        cbSwipe = (CheckBox) findViewById(R.id.cbSwipe);
        cbManualKeyIn = (CheckBox) findViewById(R.id.cbManualKeyIn);
        cbFallback = (CheckBox) findViewById(R.id.cbFallback);
        cbContactless = (CheckBox) findViewById(R.id.cbContactless);

        btnOkEntryMode = (Button) findViewById(R.id.btnOkEntryMode);

        initView();

        if (cbChip.isChecked()) {
            temp_entry_mode += "-chip-";
        } else {
            if (temp_entry_mode.contains("-chip-")) {
                temp_entry_mode.replaceAll("-chip-", "");
            }
        }

        if (cbSwipe.isChecked()) {
            temp_entry_mode += "-swipe-";
        } else {
            if (temp_entry_mode.contains("-swipe-")) {
                temp_entry_mode.replaceAll("-swipe-", "");
            }
        }

        if (cbManualKeyIn.isChecked()) {
            temp_entry_mode += "-manual_key_in-";
        } else {
            if (temp_entry_mode.contains("-manual_key_in-")) {
                temp_entry_mode.replaceAll("-manual_key_in-", "");
            }
        }

        if (cbFallback.isChecked()) {
            temp_entry_mode += "-fallback-";
        } else {
            if (temp_entry_mode.contains("-fallback-")) {
                temp_entry_mode.replaceAll("-fallback-", "");
            }
        }

        if (cbContactless.isChecked()) {
            temp_entry_mode += "-contactless-";
        } else {
            if (temp_entry_mode.contains("-contactless-")) {
                temp_entry_mode.replaceAll("-contactless-", "");
            }
        }

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);
        final SharedPreferences.Editor edit = prefsettings.edit();

        btnOkEntryMode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (cbChip.isChecked()) {
                    temp_entry_mode = "-chip-";
                } else {
                    temp_entry_mode = "";
                }

                if (cbSwipe.isChecked()) {
                    temp_entry_mode += "-swipe-";
                } else {
                    temp_entry_mode += "";
                }

                if (cbManualKeyIn.isChecked()) {
                    temp_entry_mode += "-manual_key_in-";
                } else {
                    temp_entry_mode += "";
                }

                if (cbFallback.isChecked()) {
                    temp_entry_mode += "-fallback-";
                } else {
                    temp_entry_mode += "";
                }

                if (cbContactless.isChecked()) {
                    temp_entry_mode += "-contactless-";
                } else {
                    temp_entry_mode += "";
                }

                edit.putString(Constants.entry_mode, temp_entry_mode);
                edit.apply();

                finish();
            }

        });
    }

    private void initView() {
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);

        if (prefsettings.getString(Constants.entry_mode, "").toLowerCase().contains("-chip-")) {
            cbChip.setChecked(true);
        } else {
            cbChip.setChecked(false);
        }

        if (prefsettings.getString(Constants.entry_mode, "").toLowerCase().contains("-swipe-")) {
            cbSwipe.setChecked(true);
        } else {
            cbSwipe.setChecked(false);
        }

        if (prefsettings.getString(Constants.entry_mode, "").toLowerCase().contains("-manual_key_in-")) {
            cbManualKeyIn.setChecked(true);
        } else {
            cbManualKeyIn.setChecked(false);
        }

        if (prefsettings.getString(Constants.entry_mode, "").toLowerCase().contains("-fallback-")) {
            cbFallback.setChecked(true);
        } else {
            cbFallback.setChecked(false);
        }

        if (prefsettings.getString(Constants.entry_mode, "").toLowerCase().contains("-contactless-")) {
            cbContactless.setChecked(true);
        } else {
            cbContactless.setChecked(false);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
