package com.lxh.library.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckLogin {
    int[] value();

    CheckType[] checkType() default CheckType.LOGIN;
}
