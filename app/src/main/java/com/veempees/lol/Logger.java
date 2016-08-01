package com.veempees.lol;

import android.util.Log;

public class Logger {
    public static void i(String s)
    {
        Log.i(Constants.LOG_PREFIX, s);
    }
    public static void e(String s)
    {
        Log.e(Constants.LOG_PREFIX, s);
    }
}
