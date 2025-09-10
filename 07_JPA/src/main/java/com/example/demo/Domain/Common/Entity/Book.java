package com.example.demo.Domain.Common.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="book") //없으면 클래스명으로 테이블이 생성됨
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    private Long bookCode;
    private String bookName;
    private String publisher;
    private String isbn;


}
