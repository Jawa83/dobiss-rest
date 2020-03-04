package com.jawa83.domotica.dobiss.core.domotica.data;

/**
 * Created by wardjanssens on 09/03/2017.
 *
 * @deprecated being refactored
 */
@Deprecated
public enum DobissTypes {
    TOGGLE(1),
    DIMMER(2),
    MOOD(3);

    private int id;

    DobissTypes(int id) { this.id = id; }

    public int getId() { return this.id; }

}
