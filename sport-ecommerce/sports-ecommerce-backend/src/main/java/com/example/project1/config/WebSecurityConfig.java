package com.example.project1.config;

import com.example.project1.module.User.repository.InvalidatedTokenRepository;
import com.nimbusds.jose.util.Pair;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;

    @Value("${jwt.signerKey}")
    private String signerKey;

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    private final Pair<String, HttpMethod>[] PUBLIC_ENDPOINTS = new Pair[] {
//            Pair.of("/users", HttpMethod.GET),
//            Pair.of("/users", HttpMethod.POST),
            Pair.of("/auth/login", HttpMethod.POST),
            Pair.of("/auth/introspect", HttpMethod.POST),
            Pair.of("/auth/logout", HttpMethod.POST),
            Pair.of("/auth/refresh", HttpMethod.POST),
            Pair.of("/user/register", HttpMethod.POST),
            Pair.of("/auth/register", HttpMethod.POST),
            Pair.of("/auth/verify-otp", HttpMethod.POST),
            Pair.of("/api/v1/auth/login", HttpMethod.POST),
            Pair.of("/auth/send-otp", HttpMethod.POST),
            Pair.of("/category", HttpMethod.GET),
            Pair.of("/product/search", HttpMethod.POST),
            Pair.of("/product/*", HttpMethod.GET),
            Pair.of("/auth/validate-forget-password/{email}", HttpMethod.POST),
            Pair.of("/auth/verify-forget-password/{email}/{otp}", HttpMethod.POST),
            Pair.of("/auth/send-otp-forget-password/{email}", HttpMethod.POST),
            Pair.of("/auth/social-login", HttpMethod.GET),
            Pair.of("/auth/social/callback", HttpMethod.GET),
            Pair.of("/brand/search", HttpMethod.POST),
            Pair.of("/attribute/find-value/{id}", HttpMethod.GET),
            Pair.of("/category/{id}", HttpMethod.GET),
            Pair.of("/product-rating/{id}", HttpMethod.GET),
            Pair.of("/blog/search", HttpMethod.POST),
            Pair.of("/blog/get-detail/{id}", HttpMethod.GET),
            Pair.of("/api-docs", HttpMethod.GET),
            Pair.of("/api-docs/**", HttpMethod.GET),
            Pair.of("/swagger-resources", HttpMethod.GET),
            Pair.of("/swagger-resources/**", HttpMethod.GET),
            Pair.of("/configuration/ui", HttpMethod.GET),
            Pair.of("/configuration/security", HttpMethod.GET),
            Pair.of("/swagger-ui/**", HttpMethod.GET),
            Pair.of("/swagger-ui.html", HttpMethod.GET),
            Pair.of("/webjars/swagger-ui/**", HttpMethod.GET),
            Pair.of("/swagger-ui/index.html", HttpMethod.GET)
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> {
                    // Cho phép tất cả yêu cầu POST đến các PUBLIC_ENDPOINTS mà không cần xác thực
                    for (Pair<String, HttpMethod> endpoint : PUBLIC_ENDPOINTS) {
                        requests.requestMatchers(endpoint.getRight(), endpoint.getLeft())
                                .permitAll();  // Cho phép mà không cần xác thực
                    }

//                    requests.requestMatchers(HttpMethod.POST,"/users" ).hasAuthority("SCOPE_ADMIN");
                    // Các yêu cầu khác cần xác thực
                    requests.anyRequest().authenticated();
                })
              .oauth2ResourceServer(oauth2 ->
                      oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter()))
                      .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
              ) // Cấu hình OAuth2 Resource Server, khong can OncePerRequestFilter

                .csrf(AbstractHttpConfigurer::disable); // Tắt bảo  vệ CSRF

        http.cors(cors -> {
            CorsConfiguration configuration = new CorsConfiguration();

            // Thay vì "*", hãy chỉ định cụ thể origin của Swagger UI
            configuration.setAllowedOriginPatterns(List.of("*")); // Hoặc chỉ định cụ thể như "http://localhost:8080"

            // Cho phép tất cả HTTP methods
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));

            // Thêm các header cần thiết cho Swagger
            configuration.setAllowedHeaders(Arrays.asList(
                    "authorization",
                    "content-type",
                    "x-auth-token",
                    "accept",
                    "origin",
                    "x-requested-with",
                    "access-control-request-method",
                    "access-control-request-headers"
            ));

            // Expose headers
            configuration.setExposedHeaders(Arrays.asList("x-auth-token", "authorization"));

            // Quan trọng: Cho phép credentials
            configuration.setAllowCredentials(true);

            // Thời gian cache preflight request
            configuration.setMaxAge(3600L);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            cors.configurationSource(source);
        });


        return http.build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    public boolean isTokenInvalidated(String token) throws ParseException {
        // Parse token thành SignedJWT
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Lấy jti từ claims
        String jti = signedJWT.getJWTClaimsSet().getJWTID();

        // Kiểm tra jti có trong database chưa
        return invalidatedTokenRepository.existsById(jti);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS256");
        NimbusJwtDecoder delegate = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        return new JwtDecoder() { // custom lai de giai ma
            @Override
            public Jwt decode(String token) throws JwtException {
                SignedJWT signedJWT;
                try {
                    signedJWT = SignedJWT.parse(token);
                } catch (ParseException e) {
                    throw new RuntimeException("Invalid JWT token format", e);
                }

                String jti;
                try {
                    jti = signedJWT.getJWTClaimsSet().getJWTID();
                } catch (ParseException e) {
                    throw new RuntimeException("Failed to get JWT ID", e);
                }

                if (invalidatedTokenRepository.existsById(jti)) {
                    throw new JwtException("Token has been invalidated");
                }

                // Delegate giải mã token bình thường
                return delegate.decode(token);
            }
        };
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }


}
