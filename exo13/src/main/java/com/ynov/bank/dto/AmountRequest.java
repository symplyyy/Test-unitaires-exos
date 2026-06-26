package com.ynov.bank.dto;

import java.math.BigDecimal;

// Utilise pour un depot ou un retrait
public class AmountRequest {

    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
