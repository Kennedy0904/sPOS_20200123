package com.example.dell.smartpos.Settings.BinRange;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dell.smartpos.R;

import java.util.List;

public class rangeArray extends BaseAdapter {
    private Context context;
    private List<RangeProduct> rangeList;

    public rangeArray(Context context, List<RangeProduct> rangeList) {
        this.context = context;
        this.rangeList = rangeList;
    }

    @Override
    public int getCount() {
        return rangeList.size();
    }

    @Override
    public Object getItem(int position) {
        return rangeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View v = View.inflate(context, R.layout.bin_range_item, null);
        TextView range = (TextView) v.findViewById(R.id.range);
        final EditText rangeStart = (EditText) v.findViewById(R.id.rangeStart);
        final EditText rangeEnd = (EditText) v.findViewById(R.id.rangeEnd);

        int i = rangeList.get(position).getId();
        String index = Integer.toString(rangeList.get(position).getId());

        rangeStart.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Log.d("TEST", "In afterTextChanged");

                if (rangeStart.getText().toString().matches("[0-9]+")) {
                    rangeList.get(position).setRangeStart(rangeStart.getText().toString());
                } else {
//                    Toast.makeText(v.getContext(), R.string.only_num, Toast.LENGTH_SHORT).show();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("TEST", "In beforeTextChanged");
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TEST", "In onTextChanged");

                if (rangeStart.getText().toString().matches("[0-9]+")) {

                } else {
//                    Toast.makeText(v.getContext(), R.string.only_num, Toast.LENGTH_SHORT).show();
                }
            }
        });

        rangeEnd.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Log.d("TEST", "In afterTextChanged");

                if (rangeEnd.getText().toString().matches("[0-9]+")) {
                    rangeList.get(position).setRangeEnd(rangeEnd.getText().toString());
                } else {
//                    Toast.makeText(v.getContext(), R.string.only_num, Toast.LENGTH_SHORT).show();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("TEST", "In beforeTextChanged");
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TEST", "In onTextChanged");
                if (rangeEnd.getText().toString().matches("[0-9]+")) {

                } else {
//                    Toast.makeText(v.getContext(), R.string.only_num, Toast.LENGTH_SHORT).show();
                }
            }
        });

        range.setText(index);
        rangeStart.setText(rangeList.get(position).getRangeStart());
        rangeEnd.setText(rangeList.get(position).getRangeEnd());
        return v;
    }

}
