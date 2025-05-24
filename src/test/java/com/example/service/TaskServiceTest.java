package com.example.service;

import com.example.dto.TaskDto;
import com.example.kafka.KafkaTaskProducer;
import com.example.model.Task;
import com.example.model.TaskStatus;
import com.example.repo.TaskRepo;
import com.example.util.TaskMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepo taskRepo;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private KafkaTaskProducer kafkaTaskProducer;

    @Test
    @DisplayName("Тест успешного получение всех задач")
    void getAllTasksTest() {
        Task task = getTestTask();
        List<Task> tasks = List.of(task);
        TaskService taskService = new TaskService(taskRepo, taskMapper, kafkaTaskProducer);
        taskRepo.save(task);
        when(taskRepo.findAll()).thenReturn(tasks);
        List result = taskService.getAllTasks();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Тест успешного получение задачи по ID")
    void getTaskByIdTest() {
        Task task = getTestTask();
        TaskDto expectedDto = getTestTaskDto();
        TaskService taskService = new TaskService(taskRepo, taskMapper, kafkaTaskProducer);
        when(taskRepo.findById(any())).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(expectedDto);
        TaskDto taskById = taskService.getTaskById(1L);
        assertNotNull(taskById);
        assertEquals(1L, taskById.getId());
        assertEquals("Test Task", taskById.getTitle());
        assertEquals(TaskStatus.NEW, taskById.getStatus());
    }

    @Test
    @DisplayName("Тест не успешного получение задачи по ID")
    void getTaskByIdExceptionTest() {
        TaskService taskService = new TaskService(taskRepo, taskMapper, kafkaTaskProducer);
        assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    @DisplayName("Тест успешного создания задачи")
    void createTaskTest() {
        TaskDto inputDto = new TaskDto(null, "Test Task", "Test Description", 1L, TaskStatus.NEW);
        Task savedTask = new Task(1L, "Test Task", "Test Description", 1L, TaskStatus.NEW);
        TaskDto expectedDto = new TaskDto(1L, "Test Task", "Test Description", 1L, TaskStatus.NEW);
        TaskService taskService = new TaskService(taskRepo, taskMapper, kafkaTaskProducer);
        when(taskMapper.toEntity(inputDto))
                .thenReturn(new Task(null, "Test Task", "Test Description", 1L, TaskStatus.NEW));
        when(taskRepo.save(any())).thenReturn(savedTask);
        when(taskMapper.toDto(savedTask)).thenReturn(expectedDto);
        TaskDto result = taskService.createTask(inputDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Task", result.getTitle());
        assertEquals(TaskStatus.NEW, result.getStatus());
    }

    @Test
    @DisplayName("Тест не успешного создания задачи")
    void createTaskExceptionTest() {
        TaskService taskService = new TaskService(taskRepo, taskMapper, kafkaTaskProducer);
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(null));
    }

    @Test
    @DisplayName("Тест успешного обновления задачи")
    void updateTaskTest() {
        TaskDto updateDto = new TaskDto(1L, "Updated Task", "Updated Description", 2L, TaskStatus.IN_PROGRESS);
        Task existingTask = getTestTask();
        Task updatedTask = new Task(1L, "Updated Task", "Updated Description", 2L, TaskStatus.IN_PROGRESS);
        TaskDto expectedDto = new TaskDto(1L, "Updated Task", "Updated Description", 2L, TaskStatus.IN_PROGRESS);
        TaskService taskService = new TaskService(taskRepo, taskMapper, kafkaTaskProducer);
        when(taskRepo.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepo.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(expectedDto);
        TaskDto result = taskService.updateTask(1L, updateDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated Task", result.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    @DisplayName("Тест не успешного обновления задачи")
    void updateTaskExceptionTest() {
        TaskService taskService = new TaskService(taskRepo, taskMapper, kafkaTaskProducer);
        assertThrows(RuntimeException.class, () -> taskService.updateTask(1L, new TaskDto(null, "Updated Task", "Updated Description", 2L, TaskStatus.IN_PROGRESS)));
    }

    @Test
    @DisplayName("Тест успешного частичного обновления задачи")
    void patchUpdateTaskTest() {
        TaskDto updateDto = new TaskDto(null, "Updated Task", null, null, null);
        Task existingTask = getTestTask();
        Task partiallyUpdatedTask = new Task(1L, "Updated Task", "Original Description", 2L, TaskStatus.CANCELLED);
        TaskDto expectedDto = new TaskDto(1L, "Updated Task", "Original Description", 2L, TaskStatus.CANCELLED);
        TaskService taskService = new TaskService(taskRepo, taskMapper, kafkaTaskProducer);
        when(taskRepo.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepo.save(any())).thenReturn(partiallyUpdatedTask);
        when(taskMapper.toDto(partiallyUpdatedTask)).thenReturn(expectedDto);
        TaskDto result = taskService.patchTask(1L, updateDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Original Description", result.getDescription());
        assertEquals(TaskStatus.CANCELLED, result.getStatus());
    }

    @Test
    @DisplayName("Тест не успешного частичного обновления задачи")
    void patchUpdateTaskExceptionTest() {
        TaskService taskService = new TaskService(taskRepo, taskMapper, kafkaTaskProducer);
        assertThrows(RuntimeException.class, () -> taskService.patchTask(1L, new TaskDto(null, "Updated Task", "Updated Description", 2L, TaskStatus.IN_PROGRESS)));
    }

    @Test
    @DisplayName("Тест успешного удаления задачи")
    void deleteTaskTest() {
        Task existingTask = getTestTask();
        TaskService taskService = new TaskService(taskRepo, taskMapper, kafkaTaskProducer);
        when(taskRepo.findById(1L))
                .thenReturn(Optional.of(existingTask))
                .thenReturn(Optional.empty());
        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        assertFalse(taskRepo.findById(1L).isPresent());
    }

    @Test
    @DisplayName("Тест не успешного удаления задачи")
    void deleteTaskExceptionTest() {
        TaskService taskService = new TaskService(taskRepo, taskMapper, kafkaTaskProducer);
        assertThrows(RuntimeException.class, () -> taskService.deleteTask(1L));
    }











    private Task getTestTask() {
        Task task = new Task(1L, "Test Task", "Test Description", 1L, TaskStatus.NEW);
        return task;
    }

    private TaskDto getTestTaskDto() {
        TaskDto taskDto = new TaskDto(1L, "Test Task", "Test Description", 1L, TaskStatus.NEW);
        return taskDto;
    }
}