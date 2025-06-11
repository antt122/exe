package com.example.demo.exception;

public class AppException extends RuntimeException {
private ErrorCode errcode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errcode = errorCode;
    }


    public ErrorCode getErrorCode() {
        return errcode;
    }
    public void setErrorCode(ErrorCode errorCode) {
        this.errcode = errorCode;
    }
}
