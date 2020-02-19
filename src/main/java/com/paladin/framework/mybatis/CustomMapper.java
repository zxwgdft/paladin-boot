package com.paladin.framework.mybatis;

import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.insert.InsertMapper;
import tk.mybatis.mapper.common.base.select.SelectAllMapper;
import tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper;
import tk.mybatis.mapper.common.example.*;

/**
 * 继承自己的MyMapper
 */

public interface CustomMapper<T> extends 
				SelectAllMapper<T>,
				SelectByPrimaryKeyMapper<T>,
				InsertMapper<T>,
				UpdateByPrimaryKeyMapper<T>,
				UpdateByPrimaryKeySelectiveMapper<T>,
				DeleteByPrimaryKeyMapper<T>,
				DeleteByExampleMapper<T>,
				SelectOneByExampleMapper<T>,
				SelectByExampleMapper<T>,
				SelectCountByExampleMapper<T>,
				UpdateByExampleMapper<T>,
				UpdateByExampleSelectiveMapper<T>,
				Marker {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
}
