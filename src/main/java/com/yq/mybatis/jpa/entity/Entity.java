package com.yq.mybatis.jpa.entity;

import com.yq.mybatis.jpa.exception.MybatisJpaException;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by maoxiaodong on 2016/11/15.
 */
public class Entity {
    private Class type;
    private String table;
    private Set<Property> properties = new HashSet<Property>();

    public Entity() {
    }

    public Entity(Class type) {
        this.type = type;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Collection<Property> getProperties() {
        return properties;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public Property getProperty(String name) {
        for (Property property : properties) {
            if (property.getName().equals(name)) {
                return property;
            }
        }
        throw new MybatisJpaException("实体：" + type.getName() + "没有属性：" + name);
    }

    public Property getId() {
        for (Property property : properties) {
            if (property.isId()) {
                return property;
            }
        }
        return null;
    }

    public Property getSort() {
        for (Property property : properties) {
            if (property.isSort()) {
                return property;
            }
        }
        return null;
    }

    public static boolean is(Class type) {
        return type != null && type.isAnnotationPresent(Table.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return type.equals(entity.type);

    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
