package com.jawa83.domotica.dobiss.core.domotica.model.request;

import com.jawa83.domotica.dobiss.core.domotica.client.DobissClient;
import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissGroupData;
import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class DobissFetchMoodsRequest implements DobissRequest<List<DobissGroupData>> {

    private final static int MOODS_NAME_LENGTH = 32;

    private DobissClient dobissClient;

    // TODO Find dobiss documentation. Is this always the same request or are there dynamic values?
    private final static String FETCH_MOODS_REQUEST = "af1020a0c000204020ffffffffffffaf";

    @Override
    public byte[] getRequestBytes() {
        return ConversionUtils.hexToBytes(FETCH_MOODS_REQUEST);
    }

    @Override
    public int getMaxOutputLines() {
        return 0;
    }

    public List<DobissGroupData> execute() throws Exception {
        String moodsString = new String(this.dobissClient.sendRequest(this));
        List<DobissGroupData> groups = new ArrayList<>();

        // Names of the groups are returned in one long string
        // Each name is assigned 32 characters (appended with spaces)
        for(int i = 0; i < moodsString.length() / MOODS_NAME_LENGTH; i++){
            groups.add(new DobissGroupData(i, moodsString.substring(i * MOODS_NAME_LENGTH, (i+1) * MOODS_NAME_LENGTH).trim()));
        }

        return groups;
    }

    @Override
    public String executeHex() throws Exception {
        return ConversionUtils.bytesToHex(this.dobissClient.sendRequest(this));
    }
}
