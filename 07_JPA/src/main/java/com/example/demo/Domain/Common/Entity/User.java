package com.example.demo.Domain.Common.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user") //없으면 클래스명으로 테이블이 생성됨
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @Column(length = 100)
    private String username;
    @Column(length = 255,nullable = false)
    private String password;
    @Column(length = 255)
    private String role;
}
