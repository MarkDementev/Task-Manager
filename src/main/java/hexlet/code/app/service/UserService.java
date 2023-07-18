package hexlet.code.app.service;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.model.User;

import java.util.List;

public interface UserService {
    User getUser(long id);
    List<User> getUsers();
    User createNewUser(UserDto userDto);
    User updateUser(long id, UserDto userDto);
    void deleteUser(long id);
    String getCurrentUserName();
    User getCurrentUser();
}
