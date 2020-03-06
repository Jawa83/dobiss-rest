package com.jawa83.domotica.dobiss.core.domotica.model.request;

import com.jawa83.domotica.dobiss.core.domotica.client.DobissClient;
import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissGroupData;
import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class DobissFetchOutputsRequest implements DobissRequest<List<DobissGroupData>> {

    // TODO make dynamic
    private final static String TEST_STRING = "af1008010100200c20ffffffffffffaf";

    private final static int GROUP_NAME_LENGTH = 32;

    private DobissClient dobissClient;

    private int moduleId;

    @Override
    public byte[] getRequestBytes() {
        return ConversionUtils.hexToBytes(TEST_STRING);
    }

    @Override
    public int getMaxOutputLines() {
        return 0;
    }

    public List<DobissGroupData> execute() throws Exception {
        String groupsString = new String(this.dobissClient.sendRequest(this));
        List<DobissGroupData> groups = new ArrayList<>();

        // Names of the groups are returned in one long string
        // Each name is assigned 32 characters (appended with spaces)
        for(int i = 0; i < groupsString.length() / GROUP_NAME_LENGTH; i++){
            groups.add(new DobissGroupData(i, groupsString.substring(i * GROUP_NAME_LENGTH, (i+1) * GROUP_NAME_LENGTH).trim()));
        }

        return groups;
    }

    @Override
    public String executeHex() throws Exception {
        return ConversionUtils.bytesToHex(this.dobissClient.sendRequest(this));
    }
}
