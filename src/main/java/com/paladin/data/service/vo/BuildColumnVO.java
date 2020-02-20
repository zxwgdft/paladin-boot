package com.paladin.data.service.vo;

import com.paladin.data.controller.dto.ColumnDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildColumnVO {

    private ColumnDTO column;

    //
    private String title;

    // 常量CODE
    private String enumCode;

    // 是否多选
    private Integer multiSelect;

    // 是否附件
    private Integer isAttachment;

    // 附件数量
    private Integer attachmentCount;

    // 是否必填
    private Integer required;

    // 是否列表显示
    private Integer tableable;

    // 是否可编辑
    private Integer editable;

    // 是否新增
    private Integer addable;

    // 是否大文本
    private Integer largeText;

    // 最大长度
    private Integer maxLength;

    // 正则验证表达式
    private String regularExpression;


}
