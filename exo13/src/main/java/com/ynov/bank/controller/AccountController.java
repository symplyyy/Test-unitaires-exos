package com.ynov.bank.controller;

import com.ynov.bank.dto.AmountRequest;
import com.ynov.bank.dto.CreateAccountRequest;
import com.ynov.bank.dto.TransferRequest;
import com.ynov.bank.model.Account;
import com.ynov.bank.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody CreateAccountRequest request) {
        Account created = service.create(request);
        // 201 quand on cree quelque chose
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Account> getAll() {
        return service.getAll();
    }

    @GetMapping("/{number}")
    public Account getOne(@PathVariable String number) {
        return service.getByNumber(number);
    }

    @PostMapping("/{number}/deposit")
    public Account deposit(@PathVariable String number, @RequestBody AmountRequest request) {
        return service.deposit(number, request.getAmount());
    }

    @PostMapping("/{number}/withdraw")
    public Account withdraw(@PathVariable String number, @RequestBody AmountRequest request) {
        return service.withdraw(number, request.getAmount());
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
        service.transfer(request.getFrom(), request.getTo(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}
