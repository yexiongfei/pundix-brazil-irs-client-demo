import com.alibaba.fastjson.JSON;
import com.brazil.irs.encrypt.AesGcmEncryption;
import com.brazil.irs.encrypt.AuthenticatedEncryptionException;
import com.brazil.irs.params.Params;
import com.brazil.irs.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ApiTest {


    //private static final String pri_key = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEA0FaRrHodVaztvsPoB2W4AvbNzqZp9abdrUKKq8WGO5DYTewmtS2Lq3LG9SMjqvbMC5NKRyTQgnhlG+kUgM6okwIDAQABAkEAhCEjKlwYFqoh7gaZNXfRiC+2vwTT1rQJi0TUZbRUOZmyZPUXBKeaS1aSRaISZNg3jP873xbb/fDlud/6JqNngQIhAOgZ5UNJk13i9+C5ukizdb9n/4nvwPxZ/fU5glNmWHGRAiEA5cpKEqNdFhbRyHg8SZFR0YDfHBL7TQVXQgS1tvx4JeMCIQC4k89k4WAWa+cgidMA+eaL3xKzCT158b8qgx+HX366YQIhAOVfeXUFUMPd32XmbvdYcX1Y0FxrriGBtUoW/XQ8q4nVAiAxkPXpVEkdz9KK+488lJrwblB9mf6cOIsY48s2U7VpVA==";

    /**
     * xwallet
     */
    private static final String url = "https://brazil-irs-datatransfer-fat-2.pundix.com/xwallet";
    /**
     * merchant
     */
    private static final String url2 = "https://brazil-irs-datatransfer-fat-2.pundix.com/merchant";
    //private static final String url = "http://127.0.0.1:4112/xwallet";
    //private static final String rsaPubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANBWkax6HVWs7b7D6AdluAL2zc6mafWm3a1CiqvFhjuQ2E3sJrUti6tyxvUjI6r2zAuTSkck0IJ4ZRvpFIDOqJMCAwEAAQ==";


    private static final String aesSecret = "HLA+pcYZc2dl4QnN6erPxA==";
    private static final String authentication = "f5952595f9264856b8bde8e910032d4c";

    private static final Map<String, String> headers = new HashMap<>();
    static {
        headers.put("authentication", authentication);
    }



    @Test
    public void testApi() throws Exception {
        Params.CommonInput commonInput = new Params.CommonInput();
        commonInput.setStartDate("2018-01-01");
        commonInput.setEndDate("2021-01-01");
        byte[] aesKeyBytes = Base64.getDecoder().decode(aesSecret);

        byte[] resp = HttpUtils.postJSON(url + "/api/clients", aesEncrypt(aesKeyBytes, JSON.toJSONString(commonInput).getBytes()), headers);
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
