package com.example.dell.smartpos;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerFragment extends Fragment {

    ViewPager mPager;
    DynamicFragmentAdapter mAdapter;
    ArrayList<MenuIcon> menuList = new ArrayList<>();
    HashMap<String, String> map;

    String cardPayBankId = "";

    public ViewPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

		Toolbar mToolbar = ((MainActivity) getActivity()).findViewById(R.id.toolbar);

		LinearLayout.LayoutParams layoutParams =
				(LinearLayout.LayoutParams) mToolbar.getLayoutParams();
		layoutParams.width = 600;
		mToolbar.setLayoutParams(layoutParams);

		((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.main_menu));

        HashMap<String, String> map = (HashMap<String, String>) getArguments().getSerializable("map");
        String merchantID = map.get(Constants.MERID);

        /***************
         ** Menu List **
         ***************/
        menuList = new ArrayList<>();
        MenuIcon cardPayment = new MenuIcon(R.drawable.ic_cardpayment2, getString(R.string.card_payments_menu));
        MenuIcon scanQRPayment = new MenuIcon(R.drawable.ic_scanqr2, getString(R.string.scan_qr_menu));
        MenuIcon presentQRPayment = new MenuIcon(R.drawable.ic_merchantqr, getString(R.string.present_qr_menu));
        MenuIcon mobilePayment = new MenuIcon(R.drawable.ic_mobilepayment, getString(R.string.mobile_menu));
        MenuIcon txnHistory = new MenuIcon(R.drawable.ic_history2, getString(R.string.transaction_history_menu));
        MenuIcon reports = new MenuIcon(R.drawable.ic_report2, getString(R.string.reports_menu));
        MenuIcon settings = new MenuIcon(R.drawable.ic_settings2, getString(R.string.settings_menu));
        MenuIcon fps = new MenuIcon(R.drawable.ic_fps, getString(R.string.fps_menu));
        MenuIcon simplePay = new MenuIcon(R.drawable.ic_simple_pay, getString(R.string.simplepay_menu));
        MenuIcon installment = new MenuIcon(R.drawable.ic_installment, getString(R.string.installment_menu));
        MenuIcon settlement = new MenuIcon(R.drawable.ic_settlement, getString(R.string.settlement_menu));

        menuList.add(cardPayment);
        menuList.add(scanQRPayment);
        menuList.add(presentQRPayment);
        menuList.add(mobilePayment);
        menuList.add(txnHistory);
        menuList.add(reports);
        menuList.add(settings);

        if (merchantID != null && merchantID.equalsIgnoreCase("560200335")){
            menuList.add(fps);
            menuList.add(simplePay);
            menuList.add(installment);
        }

        cardPayBankId = GlobalFunction.getCardPayBankId(getContext());

        if (!cardPayBankId.equals("") && GlobalFunction.isValidCardBank(cardPayBankId) && GlobalFunction.isFDMS_BASE24Installed(
                getActivity().getPackageManager())) {
            menuList.add(settlement);
        }

        List<Fragment> fragmentList = new ArrayList<>();
        List<List<MenuIcon> > lists = Lists.partition(menuList, 9); //partition list into a fixed size

        for (List<MenuIcon> sublist: lists){ //loop and add one new fragment page if exceed fixed size set
            ArrayList<MenuIcon> menu = new ArrayList<>(sublist);   //sublist is not serializable, thus need to recreate arrayList

            //pass variables from main activity to menu fragment
            map = (HashMap<String, String>)getArguments().getSerializable("map");

            fragmentList.add(MenuFragment.newInstance(menu, map));
            mAdapter = new DynamicFragmentAdapter(getFragmentManager(), fragmentList);
            mPager = (ViewPager) view.findViewById(R.id.menuPager);
            mPager.setAdapter(mAdapter);
        }

        return view;
    }

    // newInstance constructor for creating fragment with arguments
    public static ViewPagerFragment newInstance(ArrayList<MenuIcon> menuList, HashMap<String, String> map) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putSerializable("menuList", (Serializable)menuList);
        args.putSerializable("map", (Serializable)map);
        fragment.setArguments(args);

        return fragment;
    }
}
