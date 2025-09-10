package com.example.demo.Domain.Common.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="lend") //없으면 클래스명으로 테이블이 생성됨
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne //N:1
    @JoinColumn(
            name="username",
            foreignKey =@ForeignKey(
                    name="FK_LEND_USER",
                    foreignKeyDefinition = "FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE ON UPDATE CASCADE"
            )
    )
    private User user;

    @ManyToOne //N:1
    @JoinColumn(
        name="bookCode",
        foreignKey = @ForeignKey(
                name="FK_LEND_BOOK",
                foreignKeyDefinition = "FOREIGN KEY (bookCode) REFERENCES book(bookCode) ON DELETE CASCADE ON UPDATE CASCADE"
        )
    )
    private Book book;

}
