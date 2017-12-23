package com.yq.mybatis.jpa.builder;


import com.yq.mybatis.jpa.entity.ByProperty;
import com.yq.mybatis.jpa.entity.Entity;
import com.yq.mybatis.jpa.entity.Property;
import com.yq.mybatis.jpa.util.CollectionUtil;
import com.yq.mybatis.jpa.util.StrUtil;
import org.apache.ibatis.mapping.*;

import java.util.*;

/**
 * Created by maoxiaodong on 2016/11/9.
 */
public class Select extends AbstractBuilder<Select> {
    protected StringBuffer selectClause = new StringBuffer();
    private StringBuffer orderByClause = new StringBuffer();
    private StringBuffer groupByClause = new StringBuffer();

    public Select() {
        this.sqlCommandType = SqlCommandType.SELECT;
    }

    protected String toSql() {
        StringBuilder buf = new StringBuilder("<script> select ");
        buf.append(getSelectClause());
        buf.append(" from ").append(this.fromClause);

        if (getWhereClause().length() > 0) {
            buf.append(" where ");
            buf.append(getWhereClause());
        }

        toGroupBy();
        if (groupByClause.length() > 0) {
            buf.append(" group by ").append(this.groupByClause);
        }

        toOrderByClause();
        if (orderByClause.length() > 0) {
            buf.append(" order by ").append(orderByClause);
        }
        buf.append("</script>");
        return buf.toString();
    }

    public Select orderBy(String field, String value) {
        Property property = entity.getProperty(field);
        orderByClause.append(",");
        orderByClause.append(entity.getTable());
        orderByClause.append(".");
        orderByClause.append(property.getColumn());
        orderByClause.append(" ");
        orderByClause.append(value);
        return this;
    }

    private void toOrderByClause() {
        if (orderByClause.length() == 0 && CollectionUtil.isCollection(returnType.getRawType())) {
            Property sort = entity.getSort();
            if (sort != null) {
                orderBy(sort.getColumn(), sort.getSortOrder());
            }
        }
        StrUtil.replaceFirst(this.orderByClause, ",");
    }

    public Select groupBy(String field) {
        Property property = entity.getProperty(field);
        groupByClause.append(",");
        groupByClause.append(entity.getTable());
        groupByClause.append(".");
        groupByClause.append(property.getColumn());
        return this;
    }

    protected void toGroupBy(){
        StrUtil.replaceFirst(groupByClause, ",");
    }

    public Select select(String field) {
        Property property = entity.getProperty(field);
        if (property != null) {
            selectClause.append(", ");
            selectClause.append(entity.getTable());
            selectClause.append(".");
            selectClause.append(property.getColumn());
        }

        return this;
    }

    private StringBuffer getSelectClause() {
        if (selectClause.length() == 0) {
            Collection<Property> properties = entity.getProperties();
            for (Property property : properties) {
                select(property.getName());
            }
        }
        StrUtil.replaceFirst(this.selectClause, ",");
        return selectClause;
    }


    @Override
    protected SqlFractional parseMethodName() {
        SqlFractional sqlFractional = super.parseMethodName();

        Set<String> fields = sqlFractional.getProperty();
        if (!fields.isEmpty()) {
            for (String field : fields) {
                select(field);
            }
        }

        if (StrUtil.isNotEmpty(sqlFractional.getGroupByProperty())) {
            groupBy(sqlFractional.getGroupByProperty());
        }

        if (!sqlFractional.getOrderByClause().isEmpty()) {
            Set<ByProperty> byProperties = sqlFractional.getOrderByClause();
            for (ByProperty byProperty : byProperties) {
                orderBy(byProperty.getProperty(), byProperty.getSuffix());
            }
        }
        return sqlFractional;
    }

    @Override
    protected MappedStatement.Builder builder() {
        MappedStatement.Builder builder = super.builder();
        List<ResultMap> resultMaps;
        String resultMapId = builder.id() + "-Inline";
        if (Entity.is(returnType.getOwnerType())) {
            DefaultResultMap defaultResultMap = new DefaultResultMap(configuration, entity, resultMapId);
            resultMaps = new ArrayList<ResultMap>();
            resultMaps.add(defaultResultMap.getResultMap());
        } else {
            resultMaps = new ArrayList<ResultMap>();
            ResultMap.Builder resultMapBuilder = new ResultMap.Builder(
                    configuration,
                    resultMapId,
                    returnType.getOwnerType(),
                    new ArrayList<ResultMapping>());
            resultMaps.add(resultMapBuilder.build());
        }
        builder.resultMaps(resultMaps);
        return builder;
    }
}
