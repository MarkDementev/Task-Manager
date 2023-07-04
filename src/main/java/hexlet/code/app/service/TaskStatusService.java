package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.TaskStatus;

public interface TaskStatusService {
    TaskStatus getTaskStatus(long id);
    Iterable<TaskStatus> getTaskStatuses();
    TaskStatus createTaskStatus(TaskStatusDto taskStatusDto);
    TaskStatus updateTaskStatus(long id, TaskStatusDto taskStatusDto);
    void deleteTaskStatus(long id);
}
