package com.ciklum.demowsmobileapp.exception;

public class UserServiceException extends RuntimeException{

    private static final long serialVersionUID = 1822390971695641347L;

    public UserServiceException(String message) {
        super(message);
    }
}
