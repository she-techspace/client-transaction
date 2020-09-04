package com.investec.clienttransaction.exception;

public class ErrorResponse {
    private String errorMessages;

    public ErrorResponse(String errorMessages) {
        this.errorMessages = errorMessages;
    }

    public ErrorResponse() {
    }

    public String getErrorMessages() {
        return this.errorMessages;
    }


    public void setErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errorMessages='" + errorMessages + '\'' +
                '}';
    }
}
