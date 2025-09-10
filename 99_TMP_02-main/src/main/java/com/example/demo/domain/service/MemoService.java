package com.example.demo.domain.service;

import com.example.demo.domain.dto.MemoDto;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

//@Transactional은 인터페이스가 아니라 구현체 쪽에 붙어야 정상 동작.
//인터페이스 처리로 결합도를 낮춤 (중간에 MemoService 추가)
public interface MemoService {
    @Transactional(rollbackFor = SQLException.class, transactionManager = "jpaTransactionManager")
    //메서드 실행을 하나의 트랜잭션으로 묶어 관리하도록 스프링에게 지시

    //rollbackFor = SQLException.class : SQLException은 checked 예외이므로 기본 규칙만으로 롤백이 되지 않아
    //별도로 SQLException.class 지정하여 예외가 발생해도 롤백되도록 설정

    //transactionManager = "jpaTransactionManager" : TxConfig 클래스에서 @Bean(name="jpaTransactionManager") 로 등록해둔 JPA 사용

    Long addMemoTx(MemoDto dto) throws Exception;
    //Long : 새로 저장된 Memo 엔티티의 식별자(PK) 값 반환을 위함
    //MemoDto dto : 서비스 계층에서 이 DTO를 엔티티(Memo)로 변환 후 저장
    //예외처리 최상위 Exception
}
