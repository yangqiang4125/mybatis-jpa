package com.yq.mybatis.samples.entity;

import javax.persistence.*;

@Table(name = "test_user")
public class User {

    @Id
    @GeneratedValue(generator = "test_user_seq")
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Integer age;
    private String mobilePhone;

    @OrderBy
    private Integer sort;

    public User() {
    }

    public User(String name, Integer age, String mobilePhone, Integer sort) {
        this.name = name;
        this.age = age;
        this.mobilePhone = mobilePhone;
        this.sort = sort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
