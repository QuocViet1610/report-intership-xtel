package com.example.project1.utils;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.model.Enum.LanguageEnum;
import com.example.project1.model.enity.User.User;
import com.example.project1.model.enity.User.UserRole;
import com.example.project1.module.User.repository.UserRepository;
import com.example.project1.module.User.repository.UserRoleRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenUtil {
    private final HttpServletRequest httpServletRequest;
    UserRepository userRepository;
    UserRoleRepository userRoleRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;


    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("sport-ecommerce.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user)) // phan quyen
                .claim("userId", user.getId())
                .claim("userName", user.getFullName())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        List<UserRole> userRoles =  userRoleRepository.findAllByUserId(user.getId());


        if (!CollectionUtils.isEmpty(userRoles))
            userRoles.forEach(userRole -> {
                stringJoiner.add("ROLE_" + userRole.getRole().getName());
//              stringJoiner.add(role.getPermissions().stream().map(Permission::getName));
//                role.getPermissions().forEach(permission -> {
//                    stringJoiner.add(permission.getName());
//                });
            });

        return stringJoiner.toString();
    }

    private SecretKey getSignInKey() {
        byte[] decodedKey;
        try {
            decodedKey = Base64.getDecoder().decode(SIGNER_KEY);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid base64 encoding for secret key", e);
        }
        // Sử dụng HMAC SHA để tạo Key từ byte[] đã giải mã
        return new SecretKeySpec(decodedKey, "HmacSHA256"); // Hoặc HmacSHA512, tùy vào yêu cầu
    }

    public Jwt extractAllClaims(String token) {
        // Tạo JwtDecoder từ khóa bí mật (HMAC) de gia ma token
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSignInKey()).build();

        // Giải mã và trả về Jwt object chứa các claim
        return jwtDecoder.decode(token);
    }

    public <T> T extractClaim(String token, Function<Jwt, T> claimsResolver) {
        final Jwt claims = this.extractAllClaims(token); // lay doi tuong Claim

        return claimsResolver.apply(claims);
    }

    public String extractUserName(String token){
        return extractClaim(token, s -> s.getSubject());
    }

    public boolean isTokenExpired(String token) {
        Instant expirationInstant = this.extractClaim(token, Jwt::getExpiresAt);
        Date expirationDate = Date.from(expirationInstant);
        return expirationDate.before(new Date());
    }
    public Boolean validateToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expriryTime;
        try{
            expriryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        }catch (Exception e){
            return false;
        }

        boolean verified = signedJWT.verify(verifier) && expriryTime.after(new Date());
        return verified;
    }

    public String getLanguage(){
        String requestLangue = httpServletRequest.getHeader("Accept-language");
        if(requestLangue == null || requestLangue.isEmpty()){
            requestLangue = LanguageEnum.vi.name();
        }
        return requestLangue;
    }

    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    public static String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof Jwt) {
            return ((Jwt) principal).getClaim("sub");
        } else {
            return principal.toString();
        }
    }

    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Jwt jwt) {
            return jwt.getClaim("userId");
        }

        throw new ValidateException(Translator.toMessage("Người dùng không tồn tại "));
    }
}
