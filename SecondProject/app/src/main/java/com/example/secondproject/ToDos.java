package com.example.secondproject;

public class ToDos {

    private String todo, Oid;
    private int year, month, day;

    public ToDos(String todo, int month, int day, String Oid) {
        this.todo = todo;
        this.month = month;
        this.day = day;
        this.Oid = Oid;
    }

    public String getToDo() {
        return todo;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getOid() {return this.Oid;}

//    public int compareTo(ToDos o) {
//        Calendar cal1 = Calendar.getInstance();
//        cal1.setTime(this.birthDay);
//        Calendar cal2 = Calendar.getInstance();
//        cal2.setTime(o.birthDay);
//
//        int month1 = cal1.get(Calendar.MONTH);
//        int month2 = cal2.get(Calendar.MONTH);
//
//        if(month1 < month2)
//            return -1;
//        else if(month1 == month2)
//            return cal1.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH);
//
//        else return 1;
//
//    }


}
