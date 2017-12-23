package com.yq.mybatis.jpa.builder;


import com.yq.mybatis.jpa.entity.Property;
import com.yq.mybatis.jpa.util.StrUtil;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.Collection;
import java.util.Set;

/**
 * Created by maoxiaodong on 2016/11/9.
 */
public class Update extends AbstractBuilder<Update> {
    private StringBuffer updateClause = new StringBuffer();

    public Update() {
        this.sqlCommandType = SqlCommandType.UPDATE;
    }

    public String toSql() {
        StringBuilder buf = new StringBuilder("<script> update ");
        buf.append(this.fromClause).append(" set ");
        buf.append(getUpdateClause());
        if (getWhereClause().length() > 0) {
            buf.append(" where ");
            buf.append(getWhereClause());
        }
        buf.append("</script>");
        return buf.toString();
    }

    public Update update(String field) {
        Property property = entity.getProperty(field);
        updateClause.append(",");
        updateClause.append(entity.getTable());
        updateClause.append(".");
        updateClause.append(property.getColumn());
        updateClause.append("=#{");
        updateClause.append(property.getName());
        updateClause.append("}");
        return this;
    }

    private StringBuffer getUpdateClause() {
        if (updateClause.length() == 0) {
            Collection<Property> properties = entity.getProperties();
            for (Property property : properties) {
                update(property.getName());
            }
        }
        StrUtil.replaceFirst(this.updateClause, ",");
        return updateClause;
    }

    @Override
    public SqlFractional parseMethodName() {
        SqlFractional sqlFractional = super.parseMethodName();
        Set<String> fields = sqlFractional.getProperty();
        if (!fields.isEmpty()) {
            for (String field : fields) {
                update(field);
            }
        }
        return sqlFractional;
    }

}
