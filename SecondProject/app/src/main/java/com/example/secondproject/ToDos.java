package com.example.secondproject;

public class ToDos {

    private String todo, Oid;
    private int month, day;

    public ToDos(String todo, int month, int day, String Oid) {
        this.todo = todo;
        this.month = month;
        this.day = day;
        this.Oid = Oid;
    }

    public String getToDo() {
        return todo;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getOid() {return this.Oid;}

}
