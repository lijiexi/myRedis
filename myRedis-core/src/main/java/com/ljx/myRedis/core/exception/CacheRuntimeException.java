package com.ljx.myRedis.core.exception;

public class CacheRuntimeException extends RuntimeException{
    public CacheRuntimeException () {}

    public CacheRuntimeException (String message) {
        super(message);
    }

}
