package com.yq.mybatis.jpa.util;

import com.yq.mybatis.jpa.Mapper;
import com.yq.mybatis.jpa.annotation.Parameter;
import com.yq.mybatis.jpa.annotation.ParameterMap;
import com.yq.mybatis.jpa.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.springframework.core.MethodParameter;

import javax.persistence.Table;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoxiaodong on 2016/11/9.
 */
public class ReflectUtil {

    public static Class getFirstParameterizedType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypes = parameterizedType.getActualTypeArguments();
            if (actualTypes.length == 1) {
                Type actualType = actualTypes[0];
                if (actualType instanceof Class) {
                    return (Class) actualType;
                } else if (actualType instanceof TypeVariable) {
                    TypeVariable typeVariable = (TypeVariable) actualType;
                }
            }

        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type;
            Type arrayType = genericArrayType.getGenericComponentType();
            return getFirstParameterizedType(arrayType);
        }
        return null;
    }

    public static TypeSketch getReturnType(Method method, Class mapperInterface) {
        TypeSketch typeSketch = new TypeSketch();
        typeSketch.setRawType(method.getReturnType());
        Type type = TypeParameterResolver.resolveReturnType(method, mapperInterface);
        if (type instanceof Class) {
            typeSketch.setOwnerType((Class) type);
        } else {
            Class ownerType = getFirstParameterizedType(type);
            typeSketch.setOwnerType(ownerType);
        }

        return typeSketch;
    }

    public static List<TypeSketch> getParameters(Method method, Class mapperInterface) {
        List<TypeSketch> typeSketchSet = new ArrayList<TypeSketch>(method.getParameterTypes().length);
        if(method.isAnnotationPresent(ParameterMap.class)){
            ParameterMap parameterMap = method.getAnnotation(ParameterMap.class);
            for(Parameter parameter:parameterMap.value()){
                TypeSketch typeSketch = new TypeSketch();
                typeSketch.setName(parameter.name());
                typeSketch.setRawType(parameter.type());
                typeSketch.setMode(parameter.mode());
                typeSketch.setJdbcType(parameter.jdbcType());
                typeSketchSet.add(typeSketch);
            }
        }else{
            for (int i = 0; i < method.getParameterTypes().length; i++) {
                TypeSketch typeSketch = new TypeSketch();
                MethodParameter parameter = new MethodParameter(method,i);
                Param param = parameter.getParameterAnnotation(Param.class);
                Class rawType = parameter.getParameterType();
                String name;
                if (param != null) {
                    name = param.value();
                } else {
                    if (CollectionUtil.isCollection(rawType)) {
                        name = "collection";
                    } else if (rawType.isArray()) {
                        name = "array";
                    } else {
                        name = i + "";
                    }
                }

                typeSketch.setRawType(rawType);
                typeSketch.setName(name);
                Type[] types = TypeParameterResolver.resolveParamTypes(method, mapperInterface);
                if (types.length > 0) {
                    Class ownerType = getFirstParameterizedType(types[0]);
                    typeSketch.setOwnerType(ownerType);
                }
                typeSketchSet.add(typeSketch);
            }
        }

        return typeSketchSet;
    }

    public static Class getEntityTypeByMapper(Class mapperInterface) {
        Type[] types = mapperInterface.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class rawType = (Class) parameterizedType.getRawType();
                if (Mapper.class.isAssignableFrom(rawType)
                        && Mapper.class != rawType) {
                    Class entityType = (Class) parameterizedType.getActualTypeArguments()[0];
                    if (entityType.isAnnotationPresent(Table.class)) {
                        return entityType;
                    }
                }
            }

        }
        return null;
    }
}
