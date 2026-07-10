package com.systembank.byteBank.models;

public enum AccountType {

    CURRENT("Conta Corrente"),
    SAVINGS("Conta Poupança");

    private String description;

    AccountType(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
