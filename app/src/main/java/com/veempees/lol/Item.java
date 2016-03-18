package com.veempees.lol;

import java.util.ArrayList;
import java.util.List;

// This is the base class of every item
public class Item extends Memento  {

    boolean done;

    public Item(String text)
    {
        super(text);
        done = false;
    }

    public Item(Integer ID, String text, String quantity, String measurement, boolean done)
    {
        super(ID, text, quantity, measurement);
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
