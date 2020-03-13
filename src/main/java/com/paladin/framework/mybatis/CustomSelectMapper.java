package com.paladin.framework.mybatis;

import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.select.SelectAllMapper;
import tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;
import tk.mybatis.mapper.common.example.SelectCountByExampleMapper;
import tk.mybatis.mapper.common.example.SelectOneByExampleMapper;

/**
 * 继承自己的MyMapper
 */
public interface CustomSelectMapper<T> extends
        SelectAllMapper<T>,
        SelectByPrimaryKeyMapper<T>,
        SelectOneByExampleMapper<T>,
        SelectByExampleMapper<T>,
        SelectCountByExampleMapper<T>,
        Marker {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
}

