package com.example.dell.smartpos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class LocalCacheUtil {

    //this java for partnerlogo cache func    if PartnerLogoUtil.java get network bitmap==null then enter this java and check where the pic save in local
    private static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/partnerlogo";

    public void setBitmapToLocal(String filename, Bitmap bitmap) {
        try {
            File file = new File(CACHE_PATH, filename);

            File parentFile = file.getParentFile();

            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromLocal(String filename) {
        try {
            File file = new File(CACHE_PATH, filename);
            Log.d("OTTO", file.toString());
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
