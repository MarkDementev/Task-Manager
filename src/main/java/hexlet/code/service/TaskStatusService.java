package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;

import java.util.List;

public interface TaskStatusService {
    TaskStatus getTaskStatus(long id);
    List<TaskStatus> getTaskStatuses();
    TaskStatus createTaskStatus(TaskStatusDto taskStatusDto);
    TaskStatus updateTaskStatus(long id, TaskStatusDto taskStatusDto);
    void deleteTaskStatus(long id);
}
