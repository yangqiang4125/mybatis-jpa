package com.yq.mybatis.jpa.builder;

import com.yq.mybatis.jpa.Definition;

/**
 * Created by maoxiaodong on 2016/11/12.
 */
public class CommonParser {

    public SqlFractional parse(String name) {
        SqlFractional sqlFractional = new SqlFractional();
        String prefix = Definition.getPrefix(name);

        int by = name.indexOf(Definition.BY);
        int group = name.indexOf(Definition.GROUP_BY);
        int order = name.indexOf(Definition.ORDER_BY);
        by = getBy(by, group, order);

        String columns = getColumns(name, prefix, by, group, order);
        String whereClause = getWhereClause(name, prefix + columns, by, group, order);
        String groupClause = getGroupClause(name, group, order);
        String orderClause = getOrderClause(name, order);
        sqlFractional.setProperty(columns);
        sqlFractional.setWhereClause(whereClause);
        sqlFractional.setGroupByProperty(groupClause);
        sqlFractional.setOrderByClause(orderClause);
        return sqlFractional;
    }

    private int getBy(int by, int group, int order) {
        if ((group < by && group > -1) || (order < by && order > -1)) {
            by = -1;
        }
        return by;
    }

    private String getColumns(String name, String prefix, int by, int group, int order) {
        int begin = prefix.length();
        int end = name.length();
        if (by == -1) {
            if (group > -1) {
                end = group;
            } else if (order > -1) {
                end = order;
            }
        } else {
            end = by;
        }
        return name.substring(begin, end);
    }

    private String getWhereClause(String name, String prefix, int by, int group, int order) {
        int begin = prefix.length();
        if (by > -1) {
            begin += Definition.BY.length();
        } else {
            return "";
        }

        int end = name.length();
        if (group == -1) {
            if (order > -1) {
                end = order;
            }
        } else {
            end = group;
        }
        return name.substring(begin, end);
    }

    private String getGroupClause(String name, int group, int order) {
        if (group == -1) {
            return "";
        }
        int begin = name.indexOf(Definition.GROUP_BY) + Definition.GROUP_BY.length();
        int end = name.length();
        if (order > -1) {
            end = order;
        }
        return name.substring(begin, end);
    }

    private String getOrderClause(String name, int order) {
        if (order == -1) {
            return "";
        }
        int begin = name.indexOf(Definition.ORDER_BY) + Definition.ORDER_BY.length();

        int end = name.length();
        return name.substring(begin, end);
    }
}
