package com.example.secondproject;

import android.graphics.Bitmap;

public class GalleryData {
    private String Oid;
    private Bitmap img_bit;

    public GalleryData(Bitmap _img_bit, String _Oid) {
        this.img_bit = _img_bit;
        this.Oid = _Oid;
    }

    public Bitmap getBitmap() {
        return this.img_bit;
    }

    public String getOid() {
        return this.Oid;
    }
}
