package com.ynov.bank.service;

import com.ynov.bank.dto.CreateAccountRequest;
import com.ynov.bank.exception.AccountAlreadyExistsException;
import com.ynov.bank.exception.AccountNotFoundException;
import com.ynov.bank.exception.InsufficientFundsException;
import com.ynov.bank.exception.InvalidAmountException;
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

    public Account create(CreateAccountRequest request) {
        if (repository.existsByNumber(request.getNumber())) {
            throw new AccountAlreadyExistsException(request.getNumber());
        }
        // un nouveau compte demarre toujours a 0
        Account account = new Account(request.getNumber(), request.getOwner());
        return repository.save(account);
    }

    public Account getByNumber(String number) {
        return repository.findByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(number));
    }

    public List<Account> getAll() {
        return repository.findAll();
    }

    public Account deposit(String number, BigDecimal amount) {
        requirePositive(amount);
        Account account = getByNumber(number);
        account.setBalance(account.getBalance().add(amount));
        return repository.save(account);
    }

    public Account withdraw(String number, BigDecimal amount) {
        requirePositive(amount);
        Account account = getByNumber(number);
        requireEnoughFunds(account, amount);
        account.setBalance(account.getBalance().subtract(amount));
        return repository.save(account);
    }

    public void transfer(String fromNumber, String toNumber, BigDecimal amount) {
        requirePositive(amount);
        // les deux comptes doivent exister avant de bouger l'argent
        Account from = getByNumber(fromNumber);
        Account to = getByNumber(toNumber);
        requireEnoughFunds(from, amount);

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        repository.save(from);
        repository.save(to);
    }

    // le montant doit etre strictement positif
    private void requirePositive(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Le montant doit etre strictement positif");
        }
    }

    private void requireEnoughFunds(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(account.getNumber());
        }
    }
}
