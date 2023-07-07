package hexlet.code.app.controller;

import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.service.LabelService;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static hexlet.code.app.controller.LabelController.LABEL_CONTROLLER_PATH;

import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    public static final String ID = "/{id}";
    private final LabelService labelService;

    @GetMapping(ID)
    public Label getLabel(@PathVariable final Long id) {
        return labelService.getLabel(id);
    }

    @GetMapping
    public List<Label> getLabels() {
        return (List<Label>) labelService.getLabels();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Label createLabel(@RequestBody @Valid final LabelDto dto) {
        return labelService.createLabel(dto);
    }

    @PutMapping(ID)
    public Label updateLabel(@PathVariable final long id, @RequestBody @Valid final LabelDto dto) {
        return labelService.updateLabel(id, dto);
    }

    @DeleteMapping(ID)
    public void deleteLabel(@PathVariable long id) {
        labelService.deleteLabel(id);
    }
}









