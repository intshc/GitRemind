package com.example.gitremind.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (!isExcludedEndpoint(httpRequest)) {
            String token = httpRequest.getHeader("Authorization");

            boolean isTokenValid = jwtUtil.verifyToken(token, "accessToken");

            if (isTokenValid) {
                // 토큰 검증 성공 시 통과 처리
                chain.doFilter(request, response);
            } else {
                // 토큰 검증 실패 시 401 에러
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            // 지정된 경로는 통과 처리
            chain.doFilter(request, response);
        }
    }

    private boolean isExcludedEndpoint(HttpServletRequest httpRequest) {
        String requestUri = httpRequest.getRequestURI();
        List<String> excludedEndpoints = Arrays.asList("/user/api", "/login", "/h2-console"
                , "/favicon.ico","/auth/refresh","/api/auth/verify-token");

        boolean isExcluded = false;
        for (String prefix : excludedEndpoints) {
            if (requestUri.startsWith(prefix)) {
                isExcluded = true;
                break;
            }
        }

        // h2만 예외적으로 토큰 검증 x
        return isExcluded;
    }
}
