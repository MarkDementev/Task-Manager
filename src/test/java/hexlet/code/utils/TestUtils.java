package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import hexlet.code.dto.UserDto;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.LoginDto;
import hexlet.code.dto.LabelDto;
import hexlet.code.component.JWTHelper;
import hexlet.code.repository.UserRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.LabelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static hexlet.code.config.security.SecurityConfig.LOGIN;
import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class TestUtils {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JWTHelper jwtHelper;
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();
    private static final String UTF_HEADER = "application/json;charset=UTF-8";
    private static final String FIRST_TASK_NAME = "Новая задача";
    private static final String FIRST_TASK_DESCRIPTION = "Описание новой задачи";
    private static final String SECOND_TASK_NAME = "Новое имя";
    private static final String SECOND_TASK_DESCRIPTION = "Новое описание";
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
    private final LabelDto labelDto = new LabelDto(
            "Новая метка"
    );
    private final LabelDto secondLabelDto = new LabelDto(
            "Баг"
    );

    public void tearDown() {
        taskRepository.deleteAll();
        labelRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }

    public String getUTFHeader() {
        return UTF_HEADER;
    }

    public String getFirstTaskName() {
        return FIRST_TASK_NAME;
    }

    public String getFirstTaskDescription() {
        return FIRST_TASK_DESCRIPTION;
    }

    public String getSecondTaskName() {
        return SECOND_TASK_NAME;
    }

    public String getSecondTaskDescription() {
        return SECOND_TASK_DESCRIPTION;
    }

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

    public LabelDto getLabelDto() {
        return labelDto;
    }

    public LabelDto getSecondLabelDto() {
        return secondLabelDto;
    }

    public ResultActions createDefaultUser() throws Exception {
        return createUser(userDto);
    }

    public ResultActions createSecondDefaultUser() throws Exception {
        return createUser(secondUserDto);
    }

    public ResultActions createDefaultUserLoginTaskStatus() throws Exception {
        createDefaultUser();
        return createTaskStatus(taskStatusDto, loginDto);
    }

    public ResultActions createDefaultUserLoginLabel() throws Exception {
        createDefaultUser();
        return createLabel(labelDto, loginDto);
    }

    public ResultActions createUser(final UserDto dto) throws Exception {
        final var request = post(USER_CONTROLLER_PATH)
                .content(asJson(dto))
                .contentType(APPLICATION_JSON);

        return perform(request);
    }

    public ResultActions createLabel(final LabelDto dto, final LoginDto loginDto) throws Exception {
        login(loginDto);

        return perform(post(LABEL_CONTROLLER_PATH)
                .content(asJson(dto)).contentType(APPLICATION_JSON), loginDto.getEmail());
    }

    public ResultActions login(final LoginDto loginDto) throws Exception {
        return perform(post(LOGIN).content(asJson(loginDto)).contentType(APPLICATION_JSON));
    }

    public ResultActions createTaskStatus(final TaskStatusDto dto, final LoginDto loginDto) throws Exception {
        login(loginDto);

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
