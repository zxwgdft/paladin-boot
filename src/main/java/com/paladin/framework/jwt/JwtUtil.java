package com.paladin.framework.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * @author TontoZhou
 * @since 2020/8/3
 */
public class JwtUtil {

    /**
     * 只支持hmac算法生成秘钥
     *
     * @param alg hmac算法类型
     * @return
     */
    public static byte[] generateKey(SignatureAlgorithm alg) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(alg.getJcaName());
            return keygen.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("创建Key异常", e);
        }
    }

    /**
     * 生成字符串形式的秘钥，只支持hmac算法
     *
     * @param alg hmac算法类型
     * @return
     */
    public static String generateBase64Key(SignatureAlgorithm alg) {
        return Base64.encodeBase64String(generateKey(alg));
    }

    public static void main(String[] args) {
        String key = generateBase64Key(SignatureAlgorithm.HS512);
        System.out.println(key);
    }
}
