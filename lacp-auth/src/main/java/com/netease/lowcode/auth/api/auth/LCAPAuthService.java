package com.netease.lowcode.auth.api.auth;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 低代码认证服务
 * 框架的扩展点，认证依赖库可以集成实现
 * @author system
 */
public interface LCAPAuthService {
    String SESSION_USERID_STR = "UserId";
    String SESSION_USERNAME_STR = "UserName";
    String SESSION_PHONE_STR = "Phone";
    String SESSION_EMAIL_STR = "Email";
    String SESSION_NICKNAME_STR = "NickName";
    String SESSION_SOURCE_STR = "Source";
    /**
     * 可为空，预留扩展使用
     */
    String SESSION_EXTRA_STR = "ExtraInfo";

    /**
     * 扩展点NASL: nasl.auth.userInfo,nasl.auth.getCurrentUserInfo
     * 识别当前用户身份
     * @param request
     * @return map 默认应该包括 userId,userName,Phone,Email,NickName,Source
     * @throws Exception
     */
    Map<String, String> getSession(HttpServletRequest request) throws Exception;


    /**
     * 当前会话是否匹配当前认证服务
     *
     * @param request
     * @return
     */
    boolean match(HttpServletRequest request);

    /**
     * 是否为远程用户管理或不需要用户管理
     * @return
     */
    boolean isRemoteUser();
    /**
     * 扩展点NASL:nasl.auth.logout
     * 注销登出
     * @param request
     * @param response
     * @throws Exception
     */
    void clearSession(HttpServletRequest request, HttpServletResponse response)  throws Exception;

    /**
     * 认证类型
     *
     * @return
     */
    String type();

    /**
     * 配置，可返回为空，预留方法
     * @return Map
     */
    default Map<String,String> properties(){return new HashMap<>();}
}
