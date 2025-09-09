package com.example.demo.domain.service;

import com.example.demo.domain.dto.MemoDto;
import com.example.demo.domain.entity.Memo;
import com.example.demo.domain.repository.MemoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service // 스프링이 "이 클래스는 서비스 계층 Bean이다"라고 인식해서 자동으로 컨테이너에 등록
@Slf4j // Lombok이 log라는 Logger 객체를 자동 생성
public class MemoServiceImpl implements MemoService { //MemoService 인터페이스를 구현하는 실제 서비스 클래스 (MemoServiceImpl)
    //JPA REPOSITORY
    @Autowired //스프링 설정 클래스임을 명시 (Bean 등록 및 관리 목적)
    private MemoRepository memoRepository; //DB 연결 정보를 가진 MemoRepository Bean을 주입

    //트랜잭션 설정할 것
    @Override
    @Transactional(rollbackFor = SQLException.class, transactionManager = "jpaTransactionManager")
    //메서드 실행을 하나의 트랜잭션으로 묶어 관리하도록 스프링에게 지시
    // rollbackFor = SQLException.class : SQLException은 checked 예외이므로 기본 규칙만으로 롤백이 되지 않아
    //  ↑ 별도로 SQLException.class 지정하여 예외가 발생해도 롤백되도록 설정
    // transactionManager = "jpaTransactionManager" : TxConfig 클래스에서 @Bean(name="jpaTransactionManager") 로 등록해둔 JPA 사용
    public Long addMemoTx(MemoDto dto) throws Exception{ //"메모 추가" 서비스 기능을 수행하는 메서드
        //Long : 새로 저장된 Memo 엔티티의 식별자(PK) 값 반환을 위함
        //MemoDto dto : 서비스 계층에서 이 DTO를 엔티티(Memo)로 변환 후 저장
        //예외처리 최상위 Exception
        log.info("MemoService's addMemoTx2 Call!"); // 서비스 계층에서 해당 메서드가 호출 확인 출력문
        //코드 완성
        Memo memo=dto.toEntity(); //DTO → Entity (MemoDto 객체를 JPA Entity(Memo) 객체로 변환)
        memoRepository.save(memo); //MemoRepository(JPA)를 통해 DB에 memo 저장.
        return memo.getId();    //memo.id가 반환되며 자동으로 채워짐 (AUTO_INCREMENT)
    }
}