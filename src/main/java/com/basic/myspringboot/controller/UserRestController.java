package com.basic.myspringboot.controller;

import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // JSON 형태로 응답하는 컨트롤러
@Slf4j // 로깅 객체(log) 자동 생성
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동 생성하여 DI(의존성 주입) 수행
@RequestMapping("/api/users") // 기본 URL 경로 지정
public class UserRestController {

    // Spring 추천 방식: final 선언 후 생성자를 통한 의존성 주입
    private final UserRepository userRepository;

    // User 등록 (Create)
    @PostMapping
    public User create(@RequestBody User userDetail) { // HTTP Body의 JSON을 객체로 변환
        return userRepository.save(userDetail);
    }

    // User 목록 전체 조회 (Read)
    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // Id로 단건 User 조회 (Read)
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) { // URL 경로의 {id} 값을 변수로 받음
        Optional<User> optionalUser = userRepository.findById(id);
        return getExistUser(optionalUser);
    }

    // Email로 조회 후 User 수정 (Update)
    @PatchMapping("/{email}/") // 자원의 일부만 수정하므로 PATCH 사용 (경로 끝 슬래시 주의)
    public User updateUser(@PathVariable String email, @RequestBody User userDetail) {
        // 1. 기존 유저 찾기
        User existUser = getExistUser(userRepository.findByEmail(email));
        // 2. 이름 변경
        existUser.setName(userDetail.getName());
        // 3. 저장 및 반환
        return userRepository.save(existUser);
    }

    // User 삭제 (Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = getExistUser(optionalUser);
        userRepository.delete(user);

        // 데이터 본문 없이 상태 코드 200(OK)만 깔끔하게 반환
        return ResponseEntity.ok().build();
    }

    // [공통 메서드] Optional 객체를 검증하여 User를 반환하거나 커스텀 예외 발생
    private static User getExistUser(Optional<User> optionalUser) {
        return optionalUser
                // 데이터가 없으면 BusinessException 던짐 (404 Not Found 상태 코드와 함께)
                .orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
    }
}