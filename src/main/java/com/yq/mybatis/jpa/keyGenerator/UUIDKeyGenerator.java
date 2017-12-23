package com.yq.mybatis.jpa.keyGenerator;

import com.yq.mybatis.jpa.util.CollectionUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import java.sql.Statement;
import java.util.Collection;
import java.util.UUID;

/**
 * uuid类型的主键生成器
 *
 * @author maoxiangdong
 * @version 1.0
 */
public class UUIDKeyGenerator extends NoKeyGenerator {

    @Override
    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        processGeneratedKeys(ms, CollectionUtil.toCollection(parameter));
    }

    private void processGeneratedKeys(MappedStatement ms, Collection<Object> parameters) {
        Configuration configuration = ms.getConfiguration();
        for (Object parameter : parameters) {
            MetaObject metaParam = configuration.newMetaObject(parameter);
            String[] e = ms.getKeyProperties();
            metaParam.setValue(e[0], getUUID());
        }
    }

    private String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
