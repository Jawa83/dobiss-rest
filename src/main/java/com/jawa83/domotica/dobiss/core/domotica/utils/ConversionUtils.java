package com.jawa83.domotica.dobiss.core.domotica.utils;

import java.util.Arrays;

/**
 * Created by wardjanssens on 25/02/2017.
 */

public class ConversionUtils {

    final private static char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(byteToHex(aByte));
        }
        return result.toString();
    }

    public static String byteToHex(byte aByte) {
        char[] hexChars = new char[2];
        int v = aByte & 0xFF;
        hexChars[0] = HEX_ARRAY[v >>> 4];
        hexChars[1] = HEX_ARRAY[v & 0x0F];
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

    public static byte[] trimBytes(byte[] bytes, byte trimByte) {
        int index = bytes.length;
        while (bytes[index - 1] == trimByte) {
            index--;
        }
        return Arrays.copyOf(bytes, index);
    }

}
