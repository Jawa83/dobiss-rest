package com.jawa83.domotica.dobiss.core.domotica.data;

import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;

/**
 * Created by wardjanssens on 24/02/2017.
 *
 *
 */
@Deprecated
public enum DobissInput {

    FETCH_GROUPS     ("1020a0a000202020", 66),
    FETCH_MOODS      ("1020a0c000204020", 100),
    FETCH_OUTPUTS_1  ("1008010100200c20", 26), // Relais
    FETCH_OUTPUTS_2  ("1008020100200c20", 26), // Relais
    FETCH_OUTPUTS_3  ("1010030100200420", 10), // Dimmer
    FETCH_OUTPUTS_4  ("1010040100200420", 10), // Dimmer
    FETCH_OUTPUTS_5  ("1010050100200420", 10), // Dimmer
    FETCH_OUTPUTS_6  ("1008060100200c20", 26), // Relais
    FETCH_OUTPUTS_7  ("1008070100200c20", 26), // Relais
    FETCH_OUTPUTS_8  ("1008080100200c20", 26), // Relais
    FETCH_OUTPUTS_9  ("1008090100200c20", 26), // Relais
    FETCH_OUTPUTS_10 ("10080a0100200c20", 26), // Relais
    STATUS_OUTPUTS_1 ("0108010000000100", 4),
    STATUS_OUTPUTS_2 ("0108020000000100", 4),
    STATUS_OUTPUTS_3 ("0110030000000100", 4),
    STATUS_OUTPUTS_4 ("0110040000000100", 4),
    STATUS_OUTPUTS_5 ("0110050000000100", 4),
    STATUS_OUTPUTS_6 ("0108060000000100", 4),
    STATUS_OUTPUTS_7 ("0108070000000100", 4),
    STATUS_OUTPUTS_8 ("0108080000000100", 4),
    STATUS_OUTPUTS_9 ("0108090000000100", 4),
    STATUS_OUTPUTS_10("01080a0000000100", 4);

    // TODO dimmers
    // af 02 ff 04 00 00 08 01 08 ff ff ff ff ff ff af
    // 04 00 01 ff ff 32 ff ff   (32 hex = 50%)

    private static final String REQUEST_PREFIX_HEX = "af";
    private static final String REQUEST_SUFFIX_HEX = "ffffffffffffaf";

    private final String hexRequest;
    private final int maxLines;

    DobissInput(String hexRequest, int maxLines) {
        this.hexRequest = hexRequest;
        this.maxLines = maxLines;
    }

    public byte[] getRequest() {
        String fullHexRequest = REQUEST_PREFIX_HEX + this.hexRequest + REQUEST_SUFFIX_HEX;
        return ConversionUtils.hexToBytes(fullHexRequest);
    }

    public int getMaxLines() {
        return this.maxLines;
    }

}
