package com.yq.mybatis.jpa.entity;

import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.type.JdbcType;

/**
 * Created by maoxiaodong on 2016/11/12.
 */
public class TypeSketch {
    private String name;
    private Class rawType;
    private Class ownerType;
    private JdbcType jdbcType;
    private ParameterMode mode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getRawType() {
        return rawType;
    }

    public void setRawType(Class rawType) {
        this.rawType = rawType;
    }

    public Class getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Class ownerType) {
        if (ownerType == null) {
            if (rawType.isArray()) {
                ownerType = Object.class;
            } else {
                ownerType = rawType;
            }
        }
        this.ownerType = ownerType;
    }

    public ParameterMode getMode() {
        return mode;
    }

    public void setMode(ParameterMode mode) {
        this.mode = mode;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeSketch that = (TypeSketch) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
