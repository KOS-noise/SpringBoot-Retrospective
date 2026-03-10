package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 1. 스프링 부트의 모든 빈(Bean)을 로드해서 실제 환경과 유사하게 테스트하겠다는 선언
@Transactional
class CustomerRepositoryTest {

    @Autowired // 2. 스프링 컨테이너가 관리하는 CustomerRepository 객체를 자동으로 주입(연결)해줘!
    CustomerRepository customerRepository;

    @Test
        // 3. 이 메서드는 '테스트 케이스'임을 알려줌 (JUnit이 이 메서드를 실행함)
    @Rollback(value=false) // 롤백 처리를 하지 마세요!
    @Disabled
    void testCreate() {
        // [Given] 준비: 테스트를 위해 필요한 데이터를 만드는 단계
        Customer customer = new Customer(); // 4. 새로운 고객(Customer) 객체를 생성
        customer.setCustomerId("A004");      // 5. 고객 아이디를 "A002"로 설정
        customer.setCustomerName("스프링부트4"); // 6. 고객 이름을 "스프링부트"로 설정

        // [When] 실행: 실제로 검증하고 싶은 로직(DB 저장 등)을 수행하는 단계
        Customer addCustomer = customerRepository.save(customer); // 7. 레포지토리를 통해 DB에 저장하고, 저장된 결과를 반환받음

        // [Then] 검증: 실행 결과가 내가 예상한 값과 일치하는지 확인하는 단계
        assertThat(addCustomer).isNotNull(); // 8. 저장되어 나온 객체(addCustomer)가 null이 아니어야 함 (저장 성공 확인)
        assertThat(addCustomer.getCustomerName()).isEqualTo("스프링부트4"); // 9. 저장된 고객의 이름이 정말 "스프링부트"인지 확인
    }

    @Test
    void testFindBy() {
        // DB에서 ID가 1인 고객을 조회하고, 결과를 Optional 객체로 감싸서 반환합니다. (NullPointerException 방지 목적)
        Optional<Customer> optionalCustomer = customerRepository.findById(1L);

        // 1. isPresent() 사용: 객체 안에 실제 데이터가 들어있는지(null이 아닌지) 확인합니다.
        if(optionalCustomer.isPresent()) {
            // 데이터가 존재하므로 get()을 사용해 안전하게 Customer 객체를 꺼냅니다.
            Customer customer = optionalCustomer.get();
            // 꺼낸 객체의 ID가 2L과 같은지 검증합니다. (일반적으로 1L을 찾았으므로 이 테스트는 실패할 가능성이 높습니다)
            assertThat(customer.getId()).isEqualTo(1L);
        }
        else {
            // 데이터가 존재하지 않을 때(null일 때) 실행됩니다.
            System.out.println("Customer Not Found");
        }

        // 2. ifPresent()와 람다식 사용: 데이터가 존재할 때만 괄호 안의 동작을 실행합니다.
        // 람다식 해석: 꺼낸 객체를 'customer'라고 부르고(->) 그 고객의 이름을 출력해라.
        optionalCustomer.ifPresent(customer -> System.out.println(customer.getCustomerName()));

        //orElseGet(Supplier)
//Supplier의 추상메서드는 T get() (입력값은 없고 반환값만 있다는 뜻)

// DB에서 ID가 2인 고객을 조회합니다.
// 만약 고객이 존재하면 그 데이터를 그대로 'existCustomer'에 저장하고,
// 고객이 존재하지 않으면(null이면) 람다식 `() -> new Customer()`를 실행하여 새로운 빈 고객 객체를 만들어 저장합니다.
        Customer existCustomer = customerRepository.findById(2L).orElseGet(() -> new Customer());

// DB에서 고객을 찾지 못해 새로 만들어진 빈 객체라면, 아직 식별자(ID)가 부여되지 않아 null 상태일 것입니다.
// 따라서 ID가 null인지 검증합니다.
        assertThat(existCustomer.getId()).isNull();

// (참고) 만약 엔티티의 ID 타입이 기본형 `long`이었다면 기본값이 0이므로 0L과 비교하겠지만,
// 래퍼 클래스인 `Long`을 사용했기 때문에 null과 비교하는 것이 맞습니다.
//assertThat(existCustomer.getId()).isEqualTo(0L);
    }

    @Test
    void testFindByNotFound() {
        // 1. ID가 2인 고객 조회
        // 2. 결과가 없으면(null) orElseGet 작동 -> 빈 Customer 객체를 새로 생성하여 반환
        Customer existCustomer = customerRepository.findById(2L).orElseGet(() -> new Customer());
        //orElseGet(): 데이터가 없을 때만 작동하여 대체값을 마련해 줍니다.

        // 3. 새로 만들어진 빈 객체이므로 아직 ID가 없음을(null) 확인하여 테스트 통과
        assertThat(existCustomer.getId()).isNull();
    }
}