package com.wind.config.exception;

public enum ErrorType {

    UNAUTHORIZED("base.unauthorized"),
    INTERNAL_SERVER_ERROR("base.internal_server_error"),
    MISSING_PARAMETERS("common.missing_parameter");

    private String errorCode;

    ErrorType(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
