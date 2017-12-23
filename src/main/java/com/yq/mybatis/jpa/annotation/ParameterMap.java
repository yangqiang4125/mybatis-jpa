package com.yq.mybatis.jpa.annotation;

import java.lang.annotation.*;

/**
 * Created by maoxiaodong on 2016/11/23.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ParameterMap {
    Parameter[] value();
}
