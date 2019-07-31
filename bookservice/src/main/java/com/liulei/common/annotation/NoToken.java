package com.liulei.common.annotation;

import java.lang.annotation.*;

/**
 * @description: 此注解不校验token
 * @Author: runze
 * @Date: 2019/7/26 16:55
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface NoToken {

}
