package task.clevertec.repository;

import task.clevertec.entity.User;

import java.util.List;

public interface IDaoUser extends IDao<User> {
    User getUserByUsername(String username);

    List<User> getAllUsers();

    User getUserById(Integer id);

    boolean saveUser(User user);

    boolean updateUser(User user);

    boolean deleteUserById(Integer id);
}
