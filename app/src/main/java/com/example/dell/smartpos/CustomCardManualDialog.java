package com.example.dell.smartpos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class CustomCardManualDialog extends Dialog {

    private HashMap<String, String> map;

    String amount;
    String currCode;
    String pMethod;
    String merRef;
    String merID;
    String payType;
    String operator;
    String merName;
    String mdrAmount;
    String surcharge;
    String hideSurcharge;

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

    public CustomCardManualDialog(Context context, HashMap <String, String> map) {
        super(context);

        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }

        this.map = map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_card_manual_dialog);

        edtCardNo = (EditText) findViewById(R.id.edtCardno);
        edtCVV = (EditText) findViewById(R.id.edtCVV);
//        edtCardHolder = (EditText) findViewById(R.id.edtCardname);
        cardType = (ImageView) findViewById(R.id.imgCardType);

        amount = map.get(Constants.AMOUNT);
        currCode = map.get(Constants.CURRCODE);
        merRef = map.get(Constants.MERCHANT_REF);
        merID = map.get(Constants.MERID);
        payType = map.get(Constants.PAYTYPE) == null ? "N" : map.get(Constants.PAYTYPE);
        operator = map.get(Constants.OPERATORID) == null ? "" : map.get(Constants.OPERATORID);
        merName = map.get(Constants.MERNAME) == null ? "" : map.get(Constants.MERNAME);
        mdrAmount = map.get(Constants.PRE_MDR_AMOUNT);
        surcharge = map.get(Constants.PRE_SURCHARGE);
        hideSurcharge = map.get(Constants.pref_hideSurcharge);

        //Spinner Month
        ArrayList<String> month = new ArrayList<String>();
        for (int i = 0; i < monthArray.length; i++) {
            month.add(monthArray[i]);
        }

        ArrayAdapter<String> adapMonth = new ArrayAdapter<String>(
                getContext(), R.layout.spinner_dropdown_layout, monthArray);
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

        ArrayAdapter<String> adapYear = new ArrayAdapter<String>(getContext(),
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

                submit();
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
            Toast.makeText(getContext(), "Please fill all fields ",
                    Toast.LENGTH_LONG).show();
        }else if (edtCardNo.getText().toString().equals("") && !seccode) {
            Toast.makeText(getContext(), "Please fill all fields ",
                    Toast.LENGTH_LONG).show();
        } else {

            pMethod = payMethod(edtCardNo.getText().toString());
            if(!pMethod.equals("")){
                String securityCode= "";
                if(seccode){
                    securityCode = edtCVV.getText().toString();
                }

                CardSchemeValidation validation = new CardSchemeValidation(getOwnerActivity());
                boolean result = validation.validateCardScheme(payMethod(edtCardNo.getText().toString()), payType);

                if(result){

                    map.put(Constants.CARDNO, edtCardNo.getText().toString());
                    map.put(Constants.EXPMONTH, spinnerMonth.getSelectedItem().toString());
                    map.put(Constants.EXPYEAR, spinnerYear.getSelectedItem().toString());
                    map.put(Constants.PAYMETHOD, pMethod);
                    map.put(Constants.AMOUNT, amount);
                    map.put(Constants.CURRCODE, currCode);
                    map.put(Constants.MERID, merID);
                    map.put(Constants.PAYTYPE, payType);
                    map.put(Constants.CURRCODE, currCode);
                    map.put(Constants.OPERATORID, operator);
                    map.put(Constants.MERNAME, merName);
                    map.put(Constants.PRE_MDR_AMOUNT, mdrAmount);
                    map.put(Constants.SURCHARGE, surcharge);

                    CustomConfirmationDialog dialog = new CustomConfirmationDialog(getOwnerActivity(), map);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }

            }else {
                Toast.makeText(getContext(),
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


    public String getPrefPayGate() {
        SharedPreferences prefsettings = getContext()
                .getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate,
                Constants.default_paygate);
        return prefpaygate;
    }
}
