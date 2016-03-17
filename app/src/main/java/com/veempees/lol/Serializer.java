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
            Normalize();
        }
    }

    private void Normalize() {
        /*for (Item item : theItems)
        {
            List<Integer> propsToRemove = new ArrayList<Integer>();
            for (int propId : item.getPropertyIds())
            {
                if (isValidPropertyId(propId))
                {
                    // we are OK
                }
                else
                {
                    // invalid property id
                    Logger.e("Invalid propery ID " + propId);
                    propsToRemove.add(propId);
                }
            }

            for (int id : propsToRemove)
            {
                item.RemoveProperty(id);
            }
        }*/

    }

    public void Upload(Activity activity)
    {
    }

    public List<Item> getItems()
    {
        return null;
    }

    public List<Item> getMementos()
    {
        return null;
    }

    public List<Property> getProperties()
    {
        return null;
    }

    public List<String> getQuantities()
    {
        return null;
    }

    public List<String> getMeasurements()
    {
        return null;
    }

    public void setItems(List<Item> items)
    {
    }

    public void setMementos(List<Item> mementos)
    {
    }

    public void setProperties(List<Property> properties)
    {

    }

    public void setQuantities(List<String> qtts)
    {
    }

    public void setMeasurements(List<String> mmts)
    {
    }
}
