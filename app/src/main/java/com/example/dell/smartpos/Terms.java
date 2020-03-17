package com.example.dell.smartpos;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.common.io.LineReader;

import java.util.Date;

public class Terms extends AppCompatActivity {

    //---------for autologout---------//
    Handler CheckTimeOutHandler = new Handler();
    Date lastUpdateTime;//lastest operation time
    long timePeriod;//no operation time
    float SESSION_EXPIRED = Constants.SESSION_EXPIRY / 1000;//session expired time
    boolean CheckLogout = false;//logout flag
    long CheckInterval = 1000;//checking time intercal
    //---------for autologout---------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        setTitle(R.string.terms_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---------for autologout---------//
        SharedPreferences Sessionpref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        CheckLogout = Sessionpref.getBoolean(Constants.CheckLogout, false);
        lastUpdateTime = new Date(System.currentTimeMillis());//init lastest operation time
        Log.d("OTTO", "init Terms CheckLogout:" + CheckLogout + ",lastUpdateTime:" + lastUpdateTime);
        //---------for autologout---------//

        TextView terms = (TextView) findViewById(R.id.terms);
        final TextView cont_terms = (TextView) findViewById(R.id.cont_terms);
        TextView mposSW = (TextView) findViewById(R.id.mposSW);
        final RelativeLayout cont_mposSW = (RelativeLayout) findViewById(R.id.cont_mposSW);
        TextView mposHW = (TextView) findViewById(R.id.mposHW);
        final RelativeLayout cont_mposHW = (RelativeLayout) findViewById(R.id.cont_mposHW);
        TextView privacy = (TextView) findViewById(R.id.privacy);
        final RelativeLayout cont_privacy = (RelativeLayout) findViewById(R.id.cont_privacy);
        TextView ownership = (TextView) findViewById(R.id.ownership);
        final TextView cont_ownership = (TextView) findViewById(R.id.cont_ownership);
        TextView termination = (TextView) findViewById(R.id.termination);
        final TextView cont_termination = (TextView) findViewById(R.id.cont_termination);
        TextView notification = (TextView) findViewById(R.id.notification);
        final TextView cont_notification = (TextView) findViewById(R.id.cont_notification);
        TextView questions = (TextView) findViewById(R.id.questions);
        final TextView cont_questions = (TextView) findViewById(R.id.cont_questions);

