package com.paladin.common.service.cache;

import javax.servlet.http.HttpServletRequest;

import com.paladin.framework.utils.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paladin.common.mapper.cache.SysVisitCacheMapper;
import com.paladin.common.model.cache.SysVisitCache;
import com.paladin.framework.core.ServiceSupport;

@Service
public class SysVisitCacheService extends ServiceSupport<SysVisitCache> {

	@Autowired
	private SysVisitCacheMapper visitCacheMapper;

	public int putCache(HttpServletRequest request, String key, String content) {

		if (content == null || content.length() == 0 || content.length() > 400) {
			return 0;
		}

		String ip = IPUtil.getIpAddress(request);

		if (ip == null || ip.length() == 0) {
			return 0;
		}

		SysVisitCache cache = visitCacheMapper.getCache(key, ip);
		
		if(cache == null) {
			cache = new SysVisitCache();		
			cache.setCode(key);
			cache.setIp(ip);
		}
		
		cache.setValue(content);

		return saveOrUpdate(cache);
	}

	public String getCache(HttpServletRequest request, String key) {

		String ip = IPUtil.getIpAddress(request);

		if (ip == null || ip.length() == 0) {
			return null;
		}

		SysVisitCache cache = visitCacheMapper.getCache(key, ip);
		return cache == null ? null : cache.getValue();
	}

	

}