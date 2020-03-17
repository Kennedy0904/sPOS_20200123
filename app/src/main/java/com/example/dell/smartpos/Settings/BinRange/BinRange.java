package com.example.dell.smartpos.Settings.BinRange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinRange extends AppCompatActivity {

    private ListView lvRange;
    private rangeArray adapter;
    private List<RangeProduct> rangeItem = new ArrayList<>();

    private static int count = 0;

    private Button btnNewRange;
    private Button btnOK;

    private static String cardScheme = "";
    private static String retrievedData = "";
    private static String setData = "";
    private static String constant = "";

    private static String[] rangeStart = new String[100];
    private static String[] rangeEnd = new String[100];
    private static int removedPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin_range);
        setTitle(R.string.bin_range);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        cardScheme = intent.getStringExtra("cardScheme");

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);
        final SharedPreferences.Editor edit = prefsettings.edit();

        if (cardScheme.equalsIgnoreCase("visa")) {
            retrievedData = prefsettings.getString(Constants.visa_bin_range, Constants.default_visa_bin_range);
            constant = Constants.visa_bin_range;
        } else if (cardScheme.equalsIgnoreCase("mc")) {
            retrievedData = prefsettings.getString(Constants.mc_bin_range, Constants.default_mc_bin_range);
            constant = Constants.mc_bin_range;
        } else if (cardScheme.equalsIgnoreCase("cup")) {
            retrievedData = prefsettings.getString(Constants.cup_bin_range, Constants.default_cup_bin_range);
            constant = Constants.cup_bin_range;
        } else if (cardScheme.equalsIgnoreCase("amex")) {
            retrievedData = prefsettings.getString(Constants.amex_bin_range, Constants.default_amex_bin_range);
            constant = Constants.amex_bin_range;
        } else if (cardScheme.equalsIgnoreCase("jcb")) {
            retrievedData = prefsettings.getString(Constants.jcb_bin_range, Constants.default_jcb_bin_range);
            constant = Constants.jcb_bin_range;
        } else if (cardScheme.equalsIgnoreCase("diners")) {
            retrievedData = prefsettings.getString(Constants.diners_bin_range, Constants.default_diners_bin_range);
            constant = Constants.diners_bin_range;
        }

        Arrays.fill(rangeStart, "");

        if (retrievedData.split("-R-").length > 0) {
            for (int i = 0; i < retrievedData.split("-R-").length; i++) {
                rangeStart[i] = "";
                rangeEnd[i] = "";

                if (retrievedData.split("-R-")[i].split("_").length > 1) {
                    rangeStart[i] = retrievedData.split("-R-")[i].split("_")[0];
                    rangeEnd[i] = retrievedData.split("-R-")[i].split("_")[1];
                }
                System.out.println("------" + i + " " + rangeStart[i] + " " + rangeEnd[i]);
            }
        }


        lvRange = (ListView) findViewById(R.id.rangeList);

        btnOK = (Button) findViewById(R.id.btnOK);
        btnNewRange = (Button) findViewById(R.id.btnAdd);

        lvRange.setItemsCanFocus(true);

        for (int i = 0; i < rangeStart.length; i++) {

            if (rangeStart[i].equals("")) {
                break;
            } else {
                rangeItem.add(new RangeProduct((i + 1), rangeStart[i], rangeEnd[i]));
                count++;
            }
        }

        adapter = new rangeArray(getApplicationContext(), rangeItem);
        lvRange.setAdapter(adapter);

        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(lvRange, new SwipeDismissListViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    removedPos = position;
                    rangeItem.remove(position);
                    adapter.notifyDataSetChanged();
                }

                refreshListView(removedPos);

            }
        });

        lvRange.setOnTouchListener(touchListener);


        btnNewRange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rangeItem.add(new RangeProduct((count + 1), "", ""));
                count++;
                adapter = new rangeArray(getApplicationContext(), rangeItem);
                lvRange.setAdapter(adapter);
                lvRange.setSelection(lvRange.getAdapter().getCount()-1);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                View parentView = null;
                setData = "";
                for (int i = 0; i < lvRange.getCount(); i++) {
                    parentView = getViewByPosition(i, lvRange);
                    setData += ((TextView) parentView.findViewById(R.id.rangeStart)).getText().toString() + "_" +
                            ((TextView) parentView.findViewById(R.id.rangeEnd)).getText().toString();

                    if (i != lvRange.getCount() - 1) {
                        setData += "-R-";
                    }
                }

                edit.putString(constant, setData);
                edit.apply();
                count = 0;
                finish();
            }

        });
    }

    public void refreshListView(int removedPos) {
        View parentView = null;
        String[] getRange = new String[lvRange.getCount()];
        String[] getRangeStart = new String[lvRange.getCount()];
        String[] getRangeEnd = new String[lvRange.getCount()];
        Arrays.fill(getRange, "");
        Arrays.fill(getRangeStart, "");
        Arrays.fill(getRangeEnd, "");
        int counter = 0;
        for (int i = 0; i < lvRange.getCount() + 1; i++) {
            parentView = getViewByPosition(i, lvRange);

            if (i == removedPos) {
                counter = 1;
            } else {
                if (counter == 0) {
                    getRange[i] = ((TextView) parentView.findViewById(R.id.range)).getText().toString();
                    getRangeStart[i] = ((TextView) parentView.findViewById(R.id.rangeStart)).getText().toString();
                    getRangeEnd[i] = ((TextView) parentView.findViewById(R.id.rangeEnd)).getText().toString();
                } else {
                    getRange[i - 1] = ((TextView) parentView.findViewById(R.id.range)).getText().toString();
                    getRangeStart[i - 1] = ((TextView) parentView.findViewById(R.id.rangeStart)).getText().toString();
                    getRangeEnd[i - 1] = ((TextView) parentView.findViewById(R.id.rangeEnd)).getText().toString();
                }
            }
        }

        rangeItem.clear();
        adapter.notifyDataSetChanged();
        count = 0;

        for (int i = 0; i < getRange.length; i++) {
            rangeItem.add(new RangeProduct((i + 1), getRangeStart[i], getRangeEnd[i]));
            count++;
        }

        adapter = new rangeArray(getApplicationContext(), rangeItem);
        lvRange.setAdapter(adapter);
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        rangeItem.clear();
        adapter.notifyDataSetChanged();
        count = 0;
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        rangeItem.clear();
        adapter.notifyDataSetChanged();
        count = 0;
        finish();
    }

}
