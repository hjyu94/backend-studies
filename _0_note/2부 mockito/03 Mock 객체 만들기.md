## 03 Mock 객체 만들기

StudyService 는 StudyRepository, MemberService 를 필드로 들고 있고 생성자에서도 사용된다.
(StudyService 는 위의 둘에 의존적인 상태)

그런데 둘 다 인터페이스만 있고 구현체가 없는데 StudyService 를 테스트해야 한다.
이럴 때 Mock 을 사용하면 좋다.

Mockito.mock() 메소드로 만드는 방법
--
```
    MemberService memberService = mock(MemberService.class);
    StudyRepository studyRepository = mock(StudyRepository.class);
```

@Mock 애노테이션으로 만드는 방법
--
- JUnit 5 extension으로 MockitoExtension을 사용해야 한다.   
- 필드   
- 메소드 매개변수   
```
@ExtendWith(MockitoExtension.class)
class StudyServiceTest {
    @Mock MemberService memberService;
    @Mock StudyRepository studyRepository;
}
```

```
@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Test
    void createStudyService(@Mock MemberService memberService,
                            @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);
    }

}
```