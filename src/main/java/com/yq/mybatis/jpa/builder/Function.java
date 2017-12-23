package com.yq.mybatis.jpa.builder;

import com.yq.mybatis.jpa.Definition;
import com.yq.mybatis.jpa.entity.TypeSketch;
import com.yq.mybatis.jpa.util.StrUtil;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.Set;

/**
 * Created by maoxiaodong on 2016/11/16.
 */
public class Function extends Select {
    protected String funcName;
    protected StringBuffer parameterClause = new StringBuffer();

    public Function() {
        this.sqlCommandType = SqlCommandType.SELECT;
    }

    @Override
    protected String toSql() {
        toParameterClause();
        StringBuffer clause = new StringBuffer(funcName);
        clause.append("(");
        if (parameterClause.length() > 0) {
            clause.append(parameterClause);
        }
        clause.append(")");
        return Definition.getNoTableSelect(dialect, clause.toString());
    }

    @Override
    protected SqlFractional parseMethodName() {
        SqlFractional sqlFractional = super.parseMethodName();
        Set<String> names = sqlFractional.getProperty();
        for (String name : names) {
            funcName = StrUtil.camelToUnderline(name);
            funcName = funcName.replace("$",".");
        }
        return sqlFractional;
    }

    protected void toParameterClause() {
        for (TypeSketch typeSketch : parameters) {
            parameterClause.append(",#{");
            parameterClause.append(typeSketch.getName());
            if(typeSketch.getMode() != null){
                parameterClause.append(",mode=");
                parameterClause.append(typeSketch.getMode());
            }

            if(typeSketch.getJdbcType() != null){
                parameterClause.append(",jdbcType=");
                parameterClause.append(typeSketch.getJdbcType());
            }
            parameterClause.append("}");
        }
        StrUtil.replaceFirst(parameterClause, ",");
    }
}
