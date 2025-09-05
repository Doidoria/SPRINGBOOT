package com.example.demo.Controller.GlobarException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//IOC/DI, MVC, AOP 정리하자

@ControllerAdvice  //관점지향코드 (핵심적인 로직이 아닌 반복적으로 수행하지만 Aop 처리)
@Slf4j
public class GlobarExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String allExceptionHandler(Exception e){
        log.info("Global Exception Handler..."+e);
        return "global_error";
    }
}
