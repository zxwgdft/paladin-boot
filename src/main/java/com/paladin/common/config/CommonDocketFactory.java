package com.paladin.common.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TontoZhou
 * @since 2019/12/31
 */
@Component
public class CommonDocketFactory {

    public static Docket newInstance() {
        List<Response> responseMessageList = new ArrayList<>();

        ObjectVendorExtension errorResponse = new ObjectVendorExtension("ErrorResponse");
        errorResponse.addProperty(new StringVendorExtension("code", "错误编码"));
        errorResponse.addProperty(new StringVendorExtension("message", "错误信息"));
        errorResponse.addProperty(new StringVendorExtension("data", "错误相关数据"));

        List<VendorExtension> extensions = new ArrayList<>();
        extensions.add(errorResponse);

        responseMessageList.add(new ResponseBuilder().code("400").description("请求异常").vendorExtensions(extensions).build());
        responseMessageList.add(new ResponseBuilder().code("404").description("未找到请求资源").vendorExtensions(extensions).build());
        responseMessageList.add(new ResponseBuilder().code("401").description("请求身份未验证").vendorExtensions(extensions).build());
        responseMessageList.add(new ResponseBuilder().code("500").description("操作失败").vendorExtensions(extensions).build());

        return new Docket(DocumentationType.OAS_30)
                .pathMapping("/")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, responseMessageList)
                .globalResponses(HttpMethod.POST, responseMessageList);

    }

    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SERVICE API")
                .termsOfServiceUrl("")
                .contact(new Contact("TontoZhou", "https://github.com/zxwgdft", "823498927@qq.com"))
                .version("1.0")
                .build();
    }

}
