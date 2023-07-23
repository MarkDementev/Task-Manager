package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;

import hexlet.code.config.SpringConfigForIT;
import hexlet.code.repository.LabelRepository;
import hexlet.code.utils.TestUtils;
import hexlet.code.model.Label;

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

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;

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

        response.setContentType(utils.getUTFHeader());

        final Label labelFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(labelFromResponse.getName(), utils.getLabelDto().getName());
        assertNotNull(labelFromResponse.getCreatedAt());
    }

    @Test
    public void getLabelTest() throws Exception {
        utils.createDefaultUserLoginLabel();

        final Label expectedLabel = labelRepository.findByName("Новая метка");
        final var response = utils.perform(
                        get(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId()),
                        utils.getUserDto().getEmail()
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        response.setContentType(utils.getUTFHeader());

        final Label labelFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedLabel.getId(), labelFromResponse.getId());
        assertEquals(expectedLabel.getName(), labelFromResponse.getName());
        assertNotNull(labelFromResponse.getCreatedAt());
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
        final List<Label> labelsFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(labelsFromResponse).hasSize(1);
    }

    @Test
    public void updateLabelTest() throws Exception {
        utils.createDefaultUserLoginLabel();

        Long createdLabelId = labelRepository.findByName("Новая метка").getId();
        final var response = utils.perform(put(LABEL_CONTROLLER_PATH + ID, createdLabelId)
                                .content(asJson(utils.getSecondLabelDto())).contentType(APPLICATION_JSON),
                        utils.getUserDto().getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        response.setContentType(utils.getUTFHeader());

        final Label labelFromResponse = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(labelFromResponse.getId(), createdLabelId);
        assertEquals(labelFromResponse.getName(), utils.getSecondLabelDto().getName());
        assertNotNull(labelFromResponse.getCreatedAt());
    }

    @Test
    public void deleteLabelTest() throws Exception {
        utils.createDefaultUserLoginLabel();

        Long createdLabelId = labelRepository.findByName("Новая метка").getId();

        utils.perform(delete(LABEL_CONTROLLER_PATH + ID, createdLabelId),
                        utils.getUserDto().getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertNull(labelRepository.findByName("Новая метка"));
    }
}
