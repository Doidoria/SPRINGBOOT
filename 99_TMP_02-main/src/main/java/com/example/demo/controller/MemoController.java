package com.example.demo.controller;

import com.example.demo.domain.dto.MemoDto;
import com.example.demo.domain.service.MemoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller //뷰와 Model 연결
@Slf4j //Lombok이 자동으로 Slf4j Logger 인스턴스를 생성 (getter, setter, toString, equals, hashCode)
@RequestMapping("/memo") //URL("/memo") 경로 요청 처리
public class MemoController { // 외부에서 접근 가능한 MemoController 클래스 생성

    @Autowired //스프링 설정 클래스임을 명시 (Bean 등록 및 관리 목적)
    private MemoService memoService; //DB 연결 정보를 가진 memoService Bean을 주입

	@GetMapping("/add") //("/add") URL 경로에 매핑(연결)
	public void add_get() throws Exception { //("/memo/add") 경로에 GET 요청이 들어올 경우 실행 (예외처리 Exception로 통합처리)
		log.info("GET /memo/add..."); //("/add")경로에 연결 시 출력문
	}

	@PostMapping("/add") //("/add") URL 경로에 POST 맵핑(처리)
    //@Valid MemoDto dto : 요청 파라미터를 MemoDto 객체에 바인딩 후 유효성 검증(@Valid) 수행
    //Model model : 뷰로 데이터 전달을 위한 모델 객체
    //BindingResult bindingResult : @Valid 검증 결과 저장 객체(에러 여부 확인 가능)
    //RedirectAttributes redirectAttributes : 리다이렉트 시 데이터를 전달할 수 있는 객체
    //(예외처리 Exception로 통합처리)
	public String add_post(@Valid MemoDto dto, BindingResult bindingResult,Model model, RedirectAttributes redirectAttributes) throws Exception{
		log.info("POST /memo/add..."+dto); //요청된 DTO 로그 출력문
        //01 파라미터 받기(MemoDto)
        //02 유효성 검증(BindingResult)
        log.info("유효성 오류 발생 여부 : "+bindingResult.hasErrors()); //유효성 검증 에러 발생 여부 확인 출력문
        if(bindingResult.hasErrors()){  //bindingResult 결과가 필드에 들어가있다면 -> (유효성 검증 오류가 하나라도 발생하면 true)
            for(FieldError error : bindingResult.getFieldErrors()){ //발생한 모든 필드 에러를 반복 처리
                log.info("에러 필드 : "+error.getField()+"에러 메세지 : "+error.getDefaultMessage());
                //어떤 에러 필드, 에러 메세지가 발생했는지 확인을 위한 로그 출력문
                model.addAttribute(error.getField(),error.getDefaultMessage());
                //해당 필드 이름을 key로, 에러 메시지를 value로 모델에 담아 뷰로 전달
            }
            //유효성 검증 오류 발생시 -> memo/add 이동(오류필트메시지 전달)
            return "memo/add"; // (memo/add)경로 반환
        }
        //03 서비스 실행(memoService(dto) 전달 후 memoId값 반환)
        Long memoId=memoService.addMemoTx(dto);  //유효성 검증에 이상이 없으면 서비스로 전달 (추가된 ID번호 확인을 위한 memoId 저장)
        if(memoId!=null) //memoId가 null값이 아니면 ↓
            redirectAttributes.addFlashAttribute("result","메모저장 성공 ID : "+memoId);
            //("result")라는 이름(key)으로 ("메모저장 성공 ID :"+memoId) 값 -> (value)에 저장

        //04 뷰로 이동 Redirect:/ , 리다이렉트 메시지 전달
        //redirectAttributes.addFlashAttribute("result","메모저장 성공 ID : " + memoId);
        return memoId!=null ? "redirect:/":"memo/add";
        //memoId가 null값이 아니라면(true) -> redirect:/ (false) -> memo/add
	}
}


