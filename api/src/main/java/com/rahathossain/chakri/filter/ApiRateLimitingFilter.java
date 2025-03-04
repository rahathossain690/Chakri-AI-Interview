package com.rahathossain.chakri.filter;

import com.rahathossain.chakri.service.RateLimiterService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiRateLimitingFilter implements Filter {

    private final RateLimiterService rateLimiterService;

    public ApiRateLimitingFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!rateLimiterService.isApiRequestAllowed()) {
            sendRateLimitResponse(response);
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendRateLimitResponse(ServletResponse response) throws IOException {
        ((HttpServletResponse) response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    }
}
