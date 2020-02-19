package com.paladin.framework.jwt;

import com.paladin.framework.exception.SystemException;
import com.paladin.framework.exception.SystemExceptionCode;
import com.paladin.framework.utils.secure.RSAEncryptUtil;
import com.paladin.framework.utils.secure.RSAKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;

public class RSATokenProvider implements TokenProvider {

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    private long tokenExpireMilliseconds;
    private String issuer;

    public static RSATokenProvider randomInstance() {
        RSATokenProvider tokenProvider = new RSATokenProvider();
        RSAKey key = null;
        try {
            key = RSAEncryptUtil.getRSAKey("jwt", 1024);
        } catch (Exception e) {
            // 不会报错
        }
        tokenProvider.publicKey = key.getPublicKey();
        tokenProvider.privateKey = key.getPrivateKey();
        tokenProvider.tokenExpireMilliseconds = 30 * 60 * 1000;
        return tokenProvider;
    }

    public String createJWT(String subject, Map<String, Object> claims, Date expiration) {
        Date now = new Date();
        if (expiration == null) {
            expiration = new Date(now.getTime() + this.tokenExpireMilliseconds);
        }
        return Jwts.builder()
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiration)
                // claims加在最后，可覆盖之前的build
                .addClaims(claims)
                .signWith(SignatureAlgorithm.RS512, privateKey)
                .compact();
    }

    public Claims parseJWT(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(jwtToken)
                .getBody();
    }


    public void setPublicKey(String base64PublicKey) {
        try {
            this.publicKey = RSAEncryptUtil.getRSAPublicKey(base64PublicKey);
        } catch (Exception e) {
            throw new SystemException(SystemExceptionCode.CODE_ERROR_CONFIG, "设置Base64公钥错误", e);
        }
    }

    public void setPrivateKey(String base64PrivateKey) {
        try {
            this.privateKey = RSAEncryptUtil.getRSAPrivateKey(base64PrivateKey);
        } catch (Exception e) {
            throw new SystemException(SystemExceptionCode.CODE_ERROR_CONFIG, "设置Base64私钥错误", e);
        }
    }

    public void setTokenExpireMilliseconds(long tokenExpireMilliseconds) {
        this.tokenExpireMilliseconds = tokenExpireMilliseconds;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
