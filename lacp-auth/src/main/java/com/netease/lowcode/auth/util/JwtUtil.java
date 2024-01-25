package com.netease.lowcode.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author system
 */
public class JwtUtil {

    private String secret;

    public JwtUtil(String secret) {
        this.secret = secret;
    }

    public String createToken(Map<String, String> claimMap, long expireTime) {
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("alg", "HS256");
        headMap.put("typ", "JWT");
        Date dateTime = new Date();
        //header
        JWTCreator.Builder jwt = JWT.create().withHeader(headMap);
        //expire
        jwt.withIssuedAt(dateTime).withExpiresAt(new Date(dateTime.getTime() + expireTime * 1000));
        if (isMapEmpty(claimMap)) {
            return jwt.sign(Algorithm.HMAC256(secret.getBytes()));
        }
        claimMap.forEach(jwt::withClaim);
        String token = jwt.sign(Algorithm.HMAC256(secret.getBytes()));
        return token;
    }

    private boolean isMapEmpty(Map<String, String> claimMap) {
        return claimMap == null || claimMap.isEmpty();
    }

    public Map<String, Claim> decryptToken(String token) throws JWTVerificationException {
        //服务器时间不同步处理方式--允许未来5分钟内的产生的Token
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret.getBytes())).acceptIssuedAt(300).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaims();
    }

}