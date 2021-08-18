package com.paladin.framework.utils;

import com.paladin.framework.utils.secure.SecureUtil;
import org.apache.commons.codec.binary.Base64;

import java.util.concurrent.ThreadLocalRandom;

/**
 * ThreadLocalRandom 拥有更高的性能，jmh（10线程）测试下快了近10倍
 */
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
        return Base64.encodeBase64URLSafeString(SecureUtil.random16bytes());
    }


    public static void main(String[] args) {
        System.out.println(createUUID());
    }

}
