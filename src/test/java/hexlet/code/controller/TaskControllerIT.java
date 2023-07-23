package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;

import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.repository.LabelRepository;
import hexlet.code.utils.TestUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.controller.TaskStatusController.ID;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class TaskControllerIT {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils utils;

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void createTaskTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();

        final TaskDto taskDto = new TaskDto(
                userRepository.findByFirstName("fname").getId(),
                taskStatusRepository.findByName("Новый").getId(),
                null,
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );
        final User createdUser = userRepository.findByFirstName("fname");
        final TaskStatus createdTaskStatus = taskStatusRepository.findByName("Новый");

        final var response = utils.perform(
                        post(TASK_CONTROLLER_PATH).content(asJson(taskDto))
                                .contentType(APPLICATION_JSON),
                        utils.getUserDto().getEmail()
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        response.setContentType(utils.getUTFHeader());

        final Task taskFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(taskFromResponse.getAuthor().getId(), createdUser.getId());
        assertEquals(taskFromResponse.getExecutor().getId(), createdUser.getId());
        assertEquals(taskFromResponse.getTaskStatus().getId(), createdTaskStatus.getId());
        assertEquals(taskFromResponse.getName(), utils.getFirstTaskName());
        assertEquals(taskFromResponse.getDescription(), utils.getFirstTaskDescription());
        assertNotNull(taskFromResponse.getCreatedAt());
    }

    @Test
    public void getTaskTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();

        final TaskDto taskDto = new TaskDto(
                userRepository.findByFirstName("fname").getId(),
                taskStatusRepository.findByName("Новый").getId(),
                null,
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());

        final Task expectedTask = taskRepository.findByName("Новая задача");
        final var response = utils.perform(
                        get(TASK_CONTROLLER_PATH + ID, expectedTask.getId())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        response.setContentType(utils.getUTFHeader());

        final Task taskFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedTask.getId(), taskFromResponse.getId());
        assertEquals(expectedTask.getAuthor().getId(), taskFromResponse.getAuthor().getId());
        assertEquals(expectedTask.getExecutor().getId(), taskFromResponse.getExecutor().getId());
        assertEquals(expectedTask.getTaskStatus().getId(), taskFromResponse.getTaskStatus().getId());
        assertEquals(expectedTask.getName(), taskFromResponse.getName());
        assertEquals(expectedTask.getDescription(), taskFromResponse.getDescription());
        assertNotNull(taskFromResponse.getCreatedAt());
    }

    @Test
    public void getTasksTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();

        final TaskDto taskDto = new TaskDto(
                userRepository.findByFirstName("fname").getId(),
                taskStatusRepository.findByName("Новый").getId(),
                null,
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());

        final var response = utils.perform(get(TASK_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<Task> tasksFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(tasksFromResponse).hasSize(1);
    }

    @Test
    public void updateTaskTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();
        utils.createSecondDefaultUser();

        final TaskDto taskDto = new TaskDto(
                userRepository.findByFirstName("fname").getId(),
                taskStatusRepository.findByName("Новый").getId(),
                null,
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );
        final TaskDto testSecondTaskDto = new TaskDto(
                userRepository.findByFirstName("UPDATEDfname").getId(),
                taskStatusRepository.findByName("Новый").getId(),
                null,
                utils.getSecondTaskName(),
                utils.getSecondTaskDescription()
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());

        Long createdTaskId = taskRepository.findByName("Новая задача").getId();
        final var response = utils.perform(put(TASK_CONTROLLER_PATH + ID, createdTaskId)
                                .content(asJson(testSecondTaskDto)).contentType(APPLICATION_JSON),
                        utils.getUserDto().getEmail()
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        response.setContentType(utils.getUTFHeader());

        final Task taskFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertNotNull(taskFromResponse.getId());
        assertEquals(taskFromResponse.getAuthor().getEmail(), utils.getUserDto().getEmail());
        assertEquals(taskFromResponse.getExecutor().getEmail(), utils.getSecondUserDto().getEmail());
        assertEquals(taskFromResponse.getTaskStatus().getName(), utils.getTaskStatusDto().getName());
        assertEquals(taskFromResponse.getName(), utils.getSecondTaskName());
        assertEquals(taskFromResponse.getDescription(), utils.getSecondTaskDescription());
        assertNotNull(taskFromResponse.getCreatedAt());
    }

    @Test
    public void deleteTaskTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();

        final TaskDto taskDto = new TaskDto(
                userRepository.findByFirstName("fname").getId(),
                taskStatusRepository.findByName("Новый").getId(),
                null,
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());

        Long createdTaskId = taskRepository.findByName("Новая задача").getId();

        utils.perform(delete(TASK_CONTROLLER_PATH + ID, createdTaskId),
                        utils.getUserDto().getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertNull(taskRepository.findByName("Новая задача"));
    }

    @Test
    public void createTaskWithLabelsTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();
        utils.createLabel(utils.getLabelDto(), utils.getLoginDto());

        final TaskDto taskDto = new TaskDto(
                userRepository.findByFirstName("fname").getId(),
                taskStatusRepository.findByName("Новый").getId(),
                List.of(labelRepository.findByName("Новая метка").getId()),
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );
        final User createdUser = userRepository.findByFirstName("fname");
        final TaskStatus createdTaskStatus = taskStatusRepository.findByName("Новый");
        final Label createdLabel = labelRepository.findByName("Новая метка");
        final var response = utils.perform(
                        post(TASK_CONTROLLER_PATH).content(asJson(taskDto))
                                .contentType(APPLICATION_JSON),
                        utils.getUserDto().getEmail()
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        response.setContentType(utils.getUTFHeader());

        final Task taskFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(taskFromResponse.getAuthor().getId(), createdUser.getId());
        assertEquals(taskFromResponse.getExecutor().getId(), createdUser.getId());
        assertEquals(taskFromResponse.getTaskStatus().getId(), createdTaskStatus.getId());
        assertEquals(taskFromResponse.getLabels().get(0).getId(), createdLabel.getId());
        assertEquals(taskFromResponse.getName(), utils.getFirstTaskName());
        assertEquals(taskFromResponse.getDescription(), utils.getFirstTaskDescription());
        assertNotNull(taskFromResponse.getCreatedAt());
    }

    @Test
    public void updateTaskWithLabelsTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();
        utils.createSecondDefaultUser();
        utils.createLabel(utils.getLabelDto(), utils.getLoginDto());

        final TaskDto taskDto = new TaskDto(
                userRepository.findByFirstName("fname").getId(),
                taskStatusRepository.findByName("Новый").getId(),
                null,
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );
        final TaskDto testSecondTaskDto = new TaskDto(
                userRepository.findByFirstName("UPDATEDfname").getId(),
                taskStatusRepository.findByName("Новый").getId(),
                List.of(labelRepository.findByName("Новая метка").getId()),
                utils.getSecondTaskName(),
                utils.getSecondTaskDescription()
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());

        Long createdTaskId = taskRepository.findByName("Новая задача").getId();
        final Label createdLabel = labelRepository.findByName("Новая метка");
        final var response = utils.perform(put(TASK_CONTROLLER_PATH + ID, createdTaskId)
                                .content(asJson(testSecondTaskDto)).contentType(APPLICATION_JSON),
                        utils.getUserDto().getEmail()
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        response.setContentType(utils.getUTFHeader());

        final Task taskFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertNotNull(taskFromResponse.getId());
        assertEquals(taskFromResponse.getAuthor().getEmail(), utils.getUserDto().getEmail());
        assertEquals(taskFromResponse.getExecutor().getEmail(), utils.getSecondUserDto().getEmail());
        assertEquals(taskFromResponse.getTaskStatus().getName(), utils.getTaskStatusDto().getName());
        assertEquals(taskFromResponse.getLabels().get(0).getId(), createdLabel.getId());
        assertEquals(taskFromResponse.getName(), utils.getSecondTaskName());
        assertEquals(taskFromResponse.getDescription(), utils.getSecondTaskDescription());
        assertNotNull(taskFromResponse.getCreatedAt());
    }

    @Test
    public void getFilteredTasksTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();
        utils.createLabel(utils.getLabelDto(), utils.getLoginDto());
        utils.createLabel(utils.getSecondLabelDto(), utils.getLoginDto());

        final TaskDto taskDto = new TaskDto(
                userRepository.findByFirstName("fname").getId(),
                taskStatusRepository.findByName("Новый").getId(),
                List.of(labelRepository.findByName("Новая метка").getId()),
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );
        final TaskDto secondTaskDto = new TaskDto(
                userRepository.findByFirstName("fname").getId(),
                taskStatusRepository.findByName("Новый").getId(),
                List.of(labelRepository.findByName("Баг").getId()),
                utils.getSecondTaskName(),
                utils.getSecondTaskDescription()
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());
        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(secondTaskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());

        final var response = utils.perform(
                        get(TASK_CONTROLLER_PATH)
                                .param("taskStatus", String.valueOf(
                                        taskStatusRepository.findByName("Новый").getId()))
                                .param("executorId", String.valueOf(
                                        userRepository.findByFirstName("fname").getId()))
                                .param("labelsId", String.valueOf(
                                        labelRepository.findByName("Новая метка").getId())),
                        utils.getUserDto().getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Task> tasksFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(tasksFromResponse).hasSize(1);
    }
}
