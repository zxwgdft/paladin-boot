package com.paladin.data.service.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBConnectionVO {

    private String name;
    private String url;
    private String type;
    private String userName;
    private String password;
    private String note;

}