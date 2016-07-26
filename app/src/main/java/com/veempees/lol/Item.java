package com.veempees.lol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// This is the base class of every item
public class Item extends Memento  {

    boolean done;

    public Item(String value)
    {
        super(value);
        done = false;
    }

    /*public Item(Integer ID, boolean isDone, String value, int qttID, int mmtID)
    {
        super(ID, value, qttID, mmtID);
        this.done = isDone;
    }*/

    public Item(BigDecimal ID, boolean isDone, String value, BigDecimal qttID, BigDecimal mmtID)
    {
        super(ID, value, qttID, mmtID);
        this.done = isDone;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
