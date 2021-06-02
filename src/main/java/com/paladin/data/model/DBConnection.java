package com.paladin.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "db_connection")
public class DBConnection {

    @Id
    private String name;

    private String url;

    private String type;

    private String userName;

    private String password;

    private String note;


}
