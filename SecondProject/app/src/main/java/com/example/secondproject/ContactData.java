package com.example.secondproject;

import android.graphics.Bitmap;

public class ContactData {
    private String name;
    private String number;
    private String Oid;
    private Bitmap img_bit;


    public ContactData(String name, String number, String Oid, Bitmap img){
        this.name = name;
        this.number = number;
        this.Oid = Oid;
        this.img_bit = img;
    }


    public String getName()
    {
        return this.name;
    }

    public String getNumber()
    {
        return this.number;
    }

    public String getOid() {return this.Oid;}

    public Bitmap getBitmap() {return this.img_bit;}

    public String getNumber_raw()
    {
        return this.number.replaceAll("-", "");
    }

//    public void setImage(int image)
//    {
//        this.image = image;
//    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }
}
