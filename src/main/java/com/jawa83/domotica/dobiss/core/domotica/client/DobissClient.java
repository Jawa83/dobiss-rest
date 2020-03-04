package com.jawa83.domotica.dobiss.core.domotica.client;

public interface DobissClient {

    byte[] sendRequest(byte[]... params) throws Exception;

}
