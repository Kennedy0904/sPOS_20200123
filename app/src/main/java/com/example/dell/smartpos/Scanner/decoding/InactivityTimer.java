package com.example.dell.smartpos.Scanner.decoding;

import android.app.Activity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/11/12.
 */

public final class InactivityTimer {

    //--Edited 25/07/18 by KJ--//
    private int INACTIVITY_DELAY_SECONDS;
    //--done Edited 25/07/18 by KJ--//

    private final ScheduledExecutorService inactivityTimer =
            Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
    private final Activity activity;
    private ScheduledFuture<?> inactivityFuture = null;

    //--Edited 25/07/18 by KJ--//
    public InactivityTimer(Activity activity, int timeout) {
        this.activity = activity;
        this.INACTIVITY_DELAY_SECONDS = timeout * 60;
        onActivity();
    }
    //--done Edited 25/07/18 by KJ--//

    public void onActivity() {
        cancel();
        inactivityFuture = inactivityTimer.schedule(new FinishListener(activity),
                INACTIVITY_DELAY_SECONDS,
                TimeUnit.SECONDS);
    }

    private void cancel() {
        if (inactivityFuture != null) {
            inactivityFuture.cancel(true);
            inactivityFuture = null;
        }
    }

    public void shutdown() {
        cancel();
        inactivityTimer.shutdown();
    }

    private static final class DaemonThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    }

}
