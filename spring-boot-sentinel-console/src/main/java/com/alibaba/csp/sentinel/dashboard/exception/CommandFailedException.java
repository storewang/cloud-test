package com.alibaba.csp.sentinel.dashboard.exception;

/**
 * CommandFailedException
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
public class CommandFailedException extends RuntimeException{
    public CommandFailedException() {}

    public CommandFailedException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
