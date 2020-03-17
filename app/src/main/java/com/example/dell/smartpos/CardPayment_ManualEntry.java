package com.example.dell.smartpos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dell.smartpos.CardModule.tradepaypw.TradeResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class CardPayment_ManualEntry extends AppCompatActivity {

    String amount;
    String currCode;
    String pMethod;
    String merRef;
    String merID;
    String payType;
    String operator;
    String merName;

    private EditText edtCardNo;
    private EditText edtCVV;
    ImageView cardType;
    boolean seccode;

    String[] monthArray = { "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12" };
    String[] yearArray = new String[12];
    Spinner spinnerMonth;
    Spinner spinnerYear;
    private String epMonth;
    private String epYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment__manual_entry);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        edtCardNo = (EditText) findViewById(R.id.edtCardno);
        edtCVV = (EditText) findViewById(R.id.edtCVV);
//        edtCardHolder = (EditText) findViewById(R.id.edtCardname);
        cardType = (ImageView) findViewById(R.id.imgCardType);

        Intent paymentIntent = getIntent();

        amount = paymentIntent.getStringExtra("amount");
        currCode = paymentIntent.getStringExtra("currCode");
        merRef = paymentIntent.getStringExtra("merRef");
        merID = paymentIntent.getStringExtra("merID");
        payType = paymentIntent.getStringExtra("payType") == null ? "N" : paymentIntent.getStringExtra("payType");
        operator = paymentIntent.getStringExtra("operator") == null ? "" : paymentIntent.getStringExtra("operator");
        merName = paymentIntent.getStringExtra("merName") == null ? "" : paymentIntent.getStringExtra("merName");

        //Spinner Month
        ArrayList<String> month = new ArrayList<String>();
        for (int i = 0; i < monthArray.length; i++) {
            month.add(monthArray[i]);
        }

        ArrayAdapter<String> adapMonth = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_dropdown_layout, monthArray);
        adapMonth.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spinnerMonth = (Spinner) findViewById(R.id.spinner1);
        spinnerMonth.setAdapter(adapMonth);
        spinnerMonth.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int position, long id) {
                epMonth = adapterView.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        //Spinner Year
        spinnerYear = (Spinner) findViewById(R.id.spinner2);
        String yearLast = new SimpleDateFormat("yyyy", Locale.CHINESE).format(Calendar.getInstance().getTime());
        for(int i=0;i<12;i++){
            yearArray[i]= String.valueOf((Integer.parseInt(yearLast))+i);
        }

        ArrayAdapter<String> adapYear = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_dropdown_layout, yearArray);
        adapYear.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spinnerYear.setAdapter(adapYear);
        spinnerYear.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int position, long id) {
                epYear = adapterView.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        //Button CLEAR
        Button clear = (Button) findViewById(R.id.btn_clr);
        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clearFields();
            }
        });

        //Button SUBMIT
        Button submit = (Button) findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CardSchemeValidation validation = new CardSchemeValidation(CardPayment_ManualEntry.this);
                boolean result = validation.validateCardScheme(payMethod(edtCardNo.getText().toString()), payType);

                if(result){
                    submit();
                }

            }
        });

        edtCardNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                payMethod(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });
    }

    public void submit() {
        // Check for missing field/s
        if (edtCardNo.getText().toString().equals("")
                || edtCVV.getText().toString().equals("")
                && seccode) {
            Toast.makeText(getApplicationContext(), "Please fill all fields ",
                    Toast.LENGTH_LONG).show();
        }else if (edtCardNo.getText().toString().equals("") && !seccode) {
            Toast.makeText(getApplicationContext(), "Please fill all fields ",
                    Toast.LENGTH_LONG).show();
        } else {

            pMethod = payMethod(edtCardNo.getText().toString());
            if(!pMethod.equals("")){
                String securityCode= "";
                if(seccode){
                    securityCode = edtCVV.getText().toString();
                }

                makePayment();

            }else {
                Toast.makeText(getApplicationContext(),
                        "Please input correct card number",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void clearFields() {
        edtCardNo.setText("");
        edtCVV.setText("");
    }

    public String payMethod(String cardno) {

        String pMethod = "";
        String a = cardno;
        int c = a.length();

        if(Mod10.mod10(cardno)){

            if (c == 13) {
                int prefix = Integer.parseInt(a.substring(0, 4));
                if (prefix >= 4000 && prefix <= 4999) {
                    pMethod = "VISA";
                    cardType.setImageResource(R.drawable.ic_visa);
                } else {
                    pMethod = "";
                    cardType.setImageDrawable(null);
                }
            } else if (c == 14) {
                int prefix = Integer.parseInt(a.substring(0, 4));
                if (prefix >= 3000 && prefix <= 3050 || prefix >= 3600) {
                    pMethod = "Diners";
                    cardType.setImageResource(R.drawable.ic_discover);
                } else {
                    pMethod = "";
                    cardType.setImageDrawable(null);
                }
            } else if (c == 15) {
                int prefix = Integer.parseInt(a.substring(0, 4));
                if (prefix >= 3400 && prefix <= 3499 || prefix >= 3700
                        && prefix <= 3799) {
                    pMethod = "AMEX";
                    cardType.setImageResource(R.drawable.ic_amex);
                } else if (prefix == 2014 || prefix == 2149) {
                    pMethod = "Diners";
                    cardType.setImageResource(R.drawable.ic_discover);
                }
            } else if (c == 16) {
                int prefix = Integer.parseInt(a.substring(0, 4));
                if (prefix >= 3528 && prefix <= 3589) {
                    pMethod = "JCB";
                    cardType.setImageResource(R.drawable.ic_jcb);
                } else if (prefix >= 4000 && prefix <= 4999) {
                    pMethod = "VISA";
                    cardType.setImageResource(R.drawable.ic_visa);

                } else if (prefix >= 5100 && prefix <= 5599) {
                    pMethod = "Master";
                    cardType.setImageResource(R.drawable.ic_master);

                } else if (prefix >= 6200 && prefix <= 6299) {
                    int prefix2 = Integer.parseInt(a.substring(0, 6));
                    if (prefix2 >= 622126 && prefix2 <= 622925) {
                        pMethod = "Diners";
                        cardType.setImageResource(R.drawable.ic_discover);
                    } else {
                        pMethod = "CHINAPAY";
                        cardType.setImageResource(R.drawable.ic_cup);
                    }
                } else if (prefix == 6011 || prefix >= 6440 && prefix <= 6599) {
                    pMethod = "Diners";
                    cardType.setImageResource(R.drawable.ic_discover);
                }

            } else if (c == 17) {
                int prefix = Integer.parseInt(a.substring(0, 4));
                if (prefix >= 6200 && prefix <= 6299) {
                    pMethod = "CHINAPAY";
                    cardType.setImageResource(R.drawable.ic_cup);
                } else {
                    pMethod = "";
                    cardType.setImageDrawable(null);
                }
            } else if (c == 18) {
                int prefix = Integer.parseInt(a.substring(0, 4));
                if (prefix >= 6200 && prefix <= 6299) {
                    pMethod = "CHINAPAY";
                    cardType.setImageResource(R.drawable.ic_cup);
                } else {
                    pMethod = "";
                    cardType.setImageDrawable(null);
                }
            } else if (c == 19) {
                int prefix = Integer.parseInt(a.substring(0, 4));
                if (prefix >= 6200 && prefix <= 6299) {
                    pMethod = "CHINAPAY";
                    cardType.setImageResource(R.drawable.ic_cup);
                } else {
                    pMethod = "";
                    cardType.setImageDrawable(null);
                }
            } else {
                pMethod = "";
                cardType.setImageDrawable(null);
            }

        }else{
            pMethod = "";
            cardType.setImageDrawable(null);
        }

        return pMethod;
    }

    private void makePayment(){
        /**********************
         ** After Mag Success **
         ***********************/
        HashMap<String, String> map = new HashMap();
        map.put(Constants.CARDNO, edtCardNo.getText().toString());
        map.put(Constants.EXPMONTH, spinnerMonth.getSelectedItem().toString());
        map.put(Constants.EXPYEAR, spinnerYear.getSelectedItem().toString());
        map.put(Constants.AMOUNT, amount);
        map.put(Constants.CURRCODE, currCode);
        map.put(Constants.MERID, merID);
        map.put(Constants.MERNAME, merName);
        map.put(Constants.MERCHANT_REF, merRef);
        map.put(Constants.PAYTYPE, payType);
        map.put(Constants.ENTRYMODE, "M");
        Log.d("SwingCardEMVData", amount);
//        Log.i(TAG, "Start MANUAL TradeResult");
        TradeResult tradeResult = new TradeResult(CardPayment_ManualEntry.this, map);
        tradeResult.initData();
    }

    public String getPrefPayGate() {
        SharedPreferences prefsettings = getApplicationContext()
                .getSharedPreferences("Settings", MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate,
                Constants.default_paygate);
        return prefpaygate;
    }
}
