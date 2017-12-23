package com.yq.mybatis.jpa;

import com.yq.mybatis.jpa.builder.*;
import com.yq.mybatis.jpa.entity.Dialect;
import com.yq.mybatis.jpa.entity.SqlType;
import com.yq.mybatis.jpa.exception.MybatisJpaException;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询类型和sql生成器对应关系
 *
 * @author maoxiangdong
 * @version 1.0
 */
public class BuilderMap {
    private static Map<Dialect, Map<String, Class>> builderMap = new HashMap<Dialect, Map<String, Class>>();

    static {
        Map<String, Class> defMap = new HashMap<String, Class>();
        defMap.put(SqlType.SELECT.name(), Select.class);
        defMap.put(SqlType.DELETE.name(), Delete.class);
        defMap.put(SqlType.INSERT.name(), Insert.class);
        defMap.put(SqlType.UPDATE.name(), Update.class);
        defMap.put(SqlType.COUNT.name(), Count.class);
        defMap.put(SqlType.FUNCTION.name(), Function.class);
        defMap.put(SqlType.PROCEDURE.name(), Procedure.class);

        Map<String, Class> oracleMap = new HashMap<String, Class>();
        oracleMap.put(SqlType.INSERT.name(), OracleInsert.class);
        builderMap.put(Dialect.DEFAULT, defMap);
        builderMap.put(Dialect.ORACLE, oracleMap);
    }

    static Builder get(Dialect dialect, SqlType sqlType) {
        return get(dialect, sqlType.name());
    }

    static Builder get(Dialect dialect, String sqlType) {
        if (!builderMap.containsKey(dialect) || !builderMap.get(dialect).containsKey(sqlType)) {
            dialect = Dialect.DEFAULT;
        }
        Map<String, Class> builderMap = BuilderMap.builderMap.get(dialect);
        Class type = builderMap.get(sqlType);
        Builder builder = null;
        try {
            builder = (Builder) type.newInstance();
        } catch (InstantiationException e) {
            throw new MybatisJpaException("实例化生成器出错：" + type.getName(), e);
        } catch (IllegalAccessException e) {
            throw new MybatisJpaException("实例化生成器出错：" + type.getName(), e);
        }
        return builder;
    }

    static void add(Dialect dialect, String sqlType, Class builder) {
        Map<String, Class> map = builderMap.get(dialect);
        if (map == null) {
            map = new HashMap<String, Class>();
        }
        map.put(sqlType, builder);
        BuilderMap.builderMap.put(dialect, map);
    }

    static void add(String sqlType, Class builder) {
        add(Dialect.DEFAULT, sqlType, builder);
    }
}
