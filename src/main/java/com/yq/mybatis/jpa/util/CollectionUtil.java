package com.yq.mybatis.jpa.util;

import java.util.*;

/**
 * Created by maoxiaodong on 2016/11/13.
 */
public class CollectionUtil {
    public static boolean isCollection(Class type) {
        return type != null && Collection.class.isAssignableFrom(type);
    }

    public static Collection<Object> toCollection(Object parameter) {
        Object parameters = null;
        if (parameter instanceof Collection) {
            parameters = parameter;
        } else if (parameter instanceof Map) {
            Map parameterMap = (Map) parameter;
            if (parameterMap.containsKey("collection")) {
                parameters = parameterMap.get("collection");
            } else if (parameterMap.containsKey("list")) {
                parameters = parameterMap.get("list");
            } else if (parameterMap.containsKey("array")) {
                parameters = Arrays.asList((Object[]) (parameterMap.get("array")));
            }
        }

        if (parameters == null) {
            parameters = new ArrayList();
            ((Collection) parameters).add(parameter);
        }

        return (Collection) parameters;
    }
}
