package com.paladin.demo.controller.org.dto;

import com.paladin.common.core.export.ExportCondition;

public class OrgAgencyExportCondition extends ExportCondition {

    private OrgAgencyQuery query;

    public OrgAgencyQuery getQuery() {
        return query;
    }

    public void setQuery(OrgAgencyQuery query) {
        this.query = query;
    }
}