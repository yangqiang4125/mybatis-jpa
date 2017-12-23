package com.yq.mybatis.jpa.entity;

/**
 * Created by maoxiaodong on 2016/11/9.
 */
public enum SqlType {
    SELECT, INSERT, UPDATE, DELETE, COUNT, FUNCTION, PROCEDURE;

    public static SqlType get(String name) {
        return SqlType.valueOf(name);
    }
}
