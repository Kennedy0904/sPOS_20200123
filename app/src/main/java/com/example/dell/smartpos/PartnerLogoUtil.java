package com.example.dell.smartpos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class PartnerLogoUtil {


    public static void setImageToImageView(final ImageView imageView, final String imgURL, final String picname) {
        if (imageView != null) {
            LocalCacheUtil initlocalCacheUtil = new LocalCacheUtil();
            Bitmap initbitmap = initlocalCacheUtil.getBitmapFromLocal(picname);
            if (initbitmap != null) {
                initbitmap = createFramedPhoto(initbitmap, 10);
                imageView.setImageBitmap(initbitmap);
                Log.e("OTTO", "本地图片1  ");
            } else {
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Bitmap bitmap = (Bitmap) msg.obj;
                        LocalCacheUtil localCacheUtil = new LocalCacheUtil();
                        if (bitmap != null) {
                            localCacheUtil.setBitmapToLocal(picname, bitmap);
                            bitmap = createFramedPhoto(bitmap, 10);
                            imageView.setImageBitmap(bitmap);
                            Log.e("OTTO", "网络图片  ");
                        } else {
                            bitmap = localCacheUtil.getBitmapFromLocal(picname);
                            if (bitmap != null) {
                                bitmap = createFramedPhoto(bitmap, 10);
                                imageView.setImageBitmap(bitmap);
                                Log.e("OTTO", "本地图片2  ");
                            } else {
                                Log.e("OTTO", "无图片  ");
                            }
                        }
                    }
                };
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Bitmap bitmap = null;
                        try {
                            URL url = new URL(imgURL);
                            Log.e("HAHAHA", "设置图片  " + imgURL);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setRequestMethod("GET");
                            if (conn.getResponseCode() == 200) {
                                InputStream inputStream = conn.getInputStream();
                                bitmap = BitmapFactory.decodeStream(inputStream);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Message msg = new Message();
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        } else {
            Log.d("OTTO", "imageView is null");
        }
    }

    public static Bitmap createFramedPhoto(Bitmap image, float outerRadiusRat) {
        int x = image.getWidth();
        int y = image.getHeight();
        //根据源文件新建一个darwable对象
        Drawable imageDrawable = new BitmapDrawable(image);

        // 新建一个新的输出图片
        Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // 新建一个矩形
        RectF outerRect = new RectF(0, 0, x, y);

        // 产生一个红色的圆角矩形
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);

        // 将源图片绘制到这个圆角矩形上
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        imageDrawable.setBounds(0, 0, x, y);
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        imageDrawable.draw(canvas);
        canvas.restore();

        return output;
    }
}
