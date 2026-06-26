package com.ynov.bank.service;

import com.ynov.bank.dto.CreateAccountRequest;
import com.ynov.bank.exception.AccountAlreadyExistsException;
import com.ynov.bank.exception.AccountNotFoundException;
import com.ynov.bank.exception.InsufficientFundsException;
import com.ynov.bank.exception.InvalidAmountException;
import com.ynov.bank.model.Account;
import com.ynov.bank.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;

    // --- creation ---

    @Test
    void create_succeedsWithZeroBalance() {
        CreateAccountRequest request = createRequest("ACC-1", "Alice");
        when(repository.existsByNumber("ACC-1")).thenReturn(false);
        when(repository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        Account result = service.create(request);

        assertThat(result.getNumber()).isEqualTo("ACC-1");
        assertThat(result.getOwner()).isEqualTo("Alice");
        assertThat(result.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void create_throwsWhenNumberAlreadyExists() {
        when(repository.existsByNumber("ACC-1")).thenReturn(true);

        assertThatThrownBy(() -> service.create(createRequest("ACC-1", "Alice")))
                .isInstanceOf(AccountAlreadyExistsException.class);

        verify(repository, never()).save(any());
    }

    // --- consultation ---

    @Test
    void getByNumber_returnsAccountWhenItExists() {
        when(repository.findByNumber("ACC-1")).thenReturn(Optional.of(account("ACC-1", "Alice", "100")));

        Account result = service.getByNumber("ACC-1");

        assertThat(result.getNumber()).isEqualTo("ACC-1");
    }

    @Test
    void getByNumber_throwsWhenAccountIsMissing() {
        when(repository.findByNumber("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByNumber("UNKNOWN"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void getAll_returnsEveryAccount() {
        when(repository.findAll()).thenReturn(List.of(
                account("ACC-1", "Alice", "100"),
                account("ACC-2", "Bob", "200")));

        assertThat(service.getAll()).hasSize(2);
    }

    // --- depot ---

    @Test
    void deposit_increasesBalance() {
        when(repository.findByNumber("ACC-1")).thenReturn(Optional.of(account("ACC-1", "Alice", "100")));
        when(repository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        Account result = service.deposit("ACC-1", new BigDecimal("50"));

        assertThat(result.getBalance()).isEqualByComparingTo("150");
    }

    @Test
    void deposit_throwsWhenAmountIsZero() {
        assertThatThrownBy(() -> service.deposit("ACC-1", BigDecimal.ZERO))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void deposit_throwsWhenAmountIsNegative() {
        assertThatThrownBy(() -> service.deposit("ACC-1", new BigDecimal("-10")))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).save(any());
    }

    // --- retrait ---

    @Test
    void withdraw_decreasesBalance() {
        when(repository.findByNumber("ACC-1")).thenReturn(Optional.of(account("ACC-1", "Alice", "100")));
        when(repository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        Account result = service.withdraw("ACC-1", new BigDecimal("40"));

        assertThat(result.getBalance()).isEqualByComparingTo("60");
    }

    @Test
    void withdraw_throwsWhenAmountIsZero() {
        assertThatThrownBy(() -> service.withdraw("ACC-1", BigDecimal.ZERO))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void withdraw_throwsWhenAmountIsNegative() {
        assertThatThrownBy(() -> service.withdraw("ACC-1", new BigDecimal("-5")))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void withdraw_throwsWhenFundsAreInsufficient() {
        // solde de 30, on essaie de retirer 50 -> doit refuser
        when(repository.findByNumber("ACC-1")).thenReturn(Optional.of(account("ACC-1", "Alice", "30")));

        assertThatThrownBy(() -> service.withdraw("ACC-1", new BigDecimal("50")))
                .isInstanceOf(InsufficientFundsException.class);
        verify(repository, never()).save(any());
    }

    // --- virement ---

    @Test
    void transfer_movesFundsBetweenAccounts() {
        Account from = account("ACC-1", "Alice", "100");
        Account to = account("ACC-2", "Bob", "20");
        when(repository.findByNumber("ACC-1")).thenReturn(Optional.of(from));
        when(repository.findByNumber("ACC-2")).thenReturn(Optional.of(to));
        when(repository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        service.transfer("ACC-1", "ACC-2", new BigDecimal("30"));

        assertThat(from.getBalance()).isEqualByComparingTo("70");
        assertThat(to.getBalance()).isEqualByComparingTo("50");
        verify(repository, times(2)).save(any(Account.class)); // les deux comptes sont sauvegardes
    }

    @Test
    void transfer_throwsWhenAmountIsZero() {
        assertThatThrownBy(() -> service.transfer("ACC-1", "ACC-2", BigDecimal.ZERO))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void transfer_throwsWhenAmountIsNegative() {
        assertThatThrownBy(() -> service.transfer("ACC-1", "ACC-2", new BigDecimal("-1")))
                .isInstanceOf(InvalidAmountException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void transfer_throwsWhenSourceHasInsufficientFunds() {
        when(repository.findByNumber("ACC-1")).thenReturn(Optional.of(account("ACC-1", "Alice", "10")));
        when(repository.findByNumber("ACC-2")).thenReturn(Optional.of(account("ACC-2", "Bob", "0")));

        assertThatThrownBy(() -> service.transfer("ACC-1", "ACC-2", new BigDecimal("50")))
                .isInstanceOf(InsufficientFundsException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void transfer_throwsWhenTargetAccountDoesNotExist() {
        when(repository.findByNumber("ACC-1")).thenReturn(Optional.of(account("ACC-1", "Alice", "100")));
        when(repository.findByNumber("ACC-2")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.transfer("ACC-1", "ACC-2", new BigDecimal("50")))
                .isInstanceOf(AccountNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void transfer_throwsWhenSourceAccountDoesNotExist() {
        when(repository.findByNumber("ACC-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.transfer("ACC-1", "ACC-2", new BigDecimal("50")))
                .isInstanceOf(AccountNotFoundException.class);
        verify(repository, never()).save(any());
    }

    // petits helpers pour ne pas repeter la creation d'objets

    private CreateAccountRequest createRequest(String number, String owner) {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setNumber(number);
        request.setOwner(owner);
        return request;
    }

    private Account account(String number, String owner, String balance) {
        Account account = new Account(number, owner);
        account.setBalance(new BigDecimal(balance));
        return account;
    }
}
