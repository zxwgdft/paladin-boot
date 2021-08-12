package com.paladin.framework.utils.secure;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RSA加密工具类
 *
 * @author TontoZhou
 * @since 2019/11/21
 */
public class RSAEncryptUtil {

    private static final Map<String, RSAKey> keyMap = new ConcurrentHashMap<>();

    private static final Map<String, RSAPublicKey> publicKeyMap = new ConcurrentHashMap<>();
    private static final Map<String, RSAPrivateKey> privateKeyMap = new ConcurrentHashMap<>();


    // 线程变量
    private static ThreadLocal<Cipher> cipherHolder = new ThreadLocal<Cipher>() {
        protected Cipher initialValue() {
            try {
                return Cipher.getInstance("RSA");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };


    /**
     * 获取RSA密钥，如果没有则创建（缓存控制）
     *
     * @param keyName 秘钥对名称，用于缓存查找
     * @param keySize 秘钥长度（bit）
     * @return 包含公钥私钥的封装类
     */
    public static RSAKey getRSAKey(String keyName, int keySize) throws Exception {
        return getRSAKey(keyName, keySize, true);
    }

    /**
     * 获取RSA密钥，如果没有则创建（缓存控制）
     *
     * @param keyName 秘钥对名称，用于缓存查找
     * @param keySize 秘钥长度（bit）
     * @param urlSafe 秘钥字符串是否url安全
     * @return 包含公钥私钥的封装类
     */
    public static RSAKey getRSAKey(String keyName, int keySize, boolean urlSafe) throws Exception {
        RSAKey key = keyMap.get(keyName);
        if (key == null) {
            synchronized (keyMap) {
                key = keyMap.get(keyName);
                if (key == null) {
                    // 获取键对
                    KeyPair keyPair = createRSAKeyPair(keySize);
                    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
                    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥

                    key = new RSAKey(keyName, publicKey, privateKey, urlSafe);

                    publicKeyMap.put(key.getPublicKeyString(), publicKey);
                    privateKeyMap.put(key.getPrivateKeyString(), privateKey);
                    keyMap.put(keyName, key);
                }
            }
        }
        return key;
    }

    /**
     * 创建一对秘钥
     *
     * @param keySize 秘钥长度（bit）
     */
    public static KeyPair createRSAKeyPair(int keySize) throws Exception {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(keySize, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        return keyPairGen.generateKeyPair();
    }

    /**
     * 获取公钥（会进行缓存控制，针对不怎么改变的秘钥）
     *
     * @param publicKeyString 公钥字符串
     */
    public static RSAPublicKey getRSAPublicKeyByCache(String publicKeyString) throws Exception {
        RSAPublicKey key = publicKeyMap.get(publicKeyString);
        if (key == null) {
            synchronized (publicKeyMap) {
                key = publicKeyMap.get(publicKeyString);
                if (key == null) {
                    //base64编码的公钥
                    byte[] decoded = Base64.decodeBase64(publicKeyString);
                    key = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
                    publicKeyMap.put(publicKeyString, key);
                }
            }
        }
        return key;
    }

    /**
     * 创建公钥
     *
     * @param publicKeyString 公钥字符串
     */
    public static RSAPublicKey getRSAPublicKey(String publicKeyString) throws Exception {
        byte[] decoded = Base64.decodeBase64(publicKeyString);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
    }


    /**
     * 获取私钥（会进行缓存控制，针对不怎么改变的秘钥）
     *
     * @param privateKeyString 私钥字符串
     */
    public static RSAPrivateKey getRSAPrivateKeyByCache(String privateKeyString) throws Exception {
        RSAPrivateKey key = privateKeyMap.get(privateKeyString);
        if (key == null) {
            synchronized (privateKeyMap) {
                key = privateKeyMap.get(privateKeyString);
                if (key == null) {
                    //base64编码的私钥
                    byte[] decoded = Base64.decodeBase64(privateKeyString);
                    key = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
                    privateKeyMap.put(privateKeyString, key);
                }
            }
        }
        return key;
    }


    /**
     * 创建私钥
     *
     * @param privateKeyString 私钥字符串
     */
    public static RSAPrivateKey getRSAPrivateKey(String privateKeyString) throws Exception {
        byte[] decoded = Base64.decodeBase64(privateKeyString);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    /**
     * 公钥加密
     *
     * @param content   加密内容
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密异常
     */
    public static String encrypt(String content, RSAPublicKey publicKey, boolean urlSafe) throws Exception {
        Cipher cipher = cipherHolder.get();
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content.getBytes("UTF-8"));
        return urlSafe ? Base64.encodeBase64URLSafeString(bytes) : Base64.encodeBase64String(bytes);
    }

    /**
     * 私钥解密
     *
     * @param content    加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String content, RSAPrivateKey privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(content.getBytes("UTF-8"));
        //RSA解密
        Cipher cipher = cipherHolder.get();
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(inputByte));
    }


    public static void main(String[] args) throws Exception {
        RSAKey key = RSAEncryptUtil.getRSAKey("random", 512);

        String pri = key.getPrivateKeyString();
        String pub = key.getPublicKeyString();

        System.out.println(pri);
        System.out.println("-----------------");
        System.out.println(pub);

    }

}
