package com.paladin.framework.utils;

import org.apache.commons.codec.binary.Base64;

import java.util.concurrent.ThreadLocalRandom;

public class UUIDUtil {

    /**
     * @return 32位UUID
     */
    public static String create32UUID() {
        // 使用ThreadLocalRandom获取UUID获取更优的效果
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long mostSigBits = random.nextLong();
        long leastSigBits = random.nextLong();

        return (digits(mostSigBits >> 32, 8) +
                digits(mostSigBits >> 16, 4) +
                digits(mostSigBits, 4) +
                digits(leastSigBits >> 48, 4) +
                digits(leastSigBits, 12));
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    /**
     * @return 压缩的UUID（对32位UUID进行Base64编码）
     */
    public static String createUUID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long mostSigBits = random.nextLong();
        long leastSigBits = random.nextLong();

        byte[] randomBytes = new byte[16];

        randomBytes[0] = (byte) (mostSigBits >> 56);
        randomBytes[1] = (byte) (mostSigBits >> 48);
        randomBytes[2] = (byte) (mostSigBits >> 40);
        randomBytes[3] = (byte) (mostSigBits >> 32);
        randomBytes[4] = (byte) (mostSigBits >> 24);
        randomBytes[5] = (byte) (mostSigBits >> 16);
        randomBytes[6] = (byte) (mostSigBits >> 8);
        randomBytes[7] = (byte) (mostSigBits);

        randomBytes[8] = (byte) (leastSigBits >> 56);
        randomBytes[9] = (byte) (leastSigBits >> 48);
        randomBytes[10] = (byte) (leastSigBits >> 40);
        randomBytes[11] = (byte) (leastSigBits >> 32);
        randomBytes[12] = (byte) (leastSigBits >> 24);
        randomBytes[13] = (byte) (leastSigBits >> 16);
        randomBytes[14] = (byte) (leastSigBits >> 8);
        randomBytes[15] = (byte) (leastSigBits);

        return Base64.encodeBase64URLSafeString(randomBytes);
    }


    public static void main(String[] args) {
        System.out.println(createUUID());
    }

}
