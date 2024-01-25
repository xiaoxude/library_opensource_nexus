package com.netease.lowcode.auth;

import com.netease.lowcode.auth.domain.LCAPUser;
import com.netease.lowcode.auth.util.AuthManagerHelper;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.core.annotation.Required;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * System built in generic class
 * LCAPAuthService
 *
 * @author sys
 */
@Service
public class LCAPAuthService implements InitializingBean {

    @Value("${auth.token.secret:56F0D8DB90241C6E}")
    private volatile String secret;

    /**
     * 获取当前登录用户
     * @return LCAPUser 用户信息
     */
    @NaslLogic(override = true,enhance = false)
    public static LCAPUser getUser(){
        //获取用户信息
        return AuthManagerHelper.parseToken();
    }
    /**
     * 创建Token
     * @param lcapUser  用户信息
     * @return
     */
    @NaslLogic(override = true,enhance = false)
    public static Boolean createToken(@Required LCAPUser lcapUser){
        //颁发
        AuthManagerHelper.createToken(lcapUser);
        return true;
    }
    /**
     * 清除Token
     * @return
     */
    @NaslLogic(override = true,enhance = false)
    public static Boolean removeToken(){
        //注销
        AuthManagerHelper.removeToken();
        return true;
    }
    @Override
    public void afterPropertiesSet() {
        AuthManagerHelper.SECRET = secret;
    }
}
