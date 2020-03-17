package com.example.dell.smartpos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    Date fromDate, toDate;
    private EditText etFromDate, etToDate;

    int mYear, mMonth, mDay;

    boolean valid;

    private LinearLayout summaryreport, transactionreport, refundreport, requestrefundreport, voidreport;

    private int day_trx_checking;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.reports));

        etFromDate = (EditText) rootView.findViewById(R.id.etReportFromDate);
        etToDate = (EditText) rootView.findViewById(R.id.etReportToDate);

        Date curDate = new Date();
        String strDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(curDate);
//        Date prevDate = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime ( prevDate ); // convert your date to Calendar object
//        cal.add(Calendar.DATE, -1);
//        prevDate = cal.getTime();

//        String strDate1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(prevDate);
        etFromDate.setText(strDate);

        etFromDate.setOnClickListener(new EditText.OnClickListener(){
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                showDatePickerFrom();

            }
        });


        etToDate.setText(strDate);
        etToDate.setOnClickListener(new EditText.OnClickListener(){

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                showDatePickerTo();
            }
        });

        summaryreport = (LinearLayout) rootView.findViewById(R.id.summaryreport);

        summaryreport.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                validateDate();

                if(valid == true){
                    valid = false;
                    Intent summary = new Intent(getActivity(), SummaryReport.class);
                    summary.putExtra(Constants.MERID, getArguments().getString(Constants.MERID));
                    summary.putExtra(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                    summary.putExtra(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
                    summary.putExtra("fromDate", etFromDate.getText().toString());
                    summary.putExtra("toDate", etToDate.getText().toString());
                    startActivity(summary);
                }
            }

        });

        transactionreport = (LinearLayout) rootView.findViewById(R.id.transactionreport);

        transactionreport.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                validateDate();

                if(valid == true){
//                    valid = false;
//                    Intent summary = new Intent(getActivity(), TransactionReport.class);
//                    summary.putExtra(Constants.MERID, getArguments().getString(Constants.MERID));
//                    summary.putExtra(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
//                    summary.putExtra(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
//                    summary.putExtra("fromDate", etFromDate.getText().toString());
//                    summary.putExtra("toDate", etToDate.getText().toString());
//                    startActivity(summary);

                    valid = false;
                    Intent summary = new Intent(getActivity(), Report.class);
                    summary.putExtra(Constants.MERID, getArguments().getString(Constants.MERID));
                    summary.putExtra(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                    summary.putExtra(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
                    summary.putExtra("reportType", "Transaction Report");
                    summary.putExtra("fromDate", etFromDate.getText().toString());
                    summary.putExtra("toDate", etToDate.getText().toString());
                    startActivity(summary);
                }
            }

        });

        refundreport = (LinearLayout) rootView.findViewById(R.id.refundreport);

        refundreport.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                validateDate();

                if(valid == true){
//                    valid = false;
//                    Intent summary = new Intent(getActivity(), RefundReport.class);
//                    summary.putExtra(Constants.MERID, getArguments().getString(Constants.MERID));
//                    summary.putExtra(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
//                    summary.putExtra(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
//                    summary.putExtra("fromDate", etFromDate.getText().toString());
//                    summary.putExtra("toDate", etToDate.getText().toString());
//                    startActivity(summary);

                    valid = false;
                    Intent summary = new Intent(getActivity(), Report.class);
                    summary.putExtra(Constants.MERID, getArguments().getString(Constants.MERID));
                    summary.putExtra(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                    summary.putExtra(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
                    summary.putExtra("reportType", "Refund Report");
                    summary.putExtra("fromDate", etFromDate.getText().toString());
                    summary.putExtra("toDate", etToDate.getText().toString());
                    startActivity(summary);
                }
            }

        });

        requestrefundreport = (LinearLayout) rootView.findViewById(R.id.requestrefundreport);

        requestrefundreport.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                validateDate();

                if(valid == true){
//                    valid = false;
//                    Intent summary = new Intent(getActivity(), RequestRefundReport.class);
//                    summary.putExtra(Constants.MERID, getArguments().getString(Constants.MERID));
//                    summary.putExtra(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
//                    summary.putExtra(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
//                    summary.putExtra("fromDate", etFromDate.getText().toString());
//                    summary.putExtra("toDate", etToDate.getText().toString());
//                    startActivity(summary);

                    valid = false;
                    Intent summary = new Intent(getActivity(), Report.class);
                    summary.putExtra(Constants.MERID, getArguments().getString(Constants.MERID));
                    summary.putExtra(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                    summary.putExtra(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
                    summary.putExtra("reportType", "Request Refund Report");
                    summary.putExtra("fromDate", etFromDate.getText().toString());
                    summary.putExtra("toDate", etToDate.getText().toString());
                    startActivity(summary);
                }
            }

        });

        voidreport = (LinearLayout) rootView.findViewById(R.id.voidreport);

        voidreport.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                validateDate();

                if(valid == true){
//                    valid = false;
//                    Intent summary = new Intent(getActivity(), VoidReport.class);
//                    summary.putExtra(Constants.MERID, getArguments().getString(Constants.MERID));
//                    summary.putExtra(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
//                    summary.putExtra(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
//                    summary.putExtra("fromDate", etFromDate.getText().toString());
//                    summary.putExtra("toDate", etToDate.getText().toString());
//                    startActivity(summary);

                    valid = false;
                    Intent summary = new Intent(getActivity(), Report.class);
                    summary.putExtra(Constants.MERID, getArguments().getString(Constants.MERID));
                    summary.putExtra(Constants.MERNAME, getArguments().getString(Constants.MERNAME));
                    summary.putExtra(Constants.CURRCODE, getArguments().getString(Constants.CURRCODE));
                    summary.putExtra("reportType", "Void Report");
                    summary.putExtra("fromDate", etFromDate.getText().toString());
                    summary.putExtra("toDate", etToDate.getText().toString());
                    startActivity(summary);
                }
            }

        });

        SharedPreferences pref = getActivity().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        day_trx_checking = pref.getInt(Constants.pref_transaction_checking, Constants.default_transaction_checking);

        // Inflate the layout for this fragment
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

    DatePickerDialog.OnDateSetListener onFromDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String month = String.format(Locale.US, "%02d", monthOfYear +1);
            String day = String.format(Locale.US, "%02d", dayOfMonth );
            etFromDate.setText(
                    new StringBuilder()
                            // Month is 0 based so add 1

                            .append(day).append("/")
                            .append(month).append("/")
                            .append(year));
        }
    };

    DatePickerDialog.OnDateSetListener onToDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String month = String.format(Locale.US, "%02d", monthOfYear +1);
            String day = String.format(Locale.US, "%02d", dayOfMonth );
            etToDate.setText(
                    new StringBuilder()
                            // Month is 0 based so add 1

                            .append(day).append("/")
                            .append(month).append("/")
                            .append(year));
        }
    };

    private boolean validateDate(){
        //Check if Valid Date
        Boolean parseSuccess;
        try {
            fromDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(etFromDate.getText().toString());
            toDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(etToDate.getText().toString());
            parseSuccess = true;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            parseSuccess=false;
        }
        if(parseSuccess==true){
            if(fromDate.after(toDate)){
                Toast.makeText(getActivity(), getString(R.string.invalid_date), Toast.LENGTH_SHORT).show();
                valid = false;

            }else{
                valid = true;
            }
        }
        return valid;
    }

}
