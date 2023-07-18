package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.service.LabelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

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

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;

import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    public static final String ID = "/{id}";
    private final LabelService labelService;

    @Operation(summary = "Get label")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Label.class)))
    @GetMapping(ID)
    public Label getLabel(@PathVariable final Long id) {
        return labelService.getLabel(id);
    }

    @Operation(summary = "Get labels")
    @ApiResponses(@ApiResponse(responseCode = "200", content = @Content(
            schema = @Schema(implementation = Label.class))))
    @GetMapping
    public List<Label> getLabels() {
        return labelService.getLabels();
    }

    @Operation(summary = "Create new label")
    @ApiResponse(responseCode = "201", description = "Label created")
    @PostMapping
    @ResponseStatus(CREATED)
    public Label createLabel(@RequestBody @Valid final LabelDto dto) {
        return labelService.createLabel(dto);
    }

    @Operation(summary = "Update label")
    @ApiResponse(responseCode = "200", description = "Label updated")
    @PutMapping(ID)
    public Label updateLabel(@PathVariable final long id, @RequestBody @Valid final LabelDto dto) {
        return labelService.updateLabel(id, dto);
    }

    @Operation(summary = "Delete label")
    @ApiResponse(responseCode = "200", description = "Label deleted")
    @DeleteMapping(ID)
    public void deleteLabel(@PathVariable long id) {
        labelService.deleteLabel(id);
    }
}









