package com.paladin.common.service.cache;

import com.paladin.common.mapper.cache.SysVisitCacheMapper;
import com.paladin.common.model.cache.SysVisitCache;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SysVisitCacheService extends ServiceSupport<SysVisitCache> {

    @Autowired
    private SysVisitCacheMapper visitCacheMapper;

    public boolean putCache(HttpServletRequest request, String key, String content) {
        if (content == null || content.length() == 0 || content.length() > 400) {
            return false;
        }
        String ip = WebUtil.getIpAddress(request);
        if (ip == null || ip.length() == 0) {
            return false;
        }
        SysVisitCache cache = visitCacheMapper.getCache(key, ip);
        if (cache == null) {
            cache = new SysVisitCache();
            cache.setCode(key);
            cache.setIp(ip);
        }
        cache.setValue(content);
        return saveOrUpdate(cache);
    }

    public String getCache(HttpServletRequest request, String key) {
        String ip = WebUtil.getIpAddress(request);
        if (ip == null || ip.length() == 0) {
            return null;
        }
        SysVisitCache cache = visitCacheMapper.getCache(key, ip);
        return cache == null ? null : cache.getValue();
    }


}