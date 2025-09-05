package com.example.demo.Domain.Common.Repository;

import com.example.demo.Domain.Common.Entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoRepository extends JpaRepository<Memo,Long> {  //기본 CRDD 함수들이 만들어짐
    //메서드명명법

    //JPQL (SQL문 직접 작성)

}
