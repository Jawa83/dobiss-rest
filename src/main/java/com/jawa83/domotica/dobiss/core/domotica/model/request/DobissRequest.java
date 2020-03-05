package com.jawa83.domotica.dobiss.core.domotica.model.request;

public interface DobissRequest<O> {

    byte[] getRequestBytes();

    /**
     * Indication of the maximum amount of output lines we expect from Dobiss for this request.
     * The is only used for performance reasons (less lines = less actual requests).
     * If 0, then the default amount of max lines will be used.
     *
     * @return The maximum amount of output lines
     */
    int getMaxOutputLines();

    O execute() throws Exception;

    String executeHex() throws Exception;

}
