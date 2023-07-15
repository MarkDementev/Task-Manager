package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.dto.TaskToUpdateDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.LabelRepository;

import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;

    @Override
    public Task getTask(long id) {
        return taskRepository.findById(id).get();
    }

    @Override
    public Task createTask(TaskDto taskDto) {
        final Task newTask = new Task();
        User author = userRepository.findById(taskDto.getAuthorId()).get();
        User executor = userRepository.findById(taskDto.getExecutorId()).get();
        List<Label> labels = formLabelsList(taskDto.getLabels());
        TaskStatus taskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId()).get();

        newTask.setAuthor(author);
        newTask.setExecutor(executor);
        newTask.setTaskStatus(taskStatus);
        newTask.setLabels(labels);
        newTask.setName(taskDto.getName());
        newTask.setDescription(taskDto.getDescription());

        return taskRepository.save(newTask);
    }

    @Override
    public Task updateTask(long id, TaskToUpdateDto taskDto) {
        final Task taskToUpdate = taskRepository.findById(id).get();
        User executor = userRepository.findById(taskDto.getExecutorId()).get();
        List<Label> labels = formLabelsList(taskDto.getLabelsId());
        TaskStatus taskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId()).get();

        taskToUpdate.setExecutor(executor);
        taskToUpdate.setTaskStatus(taskStatus);
        taskToUpdate.setLabels(labels);
        taskToUpdate.setName(taskDto.getName());
        taskToUpdate.setDescription(taskDto.getDescription());

        return taskRepository.save(taskToUpdate);
    }

    @Override
    public void deleteTask(long id) {
        final Task taskToDelete = taskRepository.findById(id).get();

        taskRepository.delete(taskToDelete);
    }

    private List<Label> formLabelsList(List<Long> labelsId) {
        if (labelsId == null) {
            return null;
        }

        List<Label> outputList = new ArrayList<>();

        for (Long labelId : labelsId) {
            outputList.add(labelRepository.findById(labelId).get());
        }
        return outputList;
    }
}
