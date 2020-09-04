package com.shiro.demo;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义Realm
 */
public class CustomerRealm extends AuthorizingRealm {
    Map<String, String> userMap = new HashMap<String, String>();
    //Realm名称
    private String name = "customerRealm";
    //盐名称
    private String saltName="Mark";

    {
        //userMap.put("Mark", "123456");
        //密码加密
       // userMap.put("Mark",new Md5Hash("123456").toString());
        //密码加盐
        userMap.put("Mark",new Md5Hash("123456",saltName).toString());
        setName(name);
    }
    //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();

        //获取角色数据
        Set<String> roles = getRolesByUserName(username);

        //获取权限数据
        Set<String> permissions = getPermissionsByUserName(username);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);

        return authorizationInfo;
    }

    //认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //用户名
        String username = (String) token.getPrincipal();
        //从数据库中获取
        String password = getPaswordByUserName(username);
        if (password == null) {
            return null;
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password, name);

        //如果加盐，则需要设置盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(saltName));

        return authenticationInfo;
    }

    private String getPaswordByUserName(String username) {
        return userMap.get(username);
    }

    private Set<String> getPermissionsByUserName(String username) {
        Set<String> set = new HashSet<String>();
        set.add("user:add");
        set.add("user.delete");
        return set;
    }

    private Set<String> getRolesByUserName(String username) {
        Set<String> set = new HashSet<String>();
        set.add("admin");
        set.add("user");
        return set;
    }

}
