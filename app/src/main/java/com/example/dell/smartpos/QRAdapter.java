package com.example.dell.smartpos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class QRAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<QRItem> dataSet;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(QRItem item);
    }

    public QRAdapter( ArrayList<QRItem> dataSet, OnItemClickListener listener) {
        this.dataSet = dataSet;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.presentqr_item, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.qrName.setText(dataSet.get(position).getQrName());
        holder.qrIcon.setImageResource(dataSet.get(position).getQrIcon());

        if(!dataSet.get(position).isActive()){
            holder.qrIcon.setAlpha(70);
            holder.qrLayout.setBackgroundResource(R.drawable.icon_dimbackground);
        }

        holder.qrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(dataSet.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout qrLayout;
    ImageView qrIcon;
    TextView qrName;

    public MyViewHolder(View view) {
        super(view);

        qrName = itemView.findViewById(R.id.qrName);
        qrIcon = itemView.findViewById(R.id.qrIcon);
        qrLayout = itemView.findViewById(R.id.qrLayout);
    }
}

class QRItem {

    int qrIcon;
    String qrName;
    boolean active;

    public QRItem(int icon, String name, boolean active) {

        super();
        this.qrIcon = icon;
        this.qrName = name;
        this.active = active;
    }

    public int getQrIcon() {
        return qrIcon;
    }

    public void setQrIcon(int qrIcon) {
        this.qrIcon = qrIcon;
    }

    public String getQrName() {
        return qrName;
    }

    public void setQrName(String qrName) {
        this.qrName = qrName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
