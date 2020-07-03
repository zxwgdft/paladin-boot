package com.paladin.common.config;

import okhttp3.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 推荐使用OkHttpClient，如果直接用OkHttpClient发送请求，API更简单，但是需要记得关闭response、异常处理等操作
 * <p>
 * 使用RestTemplate封装后，可以更省心使用
 * <p>
 * OkHttpClient与HttpClient相比最主要的就是API更简单，并且默认设置更合理，性能相差不大，
 * 在运用得当的情况下HttpClient更快，但也更容易出错
 *
 *
 * @author TontoZhou
 * @since 2020/3/26
 */
@Configuration
public class HttpClientConfiguration {


    @Bean("restTemplate")
    public RestTemplate okHttp3RestTemplate(RestTemplateBuilder builder, OkHttpClient okHttpClient) {
        RestTemplate restTemplate = builder.build();
        OkHttp3ClientHttpRequestFactory clientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        return builder.build();
    }


    public static void main(String[] args) {
        // OkHttpClient 使用样例代码

        OkHttpClient client = new OkHttpClient();

        //创建一个Request
        Request request = new Request.Builder()
                .get()
                .url("https://www.baidu.com")
                .build();
        // 异步
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println(response.body().string());
                }
            }
        });

        // 同步
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
