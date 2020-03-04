package com.jawa83.domotica.dobiss.core.domotica.service;

import com.jawa83.domotica.dobiss.core.domotica.client.DobissClient;
import com.jawa83.domotica.dobiss.core.domotica.model.DobissRequestStatusRequest;
import com.jawa83.domotica.dobiss.core.domotica.model.DobissSendActionRequest;
import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DobissServiceImpl implements DobissService {

    private DobissClient dobissClient;

    public DobissServiceImpl(DobissClient dobissClient) {
        this.dobissClient = dobissClient;
    }

    /**
     * @param module  Module number of the output (light)
     * @param address Address number of the output (light)
     */
    @Override
    public void toggleOutput(int module, int address) throws Exception {
        dobissClient.sendRequest(DobissSendActionRequest.builder()
                .module(module)
                .address(address)
                .build().getRequestBytes());
    }

    /**
     * @param module  Module number of the output (light)
     * @param address Address number of the output (light)
     * @param value   Value of the dimmed light (100 = full light / 0 = no light)
     */
    @Override
    public void dimOutput(int module, int address, int value) throws Exception {
        dobissClient.sendRequest(DobissSendActionRequest.builder()
                .module(module)
                .address(address)
                .actionType(DobissSendActionRequest.ActionType.ON)
                .value(value)
                .build().getRequestBytes());
    }

    @Override
    public String requestStatus(int type, int module) throws Exception {
        byte[] result = dobissClient.sendRequest(DobissRequestStatusRequest.builder()
                .type(type)
                .module(module)
                .build().getRequestBytes());
        String resultString = ConversionUtils.bytesToHex(result);
        log.debug(resultString);
        return resultString;
    }

}
