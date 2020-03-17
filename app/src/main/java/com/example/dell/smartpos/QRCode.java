package com.example.dell.smartpos;

import android.graphics.Bitmap;

public class QRCode {
    Bitmap bitmap;
    String createdTime;
    String expiryTime;
    String orderId;
    String QRRef;
    String QRRef2;
    QRCode() {
    }

    public String getOrderId() {
        return orderId;
    }

    public Bitmap getBitmapQR() {
        return bitmap;
    }

    public void setBitmapQR(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }


    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getQRRef() {
        return QRRef;
    }

    public void setQRRef(String QRRef) {
        this.QRRef = QRRef;
    }

    public String getQRRef2() {
        return QRRef2;
    }

    public void setQRRef2(String QRRef2) {
        this.QRRef2 = QRRef2;
    }
}