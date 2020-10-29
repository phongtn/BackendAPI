package com.wind.config.exception;

public class ValidateException extends ServerApiException {

    public ValidateException(String errorCode) {
        super(errorCode);
    }

    public ValidateException(String errorCode, Object... params) {
        super(errorCode, params);
    }
}
