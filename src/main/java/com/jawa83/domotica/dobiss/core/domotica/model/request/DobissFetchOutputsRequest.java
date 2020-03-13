package com.jawa83.domotica.dobiss.core.domotica.model.request;

import com.jawa83.domotica.dobiss.core.domotica.client.DobissClient;
import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissGroupData;
import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class DobissFetchOutputsRequest implements DobissRequest<List<DobissGroupData>> {

    private final static byte[] BASE_FETCH_OUTPUTS_REQUEST = ConversionUtils.hexToBytes("af10ffff0100200c20ffffffffffffaf");

    private final static int INDEX_TYPE = 2;
    private final static int INDEX_MODULE = 3;

    private final static int OUTPUT_NAME_LENGTH = 32;

    private DobissClient dobissClient;
    private ModuleType type;
    private int module;

    @Override
    public byte[] getRequestBytes() {
        byte[] byteArray = BASE_FETCH_OUTPUTS_REQUEST;

        byteArray[INDEX_TYPE] = type.getValue();
        byteArray[INDEX_MODULE] = (byte) module;

        return byteArray;
    }

    @Override
    public int getMaxOutputLines() {
        return 26;
    }

    public List<DobissGroupData> execute() throws Exception {
        // TODO trim empty bytes and don't add group when resulting name is empty
        String groupsString = new String(this.dobissClient.sendRequest(this));
        List<DobissGroupData> groups = new ArrayList<>();

        // Names of the groups are returned in one long string
        // Each name is assigned 32 characters (appended with spaces)
        for(int i = 0; i < groupsString.length() / OUTPUT_NAME_LENGTH; i++){
            groups.add(new DobissGroupData(i, groupsString.substring(i * OUTPUT_NAME_LENGTH, (i+1) * OUTPUT_NAME_LENGTH).trim()));
        }

        return groups;
    }

    @Override
    public String executeHex() throws Exception {
        return ConversionUtils.bytesToHex(this.dobissClient.sendRequest(this));
    }
}
