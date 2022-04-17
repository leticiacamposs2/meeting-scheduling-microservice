package com.leticiacampos.meetingschedulingmicroservice.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String s) {
        super(s);
    }
}
