package com.example.project1.middleware.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Nested {

    boolean isCollection() default false;

}
