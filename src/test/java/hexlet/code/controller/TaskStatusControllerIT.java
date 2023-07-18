package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;

import hexlet.code.config.SpringConfigForIT;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
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
import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;

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
public class TaskStatusControllerIT {
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
                        post(TASK_STATUS_CONTROLLER_PATH).content(asJson(utils.getTaskStatusDto()))
                                .contentType(APPLICATION_JSON),
                        utils.getUserDto().getEmail()
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        response.setContentType(utils.getUTFHeader());

        final List<TaskStatus> allCreatedTaskStatuses = taskStatusRepository.findAll();
        final TaskStatus createdTaskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(allCreatedTaskStatuses).hasSize(1);
        assertNotNull(createdTaskStatus.getId());
        assertEquals(createdTaskStatus.getName(), utils.getTaskStatusDto().getName());
        assertNotNull(createdTaskStatus.getCreatedAt());
    }

    @Test
    public void getTaskStatusTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();

        final TaskStatus expectedTaskStatus = taskStatusRepository.findByName("Новый");
        final var response = utils.perform(
                        get(TASK_STATUS_CONTROLLER_PATH + ID, expectedTaskStatus.getId())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        response.setContentType(utils.getUTFHeader());

        final TaskStatus createdTaskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedTaskStatus.getId(), createdTaskStatus.getId());
        assertEquals(expectedTaskStatus.getName(), createdTaskStatus.getName());
        assertNotNull(createdTaskStatus.getCreatedAt());
    }

    @Test
    public void getTaskStatusesTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();

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
        utils.createDefaultUserLoginTaskStatus();

        Long createdTaskStatusId = taskStatusRepository.findByName("Новый").getId();
        final var response = utils.perform(put(TASK_STATUS_CONTROLLER_PATH + ID, createdTaskStatusId)
                                .content(asJson(utils.getSecondTaskStatusDto())).contentType(APPLICATION_JSON),
                        utils.getUserDto().getEmail()
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        response.setContentType(utils.getUTFHeader());

        final TaskStatus taskStatusFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final List<TaskStatus> allTaskStatuses = taskStatusRepository.findAll();

        assertThat(allTaskStatuses).hasSize(1);
        assertEquals(taskStatusFromResponse.getId(), createdTaskStatusId);
        assertEquals(taskStatusFromResponse.getName(), utils.getSecondTaskStatusDto().getName());
        assertNotNull(taskStatusFromResponse.getCreatedAt());
    }

    @Test
    public void deleteTaskStatusTest() throws Exception {
        utils.createDefaultUserLoginTaskStatus();

        Long createdTaskStatusId = taskStatusRepository.findByName("Новый").getId();

        utils.perform(delete(TASK_STATUS_CONTROLLER_PATH + ID, createdTaskStatusId),
                        utils.getUserDto().getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<TaskStatus> allTaskStatuses = taskStatusRepository.findAll();

        assertThat(allTaskStatuses).hasSize(0);
    }
}
