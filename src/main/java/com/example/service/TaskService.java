package com.example.service;

import com.example.annotations.LogAfterReturning;
import com.example.annotations.LogAfterThrowing;
import com.example.annotations.LogAround;
import com.example.annotations.LogBefore;
import com.example.dto.TaskDto;
import com.example.model.Task;
import com.example.repo.TaskRepo;
import com.example.util.TaskMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepo taskRepo;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepo taskRepo, TaskMapper taskMapper) {
        this.taskRepo = taskRepo;
        this.taskMapper = taskMapper;
    }

    @LogBefore
    public List<TaskDto> getAllTasks() {
        return taskRepo.findAll().stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @LogBefore
    @LogAfterThrowing
    public TaskDto getTaskById(Long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        return taskMapper.toDto(task);
    }

    @LogAround
    @LogAfterReturning
    public TaskDto createTask(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task savedTask = taskRepo.save(task);
        return taskMapper.toDto(savedTask);
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
        return taskMapper.toDto(updatedTask);
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
        return taskMapper.toDto(patchedTask);
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
}