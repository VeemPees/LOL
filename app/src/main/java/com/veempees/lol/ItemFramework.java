package com.veempees.lol;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.*;

public class ItemFramework {
    private static ItemFramework instance;
    private List<Item> theItems;


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
        theItems = new ArrayList<>();
        theMementos = new ArrayList<>();
        theProps = new ArrayList<>();
        theQtts = new ArrayList<>();
        theMmts = new ArrayList<>();
    }

    public void reload(Activity activity)
    {
        resetQtts();
        resetProps();
        resetMmts();
        resetMementos();
        resetItems();
        serializer.Download(activity);
    }

    private void resetItems()
    {
        theItems.clear();
    }

/*
    public void store(Activity activity)
    {
        serializer.Upload(activity);
    }
*/

/*
    public List<Item> getItems() {
        return theItems;
    }
*/

/*
    private Item itemExists(Item item)
    {
        for (Item mi : theItems)
        {
            if (item.getText().compareToIgnoreCase(mi.getText()) == 0)
            {
                return mi;
            }
        }
        return null;
    }
*/





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

    public void addItem(Item item) {
       theItems.add(item);
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





    /*
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
*/
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
/*
    }
*/
    public int getItemCountByPropId(long propId)
    {
        return getItemsByPropId((int)propId).size();
    }

    public Item getItemById(long id) {
        //@pg currently the ID is the position
        return theItems.get((int) id);
    }

    public int getItemIDByGroupAndPosition(long propID, int childPosition)
    {
        List<Item> items = getItemsByPropId((int)propID);
        Item it = items.get(childPosition);

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

    /*
    Qtt
     */
    private List<String> theQtts;

    List<String> getQtts()
    {
        return theQtts;
    }

    void resetQtts()
    {
        theQtts.clear();
    }

    void addQtt(int index, String value)
    {
        assertTrue(index == theQtts.size());
        theQtts.add(value);
    }

    String getQtt(int index)
    {
        assertTrue(index < theQtts.size());
        return theQtts.get(index);
    }

    /*
    Mmt
     */
    private List<String> theMmts;

    List<String> getMmts()
    {
        return theMmts;
    }

    void resetMmts()
    {
        theMmts.clear();
    }

    void addMmt(int index, String value)
    {
        assertTrue(index == theMmts.size());
        theMmts.add(value);
    }

    String getMmt(int index)
    {
        assertTrue(index < theMmts.size());
        return theMmts.get(index);
    }

    /*
    Props
     */
    private List<String> theProps;

    List<String> getProps()
    {
        return theProps;
    }

    void resetProps()
    {
        theProps.clear();
    }

    public void addProp(int index, String value)
    {
        theProps.add(value);
    }

    String getProp(long index)
    {
        assertTrue(index < theProps.size());
        return theProps.get((int)index);
    }

    public int getPropertyCount(boolean countEmptyOnes)
    {
        // TODO correctly handle countEmptyOnes
        return theProps.size();
    }

    public void RemoveProp(int index)
    {
        theProps.remove(index);
    }

    /*
    Mementos
     */
    private List<Memento> theMementos;

    List<Memento> getMementos()
    {
        return theMementos;
    }

    void resetMementos()
    {
        theMementos.clear();
    }

    public void addMemento(Memento memento) {
        theMementos.add(memento);
    }

    Memento getMemento(int index)
    {
        assertTrue(index < theMementos.size());
        return theMementos.get(index);
    }
}
