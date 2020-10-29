package com.wind.config.exception;

public class ServerApiException extends RuntimeException {

    private String errorCode;

    private Object[] params;

    public ServerApiException(Throwable cause) {
        super(cause);
    }

    public ServerApiException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        params = new String[0];
    }

    public ServerApiException(String errorCode) {
        this.errorCode = errorCode;
        params = new String[0];
    }

    public ServerApiException(String errorCode, Object... params) {
        this.errorCode = errorCode;
        this.params = params;
    }

    public Object[] getParams() {
        return params;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}
