package com.jawa83.domotica.dobiss.core.domotica.service;

import com.jawa83.domotica.dobiss.core.domotica.client.DobissClient;
import com.jawa83.domotica.dobiss.core.domotica.model.DobissOutput;
import com.jawa83.domotica.dobiss.core.domotica.model.DobissRequestStatusRequest;
import com.jawa83.domotica.dobiss.core.domotica.model.DobissRequestStatusRequest.ModuleType;
import com.jawa83.domotica.dobiss.core.domotica.model.DobissSendActionRequest;
import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DobissServiceImpl implements DobissService {

    private final static int MAX_OUTPUTS_PER_MODULE = 8;
    private final static byte EMPTY_BYTE = (byte) -1;

    private Map<Integer, ModuleType> moduleTypeMap = new HashMap<>();

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
    public String requestModuleStatusAsHex(int module) throws Exception {
        byte[] result = requestStatus(module);
        return result == null ? null : ConversionUtils.bytesToHex(ConversionUtils.trimBytes(result, EMPTY_BYTE));
    }

    @Override
    public List<DobissOutput> requestModuleStatusAsObject(int module) throws Exception {
        byte[] result = requestStatus(module);
        if (result == null) {
            return null;
        }
        List<DobissOutput> resultList = new ArrayList<>();
        for (int i = 0; i < result.length; i++) {
            if (result[i] != EMPTY_BYTE) {
                resultList.add(new DobissOutput(i, result[i]));
            }
        }
        return resultList;
    }

    private byte[] requestStatus(int module) throws Exception {
        byte[] result;
        if (moduleTypeMap.containsKey(module)) {
            // Use known module type
            return Arrays.copyOf(
                    dobissClient.sendRequest(DobissRequestStatusRequest.builder()
                        .type(moduleTypeMap.get(module))
                        .module(module)
                        .build().getRequestBytes()),
                    MAX_OUTPUTS_PER_MODULE);
        }
        // Module type not yet known, iteration over possibilities and store when found
        for (ModuleType type : ModuleType.values()) {
            result = dobissClient.sendRequest(DobissRequestStatusRequest.builder()
                    .type(type)
                    .module(module)
                    .build().getRequestBytes());
            if (result.length > 0) {
                moduleTypeMap.put(module, type);
                return Arrays.copyOf(result, MAX_OUTPUTS_PER_MODULE);
            }
        }
        return null;
    }

}
