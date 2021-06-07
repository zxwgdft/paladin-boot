package com.paladin.common.model.sys;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class SysConstant {

    @Id
    private String type;

    @Id
    private String code;

    private String name;

    private Integer orderNo;

}