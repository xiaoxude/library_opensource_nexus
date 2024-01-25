package com.netease.lowcode.auth.api.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.lowcode.auth.domain.LCAPUser;
import com.netease.lowcode.auth.util.AuthManagerHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * System built in generic class
 * LCAPAuthService
 *
 * @author sys
 */
public class DefaultLCAPAuthService implements LCAPAuthService {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, String> getSession(HttpServletRequest request) throws Exception {
        Map<String, String> sessionMap = new HashMap<>();
        LCAPUser lcapUser = AuthManagerHelper.parseToken(request);
        if (Objects.nonNull(lcapUser)) {
            sessionMap.put(SESSION_USERID_STR, lcapUser.userId);
            sessionMap.put(SESSION_USERNAME_STR, lcapUser.userName);
            sessionMap.put(SESSION_EXTRA_STR, objectMapper.writeValueAsString(lcapUser));
        }
        return sessionMap;
    }

    @Override
    public boolean match(HttpServletRequest request) {
        return AuthManagerHelper.containsSessionKey(request);
    }

    @Override
    public boolean isRemoteUser() {
        return true;
    }

    @Override
    public void clearSession(HttpServletRequest request, HttpServletResponse response) {
        AuthManagerHelper.removeToken();
    }

    @Override
    public String type() {
        return "SYSTEM";
    }
}
