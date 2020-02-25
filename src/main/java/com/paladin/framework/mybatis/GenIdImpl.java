package com.paladin.framework.mybatis;

import com.paladin.framework.utils.UUIDUtil;
import tk.mybatis.mapper.genid.GenId;

public class GenIdImpl implements GenId<String> {
    @Override
    public String genId(String table, String column) {
        return UUIDUtil.createUUID();
    }
}
