package com.paladin.common.service.cache;

import com.paladin.common.mapper.cache.SysVisitCacheMapper;
import com.paladin.common.model.cache.SysVisitCache;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.WebUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SysVisitCacheService extends ServiceSupport<SysVisitCache, SysVisitCacheMapper> {

    public void putCache(HttpServletRequest request, String key, String content) {
        if (content == null || content.length() == 0 || content.length() > 400) {
            return;
        }
        String ip = WebUtil.getIpAddress(request);
        if (ip == null || ip.length() == 0) {
            return;
        }

        SysVisitCache cache = getSqlMapper().getCache(key, ip);
        if (cache == null) {
            cache = new SysVisitCache();
            cache.setCode(key);
            cache.setIp(ip);
            save(cache);
        } else {
            cache.setValue(content);
            updateWhole(cache);
        }

    }

    public String getCache(HttpServletRequest request, String key) {
        String ip = WebUtil.getIpAddress(request);
        if (ip == null || ip.length() == 0) {
            return null;
        }
        SysVisitCache cache = getSqlMapper().getCache(key, ip);
        return cache == null ? null : cache.getValue();
    }


}