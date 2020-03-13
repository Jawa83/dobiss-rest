package com.jawa83.domotica.dobiss.core.domotica.model.request;

import com.jawa83.domotica.dobiss.core.domotica.client.DobissClient;
import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissOutput;
import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
public class DobissRequestStatusRequest implements DobissRequest<List<DobissOutput>> {

    private final static byte[] BASE_REQUEST_STATUS_REQUEST = ConversionUtils.hexToBytes("af01ffff0000000100ffffffffffffaf");
    private final static int MAX_OUTPUTS_PER_MODULE = 8;
    private final static byte EMPTY_BYTE = (byte) -1;

    private final static int INDEX_TYPE = 2;
    private final static int INDEX_MODULE = 3;

    private DobissClient dobissClient;
    private ModuleType type;
    private int module;

    @Override
    public byte[] getRequestBytes() {
        byte[] byteArray = BASE_REQUEST_STATUS_REQUEST;

        byteArray[INDEX_TYPE] = type.getValue();
        byteArray[INDEX_MODULE] = (byte) module;

        return byteArray;
    }

    @Override
    public int getMaxOutputLines() {
        return 4;
    }

    @Override
    public List<DobissOutput> execute() throws Exception {
        byte[] result = this.dobissClient.sendRequest(this);
        if (result == null || result.length == 0) {
            return null;
        }
        result = Arrays.copyOf(result, MAX_OUTPUTS_PER_MODULE);
        List<DobissOutput> resultList = new ArrayList<>();
        for (int i = 0; i < result.length; i++) {
            if (result[i] != EMPTY_BYTE) {
                resultList.add(new DobissOutput(i, result[i]));
            }
        }
        return resultList;
    }

    @Override
    public String executeHex() throws Exception {
        byte[] result = this.dobissClient.sendRequest(this);
        if (result == null || result.length == 0) {
            return null;
        }
        result = Arrays.copyOf(result, MAX_OUTPUTS_PER_MODULE);
        return ConversionUtils.bytesToHex(ConversionUtils.trimBytes(result, EMPTY_BYTE));
    }
}
