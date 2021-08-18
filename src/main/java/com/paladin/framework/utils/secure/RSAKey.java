package com.paladin.framework.utils.secure;

import lombok.Getter;
import org.apache.commons.codec.binary.Base64;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author TontoZhou
 * @since 2019/11/25
 */
@Getter
public class RSAKey {

    private String keyName;

    private RSAPrivateKey privateKey;   // 私钥
    private RSAPublicKey publicKey;     // 公钥

    private String privateKeyString;
    private String publicKeyString;

    public RSAKey(String keyName, RSAPublicKey publicKey, RSAPrivateKey privateKey, boolean urlSafe) {
        this.keyName = keyName;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.publicKeyString = urlSafe ? Base64.encodeBase64URLSafeString(publicKey.getEncoded()) :
                Base64.encodeBase64String(publicKey.getEncoded());
        this.privateKeyString = urlSafe ? Base64.encodeBase64URLSafeString(privateKey.getEncoded()) :
                Base64.encodeBase64String(privateKey.getEncoded());
    }

    public boolean isThisPublicKey(String publicKeyString) {
        return this.publicKeyString.equals(publicKeyString);
    }

    public boolean isThisPrivateKey(String privateKeyString) {
        return this.privateKeyString.equals(privateKeyString);
    }

}
