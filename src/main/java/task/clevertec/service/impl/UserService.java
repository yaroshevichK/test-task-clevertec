package task.clevertec.service.impl;

import task.clevertec.entity.User;
import task.clevertec.repository.IDaoUser;
import task.clevertec.repository.impl.DaoUser;
import task.clevertec.service.IUserService;

public class UserService implements IUserService {
    private final IDaoUser daoUser = new DaoUser();

    @Override
    public User getUser(String username) {
        return daoUser.getUserByUsername(username);
    }
}
