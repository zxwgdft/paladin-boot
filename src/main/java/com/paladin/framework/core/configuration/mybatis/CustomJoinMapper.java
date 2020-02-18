package com.paladin.framework.core.configuration.mybatis;

import tk.mybatis.mapper.common.join.SelectAllJoinMapper;
import tk.mybatis.mapper.common.join.SelectExampleJoinMapper;
import tk.mybatis.mapper.common.join.SelectOneJoinMapper;

public interface CustomJoinMapper<T, J> extends 
				SelectAllJoinMapper<T, J>, 
				SelectExampleJoinMapper<T, J>,
				SelectOneJoinMapper<T, J>	{

}
