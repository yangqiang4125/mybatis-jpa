package com.yq.mybatis.jpa.builder;

import com.yq.mybatis.jpa.entity.Dialect;
import com.yq.mybatis.jpa.entity.Entity;
import com.yq.mybatis.jpa.entity.TypeSketch;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Method;
import java.util.List;

/**
 * sql生成器接口，自定义sql生成器时必选实现该接口
 *
 * @author maoxiangdong
 * @version 1.0
 */
public interface Builder {
    void setConfiguration(Configuration configuration);

    void setSqlType(String sqlType);

    void setDialect(Dialect dialect);

    void setMapperInterface(Class mapperInterface);

    void setMapperMethod(Method mapperMethod);

    void setParameters(List<TypeSketch> parameters);

    void setReturnType(TypeSketch returnType);

    void setEntity(Entity entity);

    void finish();
}
