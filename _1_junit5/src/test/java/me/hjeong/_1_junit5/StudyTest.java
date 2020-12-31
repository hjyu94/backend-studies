package me.hjeong._1_junit5;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.time.Duration;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("@BeforeAll");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("@AfterAll");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("@BeforeEach");
    }

    @AfterEach
    void afterEach() {
        System.out.println("@AfterEach");
    }

    @Test
    void create_new_study() {
        Study study = new Study();
        assertNotNull(study);

        // 1)
        assertEquals(StudyStatus.DRAFT, study.getStatus(), new Supplier<String>() {
            @Override
            public String get() {
                return "스터디를 처음 만들면 상태값이 DRAFT 여야 한다.";
            }
        });
        // 2)
        assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태값이 DRAFT 여야 한다.");
        // 3)
        assertEquals(StudyStatus.DRAFT, study.getStatus(), "스터디를 처음 만들면 상태값이 DRAFT 여야 한다.");

        // 2번처럼 쓰는 것이 성능상 이점이다. 람다식의 실행부분은 assertEquals() 가 실패했을 때만 실행되기 때문에
        // 문자열을 만드는 데 시간이 꽤 소요되는 연산인 경우 2번처럼 적어주는게 좋다.

        System.out.println("create");
    }

    @Test
    void create_new_study_again() {
        Study study = new Study(-10);

        // multiple failures 체크 가능
        assertAll(
                () -> assertNotNull(study),
                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(),
                        () -> "스터디를 처음 만들면 상태값이 " + StudyStatus.DRAFT + " 여야 한다."),
                () -> assertTrue(study.getLimit() > 0, " 스터디 최대 참석 가능 인원은 0보다 커야 한다.")
        );
    }

    @Test
    @DisplayName("스터디 만들기")
    void create2() {
        System.out.println("create2");
    }

    @Test
    void check_throw_exception() {
        // executable 을 실행했을 때 expectedType 의 에러가 발생하는지 테스
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        String message = exception.getMessage();
        assertEquals("limit은 0보다 커야 합니다.", message);
    }

    @Test
    void test_timeout() {
        // 뒤의 executable 파라미터를 전부 실행. 테스트 실행 시간 317ms
        assertTimeout(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(300);
        });
    }

    @Test
    void test_timeout_Preemptively() {
        // timeout 값 동안만 뒤의 executable 파라미터를 실행. 테스트 실행 시간 128ms
        // db transaction 경우등에 사용하면 롤백이 정상적으로 안 되는 경우가 있을 수 있다.
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(300);
        });
    }

    @Test
    @DisplayName("시스템 환경변수값이 LOCAL일 때만 아래 코드를 실행하는 테스트 - @Disabled 와 비슷")
    void assume_true() {
        String test_env = System.getenv("TEST_ENV");
        System.out.println("test_env = " + test_env);
        assumeTrue("LOCAL".equalsIgnoreCase(test_env));

        Study study = new Study(10);
        assertNull(study);
    }

    @Test
    void assuming_that() {
        String test_env = System.getenv("TEST_ENV");
        assumingThat("LOCAL".equalsIgnoreCase(test_env), () -> {
            Study study = new Study(10);
            // ...
        });
        assumingThat("hjeong".equalsIgnoreCase(test_env), () -> {
            Study study = new Study(20);
            // ...
        });
    }

    @Test
    @DisabledOnOs({OS.MAC, OS.LINUX})
    void test_is_disabled_if_os_is_mac_or_linux() {
        System.out.println("DisabledOnOs: MAC, LINUX");

        Study study = new Study(10);
        assertNull(study);
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void test_is_enabled_if_os_is_ma() {
        System.out.println("EnabledOnOs: MAC");

        Study study = new Study(10);
        assertNull(study);
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void test_is_enabled_on_java_8() {
        System.out.println("java 8 !");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
    // matches = 정규표현식 값
    void if_environment_var() {
        // ...
    }

    @Test
    @Tag("slow")
    void slow_test() {}

    @Test
    @Tag("fast")
    void fast_test() {}
}