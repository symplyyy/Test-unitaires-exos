package com.ynov.bank.service;

import com.ynov.bank.dto.CreateAccountRequest;
import com.ynov.bank.model.Account;
import com.ynov.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    // TODO : ecrit apres les tests (TDD), implementation a venir
    public Account create(CreateAccountRequest request) {
        throw new UnsupportedOperationException("create pas encore fait");
    }

    public Account getByNumber(String number) {
        throw new UnsupportedOperationException("getByNumber pas encore fait");
    }

    public List<Account> getAll() {
        throw new UnsupportedOperationException("getAll pas encore fait");
    }

    public Account deposit(String number, BigDecimal amount) {
        throw new UnsupportedOperationException("deposit pas encore fait");
    }

    public Account withdraw(String number, BigDecimal amount) {
        throw new UnsupportedOperationException("withdraw pas encore fait");
    }

    public void transfer(String fromNumber, String toNumber, BigDecimal amount) {
        throw new UnsupportedOperationException("transfer pas encore fait");
    }
}
