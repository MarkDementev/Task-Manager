package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.dto.TaskToUpdateDto;
import hexlet.code.app.model.Task;

public interface TaskService {
    Task getTask(long id);
    Iterable<Task> getTasks();
    Task createTask(TaskDto taskDto);
    Task updateTask(long id, TaskToUpdateDto taskDto);
    void deleteTask(long id);
}
