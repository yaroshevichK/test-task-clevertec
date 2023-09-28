package task.clevertec.mapper.impl;

import task.clevertec.entity.Account;
import task.clevertec.entity.Bank;
import task.clevertec.entity.Currency;
import task.clevertec.entity.User;
import task.clevertec.entity.response.AccountResponse;
import task.clevertec.entity.response.BankResponse;
import task.clevertec.entity.response.CurrencyResponse;
import task.clevertec.entity.response.UserResponse;
import task.clevertec.mapper.Mapper;

import java.util.Optional;

public class AccountMapper implements Mapper<Account, AccountResponse> {
    @Override
    public Account ResponseToEntity(AccountResponse accountResponse) {
        Currency currency = Optional.ofNullable(accountResponse.getCurrency())
                .map(curr -> Currency.builder()
                        .id(curr.getId())
                        .name(curr.getName())
                        .build())
                .orElse(null);
        Bank bank = Optional.ofNullable(accountResponse.getBank())
                .map(curr -> Bank.builder()
                        .id(curr.getId())
                        .name(curr.getName())
                        .build())
                .orElse(null);
        Bank userBank = Optional.ofNullable(accountResponse.getUser())
                .map(curr -> Bank.builder()
                        .id(curr.getCurrentBank().getId())
                        .name(curr.getCurrentBank().getName())
                        .build())
                .orElse(null);
        User user = Optional.ofNullable(accountResponse.getUser())
                .map(curr -> User.builder()
                        .id(curr.getId())
                        .name(curr.getName())
                        .currentBank(userBank)
                        .build())
                .orElse(null);
        return Account.builder()
                .id(accountResponse.getId())
                .number(accountResponse.getNumber())
                .currency(currency)
                .bank(bank)
                .user(user)
                .dateOpen(accountResponse.getDateOpen())
                .balance(accountResponse.getBalance())
                .dateIncomePercent(accountResponse.getDateIncomePercent())
                .build();
    }

    @Override
    public AccountResponse entityToResponse(Account account) {
        CurrencyResponse currency = Optional.ofNullable(account.getCurrency())
                .map(curr -> CurrencyResponse.builder()
                        .id(curr.getId())
                        .name(curr.getName())
                        .build())
                .orElse(null);
        BankResponse bank = Optional.ofNullable(account.getBank())
                .map(curr -> BankResponse.builder()
                        .id(curr.getId())
                        .name(curr.getName())
                        .build())
                .orElse(null);
        BankResponse userBank = Optional.ofNullable(account.getUser())
                .map(curr -> BankResponse.builder()
                        .id(curr.getCurrentBank().getId())
                        .name(curr.getCurrentBank().getName())
                        .build())
                .orElse(null);
        UserResponse user = Optional.ofNullable(account.getUser())
                .map(curr -> UserResponse.builder()
                        .id(curr.getId())
                        .name(curr.getName())
                        .currentBank(userBank)
                        .build())
                .orElse(null);
        return AccountResponse.builder()
                .id(account.getId())
                .number(account.getNumber())
                .currency(currency)
                .bank(bank)
                .user(user)
                .dateOpen(account.getDateOpen())
                .balance(account.getBalance())
                .dateIncomePercent(account.getDateIncomePercent())
                .build();
    }
}
