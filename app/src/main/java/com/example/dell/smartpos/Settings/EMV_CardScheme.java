package com.example.dell.smartpos.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;
import com.example.dell.smartpos.Settings.BinRange.BinRange;

public class EMV_CardScheme extends AppCompatActivity {
    private Button btnOK;
    private CheckBox cbVisa;
    private CheckBox cbMC;
    private CheckBox cbCUP;
    private CheckBox cbAMEX;
    private CheckBox cbJCB;
    private CheckBox cbDiners;

    private static String temp_card_scheme = "";

    public EMV_CardScheme() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_card_scheme);
        setTitle(R.string.card_scheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cbVisa = (CheckBox) findViewById(R.id.cbVisa);
        cbMC = (CheckBox) findViewById(R.id.cbMC);
        cbCUP = (CheckBox) findViewById(R.id.cbCUP);
        cbAMEX = (CheckBox) findViewById(R.id.cbAMEX);
        cbJCB = (CheckBox) findViewById(R.id.cbJCB);
        cbDiners = (CheckBox) findViewById(R.id.cbDiners);

        LinearLayout visa_layout = (LinearLayout) findViewById(R.id.visa_layout);
        LinearLayout mc_layout = (LinearLayout) findViewById(R.id.mc_layout);
        LinearLayout chinaUnionPay_layout = (LinearLayout) findViewById(R.id.chinaUnionPay_layout);
        LinearLayout amex_layout = (LinearLayout) findViewById(R.id.amex_layout);
        LinearLayout jcb_layout = (LinearLayout) findViewById(R.id.jcb_layout);
        LinearLayout diners_layout = (LinearLayout) findViewById(R.id.diners_layout);

        btnOK = (Button) findViewById(R.id.btnOK);

        initView();

        if (cbVisa.isChecked()) {
            temp_card_scheme += "-visa-";
        } else {
            if (temp_card_scheme.contains("-visa-")) {
                temp_card_scheme.replaceAll("-visa-", "");
            }
        }

        if (cbMC.isChecked()) {
            temp_card_scheme += "-mastercard-";
        } else {
            if (temp_card_scheme.contains("-mastercard-")) {
                temp_card_scheme.replaceAll("-mastercard-", "");
            }
        }

        if (cbCUP.isChecked()) {
            temp_card_scheme += "-cup-";
        } else {
            if (temp_card_scheme.contains("-cup-")) {
                temp_card_scheme.replaceAll("-cup-", "");
            }
        }

        if (cbAMEX.isChecked()) {
            temp_card_scheme += "-amex-";
        } else {
            if (temp_card_scheme.contains("-amex-")) {
                temp_card_scheme.replaceAll("-amex-", "");
            }
        }

        if (cbJCB.isChecked()) {
            temp_card_scheme += "-jcb-";
        } else {
            if (temp_card_scheme.contains("-jcb-")) {
                temp_card_scheme.replaceAll("-jcb-", "");
            }
        }

        if (cbDiners.isChecked()) {
            temp_card_scheme += "-diners-";
        } else {
            if (temp_card_scheme.contains("-diners-")) {
                temp_card_scheme.replaceAll("-diners-", "");
            }
        }

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);
        final SharedPreferences.Editor edit = prefsettings.edit();

        //When Click Actions
        visa_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (cbVisa.isChecked()) {
                    edit.putString(Constants.card_scheme, temp_card_scheme);
                    edit.apply();
                    final Intent intent = new Intent(EMV_CardScheme.this, BinRange.class);
                    intent.putExtra("cardScheme", "visa");
                    startActivity(intent);
                }
            }
        });

        mc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cbMC.isChecked()) {
                    edit.putString(Constants.card_scheme, temp_card_scheme);
                    edit.apply();
                    final Intent intent = new Intent(EMV_CardScheme.this, BinRange.class);
                    intent.putExtra("cardScheme", "mc");
                    startActivity(intent);
                }
            }
        });

        chinaUnionPay_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (cbCUP.isChecked()) {
                    edit.putString(Constants.card_scheme, temp_card_scheme);
                    edit.apply();
                    final Intent intent = new Intent(EMV_CardScheme.this, BinRange.class);
                    intent.putExtra("cardScheme", "cup");
                    startActivity(intent);
                }
            }
        });

        amex_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (cbAMEX.isChecked()) {
                    edit.putString(Constants.card_scheme, temp_card_scheme);
                    edit.apply();
                    final Intent intent = new Intent(EMV_CardScheme.this, BinRange.class);
                    intent.putExtra("cardScheme", "amex");
                    startActivity(intent);
                }
            }
        });

        jcb_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cbJCB.isChecked()) {
                    edit.putString(Constants.card_scheme, temp_card_scheme);
                    edit.apply();
                    final Intent intent = new Intent(EMV_CardScheme.this, BinRange.class);
                    intent.putExtra("cardScheme", "jcb");
                    startActivity(intent);
                }
            }
        });

        diners_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (cbDiners.isChecked()) {
                    edit.putString(Constants.card_scheme, temp_card_scheme);
                    edit.apply();
                    final Intent intent = new Intent(EMV_CardScheme.this, BinRange.class);
                    intent.putExtra("cardScheme", "diners");
                    startActivity(intent);
                }
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                temp_card_scheme = "";
                if (cbVisa.isChecked()) {
                    temp_card_scheme = "-visa-";
                } else {
                    temp_card_scheme = "";
                }

                if (cbMC.isChecked()) {
                    temp_card_scheme += "-mastercard-";
                } else {
                    temp_card_scheme += "";
                }

                if (cbCUP.isChecked()) {
                    temp_card_scheme += "-cup-";
                } else {
                    temp_card_scheme += "";
                }

                if (cbAMEX.isChecked()) {
                    temp_card_scheme += "-amex-";
                } else {
                    temp_card_scheme += "";
                }

                if (cbJCB.isChecked()) {
                    temp_card_scheme += "-jcb-";
                } else {
                    temp_card_scheme += "";
                }

                if (cbDiners.isChecked()) {
                    temp_card_scheme += "-diners-";
                } else {
                    temp_card_scheme += "";
                }

                edit.putString(Constants.card_scheme, temp_card_scheme);
                edit.commit();

                finish();
            }

        });
    }

    private void initView() {
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);

        String card_scheme = prefsettings.getString(Constants.card_scheme, Constants.default_card_scheme).toLowerCase();

        if (card_scheme.contains("-visa-")) {
            cbVisa.setChecked(true);
        } else {
            cbVisa.setChecked(false);
        }

        if (card_scheme.contains("-mastercard-")) {
            cbMC.setChecked(true);
        } else {
            cbMC.setChecked(false);
        }

        if (card_scheme.contains("-cup-")) {
            cbCUP.setChecked(true);
        } else {
            cbCUP.setChecked(false);
        }

        if (card_scheme.contains("-amex-")) {
            cbAMEX.setChecked(true);
        } else {
            cbAMEX.setChecked(false);
        }

        if (card_scheme.contains("-jcb-")) {
            cbJCB.setChecked(true);
        } else {
            cbJCB.setChecked(false);
        }

        if (card_scheme.contains("-diners-")) {
            cbDiners.setChecked(true);
        } else {
            cbDiners.setChecked(false);
        }
    }


    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
