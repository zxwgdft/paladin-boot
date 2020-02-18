package com.paladin.framework.utils;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {

    /**
     * @param request
     * @return String
     * @description 获取IP地址
     * <p><strong>注意：</strong>IP地址经过多次反向代理后会有多个IP值，</p>
     * <p>其中第一个IP才是真实IP，所以不能通过request.getRemoteAddr()获取IP地址，</p>
     * <p>如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址</p>
     * <p>X-Forwarded-For中第一个非unknown的有效IP才是用户真实IP地址。</p>
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = null;
        // 获取用户真是的地址
        String Xip = request.getHeader("X-Real-IP");
        // 获取多次代理后的IP字符串值
        String XFor = request.getHeader("X-Forwarded-For");

        if (StringUtil.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            // 多次反向代理后会有多个IP值，第一个用户真实的IP地址
            int index = XFor.indexOf(",");
            if (index >= 0) {
                return XFor.substring(0, index);
            } else {
                return XFor;
            }
        }

        ip = Xip;
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
