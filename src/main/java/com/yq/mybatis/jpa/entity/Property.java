package com.yq.mybatis.jpa.entity;

import javax.persistence.*;
import java.lang.reflect.Field;

/**
 * Created by maoxiaodong on 2016/11/15.
 */
public class Property {
    private String name;
    private String column;
    private Class type;
    private boolean isId = false;
    private boolean isSort = false;
    private boolean autoGenerate = false;
    private String sortOrder;
    private GenerationType strategy;
    private String generator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public boolean isId() {
        return isId;
    }

    public void setId(boolean id) {
        isId = id;
    }

    public boolean isSort() {
        return isSort;
    }

    public boolean isAutoGenerate() {
        return autoGenerate;
    }

    public void setAutoGenerate(boolean autoGenerate) {
        this.autoGenerate = autoGenerate;
    }

    public void setSort(boolean sort) {
        isSort = sort;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public GenerationType getStrategy() {
        return strategy;
    }

    public void setStrategy(GenerationType strategy) {
        this.strategy = strategy;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public static boolean is(Field field) {
        return !field.isAnnotationPresent(Transient.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        return name.equals(property.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
