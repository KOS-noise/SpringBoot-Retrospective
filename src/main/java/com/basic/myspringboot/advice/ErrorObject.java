package com.basic.myspringboot.advice;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data // 코드에 안 보이지만 Getter, Setter 등을 알아서 다 만들어주는 마법 같은 역할
public class ErrorObject {
    private Integer statusCode; // 에러 번호 저장 (예: 404, 500)
    private String message; // 에러 내용 저장 (예: "비밀번호가 틀렸습니다")
    private String timestamp; // 에러가 터진 시간 저장

    // 에러 발생 시간을 보기 좋게 꾸며서 꺼내주는 역할
    public String getTimestamp() {
        LocalDateTime ldt = LocalDateTime.now(); // 지금 딱 현재 시간을 가져옴
        return DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss E a", // '년-월-일 시:분:초 요일 오전/오후' 모양으로 틀을 잡음
                Locale.KOREA).format(ldt); // 한국 시간에 맞춰서 예쁜 글자 형태로 바꿔줌
    }
}