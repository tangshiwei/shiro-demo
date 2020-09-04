package com.shiro.demo;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * JdbcRealm 认证,授权
 * 需要三张表：
 * user表,role表,role_permiss表
 */
public class JdbcRealmTest {
    DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }

    @Test
    public void testAuthentication() {
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        //注意点：设置权限开关，默认false
        jdbcRealm.setPermissionsLookupEnabled(true);

        //自定义查询语句
        String authenticationSql = "select password from users where username = ?";
        //String DEFAULT_SALTED_AUTHENTICATION_QUERY = "select password, password_salt from users where username = ?";
        String userRolesSql = "select role_name from user_roles where username = ?";
        String permissionsSql = "select permission from roles_permissions where role_name = ?";

        jdbcRealm.setAuthenticationQuery(authenticationSql);
        jdbcRealm.setUserRolesQuery(userRolesSql);
        jdbcRealm.setPermissionsQuery(permissionsSql);

        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        //2.主题提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("Mark", "123456");

        //是否认证
        subject.login(token);
        System.out.println("isAuthenticated是否认证: " + subject.isAuthenticated());

        //角色
        subject.checkRole("admin");
        //subject.checkRoles("admin","user");

        //权限
        subject.checkPermission("user:update");

    }
}
