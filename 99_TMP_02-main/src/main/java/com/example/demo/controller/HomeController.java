package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //뷰와 Model 연결
@Slf4j //로거(org.slf4j.Logger)를 자동 생성해주는 역할
public class HomeController { //외부에서 접근 가능한 HomeController 클래스 생성

    @GetMapping(value = "/") //("/") URL 최상위 경로에 매핑(연결)
    public String home(){

        log.info("GET /"); //("/")경로에 연결 시 출력문
        return "index"; //(index)경로 반환
    }
}
