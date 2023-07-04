package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;

import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
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
import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
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
public class TaskStatusIT {
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TestUtils utils;

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void createTaskStatusTest() throws Exception {
        utils.createDefaultUser();

        final var response = utils.perform(
                        post(TASK_STATUS_CONTROLLER_PATH).content(asJson(utils.getTestTaskStatusDto()))
                                .contentType(APPLICATION_JSON),
                        "email@email.com"
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        response.setContentType("application/json;charset=UTF-8");
        final List<TaskStatus> allCreatedTaskStatuses = taskStatusRepository.findAll();
        final TaskStatus createdTaskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(allCreatedTaskStatuses).hasSize(1);
        assertEquals(createdTaskStatus.getName(), "Новый");
    }

    @Test
    public void getTaskStatusTest() throws Exception {
        utils.createDefaultTaskStatus();

        final TaskStatus expectedTaskStatus = taskStatusRepository.findAll().get(0);
        final var response = utils.perform(
                        get(TASK_STATUS_CONTROLLER_PATH + ID, expectedTaskStatus.getId())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();
        response.setContentType("application/json;charset=UTF-8");
        final TaskStatus taskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedTaskStatus.getId(), taskStatus.getId());
        assertEquals(expectedTaskStatus.getName(), taskStatus.getName());
    }

    @Test
    public void getTaskStatusesTest() throws Exception {
        utils.createDefaultTaskStatus();

        final var response = utils.perform(get(TASK_STATUS_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<TaskStatus> taskStatuses = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(taskStatuses).hasSize(1);
    }

    @Test
    public void updateTaskStatusTest() throws Exception {
        utils.createDefaultTaskStatus();

        Long createdTaskStatusId = taskStatusRepository.findAll().get(0).getId();
        final var response = utils.perform(put(TASK_STATUS_CONTROLLER_PATH + ID, createdTaskStatusId)
                                .content(asJson(utils.getTestSecondTaskStatusDto())).contentType(APPLICATION_JSON),
                                "email@email.com"
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        response.setContentType("application/json;charset=UTF-8");
        final TaskStatus taskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final List<TaskStatus> allTaskStatuses = taskStatusRepository.findAll();

        assertThat(allTaskStatuses).hasSize(1);
        assertEquals(taskStatus.getId(), createdTaskStatusId);
        assertEquals(taskStatus.getName(), "В работе");
    }

    @Test
    public void deleteTaskStatusTest() throws Exception {
        utils.createDefaultTaskStatus();

        Long createdTaskStatusId = taskStatusRepository.findAll().get(0).getId();
        final var response = utils.perform(delete(TASK_STATUS_CONTROLLER_PATH + ID, createdTaskStatusId),
                        "email@email.com")
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<TaskStatus> allTaskStatuses = taskStatusRepository.findAll();

        assertThat(allTaskStatuses).hasSize(0);
    }
}
