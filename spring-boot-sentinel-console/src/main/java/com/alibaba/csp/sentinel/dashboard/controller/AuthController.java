package com.alibaba.csp.sentinel.dashboard.controller;

import com.alibaba.csp.sentinel.dashboard.service.auth.AuthService;
import com.alibaba.csp.sentinel.dashboard.service.auth.SimpleWebAuthServiceImpl;
import com.alibaba.csp.sentinel.dashboard.util.DashboardConfig;
import com.alibaba.csp.sentinel.dashboard.vo.Result;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Value("${auth.username:sentinel}")
    private String authUsername;

    @Value("${auth.password:sentinel}")
    private String authPassword;

    @Autowired
    private AuthService<HttpServletRequest> authService;

    @PostMapping("/login")
    public Result<AuthService.AuthUser> login(HttpServletRequest request, String username, String password) {
        if (StringUtils.isNotBlank(DashboardConfig.getAuthUsername())) {
            authUsername = DashboardConfig.getAuthUsername();
        }

        if (StringUtils.isNotBlank(DashboardConfig.getAuthPassword())) {
            authPassword = DashboardConfig.getAuthPassword();
        }

        /*
         * If auth.username or auth.password is blank(set in application.properties or VM arguments),
         * auth will pass, as the front side validate the input which can't be blank,
         * so user can input any username or password(both are not blank) to login in that case.
         */
        if (StringUtils.isNotBlank(authUsername) && !authUsername.equals(username)
                || StringUtils.isNotBlank(authPassword) && !authPassword.equals(password)) {
            LOGGER.error("Login failed: Invalid username or password, username=" + username);
            return Result.ofFail(-1, "Invalid username or password");
        }

        AuthService.AuthUser authUser = new SimpleWebAuthServiceImpl.SimpleWebAuthUserImpl(username);
        request.getSession().setAttribute(SimpleWebAuthServiceImpl.WEB_SESSION_KEY, authUser);
        return Result.ofSuccess(authUser);
    }

    @PostMapping(value = "/logout")
    public Result<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return Result.ofSuccess(null);
    }

    @PostMapping(value = "/check")
    public Result<?> check(HttpServletRequest request) {
        AuthService.AuthUser authUser = authService.getAuthUser(request);
        if (authUser == null) {
            return Result.ofFail(-1, "Not logged in");
        }
        return Result.ofSuccess(authUser);
    }
}