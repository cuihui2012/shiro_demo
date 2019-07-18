package com.nevercome.shiro;

import com.nevercome.shiro.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class CustomRealmTest {

    @Test
    public void test() {
        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

        //获取自定义的realm对象
        CustomRealm customRealm = new CustomRealm();
        defaultSecurityManager.setRealm(customRealm);

        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //设置加密次数
        matcher.setHashIterations(1);
        //设置加密算法名称
        matcher.setHashAlgorithmName("md5");
        //realm中设置加密
        customRealm.setCredentialsMatcher(matcher);


        //Subject提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //securityManager进行认证
        UsernamePasswordToken token = new UsernamePasswordToken("sun", "123");
        //登录
        subject.login(token);

        System.out.println("isAuthenticated: " + subject.isAuthenticated());

        subject.checkRoles("admin", "user");
        subject.checkPermissions("user:add", "admin:add");
    }
}
