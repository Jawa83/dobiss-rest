package com.jawa83.domotica.dobiss.core.domotica.service;

import com.jawa83.domotica.dobiss.core.domotica.client.DobissClient;
import com.jawa83.domotica.dobiss.core.domotica.model.request.DobissFetchGroupsRequest;
import com.jawa83.domotica.dobiss.core.domotica.model.request.DobissFetchMoodsRequest;
import com.jawa83.domotica.dobiss.core.domotica.model.request.DobissFetchOutputsRequest;
import com.jawa83.domotica.dobiss.core.domotica.model.request.ModuleType;
import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissGroupData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class DobissDataServiceImpl implements DobissDataService {

    private DobissClient dobissClient;

    private Map<Integer, ModuleType> moduleTypeMap;

    @Override
    public List<DobissGroupData> fetchGroupsData() throws Exception {
        return DobissFetchGroupsRequest.builder()
                .dobissClient(dobissClient)
                .build().execute();
    }

    @Override
    public List<DobissGroupData> fetchMoodsData() throws Exception {
        return DobissFetchMoodsRequest.builder()
                .dobissClient(dobissClient)
                .build().execute();
    }

    @Override
    public List<DobissGroupData> fetchOutputsData(int module) throws Exception {
        if (moduleTypeMap.containsKey(module)) {
            // Use known module type
            return DobissFetchOutputsRequest.builder()
                    .dobissClient(dobissClient)
                    .type(moduleTypeMap.get(module))
                    .module(module)
                    .build().execute();
        }
        // Module type not yet known, iteration over possibilities and store when found
        for (ModuleType type : ModuleType.values()) {
            List<DobissGroupData> result = DobissFetchOutputsRequest.builder()
                    .dobissClient(dobissClient)
                    .type(type)
                    .module(module)
                    .build().execute();
            if (result != null && result.size() > 0) {
                moduleTypeMap.put(module, type);
                return result;
            }
        }
        return null;
    }

}
