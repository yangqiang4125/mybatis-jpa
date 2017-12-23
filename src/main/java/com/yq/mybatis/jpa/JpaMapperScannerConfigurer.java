package com.yq.mybatis.jpa;


import com.yq.mybatis.jpa.util.StrUtil;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * 重写org.mybatis.spring.mapper.MapperScannerConfigurer加入要处理的内容
 *
 * @author maoxiangdong
 * @version 1.0
 */
public class JpaMapperScannerConfigurer extends MapperScannerConfigurer {

    /**
     * 将mybatis代理类设置为com.yq.mybatis.jpa.JpaMapperFactoryBean
     *
     * @param registry
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        super.postProcessBeanDefinitionRegistry(registry);
        String[] names = registry.getBeanDefinitionNames();
        GenericBeanDefinition definition;
        for (String name : names) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(name);
            if (beanDefinition instanceof GenericBeanDefinition) {
                definition = (GenericBeanDefinition) beanDefinition;
                if (StrUtil.isNotEmpty(definition.getBeanClassName())
                        && definition.getBeanClassName().equals(MapperFactoryBean.class.getName())) {
                    definition.setBeanClass(JpaMapperFactoryBean.class);
                }
            }

        }

    }
}
