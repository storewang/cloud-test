package com.alibaba.csp.sentinel.dashboard.filter;

import com.alibaba.csp.sentinel.dashboard.service.auth.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet Filter that authenticate requests.
 *
 * Note:
 * Some urls are excluded as they needn't auth, such as:
 *
 * Index url: /
 * Authentication request url: /login,logout
 * Used for client: /registry/machine
 * Static resources: htm,html,js and so on.
 *
 * The excluded urls and urlSuffixes are configured in application.properties
 *
 * @author cdfive
 * @since 1.6.0
 */
@Component
public class AuthFilter implements Filter {

    private static final String URL_SUFFIX_DOT = ".";

    /**Some urls which needn't auth, such as /auth/login,/registry/machine and so on*/
    @Value("#{'${auth.filter.exclude-urls}'.split(',')}")
    private List<String> authFilterExcludeUrls;

    /**Some urls with suffixes which needn't auth, such as htm,html,js and so on*/
    @Value("#{'${auth.filter.exclude-url-suffixes}'.split(',')}")
    private List<String> authFilterExcludeUrlSuffixes;

    /**Authentication using AuthService interface*/
    @Autowired
    private AuthService<HttpServletRequest> authService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String servletPath = httpRequest.getServletPath();

        // Exclude the urls which needn't auth
        if (authFilterExcludeUrls.contains(servletPath)) {
            chain.doFilter(request, response);
            return;
        }

        // Exclude the urls with suffixes which needn't auth
        for (String authFilterExcludeUrlSuffix : authFilterExcludeUrlSuffixes) {
            if (StringUtils.isBlank(authFilterExcludeUrlSuffix)) {
                continue;
            }

            // Add . for url suffix so that we needn't add . in property file
            if (!authFilterExcludeUrlSuffix.startsWith(URL_SUFFIX_DOT)) {
                authFilterExcludeUrlSuffix = URL_SUFFIX_DOT + authFilterExcludeUrlSuffix;
            }

            if (servletPath.endsWith(authFilterExcludeUrlSuffix)) {
                chain.doFilter(request, response);
                return;
            }
        }

        AuthService.AuthUser authUser = authService.getAuthUser(httpRequest);

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (authUser == null) {
            // If auth fail, set response status code to 401
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}