package com.jawa83.domotica.dobiss.core.domotica.model.request;

import com.jawa83.domotica.dobiss.core.domotica.client.DobissClient;
import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;
import lombok.Builder;

@Builder
public class DobissFetchGroupsRequest implements DobissRequest<String> {

    private DobissClient dobissClient;

    // TODO Find dobiss documentation. Is this always the same request or are there dynamic values?
    private final static String FETCH_GROUPS_REQUEST = "af1020a0a000202020ffffffffffffaf";

    @Override
    public byte[] getRequestBytes() {
        return ConversionUtils.hexToBytes(FETCH_GROUPS_REQUEST);
    }

    @Override
    public int getMaxOutputLines() {
        return 0;
    }

    public String execute() throws Exception {
        // TODO return object
        return ConversionUtils.bytesToHex(this.dobissClient.sendRequest(this));
    }
}
