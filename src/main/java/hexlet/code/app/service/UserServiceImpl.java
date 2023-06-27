package hexlet.code.app.service;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;

import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUser(long id) {
        //идея - возвращать не самого пользователя, а возвращать его
        //потом создавать нового, и прогнять через ДТО, чтобы
        //почистить инфу и не показывать пароль
        return userRepository.findById(id).get();
    }

    @Override
    public Iterable<User> getUsers() {
        //сделать как в идее выше, только итерируясь по листу?
        return userRepository.findAll();
    }

    @Override
    public User createNewUser(UserDto userDto) {
        final User newUser = new User();

        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(userDto.getPassword());
//        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(long id, UserDto userDto) {
        final User userToUpdate = userRepository.findById(id).get();

        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setLastName(userDto.getLastName());
        userToUpdate.setEmail(userDto.getEmail());
        userToUpdate.setPassword(userDto.getPassword());
//        userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUser(long id) {
        final User userToDelete = userRepository.findById(id).get();

        userRepository.delete(userToDelete);
    }
}
