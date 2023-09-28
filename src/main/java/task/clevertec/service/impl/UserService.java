package task.clevertec.service.impl;

import task.clevertec.entity.User;
import task.clevertec.entity.response.UserResponse;
import task.clevertec.mapper.Mapper;
import task.clevertec.mapper.impl.UserMapper;
import task.clevertec.repository.IDaoUser;
import task.clevertec.repository.impl.DaoUser;
import task.clevertec.service.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService implements IUserService {
    private final IDaoUser daoUser = new DaoUser();
    private final Mapper<User, UserResponse> userMapper =
            new UserMapper();

    @Override
    public User getUser(String username) {
        return daoUser.getUserByUsername(username);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = daoUser.getAllUsers();
        return Optional.ofNullable(users)
                .orElse(new ArrayList<>())
                .stream()
                .map(userMapper::entityToResponse)
                .toList();
    }

    @Override
    public UserResponse findUserById(Integer id) {
        User user = daoUser.getUserById(id);
        return Optional.ofNullable(user)
                .map(userMapper::entityToResponse)
                .orElse(null);
    }

    @Override
    public boolean saveUser(UserResponse userResponse) {
        return daoUser.saveUser(userMapper.ResponseToEntity(userResponse));
    }

    @Override
    public boolean updateUser(UserResponse userResponse) {
        return daoUser.updateUser(userMapper.ResponseToEntity(userResponse));
    }

    @Override
    public boolean deleteUserById(Integer id) {
        return daoUser.deleteUserById(id);
    }


}
