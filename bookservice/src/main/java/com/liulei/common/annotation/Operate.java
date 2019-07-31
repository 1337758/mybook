package com.liulei.common.annotation;

import java.lang.annotation.*;

/**
 * @description: 操作日志记录注解
 * @Author: runze
 * @Date: 2019/7/26 16:56
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Operate {

    public String value();
}
