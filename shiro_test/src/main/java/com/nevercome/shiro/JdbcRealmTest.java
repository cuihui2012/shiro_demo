package com.nevercome.shiro;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class JdbcRealmTest {


    //设置数据源
    private DruidDataSource dataSource = new DruidDataSource();
    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/shiro");
        dataSource.setUsername("root");
        dataSource.setPassword("since2012");
    }


    private JdbcRealm jdbcRealm = new JdbcRealm();


    @Test
    public void test() {
        //构建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

        //设置数据源,设置realm,绑定 realm
        jdbcRealm.setDataSource(dataSource);
        defaultSecurityManager.setRealm(jdbcRealm);

        //设置权限开关,可查询权限数据,下述sql未设置时有默认的sql
        jdbcRealm.setPermissionsLookupEnabled(true);

        String userSql = "select password from users where username = ?";
        jdbcRealm.setAuthenticationQuery(userSql);

        String roleSql = "select role_name from test_user_roles where username = ?";
        jdbcRealm.setUserRolesQuery(roleSql);

        String permSql = "select permission from test_roles_permissions where role_name = ?";
        jdbcRealm.setPermissionsQuery(permSql);

        //Subject提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //securityManager进行认证
        UsernamePasswordToken token = new UsernamePasswordToken("sun", "123");
        //登录
        subject.login(token);
        //认证
        System.out.println("isAuthenticated: " + subject.isAuthenticated());

        subject.checkRoles("admin", "user");

        subject.checkPermissions("user:add", "admin:update");
    }

}
