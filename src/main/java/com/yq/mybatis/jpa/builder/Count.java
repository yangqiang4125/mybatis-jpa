package com.yq.mybatis.jpa.builder;

import org.apache.ibatis.mapping.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoxiaodong on 2016/11/9.
 */
public class Count extends Select{

    public Count() {
        this.sqlCommandType = SqlCommandType.SELECT;
    }

    public String toSql() {
        StringBuilder buf = new StringBuilder("<script> select count(1) from ");
        buf.append(this.fromClause);
        if (getWhereClause().length() > 0) {
            buf.append(" where ");
            buf.append(getWhereClause());
        }
        buf.append("</script>");
        return buf.toString();
    }
}
