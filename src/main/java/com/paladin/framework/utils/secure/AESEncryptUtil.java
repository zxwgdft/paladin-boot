package com.paladin.framework.utils.secure;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;


/**
 * AES加密解密工具
 *
 * @author TontoZhou
 * @since 2020/1/17
 */
public class AESEncryptUtil {

    // 线程变量
    private static ThreadLocal<Cipher> cipherHolder = new ThreadLocal<Cipher>() {
        protected Cipher initialValue() {
            try {
                return Cipher.getInstance("AES/CBC/PKCS5Padding");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    // 加密
    public static String encrypt(String content, String key, boolean urlSafe) throws Exception {
        byte[] raw = Base64.decodeBase64(key);
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = cipherHolder.get();
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return urlSafe ? Base64.encodeBase64URLSafeString(encrypted) : Base64.encodeBase64String(encrypted);
    }

    // 解密
    public static String decrypt(String content, String key) throws Exception {
        byte[] raw = Base64.decodeBase64(key);
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = cipherHolder.get();
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] original = cipher.doFinal(Base64.decodeBase64(content));
        return new String(original);
    }

    public static byte[] createKey() {
        return createKey(128);
    }

    public static byte[] createKey(int length) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(length);
            return keygen.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("创建Key异常", e);
        }
    }

    public static String createKeyString() {
        return createKeyString(128, true);
    }

    public static String createKeyString(int length, boolean urlSafe) {
        return urlSafe ? Base64.encodeBase64URLSafeString(createKey(length)) :
                Base64.encodeBase64String(createKey(length));
    }

    public static void main(String[] args) throws Exception {
        String key = createKeyString();
        System.out.println("key:" + key);
        String encrypted = encrypt("呀哈哈", key, true);
        System.out.println("encrypted:" + encrypted);
        String decrypted = decrypt(encrypted, key);
        System.out.println("decrypted:" + decrypted);
    }
}
