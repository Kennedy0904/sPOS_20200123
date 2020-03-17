package com.example.dell.smartpos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Card_Manual_Fragment extends Fragment {

    RelativeLayout manual;

    public Card_Manual_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_card__manual, container, false);
        manual = (RelativeLayout) rootView.findViewById(R.id.manual);

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validation Entry Mode Settings (MAG)
                boolean result = checkManualEntry(getArguments().getString("payType"));
                if(result){

                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constants.MERID, getArguments().getString(Constants.MERID));
                    map.put(Constants.MERCHANT_REF, getArguments().getString(Constants.MERCHANT_REF));
                    map.put(Constants.AMOUNT, getArguments().getString(Constants.AMOUNT));
                    map.put(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
                    map.put(Constants.PAYTYPE, getArguments().getString(Constants.PAYTYPE));
                    map.put(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                    map.put(Constants.OPERATORID, getArguments().getString(Constants.OPERATORID));

                    CustomCardManualDialog dialog = new CustomCardManualDialog(getActivity(), map);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();

//                    Intent intent = new Intent(getActivity(), CardPayment_ManualEntry.class);
//                    intent.putExtra("merID",  getArguments().getString("merID"));
//                    intent.putExtra("merRef",  getArguments().getString("merRef"));
//                    intent.putExtra("amount",  getArguments().getString("amount"));
//                    intent.putExtra("currCode",  getArguments().getString("currCode"));
//                    intent.putExtra("payType",  getArguments().getString("payType"));
//                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    // newInstance constructor for creating fragment with arguments
    public static Card_Manual_Fragment newInstance(HashMap <String, String> map) {
        Card_Manual_Fragment fragment = new Card_Manual_Fragment();
        Bundle args = new Bundle();
        args.putString(Constants.MERID, map.get(Constants.MERID));
        args.putString(Constants.MERCHANT_REF, map.get(Constants.MERCHANT_REF));
        args.putString(Constants.AMOUNT, map.get(Constants.AMOUNT));
        args.putString(Constants.CURRCODE, map.get(Constants.CURRCODE));
        args.putString(Constants.PAYTYPE, map.get(Constants.PAYTYPE));
        args.putString(Constants.OPERATORID, map.get(Constants.OPERATORID));
        args.putString(Constants.MERNAME, map.get(Constants.MERNAME));
        fragment.setArguments(args);

        return fragment;
    }

    private boolean checkManualEntry(String payType){

        boolean entryModeResult = false;
        boolean txnTypeResult = false;

        SharedPreferences prefsettings = getActivity().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        String allEntryMode = prefsettings.getString(Constants.entry_mode, "").toLowerCase();

        if (allEntryMode.contains("-manual_key_in-")) {
            entryModeResult = true;
        }

        if(entryModeResult){
            String txnType_manual = prefsettings.getString(Constants.manual_key_in_txn_type, "").toLowerCase();

            if (payType.equalsIgnoreCase("N")) {

                if(txnType_manual.contains("-onsale-")){
                    txnTypeResult = true;
                }else{
                    displayErrDialog(getString(R.string.emv_onsales_not_supported));
                }
            }

            if (payType.equalsIgnoreCase("H")) {

                if(txnType_manual.contains("-preauth-")){
                    txnTypeResult = true;
                }else{
                    displayErrDialog(getString(R.string.emv_preauth_not_supported));
                }
            }
        }else{
            displayErrDialog(getString(R.string.entryMode_manual_not_supported));
        }

        return txnTypeResult;
    }

    private void displayErrDialog(final String message){

        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.alert))
                .setIcon(R.drawable.ic_warning_24dp)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
