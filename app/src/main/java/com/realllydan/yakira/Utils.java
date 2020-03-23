package com.realllydan.yakira;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    public static final class Time {
        static Calendar c = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")
        public static String getDate() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM");
            return dateFormat.format(c.getTime());
        }

        @SuppressLint("SimpleDateFormat")
        public static String getYear() {
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            return yearFormat.format(c.getTime());
        }

        @SuppressLint("SimpleDateFormat")
        public static String getTime() {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            return timeFormat.format(c.getTime());
        }
    }

    public static class Toaster {
        private Context context;

        public Toaster(Context context) {
            this.context = context;
        }

        public void displayAToast(int message) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

        public void displayAToast(String message) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

        public void displayAShortToast(int message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

}
