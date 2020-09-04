package com.shiro.demo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * SimpleAccountRealm 认证,授权
 */
public class SimpleAccountRealmTest {

    SimpleAccountRealm simpleAccountRealm=new SimpleAccountRealm();

    @Before
    public void addUser(){
        //认证
        // simpleAccountRealm.addAccount("Mark","123456");
        //授权
        simpleAccountRealm.addAccount("Mark","123456","admin","user");
    }

    @Test
    public void testAuthentication(){
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        //2.主题提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken("Mark","123456");

        //是否认证
        subject.login(token);
        System.out.println("isAuthenticated是否认证: "+subject.isAuthenticated());

        //是否授权
        //subject.checkRole("admin");
        subject.checkRoles("admin","user");



    }
}
