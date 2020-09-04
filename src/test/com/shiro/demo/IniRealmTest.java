package com.shiro.demo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * IniRealm 认证,授权
 */
public class IniRealmTest {

    @Test
    public void testAuthentication(){
        IniRealm iniRealm=new IniRealm("classpath:user.ini");

        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        //2.主题提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken("Mark","123456");

        //是否认证
        subject.login(token);
        System.out.println("isAuthenticated是否认证: "+subject.isAuthenticated());

        //角色
        subject.checkRole("admin");
        //subject.checkRoles("admin","user");

        //权限
        subject.checkPermission("user:update");





    }
}
