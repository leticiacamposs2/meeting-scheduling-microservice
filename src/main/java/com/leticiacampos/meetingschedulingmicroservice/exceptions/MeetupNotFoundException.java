package com.leticiacampos.meetingschedulingmicroservice.exceptions;

public class MeetupNotFoundException extends RuntimeException {

    public MeetupNotFoundException(String s) {
        super(s);
    }

}