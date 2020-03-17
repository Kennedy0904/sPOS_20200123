package com.example.dell.smartpos.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.CurrCode;
import com.example.dell.smartpos.R;

import java.text.DecimalFormat;

public class EMV_InputLimit extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btn0;
    private Button btnClear;
    private Button btnDot;
    private Button btnEnter;

    public TextView amount = null;
    public TextView currency = null;
    public TextView card_scheme = null;
    public TextView input_type = null;

    Toast numberpadtoast = null;

    boolean firstzero = true;
    boolean firstdot = true;
    boolean dotflag = false;
    boolean ischecktoggle = false;

    String pre_amount = "0";
    String currCode = "";
    String currCode1 = "";
    String cardScheme = "";
    String constants = "";

    double setLimit;
    double floorLimit;
    double cvmLimit;
    double trxLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_input_limit);
        this.setTitle(R.string.contactless_limit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn0 = (Button) findViewById(R.id.button0);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        btn5 = (Button) findViewById(R.id.button5);
        btn6 = (Button) findViewById(R.id.button6);
        btn7 = (Button) findViewById(R.id.button7);
        btn8 = (Button) findViewById(R.id.button8);
        btn9 = (Button) findViewById(R.id.button9);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnDot = (Button) findViewById(R.id.buttonDot);

        currency = (TextView) findViewById(R.id.currency);
        amount = (TextView) findViewById(R.id.edtAmount);
        card_scheme = (TextView) findViewById(R.id.card_scheme);
        input_type = (TextView) findViewById(R.id.input_type);

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

        currCode1 = prefsettings.getString(Constants.CURRCODE, "");

        currCode = CurrCode.getName(currCode1);
        currency.setText(currCode);

        Intent intent = getIntent();
        cardScheme = intent.getStringExtra("cardScheme");
        final String limitType = intent.getStringExtra("limitType");

        System.out.println("-----------------" + cardScheme);

        card_scheme.setText(cardScheme);

        if (limitType.equalsIgnoreCase("floor")) {
            input_type.setText(R.string.input_floor_limit);
            System.out.println("--------------" + cardScheme);
//            constants = setConstant(limitType, cardScheme);
//            System.out.println("--------------" + constants);
        } else if (limitType.equalsIgnoreCase("cvm")) {
            input_type.setText(R.string.input_cvm_limit);
//            constants = setConstant(limitType, cardScheme);
        } else if (limitType.equalsIgnoreCase("txn")) {
            input_type.setText(R.string.input_txn_limit);
//            constants = setConstant(limitType, cardScheme);
        }

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs_checkdigit = checkDigit(pre_amount);
                if (rs_checkdigit == 1) {
                    showTextToast(getString(R.string.digit_over_front_and_back));
                } else if (rs_checkdigit == 2) {
                    showTextToast(getString(R.string.digit_over_back_two));
                } else if (rs_checkdigit == 3) {
                    showTextToast(getString(R.string.digit_over_front_ten));
                } else {
                    if (firstzero) {
                        pre_amount = "0";
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = true;
                    } else {

                        pre_amount = pre_amount.replaceAll(",", "");

                        if (pre_amount.substring(pre_amount.length() - 1).trim().equals(".")) {
                            pre_amount = pre_amount + "0";
                            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.0#");
                            pre_amount = formatter.format(Double.parseDouble(pre_amount));

                        } else if (pre_amount.substring(pre_amount.length() - 1).trim().equals("0") && pre_amount.contains(".")) {
                            pre_amount = pre_amount + "0";
                            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
                            pre_amount = formatter.format(Double.parseDouble(pre_amount));
                        } else {
                            pre_amount = pre_amount + "0";

                            if (pre_amount.contains(".")) {
                                DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
                                pre_amount = formatter.format(Double.parseDouble(pre_amount));
                            } else {
                                DecimalFormat formatter = new DecimalFormat("#,###,###,###.##");
                                pre_amount = formatter.format(Double.parseDouble(pre_amount));
                            }

                        }

                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    }
                }
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs_checkdigit = checkDigit(pre_amount);
                if (rs_checkdigit == 1) {
                    showTextToast(getString(R.string.digit_over_front_and_back));
                } else if (rs_checkdigit == 2) {
                    showTextToast(getString(R.string.digit_over_back_two));
                } else if (rs_checkdigit == 3) {
                    showTextToast(getString(R.string.digit_over_front_ten));
                } else {
                    if (firstzero) {
                        pre_amount = "1";
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    } else {
                        pre_amount = pre_amount + "1";
                        pre_amount = pre_amount.replaceAll(",", "");
                        DecimalFormat formatter = new DecimalFormat("#,###,###,###.##");
                        pre_amount = formatter.format(Double.parseDouble(pre_amount));
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    }
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs_checkdigit = checkDigit(pre_amount);
                if (rs_checkdigit == 1) {
                    showTextToast(getString(R.string.digit_over_front_and_back));
                } else if (rs_checkdigit == 2) {
                    showTextToast(getString(R.string.digit_over_back_two));
                } else if (rs_checkdigit == 3) {
                    showTextToast(getString(R.string.digit_over_front_ten));
                } else {
                    if (firstzero) {
                        pre_amount = "2";
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    } else {
                        pre_amount = pre_amount + "2";
                        pre_amount = pre_amount.replaceAll(",", "");
                        DecimalFormat formatter = new DecimalFormat("#,###,###,###.##");
                        pre_amount = formatter.format(Double.parseDouble(pre_amount));
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    }
                }

            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs_checkdigit = checkDigit(pre_amount);
                if (rs_checkdigit == 1) {
                    showTextToast(getString(R.string.digit_over_front_and_back));
                } else if (rs_checkdigit == 2) {
                    showTextToast(getString(R.string.digit_over_back_two));
                } else if (rs_checkdigit == 3) {
                    showTextToast(getString(R.string.digit_over_front_ten));
                } else {
                    if (firstzero) {
                        pre_amount = "3";
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    } else {
                        pre_amount = pre_amount + "3";
                        pre_amount = pre_amount.replaceAll(",", "");
                        DecimalFormat formatter = new DecimalFormat("#,###,###,###.##");
                        pre_amount = formatter.format(Double.parseDouble(pre_amount));
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    }
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs_checkdigit = checkDigit(pre_amount);
                if (rs_checkdigit == 1) {
                    showTextToast(getString(R.string.digit_over_front_and_back));
                } else if (rs_checkdigit == 2) {
                    showTextToast(getString(R.string.digit_over_back_two));
                } else if (rs_checkdigit == 3) {
                    showTextToast(getString(R.string.digit_over_front_ten));
                } else {
                    if (firstzero) {
                        pre_amount = "4";
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    } else {
                        pre_amount = pre_amount + "4";
                        pre_amount = pre_amount.replaceAll(",", "");
                        DecimalFormat formatter = new DecimalFormat("#,###,###,###.##");
                        pre_amount = formatter.format(Double.parseDouble(pre_amount));
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    }
                }
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs_checkdigit = checkDigit(pre_amount);
                if (rs_checkdigit == 1) {
                    showTextToast(getString(R.string.digit_over_front_and_back));
                } else if (rs_checkdigit == 2) {
                    showTextToast(getString(R.string.digit_over_back_two));
                } else if (rs_checkdigit == 3) {
                    showTextToast(getString(R.string.digit_over_front_ten));
                } else {
                    if (firstzero) {
                        pre_amount = "5";
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    } else {
                        pre_amount = pre_amount + "5";
                        pre_amount = pre_amount.replaceAll(",", "");
                        DecimalFormat formatter = new DecimalFormat("#,###,###,###.##");
                        pre_amount = formatter.format(Double.parseDouble(pre_amount));
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    }
                }
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs_checkdigit = checkDigit(pre_amount);
                if (rs_checkdigit == 1) {
                    showTextToast(getString(R.string.digit_over_front_and_back));
                } else if (rs_checkdigit == 2) {
                    showTextToast(getString(R.string.digit_over_back_two));
                } else if (rs_checkdigit == 3) {
                    showTextToast(getString(R.string.digit_over_front_ten));
                } else {
                    if (firstzero) {
                        pre_amount = "6";
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    } else {
                        pre_amount = pre_amount + "6";
                        pre_amount = pre_amount.replaceAll(",", "");
                        DecimalFormat formatter = new DecimalFormat("#,###,###,###.##");
                        pre_amount = formatter.format(Double.parseDouble(pre_amount));
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    }
                }
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs_checkdigit = checkDigit(pre_amount);
                if (rs_checkdigit == 1) {
                    showTextToast(getString(R.string.digit_over_front_and_back));
                } else if (rs_checkdigit == 2) {
                    showTextToast(getString(R.string.digit_over_back_two));
                } else if (rs_checkdigit == 3) {
                    showTextToast(getString(R.string.digit_over_front_ten));
                } else {
                    if (firstzero) {
                        pre_amount = "7";
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    } else {
                        pre_amount = pre_amount + "7";
                        pre_amount = pre_amount.replaceAll(",", "");
                        DecimalFormat formatter = new DecimalFormat("#,###,###,###.##");
                        pre_amount = formatter.format(Double.parseDouble(pre_amount));
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    }
                }
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs_checkdigit = checkDigit(pre_amount);
                if (rs_checkdigit == 1) {
                    showTextToast(getString(R.string.digit_over_front_and_back));
                } else if (rs_checkdigit == 2) {
                    showTextToast(getString(R.string.digit_over_back_two));
                } else if (rs_checkdigit == 3) {
                    showTextToast(getString(R.string.digit_over_front_ten));
                } else {
                    if (firstzero) {
                        pre_amount = "8";
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    } else {
                        pre_amount = pre_amount + "8";
                        pre_amount = pre_amount.replaceAll(",", "");
                        DecimalFormat formatter = new DecimalFormat("#,###,###,###.##");
                        pre_amount = formatter.format(Double.parseDouble(pre_amount));
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    }
                }
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs_checkdigit = checkDigit(pre_amount);
                if (rs_checkdigit == 1) {
                    showTextToast(getString(R.string.digit_over_front_and_back));
                } else if (rs_checkdigit == 2) {
                    showTextToast(getString(R.string.digit_over_back_two));
                } else if (rs_checkdigit == 3) {
                    showTextToast(getString(R.string.digit_over_front_ten));
                } else {
                    if (firstzero) {
                        pre_amount = "9";
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    } else {
                        pre_amount = pre_amount + "9";
                        pre_amount = pre_amount.replaceAll(",", "");
                        DecimalFormat formatter = new DecimalFormat("#,###,###,###.##");
                        pre_amount = formatter.format(Double.parseDouble(pre_amount));
                        amount.setText(pre_amount);
                        firstdot = false;
                        firstzero = false;
                    }
                }
            }
        });
        btnDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs_checkdigit = checkDigit(pre_amount);
                if (rs_checkdigit == 1) {
                    showTextToast(getString(R.string.digit_over_front_and_back));
                } else if (rs_checkdigit == 2) {
                    showTextToast(getString(R.string.digit_over_back_two));
                } else if (rs_checkdigit == 3) {
                    showTextToast(getString(R.string.digit_over_front_and_back));
                } else {
                    if (!dotflag) {
                        if (firstdot) {
                            pre_amount = "0.";
                            amount.setText(pre_amount);
                            dotflag = true;
                            firstzero = false;
                        } else {
                            pre_amount = pre_amount + ".";
                            amount.setText(pre_amount);
                            dotflag = true;
                            firstzero = false;
                        }
                    }
                }
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.setText("0");
                pre_amount = "0";
                dotflag = false;
                firstdot = true;
                firstzero = true;
            }
        });
        btnEnter = (Button) findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setConstant(limitType, cardScheme, pre_amount);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("reset", true);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

            }

        });

    }


    public void setConstant(String type, String cardScheme, String pre_amount) {
        System.out.println("----" + type + "--" + cardScheme + "---" + pre_amount);
        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefsettings.edit();

        setLimit = Double.parseDouble(pre_amount.replaceAll(",", ""));

        if (cardScheme.equalsIgnoreCase("Visa")) {

            floorLimit = Double.parseDouble(prefsettings.getString(Constants.visa_floor_limit, Constants.default_visa_floor_limit));
            cvmLimit = Double.parseDouble(prefsettings.getString(Constants.visa_cvm_limit, Constants.default_visa_cvm_limit));
            trxLimit = Double.parseDouble(prefsettings.getString(Constants.visa_transaction_limit, Constants.default_visa_transaction_limit));

            if (type.equalsIgnoreCase("floor")) {

                if(setLimit < cvmLimit && setLimit < trxLimit){
                    edit.putString(Constants.visa_floor_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_floor_limit));
                }

            } else if (type.equalsIgnoreCase("cvm")) {

                if(setLimit > floorLimit && setLimit < trxLimit){
                    edit.putString(Constants.visa_cvm_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_cvm_limit));
                }

            } else if (type.equalsIgnoreCase("txn")) {

                if(setLimit > floorLimit && setLimit > cvmLimit){
                    edit.putString(Constants.visa_transaction_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_trx_limit));
                }
            }

        } else if (cardScheme.equalsIgnoreCase("MasterCard")) {

            floorLimit = Double.parseDouble(prefsettings.getString(Constants.mc_floor_limit, Constants.default_mc_floor_limit));
            cvmLimit = Double.parseDouble(prefsettings.getString(Constants.mc_cvm_limit, Constants.default_mc_cvm_limit));
            trxLimit = Double.parseDouble(prefsettings.getString(Constants.mc_transaction_limit, Constants.default_mc_transaction_limit));

            if (type.equalsIgnoreCase("floor")) {

                if(setLimit < cvmLimit && setLimit < trxLimit){
                    edit.putString(Constants.mc_floor_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_floor_limit));
                }

            } else if (type.equalsIgnoreCase("cvm")) {

                if(setLimit > floorLimit && setLimit < trxLimit){
                    edit.putString(Constants.mc_cvm_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_cvm_limit));
                }

            } else if (type.equalsIgnoreCase("txn")) {

                if(setLimit > floorLimit && setLimit > cvmLimit){
                    edit.putString(Constants.mc_transaction_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_trx_limit));
                }
            }

        } else if (cardScheme.equalsIgnoreCase("China UnionPay")) {

            floorLimit = Double.parseDouble(prefsettings.getString(Constants.cup_floor_limit, Constants.default_cup_floor_limit));
            cvmLimit = Double.parseDouble(prefsettings.getString(Constants.cup_cvm_limit, Constants.default_cup_cvm_limit));
            trxLimit = Double.parseDouble(prefsettings.getString(Constants.cup_transaction_limit, Constants.default_cup_transaction_limit));

            if (type.equalsIgnoreCase("floor")) {
                if(setLimit < cvmLimit && setLimit < trxLimit){
                    edit.putString(Constants.cup_floor_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_floor_limit));
                }

            } else if (type.equalsIgnoreCase("cvm")) {

                if(setLimit > floorLimit && setLimit < trxLimit){
                    edit.putString(Constants.cup_cvm_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_cvm_limit));
                }

            } else if (type.equalsIgnoreCase("txn")) {

                if(setLimit > floorLimit && setLimit > cvmLimit){
                    edit.putString(Constants.cup_transaction_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_trx_limit));
                }
            }

        } else if (cardScheme.equalsIgnoreCase("American Express")) {

            floorLimit = Double.parseDouble(prefsettings.getString(Constants.amex_floor_limit, Constants.default_amex_floor_limit));
            cvmLimit = Double.parseDouble(prefsettings.getString(Constants.amex_cvm_limit, Constants.default_amex_cvm_limit));
            trxLimit = Double.parseDouble(prefsettings.getString(Constants.amex_transaction_limit, Constants.default_amex_transaction_limit));

            if (type.equalsIgnoreCase("floor")) {
                if(setLimit < cvmLimit && setLimit < trxLimit){
                    edit.putString(Constants.amex_floor_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_floor_limit));
                }

            } else if (type.equalsIgnoreCase("cvm")) {

                if(setLimit > floorLimit && setLimit < trxLimit){
                    edit.putString(Constants.amex_cvm_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_cvm_limit));
                }

            } else if (type.equalsIgnoreCase("txn")) {

                if(setLimit > floorLimit && setLimit > cvmLimit){
                    edit.putString(Constants.amex_transaction_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_trx_limit));
                }
            }

        } else if (cardScheme.equalsIgnoreCase("JCB")) {

            floorLimit = Double.parseDouble(prefsettings.getString(Constants.jcb_floor_limit, Constants.default_jcb_floor_limit));
            cvmLimit = Double.parseDouble(prefsettings.getString(Constants.jcb_cvm_limit, Constants.default_jcb_cvm_limit));
            trxLimit = Double.parseDouble(prefsettings.getString(Constants.jcb_transaction_limit, Constants.default_jcb_transaction_limit));

            if (type.equalsIgnoreCase("floor")) {
                if(setLimit < cvmLimit && setLimit < trxLimit){
                    edit.putString(Constants.jcb_floor_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_floor_limit));
                }

            } else if (type.equalsIgnoreCase("cvm")) {

                if(setLimit > floorLimit && setLimit < trxLimit){
                    edit.putString(Constants.jcb_cvm_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_cvm_limit));
                }

            } else if (type.equalsIgnoreCase("txn")) {

                if(setLimit > floorLimit && setLimit > cvmLimit){
                    edit.putString(Constants.jcb_transaction_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_trx_limit));
                }
            }

        } else if (cardScheme.equalsIgnoreCase("Diners Club/Discover")) {

            floorLimit = Double.parseDouble(prefsettings.getString(Constants.diners_floor_limit, Constants.default_diners_floor_limit));
            cvmLimit = Double.parseDouble(prefsettings.getString(Constants.diners_cvm_limit, Constants.default_diners_cvm_limit));
            trxLimit = Double.parseDouble(prefsettings.getString(Constants.diners_transaction_limit, Constants.default_diners_transaction_limit));

            if (type.equalsIgnoreCase("floor")) {
                if(setLimit < cvmLimit && setLimit < trxLimit){
                    edit.putString(Constants.diners_floor_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_floor_limit));
                }

            } else if (type.equalsIgnoreCase("cvm")) {

                if(setLimit > floorLimit && setLimit < trxLimit){
                    edit.putString(Constants.diners_cvm_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_cvm_limit));
                }

            } else if (type.equalsIgnoreCase("txn")) {

                if(setLimit > floorLimit && setLimit > cvmLimit){
                    edit.putString(Constants.diners_transaction_limit, pre_amount.replaceAll(",", ""));
                }else{
                    showTextToast(getString(R.string.invalid_trx_limit));
                }
            }
        }
        edit.apply();

    }

    private void showTextToast(String msg) {
        if (numberpadtoast == null) {
            numberpadtoast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            numberpadtoast.setText(msg);
        }
        numberpadtoast.show();
    }

    public static int checkDigit(String c_amount) {
        int front = 0;
        int back = 0;

        int findpoint = c_amount.indexOf(".");

        if (findpoint == (-1)) {
            front = c_amount.length();
            back = 0;
        } else {
            front = findpoint;
            back = (c_amount.length() - 1) - c_amount.indexOf(".");
        }

        if ((front >= 12) && (back >= 2)) {
            return 1;// over ten digit before the decimal point  and  over two decimal places
        } else if (back >= 2) {
            return 2;// over two decimal places
        } else if (front >= 12) {
            return 3;// over ten digit before the decimal point
        } else {
            return 0;//  0.01<=amount<=9999999999
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
