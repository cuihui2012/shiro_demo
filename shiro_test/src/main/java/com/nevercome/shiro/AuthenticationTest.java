package com.nevercome.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationTest {

    private SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    //设置realm
    @Before
    public void addUser() {
        simpleAccountRealm.addAccount("sun", "123", "admin", "user");
    }

    @Test
    public void test() {
        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //SecurityManager绑定realm
        defaultSecurityManager.setRealm(simpleAccountRealm);

        //Subject提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //securityManager进行认证
        UsernamePasswordToken token = new UsernamePasswordToken("sun", "123");
        //登录
        subject.login(token);
        //认证
        System.out.println("isAuthenticated: " + subject.isAuthenticated());

//        //登出
//        subject.logout();
//        //认证
//        System.out.println("isAuthenticated: " + subject.isAuthenticated());

        subject.checkRoles("admin", "user");
    }

}
