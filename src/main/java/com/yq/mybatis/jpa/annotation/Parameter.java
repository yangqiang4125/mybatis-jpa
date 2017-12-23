package com.yq.mybatis.jpa.annotation;

import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.type.JdbcType;

import java.lang.annotation.*;

/**
 * Created by maoxiaodong on 2016/11/23.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Parameter {
    String name();
    Class type() default String.class;
    JdbcType jdbcType() default JdbcType.VARCHAR;
    ParameterMode mode() default ParameterMode.IN;
}
