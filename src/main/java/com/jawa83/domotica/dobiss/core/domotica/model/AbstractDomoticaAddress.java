package com.jawa83.domotica.dobiss.core.domotica.model;

import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by wardjanssens on 22/02/2017.
 */
@Slf4j
public abstract class AbstractDomoticaAddress {

    private String name = "";
    private int type;
    private int address;
    private int module;
    private byte status = (byte) 0;

    AbstractDomoticaAddress(String name, int address, int module){
        this.name = name;
        this.address = address;
        this.module = module;
        log.debug("Item created: " + name + "[" + address + "," + module + "]");
    }

    // Getters
    public String getName()    { return this.name; }
    public int    getType()    { return this.type; }
    public int    getAddress() { return this.address; }
    public int    getModule()  { return this.module; }
    public byte   getStatus()  { return this.status; }

    // Setters
    public void setStatus(byte status) { this.status = status; }
    public void setType(int type)      { this.type = type; }

    // Utility methods
    String getHexModule() {
        return ConversionUtils.bytesToHex(new byte[] { (byte) getModule() });
    }

    String getHexAddress() {
        return ConversionUtils.bytesToHex(new byte[] { (byte) getAddress() });
    }

}
