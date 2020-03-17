package com.example.dell.smartpos;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeMap;

import static android.content.Context.MODE_PRIVATE;

public class ReportAdapter extends ArrayAdapter<Record> {

    Context context;
    int layoutResourceId;
    Record record;
    ArrayList<Record> data = null;

    String merName = "";
    String paymethod = "";

    ReportInterface reportInterface;

    public ReportAdapter(Context context, int layoutResourceId, ArrayList<Record> data, String merName, ReportInterface reportInterface) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.merName = merName;
        this.reportInterface = reportInterface;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TreeMap<String, String> reports = null;
        ArrayList<Record> ALIPAYHKOFFLrecords = null;
        ArrayList<Record> ALIPAYCNOFFLrecords = null;
        ArrayList<Record> ALIPAYOFFLrecords = null;
        ArrayList<Record> BOOSTOFFLrecords = null;
        ArrayList<Record> GCASHOFFLrecords = null;
        ArrayList<Record> GRABPAYOFFLrecords = null;
        ArrayList<Record> masterRecords = null;
        ArrayList<Record> OEPAYOFFLrecords = null;
        ArrayList<Record> PROMPTPAYOFFLRecords = null;
        ArrayList<Record> UNIONPAYOFFLrecords = null;
        ArrayList<Record> visaRecords = null;
        ArrayList<Record> WECHATOFFLrecords = null;
        ArrayList<Record> WECHATHKOFFLrecords = null;
        ArrayList<Record> WECHATONLrecords = null;

        boolean isHeader;

        View row = convertView;
        ReportAdapter.RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ReportAdapter.RecordHolder();

            //Header
            holder.header = (LinearLayout) row.findViewById(R.id.report_header);
            holder.pMethod = (TextView) row.findViewById(R.id.report_pMethod);
            holder.totalTrx = (TextView) row.findViewById(R.id.report_total_trx);
            holder.totalAmt = (TextView) row.findViewById(R.id.report_total_amt);
            holder.txtInitPosition = (TextView) row.findViewById(R.id.initPosition);
            holder.txtDestPosition = (TextView) row.findViewById(R.id.destPosition);

            //layout
            holder.txnIdLayout = (LinearLayout) row.findViewById(R.id.txnIdLayout);
            holder.invoiceNoLayout = (LinearLayout) row.findViewById(R.id.invoiceNoLayout);

