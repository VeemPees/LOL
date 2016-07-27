package com.veempees.lol;

public class Property {
    String value;
    int id;

    public Property(int id, String value)
    {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }

    public void setValue(String string) {
        this.value = string;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEmpty()
    {
        return ItemFramework.getInstance().getItemsByPropId(this.getId()).size() == 0;
    }
}
