package com.ynov.bank.model;

import java.math.BigDecimal;

// Un compte = un numero unique, un titulaire et un solde (qui demarre a 0).
public class Account {

    private String number;
    private String owner;
    private BigDecimal balance;

    public Account() {
    }

    public Account(String number, String owner) {
        this.number = number;
        this.owner = owner;
        this.balance = BigDecimal.ZERO;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
