package com.mybatis.interceptor.annotation;

import java.lang.annotation.*;

/**
 * 自定义分页注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limit {

    /**
     * 当前页面
     * @return
     */
    int page() default 0;


    /**
     * 每页显示数量
     * @return
     */
    int pageSize() default 10;



}
