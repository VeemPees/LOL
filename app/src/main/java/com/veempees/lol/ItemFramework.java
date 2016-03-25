package com.veempees.lol;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.*;

public class ItemFramework {
    private static ItemFramework instance;
    private List<Item> theItems;
    private List<Property> theProperties;
    private List<String> theQuantities;
    private List<String> theMeasurements;
    private List<Item> theMementos;
    private Serializer serializer;

    public static void initInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new ItemFramework();
        }
    }

    public static ItemFramework getInstance()
    {
        // Return the instance
        return instance;
    }

    //Constructor hidden because this is a singleton
    private ItemFramework()
    {
        serializer = new Serializer();
    }

    public void reload(Activity activity)
    {
        serializer.Download(activity);
    }

    private boolean isValidPropertyId(int propId) {
        for (Property prop : theProperties)
        {
            if (prop.getId() == propId)
            {
                return true;
            }
        }
        return false;
    }

    public void store(Activity activity)
    {
        serializer.Upload(activity);
    }

    public Property getProperty(int propID) {
        for (Property prop : theProperties)
        {
            if (prop.getId() == propID)
            {
                return prop;
            }
        }
        //@pg TODO remove null
        return null;
    }

    public List<Property> getProperties() {
        return theProperties;
    }

    public void AddNewProperty(String value)
    {
        Property p = new Property(theProperties.size(), value);
        theProperties.add(p);
    }

    public List<Item> getItems() {
        return theItems;
    }

    private Item mementoOrItemExists(Item item, boolean memento)
    {
        List<Item> whereToLook;
        if (memento)
        {
            whereToLook = theMementos;
        }
        else
        {
            whereToLook = theItems;
        }
        for (Item mi : whereToLook)
        {
            if (item.getText().compareToIgnoreCase(mi.getText()) == 0)
            {
                return mi;
            }
        }
        return null;
    }

    /*
    public Item Add(String string, List<Property> newProps, String qty, String mmt) {

        Item item = new Item(string);

        for (Property p : newProps)
        {
            item.setPropertyId(p.getId());
        }

        item.quantity = qty;
        item.measurement = mmt;

        Item existingItem = mementoOrItemExists(item, false);
        if (existingItem != null)
        {
            // already exists, just return
            return existingItem;
        }

        theItems.add(item);
        Item existingMemento = mementoOrItemExists(item, true);
        if (existingMemento == null)
        {
            theMementos.add(item);
        }
        //TODO store();
        return null; // brand new
    }
    */

    public void Add(Item item) {

        theItems.add(item);
        /* TODO check if to be added to mementos
        Item existingMemento = mementoOrItemExists(item, true);
        if (existingMemento == null)
        {
            theMementos.add(item);
        }
        */
        //TODO store();
    }

    public List<Item> getItemsByPropId(int propId) {

        List<Item> items = new ArrayList<>();

        for (Item i : theItems)
        {
            for (int id : i.getPropertyIds())
            {
                if (id == propId)
                {
                    items.add(i);
                    break;
                }
            }
        }
        return items;
    }

    public void RemoveProperty(int propId) {

        boolean alreadyFound = false;

        for (int i = 0; i < theProperties.size(); i++)
        {
            Property p = theProperties.get(i);

            assertTrue(p.getId() == i);

            if (p.getId() == propId)
            {
                alreadyFound = true;
            }
            else
            {
                if (alreadyFound)
                {
                    p.setId(p.getId() - 1);
                }
            }
        }
        theProperties.remove(propId);
    }

    public int getPropertyCount(boolean countEmptyOnes)
    {
        if (countEmptyOnes)
        {
            return theProperties.size();
        }
        else
        {
            int count = 0;
            for (Property p : theProperties)
            {
                if (!p.isEmpty())
                {
                    count++;
                }
            }
            return count;
        }
    }

    public int getPropertyId(int sequence, boolean countEmptyOnes) {

        Logger.i("Looking for the Id of the " + sequence + "th property" + (countEmptyOnes ? "" : " not counting empty ones"));

        List<Property> propertiesToCount = new ArrayList<Property>();

        for (int i = 0; i < theProperties.size(); i++)
        {
            Property p = theProperties.get(i);

            if (p.isEmpty())
            {
                if (countEmptyOnes)
                {
                    propertiesToCount.add(p);
                }
            }
            else
            {
                propertiesToCount.add(p);
            }
        }

        assertTrue(propertiesToCount.size() > 0);
        assertTrue(propertiesToCount.size() >= sequence + 1);
        return propertiesToCount.get(sequence).getId();

		/*
		int currentPosition = 0;
		EegLogger.l("Start on position 0");
		for (int i = 0; i < theProperties.size(); i++)
		{
			Property p = theProperties.get(i);
			EegLogger.l("Property(" + p.getId() + "," + p.getValue() + ")");

			if (currentPosition == sequence)
			{
				EegLogger.l("Found!");
				return p.getId();
			}
			else
			{
				if (p.isEmpty())
				{
					if (countEmptyOnes)
					{
						currentPosition++;
						EegLogger.l("Property is empty and counting empty ones, moving to next (" + currentPosition + ") position");
					}
					else
					{
						EegLogger.l("Property is empty, but not counting empty ones, remain on (" + currentPosition + ") position");
					}
				}
				else
				{
					currentPosition++;
					EegLogger.l("Property is not empty, moving to next (" + currentPosition + ") position");
				}
			}
		}
		// if we are here, we are doomed
		assertTrue(false);
		return 0;
		*/
    }

    public int getItemCountByPropId(int propId)
    {
        return getItemsByPropId(propId).size();
    }

    public Item getItemById(int id) {
        //@pg currently the ID is the position
        return theItems.get(id);
    }

    public int getChildId(int propId, int sequence)
    {
        List<Item> items = getItemsByPropId(propId);
        Item it = items.get(sequence);

        //@pg currently the ID is the position
        // now find out the position of this item in the big list
        for (int i = 0; i < theItems.size(); i++)
        {
            if (theItems.get(i) == it)
            {
                return i;
            }
        }

        // if we are here, we are doomed
        assertTrue(false);
        return 0;
    }

    List<String> getQuantities()
    {
        return theQuantities;
    }

    List<String> getMeasurements()
    {
        return theMeasurements;
    }

    List<Item> getMementos()
    {
        return theMementos;
    }

}