        terms.setOnClickListener(new View.OnClickListener() {
            int height;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cont_terms.getVisibility() == View.VISIBLE) {
                    MyCustomAnimation a = new MyCustomAnimation(cont_terms, 1000, MyCustomAnimation.COLLAPSE);
                    height = a.getHeight();
                    cont_terms.startAnimation(a);
                } else {
                    MyCustomAnimation a = new MyCustomAnimation(cont_terms, 1000, MyCustomAnimation.EXPAND);
                    a.setHeight(height);
                    cont_terms.startAnimation(a);
                }
            }
        });

        mposSW.setOnClickListener(new View.OnClickListener() {
            int height;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cont_mposSW.getVisibility() == RelativeLayout.VISIBLE) {
                    MyCustomAnimation a = new MyCustomAnimation(cont_mposSW, 1000, MyCustomAnimation.COLLAPSE);
                    height = a.getHeight();
                    cont_mposSW.startAnimation(a);
                } else {
                    MyCustomAnimation a = new MyCustomAnimation(cont_mposSW, 1000, MyCustomAnimation.EXPAND);
                    a.setHeight(height);
                    cont_mposSW.startAnimation(a);
                }
            }
        });

        mposHW.setOnClickListener(new View.OnClickListener() {
            int height;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cont_mposHW.getVisibility() == RelativeLayout.VISIBLE) {
                    MyCustomAnimation a = new MyCustomAnimation(cont_mposHW, 1000, MyCustomAnimation.COLLAPSE);
                    height = a.getHeight();
                    cont_mposHW.startAnimation(a);
                } else {
                    MyCustomAnimation a = new MyCustomAnimation(cont_mposHW, 1000, MyCustomAnimation.EXPAND);
                    a.setHeight(height);
                    cont_mposHW.startAnimation(a);
                }
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            int height;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cont_privacy.getVisibility() == View.VISIBLE) {
                    MyCustomAnimation a = new MyCustomAnimation(cont_privacy, 1000, MyCustomAnimation.COLLAPSE);
                    height = a.getHeight();
                    cont_privacy.startAnimation(a);
                } else {
                    MyCustomAnimation a = new MyCustomAnimation(cont_privacy, 1000, MyCustomAnimation.EXPAND);
                    a.setHeight(height);
                    cont_privacy.startAnimation(a);
                }
            }
        });

        ownership.setOnClickListener(new View.OnClickListener() {
            int height;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cont_ownership.getVisibility() == View.VISIBLE) {
                    MyCustomAnimation a = new MyCustomAnimation(cont_ownership, 1000, MyCustomAnimation.COLLAPSE);
                    height = a.getHeight();
                    cont_ownership.startAnimation(a);
                } else {
                    MyCustomAnimation a = new MyCustomAnimation(cont_ownership, 1000, MyCustomAnimation.EXPAND);
                    a.setHeight(height);
                    cont_ownership.startAnimation(a);
                }
            }
        });

        termination.setOnClickListener(new View.OnClickListener() {
            int height;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cont_termination.getVisibility() == View.VISIBLE) {
                    MyCustomAnimation a = new MyCustomAnimation(cont_termination, 1000, MyCustomAnimation.COLLAPSE);
                    height = a.getHeight();
                    cont_termination.startAnimation(a);
                } else {
                    MyCustomAnimation a = new MyCustomAnimation(cont_termination, 1000, MyCustomAnimation.EXPAND);
                    a.setHeight(height);
                    cont_termination.startAnimation(a);
                }
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            int height;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cont_notification.getVisibility() == View.VISIBLE) {
                    MyCustomAnimation a = new MyCustomAnimation(cont_notification, 1000, MyCustomAnimation.COLLAPSE);
                    height = a.getHeight();
                    cont_notification.startAnimation(a);
                } else {
                    MyCustomAnimation a = new MyCustomAnimation(cont_notification, 1000, MyCustomAnimation.EXPAND);
                    a.setHeight(height);
                    cont_notification.startAnimation(a);
                }
            }
        });


        questions.setOnClickListener(new View.OnClickListener() {
            int height;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cont_questions.getVisibility() == View.VISIBLE) {
//                    MyCustomAnimation a = new MyCustomAnimation(cont_questions, 1000, MyCustomAnimation.COLLAPSE);
//                    height = a.getHeight();
//                    cont_questions.startAnimation(a);
                    cont_questions.setVisibility(View.GONE);


                } else {
//                    MyCustomAnimation a = new MyCustomAnimation(cont_questions, 1000, MyCustomAnimation.EXPAND);
//                    a.setHeight(height);
//                    cont_questions.startAnimation(a);
                    cont_questions.setVisibility(View.VISIBLE);

                    final ScrollView s = (ScrollView) findViewById(R.id.scroll_term);
                    s.post(new Runnable() {
                        public void run() {
                            s.fullScroll(s.FOCUS_DOWN);
                            //        s.smoothScrollTo(0, s.getBottom());
                        }
                    });

                }
            }
        });


    }

    //-------------------------------------auto logout-------------------------------------//
    /**
     * Time counter Thread
     */
    private Runnable checkTimeOutTask = new Runnable() {

        public void run() {
            Date timeNow = new Date(System.currentTimeMillis());
            //calculate no operation time
            /*curTime - lastest opt time = no opt time*/
            timePeriod = timeNow.getTime() - lastUpdateTime.getTime();

            /*converted into second*/
            float timePeriodSecond = ((float) timePeriod / 1000);

            if (CheckLogout) {
                /* do logout */
                Log.d("OTTO", "做登出操作" + this.getClass());
                //logout flag change to true
                CheckLogout = true;
//                autologout();
            } else {
                if (timePeriodSecond > SESSION_EXPIRED) {
                    /* do logout */
                    Log.d("OTTO", "做登出操作" + this.getClass());
                    //logout flag change to true
                    CheckLogout = true;
//                    autologout();
                } else {
                    Log.d("OTTO", "没有超过规定时长" + this.getClass());
                }
            }

            if (!CheckLogout) {
                //check no opt time per 'CheckInterval' sencond
                CheckTimeOutHandler.postDelayed(checkTimeOutTask, CheckInterval);
            }
        }
    };

    //listen touch movement
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("OTTO", "onTouchEvent checklogout:" + CheckLogout);
        if (!CheckLogout) {
            updateUserActionTime();
        }
        return super.dispatchTouchEvent(event);
    }

    //reset no opt time and lastest opt time when user opt
    public void updateUserActionTime() {
        Date timeNow = new Date(System.currentTimeMillis());
        timePeriod = timeNow.getTime() - lastUpdateTime.getTime();
        lastUpdateTime.setTime(timeNow.getTime());
    }

//    public void autologout() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
//        SharedPreferences.Editor edit = pref.edit();
//        edit.putBoolean(Constants.CheckLogout, true);
//        edit.commit();
//    }

    @Override
    protected void onResume() {
        //start check timeout thread when activity show
        CheckTimeOutHandler.postAtTime(checkTimeOutTask, CheckInterval);
        super.onResume();
    }

    @Override
    protected void onPause() {
        /*activity不可见的时候取消线程*/
        //stop check timeout thread when activity no show
        CheckTimeOutHandler.removeCallbacks(checkTimeOutTask);
        super.onPause();
    }
    //-------------------------------------auto logout-------------------------------------//

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
