package com.example.service;

import com.example.annotations.LogAfterReturning;
import com.example.annotations.LogAfterThrowing;
import com.example.annotations.LogAround;
import com.example.annotations.LogBefore;
import com.example.dto.TaskDto;
import com.example.model.Task;
import com.example.repo.MainRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainService {
    private final MainRepo mainRepo;

    public MainService(MainRepo mainRepo) {
        this.mainRepo = mainRepo;
    }

    @LogBefore
    public List<Task> getAllTasks() {
        return mainRepo.findAll();
    }

    @LogBefore
    @LogAfterThrowing
    public Task getTaskById(Long id) {
        return mainRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @LogAround
    @LogAfterReturning
    public Task createTask(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setUserId(taskDto.getUserId());
        return mainRepo.save(task);
    }

    @LogAfterReturning
    @LogAfterThrowing
    public Task updateTask(Long id, TaskDto taskDto) {
        Task task = getTaskById(id);
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setUserId(taskDto.getUserId());
        return mainRepo.save(task);
    }

    @LogAfterReturning
    @LogAfterThrowing
    @Transactional
    public Task patchTask(Long id, TaskDto taskDto) {
        Task task = mainRepo.findById(id)
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

        return mainRepo.save(task);
    }

    @LogAround
    @LogAfterThrowing
    public void deleteTask(Long id) {
        if (mainRepo.findById(id).isPresent()) {
            mainRepo.deleteById(id);
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }
}