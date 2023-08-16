package com.example.gitremind.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
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
            String tokenWithoutBearer = token.substring("Bearer ".length());
            String tokenType = "accessToken";

            boolean isTokenValid = jwtUtil.verifyToken(tokenWithoutBearer, tokenType);
            if (token != null && jwtUtil.verifyToken(tokenWithoutBearer, tokenType)) {
                chain.doFilter(request, response); // 토큰 검증 성공 시 요청 처리
            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 토큰 검증 실패 시 401 Unauthorized 응답 반환.
            }
        } else {
            chain.doFilter(request, response); // 요청이 RequestMatcher에 맞지 않으면, 거치지 않고 그대로 이어집니다.
        }
    }

    private boolean isExcludedEndpoint(HttpServletRequest httpRequest) {
        String requestUri = httpRequest.getRequestURI();
        String httpMethod = httpRequest.getMethod();
        List<String> excludedEndpoints = Arrays.asList("/user/api","/login","/h2-console"
        ,"/favicon.ico");

        boolean isExcluded = false;
        for (String prefix : excludedEndpoints) {
            if (requestUri.startsWith(prefix)) {
                isExcluded = true;
                break;
            }
        }

        // h2만 예외적으로 토큰 검증 x
        return (httpMethod.equals(HttpMethod.GET.name()) && isExcluded)||requestUri.startsWith("/h2-console");   }
}
