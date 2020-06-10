package com.example.demo.controller.v1;

import com.example.demo.entity.User;
import com.example.demo.repo.UserJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController // 결과값을 JSON으로 출력합니다.
@RequestMapping(value = "/v1")
public class UserController {
    private final UserJpaRepo userJpaRepo;

    @GetMapping(value = "/user")
    public List<User> findAllUser() {
        return userJpaRepo.findAll();
    }

    @PostMapping(value = "/user")
    public User save() {
        User user = User.builder()
                .uid("yumi@naver.com")
                .name("유미")
                .build();
        return userJpaRepo.save(user);
    }
}
/*
    @RequiredArgsConstructor
    class상단에 선언하면 class내부에 final로 선언된 객체에 대해서 Constructor Injection을 수행합니다. 해당 어노테이션을 사용하지 않고 선언된 객체에 @Autowired를 사용해도 됩니다.

    @RestController
    결과 데이터를 JSON으로 내보냅니다.

    @RequestMapping(value = “/v1”)
    api resource를 버전별로 관리하기 위해 /v1 을 모든 리소스 주소에 적용되도록 처리합니다.

    @GetMapping(value = “/user”)
    user테이블에 있는 데이터를 모두 읽어옵니다.
    데이터가 한개 이상일 수 있으므로 리턴 타입은 List<User>로 선언합니다.

    userJpaRepo.findAll()
    JPA를 사용하면 기본으로 CRUD에 대해서는 별다른 설정없이 쿼리를 질의할 수 있도록 메소드를 지원합니다. findAll()은 select msrl, name, uid from user; 쿼리를 내부적으로 실행시켜 줍니다.

    @PostMapping(value = “/user”)
    user테이블에 데이터를 1건 입력합니다. userJpaRepo.save(user); 역시 JPA에서 기본으로 제공하는 메소드입니다. user객체를 전달하면 다음과 같이 내부적으로 insert문을 실행시켜 줍니다.
    insert into user (msrl, name, uid) values (null, ?, ?)
 */