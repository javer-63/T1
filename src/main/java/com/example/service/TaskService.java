package com.example.service;

import com.example.annotations.LogAfterReturning;
import com.example.annotations.LogAfterThrowing;
import com.example.annotations.LogAround;
import com.example.annotations.LogBefore;
import com.example.dto.TaskDto;
import com.example.model.Task;
import com.example.repo.TaskRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @LogBefore
    public List<TaskDto> getAllTasks() {
        return taskRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @LogBefore
    @LogAfterThrowing
    public TaskDto getTaskById(Long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        return convertToDto(task);
    }

    @LogAround
    @LogAfterReturning
    public TaskDto createTask(TaskDto taskDto) {
        Task task = convertToEntity(taskDto);
        Task savedTask = taskRepo.save(task);
        return convertToDto(savedTask);
    }

    @LogAfterReturning
    @LogAfterThrowing
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setUserId(taskDto.getUserId());

        Task updatedTask = taskRepo.save(task);
        return convertToDto(updatedTask);
    }

    @LogAfterReturning
    @LogAfterThrowing
    @Transactional
    public TaskDto patchTask(Long id, TaskDto taskDto) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (taskDto.getTitle() != null) {
            task.setTitle(taskDto.getTitle());
        }
        if (taskDto.getDescription() != null) {
            task.setDescription(taskDto.getDescription());
        }
        if (taskDto.getUserId() != null) {
            task.setUserId(taskDto.getUserId());
        }

        Task patchedTask = taskRepo.save(task);
        return convertToDto(patchedTask);
    }

    @LogAround
    @LogAfterThrowing
    public void deleteTask(Long id) {
        if (taskRepo.findById(id).isPresent()) {
            taskRepo.deleteById(id);
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }

    private TaskDto convertToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setUserId(task.getUserId());
        return dto;
    }

    private Task convertToEntity(TaskDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setUserId(dto.getUserId());
        return task;
    }
}