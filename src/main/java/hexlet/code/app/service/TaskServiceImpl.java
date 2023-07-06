package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.dto.TaskToUpdateDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.repository.TaskStatusRepository;

import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;

    @Override
    public Task getTask(long id) {
        return taskRepository.findById(id).get();
    }

    @Override
    public Iterable<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task createTask(TaskDto taskDto) {
        final Task newTask = new Task();
        User author = userRepository.findById(taskDto.getAuthorId()).get();
        User executor = userRepository.findById(taskDto.getExecutorId()).get();
        TaskStatus taskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId()).get();

        newTask.setName(taskDto.getName());
        newTask.setDescription(taskDto.getDescription());
        newTask.setAuthor(author);
        newTask.setExecutor(executor);
        newTask.setTaskStatus(taskStatus);

        return taskRepository.save(newTask);
    }

    @Override
    public Task updateTask(long id, TaskToUpdateDto taskDto) {
        final Task taskToUpdate = taskRepository.findById(id).get();
        User executor = userRepository.findById(taskDto.getExecutorId()).get();
        TaskStatus taskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId()).get();

        taskToUpdate.setName(taskDto.getName());
        taskToUpdate.setDescription(taskDto.getDescription());
        taskToUpdate.setExecutor(executor);
        taskToUpdate.setTaskStatus(taskStatus);

        return taskRepository.save(taskToUpdate);
    }

    @Override
    public void deleteTask(long id) {
        final Task taskToDelete = taskRepository.findById(id).get();

        taskRepository.delete(taskToDelete);
    }
}
