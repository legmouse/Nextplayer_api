package kr.co.nextplayer.base.login.util;

import com.auth0.jwk.Jwk;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.security.PublicKey;
import java.util.Map;

@Slf4j
public class AppleUtil {

    /**
     * 获取苹果的公钥
     * @return
     * @throws Exception
     */
    private static JsonArray getAuthKeys() throws Exception {
        String keys = HttpClientUtil.doGet("https://appleid.apple.com/auth/keys");
        log.warn("response: " + keys);
        JsonObject jsonObject = JsonParser.parseString(keys).getAsJsonObject();
        JsonArray arr = jsonObject.get("keys").getAsJsonArray();
        return arr;
    }

    public static Boolean verify(String jwt) throws  Exception{
        JsonArray arr = getAuthKeys();
        if(arr == null){
            return false;
        }
        JsonObject authKey = null;

        //先取苹果第一个key进行校验
        authKey = arr.get(0).getAsJsonObject();
        //authKey = JsonObject.parseObject(arr.getString(0));
        if(verifyExc(jwt, authKey)){
            return true;
        }else{
            //再取第二个key校验
            authKey = arr.get(1).getAsJsonObject();
            //authKey = JsonObject.parseObject(arr.getString(1));
            return verifyExc(jwt, authKey);
        }

    }

    /**
     * 对前端传来的identityToken进行验证
     * @param jwt 对应前端传来的 identityToken
     * @param authKey 苹果的公钥 authKey
     * @return
     * @throws Exception
     */
    public static Boolean verifyExc(String jwt, JsonObject authKey) throws Exception {

        Map<String, Object> map = new Gson().fromJson(authKey,Map.class);
        Jwk jwa = Jwk.fromValues(map);
        PublicKey publicKey = jwa.getPublicKey();

        String aud = "";
        String sub = "";
        if (jwt.split("\\.").length > 1) {
            String claim = new String(Base64.decodeBase64(jwt.split("\\.")[1]));
            aud = JsonParser.parseString(claim).getAsJsonObject().get("aud").toString();
            sub = JsonParser.parseString(claim).getAsJsonObject().get("sub").toString();
        }
        JwtParser jwtParser = Jwts.parser().setSigningKey(publicKey);
        jwtParser.requireIssuer("https://appleid.apple.com");
        jwtParser.requireAudience(aud);
        jwtParser.requireSubject(sub);

        try {
            Jws<Claims> claim = jwtParser.parseClaimsJws(jwt);
            if (claim != null && claim.getBody().containsKey("auth_time")) {
                System.out.println(claim);
                return true;
            }
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }



    /**
     * 对前端传来的JWT字符串identityToken的第二部分进行解码
     * 主要获取其中的aud和sub，aud大概对应ios前端的包名，sub大概对应当前用户的授权的openID
     * @param identityToken
     * @return  {"aud":"com.xkj.****","sub":"000***.8da764d3f9e34d2183e8da08a1057***.0***","c_hash":"UsKAuEoI-****","email_verified":"true","auth_time":1574673481,"iss":"https://appleid.apple.com","exp":1574674081,"iat":1574673481,"email":"****@qq.com"}
     */
    public static JsonObject parserIdentityToken(String identityToken){
        Gson gson = new Gson();
        String[] arr = identityToken.split("\\.");
        Base64 base64 = new Base64();
        String decode = new String (base64.decodeBase64(arr[1]));
        String substring = decode.substring(0, decode.indexOf("}")+1);
        JsonObject jsonObject = JsonParser.parseString(substring).getAsJsonObject();
        //JsonObject jsonObject = Json.parseObject(substring);
        return  jsonObject;
    }

}
