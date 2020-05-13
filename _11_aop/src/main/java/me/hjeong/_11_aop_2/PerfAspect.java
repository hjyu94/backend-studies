package me.hjeong._11_aop_2;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerfAspect {
    public Object logPerf(ProceedingJoinPoint pip) throws Throwable {
        long begin = System.currentTimeMillis(); // advise
        Object retVal = pip.proceed();
        System.out.println(System.currentTimeMillis() - begin + "ms passed"); // advise
        return retVal;
    }
}
