package com.example.demo.domain.dto;

import com.example.demo.domain.entity.Memo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data   //Lombok이 자동으로 Slf4j Logger 인스턴스를 생성 (getter, setter, toString, equals, hashCode)
@NoArgsConstructor //디폴트 생성자를 생성
@AllArgsConstructor //모든 인자 생성자를 생성
public class MemoDto {

	@NotBlank(message="메모를 입력하세요") //text에 값을 입력하지 않으면 나타낼 (message) 지정
	private String text; //클래스 내부 접근 제한 String text 생성
	@NotBlank(message="메모를 입력하세요") //writer에 값을 입력하지 않으면 나타낼 (message) 지정
	@Email(message="example@example.com에 맞게 입력해주세요") //writer에 이메일 형식으로 입력하지 않을 시 나타낼 (message) 지정
	private String writer; //클래스 내부 접근 제한 String writer 생성

    // DTO -> Entity 변환
    public Memo toEntity() { //DTO 필드 값을 사용해 Memo 엔티티 인스턴스를 생성
        return Memo.builder() //Memo 엔티티를 빌더 패턴으로 생성 후 반환
                .text(this.text) //Memo.text - DTO.text 매핑
                .writer(this.writer) //Memo.writer - DTO.writer 매핑
                .createAt(LocalDateTime.now()) //Memo.createAt - DTO.createAt : 생성 시각 설정(now() : 현재 시간)
                .build(); //Memo 인스턴스 생성 처리
    }
    // Entity -> DTO 변환
    public static MemoDto fromEntity(Memo memo) { //주어진 Memo 엔티티를 DTO로 변환 (null 안전 처리 포함)
        if (memo == null) return null; // memo 값이 null이면 null값 반환 (안 해주면 NullPointerException 발생 가능)
        return new MemoDto( //새로운 MemoDto 값 반환
                memo.getText(),     // Memo.text 값을 꺼내서 DTO.text에 저장
                memo.getWriter()    // Memo.Writer 값을 꺼내서 DTO.writer에 저장
        );
    }

}
