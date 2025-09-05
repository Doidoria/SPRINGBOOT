package com.example.demo.Dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PersonDtoTest {

    @Test
    public void t1(){
        PersonDto dto=new PersonDto("홍길동",50,"대구");
        System.out.println(dto);
    }

    @Test
    public void t2(){
        PersonDto dto=PersonDto.builder()
                .name("홍길동")
                .age(20)
                .addr("동굴")
                .build();
        System.out.println(dto);
    }
}