package com.yq.mybatis.jpa.builder;


import com.yq.mybatis.jpa.entity.Dialect;
import com.yq.mybatis.jpa.entity.IncreaseLongId;
import com.yq.mybatis.jpa.entity.Property;
import com.yq.mybatis.jpa.entity.TypeSketch;
import com.yq.mybatis.jpa.exception.MybatisJpaException;
import com.yq.mybatis.jpa.keyGenerator.SequenceKeyGenerator;
import com.yq.mybatis.jpa.keyGenerator.UUIDKeyGenerator;
import com.yq.mybatis.jpa.util.CollectionUtil;
import com.yq.mybatis.jpa.util.StrUtil;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import javax.persistence.GenerationType;
import java.util.Collection;

/**
 * Created by maoxiaodong on 2016/11/9.
 */
public class Insert extends AbstractBuilder<Insert> {
    private StringBuffer insertClause = new StringBuffer();
    private StringBuffer valueClause = new StringBuffer();

    public Insert() {
        this.sqlCommandType = SqlCommandType.INSERT;
    }

    public String toSql() {

        StringBuilder buf = new StringBuilder("<script> insert into ");

        buf.append(this.fromClause).append("(");
        buf.append(getInsertClause());
        buf.append(")");
        buf.append(getValueClause());
        buf.append("</script>");
        return buf.toString();
    }

    public Insert insert(String field) {
        Property property = entity.getProperty(field);
        insertClause.append(", ");
        insertClause.append(entity.getTable());
        insertClause.append(".");
        insertClause.append(property.getColumn());
        return this;
    }

    public void value(String field, String prefix, StringBuffer valueClause) {
        Property property = entity.getProperty(field);
        valueClause.append(", #{");
        valueClause.append(prefix);
        valueClause.append(property.getName());
        valueClause.append("}");
    }

    protected StringBuffer getValueClause() {
        if (parameters.size() == 1) {
            for (TypeSketch typeSketch : parameters) {
                if (CollectionUtil.isCollection(typeSketch.getRawType())
                        || typeSketch.getRawType().isArray()) {
                    valueClause.append(getValueClauseBatch(typeSketch));
                } else {
                    valueClause.append("values (");
                    valueClause.append(getValueClauseOne());
                    valueClause.append(")");
                }

            }
        } else {
            valueClause.append("values (");
            valueClause.append(getValueClauseOne());
            valueClause.append(")");

        }
        return valueClause;
    }

    protected StringBuffer getValueClauseBatch(TypeSketch typeSketch) {
        StringBuffer valueClause = new StringBuffer();
        valueClause.append("values <foreach collection=\"");
        valueClause.append(typeSketch.getName());
        valueClause.append("\" item=\"item\" separator=\",\" >");
        valueClause.append("(");
        valueClause.append(getValueClauseOne("item."));
        valueClause.append(")");
        valueClause.append("</foreach>");
        return valueClause;
    }

    protected StringBuffer getInsertClause() {
        Collection<Property> properties = entity.getProperties();
        for (Property property : properties) {
            insert(property.getName());
        }
        StrUtil.replaceFirst(this.insertClause, ",");
        return insertClause;
    }

    protected StringBuffer getValueClauseOne(String prefix) {
        StringBuffer stringBuffer = new StringBuffer();
        Collection<Property> properties = entity.getProperties();
        for (Property property : properties) {
            value(property.getName(), prefix, stringBuffer);
        }
        StrUtil.replaceFirst(stringBuffer, ",");
        return stringBuffer;
    }

    private StringBuffer getValueClauseOne() {
        return getValueClauseOne("");
    }


    @Override
    protected MappedStatement.Builder builder() {
        MappedStatement.Builder builder = super.builder();
        Property property = entity.getId();
        if (property != null && property.isAutoGenerate()) {
            builder.keyProperty(property.getName());
            if (property.getStrategy().equals(GenerationType.IDENTITY)) {
                builder.keyGenerator(new Jdbc3KeyGenerator());
            } else if (property.getStrategy().equals(GenerationType.SEQUENCE)) {
                builder.keyGenerator(new SequenceKeyGenerator(dialect, property.getGenerator()));
            } else {
                if (property.getType() == String.class) {
                    builder.keyGenerator(new UUIDKeyGenerator());
                } else if (property.getType() == Integer.class) {
                    if (dialect == Dialect.ORACLE) {
                        builder.keyGenerator(new SequenceKeyGenerator(dialect, property.getGenerator()));
                    } else {
                        builder.keyGenerator(new Jdbc3KeyGenerator());
                    }
                } else if (property.getType() == Long.class) {
                    if (dialect == Dialect.ORACLE) {
                        builder.keyGenerator(new SequenceKeyGenerator(dialect, property.getGenerator()));
                    } else {
                        builder.keyGenerator(new Jdbc3KeyGenerator());
                    }
                } else {
                    throw new MybatisJpaException("不支持该类型的主键生成：" + property.getType().getName());
                }
            }

        }
        return builder;
    }
}
