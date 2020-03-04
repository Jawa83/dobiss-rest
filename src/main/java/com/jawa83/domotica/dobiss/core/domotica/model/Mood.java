package com.jawa83.domotica.dobiss.core.domotica.model;

import com.jawa83.domotica.dobiss.core.domotica.data.DobissTypes;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by wardjanssens on 09/03/2017.
 */
@Slf4j
@Deprecated
public class Mood extends Toggle {

    public Mood(String name, int address) {
        super(name, address, (byte)255);
        this.setType(DobissTypes.MOOD.getId());
    }

    @Override
    public byte[] getToggleRequest() {
        log.info("Toggle Mood Module: " + this.getModule() + " | Address: " + this.getAddress());
        //AF02FF'mod'0000080108FFFFFFFFFFFFAF'mod''addr'02FFFF64FFFF
        return new byte[]{(byte)175, (byte)2, (byte)255, (byte)this.getModule(), (byte)0, (byte)0, (byte)8, (byte)1,
                (byte)8, (byte)255, (byte)255, (byte)255, (byte)255, (byte)255, (byte)255, (byte)175,
                (byte)83, (byte)this.getAddress(), (byte)2, (byte)255, (byte)255, (byte)100, (byte)255, (byte)255};
    }
}
