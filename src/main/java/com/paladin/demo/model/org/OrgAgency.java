package com.paladin.demo.model.org;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.paladin.framework.api.DeletedBaseModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgAgency extends DeletedBaseModel {

    // 主键
    @TableId(type = IdType.AUTO)
    private Integer id;

    // 单位名称
    private String name;

    // 单位类型
    private Integer type;

    // 上级单位
    private Integer parentId;

    // 联系人
    private String contact;

    // 联系电话
    private String contactPhone;

    // 排序号
    private Integer orderNo;

}