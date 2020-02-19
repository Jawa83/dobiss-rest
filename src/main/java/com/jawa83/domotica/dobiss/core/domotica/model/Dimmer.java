package com.jawa83.domotica.dobiss.core.domotica.model;

import com.jawa83.domotica.dobiss.core.domotica.data.DobissTypes;

/**
 * Created by wardjanssens on 28/02/2017.
 */

public class Dimmer extends Toggle {

    public Dimmer(String name, int address, int module) {
        super(name, address, module);
        this.setType(DobissTypes.DIMMER.getId());
    }

    public byte[] getDimmerRequest(int dimmerValue) {
    		return getTriggerRequest(dimmerValue, ActionType.ON);
    }
}
