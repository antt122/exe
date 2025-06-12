package com.example.demo.exception;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    USER_EXISTED(1001, "Userexists", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1002, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST ),
    INVALID_PASSWORD(1003, "Password must be at least 8 characters long and contain at least 1 uppercase letter and 1 special character", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "User not found", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1005, "Ivaild credentials", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1006, "Invalid token", HttpStatus.NOT_FOUND),
    USER_NOT_EXISTED(1007, "User does not exist",HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1008, "You not have permission", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1009, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNKNOWN_ERROR(9999, "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
    private int code;
    private String message;
    private HttpStatusCode statuscode;
    ErrorCode(int code, String message, HttpStatusCode method) {
        this.code = code;
        this.message = message;
        this.statuscode = method;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public HttpStatusCode getStatuscode() {return statuscode;}

}
