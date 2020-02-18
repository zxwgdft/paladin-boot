package com.paladin.common.controller.syst.dto;

import com.paladin.common.core.export.ExportCondition;
import com.paladin.common.service.syst.dto.SysLoggerLoginQuery;

public class SysLoggerLoginExportCondition extends ExportCondition {

	private SysLoggerLoginQuery query;

	public SysLoggerLoginQuery getQuery() {
		return query;
	}

	public void setQuery(SysLoggerLoginQuery query) {
		this.query = query;
	}

}