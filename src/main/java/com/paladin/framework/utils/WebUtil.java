package com.paladin.framework.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtil {

    /**
     * 是否Ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        // 判断是不是APP,是的话返回状态码，不返回登录页
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    /**
     * 获取服务器地址
     *
     * @param request
     * @return
     */
    public static String getServletPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    /**
     * 发送json格式回复
     *
     * @param response
     * @param obj
     */
    public static void sendJson(HttpServletResponse response, Object obj) {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        try {
            JsonUtil.writeJson(response.getWriter(), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送json格式回复，并解决跨域问题
     *
     * @param response
     * @param obj
     * @param allowOrigin
     */
    public static void sendJsonByCors(HttpServletResponse response, Object obj, String allowOrigin) {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Access-Control-Allow-Origin", allowOrigin);

        try {
            JsonUtil.writeJson(response.getWriter(), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送json格式回复，并解决跨域问题
     *
     * @param response
     * @param obj
     */
    public static void sendJsonByCors(HttpServletResponse response, Object obj) {
        sendJsonByCors(response, obj, "*");
    }

    /**
     * 判断请求内容是否json格式
     *
     * @param request
     * @return
     */
    public static boolean isJson(HttpServletRequest request) {
        return "application/json".equalsIgnoreCase(request.getContentType());
    }


    /**
     * 从请求中解析json内容
     *
     * @param request
     * @param valueType
     * @param <T>
     * @return
     */
    public static <T> T parseJsonFromRequest(HttpServletRequest request, Class<T> valueType) {
        try {
            return JsonUtil.parseJson(request.getInputStream(), valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
