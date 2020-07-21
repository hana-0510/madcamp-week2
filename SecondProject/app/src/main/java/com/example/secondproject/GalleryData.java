package com.example.secondproject;

import android.os.Parcel;
import android.os.Parcelable;
import android.graphics.Bitmap;
import java.io.Serializable;

public class GalleryData implements Serializable {
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

    private static final long serialVersionUID = -5210739585384970789L;

    private boolean isVisible;


}
