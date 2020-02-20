package com.paladin.data.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBConnectionDTO {

    private String name;
    private String url;
    private String type;
    private String userName;
    private String password;
    private String note;


}