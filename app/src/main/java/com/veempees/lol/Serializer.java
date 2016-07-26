package com.veempees.lol;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

public class Serializer  {

    public void Download(Activity activity)
    {
        if (Globals.getGlobals().IsDeviceOnline()) {

            ProgressDialog progress;
            progress = new ProgressDialog(activity);
            progress.setMessage("Calling Google Apps Script Execution API ...");

            new MakeRequestTask(Globals.getGlobals().getCredential(), progress, activity).execute();
        }
    }

    public void Upload(Activity activity)
    {
    }
}
