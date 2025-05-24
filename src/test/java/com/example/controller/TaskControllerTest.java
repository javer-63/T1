package com.example.controller;

import com.example.model.Task;
import com.example.model.TaskStatus;
import com.example.repo.TaskRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepo taskRepo;

    @Test
    @DisplayName("Тест успешного получение задачи по ID")
    void getTaskByIdTest() throws Exception {
        saveTestTask();
        mockMvc.perform(get("/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));
        taskRepo.deleteAll();
    }

    @Test
    @DisplayName("Тест не успешного получение задачи по ID")
    void getTaskByExceptionTest() throws Exception {
        mockMvc.perform(get("/tasks/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Тест успешного получение всех задач")
    void getAllTasksTest() throws Exception {
        saveTestTask();
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
        taskRepo.deleteAll();
    }

    @Test
    @DisplayName("Тест успешного создания задачи")
    void createTaskTest() throws Exception {
        String taskJson = """
                {
                    "title": "New Task",
                    "description": "New Description",
                    "userId": 1,
                    "status": "IN_PROGRESS"
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
        taskRepo.deleteAll();
    }

    @Test
    @DisplayName("Тест не успешного создания задачи")
    void createTaskExceptionTest() throws Exception {
        String taskJson = """
                {
                    "title": "New Task",
                    "description": "New Description"
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Тест успешного обновления задачи")
    void updateTaskTest() throws Exception {
        saveTestTask();
        String taskJson = """
                {
                    "title": "Updated Task",
                    "description": "Updated Description",
                    "userId": 1,
                    "status": "COMPLETED"
                }
                """;

        mockMvc.perform(put("/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk());
        taskRepo.deleteAll();
    }

    @Test
    @DisplayName("Тест не успешного обновления задачи")
    void updateTaskExceptionTest() throws Exception {
        String taskJson = """
                {
                    "title": "Updated Task",
                    "description": "Updated Description",
                    "userId": 1,
                    "status": "COMPLETED"
                }
                """;

        mockMvc.perform(put("/tasks/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Тест успешного частичного обновления задачи")
    void patchTaskTest() throws Exception {
        saveTestTask();
        String taskJson = """
                {
                    "title": "Patched Title"
                }
                """;

        mockMvc.perform(patch("/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk());
        taskRepo.deleteAll();
    }

    @Test
    @DisplayName("Тест не успешного частичного обновления задачи")
    void patchTaskExceptionTest() throws Exception {
        String taskJson = """
                {
                    "title": "Patched Title"
                }
                """;

        mockMvc.perform(patch("/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Тест успешного удаления задачи")
    void deleteTaskTest() throws Exception {
        saveTestTask();
        mockMvc.perform(delete("/tasks/{id}", 1L))
                .andExpect(status().isOk());
        taskRepo.deleteAll();
    }

    @Test
    @DisplayName("Тест не успешного удаления задачи")
    void deleteTaskExceptionTest() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    private void saveTestTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task 1");
        task.setDescription("Test Task description 1");
        task.setUserId(1L);
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskRepo.save(task);
    }
}