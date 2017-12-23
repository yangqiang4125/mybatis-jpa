package com.yq.mybatis.jpa.exception;

/**
 * Created by maoxiaodong on 2016/11/15.
 */
public class MybatisJpaException extends RuntimeException {
    private static final long serialVersionUID = 3880206998166270511L;

    public MybatisJpaException() {
    }

    public MybatisJpaException(String message) {
        super(message);
    }

    public MybatisJpaException(String message, Throwable cause) {
        super(message, cause);
    }

    public MybatisJpaException(Throwable cause) {
        super(cause);
    }
}
