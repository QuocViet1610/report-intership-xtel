package com.example.project1.module.auth;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.User.UserDto;
import com.example.project1.model.dto.respone.AuthenticationResponse;
import com.example.project1.module.User.service.serviceImpl.AuthenticationServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuthService implements IAuthService{
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${spring.security.oauth2.client.registration.google.user-info-uri}")
    private String googleUserInfoUri;

//    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}")
//    private String facebookRedirectUri;
//
//    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
//    private String facebookClientId;
//
//    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
//    private String facebookClientSecret;
//
//    @Value("${spring.security.oauth2.client.registration.facebook.auth-uri}")
//    private String facebookAuthUri;
//
//    @Value("${spring.security.oauth2.client.registration.facebook.token-uri}")
//    private String facebookTokenUri;
//
//    @Value("${spring.security.oauth2.client.registration.facebook.user-info-uri}")
//    private String facebookUserInfoUri;

    AuthenticationServiceImpl authenticationService;

    public String generateAuthUrl(String loginType) {
        String url = "";
        loginType = loginType.trim().toLowerCase(); // Normalize the login type

        if ("google".equals(loginType)) {
            GoogleAuthorizationCodeRequestUrl urlBuilder = new GoogleAuthorizationCodeRequestUrl(
                    googleClientId,
                    googleRedirectUri,
                    Arrays.asList("email", "profile", "openid"));
            url = urlBuilder.build();
        } else if ("facebook".equals(loginType)) {
            /*
            url = String.format("https://www.facebook.com/v3.2/dialog/oauth?client_id=%s&redirect_uri=%s&scope=email,public_profile&response_type=code",
                    facebookClientId, facebookRedirectUri);
             */
//            url = UriComponentsBuilder
//                    .fromUriString(facebookAuthUri)
//                    .queryParam("client_id", facebookClientId)
//                    .queryParam("redirect_uri", facebookRedirectUri)
//                    .queryParam("scope", "email,public_profile")
//                    .queryParam("response_type", "code")
//                    .build()
//                    .toUriString();
        }

        return url;
    }

    // gui requestlen gg
    public Map<String, Object> authenticateAndFetchProfile(String code, String loginType) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        String accessToken;
        //Gson gson = new Gson();

        switch (loginType.toLowerCase()) {
            case "google":
                accessToken = new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(), new GsonFactory(),
                        googleClientId,
                        googleClientSecret,
                        code,
                        googleRedirectUri
                ).execute().getAccessToken();

                // Configure RestTemplate to include the access token in the Authorization header
                restTemplate.getInterceptors().add((req, body, executionContext) -> {
                    req.getHeaders().set("Authorization", "Bearer " + accessToken);
                    return executionContext.execute(req, body);
                });

                // Make a GET request to fetch user information
                return new ObjectMapper().readValue(
                    restTemplate.getForEntity(googleUserInfoUri, String.class).getBody(),
                    new TypeReference<>() {});
                //break;

            case "facebook":
                // Facebook token request setup
//                String urlGetAccessToken = UriComponentsBuilder
//                        .fromUriString(facebookTokenUri)
//                        .queryParam("client_id", facebookClientId)
//                        .queryParam("redirect_uri", facebookRedirectUri)
//                        .queryParam("client_secret", facebookClientSecret)
//                        .queryParam("code", code)
//                        .toUriString();
//
//                // Use RestTemplate to fetch the Facebook access token
//                ResponseEntity<String> response = restTemplate.getForEntity(urlGetAccessToken, String.class);
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode node = mapper.readTree(response.getBody());
//                accessToken = node.get("access_token").asText();
//
//                // Set the URL for the Facebook API to fetch user info
//                // Lấy thông tin người dùng
//                String userInfoUri = facebookUserInfoUri + "&access_token=" + accessToken;
//                return mapper.readValue(
//                        restTemplate.getForEntity(userInfoUri, String.class).getBody(),
//                        new TypeReference<>() {});
                //break;

            default:
                System.out.println("Unsupported login type: " + loginType);
                return null;
        }
    }

    public ResponseResult<AuthenticationResponse> loginSocial(
            @Valid @RequestBody UserDto userLoginDTO,
            HttpServletRequest request
    ) {
        // Gọi hàm loginSocial từ UserService cho đăng nhập mạng xã hội
        String token = authenticationService.loginSocial(userLoginDTO);
        var result = AuthenticationResponse.builder().token(token).authenticated(true).build();
        return ResponseResult.ofSuccess(result);
    }


    public UserDto processSocialLogin(String code, String loginType) throws ValidateException, IOException {
        Map<String, Object> userInfo = authenticateAndFetchProfile(code, loginType);

        if (userInfo == null) {
            throw new ValidateException(Translator.toMessage("Đăng nhập thất bại"));
        }

        String accountId = "";
        String name = "";
        String picture = "";
        String email = "";

        if ("google".equalsIgnoreCase(loginType.trim())) {
            accountId = (String) Objects.requireNonNullElse(userInfo.get("sub"), "");
            name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
            picture = (String) Objects.requireNonNullElse(userInfo.get("picture"), "");
            email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
        } else if ("facebook".equalsIgnoreCase(loginType.trim())) {
            accountId = (String) Objects.requireNonNullElse(userInfo.get("id"), "");
            name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
            email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
            Object pictureObj = userInfo.get("picture");
            if (pictureObj instanceof Map) {
                Map<?, ?> pictureData = (Map<?, ?>) pictureObj;
                Object dataObj = pictureData.get("data");
                if (dataObj instanceof Map) {
                    Map<?, ?> dataMap = (Map<?, ?>) dataObj;
                    Object urlObj = dataMap.get("url");
                    if (urlObj instanceof String) {
                        picture = (String) urlObj;
                    }
                }
            }
        } else {
            throw new ValidateException("Login type không hợp lệ");
        }

        UserDto userLoginDTO = new UserDto(
                null,
                null,
                null,
                name,
                email,
                "",
                "",
                "",
                picture
        );

        if ("google".equalsIgnoreCase(loginType.trim())) {
            userLoginDTO.setGoogleAccountId(accountId);
        } else if ("facebook".equalsIgnoreCase(loginType.trim())) {
            userLoginDTO.setFacebookAccountId(accountId);
        }

        return userLoginDTO;

    }

}
