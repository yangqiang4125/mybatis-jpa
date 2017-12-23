package com.yq.mybatis.jpa.builder;

import com.yq.mybatis.jpa.Definition;
import com.yq.mybatis.jpa.entity.*;
import com.yq.mybatis.jpa.util.CollectionUtil;
import com.yq.mybatis.jpa.util.StrUtil;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by maoxiaodong on 2016/11/11.
 */
public abstract class AbstractBuilder<B> implements Builder {
    protected String fromClause = "";
    private StringBuffer whereClause = new StringBuffer();
    protected String id;

    protected static final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    protected Configuration configuration;
    protected SqlCommandType sqlCommandType;
    protected String sqlType;

    private Class parameterType;
    protected TypeSketch returnType;
    protected List<TypeSketch> parameters;

    protected Class mapperInterface;
    protected Method mapperMethod;
    protected Entity entity;
    protected Dialect dialect;

    protected SqlSource getSqlSource() {
        return languageDriver.createSqlSource(configuration, toSql(), getParameterType());
    }

    protected abstract String toSql();

    protected Class getParameterType() {
        if (parameters.size() == 0) {
            parameterType = String.class;
        } else if (parameters.size() == 1) {
            for (TypeSketch parameter : parameters) {
                if (parameter.getOwnerType() == null) {
                    parameterType = parameter.getRawType();
                } else {
                    parameterType = parameter.getOwnerType();
                }
            }
        } else {
            parameterType = Map.class;
        }
        return parameterType;
    }

    public B where(String field, String expression) {
        where("and", field, expression);
        return (B) this;
    }

    public B where(String field) {
        where("and", field, Definition.EQUAL);
        return (B) this;
    }

    public B where(String join, String field, String condition) {
        Property property = entity.getProperty(field);
        whereClause.append(join);
        whereClause.append(" ");
        whereClause.append(entity.getTable());
        whereClause.append(".");
        whereClause.append(property.getColumn());
        whereClause.append(" ");
        whereClause.append(Definition.getExpression(condition));

        if (condition.equals(Definition.IN) || condition.equals(Definition.NOT_IN)) {
            whereClause.append(" <foreach collection=\"");
            if (parameters.size() == 1) {
                TypeSketch typeSketch = parameters.get(0);
                whereClause.append(typeSketch.getName());
            } else {
                whereClause.append(field);
            }

            whereClause.append("\" item=\"item\" open=\"(\" separator=\",\" close=\")\">");
            whereClause.append("#{item}");
            whereClause.append("</foreach>");
        } else if (!condition.equals(Definition.NULL)
                && !condition.equals(Definition.NOT_NULL)) {
            whereClause.append(" #{");
            whereClause.append(field);
            whereClause.append("} ");

        }

        return (B) this;
    }

    public String getFromClause() {
        return fromClause;
    }

    protected StringBuffer getWhereClause() {
        if (whereClause.length() == 0) {
            Property id = entity.getId();
            if (id != null) {
                if (parameters.size() == 1) {
                    for (TypeSketch typeSketch : parameters) {
                        if (CollectionUtil.isCollection(typeSketch.getRawType())
                                || typeSketch.getRawType().isArray()) {
                            where(id.getName(), Definition.IN);
                        } else {
                            where(id.getName());
                        }

                    }
                }
            }
        }
        StrUtil.replaceFirst(whereClause, "and ");
        return whereClause;
    }

    protected SqlFractional parseMethodName() {
        CommonParser grammarParser = new CommonParser();
        SqlFractional sqlFractional = grammarParser.parse(mapperMethod.getName());
        List<ByProperty> byProperties = sqlFractional.getWhereClauses();
        for (ByProperty byProperty : byProperties) {
            where(byProperty.getPrefix(), byProperty.getProperty(), byProperty.getSuffix());
        }
        return sqlFractional;
    }

    @Override
    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public void setReturnType(TypeSketch returnType) {
        this.returnType = returnType;
    }

    @Override
    public void setParameters(List<TypeSketch> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    @Override
    public void setMapperInterface(Class mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override
    public void setMapperMethod(Method mapperMethod) {
        this.mapperMethod = mapperMethod;
        parseMethodName();
        id = mapperInterface.getName() + "." + mapperMethod.getName();
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
        fromClause = entity.getTable();
    }

    protected MappedStatement.Builder builder() {
        return new MappedStatement.Builder(configuration, id, getSqlSource(), sqlCommandType);
    }

    @Override
    public void finish() {
        configuration.addMappedStatement(builder().build());
    }
}
