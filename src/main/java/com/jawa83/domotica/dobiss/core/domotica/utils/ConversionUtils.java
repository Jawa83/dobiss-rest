package com.jawa83.domotica.dobiss.core.domotica.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by wardjanssens on 25/02/2017.
 */

public class ConversionUtils {

    final private static char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static byte[] intToBytes(int value) {
        return  ByteBuffer.allocate(4).putInt(value).array();
    }

    public static int bytesToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static byte[] trimBytes(byte[] bytes, byte trimByte) {
        int index = bytes.length;
        while (bytes[index - 1] == trimByte) {
            index--;
        }
        return Arrays.copyOf(bytes, index);
    }

}
