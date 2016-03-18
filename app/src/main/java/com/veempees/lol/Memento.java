package com.veempees.lol;

import java.util.ArrayList;
import java.util.List;

public class Memento {
    // Each memento has 0 or more properties
    protected int ID;
    protected String text;
    protected List<Integer> propIds;
    protected String quantity;
    protected String measurement;

    public Memento(String text)
    {
        this.text = text;
        this.propIds = new ArrayList<Integer>();
        this.quantity = "";
        this.measurement = "";
    }

    public Memento(Integer ID, String text, String quantity, String measurement)
    {
        this.ID = ID;
        this.text = text;
        this.propIds = new ArrayList<Integer>();
        this.quantity = quantity;
        this.measurement = measurement;
    }

    public void addProperty(Integer propID)
    {
        this.propIds.add(propID);
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
