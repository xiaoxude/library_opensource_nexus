# lcap-auth
## 运行环境
jdk1.8

## 项目介绍
在实际的业务中，B端场景往往需要实现单点登录，那么制品应用现有的登录逻辑便不满足需求了，所以这里在ide3.0版本后制品应用默认引入了lcap-auth依赖库，用来完成登录流程中的token颁发、认证、清除token。
## 项目结构
```
src
└──main
───└──resources
──────└──META-INF
─────────└──spring.factories    # spring 自动扫描配置
─────────└──services
────────────└──com.netease.lowcode.auth.api.auth.LCAPAuthService # spi指定实现类
───└──java
──────└──com
─────────└──netease
────────────└──lowcode
───────────────└──auth
──────────────────└──util
─────────────────────└──JwtUtil.java
─────────────────────└──AuthManagerHelper.java
─────────────────────└──SpringEnvironmentConfiguration.java
──────────────────└──LCAPAuthService.java # 跟制品应用约定的权限服务结构，不可修改包名、类名和结构等
──────────────────└──LibraryAutoScan.java
──────────────────└──api
─────────────────────└──auth
────────────────────────└──DefaultLCAPAuthService.java # spi实现类
────────────────────────└──LCAPAuthService.java # 依赖库给ide提供的接口，在ide的逻辑调用中使用本接口的方法，操作用户权限信息。可自行开发。
──────────────────└──domain
─────────────────────└──LCAPUser.java
```
## 二次开发
1. 实现LCAPAuthService接口。具体见LCAPAuthService.java中的注释。
```java
@Service
public class DemoLCAPAuthServiceImpl implements LCAPAuthService {

    /**
     * 获取session
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> getSession(HttpServletRequest request) throws Exception {
        Map<String, String> sessionMap = new HashMap<>();
        //todo
        return sessionMap;
    }

    @Override
    public boolean match(HttpServletRequest request) {
        //todo
        return true;
    }

    @Override
    public boolean isRemoteUser() {
        //todo
        return true;
    }

    @Override
    public void clearSession(HttpServletRequest request, HttpServletResponse response) {
        //todo
     }

    @Override
    public String type() {
        return "demo";
    }
}
```
2. 在spring.factories中配置
```
com.netease.lowcode.auth.api.auth.LCAPAuthService=com.netease.lowcode.auth.api.auth.DefaultLCAPAuthService
```
通过spi的方式注入扩展的自定义权限认证服务。
## 打包
mvn clean package打依赖库zip包，上传至资产中心使用。

