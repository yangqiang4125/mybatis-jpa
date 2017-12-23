package com.yq.mybatis.jpa.builder;

import org.apache.ibatis.mapping.SqlCommandType;

/**
 * Created by maoxiaodong on 2016/11/9.
 */
public class Delete extends AbstractBuilder<Delete> {

    public Delete() {
        this.sqlCommandType = SqlCommandType.DELETE;
    }

    public String toSql() {
        StringBuilder buf = new StringBuilder("<script> delete from ");
        buf.append(this.fromClause);
        if (getWhereClause().length() > 0) {
            buf.append(" where ");
            buf.append(getWhereClause());
        }
        buf.append("</script>");
        return buf.toString();
    }

}
