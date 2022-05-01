package com.leticiacampos.meetingschedulingmicroservice.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String s) {
        super(s);
    }
}
