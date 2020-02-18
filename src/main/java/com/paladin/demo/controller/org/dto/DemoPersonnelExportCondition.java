package com.paladin.demo.controller.org.dto;

import com.paladin.common.core.export.ExportCondition;
import com.paladin.demo.service.org.dto.DemoPersonnelQuery;

public class DemoPersonnelExportCondition extends ExportCondition {

	private DemoPersonnelQuery query;

	public DemoPersonnelQuery getQuery() {
		return query;
	}

	public void setQuery(DemoPersonnelQuery query) {
		this.query = query;
	}

}