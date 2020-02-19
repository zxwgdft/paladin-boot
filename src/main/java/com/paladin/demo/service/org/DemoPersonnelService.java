package com.paladin.demo.service.org;

import java.util.List;

import org.springframework.stereotype.Service;

import com.paladin.demo.model.org.DemoPersonnel;
import com.paladin.framework.common.Condition;
import com.paladin.framework.common.QueryType;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class DemoPersonnelService extends ServiceSupport<DemoPersonnel> {

	public List<DemoPersonnel> findPersonnel1() {
		// 简单查询可以这样输入过滤条件
		return searchAll(new Condition(DemoPersonnel.COLUMN_FIELD_IDENTIFICATIONNO, QueryType.LIKE, "320"),
				new Condition(DemoPersonnel.COLUMN_FIELD_SEX, QueryType.EQUAL, 1));
	}
	
	public List<DemoPersonnel> findPersonnel2() {
		// 通过Example可以功能更全面的进行查询排序
		Example example = new Example(DemoPersonnel.class);
		Criteria criteria = example.createCriteria();
		criteria.andLike(DemoPersonnel.COLUMN_FIELD_IDENTIFICATIONNO, "320");
		criteria.andEqualTo(DemoPersonnel.COLUMN_FIELD_SEX, 1);
		
		return searchAll(example);
	}
	
}