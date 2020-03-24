package com.paladin.framework.jwt;

import com.paladin.framework.utils.secure.AESEncryptUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import java.util.Date;
import java.util.Map;

public class AESTokenProvider implements TokenProvider {

    private long tokenExpireMilliseconds;
    private String issuer;
    private byte[] keyBytes;

    public static AESTokenProvider randomInstance() {
        AESTokenProvider tokenProvider = new AESTokenProvider();
        tokenProvider.keyBytes = AESEncryptUtil.createKey();
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
                .signWith(SignatureAlgorithm.HS512, keyBytes)
                .compact();
    }


    public Claims parseJWT(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(keyBytes)
                .parseClaimsJws(jwtToken)
                .getBody();
    }


    public void setBase64Key(String base64Key) {
        this.keyBytes = Base64.decodeBase64(base64Key);
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setTokenExpireMilliseconds(long tokenExpireMilliseconds) {
        this.tokenExpireMilliseconds = tokenExpireMilliseconds;
    }
}
