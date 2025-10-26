package kr.co.nextplayer.base.login.oauth.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.co.nextplayer.base.login.oauth.utils.UrlBuilder;
import kr.co.nextplayer.base.login.util.HttpClientUtil;
import kr.co.nextplayer.base.login.oauth.cache.AuthStateCache;
import kr.co.nextplayer.base.login.oauth.conf.AuthConfig;
import kr.co.nextplayer.base.login.oauth.conf.AuthDefaultSource;
import kr.co.nextplayer.base.login.oauth.conf.AuthSource;
import kr.co.nextplayer.base.login.oauth.model.AuthCallback;
import kr.co.nextplayer.base.login.oauth.model.AuthToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class AuthAppleRequest extends AuthDefaultRequest {

    private static boolean CHECK_SSO = true;
    private static boolean UNCHECK_SSO = false;

    public AuthAppleRequest(AuthConfig config) {
        super(config, AuthDefaultSource.APPLE);
    }

    public AuthAppleRequest(AuthConfig config, AuthSource source) {
        super(config, source, UNCHECK_SSO);
    }

    public AuthAppleRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.APPLE, authStateCache, CHECK_SSO);
    }

    public Map<String, Object> getFirstRequestToken(AuthCallback authCallback) throws Exception {

        String url = UrlBuilder.fromBaseUrl(source.accessToken()).build();
        Map<String, String> param = new HashMap<>();
        param.put("grant_type", "authorization_code");
        param.put("code", authCallback.getCode());
        param.put("redirect_uri", config.getRedirectUri());
        param.put("client_id", config.getClientId());
        //jwt secret
        String client_secret = this.getClientSecret(authCallback);
        param.put("client_secret", client_secret);

        log.warn("url:*******" + url + "*******");
        String response = null;

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> dataMap = null;
        response = HttpClientUtil.doPost(url, param);
        try {
            dataMap = mapper.readValue(response, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.warn("response:*******" + response + "*******");
        log.warn("dataMap: " + dataMap);


        //UserInfo

        return dataMap;
    }

    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(source.authorize())
            .queryParam("response_type", "code")
            .queryParam("response_mode", "form_post")
            .queryParam("client_id", config.getClientId())
            .queryParam("redirect_uri", config.getRedirectUri())
            .queryParam("state", getRealState(state))
            .queryParam("scope", "email")
            .build();
    }

    public String getClientSecret(AuthCallback authCallback) throws Exception {

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setHeaderParam("kid", authCallback.getKeyId());
        jwtBuilder.setIssuer(authCallback.getTeamId());
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.setExpiration(DateUtils.addMinutes(new Date(), 30));
        jwtBuilder.setAudience("https://appleid.apple.com");
        jwtBuilder.setSubject(authCallback.getClientId());

        PrivateKey privateKey = getPrivateKey();

        jwtBuilder.signWith(SignatureAlgorithm.ES256, privateKey);
        String secretCode = jwtBuilder.compact();

        return secretCode;
    }

    public PrivateKey getPrivateKey() throws IOException{
        // appleKeyId에 담겨있는 정보 가져오기
        Resource resources = ResourcePatternUtils
            .getResourcePatternResolver(new DefaultResourceLoader())
            .getResource("classpath:"+"config/cert/AuthKey_2PCX8M3G7P.p8");

        InputStream inputStream = resources.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String readLine = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((readLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(readLine);
            stringBuilder.append("\n");
        }
        String keyPath = stringBuilder.toString();

        // privateKey 생성하기
        Reader reader = new StringReader(keyPath);
        PEMParser pemParser = new PEMParser(reader);
        JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
        PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();
        PrivateKey privateKey = jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);

        return privateKey;
    }
    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        return null;
    }


}
