package com.yq.mybatis.jpa.builder;

import org.apache.ibatis.mapping.*;

/**
 * Created by maoxiaodong on 2016/11/18.
 */
public class Procedure extends Function {

    @Override
    protected String toSql() {
        toParameterClause();
        StringBuffer clause = new StringBuffer("{ call ");
        clause.append(funcName);
        clause.append("(");
        if (parameterClause.length() > 0) {
            clause.append(parameterClause);
        }
        clause.append(")}");
        return clause.toString();
    }

    @Override
    protected MappedStatement.Builder builder() {
        return super.builder().statementType(StatementType.CALLABLE);
    }
}
