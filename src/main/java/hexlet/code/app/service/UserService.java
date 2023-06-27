package hexlet.code.app.service;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.model.User;

public interface UserService {
    User getUser(long id);

    Iterable<User> getUsers();

    User createNewUser(UserDto userDto);

    User updateUser(long id, UserDto userDto);

    void deleteUser(long id);
}
