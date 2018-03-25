package com.yq.mybatis.jpa.entity;

/**
 * Created by yangqiang-mac on 2018/3/25.
 */
public interface IdGenerator<T> {
    /**
     * 生成下一个id并返回。
     *
     * @return 返回新的id.
     */
    public T next();
}
