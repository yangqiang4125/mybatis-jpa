package com.yq.mybatis.jpa;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by maoxiaodong on 2016/11/15.
 */
public interface CommonMapper<T, ID extends Serializable> extends Mapper<T> {
    int insert(T entity);

    int insertBatch(Collection<T> collection);

    int update(T entity);

    int delete(ID id);

    int deleteBatch(Collection<ID> collection);

    int deleteAll();

    T find(ID id);

    List<T> findAll();

    int count();
}
