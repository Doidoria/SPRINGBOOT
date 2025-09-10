package com.example.demo.controller.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice //관점지향코드 (핵심적인 로직이 아닌 반복적으로 수행하지만 Aop 처리)
//여러 컨트롤러에서 발생하는 예외처리를 중앙 집중식으로 처리
@Slf4j //로거(org.slf4j.Logger)를 자동 생성해주는 역할
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
    //Exception(최상위 예외 클래스) 타입의 예외가 발생했을 때 실행됨(Exception.class)
	public String AllExceptionExceptionHandler(Exception e, Model model) {
        //Exception e : 발생한 예외 객체를 자동으로 전달
        //Model model : 뷰(View, JSP/Thymeleaf 등)에 데이터를 전달 가능
		log.info("GlobalExceptionHandler's error : " + e); //예외처리 발생 원인 확인을 위한 출력문
        //예외발생시 webapp/WEB-INF/views/except/error.jsp로 이동합니다
		return "except/error"; //(except/error)경로 반환
	}
}







