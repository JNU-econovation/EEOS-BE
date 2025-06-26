package com.blackcompany.eeos.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Slf4j
@Component
public class UnknownEndpointFilter extends OncePerRequestFilter {


    private final RequestMappingHandlerMapping mapping;

    public UnknownEndpointFilter(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        try {
            if (mapping.getHandler(request) != null) {
                log.warn("{}는 존재하지만 시큐리티 필터 체인에 등록되지 않은 엔드포인트입니다.", request.getRequestURI());
            }
        } catch (HttpRequestMethodNotSupportedException e) {
            log.warn("{}는 {}를 지원하지 않음", request.getRequestURI(), request.getMethod());
            response405(response);
        } catch (Exception e) {
            throw new IllegalStateException("엔드포인트 확인 중 오류 발생", e);
        } finally {
            response404(response); // 핸들러가 있든 없든 실패하든 말든 프론트로 404 반환
        }
    }

    private void response404(HttpServletResponse response) throws IOException {
        // 엔드포인트가 존재하지 않으면 404 반환
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("{\"message\": \"존재하지 않는 엔드포인트입니다.\"}");
        response.getWriter().flush();
        response.getWriter().close();
    }

    private void response405(HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.getWriter().write("{\"message\": \"이 엔드포인트는 해당 메소드를 지원하지 않습니다.\"}");
        response.getWriter().flush();
        response.getWriter().close();
    }
}
