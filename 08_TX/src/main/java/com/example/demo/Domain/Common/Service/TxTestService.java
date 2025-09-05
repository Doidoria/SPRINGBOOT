package com.example.demo.Domain.Common.Service;

import com.example.demo.Domain.Common.Entity.Memo;
import com.example.demo.Domain.Common.Repository.MemoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class TxTestService {

    @Autowired
    private MemoRepository memoRepository;

    public void addMemo() throws Exception {
        log.info("TxTestService's addMemo....");
        Memo memo=Memo.builder()
                .id(null)
                .text("tx")
                .writer("abc@google.com")
                .createAt(LocalDateTime.now())
                .build();
        memoRepository.save(memo);
        memo.setId(null);
        memoRepository.save(memo);
        memo.setId(null);
        memoRepository.save(memo);
        memo.setId(null);
        throw new SQLException();
//        memoRepository.save(memo);
    }

    //트랜잭션 처리(SQLException 이 발생하면 원래되로 되돌린다.)
    @Transactional(rollbackFor = SQLException.class,transactionManager = "jpaTransactionManager")
    public void addMemoTx() throws Exception {
        log.info("TxTestService's addMemoTx....");
        Memo memo=Memo.builder()
                .id(null)
                .text("tx")
                .writer("abc@google.com")
                .createAt(LocalDateTime.now())
                .build();
        memoRepository.save(memo);
        memo.setId(null);
        memoRepository.save(memo);
        memo.setId(null);
        memoRepository.save(memo);
        memo.setId(null);
        throw new SQLException();
//        memoRepository.save(memo);
    }

}

