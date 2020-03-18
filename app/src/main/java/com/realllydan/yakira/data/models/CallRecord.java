package com.realllydan.yakira.data.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CallRecord {

    private String date, time, year, author;

    public CallRecord() {

    }

    public CallRecord(String date, String time, String year, String author) {
        this.date = date;
        this.time = time;
        this.year = year;
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
