package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;

import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.dto.TaskToUpdateDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.repository.LabelRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
                userRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                null,
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );
        final User createdUser = userRepository.findAll().get(0);
        final TaskStatus createdTaskStatus = taskStatusRepository.findAll().get(0);

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
        final List<Task> allCreatedTasks = taskRepository.findAll();

        assertThat(allCreatedTasks).hasSize(1);
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
                userRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                null,
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());

        final Task expectedTask = taskRepository.findAll().get(0);
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
                userRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                taskStatusRepository.findAll().get(0).getId(),
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
                userRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                null,
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );
        final TaskToUpdateDto testSecondTaskDto = new TaskToUpdateDto(
                userRepository.findAll().get(1).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                null,
                utils.getSecondTaskName(),
                utils.getSecondTaskDescription()
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());

        Long createdTaskId = taskRepository.findAll().get(0).getId();
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
        final List<Task> allTasks = taskRepository.findAll();

        assertThat(allTasks).hasSize(1);
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
                userRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                null,
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());

        Long createdTaskId = taskRepository.findAll().get(0).getId();

        utils.perform(delete(TASK_CONTROLLER_PATH + ID, createdTaskId),
                        utils.getUserDto().getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Task> allTasks = taskRepository.findAll();

        assertThat(allTasks).hasSize(0);
    }

    @Test
    public void createTaskWithLabelsTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();
        utils.createLabel(utils.getLabelDto(), utils.getLoginDto());

        final TaskDto taskDto = new TaskDto(
                userRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                List.of(labelRepository.findAll().get(0).getId()),
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );
        final User createdUser = userRepository.findAll().get(0);
        final TaskStatus createdTaskStatus = taskStatusRepository.findAll().get(0);
        final Label createdLabel = labelRepository.findAll().get(0);
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
        final List<Task> allCreatedTasks = taskRepository.findAll();

        assertThat(allCreatedTasks).hasSize(1);
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
                userRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                null,
                utils.getFirstTaskName(),
                utils.getFirstTaskDescription()
        );
        final TaskToUpdateDto testSecondTaskDto = new TaskToUpdateDto(
                userRepository.findAll().get(1).getId(),
                taskStatusRepository.findAll().get(0).getId(),
                List.of(labelRepository.findAll().get(0).getId()),
                utils.getSecondTaskName(),
                utils.getSecondTaskDescription()
        );

        utils.perform(post(TASK_CONTROLLER_PATH)
                .content(asJson(taskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());

        Long createdTaskId = taskRepository.findAll().get(0).getId();
        final Label createdLabel = labelRepository.findAll().get(0);
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
        final List<Task> allTasks = taskRepository.findAll();

        assertThat(allTasks).hasSize(1);
        assertNotNull(taskFromResponse.getId());
        assertEquals(taskFromResponse.getAuthor().getEmail(), utils.getUserDto().getEmail());
        assertEquals(taskFromResponse.getExecutor().getEmail(), utils.getSecondUserDto().getEmail());
        assertEquals(taskFromResponse.getTaskStatus().getName(), utils.getTaskStatusDto().getName());
        assertEquals(taskFromResponse.getLabels().get(0).getId(), createdLabel.getId());
        assertEquals(taskFromResponse.getName(), utils.getSecondTaskName());
        assertEquals(taskFromResponse.getDescription(), utils.getSecondTaskDescription());
        assertNotNull(taskFromResponse.getCreatedAt());
    }

//    @Test
//    public void getFilteredTasksTest() throws Exception {
//        utils.createDefaultUserLoginTaskStatus();
//        utils.createLabel(utils.getLabelDto(), utils.getLoginDto());
//
//        final TaskDto taskDto = new TaskDto(
//                userRepository.findAll().get(0).getId(),
//                userRepository.findAll().get(0).getId(),
//                taskStatusRepository.findAll().get(0).getId(),
//                List.of(labelRepository.findAll().get(0).getId()),
//                utils.getFirstTaskName(),
//                utils.getFirstTaskDescription()
//        );
//        final TaskDto secondTaskDto = new TaskDto(
//                userRepository.findAll().get(0).getId(),
//                userRepository.findAll().get(0).getId(),
//                taskStatusRepository.findAll().get(0).getId(),
//                List.of(labelRepository.findAll().get(0).getId()),
//                utils.getSecondTaskName(),
//                utils.getSecondTaskDescription()
//        );
//
//        utils.perform(post(TASK_CONTROLLER_PATH)
//                .content(asJson(taskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());
//        utils.perform(post(TASK_CONTROLLER_PATH)
//                .content(asJson(secondTaskDto)).contentType(APPLICATION_JSON), utils.getUserDto().getEmail());
//
//        final var response = utils.perform(
//                get(TASK_CONTROLLER_PATH)
//                        .param("taskStatus", String.valueOf(taskStatusRepository.findAll().get(0).getId()))
//                        .param("executorId", String.valueOf(userRepository.findAll().get(0).getId()))
//                        .param("labels", String.valueOf(labelRepository.findAll().get(0).getId())),
//                        utils.getUserDto().getEmail())
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse();
//
//        final List<Task> tasksFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
//        });
//
//        assertThat(tasksFromResponse).hasSize(1);
//    }
}
