package com.example.dell.smartpos.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;

public class EMV_CardDataReadAttempt extends AppCompatActivity {

    private Button btnAddChipAttempt;
    private Button btnAddContactlessAttempt;
    private Button btnMinusChipAttempt;
    private Button btnMinusContactlessAttempt;

    private TextView tv_chip;
    private TextView tv_contactless;

    private Button btnOK;

    private static String chipAttempt = "";
    private static String contactlessAttempt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_card_data_read_attempt);
        this.setTitle(R.string.read_emv_card_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAddChipAttempt = (Button) findViewById(R.id.addAttemptChip);
        btnMinusChipAttempt = (Button) findViewById(R.id.minusAttemptChip);
        btnAddContactlessAttempt = (Button) findViewById(R.id.addAttemptContactless);
        btnMinusContactlessAttempt = (Button) findViewById(R.id.minusAttemptContactless);

        btnOK = (Button) findViewById(R.id.btnOK);

        tv_chip = (TextView) findViewById(R.id.tv_chip);
        tv_contactless = (TextView) findViewById(R.id.tv_contactless);

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);
        final SharedPreferences.Editor edit = prefsettings.edit();

        chipAttempt = Integer.toString(prefsettings.getInt("chip_attempt", Constants.default_chip_attempt));
        contactlessAttempt = Integer.toString(prefsettings.getInt("contactless_attempt", Constants.default_contactless_attempt));
        tv_chip.setText(chipAttempt);
        tv_contactless.setText(contactlessAttempt);

        btnAddChipAttempt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String qtyString = tv_chip.getText().toString();
                int qty = Integer.parseInt(qtyString) + 1;

                if (qtyString.equals("5")) {
                    tv_chip.setText(Integer.toString(5));
                } else {
                    tv_chip.setText(Integer.toString(qty));
                }
            }

        });

        btnMinusChipAttempt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String qtyString = tv_chip.getText().toString();
                int qty = Integer.parseInt(qtyString) - 1;

                if (qtyString.equals("1")) {
                    tv_chip.setText(Integer.toString(1));
                } else {
                    tv_chip.setText(Integer.toString(qty));
                }

            }

        });

        btnAddContactlessAttempt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String qtyString = tv_contactless.getText().toString();
                int qty = Integer.parseInt(qtyString) + 1;

                if (qtyString.equals("5")) {
                    tv_contactless.setText(Integer.toString(5));
                } else {
                    tv_contactless.setText(Integer.toString(qty));
                }
            }

        });

        btnMinusContactlessAttempt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String qtyString = tv_contactless.getText().toString();
                int qty = Integer.parseInt(qtyString) - 1;

                if (qtyString.equals("1")) {
                    tv_contactless.setText(Integer.toString(1));
                } else {
                    tv_contactless.setText(Integer.toString(qty));
                }
            }

        });

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int chip_attempt = 0;
                int contactless_attempt = 0;

                chip_attempt = Integer.parseInt(tv_chip.getText().toString());
                contactless_attempt = Integer.parseInt(tv_contactless.getText().toString());

                edit.putInt("chip_attempt",chip_attempt);
                edit.putInt("contactless_attempt",contactless_attempt);
                edit.apply();

                finish();
            }

        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
