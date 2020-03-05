package com.jawa83.domotica.dobiss.core.domotica.client;

import com.jawa83.domotica.dobiss.core.domotica.model.request.DobissRequest;

import java.io.IOException;

public interface DobissClient {

    byte[] sendRequest(DobissRequest<?> request) throws Exception;

    byte[] sendRequest(byte[] params) throws Exception;

    byte[] sendRequest(byte[] params, int maxLines) throws Exception;

    void setKeepConnectionOpen(boolean keepOpen);

    void closeConnection() throws IOException;

}
