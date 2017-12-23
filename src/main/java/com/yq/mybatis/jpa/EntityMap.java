package com.yq.mybatis.jpa;

import com.yq.mybatis.jpa.entity.Entity;
import com.yq.mybatis.jpa.entity.Property;
import com.yq.mybatis.jpa.exception.MybatisJpaException;
import com.yq.mybatis.jpa.util.StrUtil;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 将实体与表建立对应关系
 *
 * @author maoxiangdong
 * @version 1.0
 */
public class EntityMap {
    private static Set<Entity> entitySet = new HashSet<Entity>();

    public static Entity get(Class c) {
        if (entitySet.contains(c)) {
            for (Entity entity : entitySet) {
                if (entity.equals(c)) {
                    return entity;
                }
            }
        } else {
            Entity entity = mapping(c);
            entitySet.add(entity);
            return entity;
        }

        return null;
    }

    private static Entity mapping(Class type) {
        if (Entity.is(type)) {
            Entity entity = new Entity(type);
            Table table = (Table) type.getAnnotation(Table.class);
            entity.setTable(table.name().toLowerCase());
            if (StrUtil.isEmpty(entity.getTable())) {
                entity.setTable(StrUtil.classToTableName(type));
            }

            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                if (Property.is(field)) {
                    Property property = mappingProperty(field);
                    entity.addProperty(property);
                }
            }
            return entity;
        } else {
            throw new MybatisJpaException("不是实体类：" + type.getName());
        }
    }

    private static Property mappingProperty(Field field) {
        Property property = new Property();
        property.setName(field.getName());
        property.setType(field.getType());
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            property.setColumn(column.name());
        } else {
            property.setColumn(StrUtil.camelToUnderline(field.getName()));
        }
        if (field.isAnnotationPresent(Id.class)) {
            property.setId(true);
        }

        if (field.isAnnotationPresent(OrderBy.class)) {
            OrderBy orderBy = field.getAnnotation(OrderBy.class);
            property.setSort(true);
            property.setSortOrder(orderBy.value());
        }

        if (field.isAnnotationPresent(GeneratedValue.class)) {
            GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
            property.setAutoGenerate(true);
            property.setStrategy(generatedValue.strategy());
            property.setGenerator(generatedValue.generator());
        }

        return property;
    }

}
