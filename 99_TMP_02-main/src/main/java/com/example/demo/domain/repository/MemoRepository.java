package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//메모를 어떻게 "저장/조회/삭제"에 대한 기능(추상화)만 정의
//실제 JPA가 어떻게 SQL을 만들고 실행하는지는 스프링 데이터 JPA가 알아서 처리해줌.
//즉, 우리는 인터페이스만 정의하면 -> 구현체는 스프링이 런타임에 자동 생성
@Repository
public interface MemoRepository extends JpaRepository<Memo,Long>{
    //JpaRepository<엔티티 타입, 기본키 타입>을 상속받음

    //JpaRepository가 자동 제공하는 기능 예시
    // - save(entity)       : 엔티티 저장/업데이트
    // - findById(id)       : ID로 엔티티 조회 (Optional 반환)
    // - findAll()          : 모든 엔티티 조회
    // - delete(entity)     : 엔티티 삭제
    // - deleteById(id)     : ID로 삭제
    // - count()            : 전체 데이터 개수 반환
    // - existsById(id)     : 특정 ID 존재 여부 확인
    //즉, Memo 엔티티와 관련된 CRUD 기능을 SQL 작성 없이 바로 사용 가능.
}
