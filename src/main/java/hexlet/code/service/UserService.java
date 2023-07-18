package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;

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
