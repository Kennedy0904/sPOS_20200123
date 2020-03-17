package com.example.dell.smartpos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;

public class SettlementFragment extends Fragment {

    LinearLayout currentSettlementLayout;
    LinearLayout lastSettlementLayout;

    String merID;
    String merName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Toolbar mToolbar = ((MainActivity) getActivity()).findViewById(R.id.toolbar);

        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) mToolbar.getLayoutParams();
        layoutParams.width = 600;
        mToolbar.setLayoutParams(layoutParams);

        View rootView = inflater.inflate(R.layout.fragment_settlement_option, container, false);

        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.settlement));


        currentSettlementLayout = (LinearLayout) rootView.findViewById(R.id.currentSettlementLayout);
        lastSettlementLayout = (LinearLayout) rootView.findViewById(R.id.lastSettlementLayout);

        merID = getArguments().getString(Constants.MERID);
        merName = getArguments().getString(Constants.MERNAME);

        currentSettlementLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constants.MERID, merID);
                intent.putExtra(Constants.MERNAME, merName);
                intent.setClass(getActivity(), SettlementCurrent.class);
                startActivity(intent);
            }
        });

        lastSettlementLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constants.MERID, merID);
                intent.putExtra(Constants.MERNAME, merName);
                intent.setClass(getActivity(), SettlementLast.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("SettlementFragment onResume");

        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.settlement));
    }
}
