package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.dto.TaskToUpdateDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.service.TaskService;
import hexlet.code.app.repository.TaskRepository;

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

import java.util.List;

import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;

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

    @GetMapping(ID)
    public Task getTask(@PathVariable final Long id) {
        return taskService.getTask(id);
    }

    @GetMapping
    public List<Task> getTasks() {
        return (List<Task>) taskService.getTasks();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Task createTask(@RequestBody @Valid final TaskDto dto) {
        return taskService.createTask(dto);
    }

    @PutMapping(ID)
    public Task updateTask(@PathVariable final long id, @RequestBody @Valid final TaskToUpdateDto dto) {
        return taskService.updateTask(id, dto);
    }

    @DeleteMapping(ID)
    @PreAuthorize(ONLY_AUTHOR_BY_ID)
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}
