package com.example.controller;

import com.example.dto.TaskDto;
import com.example.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService mainService;

    public TaskController(TaskService mainService) {
        this.mainService = mainService;
    }

    @GetMapping
    public List<TaskDto> getAllTasks() {
        return mainService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return mainService.getTaskById(id);
    }

    @PostMapping
    public TaskDto createTask(@RequestBody TaskDto taskDto) {
        return mainService.createTask(taskDto);
    }

    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return mainService.updateTask(id, taskDto);
    }

    @PatchMapping("/{id}")
    public TaskDto patchTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return mainService.patchTask(id, taskDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        mainService.deleteTask(id);
    }
}