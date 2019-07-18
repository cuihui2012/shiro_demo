package com.nevercome.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {

    Map<String, String> userMap = new HashMap();
    Set<String> roles = new HashSet<String>();
    Set<String> permissions = new HashSet<String>();

    {
        userMap.put("sun", "1d7b217127d82ea1eac7e3b92090a463");
        //模拟角色
        roles.add("admin");
        roles.add("user");
        //模拟权限
        permissions.add("user:add");
        permissions.add("admin:add");

        //设置realmname
        super.setName("customRealm");
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //获取用户名
        String username = (String) principalCollection.getPrimaryPrincipal();
        //从数据库或者缓存中获取角色/权限信息
        Set<String> roles = getRolesByUsername(username);
        Set<String> permissions = getRolesByPermissions(username);


        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);

        return simpleAuthorizationInfo;
    }

    //模拟数据库
    private Set<String> getRolesByPermissions(String username) {
        return permissions;
    }

    //模拟数据库
    private Set<String> getRolesByUsername(String username) {
        return roles;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //从主体获取提交的认证信息,获得用户名
        String username = (String) authenticationToken.getPrincipal();

        //从数据库验证(模拟),通常去访问数据库获取密码
        String password = getPasswordByUsername(username);
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, "customRealm");
        //加盐
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("zaq"));
        return simpleAuthenticationInfo;
    }

    //模拟查询数据库获取用户密码
    private String getPasswordByUsername(String username) {
        return userMap.get(username);
    }


    public static void main(String args[]) {
        //md5加密
//        Md5Hash md5Hash = new Md5Hash("123");
        //md5加密加盐
        Md5Hash md5Hash = new Md5Hash("123", "zaq");
        System.out.println(md5Hash.toString());
    }
}
