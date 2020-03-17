package com.example.dell.smartpos;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends Fragment {

    private LinearLayout networkTestLayout;
    private LinearLayout stationeryOrder;

    String merchantId = "";
    String merchantName = "";

    public SupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.support));

        final View rootView = inflater.inflate(R.layout.fragment_support, container, false);
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, Context.MODE_PRIVATE);

        Bundle bundle = getArguments();
        merchantId = bundle.getString(Constants.MERID);
        merchantName = bundle.getString(Constants.MERNAME);

        networkTestLayout = (LinearLayout)rootView.findViewById(R.id.networkTestLayout);
        stationeryOrder = (LinearLayout)rootView.findViewById(R.id.stationaryOrder);

        if(pref.getString(Constants.pref_order_control, Constants.default_order_control).equalsIgnoreCase("ON")){
            stationeryOrder.setVisibility(stationeryOrder.VISIBLE);
        }else{
            stationeryOrder.setVisibility(stationeryOrder.GONE);
        }

        networkTestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent networkTest = new Intent(getActivity(), TestConnection.class);
                startActivity(networkTest);
            }
        });

        stationeryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stationaryIntent = new Intent(getActivity(), StationeryOrder.class);
                stationaryIntent.putExtra(Constants.MERID, merchantId);
                stationaryIntent.putExtra(Constants.MERNAME, merchantName);
                startActivity(stationaryIntent);
            }
        });



        return rootView;

    }
}
