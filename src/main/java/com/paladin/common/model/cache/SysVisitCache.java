package com.paladin.common.model.cache;

import com.paladin.framework.core.configuration.mybatis.GenIdImpl;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;

@Getter
@Setter
public class SysVisitCache {

    @Id
    @KeySql(genId = GenIdImpl.class)
    private String id;

    private String ip;

    private String code;

    private String value;


}