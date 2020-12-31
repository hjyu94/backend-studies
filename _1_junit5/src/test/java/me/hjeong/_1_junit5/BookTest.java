package me.hjeong._1_junit5;

import org.junit.jupiter.api.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookTest {

    int value = 0;

    @Test
    @Order(1)
    void test_instance_1() {
        value++;
        System.out.println("value = " + value);
        System.out.println("this = " + this);
    }

    @RepeatedTest(10)
    @Order(2)
    void test_instance_2() {
        value++;
        System.out.println("value = " + value);
        System.out.println("this = " + this);
    }

}