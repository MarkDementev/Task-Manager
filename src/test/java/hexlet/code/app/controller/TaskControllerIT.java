package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;

import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.dto.TaskToUpdateDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.utils.TestUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.app.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.app.controller.TaskStatusController.ID;
import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.app.utils.TestUtils.asJson;
import static hexlet.code.app.utils.TestUtils.fromJson;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                userRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                "Новая задача",
                "Описание новой задачи"
        );
        final User createdUser = userRepository.findAll().get(0);
        final TaskStatus createdTaskStatus = taskStatusRepository.findAll().get(0);

        final var response = utils.perform(
                        post(TASK_CONTROLLER_PATH).content(asJson(taskDto))
                                .contentType(APPLICATION_JSON),
                        "email@email.com"
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        response.setContentType("application/json;charset=UTF-8");

        final Task createdTasks = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final List<Task> allCreatedTasks = taskRepository.findAll();

        assertThat(allCreatedTasks).hasSize(1);
        assertEquals(createdTasks.getAuthor().getId(), createdUser.getId());
        assertEquals(createdTasks.getExecutor().getId(), createdUser.getId());
        assertEquals(createdTasks.getTaskStatus().getId(), createdTaskStatus.getId());
        assertEquals(createdTasks.getName(), "Новая задача");
        assertEquals(createdTasks.getDescription(), "Описание новой задачи");
    }

    @Test
    public void getTaskTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();

        final TaskDto taskDto = new TaskDto(
                userRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                "Новая задача",
                "Описание новой задачи"
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), "email@email.com");

        final Task expectedTask = taskRepository.findAll().get(0);
        final var response = utils.perform(
                        get(TASK_CONTROLLER_PATH + ID, expectedTask.getId())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        response.setContentType("application/json;charset=UTF-8");

        final Task task = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedTask.getId(), task.getId());
        assertEquals(expectedTask.getAuthor().getId(), task.getAuthor().getId());
        assertEquals(expectedTask.getExecutor().getId(), task.getExecutor().getId());
        assertEquals(expectedTask.getTaskStatus().getId(), task.getTaskStatus().getId());
        assertEquals(expectedTask.getName(), task.getName());
        assertEquals(expectedTask.getDescription(), task.getDescription());
    }

    @Test
    public void getTasksTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();

        final TaskDto taskDto = new TaskDto(
                userRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                "Новая задача",
                "Описание новой задачи"
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), "email@email.com");

        final var response = utils.perform(get(TASK_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<Task> tasks = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(tasks).hasSize(1);
    }

    @Test
    public void updateTaskTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();
        utils.createSecondDefaultUser();

        final TaskDto taskDto = new TaskDto(
                userRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                "Новая задача",
                "Описание новой задачи"
        );
        final TaskToUpdateDto testSecondTaskDto = new TaskToUpdateDto(
                this.userRepository.findAll().get(1).getId(),
                this.taskStatusRepository.findAll().get(0).getId(),
                "Новое имя",
                "Новое описание"
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), "email@email.com");

        Long createdTaskId = taskRepository.findAll().get(0).getId();
        final var response = utils.perform(put(TASK_CONTROLLER_PATH + ID, createdTaskId)
                                .content(asJson(testSecondTaskDto)).contentType(APPLICATION_JSON),
                        "email@email.com"
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        response.setContentType("application/json;charset=UTF-8");

        final Task task = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final List<Task> allTasks = taskRepository.findAll();

        assertThat(allTasks).hasSize(1);
        assertEquals(task.getAuthor().getEmail(), "email@email.com");
        assertEquals(task.getExecutor().getEmail(), "UPDATEDemail@email.com");
        assertEquals(task.getTaskStatus().getName(), "Новый");
        assertEquals(task.getName(), "Новое имя");
        assertEquals(task.getDescription(), "Новое описание");
    }

    @Test
    public void deleteTaskTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();

        final TaskDto taskDto = new TaskDto(
                userRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                "Новая задача",
                "Описание новой задачи"
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), "email@email.com");

        Long createdTaskId = taskRepository.findAll().get(0).getId();

        utils.perform(delete(TASK_CONTROLLER_PATH + ID, createdTaskId),
                        "email@email.com")
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Task> allTasks = taskRepository.findAll();

        assertThat(allTasks).hasSize(0);
    }
}
