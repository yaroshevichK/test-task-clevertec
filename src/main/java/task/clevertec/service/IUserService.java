package task.clevertec.service;

import task.clevertec.entity.User;
import task.clevertec.entity.response.UserResponse;

import java.util.List;

public interface IUserService extends IService<User>{
    User getUser(String username);

    List<UserResponse> getAllUsers();

    UserResponse findUserById(Integer id);

    boolean saveUser(UserResponse userResponse);

    boolean updateUser(UserResponse userResponse);

    boolean deleteUserById(Integer id);
}
