package com.example.project1.middleware;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;

@Component
@Aspect
@Slf4j
public class TrimAndValidAspect {

    @Before("within(com.example.project1.module..*)")
    public void beforeMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
        Object[] params = joinPoint.getArgs();
        Object validObject = null;
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] annotationArr = annotations[i];
            for (Annotation annotation : annotationArr) {
                if (annotation instanceof TrimAndValid) {
                    validObject = params[i];
                    DataUtils.trimNested(validObject);
                    break;
                }
            }
        }
        if (validObject != null) {
            List<String> messageErrors = DataUtils.validate(validObject);
            if (!messageErrors.isEmpty()) {
                throw new ValidateException(Translator.toMessage(messageErrors.get(0)));
            }
        }
    }

}
