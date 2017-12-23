package com.yq.mybatis.jpa.builder;

import com.yq.mybatis.jpa.EntityMap;
import com.yq.mybatis.jpa.entity.Entity;
import com.yq.mybatis.jpa.entity.Property;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoxiaodong on 2016/11/11.
 */
public class DefaultResultMap {
    private Configuration configuration;
    private Entity entity;
    private ResultMap.Builder builder;


    public DefaultResultMap(Configuration configuration, Entity entity, String id) {
        this.configuration = configuration;
        this.entity = entity;
        builder = new ResultMap
                .Builder(configuration, id, entity.getType(), getResultMappingList());
    }


    public ResultMap getResultMap() {
        return builder.build();
    }

    private List<ResultMapping> getResultMappingList() {
        List<ResultMapping> list = new ArrayList<ResultMapping>();
        for (Property property : entity.getProperties()) {
            list.add(
                    new ResultMapping.Builder(
                            configuration,
                            property.getName(),
                            property.getColumn(),
                            property.getType()
                    ).build()
            );
        }
        return list;
    }
}
