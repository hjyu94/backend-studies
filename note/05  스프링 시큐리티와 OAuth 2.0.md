### 스프링 1.5 vs 스프링 2.0
- Oauth2 연동 방법이 2.0 에서 크게 변경되었지만 설정 방법은 크게 차이가 없는 경우가 많은데
- `spring-security-oauth2-autoconfigure` 라이브러리 덕분에 스프링부트 2에성도 1.5에서 쓰던 설정을 그대로 사용 가능

### 세션 저장소
1. 내장 톰캣의 메모리를 이용
    - 애플리케이션을 재실행하면 로그인이 풀린다.
    - WAS(Web application server)의 메모리에 세션을 저장하는 것
    - 2대 이상의 서버에서 서비스하고 있다면 톰캣마다 세션 동기화 설정을 해야 한다
2. 데이터베이스를 세션 저장소로 이용
    - 여러 WAS 간 공용 세션을 사용할 수 있는 가장 쉬운 방법
    - 로그인 요청마다 DB IO가 발생하여 성능상 이슈가 발생할 수 있다.
    - 로그인 요청이 많이 없는 백 오피스, 사내 시스템 용도에서 사용한다.
3. 메모리 DB 를 세션 저장소로 사용한다.
    - Redis, Memcached 등을 이용하는 방법
    - B2C 서비스에서 가장 많이 사용하는 방식
    - 실제 서비스로 사용하기 위해서는 Embedded Redis 와 같은 방식이 아닌 외부 메모리 서버가 필요하다.

### 네이버 로그인
https://developers.naver.com/apps/#/register?api=nvlogin

### 테스트
- CustomOAuth2UserService 를 찾을 수 없음
    - CustomOAuth2UserService 를 생성하는데 필요한 소셜 로그인 관련 설정값들이 없어서 생긴 에러
    - src/test/resources/application.properties 를 생성하여 가짜 설정값을 넣어준다
- 302 Status Code
    - 인증되지 않은 사용자의 요청은 스프링 시큐리티가 리다이렉션 시킨다.
    - @WithMockUser 를 사용하여 인증을 추가한다.
        - mockmvc 에서만 사용 가능한 방법
- @WebMvcTest 에서 CustomOAuth2UserService 를 찾을 수 없음
    - @WebMvcTest 는 WebSecurityConfigurerAdapter, WebMvcConfigurer, @ControllerAdvice, @Controller 를 읽는다
    - @Repository, @Service, @Component 는 스캔 대상이 아니다.
    - 따라서 SecurityConfig 는 읽었는데 이를 생성하기 위해 필요한 CustomOAuth2UserService 를 읽을 수 없어서 생기는 에러
    - 스캔 대상에서 SecurityConfig 를 제거한다.
- At least one JPA metamodel must be present!
    - @EnableJpaAuditing 을 사용하기 위해선 최소 하나의 @Entity 클래스가 필요하다.
    - @WebMvcTest 는 엔티티를 읽지 않는다.
    - @EnableJpaAuditing 을 @SpringBootApplication 과 분리한다.
    - @WebMvcTest 는 일반적인 @Configuration 을 스캔하지 않기 때문에 JpaAuditing 기능을 사용하지 않게 된다.
