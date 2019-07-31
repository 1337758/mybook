package com.liulei.common.annotation;

import java.lang.annotation.*;

/**
 * 此注解不校验token
 *
 * @author
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Token {

}
