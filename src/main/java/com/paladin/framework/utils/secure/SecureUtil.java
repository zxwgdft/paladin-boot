package com.paladin.framework.utils.secure;


import com.paladin.framework.utils.StringUtil;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 安全相关工具类
 */
public class SecureUtil {

    /**
     * 随机生成一个16字节
     */
    public static byte[] random16bytes() {
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

        return randomBytes;
    }

    public static String createSalt() {
        return Hex.encodeHexString(random16bytes());
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
