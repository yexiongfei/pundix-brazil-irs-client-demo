package com.brazil.irs.utils;

import com.brazil.irs.encrypt.AesGcmEncryption;
import com.brazil.irs.encrypt.RSAEncryption;
import lombok.Data;
import lombok.SneakyThrows;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Data
public class EncryptInterceptor implements Interceptor {

    private String publicKey;
    private RSAEncryption rsaEncryption;
    private byte[] aesBytes;
    private final AesGcmEncryption aesGcmEncryption = new AesGcmEncryption();

    public EncryptInterceptor(String publicKey, byte[] aesBytes) throws GeneralSecurityException {
        this.publicKey = publicKey;
        RSAPublicKey rsaPublicKey = RSAEncryption.generatePublicKey(Base64.getDecoder().decode(publicKey));
        this.rsaEncryption = new RSAEncryption(rsaPublicKey, null);
        this.aesBytes = aesBytes;
    }



    @SneakyThrows
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        final String method = request.method();

        RequestBody oldRequestBody = request.body();


        Buffer requestBuffer = new Buffer();
        oldRequestBody.writeTo(requestBuffer);


        String oldBodyStr = requestBuffer.readUtf8();
        requestBuffer.close();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");


        byte[] encrypt;
        if(method.equals("auth")) {
            encrypt = rsaEncryption.encrypt(oldBodyStr.getBytes());
        } else {
            encrypt = aesGcmEncryption.encrypt(aesBytes, oldBodyStr.getBytes(), null);
        }

        String newBodyStr = new String(encrypt);
        RequestBody newBody = RequestBody.create(mediaType, newBodyStr);

        request = request.newBuilder()
                .header("Content-Type", newBody.contentType().toString())
                .header("Content-Length", String.valueOf(newBody.contentLength()))
                .method(request.method(), newBody)
                // .header("appEncryptedKey", appEncryptedKey)
                //.header("appSignature", appSignature)
                //.header("appPublicKey", appPublicKeyStr)
                .build();
        //响应
        Response response = chain.proceed(request);
        if (response.code() == 200) {
            ResponseBody oldResponseBody = response.body();
            String oldResponseBodyStr = oldResponseBody.string();
            try {
                byte[] decrypt = aesGcmEncryption.decrypt(aesBytes, oldResponseBodyStr.getBytes(), null);
                ResponseBody responseBody = ResponseBody.create(mediaType, decrypt);
                Response res = chain.proceed(request);
                return res.newBuilder().body(responseBody).build();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return response;
    }
}
