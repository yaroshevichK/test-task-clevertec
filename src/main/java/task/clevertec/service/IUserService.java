package task.clevertec.service;

import task.clevertec.entity.User;

public interface IUserService extends IService<User>{
    User getUser(String username);
}
