package com.example.project1.middleware;

import com.example.project1.middleware.annotation.Trim;
import com.example.project1.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
@Aspect
@Slf4j
public class TrimAspect {

    @Before("within(com.example.project1.module..*)")
    public void beforeMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
        Object[] params = joinPoint.getArgs();
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] annotArr = annotations[i];
            for (Annotation annot : annotArr) {
                if (annot instanceof Trim) {
                    Object paramTrim = params[i];
                    DataUtils.trimValues(paramTrim);
                    break;
                }
            }
        }
    }

}
