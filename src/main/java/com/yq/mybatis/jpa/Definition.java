package com.yq.mybatis.jpa;

import com.yq.mybatis.jpa.entity.Dialect;
import com.yq.mybatis.jpa.entity.SqlType;
import com.yq.mybatis.jpa.util.StrUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 定义解析片段
 *
 * @author maoxiangdong
 * @version 1.0
 */
public class Definition {
    public static final String BY = "By";
    public static final String AND = "And";
    public static final String OR = "Or";
    public static final String LIKE = "Like";
    public static final String NOT_LIKE = "NotLike";
    public static final String IN = "In";
    public static final String NOT_IN = "NotIn";
    public static final String NULL = "Null";
    public static final String NOT_NULL = "NotNull";
    public static final String GREATER = "Greater";
    public static final String LESS = "Less";
    public static final String ORDER_BY = "OrderBy";
    public static final String GROUP_BY = "GroupBy";
    public static final String EQUAL = "Equal";
    public static final String NOT_EQUAL = "NotEqual";

    private static Map<String, String> sqlTypeMap = new HashMap<String, String>();
    private static Set<String> keywordSet = new HashSet<String>();
    private static Set<String> expressions = new HashSet<String>();
    private static Map<String, String> expressionMap = new HashMap<String, String>();
    private static Set<String> sortSet = new HashSet<String>();
    private static Map<Dialect, String> noTableSelect = new HashMap<Dialect, String>();

    static {
        sqlTypeMap.put("find", SqlType.SELECT.name());
        sqlTypeMap.put("get", SqlType.SELECT.name());
        sqlTypeMap.put("query", SqlType.SELECT.name());
        sqlTypeMap.put("select", SqlType.SELECT.name());
        sqlTypeMap.put("set", SqlType.UPDATE.name());
        sqlTypeMap.put("update", SqlType.UPDATE.name());
        sqlTypeMap.put("add", SqlType.INSERT.name());
        sqlTypeMap.put("insert", SqlType.INSERT.name());
        sqlTypeMap.put("remove", SqlType.DELETE.name());
        sqlTypeMap.put("delete", SqlType.DELETE.name());
        sqlTypeMap.put("count", SqlType.COUNT.name());
        sqlTypeMap.put("func", SqlType.FUNCTION.name());
        sqlTypeMap.put("call", SqlType.PROCEDURE.name());
        expressions.add(LIKE);
        expressions.add(NOT_LIKE);
        expressions.add(NULL);
        expressions.add(NOT_NULL);
        expressions.add(IN);
        expressions.add(NOT_IN);
        expressions.add(EQUAL);
        expressions.add(NOT_EQUAL);
        expressions.add(GREATER);
        expressions.add(LESS);

        expressionMap.put(LIKE, "like");
        expressionMap.put(NOT_LIKE, "not like");
        expressionMap.put(NULL, "is null");
        expressionMap.put(NOT_NULL, "is not null");
        expressionMap.put(EQUAL, "=");
        expressionMap.put(NOT_EQUAL, "!=");
        expressionMap.put(IN, "in");
        expressionMap.put(NOT_IN, "not in");
        expressionMap.put(GREATER, ">");
        expressionMap.put(LESS, "<");

        sortSet.add("Desc");
        sortSet.add("Asc");

        keywordSet.addAll(sqlTypeMap.keySet());
        keywordSet.add(BY);
        keywordSet.add(AND);
        keywordSet.add(OR);
        keywordSet.add(BY);
        keywordSet.add(ORDER_BY);
        keywordSet.add(GROUP_BY);
        keywordSet.addAll(expressions);
        keywordSet.add("All");
        keywordSet.add("One");
        keywordSet.add("List");
        keywordSet.add("Set");
        keywordSet.add("Batch");

        noTableSelect.put(Dialect.DEFAULT, "select %s");
        noTableSelect.put(Dialect.ORACLE, "select %s from dual");

    }

    public static String getNoTableSelect(Dialect dialect, String sql) {
        if (dialect == null || !noTableSelect.containsKey(dialect)) {
            dialect = Dialect.DEFAULT;
        }
        return String.format(noTableSelect.get(dialect), sql);
    }

    public static Set<String> getSortSet() {
        return sortSet;
    }

    public static Set<String> getExpressions() {
        return expressions;
    }

    public static String getSqlType(String name) {
        Set<String> keys = sqlTypeMap.keySet();
        for (String key : keys) {
            if (name.startsWith(key)) {
                return sqlTypeMap.get(key);
            }
        }
        return null;
    }

    public static String getPrefix(String name) {
        Set<String> keys = sqlTypeMap.keySet();
        for (String key : keys) {
            if (name.startsWith(key)) {
                return key;
            }
        }
        return null;
    }

    public static String getExpression(String name) {
        if (StrUtil.isEmpty(name)) {
            return expressionMap.get(EQUAL);
        }
        return expressionMap.get(name);
    }

    public static void addSqlTypeMap(String prefix, String sqlType) {
        sqlTypeMap.put(prefix, sqlType);
    }

    public static void addSqlTypeMap(String prefix, SqlType sqlType) {
        addSqlTypeMap(prefix, sqlType.name());
    }

    public static void expand(String prefix, String sqlType, Class builder) {
        addSqlTypeMap(prefix, sqlType);
        BuilderMap.add(sqlType, builder);
    }

    public static boolean isKeyWord(String name){
        return keywordSet.contains(name);
    }
}
