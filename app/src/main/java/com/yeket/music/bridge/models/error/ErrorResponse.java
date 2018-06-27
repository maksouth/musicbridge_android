package com.yeket.music.bridge.models.error;

public class ErrorResponse {
    public String code;
    public String message;

    public ErrorResponse(String code, String message, String ... params) {
        this.code = code;
        setMessage(message, params);
    }

    public ErrorResponse(ErrorResponse errorResponse, String ... params){
        this.code = errorResponse.code;
        setMessage(errorResponse.message, params);
    }

    private void setMessage(String message, String ... params){
        if(params.length > 0)
            this.message = String.format(message, (Object[])params);
        else this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
