package com.yq.mybatis.jpa.keyGenerator;

import com.yq.mybatis.jpa.Definition;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoxiaodong on 2016/11/18.
 */
public class PageCountGenerator implements KeyGenerator {

    @Override
    public void processBefore(Executor executor, MappedStatement mappedStatement, Statement statement, Object o) {
        mappedStatement.getKeyGenerator();
        StringBuffer buffer = new StringBuffer("select * from (") ;
        buffer.append(mappedStatement.getSqlSource().toString());
        buffer.append(")T");

    }

    @Override
    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        StringBuffer buffer = new StringBuffer("select count(1) from (") ;
        buffer.append(ms.getSqlSource().toString());
        buffer.append(")T");
        Configuration configuration = ms.getConfiguration();

        SqlSource sqlSource = new StaticSqlSource(configuration, buffer.toString());
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        ResultMap.Builder resultMapBuilder = new ResultMap.Builder(
                configuration,
                ms.getId() + ".count",
                Integer.class,
                new ArrayList<ResultMapping>());
        resultMaps.add(resultMapBuilder.build());
        MappedStatement countStatement = new MappedStatement
                .Builder(configuration, ms.getId() + ".key", sqlSource, SqlCommandType.SELECT)
                .resultMaps(resultMaps)
                .build();
        Executor keyExecutor = configuration.newExecutor(executor.getTransaction(), ExecutorType.SIMPLE);
        try {
            MetaObject metaParam = configuration.newMetaObject(parameter);
            List values = keyExecutor.query(countStatement, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
            metaParam.setValue("size", values.get(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
