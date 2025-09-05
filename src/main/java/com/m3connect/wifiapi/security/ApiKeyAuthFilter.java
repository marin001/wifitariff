package com.m3connect.wifiapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

  private static final String API_KEY_HEADER = "X-API-KEY";

  @Value("${api.key}")
  private String expectedApiKey;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String path = request.getRequestURI();

    // Allow Swagger and public endpoints through
    if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
      filterChain.doFilter(request, response);
      return;
    }

    //add dummy endpoints
    if (path.startsWith("/dummy-list-all-tariffs") || path.startsWith("/dummy-remote-tariffs")) {
        filterChain.doFilter(request, response);
        return;
    }

    String apiKey = request.getHeader(API_KEY_HEADER);
    if (expectedApiKey != null && expectedApiKey.equals(apiKey)) {
      filterChain.doFilter(request, response);
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid or missing API key");
    }
  }
}
