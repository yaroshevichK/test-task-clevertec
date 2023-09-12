package task.clevertec.repository;

import task.clevertec.entity.User;

public interface IDaoUser extends IDao<User> {
    User getUserByUsername(String username);
}
