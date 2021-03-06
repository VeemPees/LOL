package com.veempees.lol;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

public class MainActivity extends AppCompatActivity {


    PropertyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.refreshList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Globals.getGlobals().isGooglePlayServicesAvailable(MainActivity.this)) {
                    refreshResults();
                } else {
                    //TODO
                    Logger.e("Google Play Services required: " +
                            "after installing, close and relaunch this app.");
                }

/*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
*/
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.addItem);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Add new items
                Intent addIntent = new Intent(MainActivity.this, ItemEntryActivity.class);
                startActivityForResult(addIntent, Constants.ADD_ITEM_REQUEST_CODE);

            }
        });

        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        Globals.getGlobals().initCredentials(settings.getString(Constants.PREF_ACCOUNT_NAME, null));

        this.adapter = new PropertyListAdapter(this, settings);
        final ExpandableListView propertyList = (ExpandableListView) findViewById(R.id.mainList);
        propertyList.setAdapter(this.adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent myIntent;
        int id = item.getItemId();

        switch(item.getItemId())
        {
            /*case R.id.action_add_item:
                myIntent = new Intent(MainActivity.this, ItemEntryActivity.class);
                startActivityForResult(myIntent, Constants.ADD_ITEM_REQUEST_CODE);
                break;
            */
            case R.id.action_edit_properties:
                myIntent = new Intent(MainActivity.this, PropertyChangeActivity.class);
                startActivityForResult(myIntent, Constants.EDIT_ITEM_REQUEST_CODE);
                break;

            case R.id.action_settings:
                break;
            /*case R.id.action_test_drive:
                DummyGDTest();
                break;*/
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case Constants.REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Globals.getGlobals().isGooglePlayServicesAvailable(this);
                }
                break;
            case Constants.REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        Globals.getGlobals().setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(Constants.PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Logger.i("Account unspecified.");
                }
                break;
            case Constants.REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    chooseAccount();
                }
                break;
            case Constants.ADD_ITEM_REQUEST_CODE:
                //SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
                //this.adapter.renderingPropertiesChanged(sharedPrefs);
                if (Globals.getGlobals().IsDeviceOnline()) {

                    ProgressDialog progress;
                    progress = new ProgressDialog(this);
                    progress.setMessage("Uploading data ...");

                    new MakeRequestTask(Globals.getGlobals().getCredential(), progress, this, null).execute(Constants.ASYNC_REQUEST_ADD);
                }
                this.adapter.notifyDataSetChanged();
                break;
            case Constants.EDIT_ITEM_REQUEST_CODE:

            default:

        }
    }

    private void chooseAccount() {
        Globals.getGlobals().pickAccount(this);
    }

    /**
     * Called whenever this activity is pushed to the foreground, such as after
     * a call to onCreate().
     */
    @Override
    protected void onResume() {
        super.onResume();
    }


    private void reloadEverything()
    {
        if (Globals.getGlobals().IsDeviceOnline()) {

            ItemFramework.getInstance().reset();

            ProgressDialog progress;
            progress = new ProgressDialog(this);
            progress.setMessage("Downloading data ...");

            new MakeRequestTask(Globals.getGlobals().getCredential(), progress, this, null).execute(Constants.ASYNC_REQUEST_GET);

        } else {
            //TODO mOutputText.setText("No network connection available.");
            Logger.e("No network connection available.");
        }
    }

    /**
     * Attempt to get a set of data from the Google Apps Script Execution API to display. If the
     * email address isn't known yet, then call chooseAccount() method so the
     * user can pick an account.
     */
    private void refreshResults() {
        if (!Globals.getGlobals().isAccountSelected()) {
            chooseAccount();
        } else {
            reloadEverything();
        }
    }
}
