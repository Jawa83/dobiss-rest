package com.jawa83.domotica.dobiss.core.domotica.model;

import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;
import lombok.Builder;

@Builder
public class DobissRequestStatusRequest implements DobissRequest {

    //{ 0xAF, 0x01, moduleType, moduleAddress, 0x00, 0x00, 0x00, 0x01, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xAF };
    // af0108020000000100ffffffffffffaf
    // af0110040000000100ffffffffffffaf

    private final static byte[] BASE_REQUEST_STATUS_REQUEST = ConversionUtils.hexToBytes("af01ffff0000000100ffffffffffffaf");

    private final static int INDEX_TYPE = 2;
    private final static int INDEX_MODULE = 3;

    private int type;
    private int module;

    @Override
    public String getRequestString() {
        return ConversionUtils.bytesToHex(getRequestBytes());
    }

    @Override
    public byte[] getRequestBytes() {
        byte[] byteArray = BASE_REQUEST_STATUS_REQUEST;

        byteArray[INDEX_TYPE] = (byte) type;
        byteArray[INDEX_MODULE] = (byte) module;

        return byteArray;
    }
}
