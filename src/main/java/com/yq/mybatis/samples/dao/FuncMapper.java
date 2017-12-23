package com.yq.mybatis.samples.dao;


import com.yq.mybatis.jpa.Mapper;

/**
 * Created by maoxiaodong on 2016/11/23.
 */
public interface FuncMapper extends Mapper<String> {
    String funcHello(Integer id);

    /**
     * 非MAP类型的参数不传能OUT
     * @param
     * @return
     */
    //User callPro_allUser(int id);

    /**
     * MAP类型的参数需定义@ParameterMap，依序添加参数
     * @param
     * @return
     */
//    @ParameterMap({
//            @Parameter(name = "id", type = Integer.class, jdbcType = JdbcType.INTEGER),
//            @Parameter(name = "name", jdbcType = JdbcType.VARCHAR, mode = ParameterMode.OUT)
//    })
//    User callTestProcedure(Map map);
}
