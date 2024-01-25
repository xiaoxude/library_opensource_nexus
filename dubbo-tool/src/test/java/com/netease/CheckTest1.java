package com.netease;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.lowcode.dubbo.dubbo.generic.DubboGenericUtil;
import com.netease.lowcode.dubbo.dubbo.generic.Parameter;
import com.netease.lowcode.dubbo.dubbo.generic.ParameterList;
import com.netease.lowcode.dubbo.spring.DubboStarterSpringEnvironmentConfiguration;
import org.apache.dubbo.config.RegistryConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 打包时需要注释掉Test的注解
 */
@SpringBootTest(classes = DubboStarterSpringEnvironmentConfiguration.class)
@RunWith(SpringRunner.class)
public class CheckTest1 {
    @Resource
    List<RegistryConfig> registryConfigs;
    @Resource
    private DubboGenericUtil dubboGenericUtil;

    //    @Test
    public void queryDataTest() {
        System.out.println();
    }

    @Test
    public void genericTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> verifyMap = new HashMap<>();
        verifyMap.put("loginToken", "11");
        paramsMap.put("params", objectMapper.writeValueAsString(verifyMap));

        Parameter parameter = new Parameter();
        parameter.setType("co.faao.plugin.transmission.request.MessageHeader");
        parameter.setArg(paramsMap);
        List<Parameter> parameterLists = new ArrayList<>();
        parameterLists.add(parameter);
        ParameterList parameterList = new ParameterList();
        parameterList.setParameterParameter(parameterLists);
        String a = dubboGenericUtil.invoke("cc.ewell.authority.api.v3.service.IUserService",
                "3.0.0", "loginTokenVerification", parameterList, null);
        System.out.println(a);
    }
}
