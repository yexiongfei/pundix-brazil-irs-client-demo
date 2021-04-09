package com.brazil.irs.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
public class HttpUtils {

    //private EncryptInterceptor encryptInterceptor;

//    private OkHttpClient okHttpClientTimeOut;
//
//    public HttpUtils(EncryptInterceptor encryptInterceptor) {
//        //this.encryptInterceptor = encryptInterceptor;
//        this.okHttpClientTimeOut = new OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .addInterceptor(encryptInterceptor)
//                .build();
//    }


    private static OkHttpClient okHttpClientTimeOut = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            //.addInterceptor(encryptInterceptor)
            .build();


    public static  byte[] postJSON(String url, byte[] params, Map<String, String> headers) throws Exception {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(Headers.of(headers))
                .build();
        Response response = okHttpClientTimeOut.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new RuntimeException("<==!response.isSuccessful");
        }
        ResponseBody body = response.body();
        if (null == body) {
            log.warn("<==ResponseBody is null");
            return null;
        }
        return body.bytes();


    }
}
