package com.mybatis.interceptor.annotation;


import java.lang.annotation.*;

/**
 * 自定义排序注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderBy {


    /**
     * 表的别名
     */
    String tableAlias() default "";

    /**
     * 排序字段
     */
    String orderColumn() default "";


    /**
     * ASC/DESC 默认倒序
     * @return
     */
    boolean isAsc() default false;

}
