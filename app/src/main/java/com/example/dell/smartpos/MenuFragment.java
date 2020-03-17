package com.example.dell.smartpos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<MenuIcon> menuList = new ArrayList<>();

    AlertDialog.Builder dialog;
    AlertDialog alertDialog;

    boolean loginSuccess = false;

    String cardPayBankId = "";
    String cardSettlementControl = "";
    String cardSettlementPassword = "";

    private HistoryFragment historyFragment;
    private ReportFragment reportFragment;
    private SettingsFragment settingsFragment;
    private SettlementFragment settlementFragment;

    private String merchantID;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
		//((MainActivity) getActivity()).setSupportActionBar(getResources().);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.main_menu));

        merchantID = getArguments().getString(Constants.MERID);

        menuList = (ArrayList<MenuIcon>) getArguments().getSerializable("menuList");

        recyclerView = (RecyclerView) view.findViewById(R.id.menuRecycler);
        recyclerView.setAdapter(new MenuIconAdapter(menuList, getContext(), new MenuIconAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuIcon item) {

                String menu = item.getMenuName();
                String paymentOption = "";

                Intent paymentIntent = new Intent(getActivity(), EnterAmount.class);
                paymentIntent.putExtra(Constants.MERID, merchantID);
                paymentIntent.putExtra(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                paymentIntent.putExtra(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
                paymentIntent.putExtra(Constants.PAYMETHODLIST, getArguments().getString(Constants.PAYMETHODLIST));
                paymentIntent.putExtra(Constants.pref_Rate, getArguments().getString(Constants.pref_Rate));
                paymentIntent.putExtra(Constants.pref_hideSurcharge, getArguments().getString(Constants.pref_hideSurcharge));
                paymentIntent.putExtra(Constants.pref_Fixed, getArguments().getString(Constants.pref_Fixed));
                paymentIntent.putExtra(Constants.OPERATORID, getArguments().getString(Constants.OPERATORID));

                cardPayBankId = GlobalFunction.getCardPayBankId(getContext());

                if (menu.equalsIgnoreCase(getString(R.string.card_payments_menu))) {
                    if (merchantID.equalsIgnoreCase("560200335") ||
                            merchantID.equalsIgnoreCase(
                            "560200303") ||
                            (!cardPayBankId.equalsIgnoreCase("") && GlobalFunction.getDeviceMan().equalsIgnoreCase("PAX") && GlobalFunction.isFDMS_BASE24Installed(getActivity().getPackageManager()))){
                        paymentOption = "CARD PAYMENT";
                        paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                        startActivity(paymentIntent);
                    } else {
                        Toast.makeText(getActivity(), R.string.payment_option_not_supported, Toast.LENGTH_SHORT).show();
                    }

                } else if (menu.equalsIgnoreCase(getString(R.string.scan_qr_menu))) {
                    paymentOption = "SCAN QR PAYMENT";
                    paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                    startActivity(paymentIntent);

                } else if(menu.equalsIgnoreCase(getString(R.string.present_qr_menu))) {
                    paymentOption = "PRESENT QR PAYMENT";
                    paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                    startActivity(paymentIntent);

                } else if(menu.equalsIgnoreCase(getString(R.string.mobile_menu))) {

                    if (merchantID.equalsIgnoreCase("560200335") || merchantID.equalsIgnoreCase(
                            "560200303")) {
                        paymentOption = "MOBILE PAYMENT";
                        paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                        startActivity(paymentIntent);
                    } else {
                        Toast.makeText(getActivity(), R.string.payment_option_not_supported, Toast.LENGTH_SHORT).show();
                    }

                } else if(menu.equalsIgnoreCase(getString(R.string.transaction_history_menu))) {
                    historyFragment = new HistoryFragment();

                    Bundle historyArgs = new Bundle();
                    historyArgs.putString(Constants.MERID, getArguments().getString(Constants.MERID));
                    historyArgs.putString(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                    historyFragment.setArguments(historyArgs);
                    ((MainActivity) getActivity()).setFragment(historyFragment);

//                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.main_frame, historyFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();

                } else if (menu.equalsIgnoreCase(getString(R.string.reports_menu))) {
                    reportFragment = new ReportFragment();

                    Bundle reportArgs = new Bundle();
                    reportArgs.putString(Constants.MERID, getArguments().getString(Constants.MERID));
                    reportArgs.putString(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                    reportArgs.putString(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
                    reportFragment.setArguments(reportArgs);
                    ((MainActivity) getActivity()).setFragment(reportFragment);

                } else if (menu.equalsIgnoreCase(getString(R.string.settings_menu))) {
                    settingDialog();

                } else if (menu.equalsIgnoreCase(getString(R.string.fps_menu))){
                    if (merchantID.equalsIgnoreCase("560200335") || merchantID.equalsIgnoreCase(
                            "560200303")) {
                        paymentOption = "FPS PRESENT QR";
                        paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                        startActivity(paymentIntent);
                    } else {
                        Toast.makeText(getActivity(), R.string.payment_option_not_supported, Toast.LENGTH_SHORT).show();
                    }

                } else if (menu.equalsIgnoreCase(getString(R.string.simplepay_menu))) {
                    paymentOption = "SIMPLE PAY";
                    paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                    startActivity(paymentIntent);

                } else if (menu.equalsIgnoreCase(getString(R.string.installment_menu))) {
                    paymentOption = "INSTALLMENT";
                    paymentIntent.putExtra(Constants.PAYMENTOPTION, paymentOption);
                    startActivity(paymentIntent);
                } else if (menu.equalsIgnoreCase(getString(R.string.settlement_menu))) {
                    settlementDialog();
                }
            }
        }));

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        return view;
    }

    private void settingDialog() {

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
                    edit.putString(Constants.pref_setting_mode, "mode3");
                    //  edit.commit();
                    loginSuccess = true;
                } else if (value.equals(pref.getString(Constants.pref_Mer_SettingPW, Constants.default_settingPW)) || value.equals(pref.getString(Constants.pref_Mer_SettingPW, Constants.default_settingPW_2))) {
                    edit.putString(Constants.pref_setting_mode, "mode2");
                    loginSuccess = true;
                } else if (value.equals(pref.getString(Constants.admin_PW, Constants.admin_PW))) {
                    edit.putString(Constants.pref_setting_mode, "mode1");
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

    private void settlementDialog() {

        cardSettlementControl = GlobalFunction.getCardSettlementControl(getContext());
        if (cardSettlementControl.equalsIgnoreCase("ON")) {
            dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(R.string.input_adminPW);

            //EditText
            final EditText inputPassword = new EditText(getActivity());
            inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            inputPassword.setHint(getString(R.string.password));

            dialog.setView(inputPassword);
            dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    cardSettlementPassword = GlobalFunction.getCardSettlementPassword(getContext());
                    String password = inputPassword.getText().toString().trim();

                    if (password.equals(cardSettlementPassword)){
                        settlementFragment = new SettlementFragment();

                        Bundle reportArgs = new Bundle();
                        reportArgs.putString(Constants.MERID, getArguments().getString(Constants.MERID));
                        reportArgs.putString(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                        reportArgs.putString(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
                        settlementFragment.setArguments(reportArgs);
                        ((MainActivity) getActivity()).setFragment(settlementFragment);
                    } else {
                        Toast.makeText(getActivity(), R.string.error3, Toast.LENGTH_SHORT).show();
                    }

                }
            });
            alertDialog = dialog.create();
            alertDialog.show();
        } else {
            // Without password
            settlementFragment = new SettlementFragment();

            Bundle reportArgs = new Bundle();
            reportArgs.putString(Constants.MERID, getArguments().getString(Constants.MERID));
            reportArgs.putString(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
            reportArgs.putString(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
            settlementFragment.setArguments(reportArgs);
            ((MainActivity) getActivity()).setFragment(settlementFragment);
        }
    }

    // newInstance constructor for creating fragment with arguments
    public static MenuFragment newInstance(List<MenuIcon> menuList, HashMap<String, String> map) {
        MenuFragment fragment = new MenuFragment();
        Bundle menuArgs = new Bundle();
        menuArgs.putSerializable("menuList", (Serializable)menuList);
        menuArgs.putString(Constants.CURRCODE, map.get(Constants.CURRCODE));
        menuArgs.putString(Constants.MERNAME, map.get(Constants.MERNAME));
        menuArgs.putString(Constants.MERID, map.get(Constants.MERID));
        menuArgs.putString(Constants.PAYMETHODLIST, map.get(Constants.PAYMETHODLIST));
        menuArgs.putString(Constants.pref_Rate, map.get(Constants.pref_Rate));
        menuArgs.putString(Constants.pref_hideSurcharge, map.get(Constants.pref_hideSurcharge));
        menuArgs.putString(Constants.pref_Fixed, map.get(Constants.pref_Fixed));
        menuArgs.putString(Constants.OPERATORID, map.get(Constants.OPERATORID));
        fragment.setArguments(menuArgs);

        return fragment;
    }
}
