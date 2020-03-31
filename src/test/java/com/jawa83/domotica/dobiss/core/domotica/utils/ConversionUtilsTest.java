package com.jawa83.domotica.dobiss.core.domotica.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@Slf4j
class ConversionUtilsTest {

    @Test
    public void test() {
        byte[] byteArray = new byte[8];
        Arrays.fill(byteArray, (byte) -1);
        System.out.println(byteArray);
    }

    @Test
    public void test2() {
        Integer test = 255;
        byte testByte = test.byteValue();
        log.info(ConversionUtils.byteToHex(testByte));
    }

    @Test
    void trimBytes() {
        byte[] byteArray = new byte[8];
        Arrays.fill(byteArray, (byte) -1);
        byteArray[6] = (byte) 0;
        log.info(ConversionUtils.bytesToHex(byteArray));
        byteArray = ConversionUtils.trimBytes(byteArray, (byte) -1);
        log.info(ConversionUtils.bytesToHex(byteArray));
    }
}