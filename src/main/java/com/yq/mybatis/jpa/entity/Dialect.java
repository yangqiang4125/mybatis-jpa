package com.yq.mybatis.jpa.entity;

import com.yq.mybatis.jpa.exception.MybatisJpaException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public enum Dialect {
    DEFAULT, MYSQL, MARIADB, SQLITE, ORACLE, HSQLDB, POSTGRESQL, SQLSERVER, DB2, INFORMIX, H2, SQLSERVER2012;

    public static Dialect get(DataSource dataSource) {
        Dialect dialect = Dialect.DEFAULT;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            String[] url = metaData.getURL().split(":");
            String key = url[1];
            dialect = Dialect.valueOf(key.toUpperCase());
        } catch (SQLException e) {
            throw new MybatisJpaException("获取数据库方言出错", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new MybatisJpaException("关闭数据出错", e);
            }
        }

        return dialect;
    }

}
