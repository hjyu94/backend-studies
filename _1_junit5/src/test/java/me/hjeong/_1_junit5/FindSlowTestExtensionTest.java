package me.hjeong._1_junit5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

//@ExtendWith(FindSlowTestExtension.class)
public class FindSlowTestExtensionTest {

    @RegisterExtension
    static FindSlowTestExtension findSlowTestExtension =
            new FindSlowTestExtension(1000L);

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
