package com.jawa83.domotica.dobiss.core.domotica.model;

import java.util.ArrayList;

/**
 * Created by wardjanssens on 28/02/2017.
 *
 * @deprecated being refactored
 */
@Deprecated
public class Group {

    private int id;
    private String name;
    private ArrayList<AbstractDomoticaAddress> items = new ArrayList<>();

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public ArrayList<AbstractDomoticaAddress> getItems() {
        return this.items;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + name;
    }

}
