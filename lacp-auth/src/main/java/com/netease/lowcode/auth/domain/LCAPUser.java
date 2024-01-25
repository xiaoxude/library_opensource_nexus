package com.netease.lowcode.auth.domain;

import com.netease.lowcode.core.annotation.NaslStructure;
import com.netease.lowcode.core.annotation.Required;

import java.util.Map;
/**
 * System built in generic class
 * LCAPUser
 *
 * @author sys
 */
@NaslStructure
public class LCAPUser {
    @Required
    public String userId;
    @Required
    public String userName;
    public Map<String,String> extensionInfos;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, String> getExtensionInfos() {
        return extensionInfos;
    }

    public void setExtensionInfos(Map<String, String> extensionInfos) {
        this.extensionInfos = extensionInfos;
    }
}
