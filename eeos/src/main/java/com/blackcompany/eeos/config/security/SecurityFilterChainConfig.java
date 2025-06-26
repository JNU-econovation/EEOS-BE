package com.blackcompany.eeos.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterChainConfig {

    private final AccessTokenFilter authFilter;
    private final OptionsFilter optionsFilter;
    private final DynamicCorsConfigurationSource corsConfigurationSource;
    private final AccessTokenEntryPoint accessTokenEntryPoint;
    private final UnknownEndpointFilter unknownEndpointFilter;

    @Bean
    @Order(1)
    // swagger
    SecurityFilterChain swagger(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((requests) -> {
            requests.requestMatchers("/api/docs/**", "/api/swagger-ui/**").permitAll();
        });

        httpSecurity.logout(AbstractHttpConfigurer::disable);
        httpSecurity.securityContext(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    // 인증 필요 없는 엔드포인트
    SecurityFilterChain nonAuthenticated(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((requests) -> {
            requests.requestMatchers("/api/auth/logout").permitAll()
                    .requestMatchers("/api/auth/login/additional-info").permitAll()
                    .requestMatchers("/api/auth/login/**").permitAll()
                    .requestMatchers("/api/guest/**").permitAll();
        });

        httpSecurity.logout(AbstractHttpConfigurer::disable);
        httpSecurity.securityContext(AbstractHttpConfigurer::disable);

        httpSecurity.cors(
                httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource));

        httpSecurity.addFilterAfter(optionsFilter, CorsFilter.class);

        return httpSecurity.build();
    }

    @Bean
    @Order(3)
    // 인증이 필요한 엔드포인트
    SecurityFilterChain authenticated(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((requests) -> {
            requests.requestMatchers("/api/comments/**").authenticated()
                    .requestMatchers("/api/attend/**").authenticated()
                    .requestMatchers("/api/target/**").authenticated()
                    .requestMatchers("/api/members/**").authenticated()
                    .requestMatchers("/api/teams/**").authenticated()
                    .requestMatchers("/api/team-building/**").authenticated()
                    .requestMatchers("/api/calendars/**").authenticated();
        }
        );

        commonConfiguration(httpSecurity);

        httpSecurity.cors(
                httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource));

        httpSecurity.addFilterAt(authFilter, LogoutFilter.class);
        httpSecurity.exceptionHandling(ex -> ex.authenticationEntryPoint(accessTokenEntryPoint));
        httpSecurity.addFilterAfter(optionsFilter, CorsFilter.class);

        return httpSecurity.build();
    }

    @Bean
    @Order(4)
    SecurityFilterChain unknownEndpoint(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher("/**");

        commonConfiguration(httpSecurity);
        httpSecurity.logout(AbstractHttpConfigurer::disable);
        // 서버가 처리할 수 있는 엔드포인트인지 확인하는 필터
        httpSecurity.addFilterBefore(unknownEndpointFilter, DisableEncodeUrlFilter.class);

        return httpSecurity.build();
    }

    private void commonConfiguration(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
    }

}
