package com.fernferret.allpay.commons;

/**
 * @author krinsdeath
 */
public class IncompleteBankException extends RuntimeException {
    private final String message;

    public IncompleteBankException(String msg) {
        this.message = msg;
    }

    public String getLocalizedMessage() {
        return this.message;
    }

}
