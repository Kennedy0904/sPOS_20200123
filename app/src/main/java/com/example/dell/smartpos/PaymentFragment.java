package com.example.dell.smartpos;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {

    private RelativeLayout
            cardpayment,
            scanqrpayment,
            presentqrpayment,
            mobilepayment,
            transactionHistory,
            reports,
            settings,
            settlement,
            fps,
            simplePay,
            installment;


    AlertDialog.Builder dialog;
    AlertDialog alertDialog;

    //Settlement Dialog
    AlertDialog.Builder settlementDialog;
    AlertDialog alertSettlementDialog;

    String paymentOption = "";
    String cardPayBankId = "";
    String password = "";
    String cardSettlementControl = "";
    String cardSettlementPassword = "";
    boolean loginSuccess = false;

    private HistoryFragment historyFragment;
    private ReportFragment reportFragment;
    private SettingsFragment settingsFragment;
    private SettlementFragment settlementFragment;

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_payment, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.main_menu));

        cardpayment = (RelativeLayout) rootView.findViewById(R.id.cardpayment);
        scanqrpayment = (RelativeLayout) rootView.findViewById(R.id.scanqrpayment);
        presentqrpayment = (RelativeLayout) rootView.findViewById(R.id.presentqrpayment);
        mobilepayment = (RelativeLayout) rootView.findViewById(R.id.mobilepayment);
        transactionHistory = (RelativeLayout) rootView.findViewById(R.id.transactionHistory);
        reports = (RelativeLayout) rootView.findViewById(R.id.reports);
        settings = (RelativeLayout) rootView.findViewById(R.id.settings);
        settlement = (RelativeLayout) rootView.findViewById(R.id.settlement);
        fps = (RelativeLayout) rootView.findViewById(R.id.fps);
        simplePay = (RelativeLayout) rootView.findViewById(R.id.simplePay);
        installment = (RelativeLayout) rootView.findViewById(R.id.installment);

        final Intent paymentIntent = new Intent(getActivity(), EnterAmount.class);
        paymentIntent.putExtra(Constants.MERID, getArguments().getString(Constants.MERID));
        paymentIntent.putExtra(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
        paymentIntent.putExtra(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
        paymentIntent.putExtra(Constants.PAYMETHODLIST, getArguments().getString(Constants.PAYMETHODLIST));
        paymentIntent.putExtra(Constants.PAYBANKIDLIST, getArguments().getString(Constants.PAYBANKIDLIST));
        paymentIntent.putExtra(Constants.pref_Rate, getArguments().getString(Constants.pref_Rate));
        paymentIntent.putExtra(Constants.pref_hideSurcharge, getArguments().getString(Constants.pref_hideSurcharge));
        paymentIntent.putExtra(Constants.pref_Fixed, getArguments().getString(Constants.pref_Fixed));
        paymentIntent.putExtra(Constants.OPERATORID, getArguments().getString(Constants.OPERATORID));

        final Intent settlementIntent = new Intent(getActivity(), Settlement.class);
        settlementIntent.putExtra(Constants.MERID, getArguments().getString(Constants.MERID));

        if (getArguments().getString(Constants.MERID).equalsIgnoreCase("560200335")
                || getArguments().getString(Constants.MERID).equalsIgnoreCase("560200303")
                || getArguments().getString(Constants.MERID).equalsIgnoreCase("85003895")) {
            simplePay.setVisibility(View.VISIBLE);
            installment.setVisibility(View.VISIBLE);
        } else {
            fps.setVisibility(View.GONE);
            simplePay.setVisibility(View.GONE);
            installment.setVisibility(View.GONE);
        }

        cardPayBankId = GlobalFunction.getCardPayBankId(this.getContext());
        System.out.println("cardPayBankId dsdsd: " + cardPayBankId);
        cardpayment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getArguments().getString(Constants.MERID).equalsIgnoreCase("560200335") || getArguments().getString(Constants.MERID).equalsIgnoreCase("560200303") || !cardPayBankId.equalsIgnoreCase("")) {
                    paymentOption = "CARD PAYMENT";
                    paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                    startActivity(paymentIntent);
                } else {
                    Toast.makeText(getActivity(), R.string.payment_option_not_supported, Toast.LENGTH_SHORT).show();
                }
            }

        });

        scanqrpayment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                paymentOption = "SCAN QR PAYMENT";
                paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                startActivity(paymentIntent);
            }

        });

        presentqrpayment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                paymentOption = "PRESENT QR PAYMENT";
                paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                startActivity(paymentIntent);
            }
        });

        fps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getArguments().getString(Constants.MERID).equalsIgnoreCase("560200335") || getArguments().getString(Constants.MERID).equalsIgnoreCase("560200303")) {
                    paymentOption = "FPS PRESENT QR";
                    paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                    startActivity(paymentIntent);
                } else {
                    Toast.makeText(getActivity(), R.string.payment_option_not_supported, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mobilepayment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getArguments().getString(Constants.MERID).equalsIgnoreCase("560200335") || getArguments().getString(Constants.MERID).equalsIgnoreCase("560200303")) {
                    paymentOption = "MOBILE PAYMENT";
                    paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                    startActivity(paymentIntent);
                } else {
                    Toast.makeText(getActivity(), R.string.payment_option_not_supported, Toast.LENGTH_SHORT).show();
                }
            }

        });

        transactionHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                historyFragment = new HistoryFragment();

                Bundle historyArgs = new Bundle();
                historyArgs.putString(Constants.MERID, getArguments().getString(Constants.MERID));
                historyArgs.putString(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                historyFragment.setArguments(historyArgs);
                ((MainActivity) getActivity()).setFragment(historyFragment);
            }

        });

        reports.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reportFragment = new ReportFragment();

                Bundle reportArgs = new Bundle();
                reportArgs.putString(Constants.MERID, getArguments().getString(Constants.MERID));
                reportArgs.putString(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                reportArgs.putString(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
                reportFragment.setArguments(reportArgs);
                ((MainActivity) getActivity()).setFragment(reportFragment);
            }

        });

        simplePay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                paymentOption = "SIMPLE PAY";
                paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                startActivity(paymentIntent);
            }
        });

        installment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                paymentOption = "INSTALLMENT";
                paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                startActivity(paymentIntent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(R.string.input_settingPW);
                Toast.makeText(getActivity(), R.string.input_settingPW_msg, Toast.LENGTH_LONG).show();

                //EditText
                final EditText inputSettingPassword = new EditText(getActivity());
                inputSettingPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                inputSettingPassword.setHint(getString(R.string.password));

                dialog.setView(inputSettingPassword);
                dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(
                                Constants.SETTINGS, MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();
                        String value = inputSettingPassword.getText().toString().trim();

                        if (value.equals("")) {
                            // All User Level, non-password
                            edit.putString(Constants.pref_setting_mode, "mode3");
                            //  edit.commit();
                            loginSuccess = true;
                        } else if (value.equals(pref.getString(Constants.pref_Mer_SettingPW, Constants.default_settingPW)) || value.equals(pref.getString(Constants.pref_Mer_SettingPW, Constants.default_settingPW_2))) {
                            // Merchant Level
                            edit.putString(Constants.pref_setting_mode, "mode2");
                            //  edit.commit();
                            loginSuccess = true;
                        } else if (value.equals(pref.getString(Constants.admin_PW, Constants.admin_PW))) {
                            // AsiaPay Admin Level
                            edit.putString(Constants.pref_setting_mode, "mode1");
                            //  edit.commit();
                            loginSuccess = true;
                        } else {
                            loginSuccess = false;
                            Toast.makeText(getActivity(), R.string.error3, Toast.LENGTH_SHORT).show();
                        }
                        edit.commit();

                        if (loginSuccess) {
                            settingsFragment = new SettingsFragment();

                            Bundle settingsArgs = new Bundle();
                            settingsArgs.putString(Constants.MERID, getArguments().getString(Constants.MERID));
                            settingsArgs.putString(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                            //--Edited 25/07/18 by KJ--//
                            settingsArgs.putString(Constants.PAYMETHODLIST, getArguments().getString(Constants.PAYMETHODLIST));
                            settingsArgs.putString(Constants.CURRCODE,  getArguments().getString(Constants.CURRCODE));
                            //--done Edited 25/07/18 by KJ--//
                            settingsFragment.setArguments(settingsArgs);
                            ((MainActivity) getActivity()).setFragment(settingsFragment);
                        }

                    }
                });
                alertDialog = dialog.create();
                alertDialog.show();
                loginSuccess = false;
            }

        });


        settlement.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                cardSettlementControl = GlobalFunction.getCardSettlementControl(getContext());
                System.out.println("cardSettlementControl " + cardSettlementControl);
                if (cardSettlementControl.equalsIgnoreCase("ON")) {
                    settlementDialog = new AlertDialog.Builder(getActivity());
                    settlementDialog.setTitle(R.string.input_adminPW);

                    //EditText
                    final EditText inputPassword = new EditText(getActivity());
                    inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    inputPassword.setHint(getString(R.string.password));

                    settlementDialog.setView(inputPassword);
                    settlementDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface settlementDialog, int whichButton) {

                            cardSettlementPassword = GlobalFunction.getCardSettlementPassword(getContext());
                            password = inputPassword.getText().toString().trim();

                            if (password.equals(cardSettlementPassword)){
                                confirmAction();
                            } else {
                                Toast.makeText(getActivity(), R.string.error3, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    alertSettlementDialog = settlementDialog.create();
                    alertSettlementDialog.show();
                } else {
                    // Without password
                    confirmAction();
                }
            }

        });
        return rootView;
    }

    public void confirmAction() {
        settlementFragment = new SettlementFragment();

        Bundle settlementArgs = new Bundle();
        settlementArgs.putString(Constants.MERID, getArguments().getString(Constants.MERID));
        settlementArgs.putString(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
        settlementFragment.setArguments(settlementArgs);
        ((MainActivity) getActivity()).setFragment(settlementFragment);
    }
}