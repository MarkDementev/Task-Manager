package hexlet.code.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.dto.LoginDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import hexlet.code.app.component.JWTHelper;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.TaskRepository;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static hexlet.code.app.config.security.SecurityConfig.LOGIN;
import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
//import static hexlet.code.app.controller.UserController.ID;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class TestUtils {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JWTHelper jwtHelper;
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public void tearDown() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }

    private final UserDto userDto = new UserDto(
            "email@email.com",
            "fname",
            "lname",
            "pwd"
    );

    private final UserDto secondUserDto = new UserDto(
            "UPDATEDemail@email.com",
            "UPDATEDfname",
            "UPDATEDlname",
            "UPDATEDpwd"
    );

    private final LoginDto loginDto = new LoginDto(
            getUserDto().getEmail(),
            getUserDto().getPassword()
    );

    private final TaskStatusDto taskStatusDto = new TaskStatusDto(
            "Новый"
    );

    private final TaskStatusDto secondTaskStatusDto = new TaskStatusDto(
            "В работе"
    );

    public UserDto getUserDto() {
        return userDto;
    }

    public UserDto getSecondUserDto() {
        return secondUserDto;
    }

    public LoginDto getLoginDto() {
        return loginDto;
    }

    public TaskStatusDto getTaskStatusDto() {
        return taskStatusDto;
    }

    public TaskStatusDto getSecondTaskStatusDto() {
        return secondTaskStatusDto;
    }

    public ResultActions createDefaultUser() throws Exception {
        return createUser(userDto);
    }

    public ResultActions createSecondDefaultUser() throws Exception {
        return createUser(secondUserDto);
    }

    public ResultActions createDefaultUserLoginTaskStatus() throws Exception {
        createDefaultUser();
        return createTaskStatus(taskStatusDto, getLoginDto());
    }

    public ResultActions createUser(final UserDto dto) throws Exception {
        final var request = post(USER_CONTROLLER_PATH)
                .content(asJson(dto))
                .contentType(APPLICATION_JSON);

        return perform(request);
    }

    public ResultActions createTaskStatus(final TaskStatusDto dto, final LoginDto loginDto) throws Exception {
        perform(post(LOGIN).content(asJson(loginDto)).contentType(APPLICATION_JSON));

        return perform(post(TASK_STATUS_CONTROLLER_PATH)
                .content(asJson(dto)).contentType(APPLICATION_JSON), loginDto.getEmail());
    }

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request, final String byUser) throws Exception {
        final String token = jwtHelper.expiring(Map.of("username", byUser));
        request.header(AUTHORIZATION, token);

        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }
}
