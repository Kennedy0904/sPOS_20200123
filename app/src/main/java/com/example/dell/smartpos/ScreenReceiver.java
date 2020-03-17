package com.example.dell.smartpos;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

    private final String TAG = "ScreenReceiver";
    PowerManager.WakeLock wakeLock;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {

            Log.d(TAG, "ScreenOn");

            // Release wakelock if it exists
            if(wakeLock != null){
                wakeLock.release();
            }

        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {

            Log.d(TAG, "ScreenOff");

            // Wake Up Device
            PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "myapp:TAG");
            wakeLock.acquire();

            // Disable keyguard
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
            keyguardLock.disableKeyguard();
        }
    }
}
