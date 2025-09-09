package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity                 //JPA가 이 클래스를 DB 테이블과 매핑되는 엔티티로 인식
@Table(name="memo")     //@Table(name="memo")이 없으면 클래스명으로 테이블이 자동 생성됨
@Data                   //Lombok이 자동으로 Slf4j Logger 인스턴스를 생성 (getter, setter, toString, equals, hashCode)
@NoArgsConstructor      //디폴트 생성자 생성
@AllArgsConstructor     //모든 인자 생성자 생성
@Builder                //빌더 패턴 생성
public class Memo {
    @Id //이 필드가 엔티티의 기본 키(PRIMARY KEY)임을 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성 전략 설정, (IDENTITY : ID 자동으로 생성 증가), AUTO_INCREMENT(또는 동등한 기능)
    private Long id; //엔티티의 고유 식별자(Primary Key) id 생성
    private String text; // 내용(문자열) 필드 - text 생성
    private String writer;  // 이메일(문자열) 필드 - text 생성
    private LocalDateTime createAt; // 생성 시각 필드 - createAt 생성
}
