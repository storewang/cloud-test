package com.alibaba.csp.sentinel.dashboard.exception;

/**
 * CommandNotFoundException
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
public class CommandNotFoundException extends Exception{
    public CommandNotFoundException() { }

    public CommandNotFoundException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
