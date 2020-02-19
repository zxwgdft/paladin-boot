package com.paladin.framework.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T> 泛型标记
 * @author TontoZhou
 * @since 2019/12/10
 */
@Getter
@Setter
@ToString
@ApiModel(description = "返回信息")
public class R<T> implements Serializable {

    @ApiModelProperty(value = "CODE", required = true)
    private int code;
    @ApiModelProperty(value = "是否成功", required = true)
    private boolean success;
    @ApiModelProperty(value = "消息", required = true)
    private String message;
    @ApiModelProperty("具体业务数据")
    private T data;

    private R(IResultCode resultCode) {
        this(resultCode, resultCode.getMessage(), null);
    }

    private R(IResultCode resultCode, T data) {
        this(resultCode, resultCode.getMessage(), data);
    }

    private R(IResultCode resultCode, String message, T data) {
        this.code = resultCode.getCode();
        this.message = message;
        this.data = data;
        this.success = HttpCode.SUCCESS == resultCode;
    }

    public final static R<?> SUCCESS = new R<>(HttpCode.SUCCESS);
    public final static R<?> FAILURE = new R<>(HttpCode.FAILURE);

    /**
     * 返回成功
     *
     * @param <T> 泛型标记
     * @return Result
     */
    public static <T> R<T> success() {
        return new R<>(HttpCode.SUCCESS);
    }

    /**
     * 成功-携带数据
     *
     * @param data 数据
     * @param <T>  泛型标记
     * @return Result
     */
    public static <T> R<T> success(T data) {
        return new R<>(HttpCode.SUCCESS, data);
    }

    /**
     * 根据状态返回成功或者失败
     *
     * @param status 状态
     * @param msg    异常msg
     * @param <T>    泛型标记
     * @return Result
     */
    public static <T> R<T> status(boolean status, String msg) {
        return status ? R.success() : R.fail(msg);
    }


    /**
     * 返回失败信息，用于 web
     *
     * @param msg 失败信息
     * @param <T> 泛型标记
     * @return {Result}
     */
    public static <T> R<T> fail(String msg) {
        return new R<T>(HttpCode.FAILURE, msg, null);
    }

    /**
     * 返回失败信息，用于 web
     *
     * @param msg  失败信息
     * @param data 数据
     * @param <T>  泛型标记
     * @return {Result}
     */
    public static <T> R<T> fail(String msg, T data) {
        return new R<T>(HttpCode.FAILURE, msg, data);
    }

    /**
     * 返回失败信息
     *
     * @param rCode 异常枚举
     * @param <T>   泛型标记
     * @return {Result}
     */
    public static <T> R<T> fail(IResultCode rCode) {
        return new R<>(rCode);
    }

    /**
     * 返回失败信息
     *
     * @param rCode 异常枚举
     * @param msg   失败信息
     * @param <T>   泛型标记
     * @return {Result}
     */
    public static <T> R<T> fail(IResultCode rCode, String msg) {
        return new R<T>(rCode, msg, null);
    }

    /**
     * 返回失败信息
     *
     * @param rCode 异常枚举
     * @param msg   失败信息
     * @param data  数据
     * @param <T>   泛型标记
     * @return {Result}
     */
    public static <T> R<T> fail(IResultCode rCode, String msg, T data) {
        return new R<T>(rCode, msg, data);
    }
}
