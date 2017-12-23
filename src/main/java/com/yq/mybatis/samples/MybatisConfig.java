package com.yq.mybatis.samples;

import com.yq.mybatis.jpa.Definition;
import com.yq.mybatis.jpa.Mapper;
import com.yq.mybatis.jpa.JpaMapperScannerConfigurer;
import com.yq.mybatis.jpa.builder.Function;
import com.yq.mybatis.jpa.builder.Procedure;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;


@Configuration
@EnableConfigurationProperties(MybatisProperties.class)
public class MybatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/**"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public JpaMapperScannerConfigurer getMapperScannerConfigurer() {
        //Definition.expand("func","FUNCTION", Function.class);//扩展function类型的查询
        //Definition.expand("call","PROCEDURE", Procedure.class);//扩展function类型的查询
        JpaMapperScannerConfigurer configurer = new JpaMapperScannerConfigurer();
        configurer.setBasePackage("com.yq.mybatis.samples");
        configurer.setMarkerInterface(Mapper.class);
        return configurer;
    }
}
