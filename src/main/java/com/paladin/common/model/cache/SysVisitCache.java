package com.paladin.common.model.cache;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysVisitCache {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String ip;

    private String code;

    private String value;


}