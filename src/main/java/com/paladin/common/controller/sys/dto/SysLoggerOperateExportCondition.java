package com.paladin.common.controller.sys.dto;

import com.paladin.common.core.export.ExportCondition;
import com.paladin.common.service.sys.dto.SysLoggerOperateQuery;

public class SysLoggerOperateExportCondition extends ExportCondition {

	private SysLoggerOperateQuery query;

	public SysLoggerOperateQuery getQuery() {
		return query;
	}

	public void setQuery(SysLoggerOperateQuery query) {
		this.query = query;
	}

}