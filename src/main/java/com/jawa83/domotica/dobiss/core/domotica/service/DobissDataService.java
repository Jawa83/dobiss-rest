package com.jawa83.domotica.dobiss.core.domotica.service;

import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissGroupData;

import java.util.List;

public interface DobissDataService {

    List<DobissGroupData> fetchGroupsData() throws Exception;

    List<DobissGroupData> fetchMoodsData() throws Exception;

    List<DobissGroupData> fetchOutputsData(int module) throws Exception;
}
