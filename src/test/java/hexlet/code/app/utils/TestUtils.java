package hexlet.code.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.repository.UserRepository;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;

@Component
public class TestUtils {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public void tearDown() {
        userRepository.deleteAll();
    }

    private final UserDto testUserDto = new UserDto(
            "email@email.com",
            "fname",
            "lname",
            "pwd"
    );

    private final UserDto testSecondUserDto = new UserDto(
            "UPDATEDemail@email.com",
            "UPDATEDfname",
            "UPDATEDlname",
            "UPDATEDpwd"
    );

    public UserDto getTestUserDto() {
        return testUserDto;
    }

    public UserDto getTestSecondUserDto() {
        return testSecondUserDto;
    }

    public ResultActions createDefaultUser() throws Exception {
        return createUser(testUserDto);
    }

    public ResultActions createUser(final UserDto dto) throws Exception {
        final var request = post(USER_CONTROLLER_PATH)
                .content(asJson(dto))
                .contentType(APPLICATION_JSON);

        return perform(request);
    }

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }
}
