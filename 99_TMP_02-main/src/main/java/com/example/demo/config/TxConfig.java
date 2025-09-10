package com.example.demo.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration //스프링 설정 클래스임을 명시 (Bean 등록 및 관리 목적)
@EnableTransactionManagement //스프링의 트랜잭션 관리 기능 활성화
public class TxConfig { //트랜잭션 매니저를 설정하는 TxConfig 클래스 생성

    @Autowired //의존성 주입 (객체 간의 의존 관계를 자동 설정)
    private DataSource dataSource; //DB 연결 정보를 가진 DataSource Bean을 주입받음

    @Bean(name="jpaTransactionManager") //Bean 이름을 "jpaTransactionManager"로 지정
    //JpaConfig에서 @EnableJpaRepositories(transactionManagerRef="jpaTransactionManager")로 연결
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        //JpaTransactionManager 객체를 Bean으로 등록하는 메서드 (JPA 환경에서 트랜잭션을 관리)
        //트랜잭션 기본 코드 작성
        JpaTransactionManager transactionManager=new JpaTransactionManager(); //JpaTransactionManager 객체 생성
        transactionManager.setEntityManagerFactory(entityManagerFactory); //JPA에서 엔티티 매니저(EntityManager)를 생성하는 핵심 객체
        transactionManager.setDataSource(dataSource); //DB 연결을 위한 커넥션 풀(DataSource) 지정
        return transactionManager; //transactionManager 객체 반환
    }

}

