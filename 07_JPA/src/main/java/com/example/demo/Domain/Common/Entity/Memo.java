package com.example.demo.Domain.Common.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="memo") //없으면 클래스명으로 테이블이 생성됨
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //AI : 자동으로 생성
    private Long id;
    @Column(length = 1024)
    private String text;
    @Column(length = 100,nullable = false) //NN : 무조건 데이터가 들어오도록
    private String writer;
    private LocalDateTime createAt;

}
