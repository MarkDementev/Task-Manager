package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;

import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.utils.TestUtils;
import hexlet.code.app.model.Label;

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

import static hexlet.code.app.controller.LabelController.LABEL_CONTROLLER_PATH;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class LabelControllerIT {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TestUtils utils;

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void createLabelTest() throws Exception {
        utils.createDefaultUser();
        utils.login(utils.getLoginDto());

        final var response = utils.perform(
                post(LABEL_CONTROLLER_PATH).content(asJson(utils.getLabelDto()))
                        .contentType(APPLICATION_JSON),
                        utils.getUserDto().getEmail()
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

                response.setContentType("application/json;charset=UTF-8");

        final Label createdLabel = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final List<Label> allCreatedLabels = labelRepository.findAll();

        assertThat(allCreatedLabels).hasSize(1);
        assertEquals(createdLabel.getName(), utils.getLabelDto().getName());
    }

    @Test
    public void getLabelTest() throws Exception {
        utils.createDefaultUserLoginLabel();

        final Label expectedLabel = labelRepository.findAll().get(0);
        final var response = utils.perform(
                        get(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId()),
                        utils.getUserDto().getEmail()
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

                response.setContentType("application/json;charset=UTF-8");

        final Label label = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedLabel.getId(), label.getId());
        assertEquals(expectedLabel.getName(), label.getName());
    }

    @Test
    public void getLabelsTest() throws Exception {
        utils.createDefaultUserLoginLabel();

        final var response = utils.perform(
                        get(LABEL_CONTROLLER_PATH),
                        utils.getUserDto().getEmail()
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        //        response.setContentType("application/json;charset=UTF-8");

        final List<Label> labels = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(labels).hasSize(1);
    }

    @Test
    public void updateLabelTest() throws Exception {
        utils.createDefaultUserLoginLabel();

        Long createdLabelId = labelRepository.findAll().get(0).getId();
        final var response = utils.perform(put(LABEL_CONTROLLER_PATH + ID, createdLabelId)
                                .content(asJson(utils.getSecondLabelDto())).contentType(APPLICATION_JSON),
                        utils.getUserDto().getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        response.setContentType("application/json;charset=UTF-8");

        final Label label = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final List<Label> allLabels = labelRepository.findAll();

        assertThat(allLabels).hasSize(1);
        assertEquals(label.getId(), createdLabelId);
        assertEquals(label.getName(), utils.getSecondLabelDto().getName());
    }

    @Test
    public void deleteLabelTest() throws Exception {
        utils.createDefaultUserLoginLabel();

        Long createdLabelId = labelRepository.findAll().get(0).getId();

        utils.perform(delete(LABEL_CONTROLLER_PATH + ID, createdLabelId),
                        utils.getUserDto().getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Label> allCreatedLabels = labelRepository.findAll();

        assertThat(allCreatedLabels).hasSize(0);
    }
}
