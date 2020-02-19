package com.paladin.framework.common;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author TontoZhou
 * @since 2019/12/10
 */
@Getter
@AllArgsConstructor
@ApiModel(description = "系统内置code")
public class FeignCode implements IResultCode {
    private int code;
    private String message;
}
