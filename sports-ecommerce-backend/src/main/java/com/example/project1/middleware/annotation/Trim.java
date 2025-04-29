package com.example.project1.middleware.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Trim {

}
