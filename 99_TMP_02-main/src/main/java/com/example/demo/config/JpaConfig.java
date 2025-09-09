package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration  //스프링 설정 클래스임을 명시 (Bean 등록 및 관리 목적)
@EntityScan(basePackages = {"com.example.demo.domain.entity"})  //JPA가 엔티티 클래스를 찾을 패키지 지정
@EnableJpaRepositories
(
                basePackages ="com.example.demo.domain.repository", //JPA Repository 인터페이스를 스캔할 패키지 지정
                transactionManagerRef = "jpaTransactionManager"     //트랜잭션 매니저는 "jpaTransactionManager"를 사용하도록 지정
)
public class JpaConfig { //외부에서 접근 가능한 JpaConfig 클래스 생성
    @Autowired  //의존성 주입 (객체 간의 의존 관계를 자동 설정)
    private DataSource dataSource;  //DB 연결 정보를 가진 DataSource Bean을 주입

    @Bean   //의존 관계를 관리하기 위한 스프링 컨테이너에 등록된 객체(스프링 빈)
    LocalContainerEntityManagerFactoryBean entityManagerFactory() { //JPA의 EntityManagerFactory -> Bean으로 등록
        // entityManagerFactory는 엔티티 매니저(EntityManager)를 생성
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        // entityManagerFactory를 스프링에서 관리할 수 있도록 LocalContainerEntityManagerFactoryBean 생성
        entityManagerFactoryBean.setDataSource(dataSource);
        // JPA가 사용할 DB 연결 정보를 DataSource에서 가져와 설정
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter()); //JPA 구현체로 Hibernate 사용
        // HibernateJpaVendorAdapter는 JPA와 Hibernate 간의 연결 어댑터
        entityManagerFactoryBean.setPackagesToScan("com.example.demo.domain.entity");
        // JPA가 엔티티(@Entity 붙은 클래스)를 스캔할 패키지 지정
        // 해당 경로에서 Memo 클래스를 매핑 처리

        Properties jpaProperties = new Properties(); // JPA 관련 설정을 담을 Properties 객체


        Map<String, Object> properties = new HashMap<>(); //Hibernate와 JPA 속성을 담을 Map 객체 생성
        properties.put("hibernate.hbm2ddl.auto", "update"); //DB 스키마 자동 생성 전략, 필요에 따라 'create', 'validate' 등으로 변경
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect"); //사용할 DB에 맞는 SQL Dialect 지정
        properties.put("hibernate.show_sql", true); //실행되는 SQL 쿼리를 콘솔에 출력 (개발 및 디버깅 용도)
        properties.put("hibernate.format_sql", true); //출력되는 SQL을 보기 좋게 정렬하여 출력

        properties.put("hibernate.hibernate.jdbc.batch_size", 1000); //JDBC batch 처리 시 한 번에 처리할 SQL 문 개수 지정
        properties.put("hibernate.hibernate.order_inserts", true); //INSERT 문을 비슷한 엔티티끼리 묶어서 순서 최적화 (성능 개선)
        properties.put("hibernate.order_updates", true); //UPDATE 문도 순서를 최적화하여 실행 (성능 개선)
        properties.put("hibernate.jdbc.batch_versioned_data", true); //버전 관리(@Version 사용하는 경우) 엔티티도 배치 처리 가능하게 설정
        entityManagerFactoryBean.setJpaPropertyMap(properties); //위에서 정의한 JPA/Hibernate 속성들을 EntityManagerFactory에 적용

        return entityManagerFactoryBean; //완료 후 EntityManagerFactoryBean 반환
    }

}

