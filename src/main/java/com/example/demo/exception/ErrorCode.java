package com.example.demo.exception;

public enum ErrorCode {
    USER_EXISTED(1001, "Userexists"),
    INVALID_USERNAME(1002, "Username must be at least 3 characters" ),
    INVALID_PASSWORD(1003, "Password must be at least 8 characters long and contain at least 1 uppercase letter and 1 special character"),
    USER_NOT_FOUND(1004, "User not found"),
    INVALID_CREDENTIALS(1005, "Ivaild credentials"),
    INVALID_TOKEN(1006, "Invalid token"),
    USER_NOT_EXISTED(1007, "User does not exist"),
    UNKNOWN_ERROR(9999, "Unknown error");
    private int code;
    private String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

}
