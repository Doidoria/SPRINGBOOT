package com.example.demo.config.auth.logoutHandler;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.config.auth.redis.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private RedisUtil redisUtil;

    // 해당 서버의 자체 로그아웃 처리
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("CustomAddLogoutHandler's logout invoke....!");

//        if (authentication != null) {
//            String userid = authentication.getName();
//            redisUtil.delete("RT:" + userid);
//            request.setAttribute("provider", ((PrincipalDetails) authentication.getPrincipal()).getDto().getProvider());
//            log.info("LogoutHandler - Deleted refresh token for {}", userid);
//        }

    }
}
