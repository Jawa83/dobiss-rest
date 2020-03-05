package com.jawa83.domotica.dobiss.core.domotica.service;

import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissModule;
import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissOutput;

import java.util.List;

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

    String requestModuleStatusAsHex(int module) throws Exception;

    List<DobissOutput> requestModuleStatusAsObject(int module) throws Exception;

    String requestOutputStatusAsHex(int module, int address) throws Exception;

    DobissOutput requestOutputStatusAsObject(int module, int address) throws Exception;

    List<DobissModule> requestAllStatus() throws Exception;

}
