package com.investec.clienttransaction.exception;

public class ClientTransactionException extends RuntimeException {

    private String errorMessage;

    public ClientTransactionException(String errorMessage) {
        this.errorMessage = errorMessage;

    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
