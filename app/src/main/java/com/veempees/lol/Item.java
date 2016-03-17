package com.veempees.lol;

import java.util.ArrayList;
import java.util.List;

// This is the base class of every item
public class Item {

    // Each item has 0 or more properties
    String text;
    List<Integer> propIds;
    boolean done;
    String quantity;
    String measurement;

    public Item(String text)
    {
        this.text = text;
        this.propIds = new ArrayList<Integer>();
        done = false;
        this.quantity = "";
        this.measurement = "";
    }

    @Override
    public String toString() {
        return getText();
    }

    public String getText() {
        return text;
    }

    public List<Integer> getPropertyIds() {
        return propIds;
    }

    public void setPropertyIds(List<Integer> attributes) {
        this.propIds = attributes;
    }

    public void setPropertyId(int id) {
        this.propIds.add(id);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public void RemoveProperty(int id) {
        if (propIds.contains(id))
        {
            propIds.remove(new Integer(id));
        }
    }
}
