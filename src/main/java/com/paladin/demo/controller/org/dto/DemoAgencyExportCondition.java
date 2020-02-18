package com.paladin.demo.controller.org.dto;

import com.paladin.common.core.export.ExportCondition;
import com.paladin.demo.service.org.dto.DemoAgencyQuery;

public class DemoAgencyExportCondition extends ExportCondition {

	private DemoAgencyQuery query;

	public DemoAgencyQuery getQuery() {
		return query;
	}

	public void setQuery(DemoAgencyQuery query) {
		this.query = query;
	}

}