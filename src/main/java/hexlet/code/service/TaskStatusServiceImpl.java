package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;

import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus getTaskStatus(long id) {
        return taskStatusRepository.findById(id).get();
    }

    @Override
    public List<TaskStatus> getTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    @Override
    public TaskStatus createTaskStatus(TaskStatusDto taskStatusDto) {
        final TaskStatus newTaskStatus = new TaskStatus();

        newTaskStatus.setName(taskStatusDto.getName());
        return taskStatusRepository.save(newTaskStatus);
    }

    @Override
    public TaskStatus updateTaskStatus(long id, TaskStatusDto taskStatusDto) {
        final TaskStatus taskStatusToUpdate = taskStatusRepository.findById(id).get();

        taskStatusToUpdate.setName(taskStatusDto.getName());
        return taskStatusRepository.save(taskStatusToUpdate);
    }

    @Override
    public void deleteTaskStatus(long id) {
        final TaskStatus taskStatusToDelete = taskStatusRepository.findById(id).get();

        taskStatusRepository.delete(taskStatusToDelete);
    }
}
