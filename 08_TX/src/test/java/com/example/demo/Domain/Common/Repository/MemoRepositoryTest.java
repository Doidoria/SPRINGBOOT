package com.example.demo.Domain.Common.Repository;

import com.example.demo.Domain.Common.Entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class MemoRepositoryTest {

    @Autowired
    private MemoRepository memoRepository;

    @Test
    public void t1(){   //생성
        Memo memo=new Memo(null,"내용1","test@naver.com", LocalDateTime.now());
        memoRepository.save(memo);
        System.out.println("ID :"+memo.getId());
    }

    @Test
    public void t2(){   //업데이트
        Memo memo=new Memo(1L,"수정된 내용1","test@test.com", LocalDateTime.now());
        memoRepository.save(memo);
        System.out.println("ID :"+memo.getId());
    }

    @Test
    public void t3(){   //삭제
        memoRepository.deleteById(1L);
    }

    @Test
    public void t4(){   //조회
        Optional<Memo> memoOptional=memoRepository.findById(3L);
        if(memoOptional.isPresent()) {    //isPresent : 존재하냐?
            Memo memo=memoOptional.get();
            System.out.println(memo);
        }
    }

    @Test
    public void t5(){   //전체조회
        List<Memo> list=memoRepository.findAll();
        list.forEach(System.out::println);
        //(el)->{System.out.println(el);}
        //el->System.out.println(el);
    }
}