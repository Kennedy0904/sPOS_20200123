package com.example.dell.smartpos.Scanner.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;
import com.example.dell.smartpos.Scanner.camera.CameraManager;
import com.google.zxing.ResultPoint;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Administrator on 2016/11/12.
 */

public final class ViewfinderView extends View {

    //set amount
    public static String amount = "";
    public static double amt;
    public static String currCode = "";
    public static String mdr = "";
    public static String hideSurcharge = "";
    public static String surC = "";
    public static String pMothod = "";
    public static String lang = "";
    public static Bitmap bmp2;

    //frame refresh time
    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 0xFF;

    //frame corner lenght
    private int ScreenRate;

    //frame corner width
    private static final int CORNER_WIDTH = 10;

    //scanning line width
    private static final int MIDDLE_LINE_WIDTH = 6;

    //scanning line padding to left and right
    private static final int MIDDLE_LINE_PADDING = 5;

    //every refresh    line moving distance
    private static final int SPEEN_DISTANCE = 5;

    //screen pixels
    private static float density;


    private static final int TEXT_PADDING_TOP = 30;
    private static final int TEXT_PADDING_LEFT = 1;
    private Paint paint;

    private int slideTop;

    private int slideBottom;

    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;

    private final int resultPointColor;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    boolean isFirst;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        density = context.getResources().getDisplayMetrics().density;
        //turn pixels into dp
        ScreenRate = (int) (20 * density);

        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);

        resultPointColor = resources.getColor(R.color.possible_result_points);
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //scanning frame  set its size in camera.CameraManager
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }

        //init line top position and bottom position
        if (!isFirst) {
            isFirst = true;
            slideTop = frame.top;
            slideBottom = frame.bottom;
        }

        //get screen width and height
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        paint.setColor(resultBitmap != null ? resultColor : maskColor);

        //draw out part of scanning frame 's shadow
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
                paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);


        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {

            //draw sanning frame corner
            paint.setColor(Color.GREEN);
            paint.setAlpha(200);
            canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
                    frame.top + CORNER_WIDTH, paint);
            canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
                    + ScreenRate, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
                    frame.top + CORNER_WIDTH, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
                    + ScreenRate, paint);
            canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
                    + ScreenRate, frame.bottom, paint);
            canvas.drawRect(frame.left, frame.bottom - ScreenRate,
                    frame.left + CORNER_WIDTH, frame.bottom, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
                    frame.right, frame.bottom, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
                    frame.right, frame.bottom, paint);


            slideTop += SPEEN_DISTANCE;
            if (slideTop >= frame.bottom) {
                slideTop = frame.top;
            }
            canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH / 2, frame.right - MIDDLE_LINE_PADDING, slideTop + MIDDLE_LINE_WIDTH / 2, paint);


            int canve_width = canvas.getWidth();
            float Text_size = (float) (canve_width / 54 * 5);
            Log.d("TAGcan", String.valueOf(Text_size));

            DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");

            if ((Double.valueOf(mdr) > 0) && ("T".equals(hideSurcharge))) {
                Log.d("OTTO", "ViewfinderView.java mdr:" + mdr + ",hidesurcharge:" + hideSurcharge);
            } else {
                //set text which under the frame  u can show amount here
                amt = Double.parseDouble(amount);
                paint.setColor(Color.RED);
                paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.large_fontSize)); //Text_size
                paint.setAlpha(255);
                paint.setAntiAlias(true);
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText((currCode + "  " + formatter.format(amt)), frame.centerX(), (float) (frame.bottom + (float) TEXT_PADDING_TOP * density), paint);
            }

            if ((Double.valueOf(mdr) > 0) && (!"T".equals(hideSurcharge))) {
                paint.setColor(Color.WHITE);
                paint.setTextSize(25);//Text_size/7*3
                paint.setAlpha(150);
                paint.setAntiAlias(true);
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText((getResources().getString(R.string.include_surcharge) + " " + String.valueOf(Double.valueOf(mdr) * 100) + "%"), frame.centerX(), (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + 10), paint);
            }

            //payment type
            paint.setColor(Color.WHITE);
            paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.xxs_fontSize)); //Text_size
            paint.setAlpha(255);
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            paint.setTextAlign(Paint.Align.LEFT);

            canvas.drawText(getResources().getString(R.string.r_pay_type) + getResources().getString(R.string.sale) , (float) (frame.left + (float) TEXT_PADDING_LEFT * density + ScreenRate), (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + ScreenRate), paint);

            //payment method
            paint.setColor(Color.WHITE);
            paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.xxs_fontSize)); //Text_size
            paint.setAlpha(255);
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            paint.setTextAlign(Paint.Align.LEFT);

            if (pMothod.equals("ALIPAYCNOFFL") || pMothod.equals("ALIPAYOFFL")) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_alipay);
                bmp2 = Bitmap.createScaledBitmap(bmp, ScreenRate, ScreenRate, false);
                //canvas.drawBitmap(bmp2, frame.centerX() - 30, (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + 45), paint);
            } else if (pMothod.equals("ALIPAYHKOFFL")) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_alipayhk);
                bmp2 = Bitmap.createScaledBitmap(bmp, ScreenRate, ScreenRate, false);
                //canvas.drawBitmap(bmp2, frame.centerX() - 30, (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + 45), paint);
            } else if (pMothod.equals("WECHATOFFL") || pMothod.equals("WECHATONL")) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_wechatpay);
                bmp2 = Bitmap.createScaledBitmap(bmp, ScreenRate, ScreenRate, false);
                //canvas.drawBitmap(bmp2, frame.centerX() - 30, (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + 45), paint);
            } else if (pMothod.equals("WECHATHKOFFL")) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_wechatpay);
                bmp2 = Bitmap.createScaledBitmap(bmp, 40, 40, false);
                //canvas.drawBitmap(bmp2, frame.centerX() - 30, (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + 45), paint);
            } else if (pMothod.equals("UNIONPAYOFFL")) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_unionpay);
                bmp2 = Bitmap.createScaledBitmap(bmp, (int) (ScreenRate * 1.5), ScreenRate, false);
                //canvas.drawBitmap(bmp2, frame.centerX() - 30, (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + 45), paint);
            } else if(pMothod.equalsIgnoreCase("GRABPAYOFFL")) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_grabpay);
                bmp2 = Bitmap.createScaledBitmap(bmp, (int) (ScreenRate * 3), ScreenRate, false);
            }  else if(pMothod.equalsIgnoreCase("LINEPAY")) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_linepay);
                bmp2 = Bitmap.createScaledBitmap(bmp, (int) (ScreenRate * 3), ScreenRate, false);
            } else if(pMothod.equalsIgnoreCase("ALIPAYTHOFFL")) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_alipayth);
                bmp2 = Bitmap.createScaledBitmap(bmp, ScreenRate, ScreenRate, false);
            } else if(pMothod.equalsIgnoreCase("MASTERWALLET")) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_masterwallet);
                bmp2 = Bitmap.createScaledBitmap(bmp, (int) (ScreenRate * 1.5), ScreenRate, false);
            } else if(pMothod.equalsIgnoreCase("BOOSTOFFL")) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_boost);
                bmp2 = Bitmap.createScaledBitmap(bmp, ScreenRate, ScreenRate, false);
            } else if(pMothod.equalsIgnoreCase("GCASHOFFL")) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_gcash);
                bmp2 = Bitmap.createScaledBitmap(bmp, ScreenRate, ScreenRate, false);
            } else {
				Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_image);
				bmp2 = Bitmap.createScaledBitmap(bmp, ScreenRate, ScreenRate, false);
			}

            if (lang.equalsIgnoreCase(Constants.LANG_ENG)) {
                // Payment Method Text
                canvas.drawText(getResources().getString(R.string.r_pay_method),
                        (float) (frame.left + (float) TEXT_PADDING_LEFT * density + ScreenRate),
                        (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + (1.9 * ScreenRate)), paint);
                // Payment Method Icon
                canvas.drawBitmap(bmp2,
                        (float) (frame.left + (float) (TEXT_PADDING_LEFT + 90) * density + ScreenRate), (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + (1.2 * ScreenRate)), paint);

            } else if (lang.equalsIgnoreCase(Constants.LANG_SIMP) || lang.equalsIgnoreCase(Constants.LANG_TRAD)) {
                // Payment Method Text
                canvas.drawText(getResources().getString(R.string.r_pay_method),
                        (float) (frame.left + (float) TEXT_PADDING_LEFT * density + ScreenRate),
                        (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + (1.925 * ScreenRate)), paint);
                // Payment Method Icon
                canvas.drawBitmap(bmp2, (float) (frame.left + (float) (TEXT_PADDING_LEFT + 45) * density + ScreenRate), (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + (1.2 * ScreenRate)), paint);
            } else if (lang.equalsIgnoreCase(Constants.LANG_THAI)){
                // Payment Method Text
                canvas.drawText(getResources().getString(R.string.r_pay_method),
                        (float) (frame.left + (float) TEXT_PADDING_LEFT * density + ScreenRate), (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + (1.9 * ScreenRate)), paint);
                // Payment Method Icon
                canvas.drawBitmap(bmp2,
                        (float) (frame.left + (float) (TEXT_PADDING_LEFT + 70) * density + ScreenRate), (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + (1.2 * ScreenRate)), paint);
            } else {
                // Payment Method Text
                canvas.drawText(getResources().getString(R.string.r_pay_method),
                        (float) (frame.left + (float) TEXT_PADDING_LEFT * density + ScreenRate),
                        (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + (1.9 * ScreenRate)), paint);
                // Payment Method Icon
                canvas.drawBitmap(bmp2,
                        (float) (frame.left + (float) (TEXT_PADDING_LEFT + 90) * density + ScreenRate), (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + (1.2 * ScreenRate)), paint);
            }

            //set introduce text or some word u want
            String introword = "";
            introword = introword + getResources().getString(R.string.scanning_desrcription);
            TextPaint tp = new TextPaint();
            tp.setColor(Color.WHITE);
            tp.setAlpha(200);
            tp.setTextSize(getResources().getDimensionPixelSize(R.dimen.xxxs_fontSize)); //Text_size
            tp.setAntiAlias(true);
            tp.setTextAlign(Paint.Align.CENTER);
            tp.setTypeface(Typeface.DEFAULT_BOLD);
            StaticLayout staticlayout = new StaticLayout(introword, tp, canvas.getWidth(), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            canvas.translate(frame.centerX(),
                    (float) (frame.bottom + (float) TEXT_PADDING_TOP * density + (2.7 * ScreenRate)));
            staticlayout.draw(canvas);


            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 3.0f, paint);
                }
            }


            //set refresh scnning frame only
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                    frame.right, frame.bottom);

        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    //getamount from page   u can input in editview or anyway u want
    public void SetamountFun(String setm, String cur, String setmdr, String sethideSurcharge, String setsurC, String setPMothod, String setLang) {
        amount = setm;
        currCode = cur;
        mdr = setmdr;
        hideSurcharge = sethideSurcharge;
        surC = setsurC;
        pMothod = setPMothod;
        lang = setLang;
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

}