package com.yq.mybatis.samples.controller;

import com.yq.mybatis.samples.dao.FuncMapper;
import com.yq.mybatis.samples.dao.UserMapper;
import com.yq.mybatis.samples.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yangqiang-mac on 2017/12/23.
 */
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;


    @RequestMapping("/")
    public Object getUser(){
        //User user = userMapper.find(1);
        User user = new User();
        user.setMobilePhone("18623192310");
        user.setName("yafen2");
        user.setAge(12);
        user.setSort(16);
        int count = userMapper.insert(user);
        List<User> list = userMapper.findOrderByAge();
        //String age = userMapper.findAgeById(1);
        return list;
    }
}
/**/
