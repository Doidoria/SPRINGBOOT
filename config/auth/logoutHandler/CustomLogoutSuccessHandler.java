package com.example.demo.config.auth.logoutHandler;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.config.auth.jwt.JWTTokenProvider;
import com.example.demo.config.auth.jwt.JwtProperties;
import com.example.demo.config.auth.redis.RedisUtil;
import com.example.demo.domain.repository.JwtTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    private void clearCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return;

        Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .forEach(cookie -> {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    log.info("Removed cookie: {}", cookieName);
                });
    }

    // 로컬 서버 로그아웃 이후 추가 처리(ex. 카카오 인증 서버 연결해제..)
    @Override
    @Transactional(rollbackFor = Exception.class,transactionManager = "jpaTransactionManager")
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("CustomLogoutSuccessHandler's onLogoutSuccess invoke....!");

        String token = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> c.getName().equals(JwtProperties.ACCESS_TOKEN_COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (token != null) {
            String userId = jwtTokenProvider.getUserEmail(token); // 여기서 userid 추출
            if (userId != null) {
                redisUtil.delete("RT:" + userId);
                log.info("Deleted refresh token for {}", userId);
            }
        } else{
            log.info("token : "+token);
        }

        clearCookie(request, response, JwtProperties.ACCESS_TOKEN_COOKIE_NAME);
        clearCookie(request, response, "userid");

        String provider = (String) request.getAttribute("provider");
        log.info("Logout provider = {}", provider);

        if (provider != null) {
            if (provider.startsWith("Kakao")) {
                log.info("Redirecting to Kakao logout...");
                response.sendRedirect("https://kauth.kakao.com/oauth/logout?client_id="+KAKAO_CLIENT_ID+"&logout_redirect_uri="+KAKAO_REDIRECT_URI);
                return;
            } else if (provider.startsWith("Naver")) {
                log.info("Redirecting to Naver logout...");
                response.sendRedirect("https://nid.naver.com/nidlogin.logout?returl=https://www.naver.com/");
                return;
            } else if (provider.startsWith("Google")) {
                log.info("Redirecting to Google logout...");
                response.sendRedirect("https://accounts.google.com/Logout");
                return;
            }
        }
        response.sendRedirect("/");
    }

}
