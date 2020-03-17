package com.example.dell.smartpos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogTenure extends Activity {

    Spinner spnTenure;

    String tenure = "";

    public String bank = "";

    private Button btnConfirm;
    private Button btnCancel;
    private TextView txtBank;

    String currCode;
    String amount;
    String merchantRef;

    List<String> listTenure = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_tenure);

        final Intent tenureIntent = getIntent();
        bank = tenureIntent.getStringExtra("bank");
        merchantRef = tenureIntent.getStringExtra(Constants.MERCHANT_REF);
        currCode = tenureIntent.getStringExtra("currCode");

        String[] tenureArray = {"Tenure"};

        if(bank.equals("Citi Bank")){
            tenureArray = new String [] {"3 Month ","6 Month","12 Month"};
            listTenure = new ArrayList<String>(Arrays.asList(tenureArray));
        }else if (bank.equals("Public Bank")){
            tenureArray = new String [] {"6 Month","12 Month"};
            listTenure = new ArrayList<String>(Arrays.asList(tenureArray));
        }else if(bank.equals("Maybank")){
            tenureArray = new String [] {"12 Month","24 Month"};
            listTenure = new ArrayList<String>(Arrays.asList(tenureArray));
        }

        spnTenure = (Spinner) findViewById(R.id.spnTenure);
        btnConfirm = (Button) findViewById(R.id.dialogConfirm);
        btnCancel = (Button) findViewById(R.id.dialogCancel);
        txtBank = (TextView) findViewById(R.id.txtBank);

        txtBank.setText(bank);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_layout, tenureArray);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnTenure.setAdapter(adapter);

        spnTenure.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        }) ;

        spnTenure.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
                //--Edited 25/07/18 by KJ--//
                if (position == 0) {
                    tenure = "0";
                } else if (position == 1) {
                    tenure = "1";
                } else if (position == 2) {
                    tenure = "2";
                } else if (position == 3) {
                    tenure = "3";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String installment = listTenure.get(Integer.parseInt(tenure));

                Intent intent = new Intent(DialogTenure.this, CardPayment_2.class);
                intent.putExtra(Constants.CURRCODE, currCode);
                intent.putExtra("paymentType", "installment");
                intent.putExtra("tenure", installment);
                intent.putExtra(Constants.MERCHANT_REF,merchantRef);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("amount", getIntent().getStringExtra("amount"));
                startActivityForResult(intent, 1);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              finish();
            }
        });

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
