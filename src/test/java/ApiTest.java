import com.alibaba.fastjson.JSON;
import com.brazil.irs.encrypt.AesGcmEncryption;
import com.brazil.irs.encrypt.AuthenticatedEncryptionException;
import com.brazil.irs.encrypt.RSAEncryption;
import com.brazil.irs.params.Params;
import com.brazil.irs.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;

@Slf4j
public class ApiTest {


    private static final String pri_key = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEA0FaRrHodVaztvsPoB2W4AvbNzqZp9abdrUKKq8WGO5DYTewmtS2Lq3LG9SMjqvbMC5NKRyTQgnhlG+kUgM6okwIDAQABAkEAhCEjKlwYFqoh7gaZNXfRiC+2vwTT1rQJi0TUZbRUOZmyZPUXBKeaS1aSRaISZNg3jP873xbb/fDlud/6JqNngQIhAOgZ5UNJk13i9+C5ukizdb9n/4nvwPxZ/fU5glNmWHGRAiEA5cpKEqNdFhbRyHg8SZFR0YDfHBL7TQVXQgS1tvx4JeMCIQC4k89k4WAWa+cgidMA+eaL3xKzCT158b8qgx+HX366YQIhAOVfeXUFUMPd32XmbvdYcX1Y0FxrriGBtUoW/XQ8q4nVAiAxkPXpVEkdz9KK+488lJrwblB9mf6cOIsY48s2U7VpVA==";

    private static final String url = "https://brazil-irs-datatransfer-fat-2.pundix.com";
    private static final String rsaPubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANBWkax6HVWs7b7D6AdluAL2zc6mafWm3a1CiqvFhjuQ2E3sJrUti6tyxvUjI6r2zAuTSkck0IJ4ZRvpFIDOqJMCAwEAAQ==";


    private static final String aesKey = "bcJ17yKisbOOBWlPjaDZKA==";

    @Test
    public void createAesKey() throws NoSuchAlgorithmException {
        byte[] aesKeyBytes = getAesKeyBytes();
        String aeskey = Base64.getEncoder().encodeToString(aesKeyBytes);
        System.out.println(aeskey);
    }




    @Test
    public void testAuth() throws Exception {

        Params.AuthInput authInput = new Params.AuthInput();
        authInput.setAesSecret(aesKey);
        String json = JSON.toJSON(authInput).toString();
        byte[] resp = HttpUtils.postJSON(url + "/auth", rsaEncrypt(json), new HashMap<>());

        try {
            byte[] bytes = aesDecrypt(Base64.getDecoder().decode(aesKey), resp);
            System.out.println("<==auth success, resp:" +  new String(bytes));
        } catch (Exception e) {
            System.out.println("<== aesDecrypt error");
            System.out.println("<==auth resp:" + new String(resp));
        }
    }


    @Test
    public void testApi() throws Exception {
        Params.CommonInput commonInput = new Params.CommonInput();
        commonInput.setStartDate("2018-01-01");
        commonInput.setEndDate("2021-01-01");
        byte[] aesKeyBytes = Base64.getDecoder().decode(aesKey);

        byte[] resp = HttpUtils.postJSON(url + "/xwallet/api/clients", aesEncrypt(aesKeyBytes, JSON.toJSONString(commonInput).getBytes()), new HashMap<>());
        try {
            byte[] bytes = aesDecrypt(aesKeyBytes, resp);
            System.out.println(new String(bytes));
        } catch (Exception e) {
            /**
             * decryptError
             */
            System.out.println("<==aes key error :" + new String(resp));
        }


    }





    public static byte[] rsaEncrypt(String json) throws GeneralSecurityException {
        RSAPublicKey rsaPublicKey = RSAEncryption.generatePublicKey(Base64.getDecoder().decode(rsaPubKey));
        RSAEncryption rsaEncryption = new RSAEncryption(rsaPublicKey, null);
        return rsaEncryption.encrypt(json.getBytes(StandardCharsets.UTF_8));
    }


    public static byte[] rsaDecrypt(String reqStr) throws GeneralSecurityException {
        System.out.println(reqStr);
        RSAPrivateKey rsaPrivateKey = RSAEncryption.generatePrivateKey(Base64.getDecoder().decode(pri_key));
        RSAEncryption rsaEncryption = new RSAEncryption(null, rsaPrivateKey);
        return rsaEncryption.decrypt(reqStr.getBytes(StandardCharsets.UTF_8));
    }


    public static byte[] aesDecrypt(byte[] aesKey, byte[] resp) throws AuthenticatedEncryptionException {
        AesGcmEncryption aesGcmEncryption = new AesGcmEncryption();
        byte[] decrypt = aesGcmEncryption.decrypt(aesKey, resp, null);
        return decrypt;
    }


    public static byte[] aesEncrypt(byte[] aesKey, byte[] resp) throws AuthenticatedEncryptionException {
        AesGcmEncryption aesGcmEncryption = new AesGcmEncryption();
        byte[] decrypt = aesGcmEncryption.encrypt(aesKey, resp, null);
        return decrypt;
    }



    public static byte[] getAesKeyBytes() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        //要生成多少位，只需要修改这里即可128, 192或256
        SecretKey sk = kg.generateKey();
        return sk.getEncoded();
    }




}
