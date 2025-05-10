package com.example.project1.config;

import com.nimbusds.jose.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
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
              ) // Cấu hình OAuth2 Resource Server

                .csrf(AbstractHttpConfigurer::disable); // Tắt bảo  vệ CSRF

        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });

        return http.build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS256");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
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
