package com.example.dell.smartpos;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    Date fromDate, toDate;
    private EditText etFromDate, etToDate, etRefno;
    private Button btnSearch, btnReset;
    Spinner spnStatus, spnRefType, spnPaymentMethod;

    String filStatus = "All";

    private int mYear;
    private int mMonth;
    private int mDay;

    private int day_trx_checking;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.transaction_history));

        etFromDate = (EditText) rootView.findViewById(R.id.etFromDate);
        etToDate = (EditText) rootView.findViewById(R.id.etToDate);
        etRefno = (EditText) rootView.findViewById(R.id.etRefno);

        String[] reference_filter = {getResources().getString(R.string.payment_ref), getResources().getString(R.string.mer_ref)};
        //--Edited 25/07/18 by KJ--//
        String[] status_filter = {getResources().getString(R.string.all), getResources().getString(R.string.accepted), getResources().getString(R.string.accepted_adj), getResources().getString(R.string.authorized), getResources().getString(R.string.pending), getResources().getString(R.string.rejected), getResources().getString(R.string.voided), getResources().getString(R.string.refunded), getResources().getString(R.string.partialrefunded), getResources().getString(R.string.cancelled)};
        //String[] pMethod_filter= {getResources().getString(R.string.all), getResources().getString(R.string.print_payMethod_alipayHKOffline), getResources().getString(R.string.print_payMethod_wechatOffline)};
        //--done Edited 25/07/18 by KJ--//
        spnStatus = (Spinner) rootView.findViewById(R.id.spnStatus);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_layout, status_filter);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(adapter);

        //--Edited 25/07/18 by KJ--//
//        spnPaymentMethod = (Spinner) rootView.findViewById(R.id.spnPaymentMethod);

//        ArrayAdapter<String> adapter3 =new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_layout, pMethod_filter);
//        adapter3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//        spnPaymentMethod.setAdapter(adapter3);
        //--done Edited 25/07/18 by KJ--//

        spnRefType = (Spinner) rootView.findViewById(R.id.spnRefType);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_dropdown_layout, reference_filter);
        adapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnRefType.setAdapter(adapter2);

        spnRefType.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        }) ;

        spnStatus.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        }) ;

        spnStatus.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                //--Edited 25/07/18 by KJ--//
                if (position == 0) {
                    filStatus = "All";
                } else if (position == 1) {
                    filStatus = "Accepted";
                } else if (position == 2) {
                    filStatus = "Accepted_Adj";
                } else if (position == 3) {
                    filStatus = "Authorized";
                } else if (position == 4) {
                    filStatus = "Pending";
                } else if (position == 5) {
                    filStatus = "Rejected";
                } else if (position == 6) {
                    filStatus = "Voided";
                } else if (position == 7) {
                    filStatus = "Refunded";
                } else if (position == 8) {
                    filStatus = "PartialRefunded";
                } else if (position == 9) {
                    filStatus = "Cancelled";
                }
                //--done Edited 25/07/18 by KJ--//
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        Date curDate = new Date();
        String strDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(curDate);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(prevDate); // convert your date to Calendar object
//        cal.add(Calendar.DATE, -1);
//        prevDate = cal.getTime();
//        String strDate1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(prevDate);
        etFromDate.setText(strDate);

        etFromDate.setOnClickListener(new EditText.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                hideKeyboard(arg0);
                showDatePickerFrom();

            }
        });

//        Date curDate = new Date();
//        String strDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(curDate);
        etToDate.setText(strDate);
        etToDate.setOnClickListener(new EditText.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                hideKeyboard(arg0);
                showDatePickerTo();
            }
        });

        btnReset = (Button) rootView.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilter();
            }
        });

        btnSearch = (Button) rootView.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Check if Valid Date
                Boolean parseSuccess;
                try {
                    fromDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(etFromDate.getText().toString());
                    toDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(etToDate.getText().toString());
                    parseSuccess = true;
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    parseSuccess = false;
                }
                if (parseSuccess == true) {
                    if (fromDate.after(toDate)) {
                        Toast.makeText(getActivity(), getString(R.string.invalid_date), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                String filterRef;
                filterRef = spnRefType.getSelectedItem().toString();
                if (filterRef.equalsIgnoreCase(getString(R.string.mer_ref))) {
                    filterRef = "MerRef";

                } else {
                    filterRef = "PayRef";
                }

                Intent his_list = new Intent(getActivity(), History_List.class);
                his_list.putExtra("filterStatus", filStatus);
                System.out.println("dswdawdasd: " + getArguments().getString(Constants.MERID));
                his_list.putExtra(Constants.MERID, getArguments().getString(Constants.MERID));
                his_list.putExtra(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                his_list.putExtra("fromDate", etFromDate.getText().toString());
                his_list.putExtra("toDate", etToDate.getText().toString());
                his_list.putExtra("refno", etRefno.getText().toString());
                his_list.putExtra("refFilter", filterRef);

                startActivity(his_list);
            }

        });

        etRefno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        SharedPreferences pref = getActivity().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        day_trx_checking = pref.getInt(Constants.pref_transaction_checking, Constants.default_transaction_checking);

        return rootView;
    }

    private void showDatePickerFrom() {
        try {
            //Set Up Current Date Into dialog
            String fromDate = etFromDate.getText().toString();
            SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
            Date fd = fDate.parse(fromDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fd);

            //DatePicker Dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

                    String month = String.format(Locale.US, "%02d", monthOfYear + 1);
                    String day = String.format(Locale.US, "%02d", dayOfMonth);
                    etFromDate.setText(

                            new StringBuilder()
                                    // Month is 0 based so add 1

                                    .append(day).append("/")
                                    .append(month).append("/")
                                    .append(year));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            //Set up min and max date
            Calendar cMin = Calendar.getInstance();
            cMin.add(Calendar.DATE, -(day_trx_checking -1));
            datePickerDialog.getDatePicker().setMinDate(cMin.getTimeInMillis());

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+ (1000 * 60 * 60));
            datePickerDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDatePickerTo() {
        try {

            String toDate = etToDate.getText().toString();
            SimpleDateFormat tDate = new SimpleDateFormat("dd/MM/yyyy");
            Date td = tDate.parse(toDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(td);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

                    String month = String.format(Locale.US, "%02d", monthOfYear + 1);
                    String day = String.format(Locale.US, "%02d", dayOfMonth);
                    etToDate.setText(

                            new StringBuilder()
                                    // Month is 0 based so add 1

                                    .append(day).append("/")
                                    .append(month).append("/")
                                    .append(year));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            Calendar cMin = Calendar.getInstance();
            cMin.add(Calendar.DATE, -(day_trx_checking -1));
            datePickerDialog.getDatePicker().setMinDate(cMin.getTimeInMillis());

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
            datePickerDialog.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void resetFilter() {

        //Reset From Date
        Date prevDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(prevDate); // convert your date to Calendar object
        cal.add(Calendar.DATE, -1);
        prevDate = cal.getTime();
        String strDate1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(prevDate);


        //Reset To Date
        Date curDate = new Date();
        String strDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(curDate);
        etToDate.setText(strDate);
        etFromDate.setText(strDate);

        spnStatus.setSelection(0);
        spnRefType.setSelection(0);
        //--Edited 25/07/18 by KJ--//
//        spnPaymentMethod.setSelection(0);
        //--done Edited 25/07/18 by KJ--//
        etRefno.setText("");
    }

}
