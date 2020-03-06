package com.jawa83.domotica.dobiss.core.domotica.service;

import com.jawa83.domotica.dobiss.core.domotica.client.DobissClient;
import com.jawa83.domotica.dobiss.core.domotica.model.request.DobissFetchGroupsRequest;
import com.jawa83.domotica.dobiss.core.domotica.model.request.DobissFetchMoodsRequest;
import com.jawa83.domotica.dobiss.core.domotica.model.request.DobissFetchOutputsRequest;
import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissGroupData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DobissDataServiceImpl implements DobissDataService {

    private DobissClient dobissClient;

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
        return DobissFetchOutputsRequest.builder()
                .dobissClient(dobissClient)
                .moduleId(module)
                .build().execute();
    }
}
