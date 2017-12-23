package com.yq.mybatis.jpa.entity;

import com.yq.mybatis.jpa.Definition;
import com.yq.mybatis.jpa.util.StrUtil;

import java.util.Collection;

/**
 * Created by maoxiaodong on 2016/11/15.
 */
public class ByProperty {
    private String prefix = "";
    private String property;
    private String suffix = "";

    public ByProperty() {
    }

    public ByProperty(String prefix, String s, Collection<String> suffixs) {
        this.prefix = prefix;
        String property = s;
        String suffix = "";
        for (String key : suffixs) {
            if (s.endsWith(key)) {
                property = StrUtil.replaceLast(s, key);
                suffix = key;
                break;
            }
        }
        this.property = StrUtil.firstLowerCase(property);
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


}
