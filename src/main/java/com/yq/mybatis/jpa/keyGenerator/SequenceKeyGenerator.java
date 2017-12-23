package com.yq.mybatis.jpa.keyGenerator;

import com.yq.mybatis.jpa.Definition;
import com.yq.mybatis.jpa.entity.Dialect;
import com.yq.mybatis.jpa.exception.MybatisJpaException;
import com.yq.mybatis.jpa.util.CollectionUtil;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * sequence类型的主键生成器
 *
 * @author maoxiangdong
 * @version 1.0
 */
public class SequenceKeyGenerator extends NoKeyGenerator {

    public static final String SELECT_KEY_SUFFIX = "!selectKey";
    private MappedStatement keyStatement;
    private String sequence;
    private Dialect dialect;

    public SequenceKeyGenerator(Dialect dialect, String sequence) {
        this.dialect = dialect;
        this.sequence = sequence;
    }

    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        Configuration configuration = ms.getConfiguration();
        SqlSource sqlSource = new StaticSqlSource(configuration, Definition.getNoTableSelect(dialect, sequence + ".nextval"));
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        ResultMap.Builder resultMapBuilder = new ResultMap.Builder(
                configuration,
                ms.getId() + ".keyId",
                Integer.class,
                new ArrayList<ResultMapping>());
        resultMaps.add(resultMapBuilder.build());
        keyStatement = new MappedStatement
                .Builder(configuration, ms.getId() + ".key", sqlSource, SqlCommandType.SELECT)
                .resultMaps(resultMaps)
                .build();
        this.processGeneratedKeys(executor, ms, CollectionUtil.toCollection(parameter));
    }

    private void processGeneratedKeys(Executor executor, MappedStatement ms, Collection<Object> parameters) {
        try {
            for (Object parameter : parameters) {
                if (parameter != null) {
                    String[] e = ms.getKeyProperties();
                    Configuration configuration = ms.getConfiguration();
                    MetaObject metaParam = configuration.newMetaObject(parameter);
                    if (e != null) {
                        Executor keyExecutor = configuration.newExecutor(executor.getTransaction(), ExecutorType.SIMPLE);
                        List values = keyExecutor.query(this.keyStatement, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
                        MetaObject metaResult = configuration.newMetaObject(values.get(0));
                        if (metaResult.hasGetter(e[0])) {
                            metaParam.setValue(e[0], metaResult.getValue(e[0]));
                        } else {
                            metaParam.setValue(e[0], values.get(0));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new MybatisJpaException("sequence查询出错", e);
        }
    }

}
