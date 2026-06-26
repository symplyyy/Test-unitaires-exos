package com.ynov.bank.repository;

import com.ynov.bank.model.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Stockage en memoire, les comptes sont ranges par numero.
// Pas de base de donnees, c'est suffisant pour le TP.
@Repository
public class AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public Account save(Account account) {
        accounts.put(account.getNumber(), account);
        return account;
    }

    public Optional<Account> findByNumber(String number) {
        return Optional.ofNullable(accounts.get(number));
    }

    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    public boolean existsByNumber(String number) {
        return accounts.containsKey(number);
    }
}
