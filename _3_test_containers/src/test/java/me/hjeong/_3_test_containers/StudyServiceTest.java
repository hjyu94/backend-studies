package me.hjeong._3_test_containers;

import me.hjeong._3_test_containers.domain.Member;
import me.hjeong._3_test_containers.domain.Study;
import me.hjeong._3_test_containers.member.MemberService;
import me.hjeong._3_test_containers.study.StudyRepository;
import me.hjeong._3_test_containers.study.StudyService;
import me.hjeong._3_test_containers.study.StudyStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/*
    @SpringBootTest
    @ExtendWith(MockitoExtension.class)
    @ActiveProfiles("test")
    @Testcontainers
    class StudyServiceTest {

        // @Autowired, @Mock

        static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest")
                .withDatabaseName("studytest"); // static 으로 만들어야 여러 테스트에서 공용으로 이 컨테이너를 사용한다.

        @BeforeAll
        static void beforeAll() {
            postgreSQLContainer.start();
        }

        @AfterAll
        static void fterAll() {
            postgreSQLContainer.stop();
        }

    }

    위 코드의 @BeforeAll, @AfterAll 에서 컨테이너를 시작했다가 중지했다가 하는 일 대신에
    @Testcontainers, @Container 로 대신할 수 있다.
*/

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
@ContextConfiguration(initializers = StudyServiceTest.ContainerPropertyInitializer.class)
class StudyServiceTest {

    static Logger LOGGER = LoggerFactory.getLogger(StudyServiceTest.class); // Lombok annotation - Slf4j

    @Mock MemberService memberService;

    @Autowired StudyRepository studyRepository;

    @Autowired Environment environment;

    @Value("${container.port}") int port;

    @Container
    static DockerComposeContainer composeContainer =
            new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("study-db", 5432);
//    static GenericContainer postgreSQLContainer = new GenericContainer("postgres:latest")
//            .withExposedPorts(5432) // host 에 매핑되는 port 는 랜덤. -p <host-random-port>:5432
//            .withEnv("POSTGRES_DB", "studytest");

    @BeforeAll
    static void beforeAll() {
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOGGER);
    }

    @BeforeEach
    void beforeEach() {
        String property = environment.getProperty("container.port");
        studyRepository.deleteAll();
    }

    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "container.port=" + composeContainer.getServicePort("study-db", 5432)
            ).applyTo((applicationContext.getEnvironment()));
        }
    }

    @Test
    void createNewStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("keesun@email.com");

        Study study = new Study(10, "테스트");

        given(memberService.findById(1L)).willReturn(Optional.of(member));

        // When
        studyService.createNewStudy(1L, study);

        // Then
        assertEquals(1L, study.getOwnerId());
        then(memberService).should(times(1)).notify(study);
        then(memberService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    @Test
    void openStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "더 자바, 테스트");
        assertNull(study.getOpenedDateTime());

        // When
        studyService.openStudy(study);

        // Then
        assertEquals(StudyStatus.OPENED, study.getStatus());
        assertNotNull(study.getOpenedDateTime());
        then(memberService).should().notify(study);
    }

}