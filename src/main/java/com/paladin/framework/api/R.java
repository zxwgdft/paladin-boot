package com.paladin.framework.api;


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
@ApiModel(description = "返回结果信息")
public class R<T> implements Serializable {

    @ApiModelProperty("CODE")
    private int code;
    @ApiModelProperty("是否成功")
    private boolean success;
    @ApiModelProperty("消息")
    private String message;
    @ApiModelProperty("具体业务数据")
    private T data;

    private R() {

    }

    public R(int code, boolean success, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public R(IResultCode resultCode, boolean success) {
        this(resultCode, success, null);
    }

    public R(IResultCode resultCode, boolean success, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
        this.success = success;
    }

    public final static R<?> SUCCESS = new R<>(HttpCode.SUCCESS, true);
    public final static R<?> FAILURE = new R<>(HttpCode.FAILURE, false);

    /**
     * 成功结果，使用http code
     */
    public static <T> R<T> success() {
        return new R<>(HttpCode.SUCCESS, true);
    }

    /**
     * 成功结果-携带数据，使用http code
     *
     * @param data 数据
     */
    public static <T> R<T> success(T data) {
        return new R<>(HttpCode.SUCCESS, true, data);
    }


    /**
     * 成功结果-携带数据，使用http code
     *
     * @param msg  成功信息
     * @param data 数据
     */
    public static <T> R<T> success(String msg, T data) {
        return new R<>(HttpCode.SUCCESS.code, true, msg, data);
    }


    /**
     * 成功结果
     *
     * @param resultCode 成功结果编码
     */
    public static <T> R<T> success(IResultCode resultCode) {
        return new R<>(resultCode, true);
    }

    /**
     * 成功结果-携带数据
     *
     * @param data       数据
     * @param resultCode 成功结果编码
     */
    public static <T> R<T> success(IResultCode resultCode, T data) {
        return new R<>(resultCode, true, data);
    }

    /**
     * 成功结果-携带数据
     *
     * @param msg  成功信息
     * @param data 数据
     * @param code 成功结果编码
     */
    public static <T> R<T> success(int code, String msg, T data) {
        return new R<>(code, true, msg, data);
    }

    /**
     * 失败结果，使用http code
     *
     * @param msg 失败信息
     */
    public static <T> R<T> fail(String msg) {
        return new R<T>(HttpCode.FAILURE.code, false, msg, null);
    }

    /**
     * 失败结果，使用http code
     *
     * @param msg  失败信息
     * @param data 数据
     */
    public static <T> R<T> fail(String msg, T data) {
        return new R<T>(HttpCode.FAILURE.code, false, msg, data);
    }

    /**
     * 失败结果，使用http code
     *
     * @param resultCode 失败结果编码
     */
    public static <T> R<T> fail(IResultCode resultCode) {
        return new R<>(resultCode, false);
    }

    /**
     * 返回失败信息
     *
     * @param code 失败结果编码
     * @param msg  失败信息
     */
    public static <T> R<T> fail(int code, String msg) {
        return new R<T>(code, false, msg, null);
    }

    /**
     * 返回失败信息
     *
     * @param code 失败结果编码
     * @param msg  失败信息
     * @param data 数据
     */
    public static <T> R<T> fail(int code, String msg, T data) {
        return new R<T>(code, false, msg, data);
    }
}
