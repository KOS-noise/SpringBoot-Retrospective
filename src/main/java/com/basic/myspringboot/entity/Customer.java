package com.basic.myspringboot.entity;

import jakarta.persistence.*; // JPA 표준 어노테이션 임포트
import lombok.Getter;
import lombok.Setter;

@Entity // JPA가 관리할 엔티티 객체 지정
@Getter
@Setter
@Table(name="customers") // 매핑할 DB 테이블 이름을 'customers'로 지정
public class Customer {

    @Id // 기본 키(PK) 지정
    @GeneratedValue(strategy= GenerationType.IDENTITY) // PK 값 자동 증가 및 생성
    private Long id;

    @Column(unique = true, nullable = false) // 중복 불가(unique), 필수 입력(null 불가)
    private String customerId; // 로그인용 고객 아이디

    @Column(nullable = false) // 필수 입력(null 불가)
    private String customerName; // 고객 이름 (주의: customerName 오타 있음)

}