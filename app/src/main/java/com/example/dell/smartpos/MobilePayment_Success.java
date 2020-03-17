package com.example.dell.smartpos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MobilePayment_Success extends AppCompatActivity {

    private TextView txtStep2, txtStep3, txtStep4;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_payment__success);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        txtStep2 = (TextView) findViewById(R.id.progress_mobile_text_2);
        txtStep3 = (TextView) findViewById(R.id.progress_mobile_text_3);
        txtStep4 = (TextView) findViewById(R.id.progress_mobile_text_4);

        txtStep2.setTypeface(null, Typeface.BOLD);
        txtStep3.setTypeface(null, Typeface.BOLD);
        txtStep4.setTypeface(null, Typeface.BOLD);

        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(MobilePayment_Success.this, MainActivity.class);
                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        Intent intent = new Intent(MobilePayment_Success.this, MainActivity.class);
        startActivity(intent);
        return true;
    }
}
