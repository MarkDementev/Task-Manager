package hexlet.code.controller;

import com.querydsl.core.types.Predicate;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.service.TaskService;
import hexlet.code.repository.TaskRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;

import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {

    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String ID = "/{id}";
    private static final String ONLY_AUTHOR_BY_ID = """
            @taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
        """;
    private final TaskService taskService;
    private final TaskRepository taskRepository;

    @Operation(summary = "Get task")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Task.class)))
    @GetMapping(ID)
    public Task getTask(@PathVariable final Long id) {
        return taskService.getTask(id);
    }

    @Operation(summary = "Get tasks")
    @ApiResponses(@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Task.class))))
    @GetMapping
    public Iterable<Task> getTasks(@QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskRepository.findAll(predicate);
    }

    @Operation(summary = "Create new task")
    @ApiResponse(responseCode = "201", description = "Task created")
    @PostMapping
    @ResponseStatus(CREATED)
    public Task createTask(@RequestBody @Valid final TaskDto dto) {
        return taskService.createTask(dto);
    }

    @Operation(summary = "Update task")
    @ApiResponse(responseCode = "200", description = "Task updated")
    @PutMapping(ID)
    public Task updateTask(@PathVariable final long id, @RequestBody @Valid final TaskDto dto) {
        return taskService.updateTask(id, dto);
    }

    @Operation(summary = "Delete task")
    @ApiResponse(responseCode = "200", description = "Task deleted")
    @DeleteMapping(ID)
    @PreAuthorize(ONLY_AUTHOR_BY_ID)
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}
