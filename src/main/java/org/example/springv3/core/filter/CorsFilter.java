package org.example.springv3.core.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("CORS 필터 작동");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, PATCH, GET, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Api-Key, X-Requested-With, Content-Type, Accept, Authorization");

        // 웹소켓: OPTIONS 메서드에 대한 응답 헤더 설정
        // 통신하기 전에 미리 가서 한 번 확인하는 것이다. 이게 OK면 통신 가능한 것.
        // 소문자가 올지 대문자가 올지 몰라서 equalsIgnoreCase로 받음
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("여기걸림?");
            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            chain.doFilter(req, res);
        }

    }
}