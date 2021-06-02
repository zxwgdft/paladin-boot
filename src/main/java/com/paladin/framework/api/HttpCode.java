package com.paladin.framework.api;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@ApiModel(description = "系统内置code")
public enum HttpCode implements IResultCode {

    // 常用code

    SUCCESS(200, "操作成功"),

    BAD_REQUEST(400, "请求异常"),

    FAILURE(500, "操作失败"),

    NOT_FOUND(404, "404，未找到请求资源"),

    UNAUTHORIZED(401, "请求身份未验证"),

    FORBIDDEN(403, "没有访问或操作权限"),

    // 自定义code
    REQUEST_VALID_ERROR(490, "请求参数验证未通过"),


    // 请求成功，扩展code
    CREATED(201, "Created"),

    ACCEPTED(202, "Accepted"),

    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),

    NO_CONTENT(204, "No Content"),

    RESET_CONTENT(205, "Reset Content"),

    PARTIAL_CONTENT(206, "Partial Content"),

    MULTI_STATUS(207, "Multi-Status"),

    ALREADY_REPORTED(208, "Already Reported"),

    IM_USED(226, "IM Used"),

    // 请求成功，但是需要作出相应处理
    MULTIPLE_CHOICES(300, "Multiple Choices"),

    MOVED_PERMANENTLY(301, "Moved Permanently"),

    FOUND(302, "Found"),

    SEE_OTHER(303, "See Other"),

    NOT_MODIFIED(304, "Not Modified"),

    TEMPORARY_REDIRECT(307, "Temporary Redirect"),

    PERMANENT_REDIRECT(308, "Permanent Redirect"),

    // 客户端请求问题扩展code

    PAYMENT_REQUIRED(402, "Payment Required"),

    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    NOT_ACCEPTABLE(406, "Not Acceptable"),

    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),

    REQUEST_TIMEOUT(408, "Request Timeout"),

    CONFLICT(409, "Conflict"),

    GONE(410, "Gone"),

    LENGTH_REQUIRED(411, "Length Required"),

    PRECONDITION_FAILED(412, "Precondition Failed"),

    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),

    URI_TOO_LONG(414, "URI Too Long"),

    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),

    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"),

    EXPECTATION_FAILED(417, "Expectation Failed"),

    I_AM_A_TEAPOT(418, "I'm a teapot"),

    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),

    LOCKED(423, "Locked"),

    FAILED_DEPENDENCY(424, "Failed Dependency"),

    TOO_EARLY(425, "Too Early"),

    UPGRADE_REQUIRED(426, "Upgrade Required"),

    PRECONDITION_REQUIRED(428, "Precondition Required"),

    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),

    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),

    // 服务器异常

    NOT_IMPLEMENTED(501, "Not Implemented"),

    BAD_GATEWAY(502, "Bad Gateway"),

    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    GATEWAY_TIMEOUT(504, "Gateway Timeout"),

    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"),

    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),

    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),

    LOOP_DETECTED(508, "Loop Detected"),

    BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded"),

    NOT_EXTENDED(510, "Not Extended"),

    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");


    final int code;

    final String message;
}
