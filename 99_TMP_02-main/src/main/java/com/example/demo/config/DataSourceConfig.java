package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration //스프링 설정 클래스임을 명시 (Bean 등록 및 관리 목적)
public class DataSourceConfig { //외부에서 접근 가능한 DataSourceConfig 클래스 생성

	@Bean //해당 메서드가 반환하는 객체(HikariDataSource)를 스프링 Bean으로 등록
	public HikariDataSource dataSource() //HikariCP 기반의 DataSource 객체 생성 및 반환
	{
        //HikariDataSource 사용할 것
        HikariDataSource dataSource=new HikariDataSource(); //HikariDataSource 객체를 직접 생성 (커넥션 풀 관리)
        //Mysql Connection
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); //DB 드라이버 클래스 지정 (MySQL 최신 드라이버)
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb"); //"localhost:3306/testdb" → 로컬 호스트의 testdb 데이터베이스 연결
        // Id : root
        dataSource.setUsername("root"); //DB 사용자 계정 ID 지정
        // Pw : 1234
        dataSource.setPassword("1234"); //DB 사용자 계정 PW 지정
        // dataSource 반환
        return dataSource; //DataSource 객체 반환
	}

}
