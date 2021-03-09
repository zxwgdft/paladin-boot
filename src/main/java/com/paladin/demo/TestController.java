package com.paladin.demo;

import com.paladin.framework.exception.BusinessException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * @author TontoZhou
 * @since 2021/3/2
 */
@Controller
@RequestMapping("/demo")
public class TestController {


    @GetMapping(value = "/redirect")
    public String redirect2msms() {
        // OkHttpClient 使用样例代码
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody
                .Builder()
                .add("username", "szgxqwjs")
                .add("password", "szgxqwjs")

                .add("forward", "10206000")
                //.add("forward","10201001")
                .add("layout", "hide")
                .build();

        //创建一个Request
        Request request = new Request.Builder()
                .post(formBody)
                .url("http://114.217.149.253:19020/monitor/auth/redirect")
                .build();
        // 同步
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return "redirect:" + response.body().string();
            }
            throw new BusinessException("跳转污水系统异常！" + response.message());
        } catch (IOException e) {
            throw new BusinessException("跳转污水系统网络异常！");
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
