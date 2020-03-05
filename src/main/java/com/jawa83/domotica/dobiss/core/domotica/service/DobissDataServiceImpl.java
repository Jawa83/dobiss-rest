package com.jawa83.domotica.dobiss.core.domotica.service;

import com.jawa83.domotica.dobiss.core.domotica.client.DobissClient;
import com.jawa83.domotica.dobiss.core.domotica.model.request.DobissFetchGroupsRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DobissDataServiceImpl implements DobissDataService {

    private DobissClient dobissClient;

    @Override
    public String fetchGroupsData() throws Exception {
        return DobissFetchGroupsRequest.builder()
                .dobissClient(dobissClient)
                .build().execute();
    }
}
