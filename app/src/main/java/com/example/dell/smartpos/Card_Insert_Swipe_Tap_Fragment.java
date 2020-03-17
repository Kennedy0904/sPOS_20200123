package com.example.dell.smartpos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Card_Insert_Swipe_Tap_Fragment extends Fragment {


    public Card_Insert_Swipe_Tap_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card__insert__swipe__tap, container, false);
    }

    // newInstance constructor for creating fragment with arguments
    public static Card_Insert_Swipe_Tap_Fragment newInstance(HashMap map) {
        Card_Insert_Swipe_Tap_Fragment fragment = new Card_Insert_Swipe_Tap_Fragment();
//        Bundle args = new Bundle();
//        args.putString("merID", map.get("merID").toString());
//        args.putString("merRef", map.get("merRef").toString());
//        args.putString("amount", map.get("amount").toString());
//        args.putString("currCode", map.get("currCode").toString());
//        args.putString("payType", map.get("payType").toString());
//        fragment.setArguments(args);
        return fragment;
    }
}
