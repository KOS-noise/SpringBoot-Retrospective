package com.basic.myspringboot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter // 클래스 내 모든 필드(message, httpStatus)의 Getter 메서드를 자동 생성함
public class BusinessException extends RuntimeException { // 실행 시점(Runtime)에 발생하는 예외를 처리하기 위해 상속받음

    // 객체 직렬화/역직렬화 시 버전 호환성을 확인하기 위한 고유 식별자
    private static final long serialVersionUID = 1L;

    private String message; // 발생한 예외의 상세 내용을 담는 필드
    private HttpStatus httpStatus; // 클라이언트에게 반환할 HTTP 상태 코드(예: 404, 500 등)를 담는 필드

    // 에러 메시지만 전달될 때 호출되는 생성자
    public BusinessException(String message) {
        // 기본 HTTP 상태 코드를 417(EXPECTATION_FAILED)로 설정하여 다른 생성자 호출
        this(message, HttpStatus.EXPECTATION_FAILED);
    }

    // 에러 메시지와 HTTP 상태 코드를 모두 전달받을 때 호출되는 생성자
    public BusinessException(String message, HttpStatus httpStatus) {
        // 전달받은 값으로 클래스 필드 초기화
        this.message = message;
        this.httpStatus = httpStatus;
    }
}