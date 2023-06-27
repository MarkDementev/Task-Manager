package hexlet.code.app.controller;

import hexlet.code.app.config.SpringConfigForIT;
//import hexlet.code.app.model.User;
//
//import java.util.List;
//
//import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;

import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.utils.TestUtils;

import static hexlet.code.app.config.SpringConfigForIT.TEST_PROFILE;
//import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;
//import static hexlet.code.app.controller.UserController.ID;
//import static hexlet.code.app.utils.TestUtils.asJson;
//import static hexlet.code.app.utils.TestUtils.fromJson;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class UserControllerIT {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils utils;

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

//    @Test
//    public void createUserTest() throws Exception {
//        final var response = utils.perform(
//                    post(USER_CONTROLLER_PATH).content(asJson(utils.getTestUserDto())).contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse();
//
//        final List<User> allCreatedUsers = userRepository.findAll();
//        final User createdUser = fromJson(response.getContentAsString(), new TypeReference<>() {
//        });
//
//        assertThat(allCreatedUsers).hasSize(1);
//        assertEquals(createdUser.getFirstName(), "fname");
//        assertEquals(createdUser.getLastName(), "lname");
//        assertEquals(createdUser.getEmail(), "email@email.com");
//    }
//
//    @Test
//    public void getUserTest() throws Exception {
//        utils.createDefaultUser();
//        final User expectedUser = userRepository.findAll().get(0);
//        final var response = utils.perform(
//                        get(USER_CONTROLLER_PATH + ID, expectedUser.getId())
//                ).andExpect(status().isOk())
//                .andReturn()
//                .getResponse();
//        final User user = fromJson(response.getContentAsString(), new TypeReference<>() {
//        });
//
//        assertEquals(expectedUser.getId(), user.getId());
//        assertEquals(expectedUser.getEmail(), user.getEmail());
//        assertEquals(expectedUser.getFirstName(), user.getFirstName());
//        assertEquals(expectedUser.getLastName(), user.getLastName());
//    }
//
//    @Test
//    public void getUsersTest() throws Exception {
//        utils.createDefaultUser();
//        final var response = utils.perform(get(USER_CONTROLLER_PATH))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse();
//        final List<User> users = fromJson(response.getContentAsString(), new TypeReference<>() {
//        });
//
//        assertThat(users).hasSize(1);
//    }


//    @PutMapping(ID)
//    public User updateUser(@PathVariable final long id, @RequestBody @Valid final UserDto dto

//    @DeleteMapping(ID)
//    public void deleteUser(@PathVariable long id)



//
//
//
//    @Disabled("For now active only positive tests")
//    @Test
//    public void twiceRegTheSameUserFail() throws Exception {
//        utils.regDefaultUser().andExpect(status().isCreated());
//        utils.regDefaultUser().andExpect(status().isUnprocessableEntity());
//
//        assertEquals(1, userRepository.count());
//    }
//
//    @Test
//    public void login() throws Exception {
//        utils.regDefaultUser();
//        final LoginDto loginDto = new LoginDto(
//                utils.getTestRegistrationDto().getEmail(),
//                utils.getTestRegistrationDto().getPassword()
//        );
//        final var loginRequest = post(LOGIN).content(asJson(loginDto)).contentType(APPLICATION_JSON);
//        utils.perform(loginRequest).andExpect(status().isOk());
//    }
//
//    @Disabled("For now active only positive tests")
//    @Test
//    public void loginFail() throws Exception {
//        final LoginDto loginDto = new LoginDto(
//                utils.getTestRegistrationDto().getEmail(),
//                utils.getTestRegistrationDto().getPassword()
//        );
//        final var loginRequest = post(LOGIN).content(asJson(loginDto)).contentType(APPLICATION_JSON);
//        utils.perform(loginRequest).andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void updateUser() throws Exception {
//        utils.regDefaultUser();
//
//        final Long userId = userRepository.findByEmail(TEST_USERNAME).get().getId();
//
//        final var userDto = new UserDto(TEST_USERNAME_2, "new name", "new last name", "new pwd");
//
//        final var updateRequest = put(USER_CONTROLLER_PATH + ID, userId)
//                .content(asJson(userDto))
//                .contentType(APPLICATION_JSON);
//
//        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());
//
//        assertTrue(userRepository.existsById(userId));
//        assertNull(userRepository.findByEmail(TEST_USERNAME).orElse(null));
//        assertNotNull(userRepository.findByEmail(TEST_USERNAME_2).orElse(null));
//    }
//
//    @Test
//    public void deleteUser() throws Exception {
//        utils.regDefaultUser();
//
//        final Long userId = userRepository.findByEmail(TEST_USERNAME).get().getId();
//
//        utils.perform(delete(USER_CONTROLLER_PATH + ID, userId), TEST_USERNAME)
//                .andExpect(status().isOk());
//
//        assertEquals(0, userRepository.count());
//    }
//
//    @Disabled("For now active only positive tests")
//    @Test
//    public void deleteUserFails() throws Exception {
//        utils.regDefaultUser();
//        utils.regUser(new UserDto(
//                TEST_USERNAME_2,
//                "fname",
//                "lname",
//                "pwd"
//        ));
//
//        final Long userId = userRepository.findByEmail(TEST_USERNAME).get().getId();
//
//        utils.perform(delete(USER_CONTROLLER_PATH + ID, userId), TEST_USERNAME_2)
//                .andExpect(status().isForbidden());
//
//        assertEquals(2, userRepository.count());
//    }
}
