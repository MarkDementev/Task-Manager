package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;

public interface TaskService {
    Task getTask(long id);
    Task createTask(TaskDto taskDto);
    Task updateTask(long id, TaskDto taskDto);
    void deleteTask(long id);
}
