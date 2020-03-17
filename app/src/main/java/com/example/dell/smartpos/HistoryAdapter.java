package com.example.dell.smartpos;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HistoryAdapter extends ArrayAdapter<Record> {

    Context context;
    int layoutResourceId;
    Record record;
    ArrayList<Record> data = null;

    String merName = "";
    String paymethod = "";
    String paytype = "";

    public HistoryAdapter(Context context, int layoutResourceId, ArrayList<Record> data, String filterOption, String merName) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.merName = merName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtAmount = (TextView) row.findViewById(R.id.amount);
            holder.txtMerRequestAmt = (TextView) row.findViewById(R.id.merRequestAmt_History);
            holder.txtSurcharge = (TextView) row.findViewById(R.id.Surcharge_History);
            holder.txtPayType = (TextView) row.findViewById(R.id.ptype);
            holder.txtPayMethod = (TextView) row.findViewById(R.id.pmtd);
            holder.txtMref = (TextView) row.findViewById(R.id.mref);
            holder.txtPref = (TextView) row.findViewById(R.id.pref);
            holder.txtStatus = (TextView) row.findViewById(R.id.status);
//            holder.txtRemark = (TextView)row.findViewById(R.id.remark);
            holder.txtTime = (TextView) row.findViewById(R.id.date);
//            holder.btnAction = (Button)row.findViewById(R.id.btnAction);
            holder.txtCurrCode = (TextView) row.findViewById(R.id.transactionreport_currCode);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        //---------judge is airpay or not-----------//
        SharedPreferences merDetails = context.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
        String hideSurcharge = merDetails.getString(Constants.pref_hideSurcharge, "");
        try {
            DesEncrypter encrypter = new DesEncrypter(merName);
            hideSurcharge = encrypter.decrypt(hideSurcharge);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("OTTO", "+++++++HistoryAdapter hideSurcharge:" + hideSurcharge);
        //---------judge is airpay or not----------//

        record = data.get(position);
        paymethod = record.payMethod;
        paytype = record.payType;

        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        record.amt = record.amt.replaceAll(",", "");
        record.amt = formatter.format(Double.parseDouble(record.amt));
        record.merRequestAmt = formatter.format(Double.parseDouble(record.merRequestAmt));
        record.merRequestAmt = record.merRequestAmt.replaceAll(",", "");

//		 if( ("T".equals(hideSurcharge))&&("WECHATOFFL".equals(paymethod)||"WECHATONL".equals(paymethod)||"ALIPAYOFFL".equals(paymethod)||"ALIPAYCNOFFL".equals(paymethod)) ){
//            holder.txtAmount.setText(record.merRequestAmt);
//        }else{
//            holder.txtAmount.setText(record.amt);
//        }
        holder.txtAmount.setText(record.amt);
        if ((record.merRequestAmt == null) || ("".equals(record.merRequestAmt))) {
            holder.txtMerRequestAmt.setText(record.amt);
        } else {
            if (Double.valueOf(record.merRequestAmt) <= 0) {
                holder.txtMerRequestAmt.setText(record.amt);
            } else {
                holder.txtMerRequestAmt.setText(record.merRequestAmt);
            }
        }
        holder.txtSurcharge.setText(record.Surcharge);
        holder.txtCurrCode.setText(record.currency);
        holder.txtMref.setText(record.merref);
        holder.txtPref.setText(record.paymentRef);

        //--Edited 25/07/18 by KJ--//

        if (record.payMethod.equalsIgnoreCase("Master")) {
            holder.txtPayMethod.setText(context.getString(R.string.MasterCard));
        } else if (record.payMethod.equalsIgnoreCase("VISA")) {
            holder.txtPayMethod.setText(context.getString(R.string.visa));
        } else {
            holder.txtPayMethod.setText(record.payMethod);
        }

//        if("N".equalsIgnoreCase(record.payType)){
            holder.txtPayType.setText(R.string.sale);
//        }else{
//            holder.txtPayType.setText(R.string.authorize);
//        }

        //--done Edited 25/07/18 by KJ--//

//        holder.txtRemark.setText(record.remark);
//        holder.btnAction.setText(R.string.void_);
//        holder.btnAction.setVisibility(Button.VISIBLE);
//        holder.btnAction.setBackgroundResource(R.drawable.btn_void);
//        holder.btnAction.setVisibility(Button.VISIBLE);
//        holder.btnAction.setText(R.string.details);
//        holder.btnAction.setBackgroundResource(R.drawable.btn_darkblue);

        String status = "";
        if (record.status.equalsIgnoreCase("Authorized")) {
            status = context.getString(R.string.authorized);
        } else if (record.status.equalsIgnoreCase("Accepted")) {
            status = context.getString(R.string.accepted);
            holder.txtStatus.setTextColor(Color.parseColor("#0b609a"));
        } else if (record.status.equalsIgnoreCase("Rejected")) {
            status = context.getString(R.string.rejected);
            holder.txtStatus.setTextColor(Color.parseColor("#EC340C"));
        } else if (record.status.equalsIgnoreCase("Refunded")) {
            status = context.getString(R.string.refunded);
            holder.txtStatus.setTextColor(Color.parseColor("#008080"));
        } else if (record.status.equalsIgnoreCase("Voided")) {
            status = context.getString(R.string.voided);
            holder.txtStatus.setTextColor(Color.parseColor("#008080"));
        } else if (record.status.equalsIgnoreCase("PartialRefunded")) {
            status = context.getString(R.string.partialrefunded);
            holder.txtStatus.setTextColor(Color.parseColor("#008080"));
        } else if (record.status.equalsIgnoreCase("Accepted_Adj")) {
            status = context.getString(R.string.accepted_adj);
            holder.txtStatus.setTextColor(Color.parseColor("#0b609a"));
        } else if (record.status.equalsIgnoreCase("Pending")) {
            status = context.getString(R.string.pending);
            holder.txtStatus.setTextColor(Color.parseColor("#9c9c9c"));
        } else if (record.status.equalsIgnoreCase("RequestRefund")) {
            status = context.getString(R.string.requestRefund);
            holder.txtStatus.setTextColor(Color.parseColor("#ff8300"));
        }  else if (record.status.equalsIgnoreCase("Cancelled")) {
            status = context.getString(R.string.cancelled);
            holder.txtStatus.setTextColor(Color.parseColor("#ffce00"));
        } else {
            status = record.status;
            holder.txtStatus.setTextColor(Color.parseColor("#9c9c9c"));
        }
        holder.txtStatus.setText(status);

        String newtime = record.getOrderdate();
        String date = newtime.substring(0, 8);
        String time = newtime.substring(8, 14);

        StringBuilder sbdate = new StringBuilder(date);
        sbdate.insert(2, "/");
        sbdate.insert(5, "/");
        StringBuilder sbtime = new StringBuilder(time);
        sbtime.insert(2, ":");
        sbtime.insert(5, ":");
        holder.txtTime.setText(sbdate.toString() + "\n" + sbtime.toString());
        return row;
    }

    static class RecordHolder {
        TextView txtTime;
        TextView txtRemark;
        TextView txtStatus;
        TextView txtPref;
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtCurrCode;
        TextView txtAmount;
        TextView txtMerRequestAmt;
        TextView txtSurcharge;
        TextView txtMref;
        TextView txtPayType;
        TextView txtPayMethod;
        Button btnAction;
    }

}

