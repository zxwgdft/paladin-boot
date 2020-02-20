package com.paladin.common.controller.sys.dto;

import com.paladin.common.core.export.ExportCondition;
import com.paladin.common.service.sys.dto.SysLoggerLoginQuery;

public class SysLoggerLoginExportCondition extends ExportCondition {

	private SysLoggerLoginQuery query;

	public SysLoggerLoginQuery getQuery() {
		return query;
	}

	public void setQuery(SysLoggerLoginQuery query) {
		this.query = query;
	}

}