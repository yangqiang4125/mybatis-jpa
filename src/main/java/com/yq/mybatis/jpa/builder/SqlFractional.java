package com.yq.mybatis.jpa.builder;

import com.yq.mybatis.jpa.Definition;
import com.yq.mybatis.jpa.entity.ByProperty;
import com.yq.mybatis.jpa.util.StrUtil;

import java.util.*;

/**
 * Created by maoxiaodong on 2016/11/9.
 */
public class SqlFractional {
    private Set<String> property = new HashSet<String>();
    private List<ByProperty> whereClauses = new ArrayList<ByProperty>();
    private Set<ByProperty> orderByClause = new HashSet<ByProperty>();
    private String groupByProperty = "";


    public Set<String> getProperty() {
        return property;
    }

    public void setProperty(String property) {
        if (StrUtil.isNotEmpty(property)) {
            String[] propertys = property.split(Definition.AND);
            for (String c : propertys) {
                if(!Definition.isKeyWord(c)){
                    this.property.add(StrUtil.firstLowerCase(c));
                }
            }
        }

    }

    public List<ByProperty> getWhereClauses() {
        return whereClauses;
    }

    public void setWhereClause(String whereClauses) {
        if (StrUtil.isNotEmpty(whereClauses)) {
            String[] andClauses = whereClauses.split(Definition.AND);
            for (String clause : andClauses) {
                String[] clauses = clause.split(Definition.OR);
                ByProperty byProperty = new ByProperty("and", clauses[0], Definition.getExpressions());
                this.whereClauses.add(byProperty);
                for (int i = 1; i < clauses.length; i++) {
                    ByProperty orByProperty = new ByProperty("or", clauses[i], Definition.getExpressions());
                    this.whereClauses.add(orByProperty);
                }
            }
        }

    }

    public Set<ByProperty> getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        if (StrUtil.isNotEmpty(orderByClause)) {
            ByProperty byProperty = new ByProperty("", orderByClause, Definition.getSortSet());
            this.orderByClause.add(byProperty);
        }
    }

    public String getGroupByProperty() {
        return groupByProperty;
    }

    public void setGroupByProperty(String groupByProperty) {
        this.groupByProperty = StrUtil.firstLowerCase(groupByProperty);
    }
}
