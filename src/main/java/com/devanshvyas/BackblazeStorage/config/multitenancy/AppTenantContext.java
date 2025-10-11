package com.devanshvyas.BackblazeStorage.config.multitenancy;

import com.devanshvyas.BackblazeStorage.service.JwtService;
import com.devanshvyas.BackblazeStorage.util.Constants;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.List;
import java.util.Objects;

@Component
public class AppTenantContext implements Filter {
    private static final String LOGGER_TENANT_ID = "tenant_id";
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    private final List<String> excludedUrls = List.of("/user/config-storage", "/user/register-sub-user");

    @Autowired
    private JwtService jwtService;

    public static String getCurrentTenant() {
        String tenant = currentTenant.get();
        return Objects.requireNonNullElse(tenant, Constants.DEFAULT_TENANT);
    }

    public static void setCurrentTenant(String tenant) {
        MDC.put(LOGGER_TENANT_ID, tenant);
        currentTenant.set(tenant);
    }

    public static void clear() {
        MDC.clear();
        currentTenant.remove();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String header = httpServletRequest.getHeader("Authorization");
        if (header != null && header.contains("Bearer ") && !excludedUrls.contains(httpServletRequest.getServletPath())) {
            String token = header.substring(7);
            String tenantId = jwtService.extractTenantId(token);
            setCurrentTenant(tenantId);
        }
        chain.doFilter(request, response);
        clear();
    }
}
