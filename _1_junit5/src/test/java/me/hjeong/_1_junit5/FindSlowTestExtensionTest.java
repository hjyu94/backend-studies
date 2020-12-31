package me.hjeong._1_junit5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FindSlowTestExtension.class)
public class FindSlowTestExtensionTest {

    @Test
    void test1() throws InterruptedException {
        Thread.sleep(1005L);
    }

    @Test
    @SlowTest
    void test2() throws InterruptedException {
        Thread.sleep(1005L);
    }

    @Test
    @FastTest
    void test3() throws InterruptedException {
        Thread.sleep(1005L);
    }

}
