package com.veempees.lol;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.script.model.ExecutionRequest;
import com.google.api.services.script.model.Operation;


/**
 * An asynchronous task that handles the Google Apps Script Execution API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
    private com.google.api.services.script.Script mService = null;
    private Exception mLastError = null;
    private ProgressDialog mProgress;
    private Activity mActivity;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_AUTHORIZATION = 1001;

    public MakeRequestTask(GoogleAccountCredential credential,
                           ProgressDialog progress,
                           Activity activity) {
        mProgress = progress;
        mActivity = activity;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.script.Script.Builder(
                transport, jsonFactory, setHttpTimeout(credential))
                .setApplicationName("LOL")
                .build();
    }

    /**
     * Extend the given HttpRequestInitializer (usually a credentials object)
     * with additional initialize() instructions.
     *
     * @param requestInitializer the initializer to copy and adjust; typically
     *         a credential object.
     * @return an initializer with an extended read timeout.
     */
    private static HttpRequestInitializer setHttpTimeout(
            final HttpRequestInitializer requestInitializer) {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest)
                    throws java.io.IOException {
                requestInitializer.initialize(httpRequest);
                // This allows the API to call (and avoid timing out on)
                // functions that take up to 6 minutes to complete (the maximum
                // allowed script run time), plus a little overhead.
                httpRequest.setReadTimeout(380000);
            }
        };
    }

    /**
     * Background task to call Google Apps Script Execution API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Call the API to run an Apps Script function that returns a list
     * of folders within the user's root directory on Drive.
     *
     * @return list of String folder names and their IDs
     * @throws IOException
     */
    private List<String> getDataFromApi()
            throws IOException, GoogleAuthException {
        // ID of the script to call. Acquire this from the Apps Script editor,
        // under Publish > Deploy as API executable.
        String scriptId = "MxHLP1QI91n-nKR__hXfJ90Nptr_xnHQR";

        List<String> folderList = new ArrayList<String>();

        // Create an execution request object.
            ExecutionRequest request = new ExecutionRequest()
                .setFunction("getData");

        List<Object> params = new ArrayList<Object>();
        params.add("a");

        request.setParameters(params);
        request.setDevMode(true);

        // Make the request.
        Operation op =
                mService.scripts().run(scriptId, request).execute();

        // Print results of request.
        if (op.getError() != null) {
            throw new IOException(getScriptError(op));
        }
        if (op.getResponse() != null &&
                op.getResponse().get("result") != null) {
            // The result provided by the API needs to be cast into
            // the correct type, based upon what types the Apps Script
            // function returns. Here, the function returns an Apps
            // Script Object with String keys and values, so must be
            // cast into a Java Map (folderSet).

            Object o = op.getResponse().get("result");

            ArrayList<String> items =
                    (ArrayList<String>)(op.getResponse().get("result"));
                /*Map<String, String> folderSet =
                        (Map<String, String>)(op.getResponse().get("result"));

                for (String id: folderSet.keySet()) {
                    folderList.add(
                            String.format("%s (%s)", folderSet.get(id), id));
                }
                */
            folderList = items;
        }

        return folderList;
    }

    /**
     * Interpret an error response returned by the API and return a String
     * summary.
     *
     * @param op the Operation returning an error response
     * @return summary of error response, or null if Operation returned no
     *     error
     */
    private String getScriptError(Operation op) {
        if (op.getError() == null) {
            return null;
        }

        // Extract the first (and only) set of error details and cast as a Map.
        // The values of this map are the script's 'errorMessage' and
        // 'errorType', and an array of stack trace elements (which also need to
        // be cast as Maps).
        Map<String, Object> detail = op.getError().getDetails().get(0);
        List<Map<String, Object>> stacktrace =
                (List<Map<String, Object>>)detail.get("scriptStackTraceElements");

        java.lang.StringBuilder sb =
                new StringBuilder("\nScript error message: ");
        sb.append(detail.get("errorMessage"));

        if (stacktrace != null) {
            // There may not be a stacktrace if the script didn't start
            // executing.
            sb.append("\nScript error stacktrace:");
            for (Map<String, Object> elem : stacktrace) {
                sb.append("\n  ");
                sb.append(elem.get("function"));
                sb.append(":");
                sb.append(elem.get("lineNumber"));
            }
        }
        sb.append("\n");
        return sb.toString();
    }


    @Override
    protected void onPreExecute() {
        mProgress.show();
    }

    @Override
    protected void onPostExecute(List<String> output) {
        mProgress.hide();
        if (output == null || output.size() == 0) {
            Logger.i("No results returned.");
        } else {
            output.add(0, "Data retrieved using the Google Apps Script Execution API:");
            Logger.i(TextUtils.join("\n", output));
        }
    }

    @Override
    protected void onCancelled() {
        mProgress.hide();
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                showGooglePlayServicesAvailabilityErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) mLastError)
                                .getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                mActivity.startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        REQUEST_AUTHORIZATION);
            } else {
                Logger.e("The following error occurred:\n"
                        + mLastError.getMessage());
            }
        } else {
            Logger.i("Request cancelled.");
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                connectionStatusCode,
                mActivity,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
}

