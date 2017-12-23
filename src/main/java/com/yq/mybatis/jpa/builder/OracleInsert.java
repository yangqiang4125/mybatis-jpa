package com.yq.mybatis.jpa.builder;

import com.yq.mybatis.jpa.entity.Property;
import com.yq.mybatis.jpa.entity.TypeSketch;
import com.yq.mybatis.jpa.exception.MybatisJpaException;
import com.yq.mybatis.jpa.keyGenerator.SequenceKeyGenerator;
import com.yq.mybatis.jpa.keyGenerator.UUIDKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;


/**
 * Created by maoxiaodong on 2016/11/17.
 */
public class OracleInsert extends Insert {

    @Override
    protected StringBuffer getValueClauseBatch(TypeSketch typeSketch) {
        StringBuffer valueClause = new StringBuffer();
        valueClause.append("<foreach collection=\"");
        valueClause.append(typeSketch.getName());
        valueClause.append("\" item=\"item\" separator=\" union all \" >");
        valueClause.append("select ");
        valueClause.append(getValueClauseOne("item."));
        valueClause.append(" from dual ");
        valueClause.append("</foreach>");
        return valueClause;
    }

    @Override
    public void finish() {
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, toSql(), getParameterType());
        MappedStatement.Builder builder = new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType);
        Property property = entity.getId();
        if (property != null && property.isAutoGenerate()) {
            builder.keyProperty(property.getName());
            if (property.getType() == String.class) {
                builder.keyGenerator(new UUIDKeyGenerator());
            } else if (property.getType() == Integer.class) {
                builder.keyGenerator(new SequenceKeyGenerator(dialect, property.getGenerator()));
            } else {
                throw new MybatisJpaException("不支持该类型的主键生成：" + property.getType().getName());
            }

        }
        configuration.addMappedStatement(builder.build());
    }
}

