package com.example.secondproject;

public class ContactData {
    private String name;
    private String number;
    private String Oid;


    public ContactData(String name, String number, String Oid){
        this.name = name;
        this.number = number;
        this.Oid = Oid;
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
