package com.example.demo.config.auth.jwt;

import com.example.demo.config.auth.redis.RedisUtil;
import com.example.demo.domain.entity.JwtToken;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.JwtTokenRepository;
import com.example.demo.domain.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    JWTTokenProvider jwtTokenProvider;
    @Autowired
    JwtTokenRepository jwtTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //전
        //access-token 쿠키 받기 Authentication 생성 이후 SecurityContextHolder에 저장
        System.out.println("[JWTAuthorizationFilter] doFilterInternal....");

        String token=null; //(access token)쿠키 받아 token=null;
        String userid=null; //userid 받기

        Cookie [] cookies=request.getCookies();
        if(cookies!=null){
            token = Arrays.stream(cookies)
                    .filter((cookie)->{return cookie.getName().equals(JwtProperties.ACCESS_TOKEN_COOKIE_NAME);})
                    .findFirst()
                    .map((cookie)->{return cookie.getValue();})
                    .orElse(null);
            userid = Arrays.stream(cookies) //쿠키에서 username만 빼오는 작업
                    .filter((cookie)->{return cookie.getName().equals("userid");})
                    .findFirst()
                    .map((cookie)->{return cookie.getValue();})
                    .orElse(null);
        }

        System.out.println("TOKEN : "+token);
        if(token!=null){
            // access-token 쿠키 받아 Authentication 생성 이후 SecurityContextHolder에 저장
            // 1) access-token 만료되었는지 확인
            try{
                if(jwtTokenProvider.validateToken(token)){
                    // 1-1) access-token 만료 x ? authentication 생성
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    if(authentication!=null)
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e){
                // 1-2) access-token 만료 o ? refresh-token 만료 여부확인
                System.out.println("ExpiredJwtException ....AccessToken Expired.."+e.getMessage());
                // 2) RefreshToken의 만료유무
                String refreshToken = redisUtil.getRefreshToken("RT:"+userid);
                try {
                    if(jwtTokenProvider.validateToken(refreshToken)){
                        // 2-1) RefreshToken!=만료 ? AccessToken 재발급 -> 쿠키전달 + DB Token Info 갱신
                        // AccessToken 재발급
                        long now = (new Date()).getTime(); // 현재시간(타임스탬프)
                        User user = userRepository.findById(userid).get(); // User entity에서 role 꺼내오기 위함
                        String accessToken = Jwts.builder()
                                    .setSubject(userid) // 본문 TITLE
                                    .setExpiration(new Date(now + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME)) // 만료날짜(초단위)
                                    .signWith(jwtTokenProvider.getKey(), SignatureAlgorithm.HS256) // 서명값
                                    .claim("userid",userid) // 본문내용
                                    .claim("auth",user.getRole()) // 본문내용 (role)
                                    .compact();
                        // 쿠키로 전달
                        Cookie cookie = new Cookie(JwtProperties.ACCESS_TOKEN_COOKIE_NAME,accessToken);
                        cookie.setMaxAge(JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME); // accesstoken 유지시간
                        cookie.setPath("/"); // 쿠키 적용경로(/ : 모든경로)
                        response.addCookie(cookie); // 응답정보에 쿠키 포함
                    }
                } catch (ExpiredJwtException e2) {
                    // 2-2) RefreshToken==만료 ? DB's Token Info 삭제
                    System.out.println("ExpiredJwtException ....RefreshToken Expired.."+e2.getMessage());
                    // access-token 제거(자동제거는 됨)
                    Cookie cookie = new Cookie(JwtProperties.ACCESS_TOKEN_COOKIE_NAME,null);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    redisUtil.delete("RT:"+userid);
                } catch (Exception e2){

                }

            } catch (Exception e){

            }

        } else { //access-token == null
            // accesstoken 만료 되었으나 refreshToken 유효한 경우
            if(userid!=null){

            }

        }

        filterChain.doFilter(request,response);

        //후
    }
}
