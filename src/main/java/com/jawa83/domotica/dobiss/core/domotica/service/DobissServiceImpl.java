package com.jawa83.domotica.dobiss.core.domotica.service;

import com.jawa83.domotica.dobiss.core.domotica.client.DobissClient;
import com.jawa83.domotica.dobiss.core.domotica.model.request.DobissRequestStatusRequest;
import com.jawa83.domotica.dobiss.core.domotica.model.request.DobissSendActionRequest;
import com.jawa83.domotica.dobiss.core.domotica.model.request.ModuleType;
import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissModule;
import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DobissServiceImpl implements DobissService {

    private final static int HEX_OUTPUT_STATUS_LENGTH = 2;

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
        DobissSendActionRequest.builder()
                .dobissClient(dobissClient)
                .module(module)
                .address(address)
                .build().execute();
    }

    /**
     * @param module  Module number of the output (light)
     * @param address Address number of the output (light)
     * @param value   Value of the dimmed light (100 = full light / 0 = no light)
     */
    @Override
    public void dimOutput(int module, int address, int value) throws Exception {
        DobissSendActionRequest.builder()
                .dobissClient(dobissClient)
                .module(module)
                .address(address)
                .actionType(DobissSendActionRequest.ActionType.ON)
                .value(value)
                .build().execute();
    }

    @Override
    public String requestModuleStatusAsHex(int module) throws Exception {
        return requestStatusHex(module);
    }

    @Override
    public List<DobissOutput> requestModuleStatusAsObject(int module) throws Exception {
        return requestStatus(module);
    }

    @Override
    public String requestOutputStatusAsHex(int module, int address) throws Exception {
        String result = requestStatusHex(module);
        int offset = address * HEX_OUTPUT_STATUS_LENGTH;
        if (result == null || result.length() < offset + HEX_OUTPUT_STATUS_LENGTH) {
            return null;
        }
        return result.substring(offset, offset + HEX_OUTPUT_STATUS_LENGTH);
    }

    @Override
    public DobissOutput requestOutputStatusAsObject(int module, int address) throws Exception {
        List<DobissOutput> moduleStatuses = requestStatus(module);
        if (moduleStatuses == null || moduleStatuses.size() == 0 ) {
            return null;
        }
        return moduleStatuses.get(address);
    }

    @Override
    public List<DobissModule> requestAllStatus() throws Exception {
        List<DobissModule> modules = new ArrayList<>();

        dobissClient.setKeepConnectionOpen(true);

        int index = 1;
        List<DobissOutput> outputs = requestModuleStatusAsObject(index);
        while (outputs != null) {
            modules.add(new DobissModule(index, outputs));
            outputs = requestModuleStatusAsObject(++index);
        }

        dobissClient.closeConnection();

        return modules;
    }

    private List<DobissOutput> requestStatus(int module) throws Exception {
        if (moduleTypeMap.containsKey(module)) {
            // Use known module type
            return DobissRequestStatusRequest.builder()
                    .dobissClient(dobissClient)
                    .type(moduleTypeMap.get(module))
                    .module(module)
                    .build().execute();
        }
        // Module type not yet known, iteration over possibilities and store when found
        for (ModuleType type : ModuleType.values()) {
            List<DobissOutput> result = DobissRequestStatusRequest.builder()
                    .dobissClient(dobissClient)
                    .type(type)
                    .module(module)
                    .build().execute();
            if (result != null) {
                moduleTypeMap.put(module, type);
                return result;
            }
        }
        return null;
    }

    private String requestStatusHex(int module) throws Exception {
        if (moduleTypeMap.containsKey(module)) {
            // Use known module type
            return DobissRequestStatusRequest.builder()
                    .dobissClient(dobissClient)
                    .type(moduleTypeMap.get(module))
                    .module(module)
                    .build().executeHex();
        }
        // Module type not yet known, iteration over possibilities and store when found
        for (ModuleType type : ModuleType.values()) {
            String result = DobissRequestStatusRequest.builder()
                    .dobissClient(dobissClient)
                    .type(type)
                    .module(module)
                    .build().executeHex();
            if (result != null) {
                moduleTypeMap.put(module, type);
                return result;
            }
        }
        return null;
    }

}
