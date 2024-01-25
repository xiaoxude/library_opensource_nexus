package com.netease.lowcode.auth.util;

import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.lowcode.auth.domain.LCAPUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author system
 */
public class AuthManagerHelper {

    public static String SECRET;
    /**
     * 登录凭证cookie名称
     */
    public static final String AUTH_TOKEN_NAME = "authorization";

    /**
     * 标识为该依赖库的特有的cookie
     * 目前代表authorization，需要该依赖库解析，和原有的普通登录颁发的authorization区分开
     */
    public static final String AUTH_LIBRARY_COOKIE_NAME = "_auth_lib_t";
    /**
     * jwt的key->userId
     */
    public static final String JWT_KEY_USERID = "UserId";
    /**
     * jwt的key->userName
     */
    public static final String JWT_KEY_USERNAME = "UserName";
    /**
     * jwt的extraInfo
     */
    public static final String JWT_KEY_EXTRA_INFO = "ExtraInfo";
    /**
     * cookie的过期时间
     */
    public static final Long EXPIRE_TIME = 86400L;

    private static ObjectMapper objectMapper = new ObjectMapper();



    public static void removeToken() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies) && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(AUTH_TOKEN_NAME)||cookies[i].getName().equals(AUTH_LIBRARY_COOKIE_NAME)) {
                    cookies[i].setMaxAge(0);
                    cookies[i].setPath("/");
                    response.addCookie(cookies[i]);
                }
            }
        }
    }

    public static void createToken(LCAPUser lcapUser) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        HashMap<String, String> map = new HashMap<>();
        map.put(JWT_KEY_USERID, lcapUser.userId);
        map.put(JWT_KEY_USERNAME, lcapUser.userName);
        if(Objects.nonNull(lcapUser.extensionInfos) &&!lcapUser.extensionInfos.isEmpty()){
            try {
                map.put(JWT_KEY_EXTRA_INFO,objectMapper.writeValueAsString(lcapUser.extensionInfos));
            } catch (JsonProcessingException ignoreException) {
            }
        }
        Cookie cookie = new Cookie(AUTH_TOKEN_NAME, new JwtUtil(SECRET).createToken(map, EXPIRE_TIME));
        cookie.setPath("/");
        cookie.setMaxAge(Math.toIntExact(EXPIRE_TIME));
        cookie.setHttpOnly(true);
        cookie.setSecure(false);

        Cookie cookieCsrf = new Cookie(AUTH_LIBRARY_COOKIE_NAME, UUID.randomUUID().toString().replace("-",""));
        cookieCsrf.setPath("/");
        cookieCsrf.setMaxAge(Math.toIntExact(EXPIRE_TIME + 1000));
        cookieCsrf.setHttpOnly(true);
        cookieCsrf.setSecure(false);
        response.addCookie(cookie);
        response.addCookie(cookieCsrf);
    }

    public static LCAPUser parseToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return parseToken(request);
    }

    public static LCAPUser parseToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies) && cookies.length > 0) {
            boolean isValidToken = Arrays.stream(cookies).anyMatch(cookie -> cookie.getName().equals(AUTH_LIBRARY_COOKIE_NAME));
            if(isValidToken){
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals(AUTH_TOKEN_NAME)) {
                        Map<String, Claim> claimMap = new JwtUtil(SECRET).decryptToken(cookies[i].getValue());
                        Iterator<String> keys = claimMap.keySet().iterator();
                        LCAPUser lcapUser = new LCAPUser();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            if(JWT_KEY_USERID.equals(key)){
                                lcapUser.userId = claimMap.get(key).asString();
                            }
                            if (JWT_KEY_USERNAME.equals(key)) {
                                lcapUser.userName = claimMap.get(key).asString();
                            }
                            if (JWT_KEY_EXTRA_INFO.equals(key)) {
                                try {
                                    lcapUser.extensionInfos = objectMapper.readValue(claimMap.get(key).asString(),new TypeReference<HashMap<String, String>>() {});
                                } catch (JsonProcessingException ignoreException) {
                                }
                            }
                        }
                        return lcapUser;
                    }
                }
            }
        }
        return null;
    }

    public static boolean containsSessionKey(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies) && cookies.length > 0) {
           return Arrays.stream(cookies).anyMatch(cookie -> cookie.getName().equals(AUTH_LIBRARY_COOKIE_NAME));
        }
        return false;
    }
}
