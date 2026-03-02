package com.bankingtransactions.bankingendpoints.eventListener;

public class AccountCreatedEvent {

    private final String email;
    private final String fullName;
    private final String accountNumber;

    public AccountCreatedEvent(String email, String fullName, String accountNumber) {
        this.email = email;
        this.fullName = fullName;
        this.accountNumber = accountNumber;
    }

    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getAccountNumber() { return accountNumber; }
}
