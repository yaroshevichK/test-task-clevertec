package task.clevertec.mapper.impl;

import task.clevertec.entity.Bank;
import task.clevertec.entity.User;
import task.clevertec.entity.response.BankResponse;
import task.clevertec.entity.response.UserResponse;
import task.clevertec.mapper.Mapper;

import java.util.Optional;

public class UserMapper implements Mapper<User, UserResponse> {
    @Override
    public User ResponseToEntity(UserResponse userResponse) {
        Bank bank = Optional.ofNullable(userResponse.getCurrentBank())
                .map(curr -> Bank.builder()
                        .id(curr.getId())
                        .name(curr.getName())
                        .build())
                .orElse(null);
        return User.builder()
                .id(userResponse.getId())
                .name(userResponse.getName())
                .currentBank(bank)
                .build();
    }

    @Override
    public UserResponse entityToResponse(User user) {
        BankResponse bank = Optional.ofNullable(user.getCurrentBank())
                .map(curr -> BankResponse.builder()
                        .id(curr.getId())
                        .name(curr.getName())
                        .build())
                .orElse(null);
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .currentBank(bank)
                .build();
    }
}
