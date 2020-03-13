package com.jawa83.domotica.dobiss.core.domotica.model.request;

public enum ModuleType {
    RELAY((byte) 8),
    DIMMER((byte) 16),
    ZERO_TO_TEN_VOLT((byte) 24);

    private byte value;

    ModuleType(byte value) {
        this.value = value;
    }

    byte getValue() {
        return this.value;
    }

}
