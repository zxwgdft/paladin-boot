package com.paladin.framework.utils.secure;


import com.paladin.framework.utils.StringUtil;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class SecureUtil {

    private final static SecureRandom secureRandom = new SecureRandom();

    public static String createSalt() {
        return createSalt(16);
    }

    public static String createSalt(int numBytes) {
        byte[] bytes = new byte[numBytes];
        secureRandom.nextBytes(bytes);
        return Hex.encodeHexString(bytes);
    }

    public static String hashByMD5(String content) {
        return hashByMD5(content, null);
    }

    public static String hashByMD5(String content, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            if (StringUtil.isNotEmpty(salt)) {
                digest.update(salt.getBytes(StandardCharsets.UTF_8));
            }

            byte[] hashed = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hashed);
        } catch (Exception e) {
            throw new RuntimeException("hash by md5 error", e);
        }
    }

}
