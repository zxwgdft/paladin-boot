package com.paladin.framework.web.filter;

import com.paladin.framework.utils.StringUtil;
import io.jsonwebtoken.lang.Collections;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author TontoZhou
 * @since 2020/6/12
 */
public class LimitFrameFilter extends OncePerRequestFilter {

    private String option;

    private LimitFrameFilter(String option) {
        this.option = option;
    }

    public static LimitFrameFilter createDenyInstance() {
        return new LimitFrameFilter("DENY");
    }

    public static LimitFrameFilter createSameOriginInstance() {
        return new LimitFrameFilter("SAMEORIGIN");
    }

    public static LimitFrameFilter createAllowFromInstance(String... addresses) {
        return new LimitFrameFilter("ALLOW-FROM " + StringUtil.join(Collections.arrayToList(addresses)));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("X-Frame-Options", option);
        filterChain.doFilter(request,response);
    }
}
