package com.jawa83.domotica.dobiss.core.domotica.client;

import com.jawa83.domotica.dobiss.core.domotica.model.request.DobissRequest;
import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

@Slf4j
@Component
public class DobissClientImpl implements DobissClient {

    private final static int DELAY_BEFORE_NEXT_ATTEMPT = 25;
    private final static int NUMBER_OF_ATTEMPTS = 25;
    private final static int DEFAULT_MAX_LINES = 100;
    private final static int SOCKET_TIMEOUT = 3000;

    // TODO load ip and port from preferences
    private final static String ip = "192.168.0.197";
    private final static int port = 1001;

    private Socket socket;
    private boolean keepConnectionOpen = false;

    @Override
    public byte[] sendRequest(DobissRequest<?> request) throws Exception {
        if (request.getMaxOutputLines() == 0) {
            return sendRequest(request.getRequestBytes());
        }
        return sendRequest(request.getRequestBytes(), request.getMaxOutputLines());
    }

    @Override
    public byte[] sendRequest(byte[] params) throws Exception {
        return sendRequest(params, DEFAULT_MAX_LINES);
    }

    // TODO cleanup
    @Override
    public byte[] sendRequest(byte[] params, int maxLines) throws Exception {
        try {
            InetAddress address = InetAddress.getByName(ip);

            if (socket == null || socket.isClosed() || !socket.isConnected()) {
                log.info("Opening new socket");
                socket = new Socket();
                socket.connect(new InetSocketAddress(address, port), SOCKET_TIMEOUT);
            }

            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.write(params);
            log.debug("Socket Request: " + ConversionUtils.bytesToHex(params));

            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);

            byte[] data = new byte[maxLines * 16];

            int count = 0;
            while (count < maxLines) {
                byte[] temp = new byte[16];
                int attempt = 0;
                while (dis.available() == 0 && attempt < NUMBER_OF_ATTEMPTS) {
                    // response might be a bit later
                    // wait a bit before checking again
                    Thread.sleep(DELAY_BEFORE_NEXT_ATTEMPT);
                    attempt++;
                }
                if (dis.available() != 0) {
                    if (dis.read(temp, 0, 16) > 0) {
                        log.debug("Socket response: " + ConversionUtils.bytesToHex(temp));
                        for (int j = 0; j < temp.length; j++) {
                            data[count * 16 + j] = temp[j];
                        }
                    }
                    count++;
                } else {
                    break;
                }
            }

            if (count == 0) {
                log.warn("Connection: No response for request " + ConversionUtils.bytesToHex(params));
                // TODO make custom Exception class
                throw new Exception("No response for request " + ConversionUtils.bytesToHex(params));
            }
            return Arrays.copyOfRange(data, 32, count * 16);

        } catch (SocketTimeoutException e) {
            log.warn("Connection: Timeout for request " + ConversionUtils.bytesToHex(params));
            throw e;
        }catch (Exception e) {
            log.error("Connection error: ", e);
            throw e;
        } finally {
            if (socket != null && socket.isConnected() && !this.keepConnectionOpen) {
                socket.close();
            }
        }
    }

    @Override
    public void setKeepConnectionOpen(boolean keepOpen) {
        this.keepConnectionOpen = keepOpen;
    }

    @Override
    public void closeConnection() throws IOException {
        if (socket.isConnected()) {
            socket.close();
        }
    }
}
