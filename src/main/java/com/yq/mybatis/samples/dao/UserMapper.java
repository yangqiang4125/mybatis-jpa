package com.yq.mybatis.samples.dao;

import com.yq.mybatis.jpa.CommonMapper;
import com.yq.mybatis.samples.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends CommonMapper<User, Integer> {

    //Date funcSysdate();

    List<User> findOrderByAge();

    List<User> findOrderByAgeDesc();

    List<User> findByNameOrderByAge(String name);

    List<User> findByNameOrAgeGreaterOrderByAge(@Param("name") String name,
                                                @Param("age") Integer age);

    String findNameById(Integer id);

    String findAgeById(Integer id);

    int updateName(User user);

    List<User> findByNameLike(@Param("name") String name);

}
