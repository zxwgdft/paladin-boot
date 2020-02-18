package com.paladin.framework.utils.secure;

import org.apache.commons.codec.binary.Base64;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author TontoZhou
 * @since 2019/11/25
 */
public class RSAKey {

    private String keyName;

    private RSAPrivateKey privateKey;   // 私钥
    private RSAPublicKey publicKey;     // 公钥

    private String privateKeyString;
    private String publicKeyString;

    public RSAKey(String keyName, RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.keyName = keyName;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        this.privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()));
    }

    public boolean isThisPublicKey(String publicKeyString) {
        return this.publicKeyString.equals(publicKeyString);
    }

    public boolean isThisPrivateKey(String privateKeyString) {
        return this.privateKeyString.equals(privateKeyString);
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public String getPrivateKeyString() {
        return privateKeyString;
    }

    public String getPublicKeyString() {
        return publicKeyString;
    }

    public String getKeyName() {
        return keyName;
    }
}
