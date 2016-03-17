package com.veempees.lol;

import android.util.Log;

/**
 * Created by Pesti_Gabor on 3/16/2016.
 */
public class Logger {
    public static void i(String s)
    {
        Log.i(Constants.LOG_Prefix, s);
    }
    public static void e(String s)
    {
        Log.e(Constants.LOG_Prefix, s);
    }
}
