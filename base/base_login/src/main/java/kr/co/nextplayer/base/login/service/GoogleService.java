package kr.co.nextplayer.base.login.service;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import kr.co.nextplayer.base.login.config.GoogleAuthorization;
import kr.co.nextplayer.base.login.dto.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

@Slf4j
@Service
public class GoogleService {

    @Resource
    private GoogleAuthorization googleAuthorization;

    //authorizingUrl
    public String authorizingUrl() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        // Creating an authentication process object
        GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow
                .Builder(httpTransport, jsonFactory, googleAuthorization.getGoogleClientSecrets(), googleAuthorization.getScopes())
                //Refresh token is available only when accessType is offline.
                .setAccessType("offline").build();
        if (googleAuthorizationCodeFlow != null) {
            // Return skip login request
            return googleAuthorizationCodeFlow.newAuthorizationUrl().setRedirectUri(googleAuthorization.getRedirectUrl()).build();
        }
        return null;
    }

    //use auth code get access token
    public Auth authorizing(String authCode) throws GeneralSecurityException, IOException {

        String authorizationCode = java.net.URLDecoder.decode(authCode, StandardCharsets.UTF_8.name());

        // Create Request Credentials
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow
                .Builder(httpTransport, jsonFactory, googleAuthorization.getGoogleClientSecrets(), googleAuthorization.getScopes())
                // Refresh token is available only when accessType is offline.
                .setAccessType("offline").build();
        GoogleAuthorizationCodeTokenRequest tokenRequest = googleAuthorizationCodeFlow.newTokenRequest(authorizationCode);
        tokenRequest.setRedirectUri(googleAuthorization.getRedirectUrl());
        // request for authorization，get Token & Refresh Token
        GoogleTokenResponse tokenResponse = tokenRequest.execute();
        String token = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();

        log.info("===token=====>{}===refreshToken=====>{}" ,token , refreshToken);

        // 获得email
        String email = null;
        if (StringUtils.isNotBlank(tokenResponse.getIdToken())) {
            GoogleIdTokenVerifier idTokenVerifier = new GoogleIdTokenVerifier.Builder(googleAuthorizationCodeFlow.getTransport(), googleAuthorizationCodeFlow.getJsonFactory()).build();
            idTokenVerifier.verify(tokenResponse.getIdToken());
            GoogleIdToken googleIdToken = idTokenVerifier.verify(tokenResponse.getIdToken());
            if (googleIdToken != null && googleIdToken.getPayload() != null) {
                email = googleIdToken.getPayload().getEmail();
                log.info("===Google getPayload=====>{}" ,googleIdToken.getPayload());
            }
        }
        log.info("===email=====>" + email);
        // todo 保留账号token、refreshToken、email信息
        Auth auth = Auth.builder().build();
        auth.setAccess_token(token);
        auth.setRefresh_token(refreshToken);
//        auth.setEmail(email);
        return auth;
    }

    //Refresh Token
    public String refreshToken(String refreshToken) throws IOException {
        String token = null;
        // 创建刷新请求对象
        GoogleRefreshTokenRequest googleRefreshTokenRequest = new GoogleRefreshTokenRequest(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                refreshToken,
                googleAuthorization.getGoogleClientSecrets().getDetails().getClientId(),
                googleAuthorization.getGoogleClientSecrets().getDetails().getClientSecret());
        // 发起刷新请求
        GoogleTokenResponse googleTokenResponse = googleRefreshTokenRequest.execute();
        if (googleTokenResponse != null && StringUtils.isNotBlank(googleTokenResponse.getAccessToken())) {
            token = googleTokenResponse.getAccessToken();
        }
        return null;
    }

}