package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.service.TaskStatusService;

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

import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;

import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {
    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";
    private final TaskStatusService taskStatusService;

    @GetMapping(ID)
    public TaskStatus getTaskStatus(@PathVariable final Long id) {
        return taskStatusService.getTaskStatus(id);
    }

    @GetMapping
    public List<TaskStatus> getTaskStatuses() {
        return (List<TaskStatus>) taskStatusService.getTaskStatuses();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public TaskStatus createTaskStatus(@RequestBody @Valid final TaskStatusDto dto) {
        return taskStatusService.createTaskStatus(dto);
    }

    @PutMapping(ID)
    public TaskStatus updateTaskStatus(@PathVariable final long id, @RequestBody @Valid final TaskStatusDto dto) {
        return taskStatusService.updateTaskStatus(id, dto);
    }

    @DeleteMapping(ID)
    public void deleteTaskStatus(@PathVariable long id) {
        taskStatusService.deleteTaskStatus(id);
    }
}
