package com.example.dell.smartpos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StationeryOrder extends AppCompatActivity {

    private Button btnAdd;
    private Button btnMinus;
    private Button btnOrder;
    private TextView txtQty;

    String merchantId  = "";
    String merchantName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationary_oder);
        this.setTitle(R.string.stationery_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent stationaryIntent = getIntent();
        merchantId = stationaryIntent.getStringExtra(Constants.MERID);
        merchantName = stationaryIntent.getStringExtra(Constants.MERNAME);

        btnAdd = (Button)findViewById(R.id.addQty);
        btnMinus = (Button)findViewById(R.id.minusQty);
        btnOrder = (Button)findViewById(R.id.btnOrder);

        txtQty = (TextView)findViewById(R.id.qty);

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String qtyString = txtQty.getText().toString();
                int qty = Integer.parseInt(qtyString) + 10;

                if(qtyString.equals("100")){
                    txtQty.setText(Integer.toString(100));
                }else{
                    txtQty.setText(Integer.toString(qty));
                }
            }

        });

        btnMinus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String qtyString = txtQty.getText().toString();
                int qty = Integer.parseInt(qtyString) - 10;

                if(qtyString.equals("10")){
                    txtQty.setText(Integer.toString(10));
                }else{
                    txtQty.setText(Integer.toString(qty));
                }

            }

        });

        btnOrder.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(StationeryOrder.this, DialogSendOrder.class);
                intent.putExtra(Constants.MERID, merchantId);
                intent.putExtra(Constants.MERNAME, merchantName);
                intent.putExtra("orderQty", txtQty.getText());
                startActivity(intent);

            }

        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
