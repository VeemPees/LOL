package com.veempees.lol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Memento {
    // Each memento has 0 or more properties
    protected int ID;
    protected String value;
    protected List<Integer> propIds;
    protected int qttID;
    protected int mmtID;

    public Memento(String value)
    {
        this.value = value;
        this.propIds = new ArrayList<Integer>();
        this.qttID = 0;
        this.mmtID = 0;
    }

/*
    public Memento(Integer ID, String value, int qttID, int mmtID)
    {
        this.ID = ID;
        this.value = value;
        this.propIds = new ArrayList<Integer>();
        this.qttID = qttID;
        this.mmtID = mmtID;
    }

*/
    public Memento(BigDecimal ID, String value, BigDecimal qttID, BigDecimal mmtID)
    {
        this.ID = ID.intValue();
        this.value = value;
        this.propIds = new ArrayList<Integer>();
        this.qttID = qttID.intValue();
        this.mmtID = mmtID.intValue();
    }

    public void addProperty(Integer propID)
    {
        this.propIds.add(propID);
    }

    public void addProperty(BigDecimal propID)
    {
        this.propIds.add(propID.intValue());
    }

    @Override
    public String toString()
    {
        return getText();
    }

    public String getText() {
        return value;
    }

    public List<Integer> getPropertyIds() {
        return propIds;
    }

    public int getQttyID() {
        return qttID;
    }

    public String getQtty()
    {
        return ItemFramework.getInstance().getQtt(this.getQttyID());
    }

    public int getMmtID() {
        return mmtID;
    }

    public String getMmt() { return ItemFramework.getInstance().getMmt(this.getMmtID()); }

}
