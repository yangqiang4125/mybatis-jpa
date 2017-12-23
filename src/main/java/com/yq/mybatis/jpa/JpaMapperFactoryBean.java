package com.yq.mybatis.jpa;

import org.mybatis.spring.mapper.MapperFactoryBean;

/**
 * 重写org.mybatis.spring.mapper.MapperFactoryBean处理mapperInterface
 *
 * @author maoxiangdong
 * @version 1.0
 */
public class JpaMapperFactoryBean<T> extends MapperFactoryBean<T> {
    public JpaMapperFactoryBean() {
    }

    public JpaMapperFactoryBean(Class<T> mapperInterface) {
        super(mapperInterface);
    }

    @Override
    protected void checkDaoConfig() {
        super.checkDaoConfig();
        Class mapperInterface = getMapperInterface();
        MapperParse parse = new MapperParse();
        parse.processConfiguration(getSqlSession().getConfiguration(), mapperInterface);
    }

}
