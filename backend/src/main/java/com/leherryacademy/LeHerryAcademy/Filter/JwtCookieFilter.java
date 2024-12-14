package com.leherryacademy.LeHerryAcademy.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class JwtCookieFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/apiAuthen")) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            // Tìm và lấy token từ cookie
            String token = Arrays.stream(cookies)
                    .filter(cookie -> "JWToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);


            if (token != null) {
                HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getHeader(String name) {
                        if ("Authorization".equals(name)) {
                            return "Bearer " + token;
                        }
                        return super.getHeader(name);
                    }
                };
                filterChain.doFilter(wrappedRequest, response);
                return;
            }


        }
        filterChain.doFilter(request,response);
    }
}
