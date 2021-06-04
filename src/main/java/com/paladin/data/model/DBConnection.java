package com.paladin.data.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("db_connection")
public class DBConnection {

    @TableId(type = IdType.ASSIGN_UUID)
    private String name;

    private String url;

    private String type;

    private String userName;

    private String password;

    private String note;


}
