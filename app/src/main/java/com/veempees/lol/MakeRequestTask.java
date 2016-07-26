package com.veempees.lol;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigDecimal;
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
            return getDataFromApi("getItems");
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Call the API to run an Apps Script function
     * @throws IOException, GoogleAuthException
     */

    private List<String> getDataFromApi(String fnName)
            throws IOException, GoogleAuthException {

        // Create an execution request object.
        ExecutionRequest request = new ExecutionRequest().setFunction(fnName);

        //List<Object> params = new ArrayList<Object>();
        //params.add("a");
        //request.setParameters(params);

        // TODO
        request.setDevMode(true);

        // Make the request.
        Operation op = mService.scripts().run(Constants.SCRIPT_ID, request).execute();

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

            if (o instanceof String) {

            } else if (o instanceof ArrayList) {

                // the data is sent in the following format
                // item array
                // memento array
                // Qtt list
                // Mmt list
                // Property list
                ArrayList<Object> ret = (ArrayList<Object>) o;

                ArrayList<ArrayList<Object>> items = (ArrayList<ArrayList<Object>>) ret.get(0);

                // items are sent in the following format
                //  0  1       2      3      4      5          6
                // ID, isDone, QttID, MmtID, Value, PropCount, [Props]
                for (ArrayList<Object> row : items) {
                    //  0  1       2      3      4      5          6
                    // ID, isDone, QttID, MmtID, Value, PropCount, [Props]
                    Item item = new Item((BigDecimal)row.get(0), (boolean)row.get(1), row.get(4).toString(), (BigDecimal)row.get(2), (BigDecimal)row.get(3));

                    int propCount = ((BigDecimal)row.get(5)).intValue();

                    for (int i = 0; i < propCount; i++) {
                        item.addProperty((BigDecimal) row.get(6 + i));
                    }
                    ItemFramework.getInstance().addItem(item);
                }

                ArrayList<ArrayList<Object>> mementos = (ArrayList<ArrayList<Object>>) ret.get(1);

                // mementos are sent in the following format
                //  0  1      2      3      4          5
                // ID, QttID, MmtID, Value, PropCount, [Props]
                for (ArrayList<Object> row : mementos) {
                    //  0  1      2      3      4          5
                    // ID, QttID, MmtID, Value, PropCount, [Props]
                    Memento memento = new Memento((BigDecimal)row.get(0), row.get(3).toString(), (BigDecimal)row.get(1), (BigDecimal)row.get(2));

                    int propCount = ((BigDecimal)row.get(4)).intValue();

                    for (int i = 0; i < propCount; i++) {
                        memento.addProperty((BigDecimal)row.get(5 + i));
                    }
                    ItemFramework.getInstance().addMemento(memento);
                }


                ArrayList<ArrayList<Object>> qtts = (ArrayList<ArrayList<Object>>) ret.get(2);

                ItemFramework.getInstance().resetQtts();
                for (ArrayList<Object> row : qtts) {
                    BigDecimal bd = (BigDecimal) row.get(0);
                    ItemFramework.getInstance().addQtt(bd.intValue(), row.get(1).toString());
                }

                ArrayList<ArrayList<Object>> mmts = (ArrayList<ArrayList<Object>>) ret.get(3);

                ItemFramework.getInstance().resetMmts();
                for (ArrayList<Object> row : mmts) {
                    BigDecimal bd = (BigDecimal) row.get(0);
                    ItemFramework.getInstance().addMmt(bd.intValue(), row.get(1).toString());
                }

                ArrayList<ArrayList<Object>> props = (ArrayList<ArrayList<Object>>) ret.get(4);

                ItemFramework.getInstance().resetProps();
                for (ArrayList<Object> row : props) {
                    BigDecimal bd = (BigDecimal) row.get(0);
                    ItemFramework.getInstance().addProp(bd.intValue(), row.get(1).toString());
                }
            }
        }

        return null;
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
        Toast.makeText(mActivity, "Done", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCancelled() {
        mProgress.hide();
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                Globals.getGlobals().showGooglePlayServicesAvailabilityErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) mLastError)
                                .getConnectionStatusCode(), mActivity);
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                mActivity.startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        Constants.REQUEST_AUTHORIZATION);
            } else {
                Logger.e("The following error occurred:\n"
                        + mLastError.getMessage());
                Toast.makeText(mActivity, mLastError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Logger.i("Request cancelled.");
        }
    }
}

