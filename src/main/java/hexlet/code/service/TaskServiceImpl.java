package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.LabelRepository;

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
    private final UserService userService;

    @Override
    public Task getTask(long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    @Override
    public Task createTask(TaskDto taskDto) {
        final Task newTask = new Task();
        User author = userRepository.findById(userService.getCurrentUser().getId()).orElseThrow();

        newTask.setAuthor(author);
        return getFromDTOThenSave(newTask, taskDto);
    }

    @Override
    public Task updateTask(long id, TaskDto taskDto) {
        final Task taskToUpdate = taskRepository.findById(id).orElseThrow();

        return getFromDTOThenSave(taskToUpdate, taskDto);
    }

    @Override
    public void deleteTask(long id) {
        taskRepository.deleteById(id);
    }

    private Task getFromDTOThenSave(Task taskToWorkWith, TaskDto taskDto) {
        User executor = userRepository.findById(taskDto.getExecutorId()).orElseThrow();
        TaskStatus taskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId()).orElseThrow();
        List<Label> labels = formLabelsList(taskDto.getLabelIds());

        taskToWorkWith.setExecutor(executor);
        taskToWorkWith.setTaskStatus(taskStatus);
        taskToWorkWith.setLabels(labels);
        taskToWorkWith.setName(taskDto.getName());
        taskToWorkWith.setDescription(taskDto.getDescription());

        return taskRepository.save(taskToWorkWith);
    }

    private List<Label> formLabelsList(List<Long> labelIds) {
        List<Label> outputList = new ArrayList<>();

        if (labelIds == null) {
            return outputList;
        }

        for (Long labelId : labelIds) {
            outputList.add(labelRepository.findById(labelId).orElseThrow());
        }
        return outputList;
    }
}
