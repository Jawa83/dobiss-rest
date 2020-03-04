package com.jawa83.domotica.dobiss.core.domotica.service;

public interface DobissService {

    /**
     *
     * @param module Module number of the output (light)
     * @param address Address number of the output (light)
     */
    void toggleOutput(int module, int address) throws Exception;

    /**
     *
     * @param module Module number of the output (light)
     * @param address Address number of the output (light)
     * @param value Value of the dimmed light (100 = full light / 0 = no light)
     */
    void dimOutput(int module, int address, int value) throws Exception;

    String requestStatus(int type, int module) throws Exception;

}