            //Body
            holder.txtAmount = (TextView) row.findViewById(R.id.report_amount);
            holder.txtMerRequestAmt = (TextView) row.findViewById(R.id.report_merRequestAmt_History);
            holder.txtSurcharge = (TextView) row.findViewById(R.id.report_Surcharge_History);
            holder.txtMref = (TextView) row.findViewById(R.id.report_mref);
            holder.txtPref = (TextView) row.findViewById(R.id.report_pref);
            holder.txtQRNumber = (TextView) row.findViewById(R.id.report_qr_number);
            holder.txtInvoiceNo = (TextView) row.findViewById(R.id.report_invoice_no);
            holder.txtTime = (TextView) row.findViewById(R.id.report_date);
            holder.txtCurrCode = (TextView) row.findViewById(R.id.report_currCode);
            row.setTag(holder);
        } else {
            holder = (ReportAdapter.RecordHolder) row.getTag();
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
        Log.d("OTTO", "+++++++TransactionReportAdapter hideSurcharge:" + hideSurcharge);
        //---------judge is airpay or not----------//

        record = data.get(position);
        paymethod = record.payMethod;

        System.out.println("--Txn PayMethod: " + paymethod);

        // Modified by Eric 20191011 - Set Payment Method Report Header
        String setHeader = GlobalFunction.setReportHeader(context, paymethod);
        holder.pMethod.setText(setHeader);


        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        record.amt = formatter.format(Double.parseDouble(record.amt.replaceAll(",", "")));
        record.merRequestAmt = formatter.format(Double.parseDouble(record.merRequestAmt.replaceAll(",", "")));
//		 if( ("T".equals(hideSurcharge))&&("WECHATOFFL".equals(paymethod)||"WECHATONL".equals(paymethod)||"ALIPAYOFFL".equals(paymethod)||"ALIPAYCNOFFL".equals(paymethod)) ){
//            holder.txtAmount.setText(record.merRequestAmt);
//        }else{
//            holder.txtAmount.setText(record.amt);
//        }
        double total = 0;

        int i;
        int positionnow = position;
        int initPosition = 0;
        int destPosition = data.size() - 1;

        if (position == 0) {
            holder.txtInitPosition.setText(String.valueOf(initPosition));
        } else {
            if (position > 0 && !data.get(position).getPaymethod().equals(data.get(position - 1).getPaymethod())) {
                initPosition = position;
                holder.txtInitPosition.setText(String.valueOf(initPosition));
            } else if (position > 0 && data.get(position).getPaymethod().equals(data.get(position - 1).getPaymethod())) {

                while (position > 0 && data.get(position).getPaymethod().equals(data.get(position - 1).getPaymethod())) {
                    position--;
                    initPosition = position;
                }

                holder.txtInitPosition.setText((String.valueOf(initPosition)));
            }
        }

        position = positionnow;

        if (position == data.size() - 1) {
            holder.txtDestPosition.setText(String.valueOf(destPosition));
        } else {

            if (position < data.size() - 1 && !data.get(position).getPaymethod().equals(data.get(position + 1).getPaymethod())) {
                destPosition = position;
                holder.txtDestPosition.setText(String.valueOf(destPosition));
            } else if (position < data.size() - 1 && data.get(position).getPaymethod().equals(data.get(position + 1).getPaymethod())) {

                while (position < data.size() - 1 && data.get(position).getPaymethod().equals(data.get(position + 1).getPaymethod())) {
                    position++;
                    destPosition = position;
                }

                holder.txtDestPosition.setText(String.valueOf(destPosition));
            }
        }

        initPosition = Integer.parseInt(holder.txtInitPosition.getText().toString());
        destPosition = Integer.parseInt(holder.txtDestPosition.getText().toString());

        for (i = initPosition; i <= destPosition; i++) {
            total = total + Double.parseDouble(data.get(i).amt.replaceAll(",", ""));
        }

        holder.totalAmt.setText(record.currency + " " + formatter.format(total));
        holder.totalTrx.setText(String.valueOf(destPosition - initPosition + 1));

        holder.txtAmount.setText(record.amt);
        if ((record.merRequestAmt == null) || ("".equals(record.merRequestAmt))) {
            holder.txtMerRequestAmt.setText(record.amt);
        } else {
            if (Double.valueOf(record.merRequestAmt.replaceAll(",", "")) <= 0) {
                holder.txtMerRequestAmt.setText(record.amt);
            } else {
                holder.txtMerRequestAmt.setText(record.merRequestAmt);
            }
        }
        holder.txtSurcharge.setText(record.Surcharge);
        holder.txtCurrCode.setText(record.currency);
        holder.txtMref.setText(record.merref);
        holder.txtPref.setText(record.paymentRef);

        /** Control the txnNo & cardNo layout **/
        if (record.getPaymethod().equalsIgnoreCase("Visa") ||
                record.getPaymethod().equalsIgnoreCase("Master")) {
            holder.txnIdLayout.setVisibility(View.GONE);
            holder.invoiceNoLayout.setVisibility(View.VISIBLE);

            holder.txtInvoiceNo.setText(record.invoiceNo);
        } else {
            holder.txnIdLayout.setVisibility(View.VISIBLE);
            holder.invoiceNoLayout.setVisibility(View.GONE);

            holder.txtQRNumber.setText(record.cardNo);
        }

        String newtime = record.getOrderdate();
        String date = newtime.substring(0, 8);
        String time = newtime.substring(8, 14);

        StringBuilder sbdate = new StringBuilder(date);
        sbdate.insert(2, "/");
        sbdate.insert(5, "/");
        StringBuilder sbtime = new StringBuilder(time);
        sbtime.insert(2, ":");
        sbtime.insert(5, ":");
        holder.txtTime.setText(sbdate.toString() + " " + sbtime.toString());

        position = positionnow;

        if (position == 0) {

            isHeader = true;

        } else {

            if (data.get(position).getPaymethod().equals(data.get(position - 1).getPaymethod())) {
                isHeader = false;
            } else {
                isHeader = true;
            }
        }

        if (isHeader == true) {
            holder.header.setVisibility(View.VISIBLE);

        } else {
            holder.header.setVisibility(View.GONE);
        }

        reports = new TreeMap<String, String>();
        ALIPAYHKOFFLrecords = new ArrayList<>();
        ALIPAYCNOFFLrecords = new ArrayList<>();
        ALIPAYOFFLrecords = new ArrayList<>();
        BOOSTOFFLrecords = new ArrayList<>();
        GCASHOFFLrecords = new ArrayList<>();
        GRABPAYOFFLrecords = new ArrayList<>();
        masterRecords = new ArrayList<>();
        OEPAYOFFLrecords = new ArrayList<>();
        PROMPTPAYOFFLRecords = new ArrayList<>();
        UNIONPAYOFFLrecords = new ArrayList<>();
        visaRecords = new ArrayList<>();
        WECHATOFFLrecords = new ArrayList<>();
        WECHATHKOFFLrecords = new ArrayList<>();
        WECHATONLrecords = new ArrayList<>();

        int ALIPAYHKOFFLcount = 0;
        double ALIPAYHKOFFLtotal = 0;

        int ALIPAYCNOFFLcount = 0;
        double ALIPAYCNOFFLtotal = 0;

        int ALIPAYOFFLcount = 0;
        double ALIPAYOFFLtotal = 0;

        int BOOSTOFFLcount = 0;
        double BOOSTOFFLtotal = 0;

        int GCASHOFFLcount = 0;
        double GCASHOFFLtotal = 0;

        int GRABPAYOFFLcount = 0;
        double GRABPAYOFFLtotal = 0;

        int masterCount = 0;
        double masterTotal = 0;

        int OEPAYOFFLcount = 0;
        double OEPAYOFFLtotal = 0;

        int PROMPTPAYOFFLcount = 0;
        double PROMPTPAYOFFLtotal = 0;

        int UNIONPAYOFFLCount = 0;
        double UNIONPAYOFFLTotal = 0;

        int visaCount = 0;
        double visaTotal = 0;

        int WECHATOFFLcount = 0;
        double WECHATOFFLtotal = 0;

        int WECHATHKOFFLcount = 0;
        double WECHATHKOFFLtotal = 0;

        int WECHATONLcount = 0;
        double WECHATONLtotal = 0;

        for (int a = 0; a < data.size(); a++) {

            record = data.get(a);
//            String amount = formatter.format(Double.parseDouble(record.getamt().replaceAll(",", "")));
            record = new Record(record.PayRef(), record.merref(),
                    record.getOrderdate(), record.currency(), record.getamt(),
                    record.getSurcharge(), record.getMerRequestAmt(),
                    record.remark(), record.orderstatus(), merName,
                    record.getPayType(), record.getPaymethod(),
                    record.getPayBankId(), record.getCardNo(),
                    record.getCardHolder(), record.getSettle(),
                    record.getBankId(),
                    record.getSettleTime(), record.getBatchNo(),
                    record.getTraceNo(), record.getInvoiceNo());

            if (data.get(a).payMethod.equalsIgnoreCase("ALIPAYHKOFFL")) {
                ALIPAYHKOFFLcount++;
                ALIPAYHKOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                ALIPAYHKOFFLrecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("ALIPAYCNOFFL")) {
                ALIPAYCNOFFLcount++;
                ALIPAYCNOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                ALIPAYCNOFFLrecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("BOOSTOFFL")) {
                BOOSTOFFLcount++;
                BOOSTOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                BOOSTOFFLrecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("ALIPAYOFFL")) {
                ALIPAYOFFLcount++;
                ALIPAYOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                ALIPAYOFFLrecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("GCASHOFFL")) {
                GCASHOFFLcount++;
                GCASHOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                GCASHOFFLrecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("GRABPAYOFFL")) {
                GRABPAYOFFLcount++;
                GRABPAYOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                GRABPAYOFFLrecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("MASTER")) {
                masterCount++;
                masterTotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                masterRecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("OEPAYOFFL")) {
                OEPAYOFFLcount++;
                OEPAYOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                OEPAYOFFLrecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("PROMPTPAYOFFL")) {
                PROMPTPAYOFFLcount++;
                PROMPTPAYOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                PROMPTPAYOFFLRecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("UNIONPAYOFFL")) {
                UNIONPAYOFFLCount++;
                UNIONPAYOFFLTotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                UNIONPAYOFFLrecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("VISA")) {
                visaCount++;
                visaTotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                visaRecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("WECHATOFFL")) {
                WECHATOFFLcount++;
                WECHATOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                WECHATOFFLrecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("WECHATHKOFFL")) {
                WECHATHKOFFLcount++;
                WECHATHKOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                WECHATOFFLrecords.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("WECHATONL")) {
                WECHATONLcount++;
                WECHATONLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                WECHATONLrecords.add(record);
            }
        }
        reports.put("ALIPAYHKOFFLTotalTrx", String.valueOf(ALIPAYHKOFFLcount));
        reports.put("ALIPAYHKOFFLTotalAmt", String.valueOf(ALIPAYHKOFFLtotal));

        reports.put("ALIPAYCNOFFLTotalTrx", String.valueOf(ALIPAYCNOFFLcount));
        reports.put("ALIPAYCNOFFLTotalAmt", String.valueOf(ALIPAYCNOFFLtotal));

        reports.put("ALIPAYOFFLTotalTrx", String.valueOf(ALIPAYOFFLcount));
        reports.put("ALIPAYOFFLTotalAmt", String.valueOf(ALIPAYOFFLtotal));

        reports.put("BOOSTOFFLTotalTrx", String.valueOf(BOOSTOFFLcount));
        reports.put("BOOSTOFFLTotalAmt", String.valueOf(BOOSTOFFLtotal));

        reports.put("GCASHOFFLTotalTrx", String.valueOf(GCASHOFFLcount));
        reports.put("GCASHOFFLTotalAmt", String.valueOf(GCASHOFFLtotal));

        reports.put("GRABPAYOFFLTotalTrx", String.valueOf(GRABPAYOFFLcount));
        reports.put("GRABPAYOFFLTotalAmt", String.valueOf(GRABPAYOFFLtotal));

        reports.put("MasterTotalTrx", String.valueOf(masterCount));
        reports.put("MasterTotalAmt", String.valueOf(masterTotal));

        reports.put("OEPAYOFFLTotalTrx", String.valueOf(OEPAYOFFLcount));
        reports.put("OEPAYOFFLTotalAmt", String.valueOf(OEPAYOFFLtotal));

        reports.put("PROMPTPAYOFFLTotalTrx", String.valueOf(PROMPTPAYOFFLcount));
        reports.put("PROMPTPAYOFFLTotalAmt", String.valueOf(PROMPTPAYOFFLtotal));

        reports.put("UNIONPAYOFFLTotalTrx", String.valueOf(UNIONPAYOFFLCount));
        reports.put("UNIONPAYOFFLTotalAmt", String.valueOf(UNIONPAYOFFLTotal));

        reports.put("VisaTotalTrx", String.valueOf(visaCount));
        reports.put("VisaTotalAmt", String.valueOf(visaTotal));

        reports.put("WECHATOFFLTotalTrx", String.valueOf(WECHATOFFLcount));
        reports.put("WECHATOFFLTotalAmt", String.valueOf(WECHATOFFLtotal));

        reports.put("WECHATHKOFFLTotalTrx", String.valueOf(WECHATHKOFFLcount));
        reports.put("WECHATHKOFFLTotalAmt", String.valueOf(WECHATHKOFFLtotal));

        reports.put("WECHATONLTotalTrx", String.valueOf(WECHATONLcount));
        reports.put("WECHATONLTotalAmt", String.valueOf(WECHATONLtotal));

        //Send transactions collected to TransactionReport
        reportInterface.setValues(reports,
                ALIPAYHKOFFLrecords,
                ALIPAYCNOFFLrecords,
                ALIPAYOFFLrecords,
                BOOSTOFFLrecords,
                GCASHOFFLrecords,
                GRABPAYOFFLrecords,
                masterRecords,
                OEPAYOFFLrecords,
                PROMPTPAYOFFLRecords,
                UNIONPAYOFFLrecords,
                visaRecords,
                WECHATOFFLrecords,
                WECHATHKOFFLrecords,
                WECHATONLrecords);

        return row;
    }

    static class RecordHolder {
        TextView pMethod;
        TextView totalTrx;
        TextView totalAmt;
        TextView txtTime;
        TextView txtPref;
        TextView txtCurrCode;
        TextView txtAmount;
        TextView txtMerRequestAmt;
        TextView txtSurcharge;
        TextView txtMref;
        TextView txtQRNumber;
        TextView txtInvoiceNo;
        LinearLayout header;
        LinearLayout txnIdLayout;
        LinearLayout invoiceNoLayout;
        TextView txtInitPosition;
        TextView txtDestPosition;
    }
}
