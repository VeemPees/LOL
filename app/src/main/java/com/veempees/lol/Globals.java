package com.veempees.lol;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;

import java.util.Arrays;

public class Globals {

    private static Globals instance;
    private static LOLApp mApp;
    private GoogleAccountCredential mCredential;

    public static void initInstance(LOLApp app)
    {
        if (instance == null)
        {
            // Create the instance
            instance = new Globals(app);
        }
    }

    public static Globals getGlobals()
    {
        // Return the instance
        return instance;
    }

    //Constructor hidden because this is a singleton
    private Globals(LOLApp app)
    {
        mApp = app;
    }

    public LOLApp getApp()
    {
        return mApp;
    }

    public ConnectivityManager getConnMgr()
    {
        return (ConnectivityManager) getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    public boolean IsDeviceOnline()
    {
        NetworkInfo networkInfo = getConnMgr().getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date. Will
     * launch an error dialog for the user to update Google Play Services if
     * possible.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    public boolean isGooglePlayServicesAvailable(Activity activity) {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApp());
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode, activity);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    public void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode, Activity activity) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                connectionStatusCode,
                activity,
                Constants.REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    public void initCredentials(String accountName) {
        // Initialize credentials and service object.

        mCredential = GoogleAccountCredential.usingOAuth2(
                mApp, Arrays.asList(Constants.SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(accountName);
    }

    public GoogleAccountCredential getCredential()
    {
        return mCredential;
    }

    public String getSelectedAccountName()
    {
        return mCredential.getSelectedAccountName();
    }

    public void setSelectedAccountName(String accountName)
    {
        mCredential.setSelectedAccountName(accountName);
    }

    public boolean isAccountSelected()
    {
        return !(mCredential.getSelectedAccountName() == null);
    }

    public void pickAccount(Activity activity)
    {
        activity.startActivityForResult(mCredential.newChooseAccountIntent(), Constants.REQUEST_ACCOUNT_PICKER);
    }
}
