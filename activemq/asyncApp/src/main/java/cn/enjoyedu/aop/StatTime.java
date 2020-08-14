package cn.enjoyedu.aop;

import cn.enjoyedu.vo.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 类说明：统计方法执行时间的Aop实现
 */
@Aspect
@Service
public class StatTime {

    private User user;

    public StatTime() {
        System.out.println("************Aop开启");
    }

    @Pointcut("execution(* cn.enjoyedu.service.reg.impl.*.*Register(..))")
    public void stat() {
    }

    @Around("stat()&&args(user)")
    public Object statTime(ProceedingJoinPoint proceedingJoinPoint, User user) {
        this.user = user;
        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed(new Object[]{this.user});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("************spend time : " + (System.currentTimeMillis() - start) + "ms");
        return result;

    }

}
