package com.paladin.framework.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;

public class UUIDUtil {

    private final static SecureRandom numberGenerator = new SecureRandom();

    /**
     * 创建32位UUID
     *
     * @return
     */
    public static String create32UUID() {
        byte[] randomBytes = new byte[16];
        numberGenerator.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f; /* clear version */
        randomBytes[6] |= 0x40; /* set to version 4 */
        randomBytes[8] &= 0x3f; /* clear variant */
        randomBytes[8] |= 0x80; /* set to IETF variant */

        byte[] data = randomBytes;

        long msb = 0;
        long lsb = 0;
        assert data.length == 16;
        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (data[i] & 0xff);
        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);

        return (digits(msb >> 32, 8) + digits(msb >> 16, 4) + digits(msb, 4) + digits(lsb >> 48, 4) + digits(lsb, 12));
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    /**
     * 创建压缩的UUID（对32位UUID进行Base64编码）
     *
     * @return
     */
    public static String createUUID() {
        byte[] randomBytes = new byte[16];
        numberGenerator.nextBytes(randomBytes);
        return Base64.encodeBase64URLSafeString(randomBytes);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(createUUID());
        }
    }

}
