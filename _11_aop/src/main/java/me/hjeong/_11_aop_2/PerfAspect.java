package me.hjeong._11_aop_2;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerfAspect {
    public Object logPerf(ProceedingJoinPoint pip) throws Throwable {
        Object retVal = pip.proceed();
        return retVal;
    }
}
