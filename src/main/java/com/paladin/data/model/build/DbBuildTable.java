package com.paladin.data.model.build;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DbBuildTable {

    //
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    //
    private String connectionName;

    //
    private String tableName;

    //
    private String tableTitle;


}