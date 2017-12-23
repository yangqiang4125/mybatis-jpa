package com.yq.mybatis.jpa;

import com.yq.mybatis.jpa.builder.Builder;
import com.yq.mybatis.jpa.entity.Dialect;
import com.yq.mybatis.jpa.entity.Entity;
import com.yq.mybatis.jpa.entity.TypeSketch;
import com.yq.mybatis.jpa.exception.MybatisJpaException;
import com.yq.mybatis.jpa.util.ReflectUtil;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;

import javax.sql.DataSource;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * 对mapperInterface解析并生成相关的mybatis配置
 *
 * @author maoxiangdong
 * @version 1.0
 */
public class MapperParse {

    public void processConfiguration(Configuration configuration, Class<?> mapperInterface) {
        Method[] methods = mapperInterface.getMethods();
        Collection<String> names = configuration.getMappedStatementNames();
        Class entityClass = ReflectUtil.getEntityTypeByMapper(mapperInterface);
        Environment environment = configuration.getEnvironment();
        DataSource dataSource = environment.getDataSource();
        Dialect dialect = Dialect.get(dataSource);
        for (Method method : methods) {
            String id = mapperInterface.getName() + "." + method.getName();
            try {
                if (!names.contains(id)) {
                    TypeSketch returnType = ReflectUtil.getReturnType(method, mapperInterface);
                    List<TypeSketch> parameters = ReflectUtil.getParameters(method, mapperInterface);
                    if (!Entity.is(entityClass)) {
                        if (Entity.is(returnType.getOwnerType())) {
                            entityClass = returnType.getOwnerType();
                        } else if (parameters.size() == 1) {
                            for (TypeSketch parameter : parameters) {
                                if (Entity.is(parameter.getOwnerType())) {
                                    entityClass = parameter.getOwnerType();
                                }
                            }
                        }
                    }

                    String sqlType = Definition.getSqlType(method.getName());
                    Builder builder = BuilderMap.get(dialect, sqlType);
                    builder.setDialect(dialect);

                    if(entityClass == null){
                        continue;
                    }

                    if(Entity.is(entityClass)){
                        Entity entity = EntityMap.get(entityClass);
                        builder.setEntity(entity);
                    }
                    builder.setReturnType(returnType);
                    builder.setSqlType(sqlType);
                    builder.setMapperInterface(mapperInterface);
                    builder.setConfiguration(configuration);
                    builder.setParameters(parameters);
                    builder.setMapperMethod(method);
                    builder.finish();
                }
            } catch (Exception e) {
                throw new MybatisJpaException("映射出错：" + id, e);
            }

        }

    }
}
