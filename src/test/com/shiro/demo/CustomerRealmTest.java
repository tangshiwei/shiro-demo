package com.shiro.demo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * 自定义 认证,授权
 */
public class CustomerRealmTest {

    @Test
    public void testAuthentication(){
        CustomerRealm customerRealm=new CustomerRealm();

        //加密
        HashedCredentialsMatcher matcher=new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);
        customerRealm.setCredentialsMatcher(matcher);


        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customerRealm);

        //2.主题提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken("Mark","123456");

        //是否认证
        subject.login(token);
        System.out.println("isAuthenticated是否认证: "+subject.isAuthenticated());


        subject.checkRole("admin");

        subject.checkPermission("user:add");





    }
}
