package com.paladin.framework.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TontoZhou
 * @since 2019/12/31
 */
public class CommonDocketFactory {

    public static Docket newInstance() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();

        ObjectVendorExtension errorResponse = new ObjectVendorExtension("ErrorResponse");
        errorResponse.addProperty(new StringVendorExtension("code", "错误编码"));
        errorResponse.addProperty(new StringVendorExtension("message", "错误信息"));
        errorResponse.addProperty(new StringVendorExtension("data", "错误相关数据"));

        List<VendorExtension> extensions = new ArrayList<>();
        extensions.add(errorResponse);

        responseMessageList.add(new ResponseMessageBuilder().code(400).message("请求异常").vendorExtensions(extensions).build());
        responseMessageList.add(new ResponseMessageBuilder().code(404).message("未找到请求资源").vendorExtensions(extensions).build());
        responseMessageList.add(new ResponseMessageBuilder().code(401).message("请求身份未验证").vendorExtensions(extensions).build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("操作失败").vendorExtensions(extensions).build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList);

    }

    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SERVICE API")
                .description("人员注册平台API")
                .termsOfServiceUrl("")
                .contact(new Contact("TontoZhou", "https://github.com/zxwgdft", "823498927@qq.com"))
                .version("2.0")
                .build();
    }

}
