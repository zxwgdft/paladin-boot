package com.paladin.framework.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author TontoZhou
 * @since 2019/12/10
 */
@Getter
@Setter
@ToString
@ApiModel(description = "返回信息")
@NoArgsConstructor
public class BaseR implements Serializable {

    @ApiModelProperty(value = "CODE", required = true)
    private int code;
    @ApiModelProperty(value = "是否成功", required = true)
    private boolean success;
    @ApiModelProperty(value = "消息", required = true)
    private String message;

    private BaseR(IResultCode resultCode) {
        this(resultCode, resultCode.getMessage());
    }

    private BaseR(IResultCode resultCode, String message) {
        this.code = resultCode.getCode();
        this.message = message;
        this.success = HttpCode.SUCCESS == resultCode;
    }

    public final static BaseR SUCCESS = new BaseR(HttpCode.SUCCESS);
    public final static BaseR FAILURE = new BaseR(HttpCode.FAILURE);

    /**
     * 返回成功
     *
     * @return Result
     */
    public static BaseR success() {
        return new BaseR(HttpCode.SUCCESS);
    }


    /**
     * 根据状态返回成功或者失败
     *
     * @param status 状态
     * @param msg    异常msg
     * @return Result
     */
    public static BaseR status(boolean status, String msg) {
        return status ? BaseR.success() : BaseR.fail(msg);
    }


    /**
     * 返回失败信息，用于 web
     *
     * @param msg 失败信息
     * @return {Result}
     */
    public static BaseR fail(String msg) {
        return new BaseR(HttpCode.FAILURE, msg);
    }


    /**
     * 返回失败信息
     *
     * @param rCode 异常枚举
     * @return {Result}
     */
    public static BaseR fail(IResultCode rCode) {
        return new BaseR(rCode);
    }

    /**
     * 返回失败信息
     *
     * @param rCode 异常枚举
     * @param msg   失败信息
     * @return {Result}
     */
    public static BaseR fail(IResultCode rCode, String msg) {
        return new BaseR(rCode, msg);
    }

}