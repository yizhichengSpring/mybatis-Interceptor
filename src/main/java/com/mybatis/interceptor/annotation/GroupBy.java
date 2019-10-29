package com.mybatis.interceptor.annotation;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GroupBy {

    /**
     * 表的别名
     */
    String tableAlias() default "";


    /**
     * 分组字段
     */
    String column() default "id";
}
