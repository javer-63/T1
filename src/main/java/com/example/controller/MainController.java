package com.example.controller;

import com.example.dto.TaskDto;
import com.example.model.Task;
import com.example.service.MainService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class MainController {
    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return mainService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return mainService.getTaskById(id);
    }

    @PostMapping
    public Task createTask(@RequestBody TaskDto taskDto) {
        return mainService.createTask(taskDto);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return mainService.updateTask(id, taskDto);
    }

    @PatchMapping("/{id}")
    public Task patchTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return mainService.patchTask(id, taskDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        mainService.deleteTask(id);
    }
}