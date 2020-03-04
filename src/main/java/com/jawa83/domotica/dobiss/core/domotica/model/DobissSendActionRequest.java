package com.jawa83.domotica.dobiss.core.domotica.model;

import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;
import lombok.Builder;

@Builder
public class DobissSendActionRequest implements DobissRequest {

    private final static byte[] BASE_SEND_ACTION_REQUEST = ConversionUtils.hexToBytes("af02ff000000080108ffffffffffffaf0000000000000000");

    // default values
    private final static int DEFAULT_ACTION_VALUE = 100;
    private final static ActionType DEFAULT_ACTION_TYPE = ActionType.TOGGLE;
    private final static int DEFAULT_DELAY_ON = -1; // disabled
    private final static int DEFAULT_DELAY_OFF = -1; // disabled
    private final static int DEFAULT_SOFT_DIM = -1; // disabled
    private final static int DEFAULT_COND = -1; // disabled

    // request byte array indexes
    private final static int INDEX_MODULE_HEADER = 3;
    private final static int INDEX_MODULE = 16;
    private final static int INDEX_ADDRESS = 17;
    private final static int INDEX_ACTION_TYPE = 18;
    private final static int INDEX_DELAY_ON = 19;
    private final static int INDEX_DELAY_OFF = 20;
    private final static int INDEX_VALUE = 21;
    private final static int INDEX_SOFT_DIM = 22;
    private final static int INDEX_COND = 23;

    /**
     * Module number where the output is on
     */
    private int module;

    /**
     * Address number of the output on the module
     */
    private int address;

    /**
     * Action type (OFF/ON/TOGGLE)
     */
    private ActionType actionType;

    /**
     * Value (0 - 100) of the requested dim value (can only be 100 or 0 for non-dimmable outputs)
     */
    private Integer value;

    /**
     * The delay before executing the action ON
     */
    private Integer delayOn;

    /**
     * The delay before executing the action OFF
     */
    private Integer delayOff;

    /**
     * The speed of dimming
     */
    private Integer softDim;

    /**
     * Not used (no description in the DOBISS documentation
     */
    private Integer cond;

    @Override
    public String getRequestString() {
        return ConversionUtils.bytesToHex(getRequestBytes());
    }

    @Override
    public byte[] getRequestBytes() {
        byte[] byteArray = BASE_SEND_ACTION_REQUEST;

        byteArray[INDEX_MODULE_HEADER] = (byte) module;
        byteArray[INDEX_MODULE] = (byte) module;
        byteArray[INDEX_ADDRESS] = (byte) address;
        byteArray[INDEX_ACTION_TYPE] = actionType == null ? DEFAULT_ACTION_TYPE.getValue() : actionType.getValue();
        byteArray[INDEX_DELAY_ON] = delayOn == null ? DEFAULT_DELAY_ON : delayOn.byteValue();
        byteArray[INDEX_VALUE] = value == null ? DEFAULT_ACTION_VALUE : value.byteValue();
        byteArray[INDEX_SOFT_DIM] = value == null ? DEFAULT_SOFT_DIM : value.byteValue();
        byteArray[INDEX_COND] = value == null ? DEFAULT_COND : value.byteValue();

        return byteArray;
    }

    public enum ActionType {
        OFF((byte) 0),
        ON((byte) 1),
        TOGGLE((byte) 2);

        private byte value;

        ActionType(byte value) {
            this.value = value;
        }

        byte getValue() {
            return this.value;
        }
    }

}
