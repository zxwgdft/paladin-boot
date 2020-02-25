package com.paladin.common.utils;

import com.paladin.framework.utils.WebUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.web.subject.WebSubject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单实现的IP登录失败限制器，只要该IP登录连续失败次数超过一定数量则锁定该IP
 * 只是在内存中记录了数据，只是用于应付单机情况下，如果需要实现集群和持久化，需要扩写
 * （应付安全测试工具）
 *
 * @author TontoZhou
 * @since 2019/11/27
 */
public class IPLoginFailLimiter extends HashedCredentialsMatcher {

    public Map<String, LimitStatus> blacklist = new ConcurrentHashMap<>();

    private static int LOGIN_FAIL_LIMIT_NUM = 5;
    private static long LOGIN_FAIL_LIMIT_TIME = 60 * 60 * 1000L; //1小时

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String ip;
        if (token instanceof HostAuthenticationToken) {
            ip = ((HostAuthenticationToken) token).getHost();
        } else {
            // 获取request ip
            WebSubject webSubject = (WebSubject) SecurityUtils.getSubject();
            HttpServletRequest request = (HttpServletRequest) webSubject.getServletRequest();
            ip = WebUtil.getIpAddress(request);
        }

        LimitStatus status = blacklist.get(ip);
        if (status != null && status.limit) {
            if (System.currentTimeMillis() - status.limitTime > LOGIN_FAIL_LIMIT_TIME) {
                synchronized (blacklist) {
                    if (System.currentTimeMillis() - status.limitTime > LOGIN_FAIL_LIMIT_TIME) {
                        status.errorNum = 0;
                        status.limit = false;
                    } else {
                        throw new AuthenticationException("多次登录失败，IP已被锁定！");
                    }
                }
            } else {
                throw new AuthenticationException("多次登录失败，IP已被锁定！");
            }
        }

        boolean match = super.doCredentialsMatch(token, info);
        if (!match) {
            synchronized (blacklist) {
                if (status == null) {
                    status = new LimitStatus(ip);
                    blacklist.put(ip, status);
                }

                status.errorNum++;
                if (status.errorNum >= LOGIN_FAIL_LIMIT_NUM) {
                    status.limit = true;
                    status.limitTime = System.currentTimeMillis();
                }
            }
        } else {
            if (status != null) {
                status.errorNum = 0;
                status.limit = false;
            }
        }

        return match;
    }

    private static class LimitStatus {
        String ip;
        int errorNum;
        long limitTime;
        boolean limit;

        LimitStatus(String ip) {
            this.ip = ip;
            errorNum = 0;
            limitTime = 0;
            limit = false;
        }
    }
}
