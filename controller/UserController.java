package com.example.demo.controller;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.domain.dtos.JoinDto;
import com.example.demo.domain.dtos.UserDto;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public void login(@AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        log.info("GET /login....");
        String endPoint = request.getRequestURI();
        if(principalDetails!=null)
            response.sendRedirect("/user");
    }

    // 사용자 계정 확인방법 - 2
    @GetMapping("/user")
    public void user(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("GET /user...."+authentication);
        log.info("name : "+authentication.getName());
        log.info("principal : "+authentication.getPrincipal());
        log.info("authoritites : "+authentication.getAuthorities());
        log.info("details : "+authentication.getDetails());
        log.info("credential : "+authentication.getCredentials());

        model.addAttribute("auth_1",authentication);
    }

    // 확인 방법 -3 Authentication's Principal 만 꺼내와 연결
    @GetMapping("/manager")
    public void manager(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        log.info("GET /manager...."+principalDetails);
        model.addAttribute("",principalDetails);
    }

    @GetMapping("/admin")
    public void admin(){
        log.info("GET /admin....");
    }

    @GetMapping("/join")
    public String join(Model model){
        log.info("GET /join....");
        model.addAttribute("joinDto", new JoinDto());
        return "join";
    }

    @PostMapping("/join")
    public String join_post(@Valid JoinDto joinDto, BindingResult bindingResult, Model model){
        log.info("POST /join...."+joinDto);

        // 1. 서버 측 비밀번호 일치 검증 로직
        if (!joinDto.getPassword().equals(joinDto.getRepassword())) {
            // 에러를 BindingResult에 추가
            bindingResult.rejectValue("repassword", "passwordInconsistency", "패스워드가 일치하지 않습니다.");
        }

        // 2. 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            model.addAttribute("joinDto", joinDto);
            return "join";
        }

        String pwd=passwordEncoder.encode(joinDto.getPassword()); //패스워드 암호화 저장

        //dto -> entity
        User user = new User();
        user.setUserid(joinDto.getUserid());
        user.setUsername(joinDto.getUsername());
        user.setPassword(pwd);
        user.setRole("ROLE_USER");
        userRepository.save(user);

        return "redirect:/login";
    }
}
